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
   final private static short POS_APAGADOS = 0;   //Posicao do numero de nodes pagados
   final private static int MAX_APAGADOS = 4;     //Numero máximo de apagados antes da limpeza
   final private static long INICIO = 2;          //Posicao do endereco do primeiro node    
   final private static String CONTROLE_A = "a_"; //início nome do arquivo para controle
   final private static String CONTROLE_B = "b_"; //início nome do arquivo para controle

//atributos
   private RandomAccessFile arquivo;
   private String nome_arquivo;   
   private String prox_nome; //para controle do nome do arquivo quando limpar apagados 

//construtor
   public Lista_Apagados(String nome) throws Exception{
      //Verificar se já existe o arquivo
      if(new File(CONTROLE_A+nome).exists()){
         this.nome_arquivo = CONTROLE_A+nome;
         this.prox_nome = CONTROLE_B+nome;
         this.arquivo = new RandomAccessFile(nome_arquivo, "rws");
      }
      else if(new File(CONTROLE_B+nome).exists()){ 
         this.nome_arquivo = CONTROLE_B+nome;
         this.prox_nome = CONTROLE_A+nome;
         this.arquivo = new RandomAccessFile(nome_arquivo, "rws");     
      }
      else{
         this.nome_arquivo = CONTROLE_A+nome;
         this.prox_nome = CONTROLE_B+nome;
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
   public long read(short chave) throws Exception{
      long dado = -1; //resposta
      long cabeca;      //posição primeiro node

      //ler a posicao do primeiro node da lista
      arquivo.seek(INICIO);
      cabeca = arquivo.readLong();
      
      //se a lista não está vazia, buscar recursivamente      
      if(cabeca != -1)
         dado = read(chave, cabeca);
      
      return dado;
   }
   /*
   overload read
   @param short chave, long node
   */
   private long read(short chave, long node) throws Exception{
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
         dado = read(chave, prox_node);

      return dado;
   }

   /*
   insert - insere um novo node na lista de forma ordenada
   @param short chave long dado
   */
   public void insert(short chave, long dado) throws Exception{
      long cabeca;          //posição primeiro node
      long endereco;        //endereco a ser escrito      

      //ler a posição do primeiro node da lista
      arquivo.seek(INICIO);
      cabeca = arquivo.readLong();

      //se a lista nao está vazia, inserir na posicao certar recursivamente
      if(cabeca != -1){
         endereco = insert(chave, dado, cabeca);
      } 
      //lista vazia, criar primeiro node
      else{
         //armazenar endereco do novo node e ir para a posicao
         endereco = arquivo.length(); //posicao do novo node
         arquivo.seek(endereco);

         //escrever dados
         arquivo.writeShort(chave);
         arquivo.writeLong(dado); 
         arquivo.writeLong(-1); //proximo node
      }

      //verificar se INICIO mudou
      if(cabeca != endereco){
         arquivo.seek(INICIO);
         arquivo.writeLong(endereco);
      }
   }
   /*
   overload insert
   @param short tamanho, long dado, long node
   @return long node
   */
   private long insert(short chave, long dado, long node) throws Exception{ 
      short tam_lido; //tamanho do registro referenciado pelo node
      long dado_lido; //posição do registro referenciado pelo node
      long ponteiro;  //posição do ponteiro para o proximo node
      long prox_node; //posição do próximo node
      long endereco;  //posição do novo node      

      //ler dados do arquivo
      arquivo.seek(node);
      tam_lido = arquivo.readShort();
      dado_lido = arquivo.readLong();
      ponteiro = arquivo.getFilePointer();
      prox_node = arquivo.readLong();
      
      //verificar se tamanho é adequado
      if(chave < tam_lido){
         //armazenar endereco do novo node e ir para a posicao
         endereco = arquivo.length(); //posicao do novo node
         arquivo.seek(endereco);
      
         //escrever dados
         arquivo.writeShort(chave);
         arquivo.writeLong(dado);
         arquivo.writeLong(node);
         node = endereco;
      }
      else{
         //verificar se existe proximo node
         if(prox_node != -1){
            endereco = insert(chave, dado, prox_node);
            
            //verificar se o proximo mudou
            if(prox_node != endereco){
               arquivo.seek(ponteiro);
               arquivo.writeLong(endereco);
            }
         } 
         //se no final da lista
         else{
            //armazenar endereco do novo node e ir para a posicao
            endereco = arquivo.length(); //posicao do novo node
            arquivo.seek(endereco);

            //escrever dados
            arquivo.writeShort(chave);
            arquivo.writeLong(dado); 
            arquivo.writeLong(-1); //proximo node

            //escrever ponteiro
            arquivo.seek(ponteiro);
            arquivo.writeLong(endereco);
         }   
      }
      return node;
   }

   /*
   remove - remove um node da lista
   @param long dado - dado é a posição do registro no arquivo originário
   @return boolean
   */
   public boolean remove(long dado) throws Exception{
      boolean confirmacao = false;
      long cabeca;      //posição primeiro node

      //ler a posicao do primeiro node da lista
      arquivo.seek(INICIO);
      cabeca = arquivo.readLong();
      
      //se a lista não está vazia, buscar recursivamente      
      if(cabeca != -1)
         confirmacao = remove(dado, cabeca, INICIO);
      
      return confirmacao;
   }
   /*
   overload remove
   @param long dado long node long
   @return boolean
   */
   private boolean remove(long dado, long node, long anterior) throws Exception{
      boolean confirmacao = false;
      short tam_lido; //tamanho do registro referenciado pelo node
      long dado_lido; //posição do registro referenciado pelo node
      long ponteiro;  //posição do ponteiro para o proximo node
      long prox_node; //posição do próximo node      

      //ler dados do arquivo
      arquivo.seek(node);
      tam_lido = arquivo.readShort();
      dado_lido = arquivo.readLong();
      ponteiro = arquivo.getFilePointer();
      prox_node = arquivo.readLong();
     
      //verificar se é o node a ser deletado
      if(dado_lido == dado){
         //escrever no ponteiro anterior o proximo ponteiro
         arquivo.seek(anterior);
         arquivo.writeLong(prox_node);
         
         //atualizar o cabeçalho
         arquivo.seek(POS_APAGADOS);
         short apagados = arquivo.readShort();
         apagados++;

         //verifica se excedido o máximo de apagados.
         if(apagados > MAX_APAGADOS){
            clear();
         }
         else{
            arquivo.seek(POS_APAGADOS);
            arquivo.writeShort(apagados);
         }

         //confirmar exclusao
         confirmacao = true;
      }
      //Nao é fim da lista
      else if(prox_node != -1){
         confirmacao = remove(dado, prox_node, ponteiro);
      }
         
      return confirmacao;
   }

   /*
   clear - cria um novo arquivo da lista sem os apagados
   */
   private void clear() throws Exception{
      //apagar eventual prox_nome
      new File(prox_nome).delete();
      
      //criar novo arquivo com o prox_nome
      RandomAccessFile novo = new RandomAccessFile(prox_nome, "rws");
      
      //dados
      short tam_lido;
      long dado_lido;
      long prox_node;

      
      //escrever cabeçalho novo
      novo.writeShort(0);
      
      //obter posicao do primeiro node do arquivo
      arquivo.seek(INICIO);
      prox_node = arquivo.readLong();

      //escrever nodes
      while(prox_node != -1){
         novo.writeLong(novo.getFilePointer()+8);

         arquivo.seek(prox_node);
         tam_lido = arquivo.readShort();
         dado_lido = arquivo.readLong();
         prox_node = arquivo.readLong();

         novo.writeShort(tam_lido);
         novo.writeLong(dado_lido);
      }
   
      //escrever o ultimo ponteiro -1
      novo.writeLong(-1);

      //trocar nomes dos arquivos
      String buffer = nome_arquivo;
      nome_arquivo = prox_nome;
      prox_nome = buffer;
      
      //trocar arquivo do objeto
      arquivo.close();
      arquivo = novo;
      
      //apagar arquivo anterior
      new File(prox_nome).delete();
   }
}



