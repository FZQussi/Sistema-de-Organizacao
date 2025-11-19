package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

public class UserService {

    private final String filePath = "users.json";
    private List<Utilizador> utilizadores = new ArrayList<>();

    public UserService() {
        loadUsers();
    }

    public void addUser(Utilizador u) {
        utilizadores.add(u);
        saveUsers();
    }

    public void updateUser(String username, Utilizador novosDados) {

        for (int i = 0; i < utilizadores.size(); i++) {
            if (utilizadores.get(i).getUsername().equals(username)) {
                utilizadores.set(i, novosDados);
                saveUsers();
                return;
            }
        }
    }

    public void removeUser(String username) {
        utilizadores.removeIf(u -> u.getUsername().equals(username));
        saveUsers();
    }

    public Utilizador getByUsername(String username) {
        return utilizadores.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst().orElse(null);
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

        if (inicio >= utilizadores.size())
            return Collections.emptyList();

        int fim = Math.min(inicio + tamanho, utilizadores.size());

        return utilizadores.subList(inicio, fim);
    }

    private void loadUsers() {
        try {
            File file = new File(filePath);
            if (!file.exists()) return;

            Reader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Utilizador>>(){}.getType();
            utilizadores = new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(utilizadores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Utilizador> getAllUsers() {
        return utilizadores;
    }
}
