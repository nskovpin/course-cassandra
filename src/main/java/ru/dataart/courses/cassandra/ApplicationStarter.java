package ru.dataart.courses.cassandra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = CassandraDataAutoConfiguration.class)
public class ApplicationStarter {

    public static void main(String[] args){
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ApplicationStarter.class, args);
    }

}
