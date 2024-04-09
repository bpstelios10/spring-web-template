package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Extension to spin-up an embedded neo4j server and remove it only after all tests are done.
 * <p>
 * Lock is used, in order to prevent many tests running in parallel start the server.
 * Once the first test class starts neo4j, then the rest will skip the startup
 * At the end of all tests (see CustomAfterSuite), the embedded server is closed and some system properties set are restored
 * <br>
 * Private class CustomAfterSuite implements ExtensionContext.Store.CloseableResource and is put in top level context store
 * to make sure it will only be invoked AFTER ALL tests are executed.
 */
public class Neo4jHarnessExtension implements BeforeAllCallback {

    private static volatile Neo4j embeddedDatabaseServer;
    private static volatile String neo4jURISystemPropertyValue;
    private static volatile boolean isExecuted = false;

    private static final Lock LOCK = new ReentrantLock();
    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jHarnessExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) {
        // lock to prevent parallel threads. Check if tests have executed this code once. then no need to rerun the whole code
        LOCK.lock();
        if (isExecuted) {
            LOCK.unlock();
            return;
        }

        LOGGER.debug("Run Neo4jHarnessExtension setup.");

        createAfterAllTestsHook(context);
        neo4jURISystemPropertyValue = System.getProperty("spring.neo4j.uri");
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withFixture("""
                        CREATE (client1:Person {id:1001, name:'first one', born:1989})
                        CREATE (client2:Person {id:1002, name:'second one', born:1999})
                        CREATE (client3:Person {id:1003, name:'third one', born:1980})
                        CREATE (LillyW:Person {name:'Lilly Wachowski', born:1967})
                        CREATE (Keanu:Person {name:'Keanu Reeves', born:1964})
                        CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})
                        CREATE (LillyW)-[:DIRECTED]->(TheMatrix)
                        CREATE (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrix)
                        CREATE (client1)-[:WATCHED {rating:2}]->(TheMatrix)
                        """
                )
                .build();

        System.setProperty("spring.neo4j.uri", String.format(embeddedDatabaseServer.boltURI().toString()));
        isExecuted = true;
        LOCK.unlock();
    }

    private static void createAfterAllTestsHook(ExtensionContext context) {
        ExtensionContext getTopLevelContext = context.getParent().orElse(context);
        while (getTopLevelContext.getParent().isPresent()) getTopLevelContext = getTopLevelContext.getParent().get();
        getTopLevelContext.getStore(ExtensionContext.Namespace.GLOBAL).put("AfterAllTestsExecutor", new CustomAfterSuite());
    }

    private static class CustomAfterSuite implements ExtensionContext.Store.CloseableResource {
        private static final Logger LOGGER = LoggerFactory.getLogger(CustomAfterSuite.class);

        @Override
        public void close() {
            LOGGER.debug("Running CustomAfterSuite to close after all tests are run");
            // restore system property from before test execution
            if (neo4jURISystemPropertyValue == null)
                System.clearProperty("spring.neo4j.uri");
            else
                System.setProperty("spring.neo4j.uri", neo4jURISystemPropertyValue);
            embeddedDatabaseServer.close();
        }
    }
}
