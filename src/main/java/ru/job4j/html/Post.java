package ru.job4j.html;

import java.time.LocalDateTime;

public class Post {
    private String name;
    private LocalDateTime dateTime;
    private String text;
    private String link;
    private int id;

    public Post(String name, LocalDateTime dateTime, String text, String link, int id) {
        this.name = name;
        this.dateTime = dateTime;
        this.text = text;
        this.link = link;
        this.id = id;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", text='" + text + '\'' +
                ", link='" + link + '\'' +
                ", id=" + id +
                '}';
    }
}
