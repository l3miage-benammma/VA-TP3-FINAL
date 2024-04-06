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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void getAllEliminatedCandidateIsEmpty() {
        //given
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(new HashSet<>());

        //when
        Set<CandidateEntity> response = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(4.0);

        //then
        assertThat(response).isEmpty();
    }
    @Test
    void getAllEliminatedCandidateIsNotEmpty() {
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Nicolas")
                .email("nicolas@gmail.com")
                .build();


        Set<CandidateEntity> candidate1Set = Set.of(candidate1);
        //given
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(candidate1Set);

        //when
        Set<CandidateEntity> response = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5.0);

        //then
        assertThat(response).isNotEmpty();
    }
    @Test
    void getCandidateByIdNotFound() {
        //given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Then - When
        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById(anyLong()));
    }


    @Test
    void getCandidateByIdFound() {
        //given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Nicolas")
                .email("nicolas@gmail.com")
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidate1));
        assertDoesNotThrow(()->candidateComponent.getCandidatById(anyLong()));

    }




}