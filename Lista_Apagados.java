/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/

/*
Lista Encadeada Short - Long 
Indice direto para apontar a posição no arquivo de registros apagados
*/

import java.io.*;

/*
Lista Encadeada para ser usada como índice direto para registros apagados
CHAVE: Short - Tamanho do registro
DADO:  Long  - Posição do registro no arquivo

Formato da lista:
Byte 0-1: Short - Número de nodes da lista apagados
Byte 2-9: Long  - Posição do primeiro node da lista
Byte 10.: Nodes 

Formato do node:
Short - chave:     tamanho do registro no arquivo originário 
Long  - dado:      Posição do registro no arquivo originário
Long  - prox_node: Posição do próximo node
*/


public class Lista_Apagados {
   
//atributos da classe
   final private static int TAMANHO_NODE = 18;    //Short+Long+Long
   final private static short POS_APAGADOS = 0;   //Posicao do numero de nodes pagados
   final private static long INICIO = 2;          //Posicao do endereco do primeiro node    
   final private static String CONTROLE_A = "a_"; //início nome do arquivo para controle
   final private static String CONTROLE_B = "b_"; //início nome do arquivo para controle

//atributos
   private RandomAccessFile arquivo;
   private String nome_arquivo;   
   private String controle; //para controle do nome do arquivo quando limpar apagados 

//construtor
   public Lista_Apagados(String nome) throws Exception{
      //Verificar se já existe o arquivo
      if(new File(CONTROLE_A+nome).exists()){
         this.nome_arquivo = CONTROLE_A+nome;
         this.controle = CONTROLE_A;
         this.arquivo = new RandomAccessFile(nome_arquivo, "rws");
      }
      else if(new File(CONTROLE_B+nome).exists(){ 
         this.nome_arquivo = CONTROLE_B+nome;
         this.controle = CONTROLE_B;
         this.arquivo = new RandomAccessFile(nome_arquivo "rws");        
      }
      else{
         this.nome_arquivo = CONTROLE_A+nome;
         this.controle = CONTROLE_A;
         this.arquivo = new RandomAccessFile(nome_arquivo, "rws");
         
         //escrever cabeçalho
         arquivo.seek(POS_APAGADOS);
         arquivo.writeShort(0);
         arquivo.seek(INICIO);
         arquivo.writeLong(-1); //lista vazia
      }
   }
   
//metodos
   /*
   read - Le da lista a posição de um registro apagado no principal com tamanho suficiente
   @param short chave
   @return long dado ou -1 se inexistente
   */
   public long read(short chave){
      long dado = -1; //resposta
      long node;      //posição primeiro node

      //ler a posicao do primeiro node da lista
      arquivo.seek(INICIO);
      node = arquivo.readLong;
      
      //se a lista não está vazia, buscar recursivamente      
      if(node != -1)
         dado = read(chave, node);
      
      return dado;
   }
   /*
   overload read
   @param short chave, long node
   */
   private long read(short chave, long node){
      long dado = -1; //resposta
      short tam_lido; //tamanho do registro referenciado pelo node
      long dado_lido; //posição do registro referenciado pelo node
      long prox_node; //posição do próximo node
      
      //ler dados do arquivo
      arquivo.seek(node);
      tam_lido = arquivo.readShort();
      dado_lido = arquivo.readLong();
      prox_node = arquivo.readLong();
      
      //verificar tamanho do registro referenciado
      if(chave <= tam_lido)
         dado = dado_lido;
      else if (prox_node != -1)
         dado = read(tamanho, prox_node);

      return dado;
   }

   /*
   insert - insere um novo node na lista de forma ordenada
   @param short tamanho long posicao
   */
   public void insert(short tamanho, long dado){
      long node;            //posição primeiro node
      long endereco;        //endereco a ser escrito      

      //ler a posição do primeiro node da lista
      arquivo.seek(INICIO);
      node = readLong();

      //se a lista nao está vazia, inserir na posicao certar recursivamente
      if(node != -1){
         endereco = insert(tamanho, posicao, node);
      } 
      //lista vazia, criar primeiro node
      else{
         //armazenar endereco do novo node e ir para a posicao
         endereco = arq.length(); //posicao do novo node
         arq.seek(endereco);

         //escrever dados
         arquivo.writeShort(tamanho);
         arquivo.writeLong(posicao);
         arquivo.writeLong(-1);
      }

      //verificar se INICIO mudou
      if(node != endereco){
         arquivo.seek(INICIO);
         arquivo.writeLong(endereco);
      }
   }
   /*
   overload insert
   @param short tamanho, long dado, long node


}



