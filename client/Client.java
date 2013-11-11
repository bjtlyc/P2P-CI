import java.util.*;
import java.net.*;
import java.io.*;

public class Client extends
{
    private final int serverPort = 7734;
    private final String procv = "P2P-CI/1.0"; 
    private final String serverIp = "127.0.0.1";
    private int p2pPort;
    private String hostName; 
    private P2pServer p2pserver;

    /*
     * Client constructor, get the host name and p2p port number, and start a thread listen on the port num
     */
    public Client()
    {
        try(Scanner s = new Scanner(System.in))
        {
            String input;
            System.out.println("Please enter the name of the server");
            this.hostName = s.nextLine();
            System.out.println("Please enter the p2p port number");
            this.p2pPort = c.nextInt();
            p2pserver = new P2pServer(hostName, p2pPort).start();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void addRfc(int rfcNum, String rfcTitle)
    {
        try(Socket socket = new Socekt(serverIp,ServerPort);
            PrintfWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
            StringBuilder msg =  new StringBuilder(256);
            msg.append("ADD RFC ").appned(rfcNum).append(" ").append(procv).append("\n");
            msg.append("Host: ").appned(hostName).append("\n");
            msg.append("Port: ").append(p2pPort).append("\n");
            msg.append("Title: ").append(rfcTitle).append("\n");
            msg.append("END\n");
            out.print(mgs.toString());

            String fromServer;
            while((fromServer = in.readLine()) != null && fromServer != "END")
            {
                System.out.println(fromServer);
            }
        }catch(IOException ioe)
        {
            ioe.printStackTrace(); 
        }

    }
    public ArrayList<String> lookupRfc(int rfcNum, String rfcTitle)
    {
        ArrayList<String> response = new ArrayList<>();
        try(Socket socket = new Socekt(serverIp,ServerPort);
            PrintfWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
            StringBuilder msg =  new StringBuilder(256);
            msg.append("LOOKUP RFC ").appned(rfcNum).append(" ").append(procv).append("\n");
            msg.append("Host: ").appned(hostName).append("\n");
            msg.append("Port: ").append(p2pPort).append("\n");
            msg.append("Title: ").append(rfcTitle).append("\n");
            msg.append("END\n");
            out.print(mgs.toString());

            String fromServer;
            while((fromServer = in.readLine()) != null && fromServer != "END")
            {
                System.out.println(fromServer);
                response.add(fromServer);
            }
        }catch(IOException ioe)
        {
            ioe.printStackTrace(); 
        }
        return response;
    }

    public void listRfc()
    {
        try(Socket socket = new Socekt(serverIp,ServerPort);
            PrintfWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
            StringBuilder msg =  new StringBuilder(256);
            msg.append("LIST ALL RFC ").appned(rfcNum).append(" ").append(procv).append("\n");
            msg.append("Host: ").appned(hostName).append("\n");
            msg.append("Port: ").append(p2pPort).append("\n");
            msg.append("END\n");
            out.print(mgs.toString());

            String fromServer;
            while((fromServer = in.readLine()) != null && fromServer != "END")
            {
                System.out.println(fromServer);
            }
        }catch(IOException ioe)
        {
            ioe.printStackTrace(); 
        }
    }
    public void getRfc()
    {
        ArrayList<String> response = lookupRfc(rfcNum, null);

        P2pProtocol p = new P2pProtocol();
        int port = p.process(response);

        try(Socket socket = new Socekt(serverIp, port);
            PrintfWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
            StringBuilder msg =  new StringBuilder(256);
            msg.append("GET RFC ").appned(rfcNum).append(" ").append(procv).append("\n");
            msg.append("Host: ").appned(hostName).append("\n");
            msg.append("OS: ").append(System.getProperty("os.name")).append("\n");
            msg.append("END\n");
            out.print(mgs.toString());

            String fromServer;
            while((fromServer = in.readLine()) != null && fromServer != "END")
            {
                System.out.println(fromServer);
            }
        }catch(IOException ioe)
        {
            ioe.printStackTrace(); 
        }
    }
    public static void main(String args[])
    {
        Client c = new Client();
        try(BufferedReader cin = new BufferedReader(new InputStreamReader(System.in)))
        {
            String input;
            while( (input = cin.getLine()).toUpperCase() != "END" )
            {
                if(input == "ADD")
                {
                    System.out.println("Please enter the rfc number");
                    int rfcNum = Integer.valueOf(cin.getLine());
                    System.out.println("Please enter the title of the rfc file");
                    String rfcTitle = Integer.valueOf(cin.getLine());
                    c.addRfc(rfcNum, rfcTitle);
                }
                else if(input == "LOOKUP")
                {
                    System.out.println("Please enter the rfc number");
                    int rfcNum = Integer.valueOf(cin.getLine());
                    System.out.println("Please enter the title of the rfc file");
                    String rfcTitle = Integer.valueOf(cin.getLine());
                    c.lookupRfc(rfcNum, rfcTitle);
                }
                else if(input == "LIST")
                {
                    c.listRfc();
                }
                else if(input == "GET")
                {
                    System.out.println("Please enter the rfc number");
                    int rfcNum = Integer.valueOf(cin.getLine());
                    c.getRfc(rfcNum);
                }
                else
                    System.out.println("Invalid command");
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        p2pserver.exit();
    }
    public static class Request
    {
        public String method;
        public int rfcNum;
        public String proctv;
        public int port;
        public String rfcTitle;
        public String hostName;
    }
}
