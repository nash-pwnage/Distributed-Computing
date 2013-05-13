import java.io.Serializable;


public class Link implements Serializable{

	int source, link;

	public Link(int source, int link) {
		super();
		this.source = source;
		this.link = link;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getLink() {
		return link;
	}

	public void setLink(int link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Link [source=" + source + ", link=" + link + "]";
	}
	

	
	
}
