package pl.edu.agh.ed.utils.runners;

import java.nio.file.Path;

public class RunnerParameters {
	private final Path dataSetPath;

	private final double maxSupport;

	private final double minSupport;

	private final double supportStep;

	private final int maxRuns;

	private RunnerParameters(Path dataSetPath, double maxSupport, double minSupport, double supportStep, int maxRuns) {
		this.dataSetPath = dataSetPath;
		this.maxSupport = maxSupport;
		this.minSupport = minSupport;
		this.supportStep = supportStep;
		this.maxRuns = maxRuns;
	}

	public Path getDataSetPath() {
		return dataSetPath;
	}

	public double getMaxSupport() {
		return maxSupport;
	}

	public double getMinSupport() {
		return minSupport;
	}

	public double getSupportStep() {
		return supportStep;
	}

	public int getMaxRuns() {
		return maxRuns;
	}

	public static RunnerParameters of(Path dataSetPath, double maxSupport, double minSupport, double supportStep,
			int maxRuns) {
		return new RunnerParameters(dataSetPath, maxSupport, minSupport, supportStep, maxRuns);
	}
}
