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
   Chaves|Dados
   Ponteiro Irmã

- Página
   Número de Chaves
   Ponteiro|Chave|Ponteiro
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
      inserir - insere um conjunto de chave | dado
      @param int chave | int dado
      @return Folha - se for necessário criar outra folha
      */
      private Folha inserir(int chave, int dado)throws Exception{
         Folha fa_irma = null;
         
         //Nao precisa duplicar
         if(this.n_chaves < MAX){
            int i;
            for(i=this.n_chaves-1; i>=0; i--){
               if(this.chaves[i]>chave || (this.chaves[i]==chave && this.dados[i]>dado)){
                  this.chaves[i+1] = this.chaves[i];
                  this.dados[i+1] = this.dados[i];
               } 
               else{      
                  break;
               }
            }
            this.chaves[i+1] = chave;
            this.dados[i+1] = dado;
            this.n_chaves++;

         }
         
         //Precisa duplicar
         else if(this.n_chaves == MAX){
            fa_irma = new Folha();
            int i = this.n_chaves -1;
            int j = this.n_chaves - this.n_chaves/2 -1;
            int limite = this.n_chaves/2;
            for(; i>=limite; i--, j--){
               fa_irma.chaves[j] = this.chaves[i];
               this.chaves[i] = -1;
               fa_irma.dados[j] = this.dados[i];
               this.dados[i] = -1;
               fa_irma.n_chaves++;
               this.n_chaves--;
            }
            fa_irma.irma = this.irma;
            this.irma = -1;
            
            //Após duplicar, inserir nova chave
            int ultimo = this.n_chaves-1;
            if(this.chaves[ultimo]>chave || 
               (this.chaves[ultimo]==chave && this.dados[ultimo]>dado)){
               this.inserir(chave, dado);
            } 
            else{
               fa_irma.inserir(chave, dado);
            }
            
         }
         else
            throw new Exception("CREATE - Número de Chaves Incompatível");
         return fa_irma;
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

      //Construtor
      public Pagina(){
         this.n_chaves = 0;
         this.ponteiros = new long[ORDEM];
         this.chaves = new int[MAX];

         for (int i=0; i<MAX; i++){
            this.ponteiros[i] = -1;
            this.chaves[i] = -1;
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
            System.out.print(" p: "+this.ponteiros[i]+" ch: "+this.chaves[i]+" |");
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
         }
         this.ponteiros[MAX] = da.readLong();
      }
   }
//--------- Árvore ------------
   //Constantes Estáticas
   private static long RAIZ = 4;

   //Atributos
   private RandomAccessFile arq;
   final int ORDEM;
   final int MAX;
   final int TAM_FOLHA;
   final int TAM_PAGINA;

   //Construtor
   public ArvoreBMais_Folha_Pagina(String nome, int ordem)throws Exception{
      arq = new RandomAccessFile(nome, "rws");
      this.ORDEM = ordem;
      this.MAX = ORDEM -1;
      this.TAM_FOLHA = 4 + MAX*4 + MAX*4 + 8;
      this.TAM_PAGINA = 4 + ORDEM*8 + MAX*4;     

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

   public ArvoreBMais_Folha_Pagina(String nome) throws Exception{
      arq = new RandomAccessFile(nome, "rws");
      if (arq.length() < 12)
         throw new Exception("Ordem Necessária para Criar Novo Arquivo");
      else{
         arq.seek(0);
         int ordem = arq.readInt();
         this.ORDEM = ordem;
         this.MAX = ORDEM -1;;
         this.TAM_FOLHA = 4 + MAX*4 + MAX*4 + 8;
         this.TAM_PAGINA = 4 + ORDEM*8 + MAX*4;     
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
   @param int chave, int dado
   @return boolean sucesso
   */
   public boolean create(int chave, int dado)throws Exception{
      boolean sucesso = false;
      if(!(chave<0) && !(dado<0)){
         arq.seek(RAIZ);
         long raiz = arq.readLong(); //endereço do nó atual
         
         //raiz vazia
         if(raiz == -1){
            Folha fa = new Folha();
            fa.n_chaves = 1;
            fa.chaves[0] = chave;
            fa.dados[0] = dado;
         
            raiz = arq.length();
            arq.seek(raiz);
            arq.writeInt(TAM_FOLHA);
            arq.write(fa.toByteArray());
         
            arq.seek(RAIZ);
            arq.writeLong(raiz);
            sucesso = true;
         }
         
         else{
            arq.seek(raiz);
            int tamanho = arq.readInt();
            
            //Se for Página
            if(tamanho == TAM_PAGINA){
                  
               //Preencher Página
               Pagina pg = new Pagina();
               byte[] data = new byte[TAM_PAGINA];
               arq.read(data);
               pg.fromByteArray(data);

               //Verificar por onde descer
               long filho = pg.ponteiros[0];
               for(int i=0; i<pg.n_chaves && chave>pg.chaves[i]; i++){
                  filho = pg.ponteiros[i+1];
               }

               //Ir até folha recusivamente
         
            }

            //Se for Folha
            else if(tamanho == TAM_FOLHA){
               
               //Preencher Folha
               Folha fa = new Folha();
               byte[] data = new byte[TAM_FOLHA];
               arq.read(data);
               fa.fromByteArray(data);

               //Inserir dados na Folha
               Folha fa_irma = fa.inserir(chave, dado);

               //Se foi necessário criar outra Folha
               if(fa_irma != null){

                  //Escrever irma no arquivo
                  data = fa_irma.toByteArray();
                  long end_irma = arq.length();
                  arq.seek(end_irma);
                  arq.writeInt(TAM_FOLHA);
                  arq.write(data);

                  //Atualizar folha original
                  fa.irma = end_irma;
                  data = fa.toByteArray();
                  arq.seek(raiz);
                  arq.writeInt(TAM_FOLHA);
                  arq.write(data);

                  //Criar Página para se tornar raiz   
                  Pagina pg = new Pagina();
                  pg.n_chaves = 1;
                  pg.ponteiros[0] = raiz;
                  pg.ponteiros[1] = end_irma;
                  pg.chaves[0] = fa.chaves[fa.n_chaves-1];

                  //Escrever Página no arquivo
                  raiz = arq.length();
                  arq.seek(raiz);
                  data = pg.toByteArray();
                  arq.writeInt(TAM_PAGINA);
                  arq.write(data);

                  //Escrever raiz em RAIZ
                  arq.seek(RAIZ);
                  arq.writeLong(raiz);
fa.print();
fa_irma.print();
pg.print();
               }
               
               //Não foi necessário criar outra folha
               else{
                  data = fa.toByteArray();
                  arq.seek(raiz);
                  arq.writeInt(TAM_FOLHA);
                  arq.write(data);
               }
            } 
            else{
               throw new Exception("CREATE - Tamnho Incompatível");     
            }
               
      
            sucesso = true;
         }
      }
      return sucesso;
   }

   /*
   Create - Overload
   @param int chave, int dado, long pai, long atual
   @return int chave a ser inserida no retorno
   */
   private int create(int chave, int dado, long pai, long atual)throws Exception{
      int nova_chave = -1;
      arq.seek(atual);
      int tamanho = arq.readInt();
      
      //Se for Página
      if(tamanho == TAM_PAGINA){
         
         //Preencher Pagina
         Pagina pg = new Pagina();
         byte[] data = new byte[TAM_PAGINA];
         arq.read(data);
         pg.fromByteArray(data);

         //Verificar por onde descer
         long filho = pg.ponteiros[0];
         for(int i=0; i<pg.n_chaves && chave>pg.chaves[i]; i++){
            filho = pg.ponteiros[i+1];
         }

         nova_chave = create(chave, dado, atual, filho);
      }
      
      //Se for folha
      else if(tamanho == TAM_FOLHA){

      }
      else{
         throw new Exception("CREATE - Tamnho Incompatível");
      }
      return nova_chave;
   }

//------ Main para Teste -----
   public static void main(String[] args)throws Exception{
      ArvoreBMais_Folha_Pagina arvore = new ArvoreBMais_Folha_Pagina("teste.db", 5);
      arvore.create(2, 2);
      arvore.create(0, 2);
      arvore.create(1, 10);
      arvore.create(1, 12);
      arvore.create(1, 11);
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

