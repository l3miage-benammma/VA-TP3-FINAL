package fr.uga.l3miage.spring.tp3.exceptions.technical;

import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.Getter;

@Getter
public class StatusActuelFauxException extends  Exception{
    private final SessionStatus status;

    public StatusActuelFauxException(String message,SessionStatus status){
        super(message); this.status=status;
    }
}