package pl.edu.agh.ed.algorithm.frequent.patterns.spmf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public abstract class AbstractSPMFFileBasedFrequentPatternsExtractor<T extends IItem> implements IFrequentPatternsExtractor<T> {
	private static final String SEPARATOR = "#SUP:";

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		Path tempInputFile = null;
		Path tempOutputFile = null;
		try {
			tempInputFile = Files.createTempFile("spmf-input", ".text");
			List<String> lines = transactionSet.stream()
				.map(transaction -> 
					transaction.getItems().stream()
						.map(IItem::getId)
						.map(id -> id.toString())
						.reduce((s1, s2) -> s1 + " " + s2)
						.orElse("")
				).collect(Collectors.toList());
			Files.write(tempInputFile, lines);
			tempOutputFile = Files.createTempFile("spmf-output", ".text");
			extract(tempInputFile.toString(), tempOutputFile.toString(), ((double)minSupport) / transactionSet.size());
			return new FrequentPatternSet<>(transactionSet, 
				Files.readAllLines(tempOutputFile)
					.stream()
					.map(line -> new FrequentPattern<>(transactionSet, getItems(line, transactionSet), getSupport(line)))
					.collect(Collectors.toSet())
			);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tempInputFile != null) {
				try {
					Files.delete(tempInputFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (tempOutputFile != null) {
				try {
					Files.delete(tempOutputFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new FrequentPatternSet<>(transactionSet);
	}
	
	private List<T> getItems(String line, ITransactionSet<T> transactionSet) {
		return Arrays.stream(line.split(SEPARATOR)[0].trim().split(" "))
				.map(Integer::parseInt)
				.map(transactionSet::getItem)
				.collect(Collectors.toList());
	}
	
	private int getSupport(String line) {
		return Integer.parseInt(line.split(SEPARATOR)[1].trim());
	}
	
	public abstract void extract(String input, String output, double minRelativeSupport) throws IOException;
}
