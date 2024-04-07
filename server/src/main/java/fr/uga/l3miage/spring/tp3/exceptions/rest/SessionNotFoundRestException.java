package fr.uga.l3miage.spring.tp3.exceptions.rest;

import lombok.Getter;


public class SessionNotFoundRestException extends RuntimeException{


    public SessionNotFoundRestException(String message) {
        super(message);

    }
}