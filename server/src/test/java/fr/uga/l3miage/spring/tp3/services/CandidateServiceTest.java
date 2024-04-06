package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void testGetCandidateAverage() throws CandidateNotFoundException{
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(129L)
                .name("IHM")
                .weight(6)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid = CandidateEvaluationGridEntity
                .builder()
                .grade(10)
                .examEntity(examEntity)
                .build();

        ExamEntity examEntity1 = ExamEntity
                .builder()
                .id(129L)
                .name("Va")
                .weight(4)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(12)
                .examEntity(examEntity)
                .build();


        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id(1L)
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGrid, candidateEvaluationGrid1))
                .build();

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity);


        assert(candidateService.getCandidateAverage(129L) == 10);
    }
    @Test
    void testGetCandidateAverageNotFound() throws CandidateNotFoundException {

        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);
        assertThrows(CandidateNotFoundRestException.class, () -> candidateService.getCandidateAverage(1L));
    }

}
