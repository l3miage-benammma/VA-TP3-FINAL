package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @MockBean
    private CandidateEvaluationGridEntity candidateEvaluationGridEntity;


    @Test
    void testGetCandidateAverage() throws CandidateNotFoundException{
        ExamEntity IHM= ExamEntity.builder()
                .weight(2)
                .build();
        ExamEntity VA= ExamEntity.builder()
                .weight(1)
                .build();

        CandidateEntity candidate = CandidateEntity
                .builder()
                .lastname("Nicolas")
                .email("nicolas@gmail.com")
                .build();

        CandidateEvaluationGridEntity grid1=CandidateEvaluationGridEntity
                .builder()
                .grade(10.0)
                .examEntity(IHM)
                .candidateEntity(candidate)
                .build();

        CandidateEvaluationGridEntity grid2=CandidateEvaluationGridEntity
                .builder()
                .grade(17.0)
                .examEntity(VA)
                .candidateEntity(candidate)
                .build();

        // ajouter les evaluations au candidat
        candidate.setCandidateEvaluationGridEntities(Set.of(grid1, grid2));
        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidate);

        // when
        double response= candidateService.getCandidateAverage((long)888);
        double expectedAverage = ((grid1.getGrade() * grid1.getExamEntity().getWeight()) +
                (grid2.getGrade() * grid2.getExamEntity().getWeight())) /
                (grid1.getExamEntity().getWeight() + grid2.getExamEntity().getWeight());
        //then
        assertThat(response).isEqualTo(expectedAverage);

    }
    @Test
    void testGetCandidateAverageNotFound() throws CandidateNotFoundException {

        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);
        assertThrows(CandidateNotFoundRestException.class, () -> candidateService.getCandidateAverage(anyLong()));
    }

}
