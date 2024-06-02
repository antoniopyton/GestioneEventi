package it.nextdevs.GestioneEventi.controller;

import it.nextdevs.GestioneEventi.DTO.UtenteDto;
import it.nextdevs.GestioneEventi.exceptions.BadRequestException;
import it.nextdevs.GestioneEventi.exceptions.NotFoundException;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/utenti")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public List<Utente> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/utenti/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE_CLASSICO')")
    public Utente getUtenteById(int id) {
        Optional<Utente> utenteOptional = utenteService.getUtenteById(id);

        if (utenteOptional.isPresent()) {
            return utenteOptional.get();
        } else {
            throw new NotFoundException("Utente con non esiste con id: " + id);
        }
    }

    @PutMapping("/utenti/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Utente updateUtente(@PathVariable int id, @RequestBody @Validated UtenteDto utenteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2 ));
        }

        return utenteService.updateUtente(id, utenteDto);
    }

    @DeleteMapping("/utenti/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public String deleteUtente(@PathVariable int id) {
        return utenteService.deleteUtente(id);
    }


}
