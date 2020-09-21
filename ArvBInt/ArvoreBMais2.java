import java.io.*;
import java.util.ArrayList;
import java.util.Arrays; 

class ArvoreBMais2{
    private int ordem; // Numero maximo de filhos
    private RandomAccessFile arquivo; 
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
            while(i < this.registros){
                dos.writeInt(-1);
                dos.writeInt(-1);
                dos.writeLong(filhos[i+1]);
                i++;
            }

            dos.writeLong(proxima);//Escrever ponteiro para página irmã
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
    public ArvoreBMais2(int ordem, String nomeArq)throws Exception{
        this.ordem = ordem;
        this.nomeArq = nomeArq;
        this.maxElementos = ordem-1;
        arquivo = new RandomAccessFile(this.nomeArq, "rws");
        
        //Verificar se arquivo está vazio
        if(arquivo.length() < 8)
            arquivo.writeLong(-1);//Raiz vazia
        Pagina p = new Pagina(10);
    }
    
    /* Funcao para verificar se arvore esta vazia
     * @return boolean resp
     */
    public boolean isEmpty()throws Exception{
        arquivo.seek(0);//mover ponteiro para cabeçalho
        return arquivo.readLong() == -1 ? true : false; 
    }

    /* Metodo para inserir elemento na ArvoreB+. Recebe a chave  e o dado como  
     * parametro e retorna um boolean para indicar o sucesso da operação 
     * Utliza o metodo create recursivo
     * @param int chave, int dados
     * @return boolean resp
     */
    public boolean create(int chave, int dado)throws Exception{
        //System.out.println(arquivo.length());
        arquivo.seek(0);
        long pagina = arquivo.readLong();//Ler raiz
        
        this.chaveAux = chave;
        this.dadoAux = dado;
        this.paginaAux = -1;//Valor nulo
        this.cresceu = false;

        boolean resp = create1(pagina);

        //Criar nova raiz
        if(cresceu) {
            
            Pagina raiz = new Pagina(ordem);
            raiz.n = 1;
            raiz.chaves[0] = chaveAux;
            raiz.dados[0]  = dadoAux;
            raiz.filhos[0] = pagina;
            raiz.filhos[1] = paginaAux;

            arquivo.seek(arquivo.length());
            long posRaiz = arquivo.getFilePointer();
            arquivo.write(raiz.getByteArray());
            arquivo.seek(0);
            arquivo.writeLong(posRaiz);
            resp = true; 
        }


        return resp;
    }
    
    /* Metodo recursivo para inserir elemento na ArvoreB+. Recebe o enderco do no 
     * atual como parametro e retorna um boolean para indicar o sucesso da operação
     * @param int chave, int dados
     * @return boolean resp
     */
    private boolean create1(long pagina) throws Exception {
        //System.out.println(arquivo.length());
        // Testa se passou para o filho de uma página folha. Nesse caso, 
        // inicializa as variáveis globais de controle.
        if(pagina==-1) {
            cresceu = true;
            paginaAux = -1;
            return false;
        }
        
        // Lê a página passada como referência
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArray(buffer);
        
        // Busca o próximo ponteiro de descida. Como pode haver repetição
        // da primeira chave, a segunda também é usada como referência.
        // Nesse primeiro passo, todos os pares menores são ultrapassados.
        int i=0;
        while(i<pa.n && chaveAux > pa.chaves[i] && pa.chaves[i] != -1 ) {
            i++;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n || chaveAux < pa.chaves[i])
            inserido = create1(pa.filhos[i]);
        else
            inserido = create1(pa.filhos[i+1]);
        
        // A partir deste ponto, as chamadas recursivas já foram encerradas. 
        // Assim, o próximo código só é executado ao retornar das chamadas recursivas.

        // A inclusão já foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursão.
        // A inclusão pode ter sido resolvida porque a chave já existia (inclusão inválida)
        // ou porque o novo elemento coube em uma página existente.
        if(!cresceu)
            return inserido;
        
        // Se tiver espaço na página, faz a inclusão nela mesmo
        if(pa.n<maxElementos) {

            // Puxa todos elementos para a direita, começando do último
            // para gerar o espaço para o novo elemento
            for(int j=pa.n; j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Escreve a página atualizada no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.getByteArray());
            
            // Encerra o processo de crescimento e retorna
            cresceu=false;
            return true;
        }
        
        // O elemento não cabe na página. A página deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referência da folha).
        
        // Cria uma nova página
        Pagina np = new Pagina(ordem);
        
        // Copia a metade superior dos elementos para a nova página,
        // considerando que maxElementos pode ser ímpar
        int meio = maxElementos/2;
        for(int j=0; j<(maxElementos-meio); j++) {    
            
            // copia o elemento
            np.chaves[j] = pa.chaves[j+meio];
            np.dados[j] = pa.dados[j+meio];   
            np.filhos[j+1] = pa.filhos[j+meio+1];  
            
            // limpa o espaço liberado
            pa.chaves[j+meio] = -1;
            pa.dados[j+meio] = -1;
            pa.filhos[j+meio+1] = -1;
        }
        np.filhos[0] = pa.filhos[meio];
        np.n = maxElementos-meio;
        pa.n = meio;
        
        // Testa o lado de inserção
        // Caso 1 - Novo registro deve ficar na página da esquerda
        if(i<=meio) {   
            
            // Puxa todos os elementos para a direita
            for(int j=meio; j>0 && j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Se a página for folha, seleciona o primeiro elemento da página 
            // da direita para ser promovido, mantendo-o na folha
            if(pa.filhos[0]==-1) {
                chaveAux = np.chaves[0];
                dadoAux = np.dados[0];
            }
            
            // caso contrário, promove o maior elemento da página esquerda
            // removendo-o da página
            else {
                chaveAux = pa.chaves[pa.n-1];
                dadoAux = pa.dados[pa.n-1];
                pa.chaves[pa.n-1] = -1;
                pa.dados[pa.n-1] = -1;
                pa.filhos[pa.n] = -1;
                pa.n--;
            }
        } 
        
        // Caso 2 - Novo registro deve ficar na página da direita
        else {
            int j;
            for(j=maxElementos-meio; j>0 && chaveAux < np.chaves[j-1]; j--) {
                np.chaves[j] = np.chaves[j-1];
                np.dados[j] = np.dados[j-1];
                np.filhos[j+1] = np.filhos[j];
            }
            np.chaves[j] = chaveAux;
            np.dados[j] = dadoAux;
            np.filhos[j+1] = paginaAux;
            np.n++;

            // Seleciona o primeiro elemento da página da direita para ser promovido
            chaveAux = np.chaves[0];
            dadoAux = np.dados[0];
            
            // Se não for folha, remove o elemento promovido da página
            if(pa.filhos[0]!=-1) {
                for(j=0; j<np.n-1; j++) {
                    np.chaves[j] = np.chaves[j+1];
                    np.dados[j] = np.dados[j+1];
                    np.filhos[j] = np.filhos[j+1];
                }
                np.filhos[j] = np.filhos[j+1];
                
                // apaga o último elemento
                np.chaves[j] = -1;
                np.dados[j] = -1;
                np.filhos[j+1] = -1;
                np.n--;
            }

        }
        
        // Se a página era uma folha e apontava para outra folha, 
        // então atualiza os ponteiros dessa página e da página nova
        if(pa.filhos[0]==-1) {
            np.proxima=pa.proxima;
            System.out.println(arquivo.length());
            pa.proxima = arquivo.length();
        }

        // Grava as páginas no arquivos arquivo
        paginaAux = arquivo.length();
        arquivo.seek(paginaAux);
        arquivo.write(np.getByteArray());

        arquivo.seek(pagina);
        arquivo.write(pa.getByteArray());
        
        return true;
    }

    public void print() throws Exception {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
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
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
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