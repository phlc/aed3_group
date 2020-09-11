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
VALOR: Long - Posição do registro no arquivo

Formato da lista:
Byte 0-1: Short - Número de nodes da lista apagados
Byte 2-9: Long - Posição do primeiro node da lista
Byte 10 - Nodes

Formato do node:
Short - tamanho do registro no arquivo originário 
Long - Posição do registro no arquivo originário
Long - Posição do próximo node
*/


public class Lista_Apagados {
   
//atributos da classe
   final private static int TAMANHO_NODE = 18; //Short+Long+Long
   final private static short POS_APAGADOS = 0; //Posicao do numero de nodes pagados
   final private static long INICIO = 2; //Posicao do endereco do primeiro node    
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
   @param short tamanho necessário
   @return long posição ou -1 se inexistente
   */
   public long read(short tamanho){
      long resp = -1; //resposta
      long node;      //posição primeiro node

      //ler a posicao do primeiro node da lista
      arquivo.seek(INICIO);
      node = arquivo.readLong;
      
      //se a lista não está vazia, buscar recursivamente      
      if(node != -1)
         resp = read(tamanho, node);
      
      return resp;
   }
   /*
   sobrecarga de read
   @param short tamanho, long node
   */
   private long read(short tamanho, long node){
      long resp = -1; //resposta
      short tam_lido; //tamanho do registro referenciado pelo node
      long posicao;   //posição do registro referenciado pelo node
      long prox_node; //posição do próximo node
      
      //ler dados do arquivo
      arquivo.seek(node);
      tam_lido = arquivo.readShort();
      posicao = arquivo.readLong();
      prox_node = arquivo.readLong();
      //verificar tamanho do registro referenciado
      if(tamanho < tam_lido)
         resp = posicao;
      else if (prox_node != -1)
         resp = read(tamanho, prox_node);

      return resp;
   }

   /*
   insert - insere um novo node na lista de forma ordenada
   @param short tamanho long posicao
   @return boolean confirmacao
   */
   public boolean insert(short tamanho, long posicao){
      boolean resp = false; //resposta
      long node;            //posição primeiro node
      
      //ler a posição do primeiro node da lista
      arquivo.seek(INICIO);
   
   
   ]


}



