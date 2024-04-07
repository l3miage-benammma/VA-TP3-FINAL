package fr.uga.l3miage.spring.tp3.exceptions.technical;

import lombok.Getter;


public class StepsNotFoundException extends Exception{
    public StepsNotFoundException(String message){
        super(message);
    }
}