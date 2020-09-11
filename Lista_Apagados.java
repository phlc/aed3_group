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
Byte - Lapide - Indicador se o node deste índice está apagado
Short - tamanho do registro no arquivo originário 
Long - Posição do registro no arquivo originário
Long - Posição do próximo node
*/


public class Lista_Apagados {
   
//atributos da classe
   final private static int TAMANHO_NODE = 19; //Byte+Short+Long+Long
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
      long resp = -1;
      long node;      

      //ler a posicao do primeiro node da lista
      arq.seek(INICIO);
      node = arq.readLong;
      

   }
}

