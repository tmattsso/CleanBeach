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

	public static boolean hasCause(Throwable t, Class<? extends Exception> clazz) {

		if (clazz.isAssignableFrom(t.getClass())) {
			return true;
		}
		if (t.getCause() == null || t.getCause() == t) {
			return false;
		}
		return hasCause(t.getCause(), clazz);
	}
}
