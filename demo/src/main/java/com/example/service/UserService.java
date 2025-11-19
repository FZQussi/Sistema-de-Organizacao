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

public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final File file; // ficheiro users.json obtido do FileUtils
    private List<Utilizador> utilizadores = new ArrayList<>();

    public UserService() {
        // Inicializa a pasta e ficheiros se não existirem
        FileUtils.initialize();
        this.file = FileUtils.getUsersFile();
        loadUsers();
    }

    public void addUser(Utilizador u) {
        utilizadores.add(u);
        saveUsers();
        logger.info("Novo utilizador adicionado: {} ({})", u.getUsername(), u.getTipo());
    }

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

    public void removeUser(String username) {
        boolean removed = utilizadores.removeIf(u -> u.getUsername().equals(username));
        saveUsers();
        if (removed) {
            logger.info("Utilizador removido: {}", username);
        } else {
            logger.warn("Tentativa de remover utilizador inexistente: {}", username);
        }
    }

    public Utilizador getByUsername(String username) {
        return utilizadores.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // LISTAGEM ORDENADA
    public List<Utilizador> listarOrdenado() {
        return utilizadores.stream()
                .sorted(Comparator.comparing(Utilizador::getUsername))
                .toList();
    }

    // FILTRAR POR TIPO
    public List<Utilizador> listarPorTipo(String tipo) {
        return utilizadores.stream()
                .filter(u -> u.getTipo().equalsIgnoreCase(tipo))
                .sorted(Comparator.comparing(Utilizador::getUsername))
                .toList();
    }

    // PESQUISAR POR NOME
    public List<Utilizador> buscarPorNome(String nome) {
        return utilizadores.stream()
                .filter(u -> (u.getNome() + " " + u.getSobrenome()).toLowerCase()
                        .contains(nome.toLowerCase()))
                .toList();
    }

    // PAGINAÇÃO
    public List<Utilizador> listarPaginado(int pagina, int tamanho) {
        int inicio = pagina * tamanho;

        if (inicio >= utilizadores.size()) {
            return Collections.emptyList();
        }

        int fim = Math.min(inicio + tamanho, utilizadores.size());

        return utilizadores.subList(inicio, fim);
    }

    private void loadUsers() {
        try {
            if (!file.exists() || file.length() == 0) {
                logger.info("Ficheiro '{}' não encontrado ou vazio. Lista de utilizadores inicializada vazia.", file.getAbsolutePath());
                return;
            }

            Reader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Utilizador>>(){}.getType();
            utilizadores = new Gson().fromJson(reader, listType);
            logger.info("Carregados {} utilizadores do ficheiro '{}'.", utilizadores.size(), file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Erro ao carregar utilizadores do ficheiro '{}'.", file.getAbsolutePath(), e);
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(utilizadores, writer);
            logger.info("Lista de utilizadores gravada com sucesso no ficheiro '{}'.", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Erro ao gravar utilizadores no ficheiro '{}'.", file.getAbsolutePath(), e);
        }
    }

    public List<Utilizador> getAllUsers() {
        return utilizadores;
    }
}
