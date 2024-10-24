package src;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract public class ServerBase {

    protected ExecutorService executor;
    protected InetSocketAddress myAddr;


    public ServerBase(InetSocketAddress myAddr) {
        this.myAddr = myAddr;
        this.executor = Executors.newCachedThreadPool();
        init();
        executor.submit(this::handle);


    }

    abstract protected void init();

    private void handle(){
        while (true)
        {
            try {
                handleMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    abstract protected void handleMessage() throws Exception;

}
