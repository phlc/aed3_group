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
import java.util.ArrayList;
import aed3.*;

import java.text.SimpleDateFormat;


 class Resposta implements Registro{
   //Atributos estáticos
   public static ArvoreBMais_Int_Int indicePergResp;
   public static ArvoreBMais_Int_Int indiceUserResp;
   public static CRUD<Resposta> arquivo;

   //Métodos estáticos

   /* Metodo para listar respostas do usuário logado na pergunta selecionada
    * @param int id do usuario online, id da pergunta selecionada 
    */
   public static void printRespUsuario(int idUser, int idPerg)throws Exception{
      int[] ids = indicePergResp.read(idPerg);
      int cont = 0;
      for (int i : ids){
         Resposta p = arquivo.read(i);
         if(p.idUsuario == idUser){
            System.out.println((++cont)+".\n");
            System.out.println(p.resposta);
            System.out.println("Nota:" + p.nota + "\n");
         }
      } 
   }

   /* Método para listar todas respostas de uma determinada pergunta
    * @param int id da pergunta
    */
   public static void printRespostas(int id)throws Exception{
      int[] ids = indicePergResp.read(id);
      for(int i = 0; i < ids.length; i++){
         System.out.println((i+1)+".\n");
         Resposta p = arquivo.read(ids[i]);
         System.out.println(p.resposta);
         System.out.println("Nota:" + p.nota + "\n");
         System.out.println("Respondido em: " + (new SimpleDateFormat("dd/MM/yyyy")).format(p.criacao) +   
                     " às " + (new SimpleDateFormat("hh:mm")).format(p.criacao) + 
                     " por " + (Acesso.arquivo.read(p.idUsuario)).nome);
         Comentario.printComentariosResp(p.getID());
      }
   }

   /* Método para incluir resposta em uma determinada pergunta
    * @para int id da pergunta, id do usuario Scanner leitor
    * @return boolean true resposta inserida, false resposta não inserida
    */
   public static boolean novaResposta(int idPerg, int idUsuario, Scanner leitor)throws Exception{
      System.out.println("Digite a resposta: ");
      String resp = leitor.nextLine();
      if(!resp.equals("")){
         System.out.println("\nCONFIRME A CRIAÇÃO DA RESPOSTA: ");
         System.out.println(resp);
         System.out.print("(SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();
         if(confirmacao.contains("S")){
            Resposta nova = new Resposta(idPerg, idUsuario, resp);
            arquivo.create(nova);
            indicePergResp.create(idPerg, nova.getID());
            indiceUserResp.create(idUsuario, nova.getID());
            return true;
         }else{
            System.out.println("Inclusão cancelada.");
            return false;
         }
      }else{
         System.out.println("Reposta inválida.");
         return false;
      }
   }
  
   /* Método para alterar resposta em uma determinada pergunta pelo usuario logado
    * @para int id da pergunta, id do usuario Scanner leitor
    * @return boolean true resposta alterada, false resposta não alterada
    */
   public static boolean alterarResp(int idPerg, int idUser, Scanner leitor)throws Exception{
      int[] ids = indicePergResp.read(idPerg);
      ArrayList<Resposta> al = new ArrayList<Resposta>();
      int cont = 0;

      for (int i : ids){
         Resposta p = arquivo.read(i);
         if(p.idUsuario == idUser){
            al.add(p);
            System.out.println((++cont)+".\n");
            System.out.println(p.resposta);
            System.out.println("Nota:" + p.nota + "\n");
         }
      } 

      System.out.println("Qual resposta deseja alterar?");
      int r = Menu.lerEscolha();
      if(r > 0 && r <= al.size()){
         Resposta p = al.get(r-1);

         System.out.println("Resposta escolhida para alteração: ");
         System.out.println(p.resposta);
         System.out.println("Nota:" + p.nota + "\n");

         System.out.println("Digite a nova resposta: ");
         String nova = leitor.nextLine();

         if(!nova.equals("")){
            System.out.println("\nCONFIRME A ALTERAÇÃO DA RESPOSTA: ");
            System.out.println(nova);
            System.out.print("(SIM(S) NÃO(N)): ");
            String confirmacao = leitor.nextLine();
            confirmacao = confirmacao.toUpperCase();

            if(confirmacao.contains("S")){
               p.resposta = nova;
               arquivo.update(p);
               System.out.println("Resposta alterada com sucesso!");
               return true;
            }else{ 
               System.out.println("Alteração cancelada.");
               return false;
            }
            
         }else{
            System.out.println("Campo deixado em branco, alteração cancelada.");
            return false;
         }
      }else{
         System.out.println("Escolha inválida.");
         return false;
      }
   }

   public static boolean arquivarPerg(int idPerg, int idUser, Scanner leitor)throws Exception{
      int[] ids = indicePergResp.read(idPerg);
      ArrayList<Resposta> al = new ArrayList<Resposta>();
      int cont = 0;

      for (int i : ids){
         Resposta p = arquivo.read(i);
         if(p.idUsuario == idUser){
            al.add(p);
            System.out.println((++cont)+".\n");
            System.out.println(p.resposta);
            System.out.println("Nota:" + p.nota + "\n");
         }
      }

      System.out.println("Qual resposta deseja arquivar?");
      int r = Menu.lerEscolha();
      if(r > 0 && r <= al.size()){
         Resposta p = al.get(r-1);
            
         System.out.println("Resposta escolhida para arquivar: ");
         System.out.println(p.resposta);
         System.out.println("Nota:" + p.nota + "\n");
            
         System.out.println("\nCONFIRME O ARQUIVAMENTO DA RESPOSTA: ");
         System.out.print("(SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();

         if(confirmacao.contains("S")){
            p.ativa = false;
            arquivo.update(p);
            indicePergResp.delete(idPerg, p.idResposta);
            indiceUserResp.delete(idUser, p.idResposta);
            System.out.println("Resposta arquivada com sucesso!");
            return true;
         }else{ 
            System.out.println("Resposta não arquivada.");
            return false;
         }

      }else{
         System.out.println("Escolha inválida.");
         return false;
      }

   }
    
   //Atributos 
   private int idResposta;
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
      return(this.idResposta);
   }
   
   /*
    * setID - atribui um ID para um objeto
    * @param int n
    */
   public void setID(int n){
     this.idResposta = n;
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