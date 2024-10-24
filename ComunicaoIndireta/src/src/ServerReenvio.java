package src;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class ServerReenvio extends ServerBase{

    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;

    private Vector<DroneInfo> log;

    private InetSocketAddress nextAddr;

    // myAddr se refere sempre ao endereçode sí próprio
    // nextAddr é um endereço de grupo que vai receber as mensagens multicast enviadas daqui
    public ServerReenvio(InetSocketAddress myAddr, InetSocketAddress nextAddr) {
        super(myAddr);
        System.out.println("ServerReenvio criado com endereço " + myAddr);
        this.nextAddr = nextAddr;
    }

    @Override
    protected void init() {
        try {
            log = new Vector<>();
            serverSocket = new ServerSocket(myAddr.getPort());
            multicastSocket = new MulticastSocket(myAddr.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void handleMessage() throws IOException, ClassNotFoundException {

        Socket client = serverSocket.accept();
        System.out.println("Conexão estabelecida com cliente " + client.getLocalAddress() + ":" + client.getLocalPort());
        System.out.println("Enviando mensagens para " + nextAddr.getAddress() + ":" + nextAddr.getPort());
        ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());



        executor.submit(() ->{
            while(true) {
                try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                     ObjectOutputStream outputStream = new ObjectOutputStream(output);
                     ){
                    DroneInfo info = (DroneInfo) inputStream.readObject();

                    System.out.println("Recebido: drone " + info.id() + " " + info.timestamp() + " -> " + info);

                    log.add(info);

                    outputStream.writeObject(info);
                    outputStream.flush();

                    DatagramPacket dp = new DatagramPacket(output.toByteArray(), output.size(), nextAddr.getAddress(), nextAddr.getPort());
                    multicastSocket.send(dp);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    public static void main(String[] args) {
        new ServerReenvio(new InetSocketAddress("localhost", 8080), new InetSocketAddress("225.0.0.1", 12345));
    }
}