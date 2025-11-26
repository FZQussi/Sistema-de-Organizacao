package com.example.service;

import java.time.Year;
import java.util.regex.Pattern;

public class RegistarEntradaService {

    private static final Pattern MATRICULA_PT = Pattern.compile(
            "^[A-Z]{2}-\\d{2}-\\d{2}$"      // AA-00-00
            + "|^\\d{2}-\\d{2}-[A-Z]{2}$"   // 00-00-AA
            + "|^\\d{2}-[A-Z]{2}-\\d{2}$"   // 00-AA-00
            + "|^[A-Z]{2}-\\d{2}-[A-Z]{2}$" // AA-00-AA
    );

    public boolean matriculaValida(String placa) {
        return MATRICULA_PT.matcher(placa.toUpperCase()).matches();
    }

    public boolean anoValido(int ano) {
        int atual = Year.now().getValue();
        return ano >= 1900 && ano <= atual;
    }
}
