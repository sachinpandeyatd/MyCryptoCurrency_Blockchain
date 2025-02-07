package org.example;

import com.google.gson.GsonBuilder;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public static int difficulty = 2;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Transaction genesisTransaction;

    public static void run (){
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbaseWallet = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to walletA
        genesisTransaction = new Transaction(coinbaseWallet.getPublicKey(), walletA.getPublicKey(), 100f, null);
        genesisTransaction.generateSignature(coinbaseWallet.getPrivateKey()); //manually sign the genesis transaction
        genesisTransaction.setTransactionId("0");
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
        UTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));

        System.out.println("Creating and mining genesis block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);


        //testing
        Block block1 = new Block(genesis.getHash());
        System.out.println("\nWalletA balance - " + walletA.getBalance());
        System.out.println("\nWalletA is attempting to send 50 coins to walletB");
        block1.addTransaction(walletA.sendCoins(walletB.getPublicKey(), 50f));
        addBlock(block1);
        System.out.println("\nWalletA's balance - " + walletA.getBalance());
        System.out.println("WalletB's balance - " + walletB.getBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendCoins(walletB.getPublicKey(), 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendCoins( walletA.getPublicKey(), 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        integrityCheck();
    }

    private static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static boolean integrityCheck(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = "0".repeat(difficulty);
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            //compare registered hash and calculated hash:
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if(!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)){
                System.out.println("This block has NOT been mined.");
                return false;
            }

            //loop through blockchain transactions
            TransactionOutput tempOutput;
            for(int t = 0; t < currentBlock.transactions.size(); t++){
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()){
                    System.out.println("Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                if(currentTransaction.getInputValue() != currentTransaction.getOutputValue()){
                    System.out.println("Inputs are not equals to outputs on transaction - " + t);
                    return false;
                }

                for (TransactionInput input : currentTransaction.inputs){
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());
                    if (tempOutput == null){
                        System.out.println("Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.getValue() != tempOutput.getValue()){
                        System.out.println("Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }
                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for(TransactionOutput output : currentTransaction.outputs){
                    tempUTXOs.put(output.getId(), output);
                }

                if( currentTransaction.outputs.get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }

                if( currentTransaction.outputs.get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }
        System.out.println("Integrity test passed. Blockchain is valid.");
        return true;
    }
}
