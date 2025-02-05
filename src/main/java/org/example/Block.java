package org.example;

import java.util.Date;

public class Block {
    private String hash;
    private String data;
    private String previousHash;
    private long timestamp;
    private int nonce;

    public Block(String data, String previousHash){
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        return Helper.hashToSHA_256(previousHash.concat(Long.toString(timestamp))
                .concat(Integer.toString(nonce)).concat(data));
    }

    public void mineBlock(int difficulty){
        String target = "0".repeat(difficulty);

        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Congratulation, you mined a block - " + hash);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
