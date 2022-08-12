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
            child.addParent(this);
        }
    }

    public String toString(){
        StringBuilder ret = new StringBuilder("<" + type + " : " + argument + ">");
        if(children.size()!=0){
            ret.append("[\n");
            for (TokenNode child: children) ret.append(child).append("\n");
            ret.append("]");
        }
        return ret.toString();
    }
}
