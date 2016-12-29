package pl.edu.agh.ed.utils.runners;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RunnerParametersDB {
	private static final Map<String, RunnerParameters> DB;

	static {
		DB = new HashMap<String, RunnerParameters>();

		DB.put("kddcup99", RunnerParameters.of(Paths.get("data", "kddcup99.txt"), 0.8, 0.5, 0.025, 5));
		DB.put("mushroom", RunnerParameters.of(Paths.get("data", "mushroom.txt"), 0.5, 0.1, 0.05, 5));
		DB.put("onlineRetail", RunnerParameters.of(Paths.get("data", "onlineRetail.txt"), 0.01, 0.001, 0.001, 5));
		// DB.put("BMS_WebView", RunnerParameters.of(Paths.get("data",
		// "BMS_WebView.txt"), 0.001, 0.0001, 0.0001, 5));

		DB.put("leviathan", RunnerParameters.of(Paths.get("data", "leviathan.txt"), 0.2, 0.025, 0.025, 5));
		DB.put("bible", RunnerParameters.of(Paths.get("data", "bible.txt"), 0.2, 0.025, 0.025, 5));
		DB.put("kosarakSeq", RunnerParameters.of(Paths.get("data", "kosarakSeq.txt"), 0.01, 0.0011, 0.001, 5));
	}

	public static RunnerParameters getParameters(String key) {
		return DB.get(key);
	}
}
