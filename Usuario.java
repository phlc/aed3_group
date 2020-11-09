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
import java.text.DecimalFormat;

/*
Classe Usuario
*/

class Usuario implements Registro{

//atributos
   private int idUsuario;
   public String nome;
   public String email;
   public String senha;

//contrutores
   public Usuario(int i, String n, String e, String s){
      this.idUsuario = i;
      this.nome = n;
      this.email = e;
      this.senha = s;
   }

   public Usuario(){
      this.idUsuario = -1;
      this.nome = "";
      this.email = "";
      this.senha = "";
   }

//metodos
   /*
   getID - retorna o ID de um objeto
   @return int id
   */
   public int getID(){
      return(this.idUsuario);
   }
   
   /*
   setID - atribui um ID para um objeto
   @param int n
   */
   public void setID(int n){
      this.idUsuario = n;
   }

   /*
   toByteArray - retorna o conteudo do objeto com byte[]
   @return byte[] ba
   */
   public byte[] toByteArray() throws IOException{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.writeInt(this.idUsuario);
      dos.writeUTF(this.nome);
      dos.writeUTF(this.email);
      dos.writeUTF(this.senha);

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
      this.nome = dis.readUTF();
      this.email = dis.readUTF();
      this.senha = dis.readUTF();
   }

   /*
   toString - forma uma String a partir dos dados do objeto
   @return String
   */
   public String toString(){
      String resp = "\nID: "+this.idUsuario;
      resp = resp + "\nNome: "+this.nome;
      resp = resp + "\nEmail: "+this.email;
      resp = resp + "\nSenha: "+this.senha;
      return(resp);
   }

   /*
   chaveSecundaria - retorna a chave secundaria
   @return String da chave secundaria
   */
   public String chaveSecundaria(){
      return(this.email);
   }

}
