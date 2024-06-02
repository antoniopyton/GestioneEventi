package it.nextdevs.GestioneEventi.controller;

import it.nextdevs.GestioneEventi.DTO.EventoDto;
import it.nextdevs.GestioneEventi.DTO.UtenteDto;
import it.nextdevs.GestioneEventi.exceptions.BadRequestException;
import it.nextdevs.GestioneEventi.exceptions.NotFoundException;
import it.nextdevs.GestioneEventi.models.Evento;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public String salvaEvento(@RequestBody @Validated EventoDto eventoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2 ));
        }
        return eventoService.salvaEvento(eventoDto);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public List<Evento> getAllUtenti() {
        return eventoService.getAllEventi();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public Evento getEventoByID(int id) {
        Optional<Evento> eventoOptional = eventoService.getEventoByID(id);

        if (eventoOptional.isPresent()) {
            return eventoOptional.get();
        } else {
            throw new NotFoundException("Evento non esiste con id: " + id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento updateEvento(@PathVariable int id, @RequestBody @Validated EventoDto eventoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2 ));
        }

        return eventoService.updateEvento(id, eventoDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public String deleteEvento(@PathVariable int id) {
        return eventoService.deleteEvento(id);
    }

    @PostMapping("/prenotazioni/{eventoId}/{utenteId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public String nuovaPrenotazione(@PathVariable int eventoId, @PathVariable int utenteId) {
        return eventoService.nuovaPrenotazione(eventoId, utenteId);
    }


    @DeleteMapping("/prenotazioni/{eventoId}/{utenteId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public String eliminaPrenotazione(@PathVariable int eventoId, @PathVariable int utenteId) {
        return eventoService.eliminaPrenotazione(eventoId, utenteId);
    }

}
