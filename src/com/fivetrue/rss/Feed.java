package com.fivetrue.rss;

import java.util.ArrayList;
import java.util.List;

/*
 * Stores an RSS feed
 */
public class Feed {

	final String title;
	final String link;
	final String description;
	final String language;
	final String copyright;
	final String pubDate;
	final String lastBuildDate;

	final List<FeedMessage> entries = new ArrayList<FeedMessage>();

	public Feed(String title, String link, String description, String language,
			String copyright, String pubDate, String lastBuildDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.copyright = copyright;
		this.pubDate = pubDate;
		this.lastBuildDate = lastBuildDate;
	}

	public List<FeedMessage> getMessages() {
		return entries;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getLanguage() {
		return language;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getLastBuildDate() {
		return lastBuildDate;
	}

	@Override
	public String toString() {
		return "Feed [title=" + title + ", link=" + link + ", description=" + description + ", language=" + language
				+ ", copyright=" + copyright + ", pubDate=" + pubDate + ", lastBuildDate=" + lastBuildDate + "]";
	}

} 