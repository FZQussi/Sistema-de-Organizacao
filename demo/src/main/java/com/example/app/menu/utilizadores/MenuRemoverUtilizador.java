package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuRemoverUtilizador {

    private static final Logger logger = LogManager.getLogger(MenuRemoverUtilizador.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    public MenuRemoverUtilizador(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {

        // Limpar consola ao entrar no menu
        ConsoleUtils.clear();
        System.out.println("\n=== Remover Utilizador ===");
        System.out.println("Digite '0' para cancelar e voltar ao menu anterior.\n");

        System.out.print("Username do utilizador a remover: ");
        String username = sc.nextLine();

        if (username.equals("0")) {
            System.out.println("Ação cancelada. Voltando ao menu anterior.");
            return;
        }

        Utilizador u = userService.getByUsername(username);

        if (u == null) {
            System.out.println("❌ Utilizador não encontrado.");
            logger.warn("Tentativa de remover utilizador inexistente: {}", username);
            return;
        }

        System.out.println("\nEncontrado:");
        System.out.println(" - Nome: " + u.getNome() + " " + u.getSobrenome());
        System.out.println(" - Tipo: " + u.getTipo());

        System.out.print("\nTem a certeza que deseja remover? (s/n ou 0 para cancelar): ");
        String confirm = sc.nextLine().toLowerCase();

        if (!confirm.equals("s")) {
            System.out.println("❌ Ação cancelada.");
            logger.info("Remoção cancelada pelo utilizador para '{}'", username);
            return;
        }

        userService.removeUser(username);
        System.out.println("✔ Utilizador removido com sucesso!");
        logger.info("Utilizador removido: {}", username);
    }
}
