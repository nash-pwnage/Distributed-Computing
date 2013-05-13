import java.util.ArrayList;

public class LocalState {

	int isDistinguishable;
	int isLeaf;
	int parent;
	int id;
	int distinguishedNodeId;
	
	int maxDegree;
	int minDegree;
	
	int child_minmax_replies;
	int child_calcK_replies;
	int child_spantree_replies;
	
	ArrayList<Integer> children;
	ArrayList<Integer> spanningTreePeers;
	int degree;
	int K;
	int isLessK;
	
	int peerRepliesForDepth;
	int depthReqSrc;
	int heightVal;
	
	int minHeight;
	int minHeightNode;
	int minHeightReplyCount;
	
	ArrayList<Link> links;
	
	public LocalState() {
		super();
		this.id = -1;
		this.parent = -1;
		this.isDistinguishable = 0;
		this.isLeaf = 1;
		this.maxDegree = 0;
		this.minDegree = 0;
		this.children = new ArrayList<Integer>();
		this.spanningTreePeers = new ArrayList<Integer>();
		this.degree = 0;
		this.child_minmax_replies = 0;
		this.isLessK = 0;
		this.K = 0;
		this.child_spantree_replies=0;
		
		this.peerRepliesForDepth = 0;
		this.depthReqSrc = -1;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightNode = this.id;
		this.minHeightReplyCount = 0;
		
		this.links=null;
		
	}

	public LocalState(int id,int distinguishable, int parent) {
		super();
		this.id = id;
		this.isDistinguishable = distinguishable;
		this.parent = parent;	
		this.isLeaf = 1;
		this.maxDegree = 0;
		this.minDegree = 0;
		this.children = new ArrayList<Integer>();
		this.degree = 0;
		this.child_minmax_replies=0;
		this.isLessK = 0;
		this.K = 0;
		this.spanningTreePeers = new ArrayList<Integer>();
		this.peerRepliesForDepth = 0;
		this.depthReqSrc = -1;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightReplyCount = 0;
		this.minHeightNode = this.id;
		this.child_spantree_replies=0;
	}	
	
	public LocalState(int id,int distinguishable, int parent, ArrayList<Link> a) {
		super();
		this.child_spantree_replies=0;
		this.id = id;
		this.isDistinguishable = distinguishable;
		this.parent = parent;	
		this.isLeaf = 1;
		this.maxDegree = 0;
		this.minDegree = 0;
		this.children = new ArrayList<Integer>();
		this.degree = 0;
		this.child_minmax_replies=0;
		this.isLessK = 0;
		this.K = 0;
		this.spanningTreePeers = new ArrayList<Integer>();
		this.peerRepliesForDepth = 0;
		this.depthReqSrc = -1;
		this.heightVal = 0;
		this.minHeight = 0;
		this.minHeightReplyCount = 0;
		this.minHeightNode = this.id;
		this.links=a;
	}	
	
	public int getIsDistinguishable() {
		return isDistinguishable;
	}

	public void setIsDistinguishable(int isDistinguishable) {
		this.isDistinguishable = isDistinguishable;
	}

	public int getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxDegree() {
		return maxDegree;
	}

	public void setMaxDegree(int maxDegree) {
		this.maxDegree = maxDegree;
	}

	public int getMinDegree() {
		return minDegree;
	}

	public void setMinDegree(int minDegree) {
		this.minDegree = minDegree;
	}

	public ArrayList<Integer> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Integer> children) {
		this.children = children;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public void incrementDegree() {
		this.degree = this.degree + 1;
	}

	void addChild(int child_id) {
		System.out.println("New Child Added: id[" + child_id + "]");
		if(isLeaf == 1){
			isLeaf = 0;
		}
		children.add(child_id);	
	}

	public int getDistinguishedNodeId() {
		return distinguishedNodeId;
	}

	public void setDistinguishedNodeId(int distinguishedNodeId) {
		this.distinguishedNodeId = distinguishedNodeId;
	}

	public int getChild_minmax_replies() {
		return child_minmax_replies;
	}

	public void setChild_minmax_replies(int child_minmax_replies) {
		this.child_minmax_replies = child_minmax_replies;
	}

	public int getIsLessK() {
		return isLessK;
	}

	public void setIsLessK(int isLessK) {
		this.isLessK = isLessK;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	@Override
	public String toString() {
		return "LocalState [isDistinguishable=" + isDistinguishable
				+ ", isLeaf=" + isLeaf + ", parent=" + parent + ", id=" + id
				+ ", distinguishedNodeId=" + distinguishedNodeId
				+ ", maxDegree=" + maxDegree + ", minDegree=" + minDegree
				+ ", child_minmax_replies=" + child_minmax_replies
				+ ", children=" + children + ", degree=" + degree + ", K=" + K
				+ ", isLessK=" + isLessK + "]";
	}
	
}
