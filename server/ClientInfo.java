/*
* Class to store client information: the host name, host port number, 
* and a hashmap that stores the RFC number and RFC title
* */
public class ClientInfo 
{
        private String hostName;
        private int port;
        private HashMap<Integer,String> rfcList;
        public ClientInfo(String hostName, int port)
        {
            this.hostName = hostName;
            this.port = port;
            this.rfcList = new HashMap<>();
        }
}
