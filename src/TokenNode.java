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
        p.addChild(this);
    }

    public void addChild(TokenNode c){
        children.add(c);
    }
}
