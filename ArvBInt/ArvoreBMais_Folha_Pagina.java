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
                              +this.dados[i]+" |");
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
   @param int chave, int dado
   @return boolean sucesso
   */
   public boolean create(int chave, int dado)throws Exception{
      boolean resp = false;
      
      //Testar se inputs são válidos
      if(chave<0 || dado<0)
         return false;

      //Testar se conjunto chave-dado já existe
      int[] conjunto = this.read(chave);
      for (int existente : conjunto){
         if(existente == dado){
            return false;
         }  
      }
      
      //Ler raiz
      arq.seek(RAIZ);
      long raiz = arq.readLong();
      
      //Se não existe raiz
      if(raiz == -1){
         //Cria nova folha
         Folha fa = new Folha();
         fa.n_chaves = 1;
         fa.chaves[0] = chave;
         fa.dados[0] = dado;

         //Escreve nova folha no arquivo
         raiz = arq.length();
         arq.seek(raiz);
         arq.writeInt(TAM_FOLHA);
         arq.write(fa.toByteArray());
   
         //Escreve endereço da folha em RAIZ
         arq.seek(RAIZ);
         arq.writeLong(raiz);

         resp = true;
      }

      //Se já existe raiz
      else{
         long nova = create(raiz, chave, dado);

         //se houve duplicação
         if(nova != -1){
            arq.seek(raiz);
            int tamanho = arq.readInt();
            
            //Se a raiz era Folha
            if(tamanho == TAM_FOLHA){

               //Carrega raiz antiga
               Folha fa = new Folha();
               byte[] data = new byte[TAM_FOLHA];
               arq.read(data);
               fa.fromByteArray(data);

               //Cria a primeira Página para ser raiz
               Pagina pg = new Pagina();
               pg.n_chaves = 1;
               pg.ponteiros[0] = raiz;
               pg.chaves[0] = fa.chaves[fa.n_chaves-1];
               pg.dados[0] = fa.dados[fa.n_chaves-1];
               pg.ponteiros[1] = nova;

               //Escrever nova Página em RAIZ
               raiz = arq.length();
               arq.seek(raiz);
               arq.writeInt(TAM_PAGINA);
               arq.write(pg.toByteArray());
               arq.seek(RAIZ);
               arq.writeLong(raiz);

               resp = true;
            }

            //Se nova é Página
            else if(tamanho == TAM_PAGINA){
//nova pagina criar nova raiz               

            }
            else
               throw new Exception("CREATE - Tamanho Incompatível - 1");
         }
      }
      return resp;
   }
   /*
   Create - Overload
   @param long endereco, int chave, int dado
   @return long nova - Se necessário duplicar
   */
   private long create(long endereco, int chave, int dado)throws Exception{
      long nova = -1;

      //Ler endereço
      arq.seek(endereco);
      int tamanho = arq.readInt();
      
      //Se for Folha
      if(tamanho == TAM_FOLHA){
         //Carregar folha
         Folha fa = new Folha();
         byte[] data = new byte[TAM_FOLHA];
         arq.read(data);
         fa.fromByteArray(data);

         //Verificar folha tem espaço
         if(fa.n_chaves < MAX){
            boolean inserido = false;
            int i = fa.n_chaves -1;
            while(!inserido){
               if(i>-1 && (fa.chaves[i]>chave || (fa.chaves[i]==chave && fa.dados[i]>dado))){
                  fa.chaves[i+1] = fa.chaves[i];
                  fa.dados[i+1] = fa.dados[i];
               }
               else{
                  fa.chaves[i+1] = chave;
                  fa.dados[i+1] = dado;
                  inserido = true;
               }
               i--;
            }      
            fa.n_chaves++;

            //Escrever Folha atualizada
            arq.seek(endereco);
            arq.writeInt(TAM_FOLHA);
            arq.write(fa.toByteArray());
         }

         //Se folha não tiver espaço - duplicar
         else if (fa.n_chaves == MAX){
            Folha fa_dup = new Folha();
            boolean inserido = false;
            int i = MAX/2 -1;
            int j = MAX -1;

            while(i>=0){
               if(inserido || fa.chaves[j]>chave || (fa.chaves[j]==chave && fa.dados[j]>dado)){
                  fa_dup.chaves[i] = fa.chaves[j];
                  fa.chaves[j] = -1;
                  fa_dup.dados[i] = fa.dados[j];
                  fa.dados[j] = -1;
                  i--;
                  j--;
                  fa_dup.n_chaves++;
                  fa.n_chaves--;
               }
               else{
                  fa_dup.chaves[i] = chave;
                  fa_dup.dados[i] = dado;
                  i--;
                  fa_dup.n_chaves++;
                  inserido = true;
               }
            }

            //Se nao tiver sido inserido inserir na folha antiga
            i = fa.n_chaves -1;
            while(!inserido){
               if(i>-1 && (fa.chaves[i]>chave || (fa.chaves[i]==chave && fa.dados[i]>dado))){
                  fa.chaves[i+1] = fa.chaves[i];
                  fa.dados[i+1] = fa.dados[i];
               }
               else{
                  fa.chaves[i+1] = chave;
                  fa.dados[i+1] = dado;
                  inserido = true;
                  fa.n_chaves++;
               }
               i--;
            }
 
            //Obter endereço Folha duplicada
            nova = arq.length();

            //Atualizar ponteiros para irmãs
            fa_dup.irma = fa.irma;
            fa.irma = nova;

            //Escrever Folha nova
            arq.seek(nova);
            arq.writeInt(TAM_FOLHA);
            arq.write(fa_dup.toByteArray());
 
            //Escrever Folha atualizada
            arq.seek(endereco);
            arq.writeInt(TAM_FOLHA);
            arq.write(fa.toByteArray());
         }
         else
            throw new Exception("CREATE - Número de Chaves Incompatível - 1");
      
      }
      
      //Se for Página
      else if(tamanho == TAM_PAGINA){
         //Carregar Página
         Pagina pg = new Pagina();
         byte[] data = new byte[TAM_PAGINA];
         arq.read(data);
         pg.fromByteArray(data);

         //Ir até a Folha
         int i = 0;

         while(i<pg.n_chaves&&(pg.chaves[i]<chave||(pg.chaves[i]==chave && pg.dados[i]<dado))){
            i++;  
         }

         //DESCIDA RECURSIVA
         long descida = pg.ponteiros[i];
         nova = create(descida, chave, dado);

         //Se houve duplicação
         if(nova != -1){

            arq.seek(nova);
            tamanho = arq.readInt();

            //Se couber na página
            if(pg.n_chaves<MAX){
               //Se for Folha
               if(tamanho == TAM_FOLHA){
         
                  //Carregar Folha nova
                  Folha fa_nova = new Folha();
                  data = new byte[TAM_FOLHA];
                  arq.read(data);
                  fa_nova.fromByteArray(data);

                  //Carrgar folha originaria
                  arq.seek(descida);
                  arq.readInt();
                  Folha fa_orig = new Folha();
                  arq.read(data);
                  fa_orig.fromByteArray(data);

                  if(i == pg.n_chaves){
                     pg.chaves[i] = fa_orig.chaves[fa_orig.n_chaves-1];
                     pg.dados[i] = fa_orig.dados[fa_orig.n_chaves-1];
                     pg.ponteiros[i+1] = nova;
                     pg.n_chaves++;
                  }
                  else if(i < pg.n_chaves){
                     pg.ponteiros[pg.n_chaves+1] = pg.ponteiros[pg.n_chaves];
                     int j = pg.n_chaves-1;
                     for( ; j>i; j--){
                        pg.ponteiros[j+1] = pg.ponteiros[j];
                        pg.chaves[j+1] = pg.chaves[j];
                        pg.dados[j+1] = pg.dados[j];
                     }
                     
                     pg.ponteiros[j+1] = nova;
                     pg.chaves[j+1] = fa_nova.chaves[fa_nova.n_chaves-1];
                     pg.dados[j+1] = fa_nova.dados[fa_nova.n_chaves-1];
                     pg.n_chaves++;

                 }
                  else
                     throw new Exception("CREATE - Número de Chaves Incompatível - 2"); 
 
                  //atualizar Página
                  arq.seek(endereco);
                  arq.writeInt(TAM_PAGINA);
                  arq.write(pg.toByteArray());
               
               }
               else if(tamanho == TAM_PAGINA){
//nova pagina cabe
               }  
               else
                  throw new Exception("CREATE - Tamanho Incompatível - 2");
               
               //Coube não há o que retornar
               nova = -1;
            }
            
            //Se não couber na página
            else if(pg.n_chaves == MAX){
//nao cabe
            }
            else
               throw new Exception("CREATE - Número de Chaves Incompatível - 3");
         }
      }
      else
         throw new Exception("CREATE - Tamanho Incompatível - 3");

      return nova;
   }

//------ Main para Teste -----
   public static void main(String[] args)throws Exception{
      ArvoreBMais_Folha_Pagina arvore = new ArvoreBMais_Folha_Pagina("teste.db", 5);
      arvore.create(2, 21);
      arvore.create(0, 01);
      arvore.create(1, 11);
      arvore.create(1, 12);
      arvore.create(1, 13);

      for(int i = 14; i<20; i++){
         arvore.create(1, i);
      }  



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

