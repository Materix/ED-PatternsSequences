package pl.edu.agh.ed.algorithm.frequent.sequences.spmf.spade;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPADE;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_StandardMap;
import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class SPADEFrequentSequenceExtractor implements IFrequentSequencesExtractor {
	private static final String SEPARATOR = "#SUP:";
	
	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		return extract(sequenceSet, ((double)minSupport) / sequenceSet.getSequences().size());
	}
	
	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, double minRelativeSupport) {
		Path tempOutputFile = null;Path tempInputFile = null;
		try {
			tempInputFile = Files.createTempFile("spmf-input", ".text");
			List<String> lines = sequenceSet.stream()
				.map(sequence -> 
					sequence.getGroups().stream()
						.map(IGroup::getItemsIds)
						.map(ids -> ids.stream().map(Object::toString).reduce((s1, s2) -> s1 + " " + s2).orElse(""))
						.reduce((s1, s2) -> s1 + " -1 " + s2)
						.map(s -> s + " -1 -2")
						.orElse("")
				).collect(Collectors.toList());
			Files.write(tempInputFile, lines);
			tempOutputFile = Files.createTempFile("spmf-output", ".text");
	        AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
	        
	        IdListCreator idListCreator =IdListCreator_StandardMap.getInstance();
	        CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative.getInstance();
	        SequenceDatabase sequenceDatabase = new SequenceDatabase(abstractionCreator, idListCreator);	        
			sequenceDatabase.loadFile(tempInputFile.toString(), minRelativeSupport);

			AlgoSPADE algoSPADE = new AlgoSPADE(minRelativeSupport, false, null);
			algoSPADE.runAlgorithm(sequenceDatabase, candidateGenerator, true, false, tempOutputFile.toString(), false);
			
			return new FrequentSequenceSet(sequenceSet, 
			Files.readAllLines(tempOutputFile)
				.stream()
				.map(line -> (IFrequentSequence)new FrequentSequence(sequenceSet, 
						getGroups(line, sequenceSet), 
						getSupport(line)))
				.collect(Collectors.toSet())
			);	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tempOutputFile != null) {
				try {
					Files.delete(tempOutputFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new FrequentSequenceSet(sequenceSet);
	}
	
	private List<IGroup> getGroups(String line, ISequenceSet sequenceSet) {
		return Arrays.stream(line.split(SEPARATOR)[0].trim().split("-1"))
				.map(String::trim)
				.filter(s -> s.length() > 0)
				.map(group -> new Group(0, Arrays.stream(group.split(" "))
						.map(Integer::parseInt)
						.map(sequenceSet::getItem)
						.collect(Collectors.toList()))
				).collect(Collectors.toList());
	}
	
	private int getSupport(String line) {
		return Integer.parseInt(line.split(SEPARATOR)[1].trim());
	}
}
