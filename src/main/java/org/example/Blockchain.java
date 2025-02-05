package org.example;

import java.util.ArrayList;

public class Blockchain {
    private static ArrayList<Block> blockchain = new ArrayList<>();

    public static ArrayList<Block> createChain (){
        blockchain.add(new Block("Hi im the first block", "0"));
        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).getHash()));
        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).getHash()));
        return blockchain;
    }
}
