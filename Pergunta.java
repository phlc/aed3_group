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

   //metodos estaticos
   //obter listagem de perguntas feitas por um usuário
   public static ArrayList<String> getList(int idUsuario) throws Exception{
      ArrayList<String> lista = new ArrayList<String>();
      int c = 1;
      int[] dados = indice.read(idUsuario);
      for(int i = 0; i < dados.length; i++){
         Pergunta p = arquivo.read(dados[i]);
         String item = "" + c + ".\n" + formatter.format(new Date(p.criacao)) + "\n" + p.pergunta;
         lista.add(item);
         c++;
      }
      return lista;
   }

   //criar pergunta no indice quando criar no arquivo principal
   public static void createAtIndex(int userId, Pergunta p) throws IOException{
      indice.create(userId, p.idPergunta);
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
      return null;
   }

}
