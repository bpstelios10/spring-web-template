package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class FunctionalEndpointTests {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionalEndpointTests.class);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllResource1_whenApiHeaderMissing_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/resource1/rate-limited-resources"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{  \"error\": \"Missing Header: X-api-key\"  }"));
    }

    @Test
    void getAllResource1_succeeds() throws Exception {
        mockMvc.perform(get("/resource1/rate-limited-resources").header("X-api-key", "tester"))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder("f9734631-6833-4885-93c5-dd41679fc908", "f9734631-6833-4885-93c5-dd41679fc907", "f9734631-6833-4885-93c5-dd41679fc906")));
    }

    @ParameterizedTest
    @MethodSource("rateLimitingPerPlan")
    void getAllResource1_forMultipleConcurrentUsersOfSameCustomers_mightHitRateLimit(String apiKey, Integer... expectedStatusCodes)
            throws Exception {
        try (ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            Callable<ResultActions> callable =
                    () -> {
                        LOG.debug("******* starting execution");
                        ResultActions result = mockMvc.perform(get("/resource1/rate-limited-resources").header("X-api-key", apiKey));
                        LOG.debug("******* finished execution");
                        return result;
                    };

            Future<ResultActions> call1 = executorService.submit(callable);
            Future<ResultActions> call2 = executorService.submit(callable);
            Future<ResultActions> call3 = executorService.submit(callable);

            List<ResultActions> results = List.of(call1.get(), call2.get(), call3.get());

            List<Integer> resultsStatusCodes = results.stream()
                    .map(ResultActions::andReturn)
                    .map(MvcResult::getResponse)
                    .map(MockHttpServletResponse::getStatus)
                    .toList();

            assertThat(resultsStatusCodes).containsExactlyInAnyOrder(expectedStatusCodes);
        }
    }

    @Test
    void getAllResource1_forMultipleConcurrentUsersOfDifferentFreeTierCustomers_wontHitRateLimit() throws Exception {
        try (ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            Callable<ResultActions> callable = () ->  mockMvc.perform(
                            get("/resource1/rate-limited-resources")
                                    .header("X-api-key", new Random().nextLong())
            );

            Future<ResultActions> call1 = executorService.submit(callable);
            Future<ResultActions> call2 = executorService.submit(callable);
            Future<ResultActions> call3 = executorService.submit(callable);

            List<ResultActions> results = List.of(call1.get(), call2.get(), call3.get());

            List<Integer> resultsStatusCodes = results.stream()
                    .map(ResultActions::andReturn)
                    .map(MvcResult::getResponse)
                    .map(MockHttpServletResponse::getStatus)
                    .toList();

            assertThat(resultsStatusCodes).containsExactlyInAnyOrder(200, 200, 200);
        }
    }

    private static Stream<Arguments> rateLimitingPerPlan() {
        return Stream.of(
                Arguments.of("tester2", new Integer[] {200, 200, 429}),
                Arguments.of("BC001-tester2", new Integer[] {200, 200, 200}),
                Arguments.of("EC001-tester2", new Integer[] {200, 200, 200})
        );
    }
}
