
public class ImageDifference implements Comparable<ImageDifference> {
	private String name1;
	private String name2;
	private int difference;
	
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public int getDifference() {
		return difference;
	}
	public void setDifference(int difference) {
		this.difference = difference;
	}
	@Override
	public int compareTo(ImageDifference o) {
		if (this!=o) {
			if (this.difference>o.difference) {
				return this.difference-o.difference;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString(){
		return this.name1+"--"+this.name2+" "+this.difference;
	}

}
