package fr.uga.l3miage.spring.tp3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Pour la conversion d'objets en JSON

    @Test
    void cantCreateSessionWithoutGoodExamID(){
        final HttpHeaders headers = new HttpHeaders();
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest=SessionProgrammationStepCreationRequest
                .builder()
                .code("test")
                .description("description")
                .build();

        Set<SessionProgrammationStepCreationRequest> steps=new HashSet<>();
        steps.add(sessionProgrammationStepCreationRequest);
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest=SessionProgrammationCreationRequest
                .builder()
                .label("test")
                .steps(steps)
                .build();
        final  SessionCreationRequest sessionCreationRequest=SessionCreationRequest
                .builder()
                .name("test")
                .examsId(Set.of((long)1))
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        // when
        ResponseEntity<String> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), String.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void TestCantCreateSessionWithEmptyExamsSet() {
        // Préparation de la requête HTTP
        HttpHeaders headers = new HttpHeaders();
        Set<SessionProgrammationStepCreationRequest> steps=new HashSet<>();
        SessionProgrammationCreationRequest programmation = SessionProgrammationCreationRequest
                .builder()
                .label("Test")
                .steps(steps) // Aucune étape spécifiée pour simplifier
                .build();

        SessionCreationRequest request = SessionCreationRequest
                .builder()
                .name("Session Test Sans Examen")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .examsId(Set.of()) // Ensemble d'examens vide
                .ecosSessionProgrammation(programmation)
                .build();
        //When
        // Envoi de la requête de création de session
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/sessions/create",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                String.class);

        //Then
        // la session se cree malgré que il y a un il y q un set vide d'examens
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}



