/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import java.util.Date;
import java.text.SimpleDateFormat;
import aed3.ArvoreBMais_Int_Int;
import aed3.ListaInvertida;
import java.util.ArrayList;
import java.text.Normalizer;

/*
Classe Usuario
*/

class Pergunta implements Registro{
   //atributos estaticos
   public static Date data;
   public static SimpleDateFormat formatter;
   public static ArvoreBMais_Int_Int indice;
   public static CRUD<Pergunta> arquivo;
   public static ListaInvertida listaChaves;
   public static int[] listaPerguntas;


   //metodos estaticos

   /*Método para carregar perguntas de novo usuário logado
    * @param - int idUsuario
    */
   public static void carregarPerguntas(int idUsuario)throws Exception{
      listaPerguntas = indice.read(idUsuario);
   }

   /* Obter print de listagem de perguntas feitas por um usuário, boolean para definir se 
    * perguntas arquivadas serão consideradas na hora do print
    * @param int idUsuario, boolean printArq
    */
   public static void printPerguntas(int idUsuario, boolean printArq) throws Exception{
      carregarPerguntas(idUsuario);
      for(int i = 0; i < listaPerguntas.length; i++){
         Pergunta p = arquivo.read(listaPerguntas[i]);
         if(p.ativa){
            System.out.println("\n" + (i+1) + ".\n" + formatter.format(new Date(p.criacao)) + "\n" + p.pergunta);
            System.out.println("[" + p.palavrasChave + "] ");
         }
         else if(printArq){
            System.out.println("\n" + (i+1) + ". (Arquivada)\n" + formatter.format(new Date(p.criacao)) 
            + "\n" + p.pergunta);
            System.out.print("[" + p.palavrasChave + "] ");
         }
      }
   }

   //criar pergunta no indice quando criar no arquivo principal
   public static void createAtIndex(int userId, Pergunta p) throws IOException{
      indice.create(userId, p.idPergunta);
   }

   /* Método para tratar palavras chaves de pesquisa, retornando valores sem caracteres
    * especiais e com letras minúsculas
    * @param String chaves
    * @return String[] arrayChaves
    */
    private static String[] tratarChaves(String chaves){
      //Decompor caracteres especiais
      chaves = Normalizer.normalize(chaves, Normalizer.Form.NFD);
      //Continuar somente com caracteres ASCII e transformar todos em minúsculos
      chaves = chaves.replaceAll("[^\\p{ASCII}]", "").toLowerCase();

      return chaves.split(";");
    }

   /*
    * novaPergunta - Método para usuário adicionar uma nova pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void novaPergunta(Scanner leitor, int idUsuario) throws Exception{
      System.out.println("\nInsira a pergunta:");
      String buffer = leitor.nextLine();

      System.out.println("\nInsira as palavras chaves delimitadas pelo caracter ';':");
      String chaves = leitor.nextLine();

      if(!buffer.equals("") && !chaves.equals("")){
         String[] chavesArray = tratarChaves(chaves);

         System.out.println("\nCONFIRME A CRIAÇÃO DA PERGUNTA: ");
         System.out.println("\""+buffer+"\"");
         System.out.println("\nQUE CONTÉM AS PALAVRAS CHAVE: ");
         System.out.println("\""+chaves+"\"");

         System.out.print("(SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();
 
         if(confirmacao.contains("S")){
            Pergunta nova = new Pergunta(idUsuario, buffer, chaves);
            arquivo.create(nova);            
            indice.create(idUsuario, nova.getID());
            
            //Incluindo palavras chave
            for(int i = 0; i < chavesArray.length; i++)
               listaChaves.create(chavesArray[i], nova.idPergunta);

            System.out.println("Pergunta incluída com sucesso!");
         }else{
            System.out.println("Inclusão de pergunta cancelada.");
         }

      }else{
         System.out.println("Inclusão de pergunta cancelada. Alguns campos não foram preenchidos.");
      }
   }

   /*
    * alterarPergunta - Método para usuário alterar uma pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void alterarPergunta(Scanner leitor, int idUsuario) throws Exception{
      printPerguntas(idUsuario, false);

      System.out.println("\nQual pergunta dejesa alterar: ");
      int escolha = Menu.lerEscolha();

      if(0 < escolha && escolha <= listaPerguntas.length){

         Pergunta p = arquivo.read(listaPerguntas[escolha-1]);

         if(p.ativa){
            System.out.println("Pergunta escolhida: "+p.pergunta);
            System.out.println("\nInsira a alteração da pergunta:");
            String buffer = leitor.nextLine();
            
            if(!buffer.equals("")){
               System.out.println("\nCONFIRME A ALTERAÇÃO DA PERGUNTA: ");
               System.out.println("\""+buffer+"\"");
               System.out.print("(SIM(S) NÃO(N)): ");
               String confirmacao = leitor.nextLine();
               confirmacao = confirmacao.toUpperCase();
      
               if(confirmacao.contains("S")){
                  System.out.println("Pergunta alterada com sucesso!");
                  p.pergunta = buffer;
                  arquivo.update(p);            
               }else{
                  System.out.println("Alteração de pergunta cancelada.");
               }

            }
         }else{
            System.out.println("\nPergunta escolhida arquivada, tente outra.");
         }
      }else{
         System.out.println("\nPergunta escolhida não existe, tente outra.");
      }

      Menu.pause(leitor);
   }

   /*
    * arquivarPergunta - Método para usuário arquivar uma pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void arquivarPergunta(Scanner leitor, int idUsuario) throws Exception{
      printPerguntas(idUsuario, false);

      System.out.println("\nQual pergunta dejesa alterar: ");
      int escolha = Menu.lerEscolha();

      if(0 < escolha && escolha <= listaPerguntas.length){

         Pergunta p = arquivo.read(listaPerguntas[escolha-1]);

         if(p.ativa){
            System.out.println("Pergunta escolhida: "+p.pergunta);
             
            
            System.out.println("\nCONFIRME O ARQUIVAMENTO DA PERGUNTA: ");
            System.out.print("(SIM(S) NÃO(N)): ");
            String confirmacao = leitor.nextLine();
            confirmacao = confirmacao.toUpperCase();
      
            if(confirmacao.contains("S")){
               System.out.println("Pergunta alterada com sucesso!");
               p.ativa = false;
               arquivo.update(p);            
            }else{
               System.out.println("Alteração de pergunta cancelada.");
            }
         }else{
            System.out.println("\nPergunta escolhida arquivada, tente outra.");
         }
      }else{
         System.out.println("\nPergunta escolhida não existe, tente outra.");
      }

      Menu.pause(leitor);
   }

   //atributos
   public int idUsuario; //chave de busca
   private int idPergunta; //dado
   public long criacao;
   public short nota;
   public String pergunta;
   public boolean ativa;
   public String palavrasChave;

   //construtores
   public Pergunta(){
      this(-1, -1, -1, Short.MIN_VALUE, "", false, "");
   }

   public Pergunta(int _idUsuario, String _pergunta, String _palavrasChave){
      this(-1, _idUsuario, System.currentTimeMillis(), (short)0, _pergunta, true, _palavrasChave);
   }

   public Pergunta(int _idPergunta, int _idUsuario, long _criacao, short _nota, String _pergunta, boolean _ativa, 
                   String _palavrasChave){
      this.idUsuario = _idUsuario;
      this.idPergunta = _idPergunta;
      this.criacao = _criacao;
      this.nota = _nota;
      this.pergunta = _pergunta;
      this.ativa = _ativa;
      this.palavrasChave = _palavrasChave;
   }

//metodos
   /*
   getID - retorna o ID de um objeto
   @return int id
   */
   public int getID(){
      return(this.idPergunta);
   }
   
   /*
   setID - atribui um ID para um objeto
   @param int n
   */
   public void setID(int n){
      this.idPergunta = n;
   }

   /*
   toByteArray - retorna o conteudo do objeto com byte[]
   @return byte[] ba
   */
   public byte[] toByteArray() throws IOException{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.writeInt(this.idUsuario);
      dos.writeInt(this.idPergunta);
      dos.writeLong(this.criacao);
      dos.writeShort(this.nota);
      dos.writeUTF(this.pergunta);
      dos.writeBoolean(this.ativa);
      dos.writeUTF(this.palavrasChave);

      return(baos.toByteArray());
   }
      
   /*
   fromByteArray - preenche o objeto a partir de um byte[]
   @param byte[] ba
   */
   public void fromByteArray(byte[] ba) throws IOException{
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
   
      this.idUsuario = dis.readInt();
      this.idPergunta = dis.readInt();
      this.criacao = dis.readLong();
      this.nota = dis.readShort();
      this.pergunta = dis.readUTF();
      this.ativa = dis.readBoolean();
      this.palavrasChave = dis.readUTF();
   }

   /*
   toString - forma uma String a partir dos dados do objeto
   Este método exibe dados relevantes para o funcionamento interno do sistema e, portanto, não deve ser
   usado para exibição de dados ao usuário
   @return String
   */
   public String toString(){
      String resp = "\nID: "+this.idPergunta;
      resp = resp + "\nID do Usuário: "+this.idUsuario;
      resp = resp + "\nCriação (milisegundos): "+this.criacao;
      resp = resp + "\nNota: "+this.nota;
      resp = resp + "\nPergunta: "+this.pergunta;
      resp = resp + "\nAtiva: "+this.ativa;
      resp = resp + "\nAtiva: "+this.palavrasChave;
      return(resp);
   }

   /*
   chaveSecundaria - retorna a chave secundaria
   No caso das perguntas, retorna null
   */
   public String chaveSecundaria(){
      return Integer.toString(this.idPergunta);
   }

}