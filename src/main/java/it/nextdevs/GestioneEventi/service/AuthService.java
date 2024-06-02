package it.nextdevs.GestioneEventi.service;

import it.nextdevs.GestioneEventi.DTO.UtenteLoginDto;
import it.nextdevs.GestioneEventi.exceptions.UnauthorizedException;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUserAndCreateToken(UtenteLoginDto utenteLoginDto) {
        Utente utente = utenteService.getUtenteByEmail(utenteLoginDto.getEmail());

        if (passwordEncoder.matches(utenteLoginDto.getPassword(), utente.getPassword())) {
            return jwtTool.createToken(utente);
        } else {
            throw new UnauthorizedException("Errore in authorization, re-login!");
        }
    }

}
