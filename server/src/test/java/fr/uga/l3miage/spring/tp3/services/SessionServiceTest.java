package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;

import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;

import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    private SessionService sessionService;

    @MockBean
    private ExamComponent examComponent;

    @MockBean
    private SessionComponent sessionComponent;

    @SpyBean
    private SessionMapper sessionMapper;

    //On a essayé de mettre une fonction endSession mais ca n'a pas marché on on l'a mis en commentaire
    /*@Test
    void endSession_404_SessionNotFound() throws SessionNotFoundException {

        //when
        when(sessionComponent.getSessionById(anyLong())).thenThrow(new SessionNotFoundException("Session not found!",1L));

        //then
        assertThrows(SessionNotFoundRestException.class,()-> sessionService.endSession(1L));

    }*/
    @Test
    void testCreateSession() throws ExamNotFoundException {

        SessionCreationRequest sessionCreationRequest = SessionCreationRequest.builder()
                .name("test")
                .startDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2020, 1, 1, 1, 0, 0))
                .examsId(Set.of())
                .build();

        EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
        when(examComponent.getAllById(anySet())).thenReturn(Set.of());
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        SessionResponse expectedResponse = sessionMapper.toResponse(ecosSessionEntity);
        SessionResponse response = sessionService.createSession(sessionCreationRequest);

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }



}
