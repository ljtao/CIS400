
public class Edge {
	
	private int start_node_id;
	private int end_node_id;
	private int distance;
	private int edge_type;
	private int direction;
	private int active;
	private String description;
	
	public int getStart_node_id() {
		return start_node_id;
	}
	public void setStart_node_id(int start_node_id) {
		this.start_node_id = start_node_id;
	}
	public int getEnd_node_id() {
		return end_node_id;
	}
	public void setEnd_node_id(int end_node_id) {
		this.end_node_id = end_node_id;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getEdge_type() {
		return edge_type;
	}
	public void setEdge_type(int edge_type) {
		this.edge_type = edge_type;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		description = desc;
	}
	public int getActive(){
		return active;
	}
	public void setActive(int act){
		active = act;
	}
	
	

}
