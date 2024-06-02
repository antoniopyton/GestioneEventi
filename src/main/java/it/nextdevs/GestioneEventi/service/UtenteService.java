package it.nextdevs.GestioneEventi.service;

import it.nextdevs.GestioneEventi.DTO.UtenteDto;
import it.nextdevs.GestioneEventi.enums.TipoUtente;
import it.nextdevs.GestioneEventi.exceptions.NotFoundException;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public String salvaUtente(UtenteDto utenteDto) {
        Utente utente = new Utente();
        utente.setNome(utenteDto.getNome());
        utente.setCognome(utenteDto.getCognome());
        utente.setEmail(utenteDto.getEmail());
        utente.setTipoUtente(TipoUtente.UTENTE_CLASSICO);
        utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));

        utenteRepository.save(utente);
        sendMail((utente.getEmail()));

        return "Utente salvato correttamente con id: " + utente.getId();
    }

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Optional<Utente> getUtenteById(int id) {
        return utenteRepository.findById(id);
    }

    public Utente updateUtente(int id, UtenteDto utenteDto) {
        Optional<Utente> utenteOptional = utenteRepository.findById(id);

        if (utenteOptional.isPresent()) {
            Utente utente = utenteOptional.get();
            utente.setNome(utenteDto.getNome());
            utente.setCognome(utenteDto.getCognome());
            utente.setEmail(utenteDto.getEmail());
            utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
            utenteRepository.save(utente);
            return utenteRepository.save(utente);
        } else {
            throw new NotFoundException("Utente con id " + id + " non esiste");
        }
    }

    public String deleteUtente(int id) {
        Optional<Utente> userOptional = utenteRepository.findById(id);

        if (userOptional.isPresent()) {
            Utente utente = userOptional.get();
            utenteRepository.delete(utente);
            return "Utente con id " + id + " correttamente eliminato";
        } else {
            throw new NotFoundException("Utente " + id + " non esiste");
        }
    }

    public Utente getUtenteByEmail(String email) {
        Optional<Utente> userOptional = utenteRepository.findUtenteByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("Utente con email " + email + " non esiste");
        }

    }

    private void sendMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione Utente");
        message.setText("Registrazione Utente avvenuta con successo");

        javaMailSender.send(message);
    }

}

