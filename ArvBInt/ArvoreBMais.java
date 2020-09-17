import java.io.*;
import java.util.ArrayList;
import java.util.Arrays; 

class ArvoreBMais{
    private int ordem; // Numero maximo de filhos
    private RandomAccessFile arq; 
    private String nomeArq;

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
            this.TAMANHO_PAGINA = 4+(registros)*2*4+(ordem)*8+8;
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
            while(i<this.registros){
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
            this.filhos[this.ordem]  = dis.readLong();
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
        arq = new RandomAccessFile(this.nomeArq, "rws");
        
        //Verificar se arquivo está vazio
        if(arq.length() < 8)
            arq.writeLong(-1);//Raiz vazia
        Pagina p =new Pagina(10);
    }
    
    /* Funcao para verificar se arvore esta vazia
     * @return boolean resp
     */
    public boolean isEmpty()throws Exception{
        arq.seek(0);//mover ponteiro para cabeçalho
        return arq.readLong() == -1 ? true : false; 
    }

    

    public int getOrdem(){
        return this.ordem;
    }

    public String getNomeArq(){
        return this.nomeArq;
    }
}