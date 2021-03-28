package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 711; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers" + "/" + i).get();
            Elements table = doc.select(".forumTable");
            Elements row = table.select("tr");
            row.remove(0);
            for (Element element : row) {
                String href = String.format("url:%s text:%s date:%s",
                        element.child(1).child(0).attr("href"),
                        element.child(1).child(0).text(),
                        element.child(5).text());
                System.out.println(href);
            }
        }
        }

}