package fi.pss.cleanbeach.data;

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
}
