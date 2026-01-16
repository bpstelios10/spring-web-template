package org.learnings.application_name.infrastructure.repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.auth.ProgrammaticPlainTextAuthProvider;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.jspecify.annotations.NonNull;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Configuration
@EnableCassandraRepositories(basePackages = "org.learnings.application_name.infrastructure.repositories")
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.data.cassandra.port}")
    private int port;
    @Value("${spring.data.cassandra.username}")
    private String username;
    @Value("${spring.data.cassandra.password}")
    private String password;
    @Value("${spring.data.cassandra.datacenter}")
    private String dataCenter;
    @Value("${spring.data.cassandra.keyspace}")
    private String keyspace;
    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;

    @Override
    @NonNull
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected String getLocalDataCenter() {
        return dataCenter;
    }

    @Override
    @NonNull
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    @NonNull
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction);
    }

    @Bean
    @Override
    @NonNull
    @Primary
    public CqlSessionFactoryBean cassandraSession() {
        CqlSessionFactoryBean cassandraSession = super.cassandraSession();
        cassandraSession.setUsername(username);
        cassandraSession.setPassword(password);

        return cassandraSession;
    }

    // Next beans are a bit redundant but since this is a learning project, it is good to have them
    @Bean("customSession")
    public CqlSession customSession() {
        return getCqlSessionBuilder()
                .withKeyspace(keyspace)
                .build();
    }

    @Bean(name = "test_keyspace_template")
    public CassandraTemplate cassandraTemplate(CqlSession customSession, CassandraConverter cassandraConverter) {
        CassandraTemplate cassandraTemplate = new CassandraTemplate(customSession, cassandraConverter);
        cassandraTemplate.setUsePreparedStatements(false);

        return cassandraTemplate;
    }

    private CqlSessionBuilder getCqlSessionBuilder() {
        return CqlSession.builder()
                .addContactPoints(getContactPointsAddresses())
                .withAuthProvider(new ProgrammaticPlainTextAuthProvider(username, password))
                .withLocalDatacenter(dataCenter)
                .addTypeCodecs(TypeCodecs.ZONED_TIMESTAMP_UTC);
    }

    private Collection<InetSocketAddress> getContactPointsAddresses() {
        return Arrays.stream(contactPoints.split(","))
                .map(address -> new InetSocketAddress(address, port)).toList();
    }
}
