package demo;

public class HelloCode {
    static String a="ABC";
    static int b=1;
    public static void main(String [] args){
        int i=1;
        int j=2;
        i+=j;
        for(;i>0;i--){
            j++;
        }
        if(i>j)
            System.out.println(i);
    }
}
