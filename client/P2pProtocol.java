/*
 *  P2P protocol class to process, parse a request msg and return a response
 */

public class P2pProtocol
{
    /*
     * status code initiliazation
     */
    private static HashMap<Integer,String> status;
    static{
        status = new HashMap<>();
        status.put(200,"OK");
        status.put(400,"Bad Request");
        status.put(404,"Not Found");
        status.put(505,"P2P-CI Version Not Support");
    }
    /*
     * Process and validate the msg, if it is a valid request, look up in the 
     * clist and form a response to it.
     *
     * what if there is header not in the protocol?
     * Is the order of the request matters?
     */
    public String process(ArrayList<String> msg,LinkedList<ClientInfo> clist, Lock r, Lock w)
    {
        Request request = parse(msg);
        Response response = Response();

        if( request.protv == "P2P-CI/1.0")
        {
            if( request.method == "ADD")
            {
                w.lock();
                try{
                    response = addRfc(clist,request);
                }finally{
                    w.unlock();
                }
            }
            else if( request.method == "LOOKUP" )
            {
                r.lock();
                try{
                    response = lookupRfc(clist,request);
                }finally{
                    r.unlock();
                }
            }
            else if( request.method == "LIST" )
            {
                r.lock();
                try{
                    response = listRfc(clist,request);
                }finally{
                    r.unlock();
                }
            }
            else
                return request.protv+" "+400+" "+status.get(400)+"\n";
            return format(resposne);
        }
        else
            return request.protv+" "+505+" "+status.get(505)+"\n";
    }

    /*
     *  Parse the request msg and form a request object, if this request is not valid, mark it.
     */
    public Request parse(ArrayList<String> msg)
    {
        Request request = new Request();
        if(msg.size() != 0 )
        {
            String [] method = msg.get(0).toUpperCase().split(" ");//whether multiple space is valid?whether there is space between RFC and number
            //process the msg and build the request object
            if( msg.length >= 1 )
                request.method = method[0];
            if( msg.length >= 2 )
                request.rfcNum = Integer.valueOf(method[2]);
            if( msg.length >= 4 )
                request.proctv = method[3];
        }
        int index = 1;
        while(msg.size() > i)
        {
            String [] header = msg.get(index).toUpperCase().split(" ");
            request.addHeader(header[0], header[1]);
        }
    }

    public String format(Response response)
    {
        StringBuilder sb = new StringBuilder(256);
        sb.append(response.procv+" "+response.statusCode+" "+status.get(response.statusCode)+"\n");
        for(String s : response.header)
            sb.append(s);
        return sb.toString();

    }

    public Response addRfc(LinkedList<ClientInfo> clist, Request request)
    {
        Response response = new Response();
        int i;
        for(i=0; i<clist.size(); i++)
        {
            ClientInfo c = clist.get(i);
            if( c.port == request.port )
            {
                if( c.rfcList.get(request.rfcNum) != null )
                {
                    System.out.println("RFC "+request.rfcNum+" is already in the record");
                    break;
                }
                else
                {
                    c.rfcList.put(request.rfcNum, request.rfcTitle);
                }
                response.statusCode = 200;
                return response;
            }
        }
        //no active user record of this host
        ClientInfo c = new ClientInfo(request.hostName, request.port);
        c.rfcList.put(request.rfcNum, request.rfcTtile);
        clist.add(c);
        response.statusCode = 200;
        return ;
    }

    public Response lookupRfc(LinkedList<ClientInfo> clist, Request request)
    {
        Response response = new Response();
        boolean iffound = false;
        for(int i=0; i<clist.size(); i++)
        {
            ClientInfo c = clist.get(i);
            if( c.rfcList.get(request.rfcNum) != null )
            {
                iffound = true;
                response.add(request.rfcNum, c.rfcList.get(request.rfcNum), c.hostname, c.port);
            }
        }
        if(!iffound)
            response.statusCode = 404;
        else
            response.statusCode = 200;
        return response;
    }
    public void listRfc(LinkedList<ClientInfo> clist, Request request)
    {
        Response response = new Response();
        for(int i=0; i<clist.size(); i++)
        {
            ClientInfo c = clist.get(i);
            Set<Integer> set = c.rfcList.keySet();
            for(Integer num : set )
                response.add(num, c.rfcList.get(num), c.hostname, c.port);
        }
        response.statusCode = 200;
        return response;
    }

    public static class Request
    {
        public String method;
        public int rfcNum;
        public String proctv;
        public HashMap<String, String> header;
        public Request()
        {
            header = new HashMap<>();
        }
    }
    public static class Response
    {
        public String proctv;
        public int statusCode;
        public ArrayList<String> header = ArrayList<>();
        public Response()
        {
            proctv = "P2P-CI";
        }
        public void add(int rfcNum, String rfcTitle, String hostName, int port)
        {
            header.add(rfcNum+" "+rfcTitle+" "+hostName+" "+port+"\n");
        }
    }
}
