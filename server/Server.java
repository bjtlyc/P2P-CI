import java.util.*;
import java.net.*;
public class Server extends Thread
{
    private int portnum = 7734;
    private LinkedList<Client> clist;


    @Override
    public void run()
    {
        boolean listening = true;
        //waiting for requst on a port
        try(SeverSocket socket = new SeverSocekt(portnum))
        {
            while(listening)
            {
                new ClientHandler(socket.accept()).start();
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

    /*
     * Class to store client information: the host name, host port number, 
     * and a hashmap that stores the RFC number and RFC title
     */
    public static class Client
    {
        private String hostName;
        private int portnum;
        private HashMap<Integer,String> rfclist;
        public Client(String hostName, int portnum)
        {
            this.hostName = hostName;
            this.portnum = portnum;
            this.rfclist = new HashMap<>();
        }
    }
}
