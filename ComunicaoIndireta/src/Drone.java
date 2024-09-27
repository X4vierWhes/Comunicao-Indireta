import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Drone{

    private Socket s;
    private BufferedWriter out;
    private ScheduledExecutorService executor;

    public Drone(int port) {
        try {

            s = new Socket(InetAddress.getByName("localhost"), port);

            out = new BufferedWriter(new PrintWriter(s.getOutputStream()));

            executor = Executors.newScheduledThreadPool(1);

            executor.scheduleWithFixedDelay(() -> {
                        try {
                            out.write(sendMsg(1));
                            out.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    0,
                    3,
                    TimeUnit.SECONDS
            );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendMsg(int id){
        System.out.println("enviando mensagem");
        return String.format("Pressão Atmosferica: %.2f|Radiação: %.2f|Temperatura: %.2f|Umidade: %.2f|ID: %d\n",
            calcAtmospherePressure(), calcRadiation(), calcTemperature(), calcMoisture(), id);
    }

    private static double calcTemperature(){
        return 10 + (Math.random() * 40);
    }

    private double calcRadiation(){return 200.0 + (1200.0 - 200.0) * Math.random();}

    private double calcAtmospherePressure(){return 950.0 + (1050.0 - 950.0) * Math.random();}

    private double calcMoisture(){return 0.0 + (100.0 - 0.0) * Math.random();}


}