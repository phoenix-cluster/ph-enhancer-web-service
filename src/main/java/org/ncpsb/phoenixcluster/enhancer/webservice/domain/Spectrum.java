package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

//public class Spectrum implements Cloneable {
public class Spectrum {
	private String title;
	private String sequence;
	private Integer charge;
	private Float precursorMz;
	private String species;
	private Float similarysocre;
	private String clusterId;
	private String projectId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	public Float getPrecursorMz() {
		return precursorMz;
	}

	public void setPrecursorMz(Float precursorMz) {
		this.precursorMz = precursorMz;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public Float getSimilarysocre() {
		return similarysocre;
	}

	public void setSimilarysocre(Float ss) {
		this.similarysocre = ss;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Spectrum() {

	}

	public Spectrum(String id, String sequence, Integer charge, Float precursorMz,
					String species, Float ss, String clusterId, String projectId) {
		super();
		this.title = id;
		this.sequence = sequence;
		this.charge = charge;
		this.precursorMz = precursorMz;
		this.species = species;
		this.similarysocre = ss;
		this.clusterId = clusterId;
		this.projectId = projectId;
	}

	public Spectrum(String id, String sequence, Integer charge,
					Float precursorMz, String species, Float ss) {

		this.title = id;
		this.sequence = sequence;
		this.charge = charge;
		this.precursorMz = precursorMz;
		this.species = species;
		this.similarysocre = ss;
	}

	/*@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null) {
			// System.out.println("otherObject == null");
			return false;
		}

		if (this == otherObject) {
			// System.out.println("this == otherObject");
			return true;
		}

		if (this.getClass() != otherObject.getClass()) {
			// System.out.println("this.getClass() != otherObject.getClass()");
			return false;
		} else {
			Spectrum other = (Spectrum) otherObject;
			return this.title.equals(other.title);
			// if (this.species == null || this.species == "" || other.species == null ||
			// other.species == "")
			// return this.spectrumId.equals(other.spectrumId) && this.charge ==
			// other.charge
			// && this.precursorMz == other.precursorMz;
			// else
			// return this.spectrumId.equals(other.spectrumId) && this.charge ==
			// other.charge
			// && this.precursorMz == other.precursorMz &&
			// this.species.equals(other.species);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Spectrum [id=" + id + ", sequence=" + sequence + ", charge=" + charge + ", precursorMz=" + precursorMz
				+ ", species=" + species + "]";
	}

	@Override
	public Spectrum clone() throws CloneNotSupportedException {
		Spectrum result = (Spectrum) super.clone();
		return result;
	}*/
	
//	public static void main(String[] args) {
//		Spectrum spectrum1 = new Spectrum("id", "AAA", 1.2F, 1.67F, "cat");
//		Spectrum spectrum2 = new Spectrum("id", "AAA", 1.2F, 1.67F, "cat");
//		Spectrum spectrum3 = new Spectrum("td", "AAA", 1.2F, 1.67F, "cat");
//		System.out.println(spectrum1);
//		System.out.println(spectrum1.hashCode());
//		System.out.println(spectrum2);
//		System.out.println(spectrum2.hashCode());
//		System.out.println(spectrum3);
//		System.out.println(spectrum3.hashCode());
//	}

}
