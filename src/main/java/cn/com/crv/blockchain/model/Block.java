package cn.com.crv.blockchain.model;

import cn.com.crv.blockchain.security.CodeUtil;
import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Block {

    /**
     * 高度
     */
    private int hight;
    /**
     * 当前区块的hash值,区块唯一标识
     */
    private String hash;
    /**
     * 生成区块的时间戳
     */
    private long timestamp;
    /**
     * 当前区块的交易集合
     */
    private List<Transaction> transactions;
    /**
     * 工作量证明，计算正确hash值的次数
     */
    private int nonce;
    /**
     * 前一个区块的hash值
     */
    private String previousHash;
    /**
     * 默克尔树
     */
    private String merkleRoot;



    public void generateMerkleRoot() throws Exception {
        if (null == this.transactions || this.transactions.isEmpty()) {
            throw new Exception("交易不能为空");
        }
        List<String> tree = new ArrayList();
        for (Transaction t : transactions) {
            tree.add(CodeUtil.sha256(JSON.toJSONString(t)));
        }

        int levelOffset = 0; // 当前处理的列表中的偏移量。
//  当前层级的第一个节点在整个列表中的偏移量。
// 每处理完一层递增，
// 当我们到达根节点时（levelSize == 1）停止。
        for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
// 对于该层上的每一对节点：
            for (int left = 0; left < levelSize; left += 2) {
// 在我们没有足够交易的情况下，
// 右节点和左节点
// 可以一样。
                int right = Math.min(left + 1, levelSize - 1);
                String tleft = tree.get(levelOffset + left);
                String tright = tree.get(levelOffset + right);
                tree.add(CodeUtil.sha256(tleft + tright));
            }
// 移动至下一层
            levelOffset += levelSize;
        }
        this.merkleRoot = tree.get(0);
    }


}
