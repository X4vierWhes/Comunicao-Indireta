package src;

import com.rabbitmq.client.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

public class RabbitClient {

    private final static String HOST = "localhost";
    private final static int PORT = 15672;
    private final static String EXCHANGE = "server";

    private static String QUEUE_NAME;

    private static boolean[] boundTopics = {true, false, false, false, false};


    private final static String FILA = "drone";
    private final static String FilaAtmosferica = "FilaAtmosferica";
    private final static String FilaRadiacao = "FilaRadiacao";
    private final static String FilaTemperatura = "FilaTemperatura";
    private final static String FilaUmidade = "FilaUmidade";
    private final static String[] filas = {FilaAtmosferica, FilaRadiacao, FilaTemperatura, FilaUmidade};
    private static boolean running = true;

    private String queueName = "";

    private static int fila = 0;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
        QUEUE_NAME = channel.queueDeclare().getQueue();
        String routingKey = "all";
        channel.queueBind(QUEUE_NAME, EXCHANGE, routingKey);
        DeliverCallback callback = (consumidor, entrega) -> {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(entrega.getBody()));

            try {
                DroneInfo info = (DroneInfo) inputStream.readObject();
                System.out.println(info);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if(!running){
                channel.basicCancel(filas[fila]);
                System.out.println("Consumidor " + filas[fila] + " parado");
            }
        };
        boolean autoAck = true;

        ExecutorService teste = Executors.newCachedThreadPool();
        teste.submit(() -> {
            while (true){
                try {
                    channel.basicConsume(QUEUE_NAME, autoAck, callback, consumidor -> { });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        channel.queueDeclare(FILA, false, false, false, null);

        String escolha = "Entrar";
        Scanner cin = new Scanner(System.in);
        while(!escolha.equals("S")){

            System.out.print(
            "Digite R para " + (boundTopics[2] ? "cancelar solicitacao de" : "solicitar") + " RADIAÇÃO" + "\n"
            + "Digite T para " + (boundTopics[1] ? "cancelar solicitacao de" : "solicitar") + " TEMPERATURA" + "\n"
            + "Digite P para " + (boundTopics[4] ? "cancelar solicitacao de" : "solicitar") + " PRESSÃO ATMOSFERICA\n"
            + "Digite U para " + (boundTopics[3] ? "cancelar solicitacao de" : "solicitar") + " UMIDADE\n"
            + "Digite A para " + (boundTopics[0] ? "cancelar solicitacao de" : "solicitar") + " TUDO\n"
            + "Digite S para SAIR. Escolha: \n");

            escolha = cin.next();

            switch(escolha){
                case "A":
                    if(boundTopics[0]){
                        channel.queueUnbind(QUEUE_NAME, EXCHANGE, "all");
                        boundTopics[0] = false;

                        System.out.println("Unbind all");
                    } else {
                        channel.queueBind(QUEUE_NAME, EXCHANGE, "all");
                        boundTopics[0] = true;
                        int i = 1;
                        while (i < 4)
                            boundTopics[i++] = false;

                        System.out.println("Bind all");
                    }
                    break;
                case "T": //Solicita temperatura
                    if(boundTopics[1]){
                        channel.queueUnbind(QUEUE_NAME, EXCHANGE, "temperatura");
                        boundTopics[1] = false;

                        System.out.println("Unbind temperatura");
                    } else {
                        channel.queueBind(QUEUE_NAME, EXCHANGE, "temperatura");
                        boundTopics[1] = true;

                        System.out.println("Bind temperatura");
                    }

//                    channel.basicConsume(FilaTemperatura, autoAck, callback, consumidor -> { });
//
//                    String a = "b";
//
//                    fila = 0;
//
//                    while(true){
//                        a = cin.next();
//                        if(a.equalsIgnoreCase("a")) {
//                            running = !running;
//                            break;
//                        }
//                    }
                    //running = !running;
                    break;
                case "R": //Solicita radiação
                    if(boundTopics[2]){
                        channel.queueUnbind(QUEUE_NAME, EXCHANGE, "radiacao");
                        boundTopics[2] = false;

                        System.out.println("Unbind radiação");
                    } else {
                        channel.queueBind(QUEUE_NAME, EXCHANGE, "radiacao");
                        boundTopics[2] = true;

                        System.out.println("Bind radiação");
                    }
//                    channel.basicConsume(FilaRadiacao, autoAck, callback, consumidor -> { });
                    break;
                case "U": //Solicita umidade
                    if(boundTopics[3]){
                        channel.queueUnbind(QUEUE_NAME, EXCHANGE, "umidade");
                        boundTopics[3] = false;

                        System.out.println("Unbind umidade");
                    } else {
                        channel.queueBind(QUEUE_NAME, EXCHANGE, "umidade");
                        boundTopics[3] = true;

                        System.out.println("Bind umidade");
                    }
//                    channel.basicConsume(FilaUmidade, autoAck, callback, consumidor -> { });
                    break;
                case "P": //Solicita pressão atmosferica
                    if(boundTopics[4]){
                        channel.queueUnbind(QUEUE_NAME, EXCHANGE, "pressao");
                        boundTopics[4] = false;

                        System.out.println("Unbind pressão atmosférica");
                    } else {
                        channel.queueBind(QUEUE_NAME, EXCHANGE, "pressao");
                        boundTopics[4] = true;

                        System.out.println("Bind pressão atmosférica");
                    }
//                    channel.basicConsume(FilaAtmosferica, autoAck, callback, consumidor -> { });
                    break;
                case "S": //Sai da aplicação
                    break;
                default:
                    break;
            }

        }
    }
}
