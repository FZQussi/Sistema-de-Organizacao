package com.example.app;

import com.example.utils.PasswordUtils;

public class GenerateHash {
    public static void main(String[] args) {
        String pass = "admin123";
        String hash = PasswordUtils.hash(pass);
        System.out.println(hash);
    }
}
