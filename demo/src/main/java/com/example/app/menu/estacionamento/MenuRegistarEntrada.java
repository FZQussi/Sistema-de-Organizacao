package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.service.RegistarEntradaService;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuRegistarEntrada {

    // Serviço principal de gestão do estacionamento
    private final GestaoEstacionamento gestao;

    // Serviço responsável pela validação de dados na entrada de veículos
    private final RegistarEntradaService service;

    // Dependências para interação com o utilizador
    private final Scanner sc;
    private final PrintStream out;

    // Construtor permitindo testes e injeção de dependências
    public MenuRegistarEntrada(GestaoEstacionamento gestao, Scanner sc, PrintStream out) {
        this.gestao = gestao;
        this.service = new RegistarEntradaService(); // Serviço interno especializado
        this.sc = sc;
        this.out = out;
    }

    // Solicita e valida a matrícula do veículo
    public String pedirMatricula() {
        while (true) {
            out.print("→ Placa (0 para voltar): ");
            String placa = sc.nextLine().trim().toUpperCase();

            // Permite retornar ao menu anterior
            if (placa.equals("0")) return "0";

            // Validação de matrícula através do service
            if (!service.matriculaValida(placa)) {
                out.println("✖ Matrícula inválida!\n");
                continue;
            }

            return placa;
        }
    }

    // Solicita o ano do veículo, garantindo valor numérico e válido
    public int pedirAno() {
        while (true) {
            out.print("→ Ano: ");
            try {
                int ano = Integer.parseInt(sc.nextLine().trim());

                // Verificação de ano pelo service
                if (service.anoValido(ano)) return ano;

                out.println("✖ Ano inválido!\n");
            } catch (NumberFormatException e) {
                // Evita crashes caso o utilizador digite letras ou símbolos
                out.println("✖ Digite apenas números!\n");
            }
        }
    }

    // Fluxo principal do menu de registo de entrada
    public void mostrar() {
        while (true) {

            // Lê a matrícula; se for 0, retorna ao menu anterior
            String placa = pedirMatricula();
            if (placa.equals("0")) return;

            // Coleta informações básicas do veículo
            out.print("Marca: ");
            String marca = sc.nextLine();

            out.print("Modelo: ");
            String modelo = sc.nextLine();

            out.print("Cor: ");
            String cor = sc.nextLine();

            int ano = pedirAno();

            // Tenta registrar o veículo no sistema
            boolean sucesso = gestao.registrarEntrada(placa, marca, modelo, cor, ano);

            if (sucesso) {
                out.println("✔ Carro registado com sucesso!");
            } else {
                out.println("✖ Falha ao registar entrada!");
            }

            // Pergunta se o utilizador deseja registrar outro veículo
            out.print("1 - Registar outra | 0 - Voltar: ");
            if (sc.nextLine().trim().equals("0")) return;
        }
    }
}
