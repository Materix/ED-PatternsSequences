package pl.edu.agh.ed.algorithm.frequent.sequences.spmf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.AlgoGSP;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.Item;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.Sequence;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.Sequences;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.abstractions.Abstraction_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.abstractions.ItemAbstractionPair;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.creators.ItemAbstractionPairCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.patterns.Pattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.gsp_AGP.items.patterns.PatternCreator;
import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequence;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class GSPFrequentSequenceExtractor implements IFrequentSequencesExtractor {

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		return extract(sequenceSet, ((double)minSupport) / sequenceSet.getSequences().size());
	}
	
	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, double minRelativeSupport) {
		ItemAbstractionPairCreator pairCreator = ItemAbstractionPairCreator.getInstance();
		PatternCreator patternCreator = PatternCreator.getInstance();
		AbstractionCreator creator = AbstractionCreator_Qualitative.getInstance();
        SequenceDatabase sequenceDatabase = new SequenceDatabase(creator);
        for (ISequence sequence: sequenceSet.getSequences()) {
        	Sequence spmfSequence = new Sequence(sequence.getId());
        	sequence.getGroups().stream().map(group -> {
        		Itemset itemset = new Itemset();
        		group.getItems().stream()
        			.map(item -> {
        				Item<Integer> spmfItem = new Item<>(item.getId());
        				Pattern pattern = sequenceDatabase.getFrequentItems().get(spmfItem);
        				if (pattern == null) {
        					pattern = patternCreator.createPattern(pairCreator.getItemAbstractionPair(spmfItem, creator.CreateDefaultAbstraction()));       					
        					sequenceDatabase.getFrequentItems().put(spmfItem, pattern);
        				}
        				pattern.addAppearance(sequence.getId());
						return spmfItem;
        			})
        			.forEach(itemset::addItem);
        		return itemset;
        	}).forEach(spmfSequence::addItemset);
        	sequenceDatabase.addSequence(spmfSequence);
        }
        
		AlgoGSP algoGSP = new AlgoGSP(minRelativeSupport, 0, Integer.MAX_VALUE, 0, creator);
		try {
			Sequences sequences = algoGSP.runAlgorithm(sequenceDatabase, true, false, null, false); // Last param if we want to know sequence id where given pattern is found
			return new FrequentSequenceSet(sequenceSet, sequences.getLevels().stream()
				.flatMap(List::stream)
				.map(pattern -> {
					return (IFrequentSequence)new FrequentSequence(sequenceSet,
							createGroups(sequenceSet, pattern),
							pattern.getAppearingIn().cardinality());
				}).collect(Collectors.toSet())
			);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	};
	
	private List<IGroup> createGroups(ISequenceSet sequenceSet, Pattern pattern) {
		List<IGroup> result = new ArrayList<>();
		List<IItem> group = new ArrayList<>();
		for (ItemAbstractionPair element: pattern.getElements()) {
			if (!((Abstraction_Qualitative)element.getAbstraction()).hasEqualRelation()) {
				result.add(new Group(0, group));
				group = new ArrayList<>();
			}
			group.add(sequenceSet.getItem((int) element.getItem().getId()));
		}
		result.add(new Group(0, group));
		return result.stream().filter(g -> g.getItems().size() != 0).collect(Collectors.toList());
	}
}
