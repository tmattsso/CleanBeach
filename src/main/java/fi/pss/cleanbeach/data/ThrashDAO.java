package fi.pss.cleanbeach.data;

import java.util.ArrayList;
import java.util.List;

public class ThrashDAO {

	private final List<Thrash> list;

	public ThrashDAO(List<Thrash> list) {
		this.list = list;
	}

	public int getTotalNum() {
		int num = 0;

		for (Thrash t : list) {
			num += t.getNum();
		}

		return num;
	}

	public int getOfType(ThrashType type) {
		int num = 0;

		for (Thrash t : list) {
			if (t.getType().equals(type)) {
				num += t.getNum();
			}
		}

		return num;
	}

	public int getOfTypeForUser(ThrashType type, User u) {
		int num = 0;

		for (Thrash t : list) {
			if (t.getType().equals(type) && t.getReporter().equals(u)) {
				num += t.getNum();
			}
		}

		return num;
	}

	public String getDesc(ThrashType otherType) {
		for (Thrash t : list) {
			if (t.getType().equals(otherType)) {
				return t.getDescription();
			}
		}
		return null;
	}

	public List<Thrash> getOtherMarkings() {
		List<Thrash> other = new ArrayList<Thrash>();
		for (Thrash t : list) {
			if (t.getType().isOther()) {
				other.add(t);
			}
		}
		return other;
	}

	public boolean isEmpty() {
		if (list == null) {
			return true;
		}
		for (Thrash t : list) {
			if (t.getNum() > 0) {
				return false;
			}
		}
		return true;
	}
}
