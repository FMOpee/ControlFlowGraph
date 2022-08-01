import java.util.Scanner;

public class testInput {
    public static void main(int a, int b, int c, int d) {
        if(a<5){
            b+=5;
            c*=a;
            System.out.println("if1");
        }
        else if( a<7){
            d=10;
            a-=4;
            System.out.println("else1if1");
        }
        else {
            d=15;
            a-=6;
            System.out.println("else1");
        }
        for(int i = 1; a<d; a++, i++){
            System.out.println("for"+i);
            c+=a;
        }
        if(c>50){
            c-=20;
            System.out.println("if2");
        }
        if(d>c/2){
            System.out.println("if3");
            d+=5;
        }
        else{
            System.out.println("else3");
            d-=5;
        }
        System.out.println(a+"\n"+b+"\n"+c+"\n"+d);

    }
}
