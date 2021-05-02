package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers" + "/" + i).get();
            Elements table = doc.select(".forumTable");
            Elements row = table.select("tr");
            row.remove(0);
            for (Element element : row) {
                String href = String.format("url:%s name:%s lastdate:%s",
                        element.child(1).child(0).attr("href"),
                        element.child(1).child(0).text(),
                        element.child(5).text());
                System.out.println(href); //работаем с таблицей постов
                Document localHref = Jsoup.connect(element.child(1).child(0).attr("href")).get();
                Element elementsOfPost = localHref.getElementsByClass("msgTable").first()
                        .child(0);
                System.out.println(String.format("text: %s date: %s", elementsOfPost.child(1).child(1).text(),
                        elementsOfPost.child(2).child(0).text().split("\\[")[0].trim()));//работаем с постом

            }
        }
    }

}