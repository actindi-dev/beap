package com.actindi.beap;

import java.util.ArrayList;
import java.util.List;

public class Beacon {
	public String name = "";
	public String appName = "";
	public String appUrl = "";
	public List<String> tags = new ArrayList<String>();

	public Beacon() {
	}

	public Beacon(String name, String appName, String appUrl, List<String> tags) {
		this.name = name;
		this.appName = appName;
		this.appUrl = appUrl;
		this.tags = tags;
	}
}
