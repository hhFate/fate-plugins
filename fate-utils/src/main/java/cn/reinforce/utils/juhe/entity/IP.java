package cn.reinforce.utils.juhe.entity;

import com.google.gson.annotations.Expose;

public class IP {

	@Expose
	private String area;
	
	@Expose
	private String location;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "IP [area=" + area + ", location=" + location + "]";
	}
	
}
