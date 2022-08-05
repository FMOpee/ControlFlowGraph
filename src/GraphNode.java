import java.util.ArrayList;

public class GraphNode {
    int id;
    ArrayList<GraphNode> to = new ArrayList<>();

    public GraphNode(int id){
        this.id = id;
    }

    public void addDestination(GraphNode gn){
        to.add(gn);
    }

    public void addDestination(ArrayList<GraphNode> gns){
        to.addAll(gns);
    }

    public void overrideTo(ArrayList<GraphNode> gns){
        to = gns;
    }

    public  GraphNode child(int index){
        return to.get(index);
    }

    public String toString(){
        return " "+id+":"+to+" ";
    }
}
