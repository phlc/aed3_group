import java.io.*;

public class Teste {

   // Arquivo declarado fora de main() para poder ser usado por outros métodos
   private static CRUD<Usuario> arqUsuario;

   public static void main(String[] args) {

      // Livros de exemplo
      Usuario[] usuarios = new Usuario[100];
    
      for(int i=0; i<usuarios.length; i++){
         usuarios[i] = new Usuario(-1, "u"+i, i+"@puc", "senha"+i);
      } 

      try {

         // Abre (cria) o arquivo de usuarios
         arqUsuario = new CRUD<Usuario>(Usuario.class.getConstructor(), "usuarios.db");

         // Insere os usuários
         for(int i=0; i<usuarios.length; i++){
            usuarios[i].setID(arqUsuario.create(usuarios[i]));
         }
   
         // Busca por dois usuarios pelo ID
         System.out.println(arqUsuario.read(usuarios[26].getID()));
         System.out.println(arqUsuario.read(usuarios[59].getID()));

         // Busca por dois usuarios pela chave secundaria
         System.out.println(arqUsuario.read(usuarios[76].chaveSecundaria()));
         System.out.println(arqUsuario.read(usuarios[91].chaveSecundaria()));

         // Altera um usuario para um tamanho maior e exibe o resultado
         usuarios[10].senha = "alterado10@puc";
         arqUsuario.update(usuarios[10]);
         System.out.println(arqUsuario.read(usuarios[10].getID()));

         // Altera um usuario para um tamanho menor e exibe o resultado
         usuarios[63].email = "63@";
         arqUsuario.update(usuarios[63]);
         System.out.println(arqUsuario.read(usuarios[63].getID()));

         // Excluir um usuario e mostra que não existe mais
         arqUsuario.delete(usuarios[10].getID());
         Usuario u = arqUsuario.read(usuarios[10].getID());
         if(u==null)
            System.out.println("Livro excluído");
         else
            System.out.println(u);

         // Inserir um menor para ocupar a posicao do 10
         Usuario n = new Usuario(-1, "n", "n@n", "nn");
         n.setID(arqUsuario.create(n));
   
         // Busca por n
         System.out.println(arqUsuario.read(n.getID()));

      } catch (Exception e) {
         e.printStackTrace();
      }

  }

}

