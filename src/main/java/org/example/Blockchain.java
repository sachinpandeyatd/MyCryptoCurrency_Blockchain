package org.example;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Blockchain {
    private static ArrayList<Block> blockchain = new ArrayList<>();
    private static int difficulty = 5;

    public static ArrayList<Block> createChain (){
        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).getHash()));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).getHash()));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);
        String blockchainStr = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainStr);
        return blockchain;
    }

    public static boolean integrityCheck(){
        Block currentBlock;
        Block previousBlock;

        for(int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if(!currentBlock.getHash().equals(currentBlock.calculateHash())){
                System.out.println("Integrity test failed - Current hashes didn't match");
                return false;
            }

            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())){
                System.out.println("Integrity test failed - previous hashes didn't match");
                return false;
            }
        }
        System.out.println("Integrity test passed");
        return true;
    }
}
