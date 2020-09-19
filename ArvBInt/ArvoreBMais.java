import java.io.*;
import java.util.ArrayList;
import java.util.Arrays; 

class ArvoreBMais{
    private int ordem; // Numero maximo de filhos
    private RandomAccessFile arq; 
    private String nomeArq;
    private int maxElementos;

    //Variaveis globais auxiliares 
    private int  chaveAux;
    private int  dadoAux;
    private long paginaAux;
    private boolean cresceu;
    private boolean diminuiu;

    class Pagina{
        protected int ordem; // Número maximo de filhos
        protected int registros; // Número maximo de registros
        protected int n; //Número de Elementos que pagina contém
        protected int[] chaves;
        protected int[] dados;
        protected long proxima; //Aponta para pagina irma quando for folha
        protected long[] filhos; //Vetor que aponta para filhos
        protected int TAMANHO_PAGINA; //Tamanho fixo da pagina calculado a partir da ordem

        public Pagina(int ordem){
            this.ordem = ordem;
            this.registros = ordem-1;
            this.n = 0;
            this.proxima = -1;

            this.chaves = new int[this.registros];
            this.dados  = new int[this.registros];
            this.filhos = new long[this.ordem];
            
            //Preencher vetore com valores nulos
            Arrays.fill(this.chaves, -1); 
            Arrays.fill(this.dados, -1);
            Arrays.fill(this.filhos, -1);  

            //n+chaves+dados+filhos
            this.TAMANHO_PAGINA = 4+((registros)*2*4)+(ordem)*8+8;
        }

        /*Funcao para retornar arranjo de bytes que representa a Pagina 
         *@return byte[] ba  
         */
        protected byte[] getByteArray()throws Exception{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(n);//Escrever quantidade de elementos na pagina
            
            int i;
            
            for(i = 0; i < n; i++){
                dos.writeLong(this.filhos[i]);//escrever ponteiros para filhos
                dos.writeInt(this.chaves[i]);//escrever chaves
                dos.writeInt(this.dados[i]);//escrever dados
            }
            
            dos.writeLong(filhos[i]);

            // Completa o restante da página com registros vazios
            byte[] registroVazio = new byte[8];
            while(i < this.registros-1){
                dos.write(registroVazio);
                dos.writeLong(filhos[i+1]);
                i++;
            }

            dos.writeLong(filhos[i]);//Escrever ponteiro para página irmã
            return bos.toByteArray();
        }

       /*Funcao para construir pagina a partir de vetor de bytes
        *@param byte[] ba  
        */
        protected void fromByteArray(byte[] ba)throws Exception{
            ByteArrayInputStream bis = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bis);

            this.n = dis.readInt();//Ler quantidade de registros
            //Ler todos registros
            for(int i = 0; i < this.registros; i++){
                this.filhos[i]  = dis.readLong();
                this.chaves[i]  = dis.readInt(); 
                this.dados[i]   = dis.readInt(); 
            }
            this.filhos[this.ordem-1]  = dis.readLong();
            this.proxima = dis.readLong();
        }
    }


    /*Construtor classe ArvoreBMais, recebe como parametro a 
     *ordem da arvore e nome do arquivo que dados serão escritos
     *@param int ordem
     *@param nomeArq
     */
    public ArvoreBMais(int ordem, String nomeArq)throws Exception{
        this.ordem = ordem;
        this.nomeArq = nomeArq;
        this.maxElementos = ordem-1;
        arq = new RandomAccessFile(this.nomeArq, "rws");
        
        //Verificar se arquivo está vazio
        if(arq.length() < 8)
            arq.writeLong(-1);//Raiz vazia
        Pagina p = new Pagina(10);
    }
    
    /* Funcao para verificar se arvore esta vazia
     * @return boolean resp
     */
    public boolean isEmpty()throws Exception{
        arq.seek(0);//mover ponteiro para cabeçalho
        return arq.readLong() == -1 ? true : false; 
    }

    /* Metodo para inserir elemento na ArvoreB+. Recebe a chave  e o dado como  
     * parametro e retorna um boolean para indicar o sucesso da operação 
     * Utliza o metodo create recursivo
     * @param int chave, int dados
     * @return boolean resp
     */
    public boolean create(int chave, int dado)throws Exception{
        arq.seek(0);
        long pagina = arq.readLong();//Ler raiz
        
        this.chaveAux = chave;
        this.dadoAux = dado;
        this.paginaAux = -1;//Valor nulo
        this.cresceu = false;

        boolean resp = create(pagina);

        //Criar nova raiz
        if(cresceu) {
            
            Pagina raiz = new Pagina(ordem);
            raiz.n = 1;
            raiz.chaves[0] = chaveAux;
            raiz.dados[0]  = dadoAux;
            raiz.filhos[0] = pagina;
            raiz.filhos[1] = paginaAux;

            arq.seek(arq.length());
            long posRaiz = arq.getFilePointer();
            arq.write(raiz.getByteArray());
            arq.seek(0);
            arq.writeLong(posRaiz);
            resp = true; 
        }


        return resp;
    }
    
    /* Metodo recursivo para inserir elemento na ArvoreB+. Recebe o enderco do no 
     * atual como parametro e retorna um boolean para indicar o sucesso da operação
     * @param int chave, int dados
     * @return boolean resp
     */
    public boolean create(long pagAtual)throws Exception{
        boolean resp;

        //Pagina atual nao existe
        if(pagAtual == -1){
            this.cresceu = true;
            this.paginaAux = -1;
            resp = false;
        }else{
            arq.seek(pagAtual);
            
            Pagina pa = new Pagina(this.ordem);//Ler pagina atual
            byte[] buffer = new byte[pa.TAMANHO_PAGINA];

            arq.read(buffer);
            pa.fromByteArray(buffer);
            
            //Encontrar posição de descida
            int i=0;
            for(i = 0; i < pa.n && pa.chaves[i] != -1 && chaveAux > pa.chaves[i];i ++);

            //Continuar buscar recursiva
            if(i == pa.n || this.chaveAux < pa.chaves[i]){
                resp = create(pa.filhos[i]);
            }
            else{
                resp = create(pa.filhos[i+1]);
            }
            
            //Chegou em um caminho
            if(cresceu){
                //Pagina com espaço para inserir novo elemento
                if(pa.n < maxElementos){
                    //Shift elementos maiores que chave nova
                    //para direita
                    for(int j = pa.n; j > i ; j--){
                        pa.chaves[j] = pa.chaves[j-1];
                        pa.dados[j] = pa.dados[j-1];
                        pa.filhos[j+1] = pa.filhos[j];
                    }
                
                    //Inserindo novo elemento
                    pa.chaves[i]   = chaveAux;
                    pa.dados[i]    = dadoAux;
                    pa.filhos[i+1] = -1;
                    pa.n++;
                    
                    //Reescrever Pagina
                    arq.seek(pagAtual);
                    arq.write(pa.getByteArray());
                    this.cresceu = false;
                    resp = true;
                }else{ //Elemento não cabe na pagAtual
                    Pagina nova = new Pagina(this.ordem);
                    
                    //Copiar a metade com elementos maiores 
                    int meio = this.maxElementos/2;
                    //Copiar primeiro filho dos maiores

                    for(int j = 0; j < this.maxElementos-meio; j++){
                        nova.chaves[j] = pa.chaves[j+meio];
                        nova.dados[j] = pa.dados[j+meio];
                        nova.filhos[j+1] = pa.filhos[j+1+meio];

                        //Valores nulos em posições apagadas
                        pa.chaves[j+meio] = -1;
                        pa.dados[j+meio]  = -1;
                        pa.filhos[j+meio] = -1;
                    }
                    nova.filhos[0] = pa.filhos[meio];
                    //Atualizar número de elementos
                    nova.n = maxElementos - meio;
                    pa.n = meio;
                    
                    //Posição de inserção no lado direito
                    if(i <= meio){
                        for(int j = meio; j > 0 && j > i; j--){
                            pa.chaves[j]   = pa.chaves[j-1];
                            pa.dados[j]    = pa.dados[j-1];
                            pa.filhos[j+1] = pa.filhos[j];
                        }

                        //Inserindo elemento
                        pa.chaves[i] = this.chaveAux;
                        pa.dados[i] = this.dadoAux;
                        pa.filhos[i+1] = this.paginaAux;
                        pa.n++;
    
                        // Folha -> Promover menor da direita
                        if(pa.filhos[0]==-1) {
                            this.chaveAux = nova.chaves[0];
                            this.dadoAux = nova.dados[0];
                        }else{ //Senão -> Promover maior da pagina
                            this.chaveAux = pa.chaves[pa.n-1];
                            this.dadoAux = pa.dados[pa.n-1];
                            //Remover elemento do caminho
                            pa.chaves[pa.n-1] = -1;
                            pa.dados[pa.n-1] = -1;
                            pa.filhos[pa.n] = -1;
                            pa.n--;
                        }
                        

                    }else{//Posição de inserção no lado esquerdo

                        int j;
                        for( j = nova.n; j > 0 && chaveAux < nova.chaves[j-1]; j--){
                            nova.chaves[j] = nova.chaves[j-1];
                            nova.dados[j] = nova.dados[j-1];
                            nova.filhos[j+1] = nova.filhos[j];
                        }
                        //Inserir elemento
                        nova.chaves[j]   = this.chaveAux;
                        nova.dados[j]    = this.dadoAux;
                        nova.filhos[j+1] = this.paginaAux;
                        nova.n++;

                        //Elemento a ser promovido
                        this.chaveAux = nova.chaves[0];
                        this.dadoAux  = nova.dados[0];

                        //Remover elemento do caminho se não for folha
                        if(pa.filhos[0]!=-1) {
                            //Shift de elementos para esquerda
                            for(j=0; j < nova.n-1; j++) {
                                nova.chaves[j] = nova.chaves[j+1];
                                nova.dados[j] = nova.dados[j+1];
                                nova.filhos[j] = nova.filhos[j+1];
                            }
                            nova.filhos[j] = nova.filhos[j+1];
                            
                            //Remover ultimo da pagina(Duplicado)
                            nova.chaves[j] = -1;
                            nova.dados[j] = -1;
                            nova.filhos[j+1] = -1;
                            nova.n--;
                        }
                        
                    }

                    if(pa.filhos[0]==-1) {
                        nova.proxima=pa.proxima;
                        pa.proxima = arq.length();
                    }
                    
                    paginaAux = arq.length();
                    arq.seek(paginaAux);
                    arq.write(nova.getByteArray());

                    arq.seek(pagAtual);
                    arq.write(pa.getByteArray());
                    resp = true;
                }           
            }                 
        }
        return resp;
    }


    public void print() throws Exception {
        long raiz;
        arq.seek(0);
        raiz = arq.readLong();
        if(raiz!=-1)
            print1(raiz);
        System.out.println();
    }
    
    // Impressão recursiva
    private void print1(long pagina) throws Exception {
        
        // Retorna das chamadas recursivas
        if(pagina==-1)
            return;
        int i;

        // Lê o registro da página passada como referência no arquivo
        arq.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arq.read(buffer);
        pa.fromByteArray(buffer);
        
        // Imprime a página
        String endereco = String.format("%04d", pagina);
        System.out.print(endereco+"  " + pa.n +":"); // endereço e número de elementos
        for(i=0; i<maxElementos; i++) {
            System.out.print("("+String.format("%04d",pa.filhos[i])+") "+pa.chaves[i]+","+String.format("%2d",pa.dados[i])+" ");
        }
        System.out.print("("+String.format("%04d",pa.filhos[i])+")");
        if(pa.proxima==-1)
            System.out.println();
        else
            System.out.println(" --> ("+String.format("%04d", pa.proxima)+")");
        
        // Chama recursivamente cada filho, se a página não for folha
        if(pa.filhos[0] != -1) {
            for(i=0; i<pa.n; i++)
                print1(pa.filhos[i]);
            print1(pa.filhos[i]);
        }
    }



    public int getOrdem(){
        return this.ordem;
    }

    public String getNomeArq(){
        return this.nomeArq;
    }
}