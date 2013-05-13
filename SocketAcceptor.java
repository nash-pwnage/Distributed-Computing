import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class SocketAcceptor extends Thread {
int id[], port[];
String hostName[];
//Socket socket[];
int lport;
HashMap<Integer, Socket> sockets;
HashMap<String,Integer> rec;



public HashMap<Integer, Socket> getSockets() {
	return sockets;
}

public void setSockets(HashMap<Integer, Socket> sockets) {
	this.sockets = sockets;
}

public SocketAcceptor(int listenPort, String[] rhostnames, int rid[])
{
	sockets= new HashMap<Integer, Socket>();
	id=rid;
	hostName=rhostnames;
	lport=listenPort;
	rec=new HashMap<String,Integer>();
	int i=0;
	//if(rhostnames[0])
	for(String hostname: rhostnames)
	{
		//String rhostname=rhostnames[i];
		int id= rid[i];i++;
		rec.put(hostname, id);
		//System.out.println("Accept from "+hostname+" : "+id);
	}
}

public void run()
{
	ServerSocket ss = null;
	try {
		ss = new ServerSocket(lport);
	//	System.out.println("Address "+ss.getInetAddress()+"local addr= "+ss.getLocalSocketAddress());
		System.out.println("SocketAcceptor New Serversocket extablished on port no: "+lport);
		System.out.println("SocketAcceptor Listening....");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int countAcceptedHosts=0;
	while (countAcceptedHosts < id.length)
	{
		 
		try {
			Socket s = ss.accept();
			countAcceptedHosts++;
		String recd_hostname=s.getInetAddress().getHostName();
		System.out.println("Connected successfully to "+recd_hostname);
		if (rec.get(recd_hostname)!=null)
		{	int id= rec.get(recd_hostname);
			sockets.put(id,s);
			System.out.println("Added a new entry from "+id+" to reciever hashmap 2");
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception Ocurred");
			e.printStackTrace();
		}
			
	}
}

}
