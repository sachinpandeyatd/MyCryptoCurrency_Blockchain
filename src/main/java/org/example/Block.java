package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    private String hash;
    private String previousHash;
    private String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timestamp;
    private int nonce;

    public Block(String previousHash){
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        return Helper.hashToSHA_256(previousHash + timestamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty){
        merkleRoot = Helper.getMerkleRoot(transactions);
        String target = Helper.getDificultyString(difficulty);

        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Congratulation, you mined a block - " + hash);
    }

    /**
     * Add transactions to this block
     * @return boolean
     */
    public boolean addTransaction(Transaction transaction){
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if (!Objects.equals(previousHash, "0")){
            if(!transaction.processTransaction()){
                System.out.println("Transaction failed to process.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to the block.");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }
}
