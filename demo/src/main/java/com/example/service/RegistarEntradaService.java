package com.example.service;

import java.time.Year;
import java.util.regex.Pattern;

/**
 * Serviço responsável por validar dados de entrada
 * relacionados com veículos, como matrícula e ano.
 */
public class RegistarEntradaService {

    /**
     * Expressão regular para validação de matrículas portuguesas.
     * Formatos aceites:
     *  - AA-00-00
     *  - 00-00-AA
     *  - 00-AA-00
     *  - AA-00-AA
     */
    private static final Pattern MATRICULA_PT = Pattern.compile(
            "^[A-Z]{2}-\\d{2}-\\d{2}$"      // AA-00-00
            + "|^\\d{2}-\\d{2}-[A-Z]{2}$"   // 00-00-AA
            + "|^\\d{2}-[A-Z]{2}-\\d{2}$"   // 00-AA-00
            + "|^[A-Z]{2}-\\d{2}-[A-Z]{2}$" // AA-00-AA
    );

    /**
     * Valida se a matrícula fornecida corresponde
     * a um formato português válido.
     *
     * @param placa matrícula do veículo
     * @return true se a matrícula for válida, false caso contrário
     */
    public boolean matriculaValida(String placa) {
        // Converte para maiúsculas para garantir consistência
        return MATRICULA_PT
                .matcher(placa.toUpperCase())
                .matches();
    }

    /**
     * Valida se o ano do veículo é plausível.
     *
     * @param ano ano de fabrico do veículo
     * @return true se o ano estiver entre 1900 e o ano atual
     */
    public boolean anoValido(int ano) {
        // Obtém o ano atual do sistema
        int atual = Year.now().getValue();
        return ano >= 1900 && ano <= atual;
    }
}
