package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

import java.util.List;

/**
 * Created by baimi on 2017/10/29.
 */
public class PageOfScoredPSM {
    private Integer pageSize;
    private Integer page;
    private Integer totalElements;
    private Integer totalPages;
    private String sortField;
    private String sortDirection;

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

    public PageOfScoredPSM(Integer pageSize, Integer page, Integer totalElements, Integer totalPages, String sortField, String sortDirection, List<ScoredPSM> scoredPSMs) {
        this.pageSize = pageSize;
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.scoredPSMs = scoredPSMs;
    }

    private List<ScoredPSM> scoredPSMs;

    public PageOfScoredPSM(Integer pageSize, Integer page, Integer totalElements, Integer totalPages, List<ScoredPSM> scoredPSMs) {
        this.pageSize = pageSize;
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.scoredPSMs = scoredPSMs;
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

    public List<ScoredPSM> getScoredPSMs() {
        return scoredPSMs;
    }

    public void setScoredPSMs(List<ScoredPSM> scoredPSMs) {
        this.scoredPSMs = scoredPSMs;
    }
}
