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
   private static CRUD<Usuario> arquivo;
   


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
   telaErro - Apresenta tela de erro
   @Scanner leitor
   */
   public static void telaErro(Scanner leitor){
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("DESCULPE, OCORREU UM ERRO INESPERADO");
      System.out.println();
      System.out.println("TENTE NOVAMENTE MAIS TARDE");
      System.out.println();
      pause(leitor);
   }

   /*
   telaFinal - Apresenta tela final
   @Scanner leitor
   */
   public static void telaFinal(Scanner leitor){
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("OBRIGADO POR CONTRIBUIR PARA NOSSA COMUNIDADE");
      System.out.println();
      System.out.println("VOLTE SEMPRE");
      System.out.println();
      pause(leitor);
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
   clear - limpa a tela do terminal
   */
   public static void clear(){
      System.out.print("\033[H\033[2J");  
      System.out.flush();  
   }

   /*
   novoUsuario - cadastra um novo usuário
   @param Scanner
   */
   public static void novoUsuario(Scanner leitor) throws Exception{
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("NOVO USUÁRIO");
      System.out.println();
      
      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      if(arquivo.read(email) != null){
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO JÁ CADASTRADO");
         System.out.println();
         System.out.println("CASO TENHA ESQUECIDO A SENHA SELECIONE OPÇÃO (3)");
         pause(leitor);
      }
      else{
         System.out.print("\nNome: ");
         String nome = leitor.nextLine();
      
         System.out.print("\nSenha: ");
         String senha = leitor.nextLine();

         Usuario novo = new Usuario(-1, nome, email, senha);
         arquivo.create(novo);

         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO CADASTRADO COM SUCESSO");
         System.out.println();
         System.out.println("FAÇA SEU LOGIN PARA ACESSAR O SISTEMA");
         pause(leitor);

     } 
   }

   /*
   main - funcao principal de acesso
   */
   public static void main(String[] args){
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
                  System.out.println("\nTeste login");
                  pause(leitor);
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
                  System.out.println("\nTeste esqueci senha");
                  pause(leitor);
                  break;
               default: 
                  System.out.println("\nEscolha Inválida");
                  pause(leitor);
            }
         }while(escolha!=0);
   
      }
      catch (Exception e){
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("BASE DE DADOS INDISPONÍVEL");
         System.out.println();
         System.out.println("TENTE NOVAMENTE MAIS TARDE");
         System.out.println();
         pause(leitor);
      }
   } 
}
