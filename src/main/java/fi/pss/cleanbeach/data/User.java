package fi.pss.cleanbeach.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "applicationusers", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"email", "oid" }) })
public class User extends AbstractEntity implements Comparable<User> {

	public static final int SALT_LENGTH_BYTES = 32;
	private static final int PASS_FIELD_LENGTH_BIT = SALT_LENGTH_BYTES * 8 + 256; // SHA-256

	@Column(nullable = false)
	private String name;
	private byte[] hashedPass;
	private String oid;
	private String oidProvider;
	@Column(nullable = false)
	private String email;

	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	private Set<UsersGroup> memberIn = new HashSet<>();

	public Set<UsersGroup> getMemberIn() {
		return memberIn;
	}

	public void setMemberIn(Set<UsersGroup> memberIn) {
		this.memberIn = memberIn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getHashedPass() {
		return hashedPass;
	}

	public void setHashedPass(byte[] hashedPass) {
		this.hashedPass = hashedPass;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOidProvider() {
		return oidProvider;
	}

	public void setOidProvider(String oidProvider) {
		this.oidProvider = oidProvider;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int compareTo(User o) {
		// sorts by user name
		return name.compareTo(o.name);
	}
}
