import java.util.Scanner;


public class Main {

  // Método principal apenas para testes
  public static void main(String[] args) {
    try{
        ArvoreBMais ab = new ArvoreBMais(5, "dados2.db");
        System.out.println(ab.create(1,4));

        System.out.println(ab.create(3,2));
        System.out.println(ab.create(2,2));
        System.out.println(ab.create(3,4));
        ab.print();
    }catch(Exception e){
        System.out.println(e);
    }
  }
}