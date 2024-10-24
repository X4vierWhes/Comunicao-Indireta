package src;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ServerRabbitMQ extends ServerBase {

    private RabbitSender rabbitSender;
    private MulticastSocket multicastSocket;

    private String host = "localhost";
    private int port = 15672;
    private String exchange;

    public ServerRabbitMQ(InetSocketAddress myAddr, String host, int port, String exchange) {
        super(myAddr);
        this.host = host;
        this.port = port;
        this.exchange = exchange;
    }

    @Override
    protected void init(){
        try {
            System.out.println("ServerRabbitMQ criado com endereço " + myAddr);
            multicastSocket = new MulticastSocket(myAddr.getPort());
            NetworkInterface interfaceRede = NetworkInterface.getByName("wlp2s0");
            multicastSocket.joinGroup(myAddr, interfaceRede);

            // o host e a porta deveriam ser dinamicos mas nao sei oq acontece que eles ficam como valores vazios aq...
            rabbitSender = new RabbitSender("localhost", 15672, "server");
            rabbitSender.connect();

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessage() throws Exception {

        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
        multicastSocket.receive(dp);
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
        DroneInfo info = (DroneInfo) inputStream.readObject();

        System.out.println("Recebido: " + info);

        rabbitSender.sendMsg(info);
    }

}