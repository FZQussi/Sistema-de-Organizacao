package com.example.model;

/**
 * Classe base que representa um utilizador do sistema.
 * Pode ser um operador ou um gerente e contém
 * informações de autenticação e dados pessoais/profissionais.
 */
public class Utilizador {

    // Credenciais de autenticação
    private String username;
    private String password; // armazenado com hash
    private String tipo;     // operador / gerente

    // Dados pessoais
    private String nome;
    private String sobrenome;
    private String descricao;
    private String nacionalidade;

    // Data de nascimento mantida como String para evitar problemas de parsing
    private String dataNascimento;

    // Dados profissionais
    private double salario;
    private String turnoEntrada;
    private String turnoSaida;

    /**
     * Construtor completo do utilizador.
     * Inicializa todas as informações pessoais e profissionais.
     */
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

    /**
     * Construtor vazio.
     * Necessário para criação do objeto sem inicialização imediata
     * (ex.: leitura de ficheiros ou frameworks).
     */
    public Utilizador() {}

    // =======================
    // GETTERS
    // =======================

    // Retorna o nome de utilizador
    public String getUsername() {
        return username;
    }

    // Retorna a password (hash)
    public String getPassword() {
        return password;
    }

    // Retorna o tipo de utilizador (operador ou gerente)
    public String getTipo() {
        return tipo;
    }

    // Retorna o nome próprio
    public String getNome() {
        return nome;
    }

    // Retorna o sobrenome
    public String getSobrenome() {
        return sobrenome;
    }

    // Retorna a descrição do utilizador
    public String getDescricao() {
        return descricao;
    }

    // Retorna a nacionalidade
    public String getNacionalidade() {
        return nacionalidade;
    }

    // Retorna a data de nascimento
    public String getDataNascimento() {
        return dataNascimento;
    }

    // Retorna o salário
    public double getSalario() {
        return salario;
    }

    // Retorna o horário de entrada do turno
    public String getTurnoEntrada() {
        return turnoEntrada;
    }

    // Retorna o horário de saída do turno
    public String getTurnoSaida() {
        return turnoSaida;
    }

    // =======================
    // SETTERS
    // =======================

    // Define o nome de utilizador
    public void setUsername(String username) {
        this.username = username;
    }

    // Define a password (já tratada/hasheada externamente)
    public void setPassword(String password) {
        this.password = password;
    }

    // Define o tipo de utilizador
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Define o nome próprio
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Define o sobrenome
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    // Define a descrição
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Define a nacionalidade
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    // Define a data de nascimento
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // Define o salário
    public void setSalario(double salario) {
        this.salario = salario;
    }

    // Define o horário de entrada do turno
    public void setTurnoEntrada(String turnoEntrada) {
        this.turnoEntrada = turnoEntrada;
    }

    // Define o horário de saída do turno
    public void setTurnoSaida(String turnoSaida) {
        this.turnoSaida = turnoSaida;
    }
}
