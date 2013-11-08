/*
 *Class to handle each client's request
 *
 *
 */
import java.net.*;
public class ClientHandler extends Thread
{
    private Socket socket;
    private LinkedList<Client> clist;
    public ClientHandler(Socket socket,LinkedList<Client> list)
    {
        this.socket = socket;
        this.list = list;
    }
    @Override
    public void run()
    {
        try(BufferedReader in = new BufferedReader();
            PrintWriter out = new PrintWriter(socket.getOutputStream());)
        {
            String inputLine;
            StringBuilder msg = new StringBuilder();
            P2pProtocol p = new P2pProtocol();
            while( (inputLine = in.readLine()) != null )
            {
                msg.append(inputLine);
            }
            String response = p.process(msg.toString());
            out.print(response);
        }
    }
}
