import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class Listener extends Thread{

	private int selfId;
	private int listenToId;
	HashMap<Integer,ObjectOutputStream> outs;
	
	// GlobalStatus global;	
	MessageProcessor processor;
	private ObjectInputStream inputStream;
	private LocalState lstate;
	
	public Listener(LocalState localstate) {
		lstate = localstate;
	}
	
	public Listener(LocalState localstate, int listenToId,
			ObjectInputStream inputstreams, HashMap<Integer,ObjectOutputStream>outs) { //, int distinguished, int parent) {
		super();
		this.selfId = localstate.id;
		this.listenToId = listenToId;
		this.inputStream = inputstreams;
		this.outs = outs;
		this.lstate = localstate;
		this.processor = new MessageProcessor(lstate, this.outs,inputstreams);
	}
	
	public void run()
	{
		System.out.println("Listener is now ready and listening to client " + this.listenToId);
		Message m=null;
		do{
			try {
				m = (Message) inputStream .readObject();
				System.out.println("Message: "+m);
				if(m==null){
					continue;
				}
			//processor.process(m);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			
			if(m!=null)
				{
					System.out.println("Processing Message: "+m.messageType);
					processor.process(m);
				}
			
		} while(true);
		
		
		
	}
	
	public int getSelfId() {
		return selfId;
	}
	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}
	public int getListenToId() {
		return listenToId;
	}
	public void setListenToId(int listenToId) {
		this.listenToId = listenToId;
	}
	
	

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	

	
	
}
