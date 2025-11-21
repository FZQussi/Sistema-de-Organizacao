package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final UserService userService;
    private Utilizador loggedUser;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Tenta autenticar o utilizador com username e password.
     * Retorna o Utilizador logado ou null se falhar.
     */
    public Utilizador login(String username, String password) {

        Utilizador user = userService.getByUsername(username);

        if (user == null) {
            logger.warn("Tentativa de login falhada: utilizador '{}' não encontrado.", username);
            return null; // Utilizador não existe
        }

        // Verificação segura com bcrypt
        if (!PasswordUtils.verify(password, user.getPassword())) {
            logger.warn("Tentativa de login falhada: password incorreta para utilizador '{}'.", username);
            return null; // Password incorreta
        }

        this.loggedUser = user;
        logger.info("Login bem-sucedido: {} ({})", username, user.getTipo());
        return loggedUser;
    }

    /**
     * Termina sessão do utilizador atual.
     */
    public void logout() {
        if (loggedUser != null) {
            logger.info("Logout efetuado: {} ({})", loggedUser.getUsername(), loggedUser.getTipo());
            this.loggedUser = null;
        } else {
            logger.warn("Tentativa de logout quando nenhum utilizador está logado.");
        }
    }

    /**
     * Retorna o utilizador atualmente logado.
     */
    public Utilizador getLoggedUser() {
        return loggedUser;
    }

    /**
     * Verifica se algum utilizador está logado.
     */
    public boolean isLogged() {
        return loggedUser != null;
    }

    /**
     * Verifica se o utilizador logado é do tipo gerente.
     */
    public boolean isGerente() {
        return loggedUser != null && "gerente".equalsIgnoreCase(loggedUser.getTipo());
    }

    /**
     * Verifica se o utilizador logado é do tipo operador.
     */
    public boolean isOperador() {
        return loggedUser != null && "operador".equalsIgnoreCase(loggedUser.getTipo());
    }
}


Adicionar sistemaa de Turnoos