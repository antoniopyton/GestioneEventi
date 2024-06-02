package it.nextdevs.GestioneEventi.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UtenteDto {

    private String nome;

    private String cognome;

    @Email(message = "L'email non può essere vuota o mancante o composta da soli spazi")
    @NotBlank
    private String email;

    @NotBlank(message = "La password non può essere vuota o mancante o composta da soli spazi")
    private String password;

}
