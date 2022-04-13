package de.nmadev.ausbildungsnachweise.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter @Setter
public class User implements Serializable {

	private Long id = -1L;
	private boolean locked = false;

	private String email = "";
	private String password = "";
	private String fullName = "Guest";

	private boolean isAzubi = false;
	private boolean isAusbilder = false;

	public boolean isLoggedIn() {
		return StringUtils.isNotBlank(email);
	}
}
