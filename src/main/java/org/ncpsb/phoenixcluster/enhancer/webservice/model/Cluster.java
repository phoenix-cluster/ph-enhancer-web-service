package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.List;

//public class Cluster implements Cloneable {
public class Cluster {

	private String id;
	private Float avPrecursorMz;
	private Integer specCount;
	private Float ratio;
	private List<Float> consensusMz;
	private List<Float> consensusIntens;
	private List<String> spectraTitles;
	private String sequencesRatios;

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

	public List<Float> getConsensusMz() {
		return consensusMz;
	}

	public void setConsensusMz(List<Float> consensusMz) {
		this.consensusMz = consensusMz;
	}

	public List<Float> getConsensusIntens() {
		return consensusIntens;
	}

	public void setConsensusIntens(List<Float> consensusIntens) {
		this.consensusIntens = consensusIntens;
	}

	public List<String> getSpectraTitles() {
		return spectraTitles;
	}

	public void setSpectraTitles(List<String> spectraTitles) {
		this.spectraTitles = spectraTitles;
	}
	public String getSequencesRatios() {
		return sequencesRatios;
	}

	public void setSequencesRatios(String sequencesRatios) {
		this.sequencesRatios = sequencesRatios;
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
