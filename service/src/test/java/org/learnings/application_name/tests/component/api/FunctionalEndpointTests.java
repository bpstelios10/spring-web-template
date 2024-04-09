package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.learnings.application_name.model.Movie;
import org.learnings.application_name.model.Person;
import org.learnings.application_name.model.WatchedRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith({Neo4jHarnessExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class FunctionalEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    private final Set<WatchedRelationship> watchedRelationships =
            Set.of(new WatchedRelationship(1L, new Movie("The Matrix", "desc"), (short) 2));
    private final Person expectedPerson = new Person(1001L, "first one", 1989, watchedRelationships);

    @Test
    void getAllPersons() throws Exception {
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("first one")))
                .andExpect(content().string(containsString("second one")))
                .andExpect(content().string(containsString("third one")));
    }

    @Test
    void getPersonByName_whenResourceExists() throws Exception {
        mockMvc.perform(get("/persons/" + expectedPerson.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(expectedPerson.getBorn()))));
    }

    @Test
    void getPersonByName_whenNoResourceWithThisID() throws Exception {
        mockMvc.perform(get("/persons/no user found"))
                .andExpect(status().isOk())
                .andExpect(content().string(blankOrNullString()));
    }

    @Test
    void getMoviesWatchedByPersonName_returnsListOfMovies() throws Exception {
        String expectedMovieTitle = watchedRelationships.stream()
                .filter(e -> e.getId() == 1L)
                .findFirst().get()
                .getMovie().getTitle();

        mockMvc.perform(get("/persons/" + expectedPerson.getName() + "/movies/watched"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"" + expectedMovieTitle + "\"]"));
    }

    @Test
    void getPersonMoviesWatchedByNameWithRatings_returnsListOfMovies() throws Exception {
        WatchedRelationship movieWithRating = watchedRelationships.stream()
                .filter(e -> e.getId() == 1L)
                .findFirst().get();
        String expectedMovieTitle = movieWithRating.getMovie().getTitle();
        short expectedRating = movieWithRating.getRating();

        mockMvc.perform(get("/persons/" + expectedPerson.getName() + "/movies/watched/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"" + expectedMovieTitle + "\":" + expectedRating + "}"));
    }

    @Test
    void savePerson() throws Exception {
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + expectedPerson.getName() + "\",\"born\":" + expectedPerson.getBorn() + "}")
        ).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValuesForMessageBody")
    void savePerson_shouldFail_forBlankValues(String content) throws Exception {
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidValuesForMessageBody() {
        return Stream.of(
                // null values
                Arguments.of("{\"name\":\"name1\"}"),
                Arguments.of("{\"born\":2001}"),
                // blank strings
                Arguments.of("{\"name\":\"\",\"born\":2001}"),
                Arguments.of("{\"name\":\" \",\"born\":2001}"),
                Arguments.of("{\"name\":\"\t\",\"born\":2001}"),
                Arguments.of("{\"name\":\"\n\",\"born\":2001}"),
                // non positive int
                Arguments.of("{\"name\":\"name1\",\"born\":-1}"),
                Arguments.of("{\"name\":\"name1\",\"born\":0}"),
                Arguments.of("{\"name\":\"name1\",\"born\":9999999999}")
        );
    }
}
