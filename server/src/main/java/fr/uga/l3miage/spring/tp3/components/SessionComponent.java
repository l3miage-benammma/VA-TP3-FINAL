package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StatusActuelFauxException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionComponent {
    private final EcosSessionRepository ecosSessionRepository;
    private final EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    private final EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;


    public EcosSessionEntity createSession(EcosSessionEntity entity){
        ecosSessionProgrammationStepRepository.saveAll(entity.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities());
        ecosSessionProgrammationRepository.save(entity.getEcosSessionProgrammationEntity());
        return ecosSessionRepository.save(entity);
    }

    public EcosSessionEntity getSessionById(Long sessionId) throws SessionNotFoundException {
        return ecosSessionRepository.findById(sessionId).orElseThrow(()-> new SessionNotFoundException(String.format("La session [%s] n'a pas été trouvée",sessionId),sessionId));
    }

    //voir si le l'évaluation a commencé
    public void isStatusEvalStarted(EcosSessionEntity session) throws StatusActuelFauxException {

        if (!(session.getStatus() == SessionStatus.EVAL_STARTED)) {
            throw new StatusActuelFauxException("L'état précédent de la session n'est pas 'EVAL_STARTED' ! > ",session.getStatus());
        }
    }



}
