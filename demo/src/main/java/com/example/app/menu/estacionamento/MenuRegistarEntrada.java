package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.service.RegistarEntradaService;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuRegistarEntrada {

    private final GestaoEstacionamento gestao;
    private final RegistarEntradaService service;
    private final Scanner sc;
    private final PrintStream out;

    public MenuRegistarEntrada(GestaoEstacionamento gestao, Scanner sc, PrintStream out) {
        this.gestao = gestao;
        this.service = new RegistarEntradaService();
        this.sc = sc;
        this.out = out;
    }

    public String pedirMatricula() {
        while (true) {
            out.print("→ Placa (0 para voltar): ");
            String placa = sc.nextLine().trim().toUpperCase();

            if (placa.equals("0")) return "0";

            if (!service.matriculaValida(placa)) {
                out.println("✖ Matrícula inválida!\n");
                continue;
            }

            return placa;
        }
    }

    public int pedirAno() {
        while (true) {
            out.print("→ Ano: ");
            try {
                int ano = Integer.parseInt(sc.nextLine().trim());
                if (service.anoValido(ano)) return ano;

                out.println("✖ Ano inválido!\n");
            } catch (NumberFormatException e) {
                out.println("✖ Digite apenas números!\n");
            }
        }
    }

    public void mostrar() {
        while (true) {

            String placa = pedirMatricula();
            if (placa.equals("0")) return;

            out.print("Marca: ");
            String marca = sc.nextLine();

            out.print("Modelo: ");
            String modelo = sc.nextLine();

            out.print("Cor: ");
            String cor = sc.nextLine();

            int ano = pedirAno();

            boolean sucesso = gestao.registrarEntrada(placa, marca, modelo, cor, ano);

            if (sucesso) {
                out.println("✔ Carro registado com sucesso!");
            } else {
                out.println("✖ Falha ao registar entrada!");
            }

            out.print("1 - Registar outra | 0 - Voltar: ");
            if (sc.nextLine().trim().equals("0")) return;
        }
    }
}
