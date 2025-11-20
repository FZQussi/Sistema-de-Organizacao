package com.example.app.menu.login;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuLogin {

    private static final Logger logger = LogManager.getLogger(MenuLogin.class);
    private final AuthService auth;
    private final Scanner sc = new Scanner(System.in);

    public MenuLogin(AuthService auth) {
        this.auth = auth;
    }

    public Utilizador mostrar() {
        while (true) {
            System.out.print("Username: ");
            String username = sc.nextLine().trim();

            System.out.print("Password: ");
            String password = sc.nextLine();

            Utilizador user = auth.login(username, password);

            if (user != null) {
                logger.info("Login efetuado: {}", username);
                return user;
            }

            System.out.println("Credenciais inválidas. Tente novamente.\n");
            logger.warn("Tentativa falhada de login: {}", username);
        }
    }
}
