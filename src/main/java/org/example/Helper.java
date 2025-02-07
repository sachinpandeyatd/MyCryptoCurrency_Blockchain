package org.example;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
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

    public static String getMerkleRoot(ArrayList<Transaction> transactions){
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<>();
        for(Transaction transaction : transactions){
            previousTreeLayer.add(transaction.getTransactionId());
        }

        ArrayList<String> treeLayer = previousTreeLayer;
        while (count > 1){
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < previousTreeLayer.size(); i++){
                treeLayer.add(hashToSHA_256(
                        previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }

    /**
     * Returns difficulty string target, to compare to hash. e.g. difficulty of 5 will return "00000"
     * @param difficulty
     * @return
     */
    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
}
