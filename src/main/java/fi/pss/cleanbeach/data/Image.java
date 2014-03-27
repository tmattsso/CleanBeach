package fi.pss.cleanbeach.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Image extends AbstractEntity {

	private String mimetype;
	// TODO makes this a proper limit, this is for testing
	@Column(length = 512000)
	private byte[] content;

	@Temporal(TemporalType.TIMESTAMP)
	private Date uploaded;

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

}
