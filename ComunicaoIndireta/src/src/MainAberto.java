package src;

import java.net.InetSocketAddress;
import java.util.Random;

public class MainAberto {
    public static class mainDrone{
        public static void main (String[]args){
           new Drone(new InetSocketAddress("localhost", 12345), new Random().nextInt(Integer.MAX_VALUE));
        }
    }

    public static class mainServer{
        public static void main (String[]args){
            new Server(
                    new InetSocketAddress("225.0.0.1", 12345),
                    new InetSocketAddress("225.0.0.1", 54321)
            );
        }
    }
}
