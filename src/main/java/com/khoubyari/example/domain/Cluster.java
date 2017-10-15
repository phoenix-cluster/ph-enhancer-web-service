package com.khoubyari.example.domain;

import java.util.ArrayList;

//public class Cluster implements Cloneable {
public class Cluster {
	/*private String id;
	private float avPrecursorMz;
	private float avPrecursorIntens;
	private int specCount;
	private float ratio;
	private List<Spectrum> spectra;
	private List<Float> mzValues;
	private List<Float> intensValues;*/

	private String id;
	private String avPrecursorMz;
	private String avPrecursorIntens;
	private String specCount;
	private String ratio;
	private String CMz;
	private String CIntens;
	private ArrayList<Spectrum> spectra;

	public Cluster(String id, String avPrecursorMz, String avPrecursorIntens,
				   String specCount, String ratio, String CMz, String CIntens, ArrayList<Spectrum> spectra) {
		this.id = id;
		this.avPrecursorMz = avPrecursorMz;
		this.avPrecursorIntens = avPrecursorIntens;
		this.specCount = specCount;
		this.ratio = ratio;
		this.CMz = CMz;
		this.CIntens = CIntens;
		this.spectra = spectra;
	}
	public Cluster() {}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvPrecursorMz() {
		return avPrecursorMz;
	}

	public void setAvPrecursorMz(String avPrecursorMz) {
		this.avPrecursorMz = avPrecursorMz;
	}

	public String getAvPrecursorIntens() {
		return avPrecursorIntens;
	}

	public void setAvPrecursorIntens(String avPrecursorIntens) {
		this.avPrecursorIntens = avPrecursorIntens;
	}

	public String getSpecCount() {
		return specCount;
	}

	public void setSpecCount(String specCount) {
		this.specCount = specCount;
	}

	public ArrayList<Spectrum> getSpectra() {
		return spectra;
	}

	public void setSpectrums(ArrayList<Spectrum> spectra) {
		this.spectra = spectra;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getMzValues() {
		return CMz;
	}

	public void setMzValues(String mzValues) {
		this.CMz = mzValues;
	}

	public String getIntensValues() {
		return CIntens;
	}

	public void setIntensValues(String CIntens) {
		this.CIntens = CIntens;
	}

	/*@Override
	public Cluster clone() throws CloneNotSupportedException {
		Cluster result = (Cluster) super.clone();
		ArrayList<Spectrum> tmpSpectra = new ArrayList<>(spectra.size());
		for (Spectrum spec : spectra) {
			tmpSpectra.add(spec.clone());
		}
		result.spectra = tmpSpectra;
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}*/
}
