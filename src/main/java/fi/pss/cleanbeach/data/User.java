package fi.pss.cleanbeach.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "username",
		"oid" }) })
public class User extends AbstractEntity {

	@Column(nullable = false)
	private String name;
	private String username;
	private byte[] hashedPass;
	private String oid;
	private String oidProvider;
	@Column(nullable = false)
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
}
