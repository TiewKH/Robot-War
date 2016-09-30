//Prepared by Tiew Kee Hui
//Uses Iterator Design Pattern - public void setMoves(Iterator movesIterator)
//Uses Iterator Design pattern - public Iterator createIterator()

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class Player implements RobotFunctions, MovesIterator{
	
	private int gameBoundary = 9;			 //Board is 10x10 so array is 0 - 9
	private int xCoordinates;				 //X Coordinates of the robot
	private int yCoordinates;				 //Y Coordinates of the robot
	private int fireXCoordinates;			 //X Coordinates of the robot's shot if any. if none the value will be -1
	private int fireYCoordinates;			 //Y Coordinates of the robot's shot if any, if none the value will be -1
	private ArrayList<Integer> moves;		 //Stores the 18 moves obtained from the Controller
	private ArrayList<Integer> previousMoves;//To save the moves in the previous round for the save() function
	private int moveIndex;					 //Stores the index in the ArrayList of the move just executed 
	private	FileWriter writer;
	private FileReader reader; 
	
	//Move up = 1, move down = 2, move right = 3, move left = 4, shoot up = 5, shoot down = 6, shoot right = 7, shoot left = 8
	
	Player(){
		//Players starts from bottom right corner of the board
		xCoordinates = 9;
		yCoordinates = 9;
		moveIndex = 0;
		fireXCoordinates = -1;
		fireYCoordinates = -1;
		moves = new ArrayList<Integer>();
		previousMoves = new ArrayList<Integer>();
	}
	
	//getXCoordinates() will return the current X Coordinates of the robot
	public int getXCoordinates(){
		return xCoordinates;
	}
	
	//getYCoordinates() will return the current Y Coordinates of the robot
	public int getYCoordinates(){
		return yCoordinates;
	}
	
	//getXFireCoordinates() will return the X coordinate of the shot of the robot if a shot is fired
	//If no shots are fired, fireCoordinates will be x = -1, y = -1
	public int getXFireCoordinates(){
		return fireXCoordinates;
	}
	
	//getYFireCoordinates() will return the Y coordinate of the shot of the robot if a shot is fired
	//If no shots are fired, fireCoordinates will be x = -1, y = -1
	public int getYFireCoordinates(){
		return fireYCoordinates;
	}
	
	//getLastMove() will return the move just executed by the robot
	public int getLastMove(){
		return moves.get(moveIndex-1);
	}
	
	//move() will make the robot update its coordinates based on the next move
	public void move(){
		//Move up = 1, move down = 2, move right = 3, move left = 4, shoot up = 5, shoot down = 6, shoot right = 7, shoot left = 8

		//Get the move specified by the player
		int currentMove = moves.get(moveIndex);
		
		//Check the current position of player tank. If the tank is at the boundary check if the move will position the tank out of the boundary. If not
		//then increase xCoordinates or yCoordinates
		if(currentMove == 1){
			if(xCoordinates != 0){
				xCoordinates--;
			}
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 2){
			if(xCoordinates != gameBoundary){
				xCoordinates++;
			}
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 3){
			if(yCoordinates != gameBoundary){
				yCoordinates++;
			}
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 4){
			if(yCoordinates != 0){
				yCoordinates--;
			}
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 5){
			if(xCoordinates == 0){
				reinitialiseFireCoordinates();
			}
			else{
				fireXCoordinates = xCoordinates - 1;
				fireYCoordinates = yCoordinates;
			}
		}
		else if(currentMove == 6){
			if(xCoordinates == gameBoundary){
				reinitialiseFireCoordinates();
			}
			else{
				fireXCoordinates = xCoordinates + 1;
				fireYCoordinates = yCoordinates;
			}
		}
		else if(currentMove == 7){
			if(yCoordinates == gameBoundary){
				reinitialiseFireCoordinates();
			}
			else{
				fireXCoordinates = xCoordinates;
				fireYCoordinates = yCoordinates + 1;
			}
		}
		else if(currentMove == 8){
			if(yCoordinates == 0){
				reinitialiseFireCoordinates();
			}
			else{
				fireXCoordinates = xCoordinates;
				fireYCoordinates = yCoordinates - 1;
			}
		}
		
		moveIndex++;
	}
	
	//save() will make the robot save the 18 moves executed in the previous turn if it has been executed into a .txt file
	public void save(File file) throws IOException{
		file.createNewFile();
		writer = new FileWriter(file);
		try{
			for(int i=0; i<previousMoves.size(); i++)
				writer.write(previousMoves.get(i)+ " "); 
		}
		catch(IOException ex){
			System.out.println("Failed...");
		}
		writer.flush();
		writer.close();
	}
	
	//load() will load the 18 moves saved into the robot
	public void load(File file) throws IOException{
		//remember to clear the ArrayList first
		moves.clear();
		reader = new FileReader(file); 
		char [] a = new char [36];
		try{
			reader.read(a);
		}
		catch(IOException ex){
			System.out.println("Failed...");
		}	
		for(int i=0; i<a.length; i=i+2){
			int x = Character.getNumericValue(a[i]);
			moves.add(x);			
		}

		reader.close();
	}
	
	//reset() will reset the positions of the robots to their original positions
	//For Player robot, reset will also flush the 18 moves given to it by the player
	//For CPU robot, it won't flush the 18 moves because if it is in the same game, the CPu will have the same moves
	public void reset(){
		xCoordinates = 9;
		yCoordinates = 9;
		moveIndex = 0;
		moves.clear();
	}
	
	//clear() is to completely reset everything to their original value
	public void clear(){
		xCoordinates = 9;
		yCoordinates = 9;
		moveIndex = 0;
		moves.clear();
		previousMoves.clear();
	}
	
	//Iterator Design Pattern - Get an iterator from the controller and copy the values to two local array lists. One for execution and one to use for save()
	public void setMoves(Iterator movesIterator){
		if(previousMoves.size() == 18){
			previousMoves.clear();
		}
		
		while(movesIterator.hasNext()){
			int moveObtained = (int) movesIterator.next();
			moves.add(moveObtained);
			previousMoves.add(moveObtained);
		}
	}
	
	//Iterator Design Patten. createIterator will create an iterator for moves. This iterator will be passed to GameBoard when a game is loaded
	public Iterator createIterator(){
		return moves.iterator();
	}
	
	//Reset fireXCoordinates and fireYCoordinates to -1
	private void reinitialiseFireCoordinates(){
		fireXCoordinates = -1;
		fireYCoordinates = -1;
	}
}