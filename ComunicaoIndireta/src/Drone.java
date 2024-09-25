import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Main

    public static void main(String[] args) {

        try(Socket socket =
                    new Socket("localhost", 12345);
            PrintWriter saida =
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedReader console = new BufferedReader(
                    new InputStreamReader(System.in))) {

            System.out.println("Conectador ao servidor: " +
                    entrada.readLine());

            String msg;
            while((msg = console.readLine()) != null){
                saida.println(msg);
                //System.out.println("Mensagem do servidor " + msg);
            }

        }catch (IOException e){
            e.getMessage();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Coleta de dados
    private double calcTemperature(){
        return 10 + (Math.random() * 40);
    }

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
