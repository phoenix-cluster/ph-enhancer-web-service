package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import io.swagger.models.auth.In;

//public class Cluster implements Cloneable {
public class SpeciesInProject extends Taxonomy{

	private Integer psmNo;

	public SpeciesInProject() {
	}

	public SpeciesInProject(String id, String name) {
		super(id, name);
	}

	public SpeciesInProject(String id, String name, Integer psmNo) {
		super(id, name);
		this.psmNo = psmNo;
	}

	public Integer getPsmNo() {
		return psmNo;
	}

	public void setPsmNo(Integer psmNo) {
		this.psmNo = psmNo;
	}
}
