import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: adicionar log de servidor
// TODO: enviar dados para endereço de multicast

public class Server {
    private DatagramSocket droneCon;
    private MulticastSocket group;
    private ExecutorService pool;

    private InetAddress clientAddr;

    public Server(InetAddress addr, InetAddress groupAddr, int port) {
        try {
            droneCon = new DatagramSocket(port);

            group = new MulticastSocket(port);
            InetSocketAddress grupo = new InetSocketAddress(groupAddr, port);
            NetworkInterface interfaceRede = NetworkInterface.getByName("en0");
            group.joinGroup(grupo, interfaceRede);

            pool = Executors.newCachedThreadPool();

            pool.execute(this::handleDroneConnection);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

private void handleDroneConnection() {
    DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);

    while (true) {
        try {
            droneCon.receive(dp);

            // guardar num log

            // reenviar diretamente para próximo grupo
            pool.submit(() -> {
                try {
                    dp.setAddress(clientAddr);
                    group.send(dp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Recebi: " + new String(dp.getData()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try(ServerSocket socketServidor = new ServerSocket(12345)){
            System.out.println("Servidor rodando na porta: " + socketServidor.getLocalPort());

            while(true){
                Socket socketClient = socketServidor.accept();
                System.out.println("Cliente conectado "
                        + socketServidor.getInetAddress());
                pool.execute(() -> {
                    try(BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(socketClient.getInputStream()));
                        PrintWriter saida =
                                new PrintWriter(socketClient.getOutputStream(), true)) {

                        saida.println("Bem vindo ao servidor...");

                        String msgCliente;
                        while((msgCliente = entrada.readLine()) != null){

                            System.out.println(msgCliente);
                            saida.println("Echo: " + msgCliente);

                        }

                    }catch (IOException e){
                        e.getMessage();
                    }finally {
                        try {
                            socketClient.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }catch (IOException io){
            io.printStackTrace();
        }finally {
            pool.shutdownNow();
        }

    }
}
