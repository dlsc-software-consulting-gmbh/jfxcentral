package com.dlsc.jfxcentral.model;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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
        Date date = syndEntry.getUpdatedDate();
        if (date == null) {
            date = syndEntry.getPublishedDate();
        }

        if (date != null) {
            return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }

        return LocalDate.now();
    }
}
