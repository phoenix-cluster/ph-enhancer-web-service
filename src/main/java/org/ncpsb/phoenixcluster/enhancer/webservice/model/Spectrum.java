package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.List;

/**
 * Created by baimi on 2017/11/26.
 */
public class Spectrum {
    private String title;
    private Float precursorMz;
    private Float precursorIntens;
    private Integer charge;
    private List<Float> peaklistMz;
    private List<Float> peaklistIntens;

    private String peptideSequence;

    private String peptideModsStr;
    private List <ModificationForWeb> peptideMods;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public String getPeptideSequence() {
        return peptideSequence;
    }

    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }
}
