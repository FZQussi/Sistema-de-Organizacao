package com.example.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Para criar uma password encriptada
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Para verificar se uma password corresponde ao hash
    public static boolean verify(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
