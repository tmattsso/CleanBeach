package fi.pss.cleanbeach.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Location extends AbstractEntity {

	public enum STATUS {
		NO_DATA, OK, DIRTY;
	}

	private String name;
	private double latitude;
	private double longitude;

	@Enumerated(EnumType.STRING)
	private STATUS status = STATUS.NO_DATA;

	@Column(length = 2048)
	private String statusMsg;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setStatusMsg(String desc) {
		statusMsg = desc;
	}

	public String getStatusMsg() {
		return statusMsg;
	}
}
