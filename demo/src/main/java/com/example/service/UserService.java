package com.example.service;

import com.example.model.Utilizador;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<Utilizador> utilizadores;
    private final String filePath = "users.json";

    // GSON com Pretty Printing (JSON bonito!)
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserService() {
        this.utilizadores = new ArrayList<>();
        loadUsers();
    }

    // --------------------------------------------
    //               CRUD UTILIZADORES
    // --------------------------------------------

    public boolean addUser(String username, String password, String tipo) {
        if (exists(username)) {
            System.out.println("Erro: O utilizador já existe!");
            return false;
        }

        utilizadores.add(new Utilizador(username, password, tipo));
        saveUsers();
        System.out.println("Utilizador criado com sucesso.");
        return true;
    }

    public boolean updateUser(String username, String newPassword, String newTipo) {
        Utilizador u = getUser(username);

        if (u == null) {
            System.out.println("Erro: Utilizador não encontrado.");
            return false;
        }

        u.setPassword(newPassword);
        u.setTipo(newTipo);

        saveUsers();
        System.out.println("Utilizador atualizado com sucesso.");
        return true;
    }

    public boolean removeUser(String username) {
        Utilizador u = getUser(username);

        if (u == null) {
            System.out.println("Erro: Utilizador não encontrado.");
            return false;
        }

        utilizadores.remove(u);
        saveUsers();
        System.out.println("Utilizador removido com sucesso.");
        return true;
    }

    // --------------------------------------------
    //             MÉTODOS DE SUPORTE
    // --------------------------------------------

    public Utilizador getUser(String username) {
        for (Utilizador u : utilizadores) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean exists(String username) {
        return getUser(username) != null;
    }

    public List<Utilizador> getAllUsers() {
        return new ArrayList<>(utilizadores); // Protege a lista original
    }

    // --------------------------------------------
    //              JSON (LOAD & SAVE)
    // --------------------------------------------

    private void loadUsers() {
        try (FileReader reader = new FileReader(filePath)) {

            Type listType = new TypeToken<List<Utilizador>>() {}.getType();
            List<Utilizador> loadedUsers = gson.fromJson(reader, listType);

            if (loadedUsers != null) {
                this.utilizadores = loadedUsers;
            }

        } catch (IOException e) {
            System.out.println("Arquivo users.json não encontrado. Será criado.");
            saveUsers(); // cria um novo vazio
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(utilizadores, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar utilizadores!");
            e.printStackTrace();
        }
    }
}
