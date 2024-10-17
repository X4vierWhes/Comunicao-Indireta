package src;

public class MainFechado {

    public static class mainDroneNorte{
        public static void main (String[]args){
            new Drone(12345);
        }
    }

    public static class mainDroneSul{
        public static void main (String[]args){
            new Drone(54321);
        }
    }

    public static class mainServerNorte{
        public static void main (String[]args){
            new Server(12345, 5556);
        }
    }

    public static class mainServerSul{
        public static void main (String[]args){
            new Server(54321, 6555);
        }
    }

    public static class mainServer3{
        public static void main (String[]args){
            new Server(5556, 5555);
        }
    }

    public static class mainServer4{
        public static void main (String[]args){
            new Server(6555, 5555);
        }
    }

}
