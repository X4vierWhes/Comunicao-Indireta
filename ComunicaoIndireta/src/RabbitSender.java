package src;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitSender {

    private static ServerSocket s;
    private final static String FILA = "drone";

    public static void main(String[] args) throws IOException {
        s = new ServerSocket(654321);
        while(true) {

            Socket client = s.accept();

            new Thread( () -> {
                try {
                    connect(client);
                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }


    }

    public static void connect(Socket client) throws IOException, TimeoutException {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(FILA, false, false, false, null);
            sendMsg(channel);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMsg(Channel c) throws IOException, InterruptedException {
        String mensagem;

        for (int i = 1; i <= 15; i++) {
            mensagem = "Faça algo (" + i + ")";
            System.out.println(" [x] Enviando essa mensagem para a fila: " + mensagem);
            c.basicPublish("", FILA, null, mensagem.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(1000);
        }
    }
}
