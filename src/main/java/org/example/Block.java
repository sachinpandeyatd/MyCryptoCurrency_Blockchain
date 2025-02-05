package org.example;

import java.util.Date;

public class Block {
    private String hash;
    private String data;
    private String previousHash;
    private long timestamp;

    public Block(String data, String previousHash){
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    private String calculateHash(){
        return Helper.hashToSHA_256(
                previousHash.concat(Long.toString(timestamp)).concat(data));
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
