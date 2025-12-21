package com.example.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitária para encriptação e verificação de passwords.
 * Utiliza o algoritmo BCrypt, que é seguro e resistente a ataques de força bruta.
 */
public class PasswordUtils {

    /**
     * Gera um hash seguro a partir de uma password em texto claro.
     * Cada hash é gerado com um salt aleatório, tornando impossível gerar
     * o mesmo hash para a mesma password duas vezes.
     *
     * @param password password em texto claro
     * @return hash da password
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifica se uma password em texto claro corresponde ao hash armazenado.
     *
     * @param password password em texto claro fornecida pelo utilizador
     * @param hashed   hash armazenado
     * @return true se a password corresponder ao hash, false caso contrário
     */
    public static boolean verify(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
