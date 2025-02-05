package org.example;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        ArrayList<Block> blockChainData = blockchain.createChain();

        String blockchainStr = new GsonBuilder().setPrettyPrinting().create().toJson(blockChainData);
        System.out.println(blockchainStr);
    }
}