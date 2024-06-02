package it.nextdevs.GestioneEventi.security;

import it.nextdevs.GestioneEventi.exceptions.NotFoundException;
import it.nextdevs.GestioneEventi.exceptions.UnauthorizedException;
import it.nextdevs.GestioneEventi.models.Utente;
import it.nextdevs.GestioneEventi.repository.UtenteRepository;
import it.nextdevs.GestioneEventi.service.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UtenteService utenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Sono qui");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Errore in authorization, token mancante!");
        }

        String token = authHeader.substring(7);

        jwtTool.verifyToken(token);

        int userId = jwtTool.getIdFromToken(token);

        Optional <Utente> utenteOptional = utenteService.getUtenteById(userId);

        if(utenteOptional.isPresent()) {
            Utente user = utenteOptional.get();

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new NotFoundException("Utente non trovato con id " + userId);
        }


        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }

}
