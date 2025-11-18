package com.example.model;

public class Utilizador {
    private String username;
    private String password; // bcrypt hash
    private String tipo; // gerente ou operador

    public Utilizador() {}

    public Utilizador(String username, String password, String tipo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
