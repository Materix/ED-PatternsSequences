package pl.edu.agh.ed.algorithm.frequent.graphs.parsemis.gspan;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.parsemis.Miner;
import de.parsemis.algorithms.gSpan.Algorithm;
import de.parsemis.graph.Edge;
import de.parsemis.graph.Graph;
import de.parsemis.graph.HPListGraph;
import de.parsemis.graph.HPMutableGraph;
import de.parsemis.miner.environment.Settings;
import de.parsemis.miner.general.Fragment;
import de.parsemis.miner.general.IntFrequency;
import de.parsemis.parsers.LabelParser;
import de.parsemis.strategy.BFSStrategy;
import pl.edu.agh.ed.algorithm.IFrequentSubgraphExtractor;
import pl.edu.agh.ed.model.graph.ILabeledEdge;
import pl.edu.agh.ed.model.graph.ILabeledGraph;
import pl.edu.agh.ed.model.graph.ILabeledGraphsSet;
import pl.edu.agh.ed.model.graph.ILabeledSubgraphsSet;
import pl.edu.agh.ed.model.graph.ILabeledVertex;

public class GSpanFrequentSubgraphExtractor implements IFrequentSubgraphExtractor {

	@Override
	public ILabeledSubgraphsSet extract(ILabeledGraphsSet graphsSet, int minSupport) {

		Settings<ILabeledVertex, ILabeledEdge> settings = new Settings<>();
		settings.minFreq = new IntFrequency(minSupport);
		settings.pathsOnly = false;
		settings.algorithm = new Algorithm<>();
		settings.closeGraph = false;
		settings.strategy = new BFSStrategy<>();
		settings.factory = new HPListGraph.Factory<>(new VertexLabelParsed(), new EdgeLabelParsed());
		settings.graphs = graphsSet.getTransactionGraphs().stream() //
				.map(this::createGraph) //
				.collect(Collectors.toList());
		Collection<Fragment<ILabeledVertex, ILabeledEdge>> fragments;
		try {
			fragments = Miner.mine(settings.graphs, settings);
			for (Fragment<ILabeledVertex, ILabeledEdge> fragment : fragments) {
				System.out.println(fragment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// int size = fragments.size();
		// for (Iterator it = fragments.iterator(); it.hasNext();) {
		// final Fragment frag = (Fragment) it.next();
		// if (frag.toGraph().getNodeCount() == 1) {
		// size--;
		// }
		// }
		//
		// ff = new Fragment[size];
		// ffHP = new ArrayList<HPGraph>();
		//
		// int i = -1;
		// for (Iterator it = fragments.iterator(); it.hasNext();) {
		// final Fragment frag = (Fragment) it.next();
		// if (frag.toGraph().getNodeCount() > 1) {
		// ff[++i] = frag;
		// ffHP.add(frag.toGraph().toHPGraph());
		//
		// }
		// }

		return null;
	}

	private Graph<ILabeledVertex, ILabeledEdge> createGraph(ILabeledGraph labeledGraph) {
		HPMutableGraph<ILabeledVertex, ILabeledEdge> graphBuilder = new HPListGraph<>();
		Map<ILabeledVertex, Integer> verticesIndices = new HashMap<>();
		Map<ILabeledEdge, Integer> edgesIndices = new HashMap<>();
		for (ILabeledVertex vertex : labeledGraph.getVertices()) {
			verticesIndices.put(vertex, graphBuilder.addNodeIndex(vertex));
		}
		for (ILabeledEdge edge : labeledGraph.getEdges()) {
			edgesIndices.put(edge, graphBuilder.addEdgeIndex(verticesIndices.get(edge.getStart()),
					verticesIndices.get(edge.getEnd()), edge, edge.isDirected() ? Edge.OUTGOING : Edge.UNDIRECTED));
		}
		return graphBuilder.toGraph();
	}

	class VertexLabelParsed implements LabelParser<ILabeledVertex> {

		@Override
		public ILabeledVertex parse(String text) throws ParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String serialize(ILabeledVertex label) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class EdgeLabelParsed implements LabelParser<ILabeledEdge> {

		@Override
		public ILabeledEdge parse(String text) throws ParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String serialize(ILabeledEdge label) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
