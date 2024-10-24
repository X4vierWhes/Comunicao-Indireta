package src;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class Server extends ServerBase{

    InetSocketAddress nextAddr;

    private MulticastSocket multicastSocket;

    public Server(InetSocketAddress myAddr, InetSocketAddress nextAddr) {
        super(myAddr);
        this.nextAddr = nextAddr;
    }

    @Override
    protected void init() {
        try {
            multicastSocket = new MulticastSocket(myAddr.getPort());
            NetworkInterface interfaceRede = NetworkInterface.getByName("wlp2s0");
            multicastSocket.joinGroup(myAddr, interfaceRede);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void handleMessage() throws Exception {

        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);

        try {
            multicastSocket.receive(dp);
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            DroneInfo info = (DroneInfo) inputStream.readObject();

            System.out.println("Recebido: " + info);

            dp = new DatagramPacket(dp.getData(), dp.getLength(), nextAddr.getAddress(), nextAddr.getPort());
            multicastSocket.send(dp);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        InetSocketAddress myAddr = new InetSocketAddress("225.0.0.1", 12345);
        InetSocketAddress nextAddr = new InetSocketAddress("225.0.0.1", 54321);
        new Server(myAddr, nextAddr);
    }
}
