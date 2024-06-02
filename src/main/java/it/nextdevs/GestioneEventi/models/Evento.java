package it.nextdevs.GestioneEventi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Evento {

    @Id
    @GeneratedValue
    private int id;

    private String titolo;

    private String descrizione;

    private LocalDate data;

    private String luogo;

    private int postiDisponibili;

    @ManyToMany(mappedBy = "eventiPrenotati")
    private List<Utente> utentiPrenotati = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private Utente organizzatore;

}
