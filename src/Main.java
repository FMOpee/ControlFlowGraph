import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private final ArrayList<String> keyWordsRemovable = new ArrayList<>(Arrays.asList(
            "import","java.util.Scanner","public","class","static","void", "int","String","float","double","boolean",";"
    ));

    private int index = 0;
    private ArrayList<String> reattachedTokens;

    public void doStuff() throws FileNotFoundException {
        ArrayList<String> tokens = tokenizer(new File("src\\testInput.java"));
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


//        for (String s: reattachedTokens) {
//            System.out.println(s);
//        }

        TokenNode root = tokenNodeProducer();

        System.out.println(root);
    }

    private ArrayList<String> tokenizer(File inputCode) throws FileNotFoundException {
        StringBuilder wholeCode = new StringBuilder();
        ArrayList<String> tokenFin = new ArrayList<>();

        Scanner sc = new Scanner(inputCode);
        while (sc.hasNextLine()){
            wholeCode.append(sc.nextLine());
        }
        tokenFin.add(wholeCode.toString());

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

}
