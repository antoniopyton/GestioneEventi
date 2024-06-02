package it.nextdevs.GestioneEventi.repository;

import it.nextdevs.GestioneEventi.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    public Optional<Utente> findUtenteByEmail(String email);
}
