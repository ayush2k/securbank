package securbank.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DeviceTrust")

public class DeviceTrust {

	@Id
	@Column(name = "deviceTrustId", unique = true)
	private String deviceTrustId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@NotNull
	@Column(name = "macAddress")
	private String macAddress;

	public String getDeviceTrustId() {
		return deviceTrustId;
	}

	public void setDeviceTrustId(String deviceTrustId) {
		this.deviceTrustId = deviceTrustId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public DeviceTrust(){}
	
	public DeviceTrust(String deviceTrustId, User user, String macAddress) {
		super();
		this.deviceTrustId = deviceTrustId;
		this.user = user;
		this.macAddress = macAddress;
	}

	public String toString() {
		return "DeviceTrust [deviceTrustId=" + deviceTrustId + ", userId=" + user.getUserId() + ", macAddress="
				+ macAddress + "]";
	}
	
	

}
