/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/

import java.util.Scanner;


/*
Classe Acesso - Gerenciador de acesso ao Sistema de Perguntas
*/
public class Acesso{
   //atributos estaticos da classe
   private static String _NOME = "PERGUNTAS";
   private static String _VERSAO = "1.0";
   


   /*
   telaInicial - Apresenta a tela inicial
   */
   public static void telaInicial(){
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("ACESSO");
      System.out.println();
      System.out.println("1) Acesso ao sistema");
      System.out.println("2) Novo Usuário (primeiro acesso)");
      System.out.println("3) Esqueci minha senha");
      System.out.println();
      System.out.println("0) Sair");
      System.out.println();
      System.out.print("Opção: ");
   }  

   /*
   pause - realiza uma pausa até novo input
   @param Scanner
   */
   public static void pause(Scanner leitor){
      System.out.println();
      System.out.println("Aperte ENTER para Continuar");
      System.out.println();
      leitor.nextLine();
   }


   /*
   main - funcao principal de acesso
   */
   public static void main(String[] args){
      //declaracoes
      Scanner leitor = new Scanner(System.in);
      int escolha = 0; 
      String buffer;  
   
      do{
         telaInicial();
         buffer = leitor.nextLine();
         
         try{
            escolha = Integer.parseInt(buffer);
         }
         catch (NumberFormatException e){ 
            escolha = -1;
         }

         switch(escolha){
            case 0:
               System.out.println("\nObrigado por utilizar nosso programa");
               pause(leitor);
               break;
            case 1:
               System.out.println("\nTeste login");
               pause(leitor);
               break;
            case 2:
               System.out.println("\nTeste novo usuário");
               pause(leitor);
               break;
            case 3:
               System.out.println("\nTeste esqueci senha");
               pause(leitor);
               break;
            default: 
               System.out.println("\nEscolha Inválida");
               pause(leitor);
         }
      }while(escolha!=0);
   } 
}