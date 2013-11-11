import java.util.*;
import java.net.*;
import java.util.concurrent.locks;

public class Server extends Thread
{
    private final int portnum = 7734;
    private final LinkedList<ClientInfo> clist = new LinkedList<>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();


    public Server()
    {
    }

    @Override
    public void run()
    {
        boolean listening = true;
        //waiting for requst on a port
        try(SeverSocket socket = new SeverSocekt(portnum))
        {
            while(listening)
            {
                new ClientHandler(socket.accept(),clist,lock).start();
            }

        }catch(IOException ioe)
        {
            System.err.printf("Could not listen on port "+portnum);
        }
    }
    public static void main(String args[])
    {
        Server s = new Server();
        s.start();
    }

}
