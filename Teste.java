import java.util.*;

public class Teste{
    public static int contarAspas(String x){
        int resp = 0;
        int tam = x.length();

        for(int i = 0; i < tam; i++)
            if(x.charAt(i) == '"')
                resp++;

        return resp/2;
    }

    public static void main(String[] args){
        Scanner leitor = new Scanner(System.in);
        String l = leitor.nextLine();
        ArrayList<String> resp = new ArrayList<String>();

        int inicio = 0;    
        int fim = 0; 

        while(fim<l.length()){
            if(l.charAt(fim)==' '){
                resp.add(l.substring(inicio, fim));
                inicio = fim + 1;
            }
            else if(l.charAt(fim)=='\"'){
               fim++;
               inicio = fim;
               while(l.charAt(fim)!='\"'){
                  fim++;
               }
               resp.add(l.substring(inicio, fim));
               inicio = fim + 1;
            }
            resp.remove("");
            fim++;
        }
        resp.add(l.substring(inicio, fim));
        resp.remove("");
        System.out.println(resp);
    }
}
