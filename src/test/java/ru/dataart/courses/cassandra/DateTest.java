package ru.dataart.courses.cassandra;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTest {

    @Test
    public void parseTest(){
        LocalDateTime dateTime = LocalDateTime.parse("2018-04-19 05:25", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println(dateTime);
    }
}
