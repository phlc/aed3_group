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
import java.util.ArrayList;

/*
Classe Usuario
*/

class Pergunta implements Registro{
   //atributos estaticos
   public static Date data;
   public static SimpleDateFormat formatter;
   public static ArvoreBMais_Int_Int indice;
   public static CRUD<Pergunta> arquivo;
   public static int[] listaPerguntas;


   //metodos estaticos

   /*Método para carregar perguntas de novo usuário logado
    * @param - int idUsuario
    */
   public static void carregarPerguntas(int idUsuario)throws Exception{
      listaPerguntas = indice.read(idUsuario);
   }

   //obter listagem de perguntas feitas por um usuário
   public static void printPerguntas(int idUsuario) throws Exception{
      carregarPerguntas(idUsuario);
      for(int i = 0; i < listaPerguntas.length; i++){
         Pergunta p = arquivo.read(listaPerguntas[i]);

         System.out.println("\n" + (i+1) + ".\n" + formatter.format(new Date(p.criacao)) + "\n" + p.pergunta);
      }
   }

   //criar pergunta no indice quando criar no arquivo principal
   public static void createAtIndex(int userId, Pergunta p) throws IOException{
      indice.create(userId, p.idPergunta);
   }
   /*
    * novaPergunta - Método para usuário criar adicionar uma nova pergunta 
    * em sua conta
    */
   public static void novaPergunta(Scanner leitor, int idUsuario) throws Exception{
      System.out.println("\nInsira a pergunta:");
      String buffer = leitor.nextLine();

      if(!buffer.equals("")){
         System.out.println("\nCONFIRME A CRIAÇÃO DA PERGUNTA: ");
         System.out.println("\""+buffer+"\"");
         System.out.print("(SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();
 
         if(confirmacao.contains("S")){
            Pergunta nova = new Pergunta(idUsuario, buffer);
            arquivo.create(nova);            
            indice.create(idUsuario, nova.getID());
         }else{
            System.out.println("Inclusão de pergunta cancelada");
         }

      }
   }
   //atributos
   public int idUsuario; //chave de busca
   private int idPergunta; //dado
   public long criacao;
   public short nota;
   public String pergunta;
   public boolean ativa;

   //construtores
   public Pergunta(){
      this(-1, -1, -1, Short.MIN_VALUE, "", false);
   }

   public Pergunta(int _idUsuario, String _pergunta){
      this(-1, _idUsuario, System.currentTimeMillis(), (short)0, _pergunta, true);
   }

   public Pergunta(int _idPergunta, int _idUsuario, long _criacao, short _nota, String _pergunta, boolean _ativa){
      this.idUsuario = _idUsuario;
      this.idPergunta = _idPergunta;
      this.criacao = _criacao;
      this.nota = _nota;
      this.pergunta = _pergunta;
      this.ativa = _ativa;
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