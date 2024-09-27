import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server{

    private ServerSocket s;
    private DatagramSocket toClient;
    private BufferedReader in;
    private ExecutorService executor;

    public Server(int port){
        try {
            s = new ServerSocket(port);
            toClient = new DatagramSocket();

            while(true){
                Socket client = s.accept();
                System.out.println("Conexão estabelecida com cliente " + client.getLocalAddress() + ":" + client.getLocalPort());
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                executor = Executors.newCachedThreadPool();

                executor.execute(() -> {
                    try {
                        String msg;
                        while ((msg = in.readLine()) != null) {
                            // formata mensagem
                            String teste = Arrays.stream(msg.toString().split("\\|")).collect(Collectors.joining("\n"));
                            teste += "\n";
                            System.out.println(teste);


                            // envia a mensagem para clientes conectados
                            DatagramPacket dp = new DatagramPacket(
                                    Arrays.copyOf(msg.toString().getBytes(), 1024), 1024,
                                    InetAddress.getByName("225.0.0.1"), 5555
                            );

                            toClient.send(dp);

                        }




                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void recieve(BufferedReader buffer) {
        try {

            StringBuilder msg = new StringBuilder();
            while (buffer.ready()){
                msg.append(buffer.readLine());
            }

            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server(12345);
    }
}