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
   acessoSistem - Gerencia o acesso ao Sistema
   @param Scanner
   */
   public static void acessoSistema(Scanner leitor) throws Exception{
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("ACESSO AO SISTEMA");
      System.out.println();
      
      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      Usuario user = arquivo.read(email);
      if(user == null){
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO NÃO CADASTRADO");
         System.out.println();
         System.out.println("SELECIONE OPÇÃO (2) PARA CRIAR USUÁRIO");
         pause(leitor);
      }
      else{
         System.out.println();
         System.out.print("Senha: ");
         String senha = leitor.nextLine();
         
         if(senha.equals(user.senha)){
            if(senha.equals("123456")){
               novaSenha(leitor, user.getID());              
            }
            else{
               clear();
               System.out.println();
               System.out.println(_NOME+" "+_VERSAO);
               System.out.println("==================");
               System.out.println();
               System.out.println("AUTENTICAÇÃO REALIZADA COM SUCESSO");
               System.out.println();
               System.out.println("AGUARDE NOVIDADES");
               pause(leitor); 
            }
         }
         else{
            clear();
            System.out.println();
            System.out.println(_NOME+" "+_VERSAO);
            System.out.println("==================");
            System.out.println();
            System.out.println("SENHA INCORRETA");
            System.out.println();
            System.out.println("CASO TENHA ESQUECIDO A SENHA SELECIONE OPÇÃO (3)");
            pause(leitor);
         }
      } 
   }

   /*
   novaSenha - Realiza a troca da senha
   @param Scanner, int id
   */
   public static void novaSenha(Scanner leitor, final int id) throws Exception{
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("TROCA DE SENHA");
      System.out.println();
      
      System.out.print("Senha atual: ");
      String senha_atual = leitor.nextLine();
      System.out.print("\nNova senha: ");
      String nova_senha = leitor.nextLine();
      System.out.print("\nConfirme a nova senha: ");
      String nova_senha2 = leitor.nextLine();
       
      Usuario user = arquivo.read(id);
   
      if(!nova_senha.equals(nova_senha2) || !senha_atual.equals(user.senha)){
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHAS INCORRETAS");
         System.out.println();
         System.out.println("TENTE NOVAMENTE");
         pause(leitor);
      }
      else{
         user.senha = nova_senha;
         arquivo.update(user);
        
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHA ATUALIZADA COM SUCESSO");
         System.out.println();
         System.out.println("FAÇA SEU LOGIN PARA ACESSAR O SISTEMA");
         pause(leitor); 
      } 
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
        
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("CONFIRME OS DADOS:");
         System.out.println();
         System.out.println("NOME: "+nome);
         System.out.println("EMAIL: "+email);
         System.out.println();
         System.out.print("CONFIRME OS DADOS (SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();
 
         if(confirmacao.contains("S")){
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
   }
   
   /*
   esqueciSenha - Recupera a senha
   @param Scanner
   */
   public static void esqueciSenha(Scanner leitor) throws Exception{
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("RECUPERAÇÃO DA SENHA");
      System.out.println();
      
      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      Usuario user = arquivo.read(email);
      if(user == null){
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO NÃO CADASTRADO");
         System.out.println();
         System.out.println("SELECIONE OPÇÃO (2) PARA CRIAR USUÁRIO");
         pause(leitor);
      }
      else{
         user.senha = "123456";
         arquivo.update(user);
        
         clear();
         System.out.println();
         System.out.println(_NOME+" "+_VERSAO);
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHA TEMPORÁRIA CRIADA");
         System.out.println();
         System.out.println("SENHA ENVIADA PARA O EMAIL: "+email);
         System.out.println();
         System.out.println("SENHA: "+user.senha);
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
