
public class Path {
	
	private Node start;
	private Node end;
	private int direction;
	private int floor_change;
	
	public Node getStart() {
		return start;
	}
	public void setStart(Node start) {
		this.start = start;
	}
	public Node getEnd() {
		return end;
	}
	public void setEnd(Node end) {
		this.end = end;
	}
	
	public boolean setDirection() {
		if(getEnd() == null || getStart() == null){
			return false;
		}
		else if(start.getFloor() > end.getFloor()){
			floor_change = -1;
			getFloorChangeOrientation(end);
			return true;			
		}
		else if(start.getFloor() < end.getFloor()){
			floor_change = 1;
			getFloorChangeOrientation(end);
			return true;
		}
		int delta_x = getStart().getX() - getEnd().getX();
		int delta_y = getStart().getY() - getEnd().getY();
		System.out.println("x: " + delta_x + " y: " + delta_y);
		if(delta_y < -10){
			//moved southish
			if(delta_x > 10){
				direction = 5;
				return true;
			}
			else if(delta_x < -10){
				direction = 3;
				return true;
			}
			else{
				direction = 4;
				return true;
			}
		}
		else if(delta_y > 10){
			//moved northish
			if(delta_x > 10){
				direction = 7;
				return true;
			}
			else if (delta_x < -10){
				direction = 1;
				return true;
			}
			else{
				direction = 0;
				return true;
			}
		}
		else{
			if(delta_x > 0){
				//moved west
				direction = 6;
				return true;
			}
			else{
				//moved east
				direction = 2;
				return true;
			}
		
		}
	}
	
	public int getDirection(){
		return direction;
	}
	
	public int getFloorChange(){
		return floor_change;
	}
	
	public void getFloorChangeOrientation(Node exit){
		if(exit.getName().endsWith("N")){
			direction = 0;
		}
		else if(exit.getName().endsWith("S")){
			direction = 4;
		}
		else if(exit.getName().endsWith("E")){
			direction = 2;
		}
		else {
			direction = 6;
		}
	
	}

	
	

}
