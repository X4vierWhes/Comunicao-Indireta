import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainAberto {
    public static class Drone1{
        public static void main(String[] args) {
            new Drone();
        }
    }

    public static class Server1{
        public static void main(String[] args) {
            try {
                new Server(InetAddress.getByName("localhost"), InetAddress.getByName("225.0.0.1"), 12345, InetAddress.getByName("localhost"));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Client1{
        public static void main(String[] args) {
            new Client(5555);
        }
    }
    public static class Client2{
        public static void main(String[] args) {
            new Client(5556);
        }
    }
    public static class Client3{
        public static void main(String[] args) {
            new Client(5557);
        }
    }


}
