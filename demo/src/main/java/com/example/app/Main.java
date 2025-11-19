package com.example.app;

import com.example.app.menu.MenuPrincipal;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        // Inicializa pastas e ficheiros necessários
        try {
            FileUtils.initialize(); // garante que a pasta SistemaOrganizacaoData e ficheiros existem
            logger.info("Estrutura de ficheiros inicializada com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao inicializar estrutura de ficheiros.", e);
            return; // interrompe a aplicação se não for possível criar a pasta/ficheiros
        }

        logger.info("Iniciando a aplicação Sistema de Gestão.");

        try {
            new MenuPrincipal().iniciar();
        } catch (Exception e) {
            logger.error("Ocorreu um erro inesperado na aplicação.", e);
        } finally {
            logger.info("Aplicação encerrada.");
        }
    }
}
