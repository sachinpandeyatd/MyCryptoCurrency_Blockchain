package org.example;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {
    public static String hashToSHA_256(String input) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashArray = md.digest(input.getBytes(StandardCharsets.UTF_8));

            for (byte hash : hashArray) {
                String hex = Integer.toHexString(0xff & hash);
                if (hex.length() == 1) hexString.append(0);
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception occurred during hashing to SHA-256 - " + e.getMessage());
        }
        return hexString.toString();
    }
}
