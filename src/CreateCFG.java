import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CreateCFG {
    private int idCounter =3;

    private final ArrayList<String> blockStarters= new ArrayList<>(Arrays.asList(
            "if", "while", "for"
    ));


    public void graphCreator(TokenNode root, GraphNode target){
        //count blocks in children
        int count =0;
        int blockIndex =0;
        for (TokenNode child: root.children){
            if(blockStarters.contains(child.type))
                count++;
        }

        //add node for each block
        ArrayList<GraphNode> blockPoints = addLinearBlock(count,target);

        //add sequence foreach block
        for (int i =0;i<root.children.size();i++){
            ArrayList<GraphNode> childrenTargets;
            if(blockStarters.contains(root.children.get(i).type)){
                int n =1;
                for (int j=i+1; j<root.children.size();j++){
                    if(!blockStarters.contains(root.children.get(j).type)) n++;
                    else break;
                }
                if(root.children.get(i).type.equals("if")){
                    boolean hasElse = root.children.get(i+n-1).type.equals("else");
                    childrenTargets = addIfBlock(hasElse, n, blockPoints.get(blockIndex++));

                    //recurse for children of each one of the block
                    for (int j=i; j-i<n; j++){
                        graphCreator(root.children.get(j),childrenTargets.get(j-i));
                    }
                }
                else if(root.children.get(i).type.equals("for")){
                    GraphNode node = addForBlock(blockPoints.get(blockIndex++));
                    for (TokenNode tn: root.children.get(i).children)
                        graphCreator(tn,node);
                }
                else if(root.children.get(i).type.equals("while")){
                    GraphNode node = addForBlock(blockPoints.get(blockIndex++));
                    for (TokenNode tn: root.children.get(i).children)
                        graphCreator(tn,node);
                }

            }
        }


    }

    private ArrayList<GraphNode> addLinearBlock(int num, GraphNode target){
        ArrayList<GraphNode> list = new ArrayList<>(Collections.singletonList(target));

        for (int i=0; i<num-1; i++){
            list.add(new GraphNode(id()));
        }

        list.get(list.size()-1).addDestination(list.get(0).to);

        for (int i=0;i<list.size()-1;i++){
            list.get(i).to = new ArrayList<>();
            list.get(i).to.add(list.get(i+1));
        }

        return list;
    }

    private ArrayList<GraphNode> addIfBlock(boolean hasElse, int num, GraphNode target){
        ArrayList<GraphNode> list= new ArrayList<>();

        if(num == 1){
            GraphNode n1 = new GraphNode(id());
            GraphNode n2 = new GraphNode(id());

            n2.to = target.to;
            target.to = new ArrayList<>(Arrays.asList(n1,n2));
            n1.addDestination(n2);

            list.add(n1);
        }
        else if(hasElse){
            GraphNode end = new GraphNode(id());

            for (int i=0; i<num; i++){
                GraphNode gn =new GraphNode(id());
                gn.addDestination(end);
                list.add(gn);
            }

            end.to = target.to;
            target.overrideTo(list);
        }
        else {

            GraphNode end = new GraphNode(id());

            for (int i=0; i<num; i++){
                GraphNode gn =new GraphNode(id());
                gn.addDestination(end);
                list.add(gn);
            }

            end.to = target.to;
            target.overrideTo(list);
            target.addDestination(end);
        }

        return list;
    }

    private GraphNode addForBlock(GraphNode target){
        GraphNode n1 = new GraphNode(id());
        GraphNode n2 = new GraphNode(id());
        GraphNode n3 = new GraphNode(id());

        n3.to = target.to;
        n3.addDestination(n1);

        target.to = new ArrayList<>();
        target.addDestination(n1);

        n1.addDestination(n2);
        n2.addDestination(n3);

        return n2;
    }

    private GraphNode addWhileBlock(GraphNode target){
        GraphNode n1 = new GraphNode(id());

        target.addDestination(n1);
        n1.addDestination(target);

        return n1;
    }

    private int id(){
        return idCounter++;
    }
}
