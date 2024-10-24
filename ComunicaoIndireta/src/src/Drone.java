package src;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Drone{

    private Socket s;
    private ObjectOutputStream out;
    private ScheduledExecutorService executor;

    public Drone(InetSocketAddress addr, int id) {
        try {

            s = new Socket(InetAddress.getByName("localhost"), addr.getPort());

            out = new ObjectOutputStream(s.getOutputStream());

            executor = Executors.newScheduledThreadPool(1);

            executor.scheduleWithFixedDelay(() -> {
                        try {
                            out.writeObject(sendMsg(id));
                            out.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    0,
                    5,
                    TimeUnit.SECONDS
            );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DroneInfo sendMsg(int id){
        DroneInfo info = new DroneInfo(calcAtmospherePressure(), calcRadiation(), calcTemperature(), calcMoisture(), LocalDateTime.now(), id);
        System.out.print("enviando mensagem ");
        System.out.println(info);
        return info;
    }

    private static double calcTemperature(){
        return 10 + (Math.random() * 40);
    }

    private double calcRadiation(){return 200.0 + (1200.0 - 200.0) * Math.random();}

    private double calcAtmospherePressure(){return 950.0 + (1050.0 - 950.0) * Math.random();}

    private double calcMoisture(){return 0.0 + (100.0 - 0.0) * Math.random();}

    public static void main(String[] args) {
        new Drone(new InetSocketAddress("localhost", 8080), new Random().nextInt(Integer.MAX_VALUE));
    }

}