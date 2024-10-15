package src;

import java.util.Scanner;

public class RabbitClient {

    public static void main(String[] args) {
        String escolha = "Entrar";
        Scanner cin = new Scanner(System.in);
        while(!escolha.equals("S")){

            System.out.println("Digite R para solicitar RADIAÇÃO" + "\n"
                    + "Digite T para solicitar TEMPERATURA" + "\n"
                    + "Digite S para SAIR.\n"
                    + "Escolha: ");

            escolha = cin.next();

            switch(escolha){
                case "T": //Solicita temperatura
                    break;
                case "R": //Solicita radiação
                    break;
                case "S": //Sai da aplicação
                    break;
                default:
                    break;
            }

        }
    }
}
