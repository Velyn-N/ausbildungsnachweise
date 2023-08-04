package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter @Setter
public class User implements Serializable, JsonEntity {

	private Long id = -1L;
	private boolean locked = false;

	private String email = "";
	private String password = "";
	private String fullName = "Guest";

	private boolean isAzubi = false;
	private boolean isAusbilder = false;
	private boolean isAdmin = false;

	public boolean isAzubi() {
		return isAzubi;
	}

	public boolean isAusbilder() {
		return isAusbilder;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean getIsAzubi() {
		return isAzubi;
	}

	public void setIsAzubi(boolean azubi) {
		isAzubi = azubi;
	}

	public boolean getIsAusbilder() {
		return isAusbilder;
	}

	public void setIsAusbilder(boolean ausbilder) {
		isAusbilder = ausbilder;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean admin) {
		isAdmin = admin;
	}

	@Override
	public boolean isValid() {
		return StringUtils.isNotBlank(email)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(fullName)
				&& isAzubi || isAusbilder || isAdmin;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", locked=" + locked +
				", email='" + email + '\'' +
				", fullName='" + fullName + '\'' +
				", isAzubi=" + isAzubi +
				", isAusbilder=" + isAusbilder +
				", isAdmin=" + isAdmin +
				'}';
	}
}
