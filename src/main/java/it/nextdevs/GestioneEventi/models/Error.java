package it.nextdevs.GestioneEventi.models;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class Error {

    private String messaggio;

    private LocalDateTime dataErrore;

    private HttpStatus statoErrore;

}
