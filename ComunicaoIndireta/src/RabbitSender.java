package src;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitSender {

    private static ServerSocket s;
    private final static String FILA = "drone";
    private final static String FilaAtmosferica = "FilaAtmosferica";
    private final static String FilaRadiacao = "FilaRadiacao";
    private final static String FilaTemperatura = "FilaTemperatura";
    private final static String FilaUmidade = "FilaUmidade";

    private static ConnectionFactory connectionFactory;

    private static Connection connection;

    private static Channel channel;

    public RabbitSender(){

    }

    public  void connect() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(FILA, false,false,false, null);
        channel.queueDeclare(FilaAtmosferica, false,false,false, null);
        channel.queueDeclare(FilaRadiacao, false,false,false, null);
        channel.queueDeclare(FilaTemperatura, false,false,false, null);
        channel.queueDeclare(FilaUmidade, false,false,false, null);
    }

    public  void sendMsg(String msg) throws IOException, InterruptedException {
        //System.out.println(msg);
        channel.basicPublish("", FILA, null, msg.getBytes(StandardCharsets.UTF_8));

        String[] dados = msg.split("\\|");
        String[] filas = {FilaAtmosferica, FilaRadiacao, FilaTemperatura, FilaUmidade};
        for(int i = 0; i < 4; i++){
            channel.basicPublish("", filas[i], null, dados[i].getBytes(StandardCharsets.UTF_8));
        }
        //System.out.println(dados[0]);

    }
}
