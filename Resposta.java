/*
 * Ciencia da Computacao - PUC minas
 * AED3 - manhã
 * Larissa Domingues Gomes
 * Marcelo Franca Cabral
 * Pedro Henrique Lima Carvalho
 * Tarcila Fernanda Resende da Silva
 */

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import aed3.*;

 class Resposta implements Registro{
    //Atributos estáticos
    public static ArvoreBMais_Int_Int indiceRespUser;
    public static ArvoreBMais_Int_Int indiceUserResp;

    //Métodos estáticos

    //Atributos 
    public int idResposta;
    public int idPergunta;
    public int idUsuario;
    public long criacao;
    public short nota;
    public String resposta;
    public boolean ativa;

    //Construtores
    public Resposta(){
        this(-1, -1, "");
    }

    public Resposta(int idPergunta, int idUsuario, String resposta){
        this(-1, idPergunta, idUsuario, System.currentTimeMillis(), (short)0, resposta, true);
    }

    public Resposta(int idResposta, int idPergunta, int idUsuario, long criacao, 
                    short nota, String resposta, boolean ativa){
        this.idResposta = idResposta;
        this.idPergunta = idPergunta;
        this.idUsuario = idUsuario;
        this.criacao = criacao;
        this.nota = nota;
        this.resposta =resposta;
        this.ativa = ativa;
    }

    //metodos

    /*
     * getID - retorna o ID de um objeto
     * @return int id
     */
    public int getID(){
       return(this.idPergunta);
    }
   
    /*
     * setID - atribui um ID para um objeto
     * @param int n
     */
    public void setID(int n){
       this.idPergunta = n;
    }
    
    /*
     * toByteArray - retorna o conteudo do objeto com byte[]
     * @return byte[] ba
     */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.idResposta);
        dos.writeInt(this.idPergunta);
        dos.writeInt(this.idUsuario);
        dos.writeLong(this.criacao);
        dos.writeShort(this.nota);
        dos.writeUTF(this.resposta);
        dos.writeBoolean(this.ativa);

        return(baos.toByteArray());
    } 
    


    /*
     * fromByteArray - preenche o objeto a partir de um byte[]
     *  @param byte[] ba
     */
    public void fromByteArray(byte[] ba) throws IOException{
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
      
      this.idResposta = dis.readInt();
      this.idPergunta = dis.readInt();
      this.idUsuario = dis.readInt();
      this.criacao = dis.readLong();
      this.nota = dis.readShort();
      this.resposta = dis.readUTF();
      this.ativa = dis.readBoolean();
     
    }
    
    /*
     * toString - forma uma String a partir dos dados do objeto
     * Este método exibe dados relevantes para o funcionamento interno do sistema e, portanto, não deve ser
     * usado para exibição de dados ao usuário
     * @return String
     */
    public String toString(){
      String resp = "\nID Resp: " + this.idResposta;
      resp = resp + "\nID Perg: " + this.idPergunta;
      resp = resp + "\nID Usua: " + this.idUsuario;
      resp = resp + "\nID Criação: " + this.criacao;
      resp = resp + "\nID Nota: " + this.nota;
      resp = resp + "\nResposta: " + this.resposta;
      resp = resp + "\nAtiva: " + this.ativa;
      return(resp);
    }

    /*
     * chaveSecundaria - retorna a chave secundaria
     * No caso das respostas, retorna idPergunta => CRUD não aceita "" ou null
     */
    public String chaveSecundaria(){
       return Integer.toString(this.idResposta);
    }
 }