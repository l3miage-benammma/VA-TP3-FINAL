package fr.uga.l3miage.spring.tp3.repositories;


import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static fr.uga.l3miage.spring.tp3.enums.TestCenterCode.GRE;
import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @BeforeEach
    public void clear(){
        candidateEvaluationGridRepository.deleteAll();
        candidateRepository.deleteAll();
        testCenterRepository.deleteAll();
    }
        @Test
        void testFindAllByTestCenterEntityCode(){
            //given
            TestCenterEntity test1 = TestCenterEntity
                    .builder()
                    .code(GRE)
                    .build();

            TestCenterEntity test2 = TestCenterEntity
                    .builder()
                    .code(TestCenterCode.DIJ)
                    .build();

            CandidateEntity candidate1 = CandidateEntity
                    .builder()
                    .firstname("alexandre")
                    .email("alexandre@etu.uni-grenoble-alpes.fr")
                    .testCenterEntity(test1)
                    .build();

            CandidateEntity candidate2 = CandidateEntity
                    .builder()
                    .firstname("Nicolas")
                    .email("nicolas@etu.uni-grenoble-alpes.fr")
                    .testCenterEntity(test2)
                    .build();



            testCenterRepository.save(test1);
            testCenterRepository.save(test2);

            candidateRepository.save(candidate1);
            candidateRepository.save(candidate2);


            // when
            Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(GRE);

            //then
            assertThat(candidateEntitiesResponses).hasSize(1);
            assertThat(candidateEntitiesResponses.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(GRE);
        }

    @Test
    void testFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        //given
        TestCenterEntity test1 = TestCenterEntity
                .builder()
                .code(GRE)
                .build();

        testCenterRepository.save(test1);

        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .firstname("Momo")
                .email("momo@gmail.com")
                .testCenterEntity(test1)
                .build();

        candidateRepository.save(candidate1);

        CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity
                .builder()
                .candidateEntity(candidate1)
                .grade(15.5)
                .build();

        candidateEvaluationGridRepository.save(candidateEvaluationGrid1);

        candidate1.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGrid1));
        candidateRepository.save(candidate1);

        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(8);
        Set<CandidateEntity> candidateEntitiesNoResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10);


        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getCandidateEvaluationGridEntities().stream().findFirst().get().getGrade()).isEqualTo(15.5);
        assertThat(candidateEntitiesNoResponses).hasSize(0);
    }

        @Test
        void testFindAllByHasExtraTimeFalseAndBirthDateBefore(){

            CandidateEntity candidate1 = CandidateEntity
                    .builder()
                    .firstname("alexandre")
                    .email("alexandre@etu.univ-grenoble-alpes.fr")
                    .birthDate(LocalDate.of(1989,2,3))
                    .hasExtraTime(false)
                    .build();

            CandidateEntity candidate2 = CandidateEntity
                    .builder()
                    .firstname("Nicolas")
                    .email("nicolas@etu.univ-grenoble-alpes.fr")
                    .birthDate(LocalDate.of(2000,2,11))
                    .hasExtraTime(false)
                    .build();

            candidateRepository.save(candidate1);
            candidateRepository.save(candidate2);

            // when
            Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2000, 9, 1));
            Set<CandidateEntity> noCandidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(1989, 1, 1));

            //then
            assertThat(candidateEntitiesResponses).hasSize(1);
            assertThat(candidateEntitiesResponses.stream().findFirst().get().getBirthDate()).isBefore(LocalDate.of(2000, 9, 1));
            assertThat(noCandidateEntitiesResponses).hasSize(0);
    }


    }
