package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.List;

//public class Cluster implements Cloneable {
public class Taxonomy {

	private String id;
	private String name;

	public Taxonomy() {
	}

	public Taxonomy(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
