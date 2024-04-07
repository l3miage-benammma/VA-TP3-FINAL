package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)

public class ExamComponentTest {
    @Autowired
    private ExamComponent examComponent;

    @MockBean
    private ExamRepository examRepository;

    @Test
    void testGetExamEntityByIdNotFound(){
        when(examRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ExamNotFoundException.class,()->examComponent.getAllById(Set.of(69L)));
    }

    @Test
    void testGetExamEntityByIdFound(){
        ExamEntity examEntity = ExamEntity.builder()
                .id(69L)
                .weight(1)
                .name("IHM")
                .build();
        when(examRepository.findAllById(anySet())).thenReturn(List.of(examEntity));
        assertDoesNotThrow(()->examComponent.getAllById(Set.of(69L)));
    }
}