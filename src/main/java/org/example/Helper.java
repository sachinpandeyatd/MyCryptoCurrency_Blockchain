package org.example;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

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

    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static byte[] applyEcdsaSignature(PrivateKey privateKey, String input){
        try{
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            return dsa.sign();
        } catch (NoSuchProviderException | NoSuchAlgorithmException |
                 InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Exception occurred while creating signature - " + e.getMessage());
        }
    }

    public static boolean verifyEcdsaSignature(PublicKey publicKey, String data, byte[] signature){
        try{
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initVerify(publicKey);
            dsa.update(data.getBytes());
            return dsa.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException |
                 InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException("Exception occurred while verifying signature - " + e.getMessage());
        }
    }
}
