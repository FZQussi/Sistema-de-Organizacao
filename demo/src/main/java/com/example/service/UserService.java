package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<Utilizador> utilizadores;
    private final String filePath = "users.json"; // ou "src/main/resources/users.json"


    public UserService() {
        utilizadores = new ArrayList<>();
        loadUsers();
    }

    // Lê ficheiro JSON
    private void loadUsers() {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Utilizador>>(){}.getType();
            utilizadores = new Gson().fromJson(reader, listType);
            if(utilizadores == null) utilizadores = new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("users.json não existe, será criado ao adicionar utilizadores.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guarda no ficheiro JSON
    private void saveUsers() {
        try (Writer writer = new FileWriter(filePath)) {
            new Gson().toJson(utilizadores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adicionar utilizador
    public void addUser(String username, String password, String tipo) {
        String hash = PasswordUtils.hash(password);
        Utilizador u = new Utilizador(username, hash, tipo);
        utilizadores.add(u);
        saveUsers();
        System.out.println("Utilizador " + username + " criado com sucesso!");
    }

    // Login
public Utilizador login(String username, String password) {
    for (Utilizador u : utilizadores) {
        if (u.getUsername().equals(username) && PasswordUtils.verify(password, u.getPassword())) {
            return u;
        }
    }
    return null;
}


    // Retorna todos utilizadores (para menu do gerente)
    public List<Utilizador> getAllUsers() {
        return utilizadores;
    }

    
}
