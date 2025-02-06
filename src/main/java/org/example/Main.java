package org.example;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Block> blockChain = Blockchain.createChain();

        boolean integrityCheck = Blockchain.integrityCheck();
        System.out.println("Is the blockchain valid - " + integrityCheck);

        MakeTransaction makeTransaction = new MakeTransaction();
    }
}