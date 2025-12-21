package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Serviço responsável pela autenticação de utilizadores.
 * Controla login, logout, utilizador autenticado
 * e integra o registo de turnos para operadores.
 */
public class AuthService {

    // Logger para registo de eventos de autenticação
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    // Serviço responsável pela gestão de turnos
    private final TurnoService turnoService = new TurnoService();

    // Serviço de gestão de utilizadores
    private final UserService userService;

    // Utilizador atualmente autenticado no sistema
    private Utilizador loggedUser;

    /**
     * Construtor do AuthService.
     *
     * @param userService serviço de acesso aos utilizadores
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    // ============================================================
    // Método protegido para acesso ao TurnoService
    // Necessário para testes unitários
    // ============================================================
    protected TurnoService getTurnoService() {
        return turnoService;
    }
    // ============================================================

    /**
     * Realiza o login de um utilizador no sistema.
     *
     * @param username nome de utilizador
     * @param password password em texto simples (validada por hash)
     * @return utilizador autenticado ou null em caso de falha
     */
    public Utilizador login(String username, String password) {

        // Procura o utilizador pelo username
        Utilizador user = userService.getByUsername(username);

        // Verifica se o utilizador existe
        if (user == null) {
            logger.warn("Tentativa de login falhada: utilizador '{}' não encontrado.", username);
            return null;
        }

        // Valida a password utilizando hash
        if (!PasswordUtils.verify(password, user.getPassword())) {
            logger.warn("Tentativa de login falhada: password incorreta para '{}'.", username);
            return null;
        }

        // Define o utilizador autenticado
        this.loggedUser = user;

        // Regista a entrada de turno caso o utilizador seja operador
        if ("operador".equalsIgnoreCase(user.getTipo())) {
            getTurnoService().registarEntrada(user);
            logger.info("Entrada de turno registada para operador '{}'.", username);
        }

        // Regista o sucesso do login
        logger.info("Login bem-sucedido: {} ({})", username, user.getTipo());
        return loggedUser;
    }

    /**
     * Realiza o logout do utilizador atualmente autenticado.
     * Caso seja operador, regista automaticamente a saída do turno.
     */
    public void logout() {
        if (loggedUser != null) {

            // Regista a saída de turno se for operador
            if ("operador".equalsIgnoreCase(loggedUser.getTipo())) {
                getTurnoService().registarSaida(loggedUser);
                logger.info("Saída de turno registada para operador '{}'.", loggedUser.getUsername());
            }

            // Regista o logout
            logger.info("Logout efetuado: {} ({})", loggedUser.getUsername(), loggedUser.getTipo());

            // Remove o utilizador autenticado
            this.loggedUser = null;

        } else {
            // Tentativa de logout sem utilizador autenticado
            logger.warn("Tentativa de logout quando nenhum utilizador está logado.");
        }
    }

    /**
     * Retorna o utilizador atualmente autenticado.
     */
    public Utilizador getLoggedUser() {
        return loggedUser;
    }

    /**
     * Indica se existe um utilizador autenticado.
     */
    public boolean isLogged() {
        return loggedUser != null;
    }

    /**
     * Verifica se o utilizador autenticado é gerente.
     */
    public boolean isGerente() {
        return loggedUser != null && "gerente".equalsIgnoreCase(loggedUser.getTipo());
    }

    /**
     * Verifica se o utilizador autenticado é operador.
     */
    public boolean isOperador() {
        return loggedUser != null && "operador".equalsIgnoreCase(loggedUser.getTipo());
    }
}
