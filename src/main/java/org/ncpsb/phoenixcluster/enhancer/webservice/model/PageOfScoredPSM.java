package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.List;

/**
 * Created by baimi on 2017/10/29.
 */
public class PageOfScoredPSM {
    private String  projectId;
    private Integer pageSize;
    private Integer page;
    private Integer totalElements;
    private Integer totalPages;
    private String sortField;
    private String sortDirection;
    private String filterByTaxonomyId;
    private List<ScoredPSMForWeb> scoredPSMs;

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public PageOfScoredPSM(String projectId, Integer pageSize, Integer page, Integer totalElements, Integer totalPages, String sortField, String sortDirection, String filterByTaxonomyId, List<ScoredPSMForWeb> scoredPSMsForWeb) {
        this.pageSize = pageSize;
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.filterByTaxonomyId = filterByTaxonomyId;
        this.scoredPSMs = scoredPSMsForWeb;
    }


    public PageOfScoredPSM(String projectId, Integer pageSize, Integer page, Integer totalElements, Integer totalPages, List<ScoredPSMForWeb> scoredPSMsForWeb) {
        this.pageSize = pageSize;
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.scoredPSMs = scoredPSMsForWeb;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<ScoredPSMForWeb> getScoredPSMs() {
        return scoredPSMs;
    }

    public void setScoredPSMs(List<ScoredPSMForWeb> scoredPSMs) {
        this.scoredPSMs = scoredPSMs;
    }
}
