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
        int quantAspas = contarAspas(l);
        ArrayList<String> resp = new ArrayList<String>();

        String aux;
        String[] parte;
        
        for(int i = 0; i < quantAspas; i++){
            int primeiro = l.indexOf("\"");
            aux = l.substring(primeiro, l.indexOf("\"", primeiro+1)+1);
            
            parte = l.split(aux);
            l = parte[1];

            for( String j : parte[0].split(" ")){
                if(!j.equals(""))
                    resp.add(j);
            }

            if(!aux.equals("\" \"") && !aux.equals("\"\"")){
                resp.add(aux.replaceAll("\"",""));
            }
        }

        for( String j : l.split(" "))
            if(!j.equals(""))
                resp.add(j);
        
        System.out.println(resp);
    }
}