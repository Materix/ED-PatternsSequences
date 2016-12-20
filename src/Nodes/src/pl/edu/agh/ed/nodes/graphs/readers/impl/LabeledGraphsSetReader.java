package pl.edu.agh.ed.nodes.graphs.readers.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.Set;
import java.util.stream.IntStream;

import pl.edu.agh.ed.model.graph.EdgeDirection;
import pl.edu.agh.ed.model.graph.ILabel;
import pl.edu.agh.ed.model.graph.ILabeledEdge;
import pl.edu.agh.ed.model.graph.ILabeledGraph;
import pl.edu.agh.ed.model.graph.ILabeledGraphsSet;
import pl.edu.agh.ed.model.graph.ILabeledVertex;
import pl.edu.agh.ed.model.graph.Label;
import pl.edu.agh.ed.model.graph.LabeledEdge;
import pl.edu.agh.ed.model.graph.LabeledGraph.LabeledGraphBuilder;
import pl.edu.agh.ed.model.graph.LabeledGraphsSet;
import pl.edu.agh.ed.model.graph.LabeledVertex;
import pl.edu.agh.ed.nodes.graphs.readers.ILabeledGraphsSetReader;

public class LabeledGraphsSetReader implements ILabeledGraphsSetReader {
	private static final String DEFAULT_SEPARATOR = " ";

	private static final String DEFAULT_VERTEX_INDICATOR = "v";

	private static final String DEFAULT_EDGE_INDICATOR = "e";

	private static final String DEFAULT_TRANSACTION_INDICATOR = "t";

	private final String separator;

	private final boolean labeledEdge;

	private final String edgeIndicator;

	private final String vertexIndicator;

	private final String transactionIndicator;

	public LabeledGraphsSetReader() {
		this(false, DEFAULT_EDGE_INDICATOR, DEFAULT_VERTEX_INDICATOR, DEFAULT_TRANSACTION_INDICATOR);
	}

	public LabeledGraphsSetReader(boolean labeledEdge, String edgeIndicator, String vertexIndicator,
			String transactionIndicator) {
		this(DEFAULT_SEPARATOR, labeledEdge, edgeIndicator, vertexIndicator, transactionIndicator);
	}

	public LabeledGraphsSetReader(String separator, boolean labeledEdge, String edgeIndicator, String vertexIndicator,
			String transactionIndicator) {
		this.separator = separator;
		this.labeledEdge = labeledEdge;
		this.edgeIndicator = edgeIndicator;
		this.vertexIndicator = vertexIndicator;
		this.transactionIndicator = transactionIndicator;
	}

	/**
	 * Format of line: Node A name;Node B name;Label
	 */
	@Override
	public ILabeledGraphsSet readLabeledGraphsSet(List<String> lines) {
		Map<String, ILabeledVertex> verticesCache = new HashMap<>();
		Map<String, ILabel> labelsCache = new HashMap<>();
		Set<ILabeledEdge> edges = new HashSet<>();
		Set<ILabeledGraph> graphs = new HashSet<>();
		Iterator<String> labelGenerator = IntStream //
				.iterate(0, i -> i + 1) //
				.mapToObj(i -> Integer.toString(i)) //
				.iterator();
		OfInt idGenerator = IntStream //
				.iterate(0, i -> i + 1) //
				.iterator();
		LabeledGraphBuilder builder = new LabeledGraphBuilder();
		for (String line : lines) {
			String[] splitted = line.split(separator);
			String type = splitted[0];
			if (type.equals(transactionIndicator)) {
				if (!builder.isEmpty()) {
					graphs.add(builder.build(idGenerator.nextInt()));
				}
			} else if (type.equals(vertexIndicator)) {
				String vertexName = splitted[1];
				String labelName = splitted[2];
				if (!verticesCache.containsKey(vertexName)) {
					if (!labelsCache.containsKey(labelName)) {
						labelsCache.put(labelName, new Label(labelName));
					}
					ILabel label = labelsCache.get(labelName);
					builder.add(label);
					ILabeledVertex vertex = new LabeledVertex(label);
					builder.add(vertex);
					verticesCache.put(vertexName, vertex);
				}
			} else if (type.equals(edgeIndicator)) {
				String startVertexName = splitted[1];
				String endVertexName = splitted[2];
				String labelName = labeledEdge ? splitted[3] : labelGenerator.next();
				if (!labelsCache.containsKey(labelName)) {
					labelsCache.put(labelName, new Label(labelName));
				}
				ILabel edgeLabel = labelsCache.get(labelName);
				builder.add(edgeLabel);
				ILabeledVertex start = verticesCache.get(startVertexName);
				ILabeledVertex end = verticesCache.get(endVertexName);
				ILabeledEdge edge = new LabeledEdge(edgeLabel, EdgeDirection.UNDIRECTED, start, end);
				start.addEdge(edge);
				end.addEdge(edge);
				builder.add(edge);
			}
		}
		if (!builder.isEmpty()) {
			graphs.add(builder.build(idGenerator.nextInt()));
		}
		return new LabeledGraphsSet(graphs);
		// return new LabeledGraph(new HashSet<>(verticesCache.values()), edges,
		// labels);
	}

	/*
	 * t # 1 v 10 a v 20 b v 30 a v 40 b e 10 20 _ e 10 30 _ e 20 30 _ e 30 40 _
	 * t # 2 v 50 b v 60 a v 70 b v 80 a e 50 60 _ e 50 70 _ e 60 70 _ e 60 80 _
	 * e 70 80 _
	 */

}
