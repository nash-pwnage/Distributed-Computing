import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;


public class Talker extends Thread{
	
	private int selfId;
	HashMap<Integer,ObjectOutputStream> outs;
	HashMap<Integer,ObjectInputStream> ins;
	LocalState localstate;
	// GlobalStatus global;

	 public Talker(LocalState localstate, HashMap<Integer, ObjectOutputStream> outs,
			HashMap<Integer, ObjectInputStream> ins) {
		super();
		this.selfId = localstate.id;
		this.outs = outs;
		this.ins = ins;
		this.localstate = localstate;
		
	}

	public int getSelfId() {
		return selfId;
	}

	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}

	public HashMap<Integer, ObjectOutputStream> getOuts() {
		return outs;
	}

	public void setOuts(HashMap<Integer, ObjectOutputStream> outs) {
		this.outs = outs;
	}

	public HashMap<Integer, ObjectInputStream> getIns() {
		return ins;
	}

	public void setIns(HashMap<Integer, ObjectInputStream> ins) {
		this.ins = ins;
	}
	

	public void run()
	 {
		 try {
			 	int sendFlag = 1, init_flag=1;
				System.out.println("Press Enter to Start Talker thread with spamming all nodes....");
				Message m = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				br.readLine();
				int choice = -1;
				System.out.println("Enter a type of message: (0/2/4/6):");
				try{
					choice = Integer.parseInt(br.readLine());				
				} catch(Exception e) {
					System.out.println("Invalid Input, please try again!");					
					run();
				}
				
				switch(choice) { // 0=JoinReq, 2=MMReq, 3=MMRep, 4=CalcK
					case 0:
						m=new Message(0, this.selfId);
						if(localstate.isDistinguishable != 1) {
							System.out.println("Join Msg can be sent by distinguished node only !!! ");
							sendFlag = 0;
							init_flag=1;
						} else {
							System.out.println("Message Type = JoinReq");
						}
						break;
	
					case 2:
						m=new Message(2, this.selfId);
						init_flag=0;
						System.out.println("Message Type = MMReq");
						break;
	
					case 4:
						m=new Message(4, this.selfId);
						init_flag=0;
						if(localstate.isDistinguishable != 1) {
							System.out.println("CalcK MSG can be sent by distinguished node only !!! ");
							sendFlag = 0;
						} else {
							System.out.println("Message Type = CalcKMsg");
							BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));							
							int val;
							System.out.println("Enter K Value:");
							try {
								val = Integer.parseInt(br1.readLine());
								localstate.K = val;
								if(localstate.degree <= val) {
									localstate.isLessK = 1;
								} else {
									localstate.isLessK = 0;
								}
								m=new Message(4, this.selfId, val);
								System.out.println("Message Type = CalcK");
							} catch(Exception e) {
								System.out.println("Invalid Input, please try again!");					
								run();
							}
						}
						break;
						
					case 6:
						init_flag=0;
						localstate.depthReqSrc = localstate.id;
						localstate.spanningTreePeers.clear();
						m = new Message(6, this.selfId);
						localstate.spanningTreePeers.addAll(localstate.children);
						if(localstate.parent != -1)
						{
							localstate.spanningTreePeers.add(localstate.parent);
						}
						sendFlag = 0;
						init_flag = 1;
						for(int peer : localstate.spanningTreePeers)
						{
							outs.get(peer).writeObject(m);
						}
						System.out.println("Sent depth request to peers : " + localstate.spanningTreePeers);
						break;
						
					case 8:
						m=new Message(8, this.selfId);
						init_flag=0;
						System.out.println("Message Type = Min Height Request");
						
						break;
						
					case 14:
						m=new Message(14, this.selfId);
						init_flag=0;
						if(localstate.isDistinguishable != 1) {
							System.out.println("Printing MSG can be sent by distinguished node only !!! ");
							sendFlag = 0;
						} else {						
								m=new Message(14, this.selfId);
							}
						
						break;
						
					default:
						System.out.println("Invalid Input, Please try again!");
						run();
						break;
				}
				if(sendFlag == 1 && init_flag==1) {
					for(ObjectOutputStream o: outs.values())
					{
						System.out.println("Sending Message("+m.message+" to " + o.toString());
						o.writeObject(m);
					}
				} else if(sendFlag == 1 && init_flag == 0) {
					for (int i : localstate.children) 
					{
						ObjectOutputStream o = outs.get(i);					
						System.out.println("Sending Message("+m.message+" to " + o.toString());
						o.writeObject(m);
					}
				}		
				run();
				
		 	} catch (NumberFormatException e) {
		 		System.out.println("Wrong choice of words Son !");
		 		// TODO Auto-generated catch block
		 		e.printStackTrace();
		 	} catch (IOException e) {
			System.out.println("Input failure.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}