import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class MessageProcessor {
	
	
	LocalState localstate;
	HashMap<Integer,ObjectOutputStream> outs;
	ObjectInputStream ins;
	
	public MessageProcessor(LocalState localstate, HashMap<Integer,ObjectOutputStream> outs, ObjectInputStream ins) {
		super();
		this.localstate = localstate;
		this.outs = outs;
		this.ins=ins;
		
	}

	public void process(Message m)
	{		
		switch(m.messageType) {
			case 0:
				//join message
				if(localstate.parent == -1)	{
					System.out.println("This is a join message");
					localstate.parent = m.senderId;
					System.out.println("This is the new parent : " + localstate.parent);
					Message joinAccept = new Message(11, localstate.id);
					
					try {
						outs.get(m.senderId).writeObject(joinAccept);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("Forwarding Join Request to all execpt Parent and Distinguished node");
					try {	
						for(int i : outs.keySet()) {
							if(i != localstate.parent && i!=localstate.distinguishedNodeId) {
								Message joinReq = new Message(0, localstate.id);
								outs.get(i).writeObject(joinReq);
							}
							if(localstate.id == localstate.distinguishedNodeId)
							{
								localstate.children.add(i);
								System.out.println("new child list : " + localstate.children);
							}
						}
					} catch(IOException IO) {
						IO.printStackTrace();
					}
					
				} else {
					Message joinReject = new Message(10, localstate.id);
					try {
						outs.get(m.senderId).writeObject(joinReject);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 11:
				//Join Accept
				localstate.addChild(m.senderId);
				
				break;
			case 10:		
				//Join Reject
				System.out.println("Recieved Join Reject Msg from id["+m.senderId+"]");
				break;
				
			case 2:
				//Calc Min Max request Degree Msg
				if(m.senderId == localstate.parent) {
					System.out.println("Recieved Calc Min Max Degree Msg from Parent: id["+m.senderId+"]");					
					localstate.child_minmax_replies = localstate.children.size();

					if(localstate.children.size() > 0) {
						System.out.println("Forwarding the Calc Min Max Degree msg to children");
						for (int i=0;i<localstate.children.size();i++) {
							Message mmDegreeReq = new Message(2, localstate.minDegree, localstate.maxDegree, localstate.id);
							try {
								outs.get(localstate.getChildren().get(i)).writeObject(mmDegreeReq);						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{   
						Message mmDegree = new Message(3, localstate.minDegree, localstate.maxDegree, localstate.id);
						System.out.println("Leaf node : so sending first reply : " + mmDegree + " to parent : " + localstate.parent);
						try {
							outs.get(localstate.parent).writeObject(mmDegree);						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				
				break;

			case 3:
				//Calc  Min Max Degree Reply Msg		
				System.out.println(" message received from " + m.senderId);
				
				if(localstate.children.contains(m.senderId)) {
					System.out.println("Recieved Min Max Degree Reply Msg from Child: id["+m.senderId+"]");
					if(m!=null)
						{
							System.out.println("max min degree reply message: "+m);
							localstate.child_minmax_replies = localstate.child_minmax_replies - 1;

							if(localstate.maxDegree < m.maxDegree) {
								localstate.maxDegree = m.maxDegree;
								System.out.println("Max changed to : " + m.maxDegree);
							}
							
							if(localstate.minDegree > m.minDegree) {
								localstate.minDegree = m.minDegree;
								System.out.println("Min changed to : " + m.minDegree);
							}
							
							if(localstate.child_minmax_replies == 0 && localstate.isDistinguishable != 1)
							{
								Message mmDegree = new Message(3, localstate.minDegree, localstate.maxDegree, localstate.id);
								try {
									outs.get(localstate.parent).writeObject(mmDegree);						
									System.out.println("Sent reply for min max to parent" + localstate.parent);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							if(localstate.isDistinguishable == 1 && localstate.child_minmax_replies <= 0) { // && localstate.child_minmax_replies == 0) {
								System.out.println("FINAL OUTPUT:Max Degree[" + localstate.maxDegree + "], Min Degree["+localstate.minDegree+"]");
							}														
						}
					
				} else {
					System.out.println("Cannot Recieve Min Max Degree Reply Msg from non child node: id["+m.senderId+"]");					
					System.exit(-1);
				}
				break;

			case 4:				
				//CalcKDegree Msg
				if(m.senderId == localstate.parent) {
					System.out.println("Recieved CalcK Msg from Parent: id["+m.senderId+"]");					
					localstate.child_calcK_replies = localstate.children.size();
					localstate.K = m.kVal;
					if(localstate.degree <= m.kVal) {
						localstate.isLessK = 1;
					} else {
						localstate.isLessK = 0;
					}
					if(localstate.children.size() > 0) {
						System.out.println("Forwarding the CalcK msg to children");
						for (int i=0;i<localstate.children.size();i++) {
							Message CalcKMsg = new Message(4, localstate.id, m.kVal);
							try {
								outs.get(localstate.getChildren().get(i)).writeObject(CalcKMsg);						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{   
						localstate.K = m.kVal;
						if(localstate.degree <= m.kVal) {
							localstate.isLessK = 1;
						} else {
							localstate.isLessK = 0;
						}
						Message CalcKReply = new Message(5, localstate.id, localstate.isLessK);
						CalcKReply.numLess = localstate.isLessK;
						System.out.println("Leaf node : so sending first calcKReply MSG: " + CalcKReply + " to parent : " + localstate.parent);
						try {
							outs.get(localstate.parent).writeObject(CalcKReply);						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				
				break;
				
			case 5:	
				if(localstate.children.contains(m.senderId)) {
					System.out.println("Recieved CalcK Reply Msg from Child: id["+m.senderId+"]");
					if(m!=null)
						{
							System.out.println("CalcK reply message: "+m);
							localstate.isLessK = localstate.isLessK + m.numLess;
							localstate.child_calcK_replies = localstate.child_calcK_replies - 1;
														
							if(localstate.child_calcK_replies == 0 && localstate.isDistinguishable !=1)
							{
								Message CalcKReply = new Message(5, localstate.id, localstate.isLessK);
								CalcKReply.numLess = localstate.isLessK;
								try {
									outs.get(localstate.parent).writeObject(CalcKReply);						
									System.out.println("Sent reply for CalcK to parent" + localstate.parent);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							if(localstate.isDistinguishable == 1 && localstate.child_calcK_replies <= 0) { // && localstate.child_minmax_replies == 0) {
								System.out.println("FINAL OUTPUT:Number of nodes less than K= " + localstate.K + " is " + localstate.isLessK);
							}														
						}					
				} else {
					System.out.println("Cannot Recieve Min Max Degree Reply Msg from non child node: id["+m.senderId+"]");					
					System.exit(-1);
				}
				break;
			
		
			case 6: 
				//height calculate request received
				localstate.spanningTreePeers.clear();
				//integrate parent and children as your spanning tree peers
				for( int peer : localstate.children)
				{
					if(peer != m.senderId){ localstate.spanningTreePeers.add(peer);}
				}

				if(localstate.parent != -1 && localstate.parent != m.senderId)
				{
					localstate.spanningTreePeers.add(localstate.parent);
				}
				
				
				localstate.depthReqSrc = m.senderId;
				
				//forward the height request to all spanning tree peers except sender 
				// should not happen for spanning tree leaf node automatically
				Message heightRequest =  new Message(6, localstate.id);
				localstate.peerRepliesForDepth = 0;
				for(int peer : localstate.spanningTreePeers)
				{
					try {
						outs.get(peer).writeObject(heightRequest);
						System.out.println("Sending packet : " +heightRequest);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Forwarded depth request to peers  - " + localstate.spanningTreePeers);
				
				
				// if leaf node then generate a reply
				if(localstate.spanningTreePeers.size() == 0)
				{
				 Message heightReply = new Message(7, localstate.id);
				 heightReply.heightVal = 1;
				 try {
			 		outs.get(localstate.depthReqSrc).writeObject(heightReply);
			 		System.out.println("Sending packet : " +heightReply);
			 		localstate.heightVal = 0;
			    	} catch (IOException e) {
					// TODO Auto-generated catch block
			    		e.printStackTrace();
			    	}
				}
				break;
				
			case 7:
				//height reply received by a node
				System.out.println("Received height reply " + m);
				localstate.peerRepliesForDepth++;
				System.out.println("replies received : " + localstate.peerRepliesForDepth);
				localstate.heightVal = Math.max(m.heightVal,localstate.heightVal);
				if(localstate.peerRepliesForDepth == localstate.spanningTreePeers.size())
				{
					if(localstate.id ==  localstate.depthReqSrc)
					{						
						// print height
						localstate.minHeightNode = localstate.id;
						localstate.minHeight = localstate.heightVal;
						System.out.println("HEIGHT OF SPANNING TREE AT THIS NODE = " + localstate.heightVal);
					}
					else
					{
					  Message heightReplyToParent =  new Message(7, localstate.id);
					  heightReplyToParent.heightVal = localstate.heightVal +1;
					  try {
						outs.get(localstate.depthReqSrc).writeObject(heightReplyToParent);
						
						System.out.println("Sending height reply " + heightReplyToParent + " to " + localstate.depthReqSrc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  
						
					}
					localstate.heightVal = 0;
					localstate.peerRepliesForDepth = 0;
			    }
				
				
				break;
			
			case 8:
				//Calc Min height request Degree Msg
				if(m.senderId == localstate.parent) {
					System.out.println("Recieved min height request Msg from Parent: id["+m.senderId+"]");					
					localstate.minHeightReplyCount = 0;

					if(localstate.children.size() > 0) {
						System.out.println("Forwarding the min height req msg to children");
						for (int i=0;i<localstate.children.size();i++) {
							Message minHeightReq = new Message(8, localstate.minDegree, localstate.maxDegree, localstate.id);
							
							try {
								outs.get(localstate.getChildren().get(i)).writeObject(minHeightReq);						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{   
						Message minHeightReply = new Message(9, localstate.minDegree, localstate.maxDegree, localstate.id);
						minHeightReply.minHeight = localstate.minHeight;
						minHeightReply.minHeightNode = localstate.minHeightNode;
						System.out.println("Leaf node : so sending first reply : " + minHeightReply + " to parent : " + localstate.parent);
						try {
							outs.get(localstate.parent).writeObject(minHeightReply);						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				break;
			case 9:
				//Calc  min height Reply Msg		
				System.out.println(" message received from " + m.senderId);
				
				if(localstate.children.contains(m.senderId)) {
					System.out.println("Recieved min height Reply Msg from Child: id["+m.senderId+"]");
					if(m!=null)
						{
							System.out.println("min height reply message: "+m);
							localstate.minHeightReplyCount++;

							if(localstate.minHeight > m.minHeight) {
								localstate.minHeight = m.minHeight;
								localstate.minHeightNode = m.minHeightNode;
								System.out.println("min height changed to : " + m.minHeight);
							}
							
													
							if(localstate.minHeightReplyCount == localstate.children.size() && localstate.isDistinguishable != 1)
							{
								Message minHeightReply = new Message(9, localstate.minDegree, localstate.maxDegree, localstate.id);
								minHeightReply.minHeight = localstate.minHeight;
								minHeightReply.minHeightNode = localstate.minHeightNode;
								try {
									outs.get(localstate.parent).writeObject(minHeightReply);						
									System.out.println("Sent reply for min height reply to parent" + localstate.parent
											 + " : " +minHeightReply);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								localstate.minHeightReplyCount = 0;
							}
							
							if(localstate.isDistinguishable == 1 && localstate.minHeightReplyCount == localstate.children.size()) { // && localstate.child_minmax_replies == 0) {
								
								localstate.minHeightReplyCount = 0;
								System.out.println("FINAL OUTPUT:Min Height [" + localstate.minHeight + "], at node  " + localstate.minHeightNode);
							}														
						}
					
					
				} else {
					System.out.println("Cannot Recieve min height Reply Msg from non child node: id["+m.senderId+"]");					
					System.exit(-1);
				}
				break;
				
			case 14:				
				//Reaching the leaf nodes !
					if(localstate.children.size() > 0) {
						System.out.println("Exploring children of node "+localstate.id);
						for (int i=0;i<localstate.children.size();i++) {
							Message Spantreemsg = new Message(14, localstate.id);
							try {
								outs.get(localstate.getChildren().get(i)).writeObject(Spantreemsg);						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{   
						
						Message Spantreemsg = new Message(15, localstate.id,localstate.links);
						Link branch= new Link(localstate.parent,localstate.id);
						localstate.links.add(branch);
						System.out.println("Leaf node sending : " + Spantreemsg + " to parent : " + localstate.parent);
						try {
							outs.get(localstate.parent).writeObject(Spantreemsg);						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				
				break;
				
			case 15:	
				if(localstate.children.contains(m.senderId)) {
					System.out.println("Recieved Msg from Child: id["+m.senderId+"]"); localstate.child_spantree_replies++;
					
					if(m!=null)
						{
							
							if(localstate.child_spantree_replies == 0 && localstate.isDistinguishable !=1)
							{
								Message Spantreemsg = new Message(15, localstate.id, localstate.links);
								try {
									outs.get(localstate.parent).writeObject(Spantreemsg);						
									System.out.println("Sent reply to parent" + localstate.parent);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							if(localstate.isDistinguishable == 1 && localstate.child_spantree_replies == localstate.children.size()) { // && localstate.child_minmax_replies == 0) {
								System.out.println("FINAL OUTPUT:"+localstate.links.toString());
							}														
						}					
				} else {
					System.out.println("Cannot Recieve Reply Msg from non child node: id["+m.senderId+"]");					
					System.exit(-1);
				}
				break;
			

		}
	}
}