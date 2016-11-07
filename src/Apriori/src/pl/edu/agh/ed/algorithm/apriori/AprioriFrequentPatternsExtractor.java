package pl.edu.agh.ed.algorithm.apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.FrequentPattern;
import pl.edu.agh.ed.model.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.patterns.IFrequentPattern;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AprioriFrequentPatternsExtractor implements IFrequentPatternsExtractor {

	@Override
	public IFrequentPatternSet extract(ITransactionSet transactionSet, int minSupport) {
        Set<IFrequentPattern> candidatePatterns = gen1ItemPatterns(transactionSet, minSupport);
        Set<IFrequentPattern> patterns = new HashSet<>(candidatePatterns);
        int tier = 2;
        while (candidatePatterns.size() >= 2) {
        	candidatePatterns = genApriori(candidatePatterns, tier, minSupport);
        	patterns.addAll(candidatePatterns);
            tier++;
        }
        return new FrequentPatternSet(transactionSet, patterns);
    }
	
    private Set<IFrequentPattern> gen1ItemPatterns(ITransactionSet transactionSet, int minSupport) {
    	return transactionSet.stream()
        	.map(ITransaction::getItems)
        	.flatMap(Set::stream)
        	.distinct()
        	.map(item -> new FrequentPattern(transactionSet, Collections.singleton(item)))
        	.filter(pattern -> pattern.getSupport() >= minSupport)
        	.collect(Collectors.toSet());
    }

    private Set<IFrequentPattern> genApriori(Set<IFrequentPattern> candidatePatterns, int tier, int minSupport) {
        List<IFrequentPattern> list = new ArrayList<>(candidatePatterns);
        Set<IFrequentPattern> result = new HashSet<>();
                
        for (int i = 0; i < list.size() - 1; i++) {
        	for (int j = i + 1; j < list.size(); j++) {
                IFrequentPattern firstPattern = list.get(i);
                IFrequentPattern secondPattern = list.get(j);
                if (canCombine(firstPattern, secondPattern)) {
                	FrequentPattern pattern = mergePatterns(firstPattern, secondPattern);
                	if (pattern.getSupport() >= minSupport && pattern.size() == tier) {
                		result.add(pattern);
                	}
                }
        	}
        }
        return result;
    }
    
    public FrequentPattern mergePatterns(IFrequentPattern firstPattern, IFrequentPattern secondPattern) {
    	Set<IItem> newItemSet = new HashSet<>(firstPattern.getItems());
    	newItemSet.addAll(secondPattern.getItems());
        return new FrequentPattern(firstPattern.getTransactionSet(), newItemSet);
    }
    
    private boolean canCombine(IFrequentPattern p, IFrequentPattern q) {
        return p.size() == q.size() && checkEqualsWithoutOneElement(p, q);
    }

    private boolean checkEqualsWithoutOneElement(IFrequentPattern firstPattern, IFrequentPattern secondPattern) {
    	Set<IItem> intersection = new HashSet<>(firstPattern.getItems());
    	intersection.retainAll(secondPattern.getItems());
    	
    	Set<IItem> first = new HashSet<>(firstPattern.getItems());
    	Set<IItem> second = new HashSet<>(secondPattern.getItems());
    	first.removeAll(intersection);
    	second.removeAll(intersection);
        return first.size() == 1 && second.size() == 1;
    }
}
