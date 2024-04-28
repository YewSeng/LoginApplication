package com.trial.constants;

public enum Role {
	MANAGER("Manager Role"),
	USER("User Role");

	private String customName;
	
	private Role(String customName) {
		this.customName = customName;
	}

	public String getCustomName() {
		return customName;
	}
}
