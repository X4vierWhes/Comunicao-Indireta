package src;

import java.net.InetSocketAddress;
import java.util.Random;

public class MainFechado {

    public static class mainDroneNorte{
        public static void main (String[]args){
            new Drone(new InetSocketAddress("localhost", 12345),
                    new Random().nextInt(Integer.MAX_VALUE)
            );
        }
    }

    public static class mainDroneSul{
        public static void main (String[]args){
            new Drone(new InetSocketAddress("localhost", 54321),
                    new Random().nextInt(Integer.MAX_VALUE)
            );
        }
    }

    public static class mainServerNorte{
        public static void main(String[] args) {
            new ServerReenvio(
                    new InetSocketAddress("localhost", 12345),
                    new InetSocketAddress("225.0.0.1", 12345)
            );
        }
    }

    public static class mainServerSul{
        public static void main(String[] args) {
            new ServerReenvio(
                    new InetSocketAddress("localhost", 54321),
                    new InetSocketAddress("225.0.0.1", 54321)
            );
        }
    }

    public static class mainServer3{
        public static void main(String[] args) {
            new Server(
                    new InetSocketAddress("225.0.0.1", 12345),
                    new InetSocketAddress("225.0.0.1", 8080)
            );
        }
    }

    public static class mainServer4{
        public static void main(String[] args) {
            new Server(
                    new InetSocketAddress("225.0.0.1", 54321),
                    new InetSocketAddress("225.0.0.1", 8081)
            );
        }
    }

}
