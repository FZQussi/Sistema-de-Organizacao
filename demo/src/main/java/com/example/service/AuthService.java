package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;

public class AuthService {

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
            return null; // Utilizador não existe
        }

        // Verificação segura com bcrypt
        if (!PasswordUtils.verify(password, user.getPassword())) {
            return null; // Password incorreta
        }

        this.loggedUser = user;
        return loggedUser;
    }

    /**
     * Termina sessão do utilizador atual.
     */
    public void logout() {
        this.loggedUser = null;
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

