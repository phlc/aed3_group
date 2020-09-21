/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/


/*INICIO CLASSE MENU 
 * FALTAM MUITOS MÉTODOS AINDA
 */

import java.net.StandardSocketOptions;
import java.util.Scanner;

public class Menu {
   public static String header = "\n==============\n" + Acesso.getNome() + "  " + Acesso.getVersao()
                                 + "\n==============\n";

   public static String menuLogin = "\nACESSO\n" + "\n[1] Acesso ao sistema\n" + "[2] Novo Usuário"
                                    + "(primeiro acesso)\n" + "[3] Esqueci minha senha" + "\n[0] Sair\n" 
                                    + "\nOpção:";
  
   public static String menuPerguntas= "\nPERGUNTAS\n" + "\n[1] Criação de perguntas\n" + "[2] Consultar/"
                                       + "Responder perguntas\n" //+ "[3] Notificações: getNotificacoes" 
                                       + "\n[0] Sair\n" + "\nOpção:";

   public static String mensagemErro = "\nERRO\n" + "\nDesculpe, ocorreu um" + "erro inesperado\n" 
                                       + "TENTE NOVAMENTE MAIS TARDE";

   private static Usuario online;

   //funcoes da interface

   /*
   clear - limpa a tela do terminal
   */
   public static void clear(){
      System.out.print("\033[H\033[2J");  
      System.out.flush();  
   }

   /*
    * telaInicial - Apresenta a tela inicial
    */
   public static void telaInicial() {
      clear();
      System.out.println(header + menuLogin);
   }


   /*
    * telaPergunta - Apresenta a tela de perguntas
    */
   public static void telaPergunta() {
      clear();
      System.out.println(header + menuPerguntas);
   }

   /*
    *  telaErro - Apresenta a tela de erro
    */
   public static void telaErro() {
      clear();
      System.out.println(header + mensagemErro);
   }

   public static Usuario login(){
      Acesso.acessoSistema();
   }


   /*
    * main - funcao principal de acesso
    */
   public static void main(String[] args){
      telaInicial();
      telaErro();
      telaPergunta();
      /*
      //declaracoes 
      Scanner leitor = new Scanner(System.in);
      int escolha = 0; 
      String buffer;
      try{
         arquivo = new CRUD<Usuario>(Usuario.class.getConstructor(), "usuarios.db");
   
         do{
            clear();
            telaInicial();
            buffer = leitor.nextLine();
            //arquivo.delete(12);
            try{
               escolha = Integer.parseInt(buffer);
            }
            catch (NumberFormatException e){ 
               escolha = -1;
            }
  
            switch(escolha){
               case 0:
                  telaFinal(leitor);
                  break;
               case 1:
                  try{
                     acessoSistema(leitor);
                  }
                  catch (Exception e){
                     telaErro(leitor);
                  }
                  break;
               case 2:
                  try{
                     novoUsuario(leitor);
                  }
                  catch (Exception e){
                     telaErro(leitor);
                  }
                  break;
               case 3:
                  try{
                     esqueciSenha(leitor);
                  }
                  catch (Exception e){
                     telaErro(leitor);
                  }
                  break;
               default: 
                  System.out.println("\nEscolha Inválida");
                  pause(leitor);
            }
         }while(escolha!=0);
     
      }
      catch (Exception e){
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("ERRO NA BASE DE DADOS: "+e.toString().replace("MyException: ",""));
         System.out.println();
         System.out.println("TENTE NOVAMENTE MAIS TARDE");
         System.out.println();
         pause(leitor);
      }*/
   } 
}