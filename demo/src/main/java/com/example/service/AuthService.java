package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;

public class AuthService {

    private UserService userService;
    private Utilizador loggedUser;

    public AuthService(UserService userService) {
        this.userService = userService;
        this.loggedUser = null;
    }

    /**
     * Tenta fazer login com username e password
     * @param username
     * @param password
     * @return Utilizador logado se sucesso, null se falhar
     */
    public Utilizador login(String username, String password) {
        Utilizador u = userService.login(username, password);
        if (u != null) {
            loggedUser = u;
        }
        return loggedUser;
    }

    /**
     * Logout do utilizador
     */
    public void logout() {
        if (loggedUser != null) {
            System.out.println("Logout: " + loggedUser.getUsername());
            loggedUser = null;
        }
    }

    /**
     * Retorna o utilizador logado atualmente
     * @return Utilizador logado ou null
     */
    public Utilizador getLoggedUser() {
        return loggedUser;
    }

    /**
     * Verifica se o utilizador logado é gerente
     * @return true se gerente, false caso contrário
     */
    public boolean isGerente() {
        return loggedUser != null && loggedUser.getTipo().equalsIgnoreCase("gerente");
    }
}

