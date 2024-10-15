package src;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server{

    private ServerSocket s;

    private Socket rabbitSocket;
    private DatagramSocket toClient;
    private int redirectPort;
    private ExecutorService executor;

    public Server(int DronePort, int redirectPort) {
        try {
            s = new ServerSocket(DronePort);

            this.redirectPort = redirectPort;

            toClient = new DatagramSocket(DronePort);

            executor = Executors.newFixedThreadPool(10);

            executor.submit(() -> {
                while(true) {
                Socket client = s.accept();
                System.out.println("Conexão estabelecida com cliente " + client.getLocalAddress() + ":" + client.getLocalPort());

                executor.submit(() -> {
                    try {
                        handleDroneMessage(new BufferedReader(new InputStreamReader(client.getInputStream())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                }
            });

            DatagramPacket teste = new DatagramPacket(new byte[1024], 1024);
            toClient.receive(teste);
            System.out.println("Recebi: " + new String(teste.getData()).trim());

//            executor.submit(this::propagateMessage);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDroneMessage(BufferedReader in) throws IOException {
        rabbitSocket = new Socket("localhost", 654321);
        while(true) {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    // formata mensagem
                    String teste = Arrays.stream(msg.split("\\|")).collect(Collectors.joining("\n"));
                    teste += "\n";
                    System.out.println(teste);


                    // envia a mensagem para clientes conectados
                    DatagramPacket dp = new DatagramPacket(
                            Arrays.copyOf(msg.getBytes(), 1024), 1024,
                            InetAddress.getByName("26.87.217.249"), redirectPort
                    );

                    toClient.send(dp);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            };
        }
    }

    private void propagateMessage(){
        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
        while(true){
            try{
                System.out.println("entrei");
                toClient.receive(dp);
                System.out.println("Recebi: " + new String(dp.getData()).trim());

                dp.setPort(redirectPort);
                toClient.send(dp);
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}