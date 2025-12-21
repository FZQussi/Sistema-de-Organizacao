package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import com.example.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Serviço responsável pela gestão de utilizadores do sistema.
 * Permite adicionar, atualizar, remover, listar, filtrar e
 * persistir utilizadores em ficheiro JSON.
 */
public class UserService {

    // Logger para auditoria e diagnóstico
    private static final Logger logger = LogManager.getLogger(UserService.class);

    // Ficheiro onde os utilizadores são armazenados (users.json)
    private final File file;

    // Lista de utilizadores carregados em memória
    private List<Utilizador> utilizadores = new ArrayList<>();

    /**
     * Construtor do UserService.
     * Inicializa a estrutura de ficheiros e carrega os utilizadores existentes.
     */
    public UserService() {
        FileUtils.initialize();
        this.file = FileUtils.getUsersFile();
        loadUsers();
    }

    /**
     * Adiciona um novo utilizador e grava a lista atualizada.
     */
    public void addUser(Utilizador u) {
        utilizadores.add(u);
        saveUsers();
        logger.info("Novo utilizador adicionado: {} ({})", u.getUsername(), u.getTipo());
    }

    /**
     * Atualiza os dados de um utilizador existente.
     */
    public void updateUser(String username, Utilizador novosDados) {
        boolean found = false;

        for (int i = 0; i < utilizadores.size(); i++) {
            if (utilizadores.get(i).getUsername().equals(username)) {
                utilizadores.set(i, novosDados);
                saveUsers();
                logger.info("Utilizador atualizado: {} ({})", username, novosDados.getTipo());
                found = true;
                break;
            }
        }

        if (!found) {
            logger.warn("Tentativa de atualizar utilizador inexistente: {}", username);
        }
    }

    /**
     * Remove um utilizador pelo username.
     */
    public void removeUser(String username) {
        boolean removed = utilizadores.removeIf(u -> u.getUsername().equals(username));
        saveUsers();

        if (removed) {
            logger.info("Utilizador removido: {}", username);
        } else {
            logger.warn("Tentativa de remover utilizador inexistente: {}", username);
        }
    }

    /**
     * Procura um utilizador pelo username.
     */
    public Utilizador getByUsername(String username) {
        return utilizadores.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lista todos os utilizadores ordenados por username.
     */
    public List<Utilizador> listarOrdenado() {
        return utilizadores.stream()
                .sorted(Comparator.comparing(Utilizador::getUsername))
                .toList();
    }

    /**
     * Lista utilizadores filtrados por tipo (ex.: operador, gerente).
     */
    public List<Utilizador> listarPorTipo(String tipo) {
        return utilizadores.stream()
                .filter(u -> u.getTipo().equalsIgnoreCase(tipo))
                .sorted(Comparator.comparing(Utilizador::getUsername))
                .toList();
    }

    /**
     * Pesquisa utilizadores pelo nome completo (nome + sobrenome).
     */
    public List<Utilizador> buscarPorNome(String nome) {
        return utilizadores.stream()
                .filter(u -> (u.getNome() + " " + u.getSobrenome())
                        .toLowerCase()
                        .contains(nome.toLowerCase()))
                .toList();
    }

    /**
     * Retorna uma lista paginada de utilizadores.
     *
     * @param pagina  índice da página (começa em 0)
     * @param tamanho número de elementos por página
     */
    public List<Utilizador> listarPaginado(int pagina, int tamanho) {
        int inicio = pagina * tamanho;

        if (inicio >= utilizadores.size()) {
            return Collections.emptyList();
        }

        int fim = Math.min(inicio + tamanho, utilizadores.size());
        return utilizadores.subList(inicio, fim);
    }

    /**
     * Carrega os utilizadores a partir do ficheiro JSON.
     */
    private void loadUsers() {
        try {
            if (!file.exists() || file.length() == 0) {
                logger.info(
                        "Ficheiro '{}' não encontrado ou vazio. Lista de utilizadores inicializada vazia.",
                        file.getAbsolutePath()
                );
                return;
            }

            Reader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Utilizador>>() {}.getType();

            utilizadores = new Gson().fromJson(reader, listType);

            logger.info(
                    "Carregados {} utilizadores do ficheiro '{}'.",
                    utilizadores.size(),
                    file.getAbsolutePath()
            );
        } catch (Exception e) {
            logger.error(
                    "Erro ao carregar utilizadores do ficheiro '{}'.",
                    file.getAbsolutePath(),
                    e
            );
        }
    }

    /**
     * Grava a lista de utilizadores no ficheiro JSON.
     */
    private void saveUsers() {
        try (Writer writer = new FileWriter(file)) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(utilizadores, writer);

            logger.info(
                    "Lista de utilizadores gravada com sucesso no ficheiro '{}'.",
                    file.getAbsolutePath()
            );
        } catch (IOException e) {
            logger.error(
                    "Erro ao gravar utilizadores no ficheiro '{}'.",
                    file.getAbsolutePath(),
                    e
            );
        }
    }

    /**
     * Retorna todos os utilizadores carregados em memória.
     */
    public List<Utilizador> getAllUsers() {
        return utilizadores;
    }
}
