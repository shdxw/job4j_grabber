package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    Map<String, Integer> months;

    public SqlRuDateTimeParser() {
        this.months = new HashMap<>();
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
    }

    @Override
    public LocalDateTime parse(String parse) {
        String[] data = parse.split(",");
        String[] time = data[1].trim().split(":"); //время
        if (data[0].equals("сегодня")) {
           return concate(LocalDateTime.now(), time);
        } else if (data[0].equals("вчера")) {
            return concate(LocalDateTime.now().minusDays(1), time);
        } else {
            String[] date = data[0].split(" ");
            return LocalDateTime.of(Integer.parseInt(date[2]) + 2000, //год и тд
                    months.get(date[1]),
                    Integer.parseInt(date[0]),
                    Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]));
        }
    }

    private LocalDateTime concate(LocalDateTime date, String[] time)  {
        return date.withHour(Integer.parseInt(time[0])).withMinute(Integer.parseInt(time[1]));
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime date = parser.parse("сегодня, 22:29");
        System.out.println(date.toString());
    }
}
