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

import java.util.Date;
import java.text.SimpleDateFormat;
import aed3.ArvoreBMais_Int_Int;
import aed3.ListaInvertida;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.stream.Collectors;


/*
Classe Pergunta
*/

class Pergunta implements Registro, Comparable<Pergunta>{
   //atributos estaticos
   public static Date data;
   public static SimpleDateFormat formatter;
   public static ArvoreBMais_Int_Int indice;
   public static CRUD<Pergunta> arquivo;
   public static ListaInvertida listaChaves;
   public static int[] listaPerguntas;


   //metodos estaticos

   /*Método para carregar perguntas de novo usuário logado
    * @param - int idUsuario
    */
   public static void carregarPerguntas(int idUsuario)throws Exception{
      listaPerguntas = indice.read(idUsuario);
   }

   /* Obter print de listagem de perguntas feitas por um usuário, boolean para definir se 
    * perguntas arquivadas serão consideradas na hora do print
    * @param int idUsuario, boolean printArq
    */
   public static void printPerguntas(int idUsuario, boolean printArq) throws Exception{
      carregarPerguntas(idUsuario);
      for(int i = 0; i < listaPerguntas.length; i++){
         Pergunta p = arquivo.read(listaPerguntas[i]);
         if(p.ativa){
            System.out.println("\n" + (i+1) + ".\n" + formatter.format(new Date(p.criacao)) + "\n" + p.pergunta);
            System.out.print("Palavras chave: ");
            
            for(int j = 0; j < p.palavrasChave.length; j++)
               System.out.print("[" + p.palavrasChave[j] + "] ");
            System.out.println();
         }
         else if(printArq){
            System.out.println("\n" + (i+1) + ". (Arquivada)\n" + formatter.format(new Date(p.criacao)) 
            + "\n" + p.pergunta);
            System.out.print("Palavras chave: ");
            
            for(int j = 0; j < p.palavrasChave.length; j++)
               System.out.print("[" + p.palavrasChave[j] + "] ");
            System.out.println();
         }
      }
   }

   //criar pergunta no indice quando criar no arquivo principal
   public static void createAtIndex(int userId, Pergunta p) throws IOException{
      indice.create(userId, p.idPergunta);
   }

   /* Método auxilar tratar chaves para controle de aspas
    * @param String x
    * @return int paresDeAspas
    */
   public static int contarAspas(String x){
      int resp = 0;
      int tam = x.length();

      for(int i = 0; i < tam; i++)
         if(x.charAt(i) == '"')
            resp++;

      return resp/2;
   }

   /* Método para tratar palavras chaves de pesquisa separa palavras por ' ' e 
    * expressoes por '"'
    * @param String l
    * @return String[] arrayChaves
    */
   private static String[] tratarChaves(String l){
      int cont = contarAspas(l); 
      ArrayList<String> resp = new ArrayList<String>();

      int inicio = 0;    
      int fim = 0; 

      while(fim<l.length()){
         if(l.charAt(fim)==' '){
            resp.add(l.substring(inicio, fim));
            inicio = fim + 1;
         } else if(l.charAt(fim)=='\"' && cont > 0){
            resp.add(l.substring(inicio, fim));
            fim++;
            inicio = fim;
            while(l.charAt(fim)!='\"'){
               fim++;
            }
            resp.add(l.substring(inicio, fim));
            inicio = fim + 1;
            cont--;
         }
         fim++;
      }
      resp.add(l.substring(inicio, fim));
      resp.removeAll(Arrays.asList(""));
      
      String[] resposta = new String[resp.size()];
      for(int j=0; j<resp.size(); j++)
         resposta[j] = resp.get(j);
      return resposta;
   }

   /*
    * novaPergunta - Método para usuário adicionar uma nova pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void novaPergunta(Scanner leitor, int idUsuario) throws Exception{
      System.out.println("\nInsira a pergunta:");
      String buffer = leitor.nextLine();

      System.out.println("\nInsira as palavras chaves delimitadas por espaço e");
      System.out.println("\nexpressões por \"\". Ex: java \"Visual Studio Code\" Programação");
      String chaves = leitor.nextLine();

      if(!buffer.equals("") && !chaves.equals("")){
         String[] chavesArray = tratarChaves(chaves);

         System.out.println("\nCONFIRME A CRIAÇÃO DA PERGUNTA: ");
         System.out.println("\""+buffer+"\"\n");
         System.out.println("\nQUE CONTÉM AS PALAVRAS CHAVE: \"");
         System.out.println(chaves);

         System.out.print("(SIM(S) NÃO(N)): ");
         String confirmacao = leitor.nextLine();
         confirmacao = confirmacao.toUpperCase();
 
         if(confirmacao.contains("S")){
            Pergunta nova = new Pergunta(idUsuario, buffer, chavesArray);
            arquivo.create(nova);            
            indice.create(idUsuario, nova.getID());

            String str;
            //Incluindo palavras chave
            for(int i = 0; i < chavesArray.length; i++){
               str = chavesArray[i].replaceAll("[^\\p{ASCII}]", "").toLowerCase();
               listaChaves.create(str.replace(" ",""), nova.idPergunta);
            }

            System.out.println("Pergunta incluída com sucesso!");
            //listaChaves.print();   
         }else{
            System.out.println("Inclusão de pergunta cancelada.");
         }

      }else{
         System.out.println("Inclusão de pergunta cancelada. Alguns campos não foram preenchidos.");
      }
      Menu.pause(leitor);
   }

   /*
    * alterarPergunta - Método para usuário alterar uma pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void alterarPergunta(Scanner leitor, int idUsuario) throws Exception{
      printPerguntas(idUsuario, false);

      System.out.println("\nQual pergunta dejesa alterar: ");
      int escolha = Menu.lerEscolha();

      if(0 < escolha && escolha <= listaPerguntas.length){

         Pergunta p = arquivo.read(listaPerguntas[escolha-1]);

         if(p.ativa){
            System.out.println("Pergunta escolhida: "+p.pergunta);
            System.out.println("\nInsira a alteração da pergunta:");
            String buffer = leitor.nextLine();

            System.out.println("Palavras chave atuais: ");
            for(int j = 0; j < p.palavrasChave.length; j++)
               System.out.print("[" + p.palavrasChave[j] + "] ");

            System.out.println("\n\nInsira palavras chaves a serem removidas:");
            String removidas = leitor.nextLine();

            System.out.println("\nInsira palavras chaves a serem incluídas");
            String incluidas = leitor.nextLine();
            
            if(!buffer.equals("")){
               System.out.println("\nCONFIRME A ALTERAÇÃO DA PERGUNTA: ");
               System.out.println("\""+buffer+"\"");
               System.out.println("Palavras chave removidas:\n");
               System.out.println(removidas);
               System.out.println("Palavras chave incluídas:\n");
               System.out.println(incluidas);

               System.out.print("(SIM(S) NÃO(N)): ");
               String confirmacao = leitor.nextLine();
               confirmacao = confirmacao.toUpperCase();
      
               if(confirmacao.contains("S")){
                  String[] removidasArray = tratarChaves(removidas);
                  String[] incluidasArray = tratarChaves(incluidas);

                  //Remover e incluir chaves novas
                  HashSet<String> incluir = new HashSet<String>(Arrays.asList(incluidasArray)); 
                  HashSet<String> remover = new HashSet<String>(Arrays.asList(removidasArray)); 
                  HashSet<String> novasChaves = new HashSet<String>(Arrays.asList(p.palavrasChave));
                  
                  incluir.removeAll(novasChaves);
                  remover.retainAll(novasChaves);
                  novasChaves.removeAll(remover);

                  removidasArray = new String[remover.size()];
                  remover.toArray(removidasArray);
                  incluidasArray = new String[incluir.size()];
                  incluir.toArray(incluidasArray);

                  String str;
                  for(int i = 0; i < removidasArray.length; i++){ 
                     str = removidasArray[i].replaceAll("[^\\p{ASCII}]", "").toLowerCase();
                     listaChaves.delete(str.replace(" ",""), p.idPergunta);
                  }

                  for(int i = 0; i < incluidasArray.length; i++){
                     novasChaves.add(incluidasArray[i]);
                     str = incluidasArray[i].replaceAll("[^\\p{ASCII}]", "").toLowerCase();
                     listaChaves.create(str.replace(" ",""), p.idPergunta);
                  }

                  if(novasChaves.size() > 0){
                     //Copiando novas chaves
                     p.palavrasChave = new String[novasChaves.size()];
                     novasChaves.toArray(p.palavrasChave);
                     
                     p.pergunta = buffer;
                     arquivo.update(p);  
                     System.out.println("Pergunta alterada com sucesso!");    
                  } else{
                     System.out.println("Alteração de pergunta cancelada. Sua pegunta não pode ficar" + 
                     "sem chave(s).");
                  }     
               }else{
                  System.out.println("Alteração de pergunta cancelada. Sua pergunta não pode ser vazia.");
               }

            }else{
               System.out.println("Alteração de pergunta cancelada.");
            }
         }else{
            System.out.println("\nPergunta escolhida arquivada, tente outra.");
         }
      }else{
         System.out.println("\nPergunta escolhida não existe, tente outra.");
      }

      Menu.pause(leitor);
   }

   /* arquivarPergunta - Método para usuário arquivar uma pergunta 
    * @param Scanner leitor, int idUsuario
    */
   public static void arquivarPergunta(Scanner leitor, int idUsuario) throws Exception{
      printPerguntas(idUsuario, false);

      System.out.println("\nQual pergunta dejesa arquivar: ");
      int escolha = Menu.lerEscolha();

      if(0 < escolha && escolha <= listaPerguntas.length){

         Pergunta p = arquivo.read(listaPerguntas[escolha-1]);

         if(p.ativa){
            System.out.println("Pergunta escolhida: "+p.pergunta);
             
            System.out.println("\nCONFIRME O ARQUIVAMENTO DA PERGUNTA: ");
            System.out.print("(SIM(S) NÃO(N)): ");
            String confirmacao = leitor.nextLine();
            confirmacao = confirmacao.toUpperCase();
      
            if(confirmacao.contains("S")){
               String str;
               for(int i = 0; i < p.palavrasChave.length; i++){
                  str = p.palavrasChave[i].replaceAll("[^\\p{ASCII}]", "").toLowerCase();
                  listaChaves.delete(str.replace(" ",""), p.idPergunta);
               }

               p.ativa = false;
               arquivo.update(p);  
               System.out.println("Pergunta alterada com sucesso!");          
            }else{
               System.out.println("Alteração de pergunta cancelada.");
            }
         }else{
            System.out.println("\nPergunta escolhida arquivada, tente outra.");
         }
      }else{
         System.out.println("\nPergunta escolhida não existe, tente outra.");
      }
      Menu.pause(leitor);
   }
   /**
    * consultarPerguntas - permite consulta ao banco de perguntas por meio de palavras chave
    * @param leitor
    * @return int (id da pergunta)> 0 , se alguma pergunta foi selecionada; -1, caso contrario.
    */
   public static Pergunta consultarPerguntas(Scanner leitor) throws Exception{
      Pergunta pergunta = null;
      Pergunta[] perguntas = pesquisarPalavras(leitor);
      if(perguntas != null && perguntas.length > 0){
         for(int i = 0; i < perguntas.length; i++){
            System.out.print(i + 1 + ". "); printPerguntaResumida(perguntas[i]);
         }

         System.out.print("Digite o número da pergunta que deseja visualizar: ");
         int escolha = Menu.lerEscolha();

         while(escolha <= 0 || escolha > perguntas.length){
            System.out.println("Opção inválida.");
            System.out.print("Digite o número da pergunta que deseja visualizar: ");
            escolha = Menu.lerEscolha();
         }
         pergunta = perguntas[escolha-1];
         Menu.clear();
         System.out.println(Menu.header);
         System.out.println("PERGUNTAS > CONSULTAR PERGUNTAS\n");
         printPerguntaCompleta(perguntas[escolha-1]);
         System.out.println("COMENTÁRIOS\n-----------\n");
         //Resposta.printComentarios(pergunta);
         System.out.println("RESPOSTAS\n---------\n");
         Resposta.printRespostas(pergunta.getID());
         System.out.println(Menu.consultPerg);
         
      }else{
         System.out.println("Não foram encontradas perguntas com as palavras chave especificadas.\n");
      }
      return pergunta;
   }

   /**
    * pesquisarPalavras - Método que permite a pesquisa de um conjunto de perguntas por meio de uma ou mais palavras-chave
    * @param Scanner leitor
    * @return Pergunta[] arranjo com as perguntas associadas ao conjunto de palavras-chave.
   */
   private static Pergunta[] pesquisarPalavras(Scanner leitor) throws Exception{
      System.out.println("\nInsira as palavras chaves delimitadas por espaço e");
      System.out.println("\nexpressões por \"\". Ex: java \"Visual Studio Code\" Programação");
      System.out.print("Palavras chave: ");
      String buffer = leitor.nextLine();
      Pergunta[] perguntas = null;
      if(!buffer.equals("")){
         int[] conj = interseccaoIds(buffer);
         perguntas = new Pergunta[conj.length];
         for(int i = 0; i < perguntas.length; i++){
            perguntas[i] = arquivo.read(conj[i]);
         }
         ordenarPerguntasDec(perguntas);
      }else{
         System.out.println("Busca por perguntas cancelada. Alguns campos não foram preenchidos.");
         Menu.pause(leitor);
      }
      return perguntas;
   }

   /**
    * printPerguntaCompleta - exibe todas as informações de uma determinada pergunta de forma organizada e amigável ao usuário 
    * @param p a pergunta a ser exibida
    */
   public static void printPerguntaCompleta(Pergunta p) throws Exception{
      System.out.println();
      System.out.println("==> " + p.pergunta + "\n");
      System.out.println("Criado em " + (new SimpleDateFormat("dd/MM/yyyy")).format(p.criacao) +   
                                " às " + (new SimpleDateFormat("hh:mm")).format(p.criacao) + 
                               " por " + (Acesso.arquivo.read(p.idUsuario)).nome);
      System.out.print("Palavras chave: ");
      int len = p.palavrasChave.length;
      for(int i = 0; i < len; i++){
         System.out.print("[" + p.palavrasChave[i] + "] ");
      }
      System.out.println("\nNota: " + p.nota + "\n");
   }

   /**
    * printPerguntaResumida - exibe apenas o texto da pergunta e sua nota
    * @param p a pergunta a ser exibida
    */
   public static void printPerguntaResumida(Pergunta p){
      System.out.println(p.pergunta + "\nNota: " + p.nota + "\n");
   }
   /**
    * ordenarPerguntasDec - ordena um arranjo de Perguntas de forma decrescente
    * Observação: faz uso da sobrecarga/override da função "compareTo"
    */
   private static void ordenarPerguntasDec(Pergunta[] perguntas){
      Arrays.sort(perguntas);
   }
   /**
    * interseccaoIds - faz a intersecção dos conjuntos de ids encontrados em cada busca de um conjunto de chaves.
    * @param chaves uma string contendo chaves não tratadas
    * @return int[] o conjunto resposta (intersecção de todos os conjuntos) contendo as chaves desejadas.
    */
   private static int[] interseccaoIds(String chaves) throws Exception{
      String[] _chaves = tratarChaves(chaves);
      String buffer = _chaves[0].replaceAll("[^\\p{ASCII}]", "").toLowerCase();

      int[] resp = listaChaves.read(buffer);

      for(int i = 1; i < _chaves.length; i++){
         buffer = _chaves[i].replaceAll("[^\\p{ASCII}]", "").toLowerCase();
         int[] arr = listaChaves.read(buffer);
         resp = interseccaoIds(resp, arr);
      }

      return resp;
   }
   /**
    * interseccaoIds - faz a intersecção de dois conjuntos de inteiros.
    * @param a1 arranjo contendo IDs de perguntas
    * @param a2 arranjo contendo IDs de perguntas
    * @return resp arranjo contendo os IDs presents tanto em a1 quanto em a2
    */
   private static int[] interseccaoIds(int[] a1, int[] a2){
      ArrayList<Integer> al = new ArrayList<>();
      for(int i = 0; i < a1.length; i++){
         for(int j = 0; j < a2.length; j++){
            if(a1[i] == a2[j]){
               al.add(a1[i]);
               j = a2.length;
            }
         }
      }
      int[] resp = new int[al.size()];
      for (int i = 0; i < al.size(); i++)
        resp[i] = (int) al.get(i);
      return resp;
   }


   //atributos
   public int idUsuario; //chave de busca
   private int idPergunta; //dado
   public long criacao;
   public short nota;
   public String pergunta;
   public boolean ativa;
   public String[] palavrasChave;

   //Override do compareTo para ordenar de forma decrescente pela nota
   @Override
   public int compareTo(Pergunta a){
         return this.nota > a.nota ? -1 : (this.nota == a.nota ? 0 : 1);
   }

   //construtores
   public Pergunta(){
      this(-1, -1, -1, Short.MIN_VALUE, "", false, null);
   }

   public Pergunta(int _idUsuario, String _pergunta, String[] _palavrasChave){
      this(-1, _idUsuario, System.currentTimeMillis(), (short)0, _pergunta, true, _palavrasChave);
   }

   public Pergunta(int _idPergunta, int _idUsuario, long _criacao, short _nota, String _pergunta, boolean _ativa, 
                   String[] _palavrasChave){
      this.idUsuario = _idUsuario;
      this.idPergunta = _idPergunta;
      this.criacao = _criacao;
      this.nota = _nota;
      this.pergunta = _pergunta;
      this.ativa = _ativa;
      this.palavrasChave = _palavrasChave;
   }

   //metodos
   /*
   getID - retorna o ID de um objeto
   @return int id
   */
   public int getID(){
      return(this.idPergunta);
   }
   
   /*
   setID - atribui um ID para um objeto
   @param int n
   */
   public void setID(int n){
      this.idPergunta = n;
   }

   /*
   toByteArray - retorna o conteudo do objeto com byte[]
   @return byte[] ba
   */
   public byte[] toByteArray() throws IOException{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.writeInt(this.idUsuario);
      dos.writeInt(this.idPergunta);
      dos.writeLong(this.criacao);
      dos.writeShort(this.nota);
      dos.writeUTF(this.pergunta);
      dos.writeBoolean(this.ativa);
      dos.writeInt(this.palavrasChave.length);

      for(int i = 0; i < this.palavrasChave.length; i++)
         dos.writeUTF(this.palavrasChave[i]);

      return(baos.toByteArray());
   }
      
   /*
   fromByteArray - preenche o objeto a partir de um byte[]
   @param byte[] ba
   */
   public void fromByteArray(byte[] ba) throws IOException{
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
   
      this.idUsuario = dis.readInt();
      this.idPergunta = dis.readInt();
      this.criacao = dis.readLong();
      this.nota = dis.readShort();
      this.pergunta = dis.readUTF();
      this.ativa = dis.readBoolean();
      int tam = dis.readInt();
      this.palavrasChave = new String[tam];

      for(int i = 0; i < tam; i++)
         this.palavrasChave[i] = dis.readUTF();

   }

   /*
   toString - forma uma String a partir dos dados do objeto
   Este método exibe dados relevantes para o funcionamento interno do sistema e, portanto, não deve ser
   usado para exibição de dados ao usuário
   @return String
   */
   public String toString(){
      String resp = "\nID: "+this.idPergunta;
      resp = resp + "\nID do Usuário: "+this.idUsuario;
      resp = resp + "\nCriação (milisegundos): "+this.criacao;
      resp = resp + "\nNota: "+this.nota;
      resp = resp + "\nPergunta: "+this.pergunta;
      resp = resp + "\nAtiva: "+this.ativa;
      resp = resp + "\nAtiva: "+this.palavrasChave;
      return(resp);
   }

   /*
   chaveSecundaria - retorna a chave secundaria
   No caso das perguntas, retorna idPergunta => CRUD não aceita "" ou null
   */
   public String chaveSecundaria(){
      return Integer.toString(this.idPergunta);
   }

}