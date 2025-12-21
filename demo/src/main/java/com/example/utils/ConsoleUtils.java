package com.example.utils;

/**
 * Classe utilitária para operações relacionadas com o console.
 */
public class ConsoleUtils {

    /**
     * Limpa o ecrã do console.
     * Funciona de forma diferente dependendo do sistema operativo:
     *  - Windows: executa o comando 'cls' no terminal.
     *  - Linux / MacOS: envia sequências de escape ANSI.
     * Se ocorrer algum erro, usa um fallback imprimindo várias linhas em branco.
     */
    public static void clear() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows: comando cls
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO() // envia a saída diretamente para o console
                        .start()
                        .waitFor(); // espera terminar o comando
            } else {
                // Linux / MacOS: sequências ANSI
                System.out.print("\033[H\033[2J"); // move o cursor para o canto superior e limpa a tela
                System.out.flush();
            }
        } catch (Exception e) {
            // fallback simples: imprime várias linhas em branco
            System.out.println("\n".repeat(50));
        }
    }
}
