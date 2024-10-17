package src;

public class MainAberto {
    public static class mainDrone{
        public static void main (String[]args){
           new Drone(12345);
        }
    }

    public static class mainServer{
        public static void main (String[]args){
            new Server(12345, 5555);
        }
    }
}
