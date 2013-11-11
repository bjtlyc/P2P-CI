/*
 *  Class to handle each client's request, read the request msg, pass to P2pProtocol class to
 *  parse it, and form a response then write back to the client. Finally close the socket created
 *  by the client.
 *
 *
 */
import java.net.*;
public class ClientHandler extends Thread
{
    private Socket socket;
    private LinkedList<ClientInfo> clist;
    private Lock r;
    private Lock l;

    public ClientHandler(Socket socket,LinkedList<ClientInfo> list,Lock r, Lock w)
    {
        this.socket = socket;
        this.list = list;
        this.r = r;
        this.w = w;
    }
    @Override
    public void run()
    {
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
}
