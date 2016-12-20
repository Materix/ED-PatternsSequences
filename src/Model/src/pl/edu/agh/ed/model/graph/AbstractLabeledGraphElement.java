package pl.edu.agh.ed.model.graph;

abstract class AbstractLabeledGraphElement implements ILabeledGraphElement {
	private final ILabel label;

	AbstractLabeledGraphElement(ILabel label) {
		this.label = label;
	}

	@Override
	public ILabel getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		AbstractLabeledGraphElement other = (AbstractLabeledGraphElement) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

}
