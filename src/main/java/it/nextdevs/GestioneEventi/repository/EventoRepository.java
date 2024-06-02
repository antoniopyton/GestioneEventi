package it.nextdevs.GestioneEventi.repository;

import it.nextdevs.GestioneEventi.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
}
