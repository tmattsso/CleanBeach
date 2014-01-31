package fi.pss.cleanbeach.data;

import javax.persistence.Entity;

@Entity
public class ThrashType extends AbstractEntity {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
