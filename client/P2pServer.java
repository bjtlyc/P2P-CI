import java.util.*;
import java.net.*;
import java.util.concurrent.locks;

public class P2pServer extends Thread
{
    private final int p2pPort;
    private final String hostName; 
    private boolean listening = true;

    public P2pServer(String hostName, int p2pPort)
    {
        this.hostName = hostName;
        this.p2pPort = p2pPort;
    }

    public void exit()
    {
        this.listening = false;
    }

    @Override
    public void run()
    {
        //waiting for requst on a port
        try(SeverSocket serversocket = new SeverSocekt(p2pPort))
        {
            while(listening)
            {
                Socket socket = serversocket.accept();
                try(BufferedReader in = new BufferedReader();
                    PrintWriter out = new PrintWriter(socket.getOutputStream());)
                {
                    String inputLine;
                    ArrayList<String> msg = new ArrayList<String>();
                    P2pProtocol p = new P2pProtocol();
                    while( (inputLine = in.readLine()) != null )
                    {
                        msg.add(inputLine);
                    }
            
                    String response = p.process(msg,clist,r,w);
                    out.print(response);

                    socket.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }catch(IOException ioe)
        {
            System.err.printf("Could not listen on port "+portnum);
        }
    }
}
