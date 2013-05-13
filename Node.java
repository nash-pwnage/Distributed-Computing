import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.String;

public class Node {

	String configFile;
	int id, listenPort, distinguishedNode, numberOfNodes;
	String hostname;
	ArrayList<Integer> edges;
	HashMap<Integer, Socket> directoryMap;
	HashMap<String, Integer> senders;
	ArrayList<Integer> adjacents; // To store adjacent node list
	ArrayList<String> hostnames; // to store the adjacent info
	int ports[], ids[]; // to store the adjacent info
	int rec[];
	String[] rHostnames;
	String[] sHostnames;
	int send[];
	int sendports[];
	LocalState localstate;

	public Node(int port, String conf) {
		this.listenPort = port;
		this.configFile = conf;
		this.id = -1;
		this.localstate = new LocalState();
		
		try {
			this.hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			System.out.println("Host Name Not Found :(");
		}

	}

	public void configure() throws FileNotFoundException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(configFile));
			StringTokenizer st = new StringTokenizer(br.readLine());
			String token = st.nextToken();
			while (token.startsWith("#")) {
				st = new StringTokenizer(br.readLine());
				token = st.nextToken();
			}

			int numberOfNodes = Integer.parseInt(token);
			ids = new int[numberOfNodes];
			ports = new int[numberOfNodes];
			hostnames = new ArrayList<String>(numberOfNodes);
			for (int i = 0; i < numberOfNodes; i++) {
				st = new StringTokenizer(br.readLine());
				ids[i] = Integer.parseInt(st.nextToken());
				hostnames.add(st.nextToken());
				ports[i] = Integer.parseInt(st.nextToken());
				if (hostname.equals(hostnames.get(i)) && listenPort == ports[i]) {
					id = ids[i];
					 localstate.id = id;
				}
				System.out.println("added node id = " + ids[i] + " hostname = "
						+ hostnames.get(i) + " port = " + ports[i]);
			}
			System.out.println(" added " + numberOfNodes + " nodes");
		
			st = new StringTokenizer(br.readLine(), "( )");
			token = st.nextToken();
			edges = new ArrayList<Integer>();
			while (!token.startsWith("#")) {
				edges.add(Integer.parseInt(token));
				token = st.nextToken();
			}

			st = new StringTokenizer(br.readLine());
			distinguishedNode = Integer.parseInt(st.nextToken());
			System.out.println(edges);
			for (int k = 0; k < edges.size(); k = k + 2) {
				System.out.println("Edges : " + edges.get(k) + " - "
						+ edges.get(k + 1));
			}
			System.out.println("Distinguished node = " + distinguishedNode);
			localstate.distinguishedNodeId = distinguishedNode;
			System.out.println("Self id = " + id);
			
			if(localstate.id == distinguishedNode) {
				localstate.isDistinguishable = 1;
			} else {
				localstate.isDistinguishable = 0;
			}

			// ADDING ADjacency .... gangnam style
			// int c2=0;
			adjacents = new ArrayList<Integer>();

			int size = edges.size();
			for (int c1 = 0; c1 < size; c1++) {
				if (edges.get(c1) == id) {
					if (c1 % 2 == 0) {
						adjacents.add(edges.get(c1 + 1));
					}

					// else {}
					if (c1 % 2 == 1) {
						adjacents.add(edges.get(c1 - 1));
					}
					// else {}
				}
			}

			//Update Degree
			localstate.degree = adjacents.size();
			localstate.maxDegree = adjacents.size();
			localstate.minDegree = adjacents.size();

			// System.out.println("\n Adjacent");
			// for(int test=0;test<adjacents.size();test++)
			// { //System.out.println(adjacents.get(test));
			// }

			int recsize = 0, sendsize = 0;

			
			  for(int test=0;test<adjacents.size();test++) {
			  if(id>adjacents.get(test)) {
			  System.out.println("Node "+id+" will send to "+
			  adjacents.get(test)); sendsize++; } else {
			  System.out.println("Node "+id+" will accept from "+
			  adjacents.get(test)); recsize++; } }
			 
	  
			  
			 System.out.println("Sizes:"+recsize+":"+sendsize);
			// Seperating the Adj list acc to Send and Receiving ID Arrays!
			
			try {
				if (recsize > 0) {
					rec = new int[recsize];
					for (int i = 0; i < recsize; i++) {
						rec[i] = -1;
					}
					rHostnames = new String[recsize];
				}

				if (sendsize > 0) {
					sHostnames = new String[sendsize];
					send = new int[sendsize];
					for (int i = 0; i < sendsize; i++) {
						send[i] = -1;
					}
					sendports = new int[sendsize];
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			int s1 = 0;
			int r1 = 0;
			for (int itr = 0; itr < adjacents.size(); itr++) {
				if (id > adjacents.get(itr)) {
					if (sendsize!=0) {
					send[s1] = adjacents.get(itr);
					sHostnames[s1] = hostnames.get(adjacents.get(itr));
					System.out.println("Got Hostnames!!");
					int k = 0;
					int temp = 0;
					// System.out.println("Adjacent size="+adjacents.size());
					while (k < ids.length) {
						if (adjacents.get(itr) == ids[k]) {
							temp = ports[k];
							System.out.println("port[k] to connect to="
									+ ports[k]);
						}
						k++;
					}
				
					sendports[s1] = temp;
					System.out.println("Send to node no:  " + send[s1]
							+ " hostname= " + sHostnames[s1] + " at port: "
							+ sendports[s1]);
					s1++;
					}
				} else {
					if (recsize!=0) {
					rec[r1] = adjacents.get(itr);
					rHostnames[r1] = hostnames.get(adjacents.get(itr));
					System.out.println("Recv fm node no:  " + rec[r1]
							+ " hostname= " + rHostnames[r1]);
					r1++;
				}
			}
			}
		} catch (NumberFormatException n) {
			System.out.println("Cannot Read Config File : expected number");
			n.printStackTrace();
		} catch (FileNotFoundException fne) {
			System.out.println("Cannot Read Config File : file not found");
			fne.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	public static void main(String argv[]) {
		
		if (argv.length != 2) {
			System.out.println("Insufficient arguments");
			return;
		}
		
		String file = argv[0];
		int port = Integer.parseInt(argv[1]);
		Node n = new Node(port, file);
		try {
			n.configure();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SocketAcceptor sAcceptor = null;
		
		// if receive list not empty start socket acceptor thread
		if (n.rec!=null) {
			sAcceptor = new SocketAcceptor(n.listenPort,
					n.rHostnames, n.rec);
			System.out
					.println("Socket acceptor Initialized with listening port= "
							+ n.listenPort);
			sAcceptor.start();
			System.out.println("Acceptor thread started");
		}
		
		// Sending out requests....
	//	System.out.println("send: "+n.send.length+" shostnames: "+n.sHostnames.length+" sports "+n.sendports.length);
		
		
		//display info of nodes where you send requests
		HashMap<Integer, Socket> sendersockets = new HashMap<Integer, Socket>();
		
		if(n.send!=null)
	
	{	
	
		for(int i=0 ;i< n.send.length;i++) {
			System.out.println("Connect to " + n.sHostnames[i] + " : "
					+ n.sendports[i]);
		}
	
	

		//wait for user input before send
		Scanner reader = new Scanner(System.in);
		System.out.println("Press Enter Before sending out...");
		int a = reader.nextInt();

		
		int senderSuccess = 0;
		int k = 0;
		

		//for( int i: n.send)
		
			//System.out.println("send : " + i);
		
		
		if (n.send[0] != -1) 
		while (senderSuccess < n.send.length) {
				try {
					System.out.println("Port no:"+n.sendports[k]);
					Socket s = new Socket(n.sHostnames[k], n.sendports[k]);
					System.out.println("New socket connection to "
							+ n.sHostnames[k] + " At port " + n.sendports[k]);
					senderSuccess++;
					sendersockets.put(n.send[k],s);
					k++;
					} catch (IOException e) {
					
						System.out.println("Connection to " + n.sHostnames[k]
							+ ": " + n.sendports[k]);
						e.printStackTrace();
					}
				
		}
		}
		
		HashMap<Integer, Socket> directoryMap = new HashMap<Integer,Socket>();
		HashMap<Integer, ObjectOutputStream> outputStreams = new HashMap<Integer, ObjectOutputStream>();
		HashMap<Integer, ObjectInputStream> inputStreams = new HashMap<Integer, ObjectInputStream>();
		directoryMap.putAll(sendersockets);
		
		try {
			if(sAcceptor!=null){
				sAcceptor.join();
				directoryMap.putAll(sAcceptor.getSockets());}
			else
				System.out.println("not waiting for acceptor thread as tere exists none.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR: Interrupted ehile waiting for server to return to main");
		}
		
		
		
		for(int i : directoryMap.keySet())
		{
			//System.out.println("output stream being extracted with client with id " + i);
			try {
				
				ObjectOutputStream out = new ObjectOutputStream(directoryMap.get(i).getOutputStream());
				System.out.println("output stream being successfully established with "+i);
				outputStreams.put(i, out);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("could not establish output streams with id " + i);
			}
		}
		
		for(int i : directoryMap.keySet())
		{
			//System.out.println("input stream being extracted with client with id " + i);
			try {
				ObjectInputStream in = new ObjectInputStream(directoryMap.get(i).getInputStream());
				System.out.println("input stream being successfully established with "+i);
				inputStreams.put(i, in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("could not establish input streams with id " + i);
			}
		}
		
		
		System.out.println("Inputstream Contents :"+inputStreams.values().toString());
		//HashMap<Integer,Listener> listenerMap= new HashMap<Integer,Listener>();
		
		
		// for each inputstream add a listener
		for(int i : inputStreams.keySet())
		{
			Listener l=new Listener(n.localstate, i, inputStreams.get(i), outputStreams);
			l.start();
			System.out.println("Adding a listener at id "+n.id+" for id "+i+" Input stream = "+inputStreams.get(i).toString());
		}
		
		System.out.println(n.localstate.toString());
		//start a talker to send messages
		Talker t = new Talker(n.localstate, outputStreams, inputStreams);
		t.start();
		
		
		
		while(true);
  }

}
