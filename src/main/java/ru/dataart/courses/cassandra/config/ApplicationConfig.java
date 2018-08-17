package ru.dataart.courses.cassandra.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableCassandraRepositories("ru.dataart.courses.cassandra.repository")
@Profile(value = {"integration", "production"})
public class ApplicationConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.address}")
    private String clusterAddress;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Bean
    public Cluster getCassandraCluster(){
        return Cluster.builder().addContactPoint(clusterAddress).build();
    }

    @Bean
    public Session getCassandraSession(){
        return getCassandraCluster().connect(keyspace);
    }


    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

}
