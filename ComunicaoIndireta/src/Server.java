import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try(ServerSocket socketServidor = new ServerSocket(12345)){
            System.out.println("Servidor rodando na porta: " + socketServidor.getLocalPort());

            while(true){
                Socket socketClient = socketServidor.accept();
                System.out.println("Cliente conectado "
                        + socketServidor.getInetAddress());
                pool.execute(() -> {
                    try(BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(socketClient.getInputStream()));
                        PrintWriter saida =
                                new PrintWriter(socketClient.getOutputStream(), true)) {

                        saida.println("Bem vindo ao servidor...");

                        String msgCliente;
                        while((msgCliente = entrada.readLine()) != null){

                            System.out.println("Mensagem do cliente " + msgCliente);
                            saida.println("Echo: " + msgCliente);

                        }

                    }catch (IOException e){
                        e.getMessage();
                    }finally {
                        try {
                            socketClient.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }catch (IOException io){
            io.printStackTrace();
        }finally {
            pool.shutdownNow();
        }

    }
}
