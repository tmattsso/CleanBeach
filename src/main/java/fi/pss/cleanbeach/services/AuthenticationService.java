package fi.pss.cleanbeach.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;

import fi.pss.cleanbeach.data.User;

@Stateless
public class AuthenticationService {

	private static final int HASH_ITERATIONS = 1000;

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

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

		byte[] salt = Arrays.copyOfRange(u.getHashedPass(), 0,
				User.SALT_LENGTH_BYTES);
		byte[] pass = Arrays.copyOfRange(u.getHashedPass(),
				User.SALT_LENGTH_BYTES, u.getHashedPass().length);

		byte[] result = hash(salt, password);

		if (Arrays.equals(pass, result)) {
			return u;
		}

		return null;
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

	public User getUser(String oid, String oidProvider) {
		Query q = em
				.createQuery("Select u From User u where oid=? and oidProvider=?");
		q.setParameter(1, oid);
		q.setParameter(2, oidProvider);

		User u = (User) q.getSingleResult();
		return u;
	}

	public User createUser(User u, String password) {
		if (password != null) {
			byte[] salt = new byte[User.SALT_LENGTH_BYTES];
			try {
				SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			byte[] both = ArrayUtils.addAll(salt, hash(salt, password));
			u.setHashedPass(both);
		}
		em.persist(u);
		return u;
	}

	public User refresh(User u) {
		return em.find(u.getClass(), u.getId());
	}
}
