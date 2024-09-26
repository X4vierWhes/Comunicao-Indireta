import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    MulticastSocket socket;

    private final String    serverAddr = "127.0.0.1";
    private final int       serverPort = 5555;


    public Client(int port) {
        try {
            socket = new MulticastSocket(port);
            InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("225.0.0.1"), 5555);
            NetworkInterface interfaceRede = NetworkInterface.getByName("en0");
            socket.joinGroup(grupo, interfaceRede);


            new Thread(this::receberDados).start();
            new Thread(
                    () -> {
                        Scanner in = new Scanner(System.in);

                        while(true){
                            System.out.println("S para solicitar dados.\nQ para fechar sistema.\n");
                            boolean flag = true;
                            while (flag) {
                                String next = in.next();
                                if (next.equals("S") || next.equals("s")) {
                                    solicitarDados();
                                    flag = false;
                                } else if (next.equals("Q") || next.equals("q")) {
                                    System.out.println("Saindo...");
                                    System.exit(0);
                                }
                            }
                        }
                    }

            ).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receberDados() {
        while (true) {
            DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(dp);

                System.out.println("Recebi: " + new String(dp.getData()));
            } catch (IOException e) {
                throw new RuntimeException(e);
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
                    serverPort);

            socket.send(dp);
            System.out.println("Solicitação enviada.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class Main1{
        public void main(String[] args) {
            new Client(5555);
        }
    }
    public class Main2{
        public void main(String[] args) {
            new Client(5556);
        }
    }
    public class Main3 {
        public void main(String[] args) {
            new Client(5557);
        }
    }
}
