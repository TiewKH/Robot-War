//Tiew worked on the game logic and on putting the game logic in the Timer and communicating with the GameBoard (updating the board, save, load, new game)
//Uses Iterator Design Pattern - private class StartListener implements ActionListener
//Uses Iterator Design Pattern - private class LoadListener implements ActionListener

import java.util.Iterator;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class GameController{
	
	private static final int numberOfMoves = 18;	//Number of moves each tank has
	private Player player;							//Player tank
	private Enemy cpu;								//CPU tank
	private GameBoard gameBoard;					//Game board
	private Timer timer;							//Timer to fire off the event
	private int numberOfTries = 0;					//How many times has the user tried the game
	private boolean executing;						//Checks if the execute button has been pressed. User cannot save or load when execute has been pressed
	
	private int playerOldXCoordinates = 9;
	private int playerOldYCoordinates = 9;
	private int playerNewXCoordinates = 9;
	private int playerNewYCoordinates = 9;
	private int playerOldXFireCoordinates = -1;
	private int playerOldYFireCoordinates = -1;
	private int playerNewXFireCoordinates = -1;
	private int playerNewYFireCoordinates = -1;
	private int playerLastMove;
	private boolean playerWin = false;
	private boolean playerFire = false;									
				
	private int cpuOldXCoordinates = 0;
	private int cpuOldYCoordinates = 0;
	private int cpuNewXCoordinates = 0;
	private int cpuNewYCoordinates = 0;
	private int cpuOldXFireCoordinates = -1;
	private int cpuOldYFireCoordinates = -1;
	private int cpuNewXFireCoordinates = -1;
	private int cpuNewYFireCoordinates = -1;
	private int cpuLastMove = 3;
	private boolean cpuWin = false;
	private boolean cpuFire = false;
	
	private int turns = 0;							//Counter to check how many moves has already been executed in a turn
	
	private File playerFile;						//Store player moves
	private File cpuFile;							//Store CPU moves
	private File controllerFile;					//Store numberOfTurns;
	
	private	FileWriter writer;
	private FileReader reader; 
	
	public GameController(){
		player = new Player();
		gameBoard = new GameBoard();
		gameBoard.addStartListener(new StartListener());
		gameBoard.addNewGameListener(new NewGameListener());
		gameBoard.addSaveListener(new SaveListener());
		gameBoard.addLoadListener(new LoadListener());
		gameBoard.addExitListener(new ExitListener());
		
		controllerFile = new File("numberOfTries.txt");
		playerFile = new File("playerMoves.txt");
		cpuFile = new File("EnemyTank.txt");

		cpu = new Enemy();
		executing = false;
		
		int r;
		for(int i = 0; i<18; i++){
			r = cpu.randGen();
			cpu.EnemyList(r);			
		}	
		
		//Timer to execute the game turn by turn. It checks the game logic and passes the values to the gameBoard for updating the icons of tanks and explosions.
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				executing = true;
				if(turns != 18){
					player.move();
					cpu.move();
					
					//Get the player and CPU new coordinates
					playerNewXCoordinates = player.getXCoordinates();
					playerNewYCoordinates = player.getYCoordinates();
					cpuNewXCoordinates = cpu.getXCoordinates();
					cpuNewYCoordinates = cpu.getYCoordinates();
					
					//Get the move they just performed
					playerLastMove = player.getLastMove();
					cpuLastMove = cpu.getLastMove();
					
					//Get the coordinates of their fire, if they did not fire or fire out of bounds, the coordinates of X and Y will be -1
					playerNewXFireCoordinates = player.getXFireCoordinates();
					playerNewYFireCoordinates = player.getYFireCoordinates();
					
					cpuNewXFireCoordinates = cpu.getXFireCoordinates();
					cpuNewYFireCoordinates = cpu.getYFireCoordinates();
					
					//Check if game draw: Both on same tile (even if one shoot and one move towards it: they both die) 
					//CPU and player switch coordinates after move; which means they pass each other and crash
					//Both shoot one box ahead of them
					if( ((playerNewXCoordinates == cpuNewXCoordinates) && (playerNewYCoordinates == cpuNewYCoordinates)) || 
					
						((playerNewXCoordinates == cpuOldXCoordinates) && (playerNewYCoordinates == cpuOldYCoordinates) && 
						 (cpuNewXCoordinates == playerOldXCoordinates) && (cpuNewYCoordinates == playerOldYCoordinates)) ||
						 
						 ((playerNewXCoordinates == cpuNewXFireCoordinates) && (playerNewYCoordinates == cpuNewYFireCoordinates) &&
						   (cpuNewXCoordinates == playerNewXFireCoordinates) && (cpuNewYCoordinates == playerNewYFireCoordinates)) ){
						playerWin = true;
						cpuWin = true;
					}
					//Check if game win: One of them is in the fire coordinate and the other one isn't
					else if ( playerNewXFireCoordinates == cpuNewXCoordinates && playerNewYFireCoordinates == cpuNewYCoordinates ){
						playerWin = true;
						cpuWin = false;
					}
					else if ( cpuNewXFireCoordinates == playerNewXCoordinates && cpuNewYFireCoordinates == playerNewYCoordinates ){
						playerWin = false;
						cpuWin = true;
					}
					
					gameBoard.updateBoard(playerNewXCoordinates, playerNewYCoordinates, playerOldXCoordinates, playerOldYCoordinates,
										  playerNewXFireCoordinates, playerNewYFireCoordinates, playerOldXFireCoordinates, playerOldYFireCoordinates, playerLastMove, 
										  cpuNewXCoordinates, cpuNewYCoordinates, cpuOldXCoordinates, cpuOldYCoordinates, 
										  cpuNewXFireCoordinates, cpuNewYFireCoordinates, cpuOldXFireCoordinates, cpuOldYFireCoordinates, cpuLastMove, 
										  playerWin, cpuWin, numberOfTries);
					turns++;
					
					if(playerWin == true && cpuWin == false){
						Object[] options = {"New Game", "Quit"};
						String msg = "Congratulations! You took " + numberOfTries + " tries to win! Please click New Game to play again!";
						int choice = JOptionPane.showOptionDialog(null, msg, "Game over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
																  options, options[0]);
						if(choice == JOptionPane.NO_OPTION){
							System.exit(0);
						}
						else if(choice == JOptionPane.YES_OPTION){
							playerWin = false;
							cpuWin = false;
							
							turns = 0;
							numberOfTries = 0;
							cpu.clear();
							int r;
							for(int i = 0; i<18; i++){
								r = cpu.randGen();
								cpu.EnemyList(r);			
							}
							player.clear();
							gameBoard.resetBoard(numberOfTries);
							
							playerOldXCoordinates = 9;
							playerOldYCoordinates = 9;
							cpuOldXCoordinates = 0;
							cpuOldYCoordinates = 0;
						}
						executing = false;
						timer.stop();
						return;
					}
					//If it is a draw or the player loses, the player has not won the game. He has to try again.
					else if((playerWin == true && cpuWin == true) || (playerWin == false && cpuWin == true)){
						String msg = "You did not win... Please try again!";
						playerWin = false;
						cpuWin = false;
						
						turns = 0;
						cpu.reset();
						player.reset();
						gameBoard.message(msg);
						gameBoard.resetBoard(numberOfTries);
						executing = false;
						
						playerOldXCoordinates = 9;
						playerOldYCoordinates = 9;
						cpuOldXCoordinates = 0;
						cpuOldYCoordinates = 0;
						
						timer.stop();
						return;
					}
				}
				else if(turns == 18){
					String msg = "Nothing happened! Play again!";
			
					turns = 0;
					cpu.reset();
					player.reset();
					gameBoard.message(msg);
					gameBoard.resetBoard(numberOfTries);		
					executing = false;
					
					playerOldXCoordinates = 9;
					playerOldYCoordinates = 9;
					cpuOldXCoordinates = 0;
					cpuOldYCoordinates = 0;
							
					timer.stop();
					return;
				}
				
				playerOldXCoordinates = playerNewXCoordinates;
				playerOldYCoordinates = playerNewYCoordinates;
				playerOldXFireCoordinates = playerNewXFireCoordinates;
				playerOldYFireCoordinates = playerNewYFireCoordinates;
					
				cpuOldXCoordinates = cpuNewXCoordinates;
				cpuOldYCoordinates = cpuNewYCoordinates;
				cpuOldXFireCoordinates = cpuNewXFireCoordinates;
				cpuOldYFireCoordinates = cpuNewYFireCoordinates;
			}
		});
	}
	
	//save() to save number of tries in a .txt file
	private void save() throws IOException{
		controllerFile.createNewFile();
		writer = new FileWriter(controllerFile);
		try{
			writer.write(numberOfTries + " "); 
		}
		catch(IOException ex){
			System.out.println("Failed...");
		}
		writer.flush();
		writer.close();
	}
	
	//load() to load number of tries from a .txt file 
	private void load() throws IOException{
		reader = new FileReader(controllerFile); 
		char [] a = new char[1];
		
		try{
			reader.read(a);
		}
		catch(IOException ex){
			System.out.println("Failed...");
		}	
		
		numberOfTries = Character.getNumericValue(a[0]);	
		
		reader.close();
	}
	
	//main() to start the game
	public static void main(String[] args){
		new GameController();
	}
	
	//Actions to be performed when the Start button is pressed
	private class StartListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if(executing == true){
				String msg = "Game is being executed! You cannot press Start again!";
				gameBoard.message(msg);
			}
			else{
				player.reset();											//For load
				Iterator moves = gameBoard.createIterator();			//Iterator Design Pattern, creating an iterator
				numberOfTries++;
				player.setMoves(moves);									//Iterator Design Pattern, sending iterator to Player

				timer.start();
			}
		}
	}
	
	//Actions to be performed when the Save button is pressed
	private class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if(executing == true){
				String msg = "Game is being executed! You cannot save game now!";
				gameBoard.message(msg);
			}
			else{
				try{
					player.save(playerFile);
					cpu.save(cpuFile);
					save();
					String msg = "File successfully saved!";
					gameBoard.message(msg);
				}
				catch(IOException ex){
					String msg = "File failed to be saved!";
					gameBoard.message(msg);
				}
			}
		}
	}
	
	//Actions to be performed when the Load button is pressed
	private class LoadListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if(executing == true){
				String msg = "Game is being executed! You cannot load game now!";
				gameBoard.message(msg);
			}
			else{
				try{
					player.load(playerFile);
					cpu.load(cpuFile);
					load();
					
					gameBoard.resetBoard(numberOfTries);
					gameBoard.loadMoves(player.createIterator());					//Iterator Design Pattern, player has to send moves to GameBoard to display the moves when a save file is loaded
					
					String msg = "File successfully loaded!";
					gameBoard.message(msg);
				}
				catch(IOException ex){
					String msg = "File failed to be loaded!";
					gameBoard.message(msg);
				}
				
			}
		}
	}
	
	//Actions to be performed when New Game button is pressed - Reset the game board to original state and to clear() player and cpu
	private class NewGameListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if(executing == true){
				String msg = "Game is being executed! You cannot start a new game now!";
				gameBoard.message(msg);
			}
			else{
				player.clear();
				cpu.clear();
				numberOfTries = 0;
				int r;
				for(int i = 0; i<18; i++){
					r = cpu.randGen();
					cpu.EnemyList(r);			
				}
				gameBoard.resetBoard(0);
				
				String msg = "A New Game has been started! The CPU now has different moves!";
				gameBoard.message(msg);
			}
		}
	}
	
	//Actions to be performed when Exit button is pressed - Quit the game
	private class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			System.exit(0);
		}
	}
}