package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser parser;

    public SqlRuParse(DateTimeParser parser) {
        this.parser = parser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements table = doc.select(".forumTable");
        Elements row = table.select("tr");
        row.remove(0);
        for (Element element : row) {
            String url = element.child(1).child(0).attr("href");
            posts.add(detail(url));
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Document localHref = null;
        try {
            localHref = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element elementsOfPost = localHref.getElementsByClass("msgTable").first()
                .child(0);
        String name = elementsOfPost.child(0).child(0).text();
        String text = elementsOfPost.child(1).child(1).text();
        String date = elementsOfPost.child(2).child(0).text().split("\\[")[0].trim();//работаем с постом
        return new Post(name, this.parser.parse(date), text, link, 0);

    }

    public static void main(String[] args) {
        List<Post> posts = new ArrayList<>();
        Parse sqlparser = new SqlRuParse(new SqlRuDateTimeParser());
        for (int i = 1; i < 2; i++) {
           posts.addAll(sqlparser.list("https://www.sql.ru/forum/job-offers" + "/" + i));
        }
        System.out.println(posts.toString());
    }
}