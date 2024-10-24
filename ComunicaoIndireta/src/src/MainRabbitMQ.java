package src;

import java.net.InetSocketAddress;
import java.util.Random;

public class MainRabbitMQ {
    public static class mainDrone{
        public static void main (String[]args){
            new Drone(new InetSocketAddress("localhost", 5555), new Random().nextInt(Integer.MAX_VALUE));
        }
    }

    public static class mainServer{
        public static void main (String[]args){
            new ServerReenvio(
                    new InetSocketAddress("localhost", 5555),
                    new InetSocketAddress("225.0.0.1", 12345));
        }
    }

    public static class mainServer2{
        public static void main (String[]args) {
            new ServerRabbitMQ(
                    new InetSocketAddress("225.0.0.1", 12345),
                    "localhost",
                    15672,
                    "server1"
                    );
        }
    }

    public static class mainServer3{
        public static void main (String[]args) {
            new ServerRabbitMQ(
                    new InetSocketAddress("225.0.0.1", 12345),
                    "localhost",
                    15672,
                    "server2"
            );
        }
    }

}
