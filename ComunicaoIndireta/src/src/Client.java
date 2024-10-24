package src;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    MulticastSocket socket;

    private String    serverAddr;
    private int       serverPort;


    public Client(String serverAddr, int serverPort) {
        try {

            this.serverAddr = serverAddr;
            this.serverPort = serverPort;

            socket = new MulticastSocket(serverPort);
            InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName(serverAddr), serverPort);
            NetworkInterface interfaceRede = NetworkInterface.getByName("wlp2s0");
            socket.joinGroup(grupo, interfaceRede);


            new Thread(this::receberDados).start();

//            new Thread(
//                    () -> {
//                        Scanner in = new Scanner(System.in);
//
//                        while(true){
//                            System.out.println("S para solicitar dados.\nQ para fechar sistema.\n");
//                            boolean flag = true;
//                            while (flag) {
//                                String next = in.next();
//                                if (next.equals("S") || next.equals("s")) {
//                                    solicitarDados();
//                                    flag = false;
//                                } else if (next.equals("Q") || next.equals("q")) {
//                                    System.out.println("Saindo...");
//                                    System.exit(0);
//                                }
//                            }
//                        }
//                    }
//
//            ).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receberDados() {
        while (true) {
            DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(dp);
                ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
                DroneInfo info = (DroneInfo) inputStream.readObject();
                System.out.println("Recebido: " + info);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void solicitarDados() {
        try {
            byte[] dados = Arrays.copyOf("request".getBytes(), 1024);
            DatagramPacket dp = new DatagramPacket(
                    dados,
                    dados.length,
                    InetAddress.getByName(serverAddr),
                    5555);

            socket.send(dp);
            System.out.println("Solicitação enviada.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Main1{
        public static void main(String[] args) {
            new Client("225.0.0.1", 8080);
        }
    }
    public static class Main2{
        public static void main(String[] args) {
            new Client("225.0.0.1", 8080);
        }
    }
    public static class Main3 {
        public static void main(String[] args) {
            new Client("225.0.0.1", 8080);
        }
    }

    public static class Main4{
        public static void main(String[] args) {
            new Client("225.0.0.1", 8081);
        }
    }
    public static class Main5{
        public static void main(String[] args) {
            new Client("225.0.0.1", 8081);
        }
    }
    public static class Main6 {
        public static void main(String[] args) {
            new Client("225.0.0.1", 8081);
        }
    }
}
