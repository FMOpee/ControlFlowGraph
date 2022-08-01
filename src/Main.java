import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private ArrayList<String> keyWordsRemovable = new ArrayList<>(Arrays.asList(
            "import","java.util.Scanner","public","class","static","void", "int"
    ));

    public void doStuff() throws FileNotFoundException {
        ArrayList<String> tokens = tokenizer(new File("src\\testInput.java"));
        boolean gotClass = false;

        ArrayList<Integer> ifs = new ArrayList<>();

        //refitting in a common syntax
        for (int i=0;i<tokens.size();i++) {
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



        System.out.println("\n\n\n");
        for (String s: tokens) {
            System.out.println(s);
        }
    }

    private ArrayList<String> tokenizer(File inputCode) throws FileNotFoundException {
        String wholeCode = "";
        ArrayList<String> tokenFin = new ArrayList<>();

        Scanner sc = new Scanner(inputCode);
        while (sc.hasNextLine()){
            wholeCode+=sc.nextLine();
        }
        tokenFin.add(wholeCode);

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

    private void iterator(int i){
        
    }

}
