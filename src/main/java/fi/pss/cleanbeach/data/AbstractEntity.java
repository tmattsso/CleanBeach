package fi.pss.cleanbeach.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity other = (AbstractEntity) obj;

		return other.id == id;
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.intValue();
		}
		return super.hashCode();
	}
}
