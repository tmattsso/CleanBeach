package fi.pss.cleanbeach.services.util;

import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LoggingInterceptor {

	private transient Logger log = Logger.getLogger(LoggingInterceptor.class
			.getSimpleName());

	@AroundInvoke
	public Object log(InvocationContext ctx) throws Exception {

		String method = ctx.getMethod().getDeclaringClass().getName() + "."
				+ ctx.getMethod().getName() + "()";
		log.fine(method + " called");

		long start = System.currentTimeMillis();
		try {
			return ctx.proceed();
		} catch (Exception e) {
			throw e;
		} finally {

			log.fine(method + " finished");
		}

	}
}
