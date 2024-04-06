package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.*;
import fr.uga.l3miage.spring.tp3.exceptions.technical.*;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void candidateNotFound() {
        // Given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then - When
        assertThrows(CandidateNotFoundException.class, () -> candidateComponent.getCandidatById(2147483647L));

        // Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("alexandre")
                .email("alexandre@gmail.com")
                .build();


        CandidateEvaluationGridEntity evaluationGridEntity = CandidateEvaluationGridEntity.builder().candidateEntity(candidateEntity).grade(5.0).build();
        Set<CandidateEvaluationGridEntity> eliminatedCandidates = Set.of(evaluationGridEntity);


        when(candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)).thenReturn(eliminatedCandidates);

        // When
        Set<CandidateEntity> result = candidateComponent.getAllEliminatedCandidate();

        // Then
        assertDoesNotThrow(() -> candidateComponent.getAllEliminatedCandidate());
        assertTrue(result.contains(candidateEntity));
    }




}