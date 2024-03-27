package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.learnings.application_name.infrastructure.repositories.PersonRepository;
import org.learnings.application_name.model.Person;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class FunctionalEndpointTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonRepository repository;

    private final Person expectedPerson = new Person(1001L, "first one", 1989);
    private final List<Person> dataSource = List.of(
            expectedPerson,
            new Person(1002L, "second one", 1999),
            new Person(1003L, "third one", 1980)
    );

    @Test
    void getAllPersons() throws Exception {
        when(repository.findAll()).thenReturn(dataSource);

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(blankOrNullString())));
    }

    @Test
    void getPersonByName_whenResourceExists() throws Exception {
        when(repository.findByName(expectedPerson.getName())).thenReturn(Optional.of(expectedPerson));

        mockMvc.perform(get("/persons/" + expectedPerson.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(expectedPerson.getBorn()))));
    }

    @Test
    void getPersonByName_whenNoResourceWithThisID() throws Exception {
        when(repository.findByName("no user found")).thenReturn(Optional.empty());

        mockMvc.perform(get("/persons/no user found"))
                .andExpect(status().isOk())
                .andExpect(content().string(blankOrNullString()));
    }

    @Test
    void savePerson() throws Exception {
        when(repository.findByName(expectedPerson.getName())).thenReturn(Optional.empty());
        when(repository.save(expectedPerson)).thenReturn(expectedPerson);

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
