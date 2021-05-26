package com.dlsc.jfxcentral.model;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Post extends ModelObject {

    private SyndFeed syndFeed;

    private SyndEntry syndEntry;

    public Post(SyndFeed syndFeed, SyndEntry syndEntry) {
        this.syndFeed = syndFeed;
        this.syndEntry = syndEntry;
    }

    public SyndFeed getSyndFeed() {
        return syndFeed;
    }

    public void setSyndFeed(SyndFeed syndFeed) {
        this.syndFeed = syndFeed;
    }

    public SyndEntry getSyndEntry() {
        return syndEntry;
    }

    public void setSyndEntry(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;
    }

    public LocalDate getDate() {
        return ZonedDateTime.ofInstant(syndEntry.getPublishedDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
    }
}
