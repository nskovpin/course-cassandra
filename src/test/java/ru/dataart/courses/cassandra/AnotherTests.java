package ru.dataart.courses.cassandra;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AnotherTests {

    @Test
    public void createTimeStamp(){
        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
    }
}
