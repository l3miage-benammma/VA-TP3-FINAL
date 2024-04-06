package fr.uga.l3miage.spring.tp3.models;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCenterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TestCenterCode code;
    private  String university;
    private String city ;

    @OneToMany(mappedBy = "testCenterEntity")
    private Set<CandidateEntity> candidateEntities;

    @OneToMany(mappedBy = "testCenterEntity")
    private Set<ExaminerEntity> examinerEntities;
}
