package fi.pss.cleanbeach.data;

import javax.persistence.Entity;

@Entity
public class ThrashType extends AbstractEntity {

	private String name;

	private boolean other;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}
}
