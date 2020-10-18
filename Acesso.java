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

   public static CRUD<Usuario> arquivo;

   /*
   telaFinal - Apresenta tela final -- nao utilizada
   @Scanner leitor
   
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
   */
   /*
   acessoSistem - Gerencia o acesso ao Sistema
   @param Scanner
   */
   public static Usuario acessoSistema(Scanner leitor) throws Exception{
      Menu.clear();
      Usuario user = null;
      System.out.println();
      System.out.println(Menu.getNome()+" "+Menu.getVersao());
      System.out.println("==================");
      System.out.println();
      System.out.println("ACESSO AO SISTEMA");
      System.out.println();

      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      user = arquivo.read(email);
      if(user == null){
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("Usuário não cadastrado");
         System.out.println();
         System.out.println("SELECIONE OPÇÃO [2] PARA CRIAR USUÁRIO");
         Menu.pause(leitor);
      }
      else{
         System.out.println();
         System.out.print("Senha: ");
         String senha = leitor.nextLine();
         
         if(senha.equals(user.senha)){
            if(senha.equals("123456")){ //se a senha do usuário é uma senha temporária
               novaSenha(leitor, user.getID());              
            }
            else{  
               Menu.clear();
               System.out.println();
               System.out.println(Menu.getNome()+" "+Menu.getVersao());
               System.out.println("==================");
               System.out.println();
               System.out.println("AUTENTICAÇÃO REALIZADA COM SUCESSO");
               System.out.println();
               Menu.pause(leitor); 
            }
         }
         else{
            Menu.clear();
            System.out.println();
            System.out.println(Menu.getNome()+" "+Menu.getVersao());
            System.out.println("==================");
            System.out.println();
            System.out.println("SENHA INCORRETA");
            System.out.println();
            System.out.println("CASO TENHA ESQUECIDO A SENHA SELECIONE OPÇÃO [3]");
            Menu.pause(leitor);
            user = null;
         }
      }
      return user; 
   }

   /*
   novaSenha - Realiza a troca da senha
   @param Scanner, int id
   */
   public static void novaSenha(Scanner leitor, final int id) throws Exception{
      Menu.clear();
      System.out.println();
      System.out.println(Menu.getNome()+" "+Menu.getVersao());
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
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHAS INCORRETAS");
         System.out.println();
         System.out.println("TENTE NOVAMENTE");
         Menu.pause(leitor);
         novaSenha(leitor, id);
      }
      else{
         user.senha = nova_senha;
         arquivo.update(user);
        
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHA ATUALIZADA COM SUCESSO");
         System.out.println();
         System.out.println("FAÇA SEU LOGIN PARA ACESSAR O SISTEMA");
         Menu.pause(leitor); 
      } 
   }
   
   /*
   novoUsuario - cadastra um novo usuário
   @param Scanner
   */
   public static void novoUsuario(Scanner leitor) throws Exception{
      Menu.clear();
      System.out.println();
      System.out.println(Menu.getNome()+" "+Menu.getVersao());
      System.out.println("==================");
      System.out.println();
      System.out.println("NOVO USUÁRIO");
      System.out.println();
      
      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      if(arquivo.read(email) != null){
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO JÁ CADASTRADO");
         System.out.println();
         System.out.println("CASO TENHA ESQUECIDO A SENHA SELECIONE OPÇÃO (3)");
         Menu.pause(leitor);
      }
      else{
         System.out.print("\nNome: ");
         String nome = leitor.nextLine();
      
         System.out.print("\nSenha: ");
         String senha = leitor.nextLine();
        
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
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
            Menu.clear();
            System.out.println();
            System.out.println(Menu.getNome()+" "+Menu.getVersao());
            System.out.println("==================");
            System.out.println();
            System.out.println("USUÁRIO CADASTRADO COM SUCESSO");
            System.out.println();
            System.out.println("FAÇA SEU LOGIN PARA ACESSAR O SISTEMA");
            Menu.pause(leitor);
         }
      } 
   }
   
   /*
   esqueciSenha - Recupera a senha
   @param Scanner
   */
   public static void esqueciSenha(Scanner leitor) throws Exception{
      Menu.clear();
      System.out.println();
      System.out.println(Menu.getNome()+" "+Menu.getVersao());
      System.out.println("==================");
      System.out.println();
      System.out.println("RECUPERAÇÃO DA SENHA");
      System.out.println();
      
      System.out.print("E-mail: ");
      String email = leitor.nextLine();

      Usuario user = arquivo.read(email);
      if(user == null){
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("USUÁRIO NÃO CADASTRADO");
         System.out.println();
         System.out.println("SELECIONE OPÇÃO (2) PARA CRIAR USUÁRIO");
         Menu.pause(leitor);
      }
      else{
         user.senha = "123456";
         arquivo.update(user);
        
         Menu.clear();
         System.out.println();
         System.out.println(Menu.getNome()+" "+Menu.getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("SENHA TEMPORÁRIA CRIADA");
         System.out.println();
         System.out.println("SENHA ENVIADA PARA O EMAIL: "+email);
         System.out.println();
         System.out.println("SENHA: "+user.senha);
         System.out.println();
         System.out.println("FAÇA SEU LOGIN PARA ACESSAR O SISTEMA");
         Menu.pause(leitor); 
      } 
   }

}