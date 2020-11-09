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

class Voto implements Registro{
    //Atributos estáticos
    public static CRUD<Voto> arquivo;

    //Métodos estáticos
    public static int escolherPergOuResp(Scanner leitor, Pergunta pergunta) throws Exception{
        //novo estado
        Menu.clear();

        Pergunta.printPerguntaCompleta(pergunta);
        System.out.println("RESPOSTAS\n---------\n");
        Resposta.printRespostas(pergunta.getID());

        System.out.println("Deseja votar na pergunta ou em uma das respostas?");
        System.out.println("1) Pergunta\n2) Resposta\n\n0) Voltar");
        System.out.print("Opção: ");
        
        int escolha = Menu.lerEscolha();
        return escolha;
    }

    public static void votarPergunta(int idUsuario, Scanner leitor, Pergunta pergunta) throws Exception{
        Menu.clear();
        byte tipoVoto = '-';
        int id_PR = -1;
        Voto voto = null;

        Pergunta.printPerguntaCompleta(pergunta);
        id_PR = pergunta.getID();
        tipoVoto = 'P';

        voto = new Voto(-1, idUsuario, tipoVoto, id_PR, false);

        if(arquivo.read(voto.chaveSecundaria()) != null){
            System.out.println("Você já votou nesta pergunta! Não é possível votar novamente.");
            Menu.pause(leitor);
        }else{
            System.out.println("Deseja dar um voto positivo ou negativo?");
            System.out.println("1) Positivo\n2) Negativo");
            System.out.print("\nOpção: ");
            int escolha = Menu.lerEscolha();
            if(escolha == 1){
                System.out.println("Confirma o voto POSITIVO na pergunta acima?");
                System.out.print("(SIM(S) NÃO(N)): ");
                String confirmacao = leitor.nextLine();
                confirmacao = confirmacao.toUpperCase();
                if(confirmacao.contains("S")){
                    voto.voto = true;
                    arquivo.create(voto);
                    pergunta.nota = (short)(pergunta.nota + 1);
                    Pergunta.arquivo.update(pergunta);
                    System.out.println("Voto registrado com sucesso!");
                    Menu.pause(leitor);
                }else{
                    System.out.println("Voto cancelado.");
                    Menu.pause(leitor);
                }
            }else{
                System.out.println("Confirma o voto NEGATIVO na pergunta acima?");
                System.out.print("(SIM(S) NÃO(N)): ");
                String confirmacao = leitor.nextLine();
                confirmacao = confirmacao.toUpperCase();
                if(confirmacao.contains("S")){
                    voto.voto = false;
                    arquivo.create(voto);
                    pergunta.nota = (short)(pergunta.nota - 1);
                    Pergunta.arquivo.update(pergunta);
                    System.out.println("Voto registrado com sucesso!");
                    Menu.pause(leitor);
                }else{
                    System.out.println("Voto cancelado.");
                    Menu.pause(leitor);
                }
            }
        }
    }

    public static void votarResposta(int idUsuario, Scanner leitor, Pergunta pergunta) throws Exception{
        Menu.clear();
        byte tipoVoto = '-';
        int id_PR = -1;
        Voto voto = null;

        Pergunta.printPerguntaResumida(pergunta);
        System.out.println("RESPOSTAS\n---------\n");
        Resposta.printRespostas(pergunta.getID());

        int[] idsRespostas = Resposta.indicePergResp.read(pergunta.getID());
        if(idsRespostas.length == 0){
            System.out.println("ESsa pergunta ainda não foi respondida!");
            Menu.pause(leitor);
        }else{
            System.out.println("Em qual resposta deseja votar?");
            System.out.print("\nOpção: ");
            int escolhaResp = Menu.lerEscolha();

            id_PR = idsRespostas[escolhaResp-1];
            Resposta resposta = Resposta.arquivo.read(id_PR);
            tipoVoto = 'R';

            voto = new Voto(-1, idUsuario, tipoVoto, id_PR, false);

            if(arquivo.read(voto.chaveSecundaria()) != null){
                System.out.println("Você já votou nesta resposta! Não é possível votar novamente.");
                Menu.pause(leitor);
            }else{
                System.out.println("Deseja dar um voto positivo ou negativo?");
                System.out.println("1) Positivo\n2) Negativo");
                System.out.print("\nOpção: ");
                int escolha = Menu.lerEscolha();
                if(escolha == 1){
                    System.out.println("Confirma o voto POSITIVO na resposta acima?");
                    System.out.print("(SIM(S) NÃO(N)): ");
                    String confirmacao = leitor.nextLine();
                    confirmacao = confirmacao.toUpperCase();
                    if(confirmacao.contains("S")){
                        voto.voto = true;
                        arquivo.create(voto);
                        resposta.nota = (short)(resposta.nota + 1);
                        Resposta.arquivo.update(resposta);
                        System.out.println("Voto registrado com sucesso!");
                        Menu.pause(leitor);
                    }else{
                        System.out.println("Voto cancelado.");
                        Menu.pause(leitor);
                    }
                }else{
                    System.out.println("Confirma o voto NEGATIVO na resposta acima?");
                    System.out.print("(SIM(S) NÃO(N)): ");
                    String confirmacao = leitor.nextLine();
                    confirmacao = confirmacao.toUpperCase();
                    if(confirmacao.contains("S")){
                        voto.voto = false;
                        arquivo.create(voto);
                        resposta.nota = (short)(resposta.nota - 1);
                        Resposta.arquivo.update(resposta);
                        System.out.println("Voto registrado com sucesso!");
                        Menu.pause(leitor);
                    }else{
                        System.out.println("Voto cancelado.");
                        Menu.pause(leitor);
                    }
                }
            }
        }
    }

    //Atributos
    private int idVoto;
    public int idUsuario;
    public byte tipo;
    public int idPR;
    public boolean voto;

    //Construtores
    public Voto(){
        this(-1, -1, (byte)-1, -1, false);
    }

    public Voto(int _idVoto, int _idUsuario, byte _tipo, int _idPR, boolean _voto){
        idVoto = _idVoto;
        idUsuario = _idUsuario;
        tipo = _tipo;
        idPR = _idPR;
        voto = _voto;
    }

    //metodos
    /*
    * getID - retorna o ID de um objeto
    * @return int id
    */
    public int getID(){
        return(this.idVoto);
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
        this.idVoto = n;
    }

   /*
    * toByteArray - retorna o conteudo do objeto com byte[]
    * @return byte[] ba
    */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.idVoto);
        dos.writeInt(this.idUsuario);
        dos.writeByte(this.tipo);
        dos.writeInt(this.idPR);
        dos.writeBoolean(this.voto);
        return(baos.toByteArray());
    }

   /*
    * fromByteArray - preenche o objeto a partir de um byte[]
    *  @param byte[] ba
    */
    public void fromByteArray(byte[] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.idVoto = dis.readInt();
        this.idUsuario = dis.readInt();
        this.tipo = dis.readByte();
        this.idPR = dis.readInt();
        this.voto = dis.readBoolean();
    }

} 