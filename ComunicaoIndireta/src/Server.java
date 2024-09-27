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
    private ServerSocket droneCon;
    private MulticastSocket group;
    private ExecutorService pool;

    private InetAddress clientAddr;

    public Server(InetAddress addr, InetAddress groupAddr, int port, InetAddress clientAddr) {
        try {
            droneCon = new ServerSocket(12345);
            System.out.println("Servidor rodando na porta: " + droneCon.getLocalPort());

//            group = new MulticastSocket(new InetSocketAddress(addr, port));
//
//            InetSocketAddress grupo = new InetSocketAddress(groupAddr, port);
//            NetworkInterface interfaceRede = NetworkInterface.getByName("en0");
//            group.joinGroup(grupo, interfaceRede);

//            this.clientAddr = clientAddr;

            pool = Executors.newCachedThreadPool();

            pool.execute(this::handleDroneConnection);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

private void handleDroneConnection() {

    while (true) {
        try {
            Socket socketClient = droneCon.accept();
            System.out.println("Cliente conectado "
                    + droneCon.getInetAddress());

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socketClient.getInputStream()));

            // guardar num log


            // reenviar diretamente para próximo grupo
            pool.submit(() -> {
                while (socketClient.isConnected()) {
                    try {
                        String msg = entrada.readLine();
                        System.out.println(msg);
                        DatagramPacket dp = new DatagramPacket(
                                msg.getBytes(), 1024,
                                clientAddr, 12345
                        );

//                        group.send(dp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

    public static class cu{
        public static void main(String[] args) {
            try {
                new Server(InetAddress.getByName("localhost"), InetAddress.getByName("227.0.0.1"), 12345, InetAddress.getByName("localhost"));
            } catch (UnknownHostException e) {
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
