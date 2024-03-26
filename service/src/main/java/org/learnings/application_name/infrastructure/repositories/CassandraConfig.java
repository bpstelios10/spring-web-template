package org.learnings.application_name.infrastructure.repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.auth.ProgrammaticPlainTextAuthProvider;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Configuration
@EnableCassandraRepositories(basePackages = "org.learnings.application_name.infrastructure.repositories",
        cassandraTemplateRef = "test_keyspace_template")
@Primary
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
    private String keySpace;
    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;

    @Override
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
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction);
    }

    @Bean("customSession")
    @Primary
    public CqlSession cqlSession() {
        return getCqlSessionBuilder()
                .withKeyspace(keySpace)
                .build();
    }

    @Bean(name = "test_keyspace_template")
    public CassandraTemplate cassandraTemplate(@Qualifier("customSession") CqlSession cqlSession,
                                               CassandraConverter cassandraConverter) {
        CassandraTemplate cassandraTemplate = new CassandraTemplate(cqlSession, cassandraConverter);
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
