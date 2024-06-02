package it.nextdevs.GestioneEventi.controller;

import it.nextdevs.GestioneEventi.DTO.UtenteDto;
import it.nextdevs.GestioneEventi.DTO.UtenteLoginDto;
import it.nextdevs.GestioneEventi.exceptions.BadRequestException;
import it.nextdevs.GestioneEventi.service.AuthService;
import it.nextdevs.GestioneEventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UtenteService utenteService;


    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String salvaUtente(@RequestBody @Validated UtenteDto utenteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2 ));
        }
        return utenteService.salvaUtente(utenteDto);
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated UtenteLoginDto utenteLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2 ));
        }

        return authService.authenticateUserAndCreateToken(utenteLoginDto);
    }

}
