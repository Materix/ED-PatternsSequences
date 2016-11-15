package pl.edu.agh.ed.algorithm.apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AprioriFrequentPatternsExtractor<T extends IItem> implements IFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
        Set<IFrequentPattern<T>> candidatePatterns = gen1ItemPatterns(transactionSet, minSupport);
        Set<IFrequentPattern<T>> patterns = new HashSet<>(candidatePatterns);
        int tier = 2;
        while (candidatePatterns.size() >= 2) {
        	candidatePatterns = genApriori(candidatePatterns, tier, minSupport);
        	patterns.addAll(candidatePatterns);
            tier++;
        }
        return new FrequentPatternSet<>(transactionSet, patterns);
    }
	
    private Set<IFrequentPattern<T>> gen1ItemPatterns(ITransactionSet<T> transactionSet, int minSupport) {
    	return transactionSet.stream()
        	.map(ITransaction::getItems)
        	.flatMap(List::stream)
        	.distinct()
        	.map(item -> new FrequentPattern<>(transactionSet, Collections.singletonList(item)))
        	.filter(pattern -> pattern.getSupport() >= minSupport)
        	.collect(Collectors.toSet());
    }

    private Set<IFrequentPattern<T>> genApriori(Set<IFrequentPattern<T>> candidatePatterns, int tier, int minSupport) {
        List<IFrequentPattern<T>> list = new ArrayList<>(candidatePatterns);
        Set<IFrequentPattern<T>> result = new HashSet<>();
                
        for (int i = 0; i < list.size() - 1; i++) {
        	for (int j = i + 1; j < list.size(); j++) {
                IFrequentPattern<T> firstPattern = list.get(i);
                IFrequentPattern<T> secondPattern = list.get(j);
                if (canCombine(firstPattern, secondPattern)) {
                	FrequentPattern<T> pattern = mergePatterns(firstPattern, secondPattern);
                	if (pattern.getSupport() >= minSupport && pattern.size() == tier) {
                		result.add(pattern);
                	}
                }
        	}
        }
        return result;
    }
    
    public FrequentPattern<T> mergePatterns(IFrequentPattern<T> firstPattern, IFrequentPattern<T> secondPattern) {
    	List<T> temp = new ArrayList<>(secondPattern.getItems());
    	List<T> intersection = new ArrayList<>();
    	for (T item: firstPattern.getItems()) {
    		if (temp.contains(item)) {
    			temp.remove(item);
    			intersection.add(item);
    		}
    	}
    	
    	List<T> first = new ArrayList<>(firstPattern.getItems());
    	List<T> second = new ArrayList<>(secondPattern.getItems());
    	first.removeAll(intersection);
    	second.removeAll(intersection);
    	intersection.add(first.get(0));
    	intersection.add(second.get(0));
        return new FrequentPattern<>(firstPattern.getTransactionSet(), intersection);
    }
    
    private boolean canCombine(IFrequentPattern<T> p, IFrequentPattern<T> q) {
        return p.size() == q.size() && checkEqualsWithoutOneElement(p, q);
    }

    private boolean checkEqualsWithoutOneElement(IFrequentPattern<T> firstPattern, IFrequentPattern<T> secondPattern) {
    	List<T> temp = new ArrayList<>(secondPattern.getItems());
    	List<T> intersection = new ArrayList<>();
    	for (T item: firstPattern.getItems()) {
    		if (temp.contains(item)) {
    			temp.remove(item);
    			intersection.add(item);
    		}
    	}
    	
    	List<T> first = new ArrayList<>(firstPattern.getItems());
    	List<T> second = new ArrayList<>(secondPattern.getItems());
    	first.removeAll(intersection);
    	second.removeAll(intersection);
        return first.size() == 1 && second.size() == 1 && !first.get(0).equals(second.get(0));
    }
}
