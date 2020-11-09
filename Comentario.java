/*
 * Ciencia da Computacao - Pucminas
 * AED3 - manha
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
import java.text.SimpleDateFormat;



 class Comentario implements Registro{
    //Atributos estáticos
    public static CRUD<Comentario> arquivo;
    public static ArvoreBMais_Int_Int indiceComPer;
    public static ArvoreBMais_Int_Int indiceComResp;

    //Métodos estáticos
    /* Metodo para imprimir na tela comentarios de uma determinada pergunta
     * @param int idPergunta
     */
    public static void printComentariosResp(int idResposta)throws Exception{
      int[] ids = indiceComResp.read(idResposta);
      int cont = 0;
      for (int i : ids){
        Comentario p = arquivo.read(i);
        System.out.println((++cont)+".\n");
        System.out.println("\t"+p.comentario);
        System.out.println("\tComentado em: " + (new SimpleDateFormat("dd/MM/yyyy")).format(p.criacao) +   
                           "às " + (new SimpleDateFormat("hh:mm")).format(p.criacao) + 
                           " por " + (Acesso.arquivo.read(p.idUsuario)).nome);
        
      } 
    }

    /* Metodo para imprimir na tela comentarios de uma determinada pergunta
     * @param int idPergunta
     */
    public static void printComentariosPerg(int idPergunta)throws Exception{
      int[] ids = indiceComPer.read(idPergunta);
      int cont = 0;
      for (int i : ids){
        Comentario p = arquivo.read(i);
        System.out.println((++cont)+".\n");
        System.out.println(p.comentario);
        System.out.println("Comentado em: " + (new SimpleDateFormat("dd/MM/yyyy")).format(p.criacao) +   
                           " às " + (new SimpleDateFormat("hh:mm")).format(p.criacao) + 
                           " por " + (Acesso.arquivo.read(p.idUsuario)).nome);
        
      } 
    }


    /* Metodo para o usuário escolher se o comentário será para a pergunta o uma resposta
     * @param Scanner leitor, Pergunta pergunta
     */
    public static int escolherPergOuResp(Scanner leitor, Pergunta pergunta) throws Exception{
        //novo estado
        Menu.clear();

        Pergunta.printPerguntaCompleta(pergunta);
        System.out.println("RESPOSTAS\n---------\n");
        Resposta.printRespostas(pergunta.getID());

        System.out.println("Deseja comentar na pergunta ou em uma das respostas?");
        System.out.println("1) Pergunta\n2) Resposta\n\n0) Voltar");
        System.out.print("Opção: ");
        
        int escolha = Menu.lerEscolha();
        return escolha;
    }

    /* Metodo para comentar em pergunta
     * @param int idUsuario, Scanner leitor, Pergunta pergunta, int escolha
     */
    public static void comentarPerg(int idUsuario, Scanner leitor, Pergunta pergunta) throws Exception{
        Menu.clear();
        
        Pergunta.printPerguntaCompleta(pergunta);
        System.out.println("Digite o comentário:");
        String com = leitor.nextLine();

        if(!com.equals("")){
            System.out.println("Confirma a inclusão do comentário na pergunta acima?");
            System.out.print("(SIM(S) NÃO(N)): ");
            String confirmacao = leitor.nextLine();
            confirmacao = confirmacao.toUpperCase();

            if(confirmacao.contains("S")){
                Comentario novo = new Comentario(idUsuario, (byte)'P', pergunta.getID(), com);
                arquivo.create(novo);
                indiceComPer.create(novo.idPR, novo.idComentario);
                System.out.println("Comentario Incluido com sucesso!");
                Menu.pause(leitor);
            }else{
                System.out.println("Inclusão de comentário cancelada.");
                Menu.pause(leitor);
            }

        }else{
            System.out.println("Comentário inválido.");
            Menu.pause(leitor);
        }
        
    }
    /* Metodo para comentar em resposta 
     * @param int idUsuario, Scanner leitor, Pergunta pergunta, int escolha
     */
    public static void comentarResp(int idUsuario, Scanner leitor, Pergunta pergunta) throws Exception{
        Menu.clear();
        
        Pergunta.printPerguntaResumida(pergunta);
        System.out.println("RESPOSTAS\n---------\n");
        Resposta.printRespostas(pergunta.getID());

        int[] idsRespostas = Resposta.indicePergResp.read(pergunta.getID());
        if(idsRespostas.length == 0){
            System.out.println("ESsa pergunta ainda não foi respondida!");
            Menu.pause(leitor);
        }else{
            System.out.println("Em qual resposta deseja comentar?");
            System.out.print("\nOpção: ");
            int escolha = Menu.lerEscolha();
            if(1 <= escolha && escolha <= idsRespostas.length){
                Resposta r = Resposta.arquivo.read(idsRespostas[escolha-1]);
                System.out.println("Resposta escolhida: "+ r.resposta+"\n");
                System.out.println("Digite o comentário:");
                String com = leitor.nextLine();

                if(!com.equals("")){
                    System.out.println("Confirma a inclusão do comentário na resposta acima?");
                    System.out.print("(SIM(S) NÃO(N)): ");
                    String confirmacao = leitor.nextLine();
                    confirmacao = confirmacao.toUpperCase();

                    if(confirmacao.contains("S")){
                        Comentario novo = new Comentario(idUsuario, (byte)'R', r.getID(), com);
                        arquivo.create(novo);
                        indiceComResp.create(novo.idPR, novo.idComentario);
                        System.out.println("Comentario Incluido com sucesso!");
                        Menu.pause(leitor);
                    }else{
                        System.out.println("Inclusão de comentário cancelada.");
                        Menu.pause(leitor);
                    }
                }else{
                    System.out.println("Comentário inválido!");
                }
                
            }else{
                System.out.println("Resposta inválida!");
                Menu.pause(leitor);
            }
        }
    }

    //Atributos
    private int idComentario;
    public int idUsuario;
    public byte tipo;
    public int idPR;
    public String comentario;
    public long criacao;

    //Construtores
    public Comentario(){
        this(-1, -1, (byte)-1, -1, "", -1);
    }

    //Construtores
    public Comentario(int idUsuario, byte tipo, int idPR, String comentario){
        this(-1, idUsuario, tipo, idPR, comentario, System.currentTimeMillis());
    }

    public Comentario(int _idComentario, int _idUsuario, byte _tipo, int _idPR, String _comentario, long _criacao){
        this.idComentario = _idComentario;
        this.idUsuario = _idUsuario;
        this.tipo = _tipo;
        this.idPR = _idPR;
        this.comentario = _comentario;
        this.criacao = _criacao;
    }

    //metodos
    /*
    * getID - retorna o ID de um objeto
    * @return int id
    */
    public int getID(){
        return(this.idComentario);
    }

    /*
    * chaveSecundaria - retorna a chave secundaria do Voto
    * @return chaveSec
    */
    public String chaveSecundaria(){
        return idUsuario + "|" + tipo + "|" + idPR; //pressupondo que 'tipo' == valor de 'P' ou 'R' na tabela ASCII
    }

   /*
    * setID - atribui um ID para um objeto
    * @param int n
    */
    public void setID(int n){
        this.idComentario = n;
    }

    /*
     * toByteArray - retorna o conteudo do objeto com byte[]
     * @return byte[] ba
     */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.idComentario);
        dos.writeInt(this.idUsuario);
        dos.writeByte(this.tipo);
        dos.writeInt(this.idPR);
        dos.writeUTF(this.comentario);
        dos.writeLong(this.criacao);
        return(baos.toByteArray());
    }
    
   /*
    * fromByteArray - preenche o objeto a partir de um byte[]
    *  @param byte[] ba
    */
    public void fromByteArray(byte[] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.idComentario = dis.readInt();
        this.idUsuario = dis.readInt();
        this.tipo = dis.readByte();
        this.idPR = dis.readInt();
        this.comentario = dis.readUTF();
        this.criacao = dis.readLong();
    }
 }
