package org.example;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class CryptoCoin {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public static int difficulty = 3;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Transaction genesisTransaction;

    public void process(){
        ArrayList<Block> blockChain = Blockchain.createChain();

        boolean integrityCheck = Blockchain.integrityCheck();
        System.out.println("Is the blockchain valid - " + integrityCheck);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        walletA = new Wallet();
        walletB = new Wallet();

        MakeTransaction makeTransaction = new MakeTransaction();

    }
}
