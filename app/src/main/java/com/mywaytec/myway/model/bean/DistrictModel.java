package com.mywaytec.myway.model.bean;

public class DistrictModel {
	private String name;
	private String code;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return code;
	}

	public void setZipcode(String zipcode) {
		this.code = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + code + "]";
	}

}
