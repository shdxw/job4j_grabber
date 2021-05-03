package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        DateTimeParser parser = new SqlRuDateTimeParser();
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers" + "/" + i).get();
            Elements table = doc.select(".forumTable");
            Elements row = table.select("tr");
            row.remove(0);
            for (Element element : row) {
                String url = element.child(1).child(0).attr("href");
                String name = element.child(1).child(0).text();
                String lastdate = element.child(5).text();

                Document localHref = Jsoup.connect(element.child(1).child(0).attr("href")).get();
                Element elementsOfPost = localHref.getElementsByClass("msgTable").first()
                        .child(0);
                String text = elementsOfPost.child(1).child(1).text();
                String date = elementsOfPost.child(2).child(0).text().split("\\[")[0].trim();//работаем с постом
                Post info = new Post(name, parser.parse(date), text, url);
                System.out.println(info.toString());
            }
        }
    }

}