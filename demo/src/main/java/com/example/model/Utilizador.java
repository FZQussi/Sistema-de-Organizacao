package com.example.model;

public class Utilizador {

    private String username;
    private String password; // armazenado com hash
    private String tipo; // operador / gerente

    // Novos campos
    private String nome;
    private String sobrenome;
    private String descricao;
    private String nacionalidade;
    private String dataNascimento; // manter como String para evitar problemas de parsing
    private double salario;
    private String turnoEntrada;
    private String turnoSaida;

    public Utilizador(String username, String password, String tipo, String nome, String sobrenome,
                      String descricao, String nacionalidade, String dataNascimento,
                      double salario, String turnoEntrada, String turnoSaida) {

        this.username = username;
        this.password = password;
        this.tipo = tipo;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.descricao = descricao;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.salario = salario;
        this.turnoEntrada = turnoEntrada;
        this.turnoSaida = turnoSaida;
    }

    public Utilizador() {}

       // GETTERS
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public double getSalario() {
        return salario;
    }

    public String getTurnoEntrada() {
        return turnoEntrada;
    }

    public String getTurnoSaida() {
        return turnoSaida;
    }

    // SETTERS
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setTurnoEntrada(String turnoEntrada) {
        this.turnoEntrada = turnoEntrada;
    }

    public void setTurnoSaida(String turnoSaida) {
        this.turnoSaida = turnoSaida;
    }

}

