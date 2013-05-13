import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	int messageType;		// 0=JoinReq, 1=JoinReply, 11=JoinAccept, 10=JoinReject,  2=MMReq, 3=MMRep 
	int message;
	int minDegree;
	int maxDegree;
	int senderId;
	int kVal;
	int numLess;
	int heightVal;
	int minHeight;
	int minHeightNode;
	ArrayList<Link> links;
	
	public Message () {
		super();
		this.messageType = -1;
		this.message = 0;
		this.minDegree = 0;
		this.maxDegree = 0;
		this.kVal = 0;
		this.numLess = 0;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = -1;
		this.links=null;
	}
	
	public Message (int type, int senderId) {
		super();
		this.messageType = type;	
		this.message = 0;
		this.minDegree = 0;
		this.maxDegree = 0;		
		this.senderId = senderId;
		this.kVal = 0;
		this.numLess =0;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = -1;
		this.links=null;
	}
	
	public Message (int type, int senderId, int K) {
		super();
		this.messageType = type;	
		this.message = 0;
		this.minDegree = 0;
		this.maxDegree = 0;		
		this.senderId = senderId;
		this.kVal = K;
		this.numLess = 0;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = -1;
		this.links=null;
	}
	
	/*
	public Message (int type, int msg, int minDeg, int maxDeg) { 
		this.messageType = type;	
		this.message = msg;
		this.minDegree = minDeg;
		this.maxDegree = maxDeg;		
	} 
	*/
	
	public Message(int messageType, int senderId, ArrayList<Link> links) {
		super();
		this.messageType = messageType;
		this.senderId = senderId;
		this.links = links;
	}

	public Message (int type, int minDeg, int maxDeg, int senderId) { 
		super();
		this.messageType = type;	
		this.message = 0;
		this.minDegree = minDeg;
		this.maxDegree = maxDeg;		
		this.senderId = senderId;		
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = -1;
	}
	
	public Message (int type, int minDeg, int maxDeg, int senderId, ArrayList<Link> a) { 
		super();
		this.messageType = type;	
		this.message = 0;
		this.minDegree = minDeg;
		this.maxDegree = maxDeg;		
		this.senderId = senderId;		
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = -1;
		this.links=a;
	}
	
	
	public int getMessageType() {
		return messageType;
	}	
	
	public void setMessageType(int message) {
		this.messageType = message;
	}
	
	public int getMessage() {
		return messageType;
	}
	
	public void setMessage(int msg) {
		this.message = msg;
	}
	
	public int getMinDegree() {
		return minDegree;
	}
	
	public void setMinDegree(int minDeg) {
		this.minDegree = minDeg;
	}
	
	public int maxDegree() {
		return maxDegree;
	}
	
	public void setMaxDegreee(int maxDeg) {
		this.maxDegree = maxDeg;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", message=" + message
				+ ", minDegree=" + minDegree + ", maxDegree=" + maxDegree
				+ ", senderId=" + senderId + ", kVal=" + kVal + ", numLess="
				+ numLess + ", heightVal=" + heightVal + "]";
	}


	
}
