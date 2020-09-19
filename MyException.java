/*
Ciencia da Computacao - Pucminas
AED3 - manha
Larissa Domingues Gomes
Marcelo Franca Cabral
Pedro Henrique Lima Carvalho
Tarcila Fernanda Resende da Silva

Classe MyException - Cria Exceções para operacoes CRUD de Arquivo
Versão 1.0 
*/




public class MyException extends Exception {
  final public static String INVALID_FILE      = "Arquivo !CRUD2.0.";
  final public static String INCONSISTENT_FILE = "Arquivos Inconsistentes CRUDxApagados.";
  final public static String INVALID_REGISTER  = "Registro inexistente.";
  
  public MyException() { 
    super(); 
  }
  public MyException(String message) { 
      super(message); 
  }

}
