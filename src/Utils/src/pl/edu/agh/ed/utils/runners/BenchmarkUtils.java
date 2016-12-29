package pl.edu.agh.ed.utils.runners;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Collectors;

import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;

public class BenchmarkUtils {
	public static void writeResultToFile(IFrequentPatternSet<IItem> result, Path output) throws IOException {
		Files.write(output, result.getFrequentPatterns().stream().map(pattern -> {
			pattern.getItems().sort(Comparator.comparingInt(IItem::getId));
			return pattern;
		}).sorted((p1, p2) -> { // ujemna -> mniejsza
			if (p2.getSupport() - p1.getSupport() != 0) {
				return (int) (p2.getSupport() - p1.getSupport());
			}
			if (p2.size() - p1.size() != 0) {
				return p2.size() - p1.size();
			}
			for (int i = 0; i < p1.size(); i++) {
				int diff = p2.getItems().get(i).getId() - p1.getItems().get(i).getId();
				if (diff != 0) {
					return diff;
				}
			}
			return 0;
		}).map(BenchmarkUtils::writeFrequentPattern).collect(Collectors.toList()), StandardOpenOption.CREATE, //
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static String writeFrequentPattern(IFrequentPattern<IItem> frequentPattern) {
		return frequentPattern.getSupport() + "\t\t" + frequentPattern.getItems().stream().map(Object::toString)
				.reduce((s1, s2) -> s1 + " " + s2).orElse("");
	}

	public static void writeResultToFile(IFrequentSequenceSet result, Path output) throws IOException {
		Files.write(output, result.getFrequentSequences().stream().map(pattern -> {
			pattern.getGroups().sort(Comparator.comparingInt(IGroup::getGroupId));
			return pattern;
		}).sorted((p1, p2) -> { // ujemna -> mniejsza
			if (p2.getSupport() - p1.getSupport() != 0) {
				return (int) (p2.getSupport() - p1.getSupport());
			}
			if (p2.getGroups().size() - p1.getGroups().size() != 0) {
				return p2.getGroups().size() - p1.getGroups().size();
			}
			for (int i = 0; i < p1.getGroups().size(); i++) {
				int diff = p2.getGroups().get(i).getGroupId() - p1.getGroups().get(i).getGroupId();
				if (diff != 0) {
					return diff;
				}
			}
			return 0;
		}).map(BenchmarkUtils::writeFrequentSequence).collect(Collectors.toList()), StandardOpenOption.CREATE, //
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static String writeFrequentSequence(IFrequentSequence frequentSequence) {
		return frequentSequence.getSupport() + "\t\t" + frequentSequence.getGroups().stream().map(Object::toString)
				.reduce((s1, s2) -> s1 + " -> " + s2).orElse("");
	}

	public static void print(Object string, PrintStream... printStreams) {
		System.out.print(string + " ");
		for (PrintStream printStream : printStreams) {
			printStream.print(string);
		}
	}

	public static void println(Object string, PrintStream... printStreams) {
		System.out.println(string + " ");
		for (PrintStream printStream : printStreams) {
			printStream.println(string);
		}
	}

	public static void println(PrintStream... printStreams) {
		System.out.println();
		for (PrintStream printStream : printStreams) {
			printStream.println();
		}
	}
}
