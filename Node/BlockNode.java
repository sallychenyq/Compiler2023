package Node;

import Token.Token;

import java.util.ArrayList;

public class BlockNode {

    private String left;
    private Token right;
    private  ArrayList<BlockItemNode> blockList;
    private int BreakId;
    private int ContinueId;

    public BlockNode(ArrayList<BlockItemNode> blockList,Token right){
        this.blockList=blockList;
        this.right=right;
    }
    public void print(){
        for(int i = 0; i < blockList.size()&&blockList.size()>0; i++){
            blockList.get(i).print();
        }
        //System.out.println("<Block>");
    }

    public ArrayList<BlockItemNode> getBlockItemNodes() {
        return blockList;
    }
    public Token getRight(){return right;}

    public int getBreakId(){
        return BreakId;
    }
    public void setBreakId(int breakId){
        this.BreakId=breakId;
    }
    public int getContinueId(){
        return ContinueId;
    }
    public void setContinueId(int continueId){
        this.ContinueId=continueId;
    }

}
