package com.dlsc.jfxcentral.model;

import java.net.URL;

import javafx.scene.image.Image;

public class Person {

    private String name;
    private String email;

    private URL website;
    private URL blog;
    private URL linkedIn;
    private URL twitter;
    private URL gitHub;

    private Image photo;

    private String description;

    private boolean champion;
    private boolean rockstar;

    public Person(String name) {
        setName(name);
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public URL getBlog() {
        return blog;
    }

    public void setBlog(URL blog) {
        this.blog = blog;
    }

    public URL getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(URL linkedIn) {
        this.linkedIn = linkedIn;
    }

    public URL getTwitter() {
        return twitter;
    }

    public void setTwitter(URL twitter) {
        this.twitter = twitter;
    }

    public URL getGitHub() {
        return gitHub;
    }

    public void setGitHub(URL gitHub) {
        this.gitHub = gitHub;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChampion() {
        return champion;
    }

    public void setChampion(boolean champion) {
        this.champion = champion;
    }

    public boolean isRockstar() {
        return rockstar;
    }

    public void setRockstar(boolean rockstar) {
        this.rockstar = rockstar;
    }
}
