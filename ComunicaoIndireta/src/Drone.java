import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Drone {
    //Precisa de um Socket que envia os dados coletados ao servidor, que ainda nao sei direito como sera implementado.
    private double AtmosphericPressure; //Pressão Atmosferica
    private double Radiation; //Radiação Solar
    private double Temperature; //Temperatura
    private double Moisture; //Umidade
    private int id; //Id do drone
    private String local;
    private MulticastSocket multicastSocket = null;
    private InetAddress multicastIp = null;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Construtores
    public Drone(){}
    public Drone(double atmosphericPressure, double radiation, double temperature, double moisture){
        this.AtmosphericPressure = atmosphericPressure;
        this.Radiation = radiation;
        this.Temperature = temperature;
        this.Moisture = moisture;
        Random random = new Random();
        this.id = random.nextInt()%2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Main

    public static void main(String[] args) {
        ScheduledExecutorService executor = null;
            try (Socket socket =
                         new Socket("localhost", 12345);
                 PrintWriter saida =
                         new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedReader console = new BufferedReader(
                         new InputStreamReader(System.in))) {

//                System.out.println("Conectador ao servidor: " +
//                        entrada.readLine());

                    executor = Executors.newScheduledThreadPool(1);
                    Random ran = new Random();
                    int id = ran.nextInt();

                    executor.scheduleAtFixedRate(
                            () -> {
                                saida.println(sendMsg(id));
                                saida.println("teste\n");
                            }
                            , 0, 3, TimeUnit.SECONDS);
                    //executor.shutdown();

                    //saida.println(sendMsg());



            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Coleta de dados
    private static String sendMsg(int id){
        System.out.println("cuzin");
        return STR."Pressão Atmosferica: \{calcAtmospherePressure()}\nRadiação: \{calcRadiation()}\nTemperatura: \{calcTemperature()}\nUmidade: \{calcMoisture()}\n ID: \{id}\n";
    }

    private static double calcTemperature(){
        return 10 + (Math.random() * 40);
    }
    
    private static double calcRadiation(){return 200.0 + (1200.0 - 200.0) * Math.random();}
    
    private static double calcAtmospherePressure(){return 950.0 + (1050.0 - 950.0) * Math.random();}

    private static double calcMoisture(){return 0.0 + (100.0 - 0.0) * Math.random();}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Getters e Setters
    public double getAtmosphericPressure() {
        return AtmosphericPressure;
    }

    public void setAtmosphericPressure(double atmosphericPressure) {
        AtmosphericPressure = atmosphericPressure;
    }

    public double getRadiation() {
        return Radiation;
    }

    public void setRadiation(double radiation) {
        Radiation = radiation;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getMoisture() {
        return Moisture;
    }

    public void setMoisture(double moisture) {
        Moisture = moisture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
