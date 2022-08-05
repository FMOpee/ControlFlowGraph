import java.util.ArrayList;
import java.util.Arrays;

public class CreateCFG {
    private final ArrayList<String> blockStarters= new ArrayList<>(Arrays.asList(
            "if", "while", "for"
    ));


    public void graphCreator(TokenNode root, GraphNode target){
        //count blocks in children
        int count =0;
        for (TokenNode child: root.children){
            if(blockStarters.contains(child.type))
                count++;
        }
        System.out.println("total block in :"+ root+ " is "+ count);
        //add node for each block
        ArrayList<GraphNode> blockPoints = addLinearBlock(count,target);
        //add sequence foreach block
        //check for children of each one

    }

    private ArrayList<GraphNode> addLinearBlock(int num, GraphNode target){
        ArrayList<GraphNode> list = new ArrayList<>(Arrays.asList(target));

        for (int i=0; i<num-1; i++){
            list.add(new GraphNode());
        }

        list.get(list.size()-1).addDestination(list.get(0).to);

        for (int i=0;i<list.size()-1;i++){
            list.get(i).to = new ArrayList<>();
            list.get(i).to.add(list.get(i+1));
        }

        return list;
    }
}
