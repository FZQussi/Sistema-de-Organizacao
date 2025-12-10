package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthServicetest.class);
    private final TurnoService turnoService = new TurnoService();

    private final UserService userService;
    private Utilizador loggedUser;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    // ============================================================
    // ADICIONAR ESTE MÉTODO (OBRIGATÓRIO PARA OS TEUS TESTES)
    // ============================================================
    protected TurnoService getTurnoService() {
        return turnoService;
    }
    // ============================================================

    public Utilizador login(String username, String password) {

        Utilizador user = userService.getByUsername(username);

        if (user == null) {
            logger.warn("Tentativa de login falhada: utilizador '{}' não encontrado.", username);
            return null;
        }

        if (!PasswordUtils.verify(password, user.getPassword())) {
            logger.warn("Tentativa de login falhada: password incorreta para '{}'.", username);
            return null;
        }

        this.loggedUser = user;

        // REGISTAR ENTRADA SE FOR OPERADOR
        if ("operador".equalsIgnoreCase(user.getTipo())) {
            getTurnoService().registarEntrada(user);
            logger.info("Entrada de turno registada para operador '{}'.", username);
        }

        logger.info("Login bem-sucedido: {} ({})", username, user.getTipo());
        return loggedUser;
    }

    public void logout() {
        if (loggedUser != null) {

            if ("operador".equalsIgnoreCase(loggedUser.getTipo())) {
                getTurnoService().registarSaida(loggedUser);
                logger.info("Saída de turno registada para operador '{}'.", loggedUser.getUsername());
            }

            logger.info("Logout efetuado: {} ({})", loggedUser.getUsername(), loggedUser.getTipo());
            this.loggedUser = null;

        } else {
            logger.warn("Tentativa de logout quando nenhum utilizador está logado.");
        }
    }

    public Utilizador getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    public boolean isGerente() {
        return loggedUser != null && "gerente".equalsIgnoreCase(loggedUser.getTipo());
    }

    public boolean isOperador() {
        return loggedUser != null && "operador".equalsIgnoreCase(loggedUser.getTipo());
    }
}



