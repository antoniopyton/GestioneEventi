package it.nextdevs.GestioneEventi.service;

import it.nextdevs.GestioneEventi.DTO.EventoDto;
import it.nextdevs.GestioneEventi.exceptions.NotFoundException;
import it.nextdevs.GestioneEventi.models.Evento;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.repository.EventoRepository;
import it.nextdevs.GestioneEventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public String salvaEvento(EventoDto eventoDto) {
        Evento evento = new Evento();
        evento.setTitolo(eventoDto.getTitolo());
        evento.setDescrizione(eventoDto.getDescrizione());
        evento.setData(eventoDto.getData());
        evento.setLuogo(eventoDto.getLuogo());
        evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
        eventoRepository.save(evento);

        return "Evento salvato correttamente con id: " + evento.getId();
    }

    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> getEventoByID(int id) {
        return eventoRepository.findById(id);
    }

    public Evento updateEvento(int id, EventoDto eventoDto) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);

        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setTitolo(eventoDto.getTitolo());
            evento.setDescrizione(eventoDto.getDescrizione());
            evento.setData(eventoDto.getData());
            evento.setLuogo(eventoDto.getLuogo());
            evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
            return eventoRepository.save(evento);
        } else {
            throw new NotFoundException("Evento non trovato con id " + id);
        }
    }

    public String deleteEvento(int id) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);

        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            eventoRepository.delete(evento);
            return "Evento con id " + id + " correttamente eliminato";
        } else {
            throw new NotFoundException("Evento " + id + " non esiste");
        }
    }

    public String nuovaPrenotazione(int eventoId, int utenteId) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        Utente utente = utenteRepository.findById(utenteId).orElseThrow(() -> new NotFoundException("Utente non trovato"));

        if (evento.getPostiDisponibili() > 0) {
            List<Evento> eventiPrenotati = utente.getEventiPrenotati();
            eventiPrenotati.add(evento);
            utente.setEventiPrenotati(eventiPrenotati);
            evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
            eventoRepository.save(evento);
            sendMailEvento(utente.getEmail());
            return "Prenotazione effettuata con successo!";
        } else {
            throw new IllegalStateException("Non ci sono posti disponibili.");
        }
    }

    public String eliminaPrenotazione(int eventoId, int utenteId) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        Utente utente = utenteRepository.findById(utenteId).orElseThrow(() -> new NotFoundException("Utente non trovato"));

        if (evento.getUtentiPrenotati().contains(utente)) {
            evento.getUtentiPrenotati().remove(utente);
            evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
            eventoRepository.save(evento);
            sendMailDisdetta(utente.getEmail());
            return "Prenotazione rimossa con successo!";
        } else {
            throw new IllegalStateException("L'utente non ha alcuna prenotazione.");
        }
    }

    private void sendMailEvento(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Prenotazione Evento");
        message.setText("Prenotazione all'evento avvenuta con successo");

        javaMailSender.send(message);
    }

    private void sendMailDisdetta(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Prenotazione Cancellata con successo");
        message.setText("Prenotazione all'evento cancellata con successo");

        javaMailSender.send(message);
    }
}

