import java.util.BitSet;

public class Image {
	private String name;
	private BitSet hash;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BitSet getHash() {
		return hash;
	}
	public void setHash(BitSet hash) {
		this.hash = hash;
	}
}
