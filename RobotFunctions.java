//Prepared by Tiew Kee Hui
import java.io.*;

public interface RobotFunctions{
	
	//getXCoordinates() will return the current X Coordinates of the robot
	public int getXCoordinates();
	
	//getYCoordinates() will return the current Y Coordinates of the robot
	public int getYCoordinates();
	
	//getXFireCoordinates() will return the X coordinate of the shot of the robot if a shot is fired
	//If no shots are fired OR OUT OF RANGE, fireCoordinates will be x = -1, y = -1
	public int getXFireCoordinates();
	
	//getYFireCoordinates() will return the Y coordinate of the shot of the robot if a shot is fired
	//If no shots are fired, fireCoordinates will be x = -1, y = -1
	public int getYFireCoordinates();
	
	//getLastMove() will return the move just executed by the robot
	public int getLastMove();
	
	//move() will make the robot update its coordinates based on the next move
	public void move();
	
	//save() will make the robot save its current 18 moves into a .txt file
	public void save(File file) throws IOException;
	
	//load() will load the 18 moves saved into the robot
	public void load(File file) throws IOException;
	
	//reset() will flush the moves of the robot
	public void reset();
	
	//clear() will completely reset everything (for New Game)
	public void clear();
}