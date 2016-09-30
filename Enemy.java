//Prepared by Gan Qi Tze

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.*;

public class Enemy implements  RobotFunctions{
	
	private int gameBoundary = 9;			//Board is 10x10 so array is 0 - 9
	private int xCoordinates;				//X Coordinates of the robot
	private int yCoordinates;				//Y Coordinates of the robot
	private int fireXCoordinates;			//X Coordinates of the robot's shot if any. if none the value will be -1
	private int fireYCoordinates;			//Y Coordinates of the robot's shot if any, if none the value will be -1
	private Random randomGen;
	private ArrayList<Integer> moves;		//Stores the 18 moves obtained from the Controller
	private int moveIndex;					//Stores the index in the ArrayList of the move just executed 
	private	FileWriter writer;
	private FileReader reader; 

	//Move up = 1, move down = 2, move right = 3, move left = 4, shoot up = 5, shoot down = 6, shoot right = 7, shoot left = 8

	Enemy(){
		xCoordinates = 0;
		yCoordinates = 0;
		moveIndex = 0;
		fireXCoordinates = -1;
		fireYCoordinates = -1;		
		moves = new ArrayList<Integer>();
		randomGen = new Random();
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

		int currentMove = moves.get(moveIndex);

		//Check the current position of player tank. If the tank is at the boundary check if the move will position the tank out of the boundary. If not
		//then increase xCoordinates or yCoordinates
		if(currentMove == 1){
			if(xCoordinates != 0)
				xCoordinates--;
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 2){
			if(xCoordinates != gameBoundary)
				xCoordinates++;
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 3){
			if(yCoordinates != gameBoundary)
				yCoordinates++;
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 4){
			if(yCoordinates != 0)
				yCoordinates--;
			reinitialiseFireCoordinates();
		}
		else if(currentMove == 5){
			if(xCoordinates == 0)
				reinitialiseFireCoordinates();
			else{
				fireXCoordinates = xCoordinates - 1;
				fireYCoordinates = yCoordinates;
			}
		}
		else if(currentMove == 6){
			if(xCoordinates == gameBoundary)
				reinitialiseFireCoordinates();
			else{
				fireXCoordinates = xCoordinates + 1;
				fireYCoordinates = yCoordinates;
			}
		}
		else if(currentMove == 7){
			if(yCoordinates == gameBoundary)
				reinitialiseFireCoordinates();
			else{
				fireXCoordinates = xCoordinates;
				fireYCoordinates = yCoordinates + 1;
			}
		}
		else if(currentMove == 8){
			if(yCoordinates == 0)
				reinitialiseFireCoordinates();
			else{
				fireXCoordinates = xCoordinates;
				fireYCoordinates = yCoordinates - 1;
			}
		}
		moveIndex++;
	}

	//To generate random moves for CPU Tank
	public int randGen(){
		int n = randomGen.nextInt(8)+1;
		return n;
	}

	//ArrayList to store the list of Random Genereated moves
	public void EnemyList(int a){
		moves.add(a);
	} 

	//save() will make the robot save its current 18 moves into a .txt file
	public void save(File file) throws IOException {
		file.createNewFile();
		writer = new FileWriter(file);
		try{
			for(int i=0; i<moves.size(); i++)
				writer.write(moves.get(i)+ " "); 
		}
		catch(IOException ex){
			System.out.println("Failed...");
		}
		writer.flush();
		writer.close();
	}

	//load() will load the 18 moves saved into the robot
	//remember to clear the ArrayList first
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
	//For CPU robot, it won't flush the 18 moves because if it is in the same game, the CPu will have the same moves
	public void reset(){
		xCoordinates = 0;
		yCoordinates = 0;
		moveIndex = 0;
	}

	//If it is a New Game, it will reset(clear) everything for CPU
	public void clear(){
		xCoordinates = 0;
		yCoordinates = 0;
		moveIndex = 0;
		moves.clear();
	}

	//Reset fireXCoordinates and fireYCoordinates to -1
	private void reinitialiseFireCoordinates(){
		fireXCoordinates = -1;
		fireYCoordinates = -1;
	}

}


