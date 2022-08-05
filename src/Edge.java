public class Edge {
    GraphNode from, to;

    public Edge(GraphNode from, GraphNode to){
        this.from = from;
        this.to = to;
    }

    public String toString(){
        return from + " >>> " + to;
    }
}
