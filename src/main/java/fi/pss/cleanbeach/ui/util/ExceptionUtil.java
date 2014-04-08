package fi.pss.cleanbeach.ui.util;

public class ExceptionUtil {

	public static Throwable getRootCause(Throwable t) {
		Throwable root = t;
		if (root.getCause() == null || root.getCause() == root) {
			return root;
		} else {
			return getRootCause(root.getCause());
		}
	}
}
