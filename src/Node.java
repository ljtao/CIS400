import java.util.ArrayList;


public class Node implements Comparable {
	
	private int uid;
	private String name;
	private int floor;
	private String description;
	private Node previous;
	private double min_distance;
	private ArrayList<Edge> adjacency_list;
	private int x;
	private int y;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Node(){
		adjacency_list = new ArrayList<Edge>();
	}
	
	public int getUid(){
		return uid;
	}
	
	public void setUid(int uid){
		this.uid = uid;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getFloor(){
		return floor;
	}
	
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String desc){
		description = desc;
	}
	
	public Node getPrevious(){
		return previous;
	}
	
	public void setPrevious(Node n){
		previous = n;
	}	
	
	public ArrayList<Edge> getAdjacencyList(){
		return adjacency_list;
	}
	
	public double getMinDistance(){
		return min_distance;
	}
	
	public void setMinDistance(double d){
		min_distance = d;
	}

	@Override
	public int compareTo(Object arg0) {
		Node other = (Node)arg0;
		if(this.min_distance < other.min_distance){
			return -1;
		}
		else if (this.min_distance > other.min_distance){
			return 1;
		}
		else {
			return 0; //equal
		}
	}
	
}
