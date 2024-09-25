import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    MulticastSocket socket;

    ExecutorService executor;

    public Client(){
        try {
            socket = new MulticastSocket(12345);
            InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("130.0.0.1"), 5555);
            NetworkInterface interfaceRede = NetworkInterface.getByName("en0");
            socket.joinGroup(grupo, interfaceRede);


            executor = Executors.newCachedThreadPool();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receberDados(){
        while (true){
            DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(dp);

                System.out.println("Recebi: " + new String(dp.getData()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
