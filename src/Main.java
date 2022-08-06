import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static GraphNode start, end, main;
    public static ArrayList<GraphNode> nodes = new ArrayList<>();

    private final ArrayList<String> keyWordsRemovable = new ArrayList<>(Arrays.asList(
            "new","import","java.util.Scanner","public","class","static","void", "int","String","float","double","boolean",";"
    ));
    private ArrayList<Edge> edges = new ArrayList<>();

    private int index = 0;
    private ArrayList<String> reattachedTokens;

    public void doStuff() throws FileNotFoundException {
        ArrayList<String> tokens = tokenizer(new File("src\\testInput.txt"));
        reattachedTokens = new ArrayList<>();

        //refitting in a common syntax
        for (int i = 0; i< tokens.size(); i++) {
            if(tokens.get(i).equals("class")){
                tokens.add(i+2,"(");
                tokens.add(i+3,")");
            }
            else if(tokens.get(i).equals("else") && !tokens.get(i+1).equals("if")){
                tokens.add(i+1,"(");
                tokens.add(i+2,")");
            }
            else if(tokens.get(i).equals("else") && tokens.get(i+1).equals("if")){
                tokens.remove(i);
                tokens.remove(i);
                tokens.add(i,"elseif");
            }

            if(keyWordsRemovable.contains(tokens.get(i))) {
                tokens.remove(i);
                i--;
            }
        }

        //removing function calls
        for(int i = 0; i< tokens.size()-1; i++){
            if(tokens.get(i).equals(")") && !tokens.get(i+1).equals("{")){
                while(!tokens.get(i).equals("(")){
                    tokens.remove(i);
                    i--;
                }
                tokens.remove(i);
                tokens.remove(i-1);i-=2;
            }
        }

        StringBuilder tempString = new StringBuilder();
        boolean inside = false;

        //reattaching tokens
        for (int i = 0; i< tokens.size()-1; i++){
            if (inside || tokens.get(i + 1).equals("(") || tokens.get(i).equals("{") || tokens.get(i).equals("}") ) {
                if (!inside) {
                    reattachedTokens.add(tokens.get(i));

                    if (tokens.get(i + 1).equals("("))
                        inside = true;
                } else if (!tokens.get(i).equals(")")) {
                    tempString.append(tokens.get(i));
                } else {
                    tempString.append(tokens.get(i));
                    reattachedTokens.add(tempString.toString());
                    inside = false;
                    tempString = new StringBuilder();
                }
            }
        }
        reattachedTokens.add("}");

        TokenNode root = tokenNodeProducer().children.get(0);

        CreateCFG creator = new CreateCFG();
        start = new GraphNode(0);
        main = new GraphNode(1);
        start.addDestination(main);
        end = new GraphNode(2);
        main.addDestination(end);

        creator.graphCreator(root, main);

        dfs(start);
        for (Edge e: edges) System.out.println(e);

        System.out.println("\n\n");

        System.out.println("Nodes: "+nodes.size());
        System.out.println("Edges: "+edges.size());
        System.out.println("Cyclo-metric Complexity: "+(edges.size()-nodes.size()+2));

    }

    private ArrayList<String> tokenizer(File inputCode) throws FileNotFoundException {
        StringBuilder wholeCode = new StringBuilder();
        ArrayList<String> tokenFin = new ArrayList<>();

        Scanner sc = new Scanner(inputCode);
        while (sc.hasNextLine()){
            wholeCode.append(sc.nextLine()).append("\n");
        }

        String code = commentRemover(wholeCode);

////        tokenFin.add(wholeCode.toString());
        tokenFin.add(code);

        return tokenizationUnit(
            tokenizationUnit(
                tokenizationUnit(
                    tokenizationUnit(
                        tokenizationUnit(
                            tokenizationUnit(
                                tokenFin, " ", false
                            ), ";", true
                        ), "{", true
                    ), "}", true
                ), "(", true
            ), ")", true
        );
    }

    private String commentRemover(StringBuilder wholeCode) {
        String code = wholeCode.toString();
        ArrayList<Character> commentless= new ArrayList<>();
        boolean doubleSlash=false;
        boolean multiLineComment = false;
        boolean stringOpen = false;
        for (int i=0; i<code.length();i++){

            char c1 = code.charAt(i);

            char c2;
            if(i<code.length()-1)
                c2= code.charAt(i+1);
            else c2='\n';

            if(multiLineComment && c1=='*' && c2=='/'){
                multiLineComment = false;
                i++;
            }
            else if(doubleSlash && c1=='\n'){
                doubleSlash = false;
            }
            else if(stringOpen && c1 =='"'){
                stringOpen = false;
            }
            else if(!multiLineComment && c1== '/' && c2 == '*'){
                multiLineComment = true;
                i++;
            }
            else if(!doubleSlash && c1=='/' && c2=='/'){
                doubleSlash = true;
                i++;
            }
            else if(!stringOpen && c1 =='"'){
                stringOpen = true;
            }
            else if(multiLineComment || stringOpen|| doubleSlash) {
            }
            else commentless.add(c1);

        }
        code = "";
        for (char c:commentless) code+=c;
//        System.out.println(code);
        return code;
    }

    private ArrayList<String> tokenizationUnit(ArrayList<String> source, String delim, boolean ret){
        ArrayList<String> tokenTemp = new ArrayList<>();
        for (String s: source){
            StringTokenizer st = new StringTokenizer(s, delim, ret);
            while (st.hasMoreElements()) {
                tokenTemp.add(st.nextToken());
            }
        }
        source = new ArrayList<>();
        source.addAll(tokenTemp);
        return source;
    }

    private TokenNode tokenNodeProducer(){
        ArrayList<TokenNode> children = new ArrayList<>();
        String type = reattachedTokens.get(index);
        String arg = reattachedTokens.get(index+1);
        index+=3;
        TokenNode tokenNode = new TokenNode(type,arg);

        while (!reattachedTokens.get(index).equals("}")){
            TokenNode child = tokenNodeProducer();
            index++;
            children.add(child);
        }
        tokenNode.addChildren(children);

        return tokenNode;
    }

    private void dfs(GraphNode g){
        nodes.add(g);
        g.visited =true;
        for (GraphNode c: g.to){
            Edge newEdge = new Edge(g, c);
            boolean found = false;
            for (Edge edge : edges) {
                if (edge.toString().equals(newEdge.toString())) {
                    found = true;
                    break;
                }
            }

            if (!found) edges.add(newEdge);

            if(!c.visited)
                dfs(c);
        }
    }

}
