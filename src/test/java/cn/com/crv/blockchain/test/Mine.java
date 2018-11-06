package cn.com.crv.blockchain.test;

import cn.com.crv.blockchain.model.Block;
import cn.com.crv.blockchain.model.Transaction;
import cn.com.crv.blockchain.security.CodeUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mine {
    @Test
    public void mine() throws Exception{

        //生成钱包
        String walletAdress1="11111111";
        String walletAdress2="22222222";
        String walletAdress3="33333333";
        String walletAdress4="44444444";
        //创建一个空的区块链
        List<Block> blockchain = new ArrayList<>();
        //生成创始区块
        Transaction firstTransaction = Transaction.builder()
                .id(0)
                .fromWallet("")
                .toWallet(walletAdress1)
                .value(50)
                .poundage(0)
                .build();
        List<Transaction> transactions = Arrays.asList(firstTransaction);
        Block firstBlock = Block.builder()
                .hight(0)
                .previousHash("")
                .timestamp(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .nonce(0)
                .transactions(transactions)
                .hash("000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f")
                .merkleRoot("4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b")
                .build();
        //将创世区块加入区块链
        blockchain.add(firstBlock);
        //矿工开始挖矿，首先创建一笔给自己的交易
        Transaction transaction1 = Transaction.builder()
                .id(1)
                .fromWallet("")
                .toWallet(walletAdress1)
                .value(50)
                .poundage(0)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2)
                .fromWallet(walletAdress2)
                .toWallet(walletAdress1)
                .value(5)
                .poundage(0.12868805)
                .build();
        Transaction transaction3 = Transaction.builder()
                .id(3)
                .fromWallet(walletAdress3)
                .toWallet(walletAdress4)
                .value(1000)
                .poundage(1)
                .build();
        //将交易打包到区块链中
        List<Transaction> txs = Arrays.asList(transaction1,transaction2,transaction3);
        Block latestBlock = blockchain.get(blockchain.size() - 1);

        int nonce=0;
        String hash="";
        String difficulty="00";
        long timeStamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        while (true) {
            //高度+时间戳+上个区块的哈希+交易+nonce
            hash= CodeUtil.sha256((latestBlock.getHight()+1)+timeStamp+latestBlock.getHash()+ JSON.toJSONString(txs)+nonce);
            if (hash.startsWith(difficulty)) {
                long endtime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                System.out.println("=====计算结果正确，计算次数为：" +nonce+ ",hash:" + hash);
                System.out.println(" 一共花费 "+(endtime-timeStamp)/1000 +" 秒");
                break;
            }
            nonce++;
            System.out.println("计算错误，hash:" + hash);
        }
        Block newBlock =  Block.builder()
                .hash(latestBlock.getHash()+1)
                .transactions(txs)
                .nonce(nonce)
                .previousHash(latestBlock.getHash())
                .timestamp(timeStamp)
                .hash(hash)
                .build();
        newBlock.generateMerkleRoot();
        blockchain.add(newBlock);
        System.out.println("挖矿后的区块链：");
        System.out.println(JSON.toJSONString(blockchain));


    }
}
