package pl.edu.agh.ed.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionsTranslator {
	private static final String INPUT_PATH = "../../../T40I10D10K.transactions";

	private static final String OUTPUT_PATH = "data/T40I10D10K.txt";

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
		Map<String, List<Integer>> cache = new HashMap<>();
		for (String line : lines) {
			String transactionId = line.split(" ")[0];
			String itemId = line.split(" ")[1];
			if (!cache.containsKey(transactionId)) {
				cache.put(transactionId, new ArrayList<>());
			}
			cache.get(transactionId).add(Integer.parseInt(itemId));
		}
		Files.write(Paths.get(OUTPUT_PATH),
				cache.values().stream()
						.map(transaction -> transaction.stream().map(Object::toString).reduce((s1, s2) -> s1 + " " + s2)
								.orElse(""))
						.collect(Collectors.toList()),
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
}
