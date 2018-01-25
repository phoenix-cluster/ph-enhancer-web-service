package org.ncpsb.phoenixcluster.enhancer.webservice.model;

/**
 * The statistics numbers of a project for venn diagram
 */
public class VennData {
    private String  projectId;
    private Integer prePSM_no;
    private Integer prePSM_not_matched_no;
    private Integer prePSM_high_conf_no;
    private Integer prePSM_low_conf_no;
    private Integer better_PSM_no;
    private Integer new_PSM_no;
    private Integer matched_spec_no;
    private Integer matched_id_spec_no;

    public VennData(String projectId, Integer prePSM_no, Integer prePSM_not_matched_no, Integer prePSM_high_conf_no, Integer prePSM_low_conf_no, Integer better_PSM_no, Integer new_PSM_no, Integer matched_spec_no, Integer matched_id_spec_no) {
        this.projectId = projectId;
        this.prePSM_no = prePSM_no;
        this.prePSM_not_matched_no = prePSM_not_matched_no;
        this.prePSM_high_conf_no = prePSM_high_conf_no;
        this.prePSM_low_conf_no = prePSM_low_conf_no;
        this.better_PSM_no = better_PSM_no;
        this.new_PSM_no = new_PSM_no;
        this.matched_spec_no = matched_spec_no;
        this.matched_id_spec_no = matched_id_spec_no;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getPrePSM_no() {
        return prePSM_no;
    }

    public void setPrePSM_no(Integer prePSM_no) {
        this.prePSM_no = prePSM_no;
    }

    public Integer getPrePSM_not_matched_no() {
        return prePSM_not_matched_no;
    }

    public void setPrePSM_not_matched_no(Integer prePSM_not_matched_no) {
        this.prePSM_not_matched_no = prePSM_not_matched_no;
    }

    public Integer getPrePSM_high_conf_no() {
        return prePSM_high_conf_no;
    }

    public void setPrePSM_high_conf_no(Integer prePSM_high_conf_no) {
        this.prePSM_high_conf_no = prePSM_high_conf_no;
    }

    public Integer getPrePSM_low_conf_no() {
        return prePSM_low_conf_no;
    }

    public void setPrePSM_low_conf_no(Integer prePSM_low_conf_no) {
        this.prePSM_low_conf_no = prePSM_low_conf_no;
    }

    public Integer getBetter_PSM_no() {
        return better_PSM_no;
    }

    public void setBetter_PSM_no(Integer better_PSM_no) {
        this.better_PSM_no = better_PSM_no;
    }

    public Integer getNew_PSM_no() {
        return new_PSM_no;
    }

    public void setNew_PSM_no(Integer new_PSM_no) {
        this.new_PSM_no = new_PSM_no;
    }

    public Integer getMatched_spec_no() {
        return matched_spec_no;
    }

    public void setMatched_spec_no(Integer matched_spec_no) {
        this.matched_spec_no = matched_spec_no;
    }

    public Integer getMatched_id_spec_no() {
        return matched_id_spec_no;
    }

    public void setMatched_id_spec_no(Integer matched_id_spec_no) {
        this.matched_id_spec_no = matched_id_spec_no;
    }
}
