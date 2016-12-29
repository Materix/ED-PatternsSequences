package pl.edu.agh.ed.model.frequent.sequence;

import java.util.List;

import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class FrequentSequence implements IFrequentSequence {
	private final ISequenceSet sequenceSet;

	private final List<IGroup> groups;

	private final long support;

	public FrequentSequence(ISequenceSet sequenceSet, List<IGroup> groups) {
		this(sequenceSet, groups, sequenceSet.stream().filter(transaction -> transaction.contains(groups)).count());
	}

	public FrequentSequence(ISequenceSet sequenceSet, List<IGroup> groups, long support) {
		this.sequenceSet = sequenceSet;
		this.groups = groups;
		this.support = support;
	}

	@Override
	public ISequenceSet getSequenceSet() {
		return sequenceSet;
	}

	@Override
	public List<IGroup> getGroups() {
		return groups;
		// return Collections.unmodifiableList(groups);
	}

	@Override
	public long getSupport() {
		return support;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sequenceSet == null) ? 0 : sequenceSet.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.size());
		if (groups != null) {
			result = prime * result + groups.stream().mapToInt(Object::hashCode).sum();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FrequentSequence other = (FrequentSequence) obj;
		if (groups == null) {
			if (other.groups != null) {
				return false;
			} else {
				return true;
			}
		}
		if (sequenceSet == null) {
			if (other.sequenceSet != null) {
				return false;
			}
		} else if (!sequenceSet.equals(other.sequenceSet)) {
			return false;
		} else if (groups.size() != other.groups.size()) {
			return false;
		}
		return groups.containsAll(other.groups);
	}

	@Override
	public String toString() {
		return groups.stream().map(Object::toString).map(s -> "{" + s + "}").reduce((s1, s2) -> s1 + " " + s2)
				.orElse("");
	}
}
