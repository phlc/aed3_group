/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/

import java.io.*;
import java.util.*;

/*
Árvore B+ Int - Int 
Indice indireto entre ID de duas entidades

Arquivo:
- Cabeçalho
   4 Bytes Ordem - int
   8 Bytes Raiz - long

- Folha
   4 Bytes Tamanho - int
   Dados - byte[]

- Página
   4 Bytes Tamanho - int
   Dados - byte[]

Objetos:
- Árvore B+
   Arquivo -  Base de Dados
   Ordem - Máximo de Filhos
   Max = Máximo de Chaves
   Tamanho da Folha - Tamanho da Folha em Bytes
   Tamanho da Página - Tamanho da Página em Bytes

- Folha
   Número de Chaves
   [Chave|Dado]
   Ponteiro Irmã

- Página
   Número de Chaves
   [Ponteiro|Chave|Dado|Ponteiro]

Funcionamento:
   Elementos armazenados em ordem crescente onde a chave de determinada Página
   é o último elemento do filho da esquerada do índice.
   ex:
            1 - 5 - 7
               /
            2 - 5
*/

class ArvoreBMais_Folha_Pagina{

//--------- Folha -------------
   /*
   Classe Folha - Cria uma nova Folha Vazia
   */
   class Folha{
      
      //Atributos
      protected int n_chaves;
      protected int[] chaves;
      protected int[] dados;
      protected long irma;

      //Construtor
      public Folha(){
         this.n_chaves = 0;
         this.chaves = new int[MAX];
         this.dados = new int[MAX];
         this.irma = -1;

         for (int i=0; i<MAX; i++){
            this.chaves[i] = -1;
            this.dados[i] = -1;
         }

      }
      
      //metodos
      /*
      print - mostra a Folha   
      */
      protected void print(){
         System.out.print("Folha(n_chaves: "+this.n_chaves+" |");
         for (int i=0; i<MAX; i++){
            System.out.print(" ch: "+this.chaves[i]+" dado: "+this.dados[i]+" |");
         }
         System.out.println(" Irmã: "+this.irma+" )");
      }   
    
      /*
      toByteArray - transforma os dados da Folha em um arranho de bytes
      @return byte[]
      */
      protected byte[] toByteArray() throws Exception{
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         DataOutputStream da = new DataOutputStream(ba);

         da.writeInt(n_chaves);
         for(int i=0; i<MAX; i++){
            da.writeInt(this.chaves[i]);
            da.writeInt(this.dados[i]);
         }
         da.writeLong(this.irma);
         
         return ba.toByteArray();
      }

      /*
      fromByteArray - preenche uma Folha com os dados de uma arranho de byte
      @param byte[]
      */
      protected void fromByteArray(byte[] data) throws Exception{
         ByteArrayInputStream ba = new ByteArrayInputStream(data);
         DataInputStream da = new DataInputStream(ba);

         this.n_chaves = da.readInt();
         for(int i=0; i<MAX; i++){
            this.chaves[i] = da.readInt();
            this.dados[i] = da.readInt();
         }
         this.irma = da.readLong();
      }

   }
//--------- Pagina ------------
   /*
   Classe Pagina - Cria uma nova Página Vazia
   */
   class Pagina{
      
      //Atributos
      protected int n_chaves;
      protected long[] ponteiros;
      protected int[] chaves;
      protected int[] dados;

      //Construtor
      public Pagina(){
         this.n_chaves = 0;
         this.ponteiros = new long[ORDEM];
         this.chaves = new int[MAX];
         this.dados = new int[MAX];

         for (int i=0; i<MAX; i++){
            this.ponteiros[i] = -1;
            this.chaves[i] = -1;
            this.dados[i] = -1;
         }
         this.ponteiros[MAX] = -1;
      }
      
      //metodos  
      /*
      print - mostra a Pagina   
      */
      protected void print(){
         System.out.print("Página(n_chaves: "+this.n_chaves+" |");
         for (int i=0; i<MAX; i++){
            System.out.print(" p: "+this.ponteiros[i]+" ch: "+this.chaves[i]+" da: "
                              +this.dados+" |");
         }
         System.out.println(" p: "+this.ponteiros[MAX]+" )");
      } 

      /*
      toByteArray - transforma os dados da Pagina em um arranho de bytes
      @return byte[]
      */
      protected byte[] toByteArray() throws Exception{
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         DataOutputStream da = new DataOutputStream(ba);

         da.writeInt(n_chaves);
         for(int i=0; i<MAX; i++){
            da.writeLong(this.ponteiros[i]);
            da.writeInt(this.chaves[i]);
            da.writeInt(this.dados[i]);
         }
         da.writeLong(this.ponteiros[MAX]);
         
         return ba.toByteArray();
      }

      /*
      fromByteArray - preenche uma Folha com os dados de uma arranho de byte
      @param byte[]
      */
      protected void fromByteArray(byte[] data) throws Exception{
         ByteArrayInputStream ba = new ByteArrayInputStream(data);
         DataInputStream da = new DataInputStream(ba);

         this.n_chaves = da.readInt();
         for(int i=0; i<MAX; i++){
            this.ponteiros[i] = da.readLong();
            this.chaves[i] = da.readInt();
            this.dados[i] = da.readInt();
         }
         this.ponteiros[MAX] = da.readLong();
      }
   }
//--------- Árvore ------------
   //Constantes Estáticas
   private static long RAIZ = 4;
   private static String DIRETORIO = "dbs";

   //Atributos
   private RandomAccessFile arq;
   final int ORDEM;
   final int MAX;
   final int TAM_FOLHA;
   final int TAM_PAGINA;

   //Construtor
   public ArvoreBMais_Folha_Pagina(String nome, int ordem)throws Exception{
      if(ordem < 4)
         throw new Exception("Ordem Mínima 4");

      File diretorio = new File(DIRETORIO);
      if(! diretorio.exists())
         diretorio.mkdir();

      arq = new RandomAccessFile(DIRETORIO+"/"+nome, "rws");
      this.ORDEM = ordem;
      this.MAX = ORDEM -1;
      this.TAM_FOLHA = 4 + MAX*4 + MAX*4 + 8;
      this.TAM_PAGINA = 4 + ORDEM*8 + MAX*4 + MAX*4;     

      if (arq.length() < 12){
         arq.seek(0);
         arq.writeInt(ORDEM);
         arq.writeLong(-1);
      } else {
         arq.seek(0);
         int fordem = arq.readInt();
         if(fordem != ORDEM)
            throw new Exception("Ordem Arquivo Incompatível");
      }
   } 

   //Metodos

//--------- Read ---------

   /*
   read - Busca o conjunto da dados de uma respectiva chava
   @param int chave
   @return int[] dados
   */
   public int[] read (int chave)throws Exception{
      int[] resp = new int[0];
      arq.seek(RAIZ);
      long raiz = arq.readLong();
      if(raiz != -1)
         resp = read(chave, raiz);
      return resp;
   }
   /*
   read - overload
   @param int chave long endereco
   */
   private int[] read (int chave, long endereco)throws Exception{
      int[] resp = new int[0];
      arq.seek(endereco);
      int tamanho = arq.readInt();

      //Se for Pagina
      if(tamanho == TAM_PAGINA){
         
         //Preencher Página
         Pagina pg = new Pagina();
         byte[] data = new byte[TAM_PAGINA];
         arq.read(data);
         pg.fromByteArray(data);     
    
         //Verificar por onde descer
         long descida = pg.ponteiros[0];
         for(int i=0; i<pg.n_chaves && chave>pg.chaves[i]; i++){
            descida = pg.ponteiros[i+1];
         }
   
         resp = read(chave, descida);
      }

      //Se for Folha
      else if(tamanho == TAM_FOLHA){
         
         //Preencher Folha
         Folha fa = new Folha();
         byte[] data = new byte[TAM_FOLHA];
         arq.read(data);
         fa.fromByteArray(data);

         //Localizar chave
         int indice = 0;
         List<Integer> lista = new ArrayList<Integer>();         

         while(indice<fa.n_chaves && chave!=fa.chaves[indice]){
            indice++;
         }
         
         if(indice<fa.n_chaves && chave==fa.chaves[indice]){
            fillArrayList(chave, lista, indice, fa);
         }
         
         resp = new int[lista.size()];
         for (int i=0; i<resp.length; i++){
            resp[i] = lista.get(i).intValue();
         }
      }
      else{
         throw new Exception("READ - Tamanho Incompatível");
      }

      return resp;
   }
   
   /*
   fillArrayList - preenche o ArryList com os dados da chave procurada
   @param int chave, List lista, int indice, Folha fa
   */
   private void fillArrayList(int chave, List<Integer> lista, int indice, Folha fa)throws Exception{
      while(indice<fa.n_chaves && chave==fa.chaves[indice]){
         lista.add(fa.dados[indice]);
         indice++;
      }
      if(indice==fa.n_chaves && fa.irma!=-1){
         arq.seek(fa.irma);
         int tamanho = arq.readInt();
         if(tamanho == TAM_FOLHA){
            fa = new Folha();
            byte[] data = new byte[TAM_FOLHA];
            arq.read(data);
            fa.fromByteArray(data);
            fillArrayList(chave, lista, 0, fa);
         }
         else{
            throw new Exception("fillArrayList - Tamanho Irmã Incompatível");
         } 
      }
   }

//------- Create ---------

   /*
   Create - Cria no arquivo um novo registro de chave | dado
   @param
   @return
   */

//------ Main para Teste -----
   public static void main(String[] args)throws Exception{
      ArvoreBMais_Folha_Pagina arvore = new ArvoreBMais_Folha_Pagina("teste.db", 5);
      arvore.create(2, 21);
      arvore.create(0, 01);
      arvore.create(1, 11);
      arvore.create(1, 12);
      arvore.create(1, 13);
      int[] resp = arvore.read(1);
      if(resp.length > 0){
         for(int i=0; i<resp.length; i++){
            System.out.println(resp[i]);
         }
      }
      else{
         System.out.println("Chave não encontrada");
      }
   }
   
}//fim da Classe ArvoreBMais_Folha_Pagina

