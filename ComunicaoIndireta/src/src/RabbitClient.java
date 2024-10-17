package src;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitClient {

    private final static String FILA = "drone";
    private final static String FilaAtmosferica = "FilaAtmosferica";
    private final static String FilaRadiacao = "FilaRadiacao";
    private final static String FilaTemperatura = "FilaTemperatura";
    private final static String FilaUmidade = "FilaUmidade";
    private final static String[] filas = {FilaAtmosferica, FilaRadiacao, FilaTemperatura, FilaUmidade};
    private static boolean running = true;

    private static int fila = 0;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(FILA, false, false, false, null);

        DeliverCallback callback = (consumidor, entrega) -> {
            String msg = new String(entrega.getBody(), StandardCharsets.UTF_8);
            System.out.println(msg);

            if(!running){
                channel.basicCancel(filas[fila]);
                System.out.println("Consumidor " + filas[fila] + " parado");
            }
        };

        boolean autoAck = true;
        //channel.basicConsume(FILA, autoAck, callback, consumidor -> { });

        String escolha = "Entrar";
        Scanner cin = new Scanner(System.in);
        while(!escolha.equals("S")){

            System.out.print("Digite R para solicitar RADIAÇÃO" + "\n"
                    + "Digite T para solicitar TEMPERATURA" + "\n"
                    + "Digite P para solicitar PRESSÃO ATMOSFERICA\n"
                    + "Digite U para solicitar UMIDADE\n"
                    + "Digite S para SAIR.\n"
                    + "Escolha: ");

            escolha = cin.next();

            switch(escolha){
                case "T": //Solicita temperatura
                    channel.basicConsume(FilaTemperatura, autoAck, callback, consumidor -> { });
                    String a = "b";
                    fila = 0;
                    while(true){
                        a = cin.next();
                        if(a.equalsIgnoreCase("a")) {
                            running = !running;
                            break;
                        }
                    }
                    //running = !running;
                    break;
                case "R": //Solicita radiação
                    channel.basicConsume(FilaRadiacao, autoAck, callback, consumidor -> { });
                    break;
                case "U": //Solicita umidade
                    channel.basicConsume(FilaUmidade, autoAck, callback, consumidor -> { });
                    break;
                case "P": //Solicita pressão atmosferica
                    channel.basicConsume(FilaAtmosferica, autoAck, callback, consumidor -> { });
                    break;
                case "S": //Sai da aplicação
                    break;
                default:
                    break;
            }

        }
    }
}
