package org.learnings.application_name.tests.component.api;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.auth.ProgrammaticPlainTextAuthProvider;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.dockerjava.api.model.PortBinding.parse;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Extension to spin-up a cassandra docker file before all class tests and remove it only when all tests are done.
 * <p></p>
 * <b>Important Notes</b>:
 * <ul>
 *     <li>If there are changes in the cassandra Image, you need to manually delete the old one and then run the tests.</li>
 *     <li>Tests don't truncate tables before/after their execution.</li>
 * </ul>
 * <p></p>
 * Improvement could be to leave the container running so that we don't start and stop it every time.
 */
public class LocalCassandraWithDockerExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Logger log = LoggerFactory.getLogger(LocalCassandraWithDockerExtension.class);
    private static final String KEYSPACE_NAME = "test_cassandra";
    private static final String CASSANDRA_CONTAINER_NAME = "test_cassandra";
    private static final String CASSANDRA_IMAGE_NAME = "test/cassandra";
    private static final String CASSANDRA_DOCKERFILE_LOCATION = "../cassandra/Dockerfile";
    private static final Lock LOCK = new ReentrantLock();

    private final DockerClient dockerClient;

    private static volatile String cassandraContainerID;
    private static volatile boolean isExecuted = false;

    public LocalCassandraWithDockerExtension() {
        DockerClientConfig standardConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(standardConfig.getDockerHost())
                .sslConfig(standardConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        dockerClient = DockerClientImpl.getInstance(standardConfig, dockerHttpClient);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        // lock to prevent parallel threads. Check if tests have executed this code once. then no need to rerun the whole code
        LOCK.lock();
        if (isExecuted) {
            LOCK.unlock();
            return;
        }

        // look for existing images of this name, to avoid dangling images
        List<Image> testCassandraImages = dockerClient.listImagesCmd().withShowAll(true).withImageNameFilter(CASSANDRA_IMAGE_NAME).exec();

        String imageId = !testCassandraImages.isEmpty() ? testCassandraImages.getFirst().getId() : dockerClient
                .buildImageCmd()
                .withDockerfile(new File(CASSANDRA_DOCKERFILE_LOCATION))
                .withTags(Set.of(CASSANDRA_IMAGE_NAME))
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        List<Container> containers = dockerClient.listContainersCmd()
                .withShowAll(true)
                .withNameFilter(singletonList(KEYSPACE_NAME))
                .exec();
        if (containers.isEmpty()) {
            CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                    .withHostConfig(HostConfig.newHostConfig().withPortBindings(parse("9042:9042")))
                    .withName(CASSANDRA_CONTAINER_NAME)
                    .exec();

            startDockerContainer(container.getId());
        } else if (containers.size() > 1) {
            log.debug("Multiple containers were found. Please clean your system.");
        } else if (containers.getFirst().getState().equalsIgnoreCase("exited")) {
            log.debug("Restarting existing container");
            startDockerContainer(containers.getFirst().getId());
        } else if (containers.getFirst().getState().equalsIgnoreCase("running")) {
            log.debug("container is up and running already");
            cassandraContainerID = containers.getFirst().getId();
        } else {
            log.debug("Cassandra container already running, using this for tests.");
        }

        // register the shutdown hook. notify that code has been executed. unlock
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("cassandra-docker-extension", this);
        isExecuted = true;
        LOCK.unlock();
    }

    @Override
    public void close() throws InterruptedException {
        if (dockerClient == null)
            throw new RuntimeException("docker client not created yet??");
        if (cassandraContainerID.isBlank())
            throw new RuntimeException("no cassandra container id is set, to be killed");

        log.debug("About to delete cassandra container with id: " + cassandraContainerID);

        dockerClient.stopContainerCmd(cassandraContainerID).exec();
        WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
        dockerClient.waitContainerCmd(cassandraContainerID).exec(resultCallback);
        resultCallback.awaitCompletion();

        dockerClient.removeContainerCmd(cassandraContainerID).exec();
        dockerClient.waitContainerCmd(cassandraContainerID).exec(resultCallback);
        resultCallback.awaitCompletion();
    }

    private void startDockerContainer(String containerToStartID) {
        Runnable startContainer = () -> dockerClient.startContainerCmd(containerToStartID).exec();
        Thread.ofVirtual().name("starting-container-thread").start(startContainer);

        log.debug("waiting for cassandra container to be healthy");
        await().atMost(Duration.ofSeconds(240L)).pollDelay(Duration.ofSeconds(5)).pollInterval(Duration.ofSeconds(5)).untilAsserted(() -> {
            try (CqlSession session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("localhost", 9042))
                    .withAuthProvider(new ProgrammaticPlainTextAuthProvider("cassandra", "cassandra"))
                    .withLocalDatacenter("datacenter1")
                    .withKeyspace(CASSANDRA_CONTAINER_NAME)
                    .build()) {
                log.debug("session was created: " + session.getName());
                assertThat(true).isTrue();
            } catch (Exception ex) {
                // Some classnotfound exceptions (cause of dependency conflicts) are thrown here when the session fails, but all is good when it succeeds
                assertThat(true).isFalse();
            }
        });

        log.debug("cassandra started successfully");
        cassandraContainerID = containerToStartID;
    }
}
