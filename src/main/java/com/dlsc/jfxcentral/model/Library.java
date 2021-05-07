package com.dlsc.jfxcentral.model;

public class Library extends ModelObject {

    private String title;
    private String description;
    private License license;
    private String homepage;
    private String repository;
    private String personId;
    private String companyId;
    private String logoImageFile;
    private String issueTracker;
    private String discussionBoard;

    public Library() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLogoImageFile() {
        return logoImageFile;
    }

    public void setLogoImageFile(String logoImageFile) {
        this.logoImageFile = logoImageFile;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getIssueTracker() {
        return issueTracker;
    }

    public void setIssueTracker(String issueTracker) {
        this.issueTracker = issueTracker;
    }

    public String getDiscussionBoard() {
        return discussionBoard;
    }

    public void setDiscussionBoard(String discussionBoard) {
        this.discussionBoard = discussionBoard;
    }
}
