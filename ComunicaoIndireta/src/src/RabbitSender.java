package src;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitSender {

    private final static String FILA = "drone";
    private final static String FilaAtmosferica = "FilaAtmosferica";
    private final static String FilaRadiacao = "FilaRadiacao";
    private final static String FilaTemperatura = "FilaTemperatura";
    private final static String FilaUmidade = "FilaUmidade";

    private static ConnectionFactory connectionFactory;

    private static Connection connection;

    private static Channel channel;

    private String host;
    private int port;
    private String exchange;

    public RabbitSender(String host, int port, String exchange) {
        this.host = host;
        this.port = port;
        this.exchange = exchange;
    }

    public  void connect() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        if (port != 15672)
            connectionFactory.setPort(port);

        System.out.println("Conectando ao RabbitMQ em " + host + ":" + port);

        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);

//        channel.queueDeclare(FILA, false,false,false, null);
//        channel.queueDeclare(FilaAtmosferica, false,false,false, null);
//        channel.queueDeclare(FilaRadiacao, false,false,false, null);
//        channel.queueDeclare(FilaTemperatura, false,false,false, null);
//        channel.queueDeclare(FilaUmidade, false,false,false, null);
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

    public void sendMsg(DroneInfo info) throws IOException {

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArray);
        out.writeObject(info);

        channel.basicPublish(exchange, "all", null, byteArray.toByteArray());

        DataOutputStream dataOut = new DataOutputStream(byteArray);

        dataOut.writeDouble(info.atmospherePressure());
        dataOut.writeLong(info.timestamp().toEpochSecond(java.time.ZoneOffset.UTC));
        dataOut.writeInt(info.id());


        channel.basicPublish(exchange, "pressao", null, byteArray.toByteArray());

        dataOut.writeDouble(info.radiation());
        dataOut.writeLong(info.timestamp().toEpochSecond(java.time.ZoneOffset.UTC));
        dataOut.writeInt(info.id());

        channel.basicPublish(exchange, "radiacao", null, byteArray.toByteArray());


        dataOut.writeDouble(info.temperature());
        dataOut.writeLong(info.timestamp().toEpochSecond(java.time.ZoneOffset.UTC));
        dataOut.writeInt(info.id());

        channel.basicPublish(exchange, "temperatura", null, byteArray.toByteArray());


        dataOut.writeDouble(info.moisture());
        dataOut.writeLong(info.timestamp().toEpochSecond(java.time.ZoneOffset.UTC));
        dataOut.writeInt(info.id());

        channel.basicPublish(exchange, "umidade", null, byteArray.toByteArray());

        out.close();
        byteArray.close();
    }

}
