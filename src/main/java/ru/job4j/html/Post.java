package ru.job4j.html;

import java.time.LocalDateTime;

public class Post {
    String name;
    LocalDateTime dateTime;
    String text;
    String link;

    public Post(String name, LocalDateTime dateTime, String text, String link) {
        this.name = name;
        this.dateTime = dateTime;
        this.text = text;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }
}
