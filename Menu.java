/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/


import java.util.Date;
import java.text.SimpleDateFormat;
import aed3.*;
import java.util.Scanner;
import java.io.File;


public class Menu{

   private static String _NOME = "PERGUNTAS";
   private static String _VERSAO = "1.0";

   public static String header = "==============\n" + _NOME + "  " + _VERSAO
                                 + "\n==============\n";

   public static String menuLogin = "\nACESSO\n" + "\n[1] Acesso ao sistema\n" + "[2] Novo Usuário"
                                    + "(primeiro acesso)\n" + "[3] Esqueci minha senha" + "\n[0] Sair\n" 
                                    + "\nOpção: ";
  
   public static String menuPerguntas = "INÍCIO\n" + "\n[1] Gerenciar perguntas\n" + "[2] Consultar/"
                                       + "Responder perguntas\n" + "[3] Notificações: getNotificacoes" 
                                       + "\n[0] Sair\n" + "\nOpção: ";

   public static String gerencPerg = "\nPERGUNTAS\n" + "\n[1] Listar\n" + "[2] Incluir\n"
                                       + "[3] Alterar\n" + "[4] Arquivar" + "\n[0] Sair\n" 
                                       + "\nOpção: ";
   
   public static String consultPerg = "\n1) Responder\n" + "2) Comentar\n" + "3) Avaliar\n" + "\n0) Retornar\n" + "\nOpção: ";
   public static String menuResp = "1) Listar suas respostas\n2) Incluir uma resposta\n3) Alterar uma resposta\n4)"
                                   + " Arquivar uma resposta\n0) Retornar ao menu anterior\n\nOpção: _";
   public static String mensagemErro = "\nERRO\n" + "\nDesculpe, ocorreu um" + "erro inesperado\n" 
                                       + "Tente novamente mais tarde";

   private static Usuario online = null ;
   private static Pergunta perg = null;
   private static int notificacoes; //seria isso um dado do usuário armazenado no arquivo? Se sim, criar novo campo no BD
   private static Scanner leitor = new Scanner(System.in);
   private static byte estado;
   
   //inicializa atributos estáticos das classes Acesso e Pergunta
   private static void inicializar() throws Exception{
      
      //Se não existir, cria um diretório DBs
      File DBs = new File("DBs");
      if(!DBs.exists())
         DBs.mkdir();
      
      Comentario.arquivo = new CRUD<Comentario>(Comentario.class.getConstructor(), "DBs/comentario.db");
      Comentario.indiceComPer = new ArvoreBMais_Int_Int(5, "DBs/indiceComPerg.db");
      Comentario.indiceComResp = new ArvoreBMais_Int_Int(5, "DBs/indiceComResp.db");
      Acesso.arquivo = new CRUD<Usuario>(Usuario.class.getConstructor(), "DBs/usuarios.db");
      Pergunta.arquivo = new CRUD<Pergunta>(Pergunta.class.getConstructor(), "DBs/perguntas.db");
      Resposta.arquivo = new CRUD<Resposta>(Resposta.class.getConstructor(), "DBs/respostas.db");
      Voto.arquivo = new CRUD<Voto>(Voto.class.getConstructor(), "DBs/votos.db");
      Pergunta.data = new Date();
      Pergunta.indice = new ArvoreBMais_Int_Int(5, "DBs/indicePerguntas.db");
      Pergunta.formatter = new SimpleDateFormat();
      Pergunta.listaChaves = new ListaInvertida(20, "DBs/palavrasChave.db", "DBs/palavrasBlocos.db");
      Resposta.indicePergResp = new ArvoreBMais_Int_Int(5, "DBs/indicePergResp.db");
      Resposta.indiceUserResp = new ArvoreBMais_Int_Int(5, "DBs/indiceUserResp.db");
      estado = 1;
   }


   //funcoes da interface
   public static String getNome(){
      return _NOME;
  }

  public static String getVersao(){
      return _VERSAO;
  }

   /*
   pause - realiza uma pausa até o novo input
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

   /* Método para leitura de entrada do usuário
    * @return int escolha
    */
   public static int lerEscolha(){
      int escolha = 0; 

      //if (leitor.hasNextLine())//limpar buffer
        //    leitor.nextLine();

      String buffer = leitor.nextLine();;
      try{
            escolha = Integer.parseInt(buffer);
      }
      catch (NumberFormatException e){ 
            escolha = -1;
      }
      return escolha;
   }

   /*
    * menuInicio - método para login, update e criação de usuário
    */
   public static void menuInicio(){
      clear();
      System.out.println(header + menuLogin);

      int escolha = lerEscolha();
      switch(escolha){
         case 0:
            telaFinal(leitor);
            estado = 0;
            break;
         case 1:
            try{
               login();
               if(online != null)
                  estado = 2;
            }
            catch (Exception e){
               telaErro();
            }
            break;
         case 2:
            try{
               Acesso.novoUsuario(leitor);
            }
            catch (Exception e){
               telaErro();
            }
            break;
         case 3:
            try{
               Acesso.esqueciSenha(leitor);
            }
            catch (Exception e){
               telaErro();
            }
            break;
         default: 
            System.out.println("\nEscolha Inválida");
            pause(leitor);
      }
   }
   /*
    * login - recebe o retorno da função acessoSistema da classe Acesso (que é um objeto Usuario, se o login for bem sucedido. Caso contrário, null)
    */
   public static void login() throws Exception{
      online = Acesso.acessoSistema(leitor);
   }

   /*
    *  telaErro - Apresenta a tela de erro
    */
   public static void telaErro(){
      clear();
      System.out.println(header + mensagemErro);
      pause(leitor);
   }

   /*
   telaFinal - Apresenta tela final -- nao utilizada
   @Scanner leitor
   */
   public static void telaFinal(Scanner leitor){
      clear();
      System.out.println();
      System.out.println(_NOME+" "+_VERSAO);
      System.out.println("==================");
      System.out.println();
      System.out.println("Obrigado por utilizar nosso software!");
      System.out.println();
      System.out.println("Volte sempre.");
      System.out.println();
      pause(leitor);
   }

   /*
    */
    public static void menuPerguntas()throws Exception{
      clear();
      System.out.println(header);
      System.out.print(menuPerguntas);

      int escolha = lerEscolha();
      switch(escolha){
         case 0:
            estado = 1;
            break;
         case 1:
            try{
               estado = 3;
               menuGerencPerg();
            }
            catch (Exception e){
               telaErro();
            }
            break;

         case 2:
            menuConsultarPerg();
            break;

         case 3:
            System.out.println("Estamos trabalhando nisso...\nAguarde novidades");
            pause(leitor);
            break;

         default: 
            System.out.println("\nEscolha Inválida");
            pause(leitor);
      }
    }


   /*
    * menuGerencPerg - Apresenta a tela e operações de gerenciamento 
    * de perguntas
    */
   public static void menuGerencPerg(){
      clear();
      System.out.println(header);
      System.out.println("PERGUNTAS > GERENCIAR PERGUNTAS\n");
      System.out.print(gerencPerg);

      int escolha = lerEscolha();
      switch(escolha){
         case 0:
            estado = 2;
            break;
         case 1:
            try{
               Pergunta.printPerguntas(online.getID(), true);
               pause(leitor);
            }
            catch (Exception e){
               pause(leitor);
            }
            break;
         case 2:
            try{
               Pergunta.novaPergunta(leitor, online.getID());
            }
            catch (Exception e){
               telaErro();
            }
            break;
         case 3:
            try{
               Pergunta.alterarPergunta(leitor, online.getID());
            }
            catch (Exception e){
               telaErro();
            }
            break;
         case 4:
            try{
               Pergunta.arquivarPergunta(leitor, online.getID());
            }
            catch (Exception e){
               telaErro();
            }
            break;
         default: 
            System.out.println("\nEscolha Inválida");
            pause(leitor);
      }
   }

   public static void menuConsultarPerg(){
      clear();
      System.out.println(header);
      System.out.println("PERGUNTAS > CONSULTAR PERGUNTAS\n");
      perg = null;
      try{
         perg = Pergunta.consultarPerguntas(leitor);
      }catch(Exception e){
         telaErro();
      }
      int escolha;
      

      if(perg != null){
         escolha = lerEscolha(); 
      } else{
         escolha = 0; 
         pause(leitor);
      }
      estado = Pergunta.escolhaMenuPergunta(escolha, leitor);
   }

   public static void menuResponder(){
      clear();
      System.out.println(header);
      System.out.println("PERGUNTAS > CONSULTAR PERGUNTAS > RESPONDER\n");

      try{
         Pergunta.printPerguntaCompleta(Pergunta.arquivo.read(perg.getID()));
         System.out.println(menuResp);
      }catch(Exception e){
         estado = 3;
         telaErro();
      }
      

      int escolha = lerEscolha(); 

      switch(escolha){
         case 0:
            estado = 4;
            break;
         case 1: 
            try{
               Resposta.printRespUsuario(online.getID(), perg.getID());
               pause(leitor);
            }catch(Exception e){
               telaErro();
            }
            break;
         case 2:
            try{
               Resposta.novaResposta(perg.getID(), online.getID(), leitor);
            }catch(Exception e){
               telaErro();
            }
            pause(leitor);
            break;
         case 3:
            try{
               Resposta.alterarResp(perg.getID(), online.getID(), leitor);
               pause(leitor);
            }catch(Exception e){
               telaErro();
            }
            break;
         case 4:
            try{
               Resposta.arquivarPerg(perg.getID(), online.getID(), leitor);
               pause(leitor);
            }catch(Exception e){
               telaErro();
            }
            break;
         default:
            System.out.println("\nEscolha inválida");
            pause(leitor);
      }
   }

   public static void menuVotar(){
      clear();
      System.out.println(header);
      System.out.println("PERGUNTAS > CONSULTAR PERGUNTAS > VOTAR\n");
      int escolha = -1;
      try{
         escolha = Voto.escolherPergOuResp(leitor, perg);
      }catch(Exception e){
         telaErro();
      }

      switch(escolha){
         case 0:
            estado = 7;
            break;
         case 1:
            try{
               Voto.votarPergunta(online.getID(), leitor, perg);
               estado = 7;
            }catch(Exception e){
               telaErro();
            }
            break;
         case 2:
            try{
               Voto.votarResposta(online.getID(), leitor, perg);
               estado = 7;
            }catch(Exception e){
               telaErro();
            }
            break;
         default:
            System.out.println("\nEscolha inválida");
            pause(leitor);
      }
   }

   public static void menuComentar(){
      clear();
      System.out.println(header);
      System.out.println("PERGUNTAS > CONSULTAR PERGUNTAS > VOTAR\n");
      int escolha = -1;
      try{
         escolha = Comentario.escolherPergOuResp(leitor, perg);
      }catch(Exception e){
         telaErro();
      }

      switch(escolha){
         case 0:
            estado = 7;
            break;
         case 1:
            try{
               Comentario.comentarPerg(online.getID(), leitor, perg);
               estado = 7;
            }catch(Exception e){
               telaErro();
            }
            break;
        /* case 2:
            try{
               Voto.votarResposta(online.getID(), leitor, perg);
               estado = 7;
            }catch(Exception e){
               telaErro();
            }
            break;*/
         default:
            System.out.println("\nEscolha inválida");
            pause(leitor);
      }
   }

   /*
    * main - funcao principal de interação com o usuário
    */
   public static void main(String[] args){
      //declaracoes 
      String buffer;
      try{
         inicializar();

         do{
            switch(estado){
               case 1:
                  menuInicio();
                  break;
               case 2:
                  menuPerguntas();
                  break;
               case 3:
                  menuGerencPerg();
                  break;
               case 4:
                  menuConsultarPerg();
                  break;
               case 5:
                  menuResponder();
                  break;
               case 6:
                  menuVotar();
                  break;
               case 7: //estado para visualizar pergunta selecionada 
                  try{
                     Pergunta.exibirMenuPergunta(perg);
                  }catch(Exception e){
                     telaErro();
                  }
                  int escolha = lerEscolha();
                  estado = Pergunta.escolhaMenuPergunta(escolha, leitor);
                  break;
               case 8:
                  menuComentar();
                  break;
               default:
                  telaErro();
                  estado = 0;
            }
            
         }while(estado!=0);
     
      }
      catch (Exception e){
         System.out.println(getNome()+" "+getVersao());
         System.out.println("==================");
         System.out.println();
         System.out.println("ERRO NA BASE DE DADOS: "+e.toString().replace("MyException: ",""));
         System.out.println();
         System.out.println("TENTE NOVAMENTE MAIS TARDE");
         System.out.println();
         pause(leitor);
      }
   } 
}
