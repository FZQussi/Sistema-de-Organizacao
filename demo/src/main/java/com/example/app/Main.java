package com.example.app;

// Importa o menu principal da aplicação
import com.example.app.menu.MenuPrincipal;

// Utilitário responsável por operações com ficheiros e diretórios
import com.example.utils.FileUtils;

// Bibliotecas do Log4j para registo de logs
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe principal da aplicação.
 * Contém o método main, ponto de entrada do programa.
 */
public class Main {

    // Logger associado à classe Main para registo de eventos e erros
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Método principal da aplicação.
     * É executado quando o programa é iniciado.
     *
     * @param args argumentos passados pela linha de comandos (não utilizados)
     */
    public static void main(String[] args) {

        // Inicializa a estrutura de pastas e ficheiros necessários ao funcionamento do sistema
        try {
            // Garante que a pasta SistemaOrganizacaoData e os ficheiros obrigatórios existem
            FileUtils.initialize();
            logger.info("Estrutura de ficheiros inicializada com sucesso.");
        } catch (Exception e) {
            // Regista erro crítico e interrompe a execução da aplicação
            logger.error("Erro ao inicializar estrutura de ficheiros.", e);
            return; // Encerra a aplicação caso a inicialização falhe
        }

        // Regista o início da aplicação
        logger.info("Iniciando a aplicação Sistema de Gestão.");

        try {
            // Cria e inicia o menu principal da aplicação
            new MenuPrincipal().iniciar();
        } catch (Exception e) {
            // Captura qualquer erro inesperado ocorrido durante a execução
            logger.error("Ocorreu um erro inesperado na aplicação.", e);
        } finally {
            // Regista o encerramento da aplicação, independentemente de erro ou sucesso
            logger.info("Aplicação encerrada.");
        }
    }
}
