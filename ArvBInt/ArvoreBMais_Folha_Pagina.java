/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva
*/

import java.io.*;

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
      arq = new RandomAcessFile(nome, "rws");
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
         int ordem = readInt();
         if(ordem != ORDEM)
            throw new Exception("Ordem Arquivo Incompatível");
      }
   }

   public ArvoreBMais_Folha_Pagina(String nome) throws Exception{
      arq = new RandomAcessFile(nome, "rws");
      if (arq.length() < 12)
         throw new Exception("Ordem Necessária para Criar Novo Arquivo");
      else{
         arq.seek(0);
         int ordem = readInt();
         this.ORDEM = ordem;
         this.MAX = ORDEM -1;;
         this.TAM_FOLHA = 4 + MAX*4 + MAX*4 + 8;
         this.TAM_PAGINA = 4 + ORDEM*8 + MAX*4;     
      }
   }

   //Metodos
   /*
   read - Busca o conjunto da dados de uma respectiva chava
   @param int chave
   @return int[] dados
   */
   public byte[] read (int chave)throws Exception{
      byte[] resp = null;
      arq.seek(RAIZ);
      long raiz = arq.readInt();
      if(raiz != -1)
         resp = read(chave, raiz);
      return resp;
   }
   /*
   read - overload
   @param int chave long endereco
   */
   private byte[] read (int chave, long endereco)throws Exception{
      byte[] resp = null;
      arq.seek(endereco);
      int tamanho = arq.readInt();
      
      //Se for Pagina
      if(tamanho == TAM_PAGINA){
         
         //Preencher Página
         Pagina pg = new Pagina();
         byte[] dados = new byte[TAM_PAGINA];
         pg.read(dados);
         pg.fromByteArray(dados);     
    
         //Verificar por onde descer
         long descida = pg.ponteiros[0];
         for(int i=0; i<MAX && pg.chaves[i] != -1 && chave >= pg.chaves[i]; i++){
            descida = pg.ponteiros[i+1];
            if (chave == pg.chaves[i])
               i = MAX;
         }
   
         resp = read(chave, descida);
      }

      //Se for Folha
      else if(tamanho == TAM_FOLHA){
         
         
      }
      else{
         throw new Exception("READ - Tamanho Incompatível");
      }

      return resp;
   }

}//fim da Classe ArvoreBMais_Folha_Pagina

