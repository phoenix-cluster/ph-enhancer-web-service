package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

import java.util.List;

/**
 * Created by baimi on 2017/11/26.
 */
public class Spectrum {
    private String spectrumTitle;
    private Float precursorMz;
    private Float precursorIntens;
    private Integer charge;
    private List<Float> peaklistMz;
    private List<Float> peaklistIntens;

    public String getSpectrumTitle() {
        return spectrumTitle;
    }

    public void setSpectrumTitle(String spectrumTitle) {
        this.spectrumTitle = spectrumTitle;
    }

    public Float getPrecursorMz() {
        return precursorMz;
    }

    public void setPrecursorMz(Float precursorMz) {
        this.precursorMz = precursorMz;
    }

    public Float getPrecursorIntens() {
        return precursorIntens;
    }

    public void setPrecursorIntens(Float precursorIntens) {
        this.precursorIntens = precursorIntens;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public List<Float> getPeaklistMz() {
        return peaklistMz;
    }

    public void setPeaklistMz(List<Float> peaklistMz) {
        this.peaklistMz = peaklistMz;
    }

    public List<Float> getPeaklistIntens() {
        return peaklistIntens;
    }

    public void setPeaklistIntens(List<Float> peaklistIntens) {
        this.peaklistIntens = peaklistIntens;
    }
}
