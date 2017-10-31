package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

import java.util.ArrayList;
import java.util.List;

//public class Cluster implements Cloneable {
public class Cluster {
	/*private String id;
	private int specCount;
	private float ratio;
	private List<Spectrum> spectra;
	*/

	private String id;
	private Float avPrecursorMz;
	private Float avPrecursorIntens;
	private Integer specCount;
	private Float ratio;
	private List<Float> mzValues;
	private List<Float> intensValues;
	private ArrayList<Spectrum> spectra;

	public Cluster(String id, Float avPrecursorMz, Float avPrecursorIntens,
				   Integer specCount, Float ratio, ArrayList<Spectrum> spectra) {
		this.id = id;
		this.avPrecursorMz = avPrecursorMz;
		this.avPrecursorIntens = avPrecursorIntens;
		this.specCount = specCount;
		this.ratio = ratio;
		this.spectra = spectra;
	}

	public Cluster() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getAvPrecursorMz() {
		return avPrecursorMz;
	}

	public void setAvPrecursorMz(Float avPrecursorMz) {
		this.avPrecursorMz = avPrecursorMz;
	}

	public Float getAvPrecursorIntens() {
		return avPrecursorIntens;
	}

	public void setAvPrecursorIntens(Float avPrecursorIntens) {
		this.avPrecursorIntens = avPrecursorIntens;
	}

	public Integer getSpecCount() {
		return specCount;
	}

	public void setSpecCount(Integer specCount) {
		this.specCount = specCount;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	public List<Float> getMzValues() {
		return mzValues;
	}

	public void setMzValues(List<Float> mzValues) {
		this.mzValues = mzValues;
	}

	public List<Float> getIntensValues() {
		return intensValues;
	}

	public void setIntensValues(List<Float> intensValues) {
		this.intensValues = intensValues;
	}

	public ArrayList<Spectrum> getSpectra() {
		return spectra;
	}

	public void setSpectra(ArrayList<Spectrum> spectra) {
		this.spectra = spectra;
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
