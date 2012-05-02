import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.mysql.jdbc.Statement;


public class Dijkstra {
	
	/*Set MySQL Queries*/
	private static final String GET_NODES_ALL = "SELECT * FROM Nodes";
	private static final String GET_EDGES_ALL = "SELECT * FROM Edges";
	private static final String GET_EDGES_NO_STAIRS = "SELECT * FROM Edges WHERE (edgeType != 1)";
	private static final String GET_EDGES_NO_ELEVATORS = "SELECT * FROM Edges WHERE (edgeType != 2)";
	private static final String GET_DEPARTMENTS = "SELECT * FROM Nodes WHERE name LIKE 'wr_%'";
	
	private static final String dbUrl = "jdbc:mysql://fling.seas.upenn.edu/ayo";
	private static final String dbClass = "com.mysql.jdbc.Driver";
	
	//CHANGE BASED ON TOUCHSCREEN LOCATION
	private static final int starting_direction = 4;

	
	///////////////////////////
	
	private static LinkedList<Edge> allEdgeList;
	private static HashMap<Integer,Node> idToNodeMap;
	
	
	
	
	public Dijkstra(){
		//TODO: constructor
		//closedNodeList = new LinkedList<Node>();
		//openNodeList = new PriorityQueue<Node>();
	}
	
	public static void populateANL(String query){
		/*TODO: iterate through node table and create Nodes with the corresponding
		 * attributes and place them all into the closedNodeList.
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection (dbUrl, "ayo", "seniordesign12");
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Node n = new Node();
				n.setUid(rs.getInt(1));
				n.setName(rs.getString(2));
				n.setFloor(rs.getInt(3));
				n.setDescription(rs.getString(4));
				n.setMinDistance(rs.getDouble(6));
				n.setX(rs.getInt(7));
				n.setY(rs.getInt(8));
				idToNodeMap.put(n.getUid(),n);
			} //end while

			con.close();
			} //end try

		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void populateAEL(String query){		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection (dbUrl, "ayo", "seniordesign12");
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Edge e = new Edge();
				e.setStart_node_id(rs.getInt(1));
				e.setEnd_node_id(rs.getInt(2));
				e.setDistance(rs.getInt(3));
				e.setEdge_type(rs.getInt(4));
				e.setDescription(rs.getString(5));
				e.setActive(rs.getInt(6));
				if(e.getActive() == 1){
					//System.out.println("adding edge from " + rs.getInt(1) + " to " + rs.getInt(2));
					allEdgeList.add(e);
				}
				/*Edge e2 = new Edge();
				e2.setStart_node_id(rs.getInt(2));
				e2.setEnd_node_id(rs.getInt(1));
				e2.setDistance(rs.getInt(3));
				e2.setEdge_type(rs.getInt(4));
				e2.setDescription(rs.getString(5));
				e2.setActive(rs.getInt(6));
				if(e2.getActive() == 1){
					System.out.println("adding edge from " + rs.getInt(2) + " to " + rs.getInt(1));
					allEdgeList.add(e2);
				}*/
			} //end while

			con.close();
			} //end try

		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static LinkedList<Node> getDepartments(){
		LinkedList<Node> output = new LinkedList<Node>();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection (dbUrl, "ayo", "seniordesign12");
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(GET_DEPARTMENTS);
			while(rs.next()){
				Node n = new Node();
				n.setUid(rs.getInt(1));
				n.setName(rs.getString(2));
				n.setFloor(rs.getInt(3));
				n.setDescription(rs.getString(4));
				n.setMinDistance(rs.getDouble(6));
				n.setX(rs.getInt(7));
				n.setY(rs.getInt(8));
				output.add(n);
				}
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return output;
	}
	
	public static void disableEdge(int start, int end){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection (dbUrl, "ayo", "seniordesign12");
			Statement stmt = (Statement) con.createStatement();
			String statement = "UPDATE Edges SET active = 0 WHERE (idStartNode = " + start + ") AND (idEndNode = " + end + ");";
			stmt.executeUpdate(statement);
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void enableEdge(int start, int end){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection (dbUrl, "ayo", "seniordesign12");
			Statement stmt = (Statement) con.createStatement();
			stmt.executeUpdate("UPDATE Edges SET active = 1 WHERE (idStartNode = " + start + ") and (idEndNode = " + end + ");");
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setupAdjacencies(){
		Iterator<Edge> iter = allEdgeList.iterator();
		while(iter.hasNext()){
			Edge temp_e = iter.next();		
			Node temp_n1 = idToNodeMap.get(temp_e.getStart_node_id());
			temp_n1.getAdjacencyList().add(temp_e);
		}
	}
		
	public static void computePaths(Node start){
		start.setMinDistance(0);
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		pq.add(start);
		while(!pq.isEmpty()){
			//dequeue
			Node temp = pq.poll();
			for(Edge e : temp.getAdjacencyList()){
				//System.out.println("Running adjacency between " + temp.getName() + " AND " + idToNodeMap.get(e.getEnd_node_id()).getName());
				Node n = idToNodeMap.get(e.getEnd_node_id());
				double edgedist = e.getDistance();
				double intermediate = temp.getMinDistance() + edgedist;		
				//System.out.println("potential n mindist value: " + intermediate);
				if (intermediate < n.getMinDistance()) {
				    pq.remove(n);
				    n.setMinDistance(intermediate);
				    n.setPrevious(temp);
				    pq.add(n);
				}
			}
		}
	}
	
		
    public static List<Node> getShortestPath(Node target){
        List<Node> hitnodes = new ArrayList<Node>();
        recurse(target, hitnodes);
        Collections.reverse(hitnodes);        
        return hitnodes;
    }
    
    public static void recurse(Node target, List<Node> path){
    	if(target != null){
    		path.add(target);
        	recurse(target.getPrevious(), path);
    	}
    	else{
    	}
    }
    
    public static String outputToString(List<Node> list){
        /* make Paths*/
    	String turn = "";
    	int count = 1;
    	int previous_dir = starting_direction;
    	if(list.get(1) != null){
    		Path startpath = new Path();
    		startpath.setEnd(list.get(1));
    		startpath.setStart(list.get(0));
    		startpath.setDirection();
    		turn = calcRelativeTurn(previous_dir, startpath.getDirection(), startpath.getFloorChange());
    		previous_dir = startpath.getDirection();
    	}
    	String s = "Starting at the ";
    		s = s + (list.get(0).getDescription() + "\n");
    		//s = s + count + ". Go " + turn + " to the ";
    	for(int i = 1; i < list.size() - 1; i++){
    		s = s + count + ". Go " + turn + " to the ";
    		s = s + (list.get(i).getDescription() + "\n"); 
    		Path startpath = new Path();
    		startpath.setEnd(list.get(i+1));
    		startpath.setStart(list.get(i));
    		startpath.setDirection();
    		turn = calcRelativeTurn(previous_dir, startpath.getDirection(), startpath.getFloorChange());
    		previous_dir = startpath.getDirection();
    		count++;
    		
    	}
		s = s + count + ". Go " + turn + " to the ";
    	s = s + list.get(list.size()-1).getDescription();
    	s = s.trim();
    	return s;
    }

    public static void initFromDB(String edgeset){ 
		allEdgeList = new LinkedList<Edge>();
		idToNodeMap = new HashMap<Integer,Node>();
    	populateANL(GET_NODES_ALL);
    	populateAEL(edgeset);
    	setupAdjacencies();
    }
    
    public static String calcRelativeTurn(int start, int end, int floor_change){
    	if(floor_change == -1){
    		return "down";
    	}
    	else if(floor_change == 1){
    		return "up";
    	}
    	else{
    		int change = start - end;
    		System.out.println("start: " + start + " end: " + end);
	    	if(change == -1) return "slightly right";
	    	if(change == -2) return "right";
	    	if(change == -3) return "sharply right";
	    	if(change == -4) return "back";
	    	if(change == -5) return "sharply left";
	    	if(change == -6) return "left";
	    	if(change == -7) return "slightly left";
	    	if(change == 0) return "straight";
	    	if(change == 1) return "slightly left";
	    	if(change == 2) return "left";
	    	if(change == 3) return "sharply left";
	    	if(change == 4) return "back";
	    	if(change == 5) return "sharply right";
	    	if(change == 6) return "right";
	    	if(change == 7) return "slightly right";
	    	return "";
    	}
    }
    
    
    public static void main(String[] args){
    	System.out.println("Running...");
    	initFromDB(GET_EDGES_NO_STAIRS);
    	computePaths(idToNodeMap.get(1));
    	System.out.println(outputToString(getShortestPath(idToNodeMap.get(19))));	//change interger value to change endpoint
    }
    
			
}


