package fi.pss.cleanbeach.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService.RegistrationException.REGISTRATION_CAUSE;

@Stateless
public class AuthenticationService {

	private static final int MIN_PASS_LENTGH = 6;
	private static final int HASH_ITERATIONS = 1000;
	public static final String PROVIDER_FB = "facebook";
	private final Pattern emailpattern = Pattern
			.compile("^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$");

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	private final Logger log = Logger.getLogger(getClass().getSimpleName());

	public User login(String email, String password) {
		Query q = em.createQuery("SELECT u FROM User u WHERE email=:email");
		q.setParameter("email", email);

		User u = null;
		try {
			u = (User) q.getSingleResult();
		} catch (NoResultException e) {
			// Ignore
		}

		if (u == null) {
			// no such user
			return null;
		}
		if (u.getHashedPass() == null) {
			// user exists, but is registered through oid
			return null;
		}

		byte[] salt = getSalt(u);
		byte[] pass = Arrays.copyOfRange(u.getHashedPass(),
				User.SALT_LENGTH_BYTES, u.getHashedPass().length);

		byte[] result = hash(salt, password);

		if (Arrays.equals(pass, result)) {
			return u;
		}

		return null;
	}

	private static byte[] getSalt(User u) {
		return Arrays.copyOfRange(u.getHashedPass(), 0, User.SALT_LENGTH_BYTES);
	}

	private static byte[] hash(byte[] salt, String pwd) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.reset();
			for (int i = 0; i < HASH_ITERATIONS; i++) {
				md.update(salt);
				md.update(pwd.getBytes());
			}
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUser(String oid, String oidProvider) throws NoSuchUser {
		Query q = em
				.createQuery("Select u From User u where oid=? and oidProvider=?");
		q.setParameter(1, oid);
		q.setParameter(2, oidProvider);

		try {
			User u = (User) q.getSingleResult();

			return u;
		} catch (NoResultException e) {
			throw new NoSuchUser();
		}
	}

	public User createUser(User u, String password)
			throws RegistrationException {
		checkValid(u, password);

		if (u.getOid() != null) {
			byte[] salt = new byte[User.SALT_LENGTH_BYTES];
			try {
				SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			byte[] both = ArrayUtils.addAll(salt, hash(salt, password));
			u.setHashedPass(both);
		}

		try {
			em.persist(u);
		} catch (RuntimeException e) {
			log.log(Level.WARNING, "exception when persisting new user", e);
			throw new RegistrationException(
					REGISTRATION_CAUSE.EMAILALREADYINUSE);
		}
		return u;
	}

	private void checkValid(User u, String password)
			throws RegistrationException {

		if (u.getName() == null || u.getName().isEmpty()) {
			log.info("Username not provided");
			throw new RegistrationException(REGISTRATION_CAUSE.NONAME);
		}

		if (u.getEmail() == null || u.getEmail().isEmpty()) {
			log.info("Username not provided");
			throw new RegistrationException(REGISTRATION_CAUSE.NOEMAIL);
		}
		if (!emailpattern.matcher(u.getEmail()).matches()) {
			log.info("Email not valid");
			throw new RegistrationException(REGISTRATION_CAUSE.EMAILNOTVALID);
		}

		if (u.getOid() == null) {
			if (password == null || password.isEmpty()) {
				log.info("No password provided");
				throw new RegistrationException(REGISTRATION_CAUSE.NOPASS);
			}
			if (password.length() < MIN_PASS_LENTGH) {
				log.info("Pass not long enough");
				throw new RegistrationException(REGISTRATION_CAUSE.PASSTOOSHORT);
			}
		}

	}

	public User refresh(User u) {
		return em.find(u.getClass(), u.getId());
	}

	public static class RegistrationException extends Exception {

		private static final long serialVersionUID = 2151988988556345659L;

		public enum REGISTRATION_CAUSE {
			NOPASS, NONAME, NOEMAIL, PASSTOOSHORT, EMAILALREADYINUSE, EMAILNOTVALID
		}

		private final REGISTRATION_CAUSE cause;

		public RegistrationException(REGISTRATION_CAUSE cause) {
			this.cause = cause;
		}

		public REGISTRATION_CAUSE getFault() {
			return cause;
		}
	}

	public static class NoSuchUser extends Exception {

		private static final long serialVersionUID = 7351005755063615170L;

	}

	public User changeUserEmail(User currentUser, String newMail) {
		currentUser = refresh(currentUser);
		currentUser.setEmail(newMail);
		currentUser = em.merge(currentUser);
		return currentUser;
	}

	public void changeUserPassword(User user, String value) {
		user = refresh(user);
		user.setHashedPass(hash(getSalt(user), value));
		em.merge(user);
	}

	public User changeUserName(User user, String value) {
		user = refresh(user);
		user.setName(value);

		return em.merge(user);
	}
}
