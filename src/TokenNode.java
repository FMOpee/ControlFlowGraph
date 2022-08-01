import java.util.ArrayList;

public class TokenNode {
    public TokenNode parent;
    public String type;
    public String argument;
    public ArrayList<TokenNode> children;

    public TokenNode(String t, String arg){
        type=t;
        argument =arg;
        children = new ArrayList<>();
    }

    public void addParent(TokenNode p){
        parent = p;
    }

    public void addChildren(ArrayList<TokenNode> c){
        children.addAll(c);
        for (TokenNode child: children){
            addParent(this);
        }
    }

    public String toString(){
        String ret = "<"+type+" : "+argument+">{\n";
        for (TokenNode child: children) ret+=child+"\n";
        ret+="}";
        return ret;
    }
}
