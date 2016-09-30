//Most of the GameBoard was prepared by Aaron Patrick Nathaniel with help from Tiew Kee Hui
//Uses Iterator Design Pattern - public Iterator createIterator()
//Uses Iterator Design Pattern - loadMoves(Iterator it)

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GameBoard extends JFrame implements ActionListener, MovesIterator {
	private static final String[] move = {"^","<","v",">"};
		
	private static final String[] shoot = {"^^^","<<<","vvv",">>>"};
		
	private static final String[] menu = {"New Game","Save","Load","Exit"};
		
		
	//Image icons for explosions, CPU, player and emptyTiles
	private ImageIcon Explosion = new ImageIcon("Explo_2.png");
		
	private ImageIcon CpuMDown = new ImageIcon("Cpu_moveDown.png");
	private ImageIcon CpuMLeft = new ImageIcon("Cpu_moveLeft.png");
	private ImageIcon CpuMRight = new ImageIcon("Cpu_moveRight.png");
	private ImageIcon CpuMUp = new ImageIcon("Cpu_moveUp.png");
	private ImageIcon CpuSDown = new ImageIcon("Cpu_shootDown.png");
	private ImageIcon CpuSLeft = new ImageIcon("Cpu_shootLeft.png");
	private ImageIcon CpuSRight = new ImageIcon("Cpu_shootRight.png");
	private ImageIcon CpuSUp = new ImageIcon("Cpu_shootUp.png");
		
	private ImageIcon PlayerMDown = new ImageIcon("Player_moveDown.png");
	private ImageIcon PlayerMLeft = new ImageIcon("Player_moveLeft.png");
	private ImageIcon PlayerMRight = new ImageIcon("Player_moveRight.png");
	private ImageIcon PlayerMUp= new ImageIcon("Player_moveUp.png");
	private ImageIcon PlayerSDown = new ImageIcon("Player_shootDown.png");
	private ImageIcon PlayerSLeft = new ImageIcon("Player_shootLeft.png");
	private ImageIcon PlayerSRight = new ImageIcon("Player_shootRight.png");
	private ImageIcon PlayerSUp= new ImageIcon("Player_shootUp.png");		
		
	private ImageIcon Tiles = new ImageIcon("square2.png");
		
		
	//Image icon to set the player icon in the game board
	private ImageIcon ImageIconPLAYER = new ImageIcon();
		
	//Image icon to set the CPU icon in the game board
	private ImageIcon ImageIconCPU = new ImageIcon();		
		
		
	//A 10x10 aray of labels to load the icons inside
	private JLabel[][] lb = new JLabel[10][10];
		
	private JLabel keyType1; //move and shoot word
	private JLabel keyType2;
		
	//Grid layout for the board
	private JPanel lblPanel = new JPanel(new GridLayout(10,10));
		
	private JPanel mainPanel;
		
	//Text Area for displaying moves the user chooses
	private JTextArea dispArea = new JTextArea();	
		
	//Count to make sure the user has to input exactly 18 moves 
	private int count = 0;
		
	//Text field to tell the user how many times he has already tried
	private JTextField tries = new JTextField("NUMBER OF TRIES: ",10); 
		
	//Array list to store the move code to be passed to Controller and Player
	//Move up = 1, move down = 2, move right = 3, move left = 4, shoot up = 5, shoot down = 6, shoot right = 7, shoot left = 8
	private ArrayList<Integer> moveNum = new ArrayList<Integer>();
	
	//Array list to store the moves the user chooses in String form
	private ArrayList<String> moveMessage = new ArrayList<String>();
	
	//4 JButtons - New Game, Save, Load 
	private JButton[] btnmenu = new JButton[4];	
		
	//JButton to remove a move 
	private JButton Delete; 
	
	//JButton to start the program after 18 moves has been entered
	private JButton Start; 
		
		
	private JPanel movePanel1;  //move
	private JPanel movePanel2;  //shoot
	private JPanel menuPanel;	//start/save..			
	private JPanel keyPanel;
	private JPanel startPanel; 		
		
	public GameBoard(){
		this.setTitle("Shooting Game");
		mainPanel = new JPanel(new BorderLayout());			
		movePanel1 = new JPanel(new BorderLayout());
		movePanel2 = new JPanel(new BorderLayout());
		menuPanel = new JPanel(new FlowLayout());
		startPanel = new JPanel(new BorderLayout());
			
		//keyPanel= new JPanel(new GridLayout(2,1));
		keyPanel= new JPanel(new BorderLayout());
		
		JLabel keyType1 = new JLabel(" MOVE ");
		JLabel keyType2 = new JLabel(" SHOOT ");
		JLabel keyType3 = new JLabel(" MOVES ");
			
		movePanel1.setSize(50,80);
		movePanel2.setSize(50,80);
		
		//Load the labels in the board with blank tiles
		for (int y=0;y<10;y++){
			for (int x=0;x<10;x++){
				lb[y][x] = new JLabel(Tiles);										
				lblPanel.add(lb[y][x]);					
			}	
		}			
			
		//Initialise the positions of the tanks
		lb[0][0].setIcon(CpuMRight);		
		lb[9][9].setIcon(PlayerMUp);
			
			
		//The 4 Move buttons
		JButton[] btnmove = new JButton[4];
		for (int i=0;i<4;i++){
			btnmove[i] = new JButton(move[i]);	
			btnmove[i].setPreferredSize(new Dimension(60, 55));
			btnmove[i].setBackground(Color.BLUE);
			btnmove[i].setForeground(Color.BLACK);
			movePanel1.add(btnmove[i]);						
			btnmove[i].addActionListener(this);
		}
			
		//The 4 Shoot buttons
		JButton[] btnshoot = new JButton[4];
		for (int i=0;i<4;i++){
			btnshoot[i] = new JButton(shoot[i]);
			btnshoot[i].setPreferredSize(new Dimension(60, 55));
			btnshoot[i].setBackground(Color.ORANGE);
			btnshoot[i].setForeground(Color.RED);
			movePanel2.add(btnshoot[i]);				
			btnshoot[i].addActionListener(this);
		}
			
		//The New Game, Save, Load and Exit buttons
		for (int i=0;i<4;i++){
			btnmenu[i] = new JButton(menu[i]);
			//btnmenu[i].setPreferredSize(new Dimension(60, 55));
			menuPanel.add(btnmenu[i]);
		}
			
	    //PLACING THINGS INTO PANEL
		movePanel1.add(btnmove[0],BorderLayout.PAGE_START);
		movePanel1.add(btnmove[1],BorderLayout.LINE_START);
		movePanel1.add(keyType1,BorderLayout.CENTER);			
		movePanel1.add(btnmove[2],BorderLayout.PAGE_END);
		movePanel1.add(btnmove[3],BorderLayout.LINE_END);
			
		movePanel2.add(btnshoot[0],BorderLayout.PAGE_START);
		movePanel2.add(btnshoot[1],BorderLayout.LINE_START);
		movePanel2.add(keyType2,BorderLayout.CENTER);	
		movePanel2.add(btnshoot[2],BorderLayout.PAGE_END);
		movePanel2.add(btnshoot[3],BorderLayout.LINE_END);
			
			
		Start = new JButton("START");
		Delete = new JButton("DELETE");
		Start.setEnabled(false);
		Start.setBackground(Color.RED);
		Start.setForeground(Color.BLACK);
		Delete.setBackground(Color.GRAY);
		Delete.setForeground(Color.BLUE);
		Start.setPreferredSize(new Dimension(80, 80));
		Delete.setPreferredSize(new Dimension(80, 80));
		Start.addActionListener(this);
		Delete.addActionListener(this);
		startPanel.add(Start, BorderLayout.CENTER);
		startPanel.add(Delete, BorderLayout.PAGE_END);
		keyPanel.add(movePanel1,BorderLayout.PAGE_START);			
		keyPanel.add(startPanel,BorderLayout.CENTER);			
		keyPanel.add(movePanel2,BorderLayout.PAGE_END);
			
			
		dispArea.setPreferredSize(new Dimension(170,150));
		dispArea.setFont(new Font("Courier",Font.BOLD,23));
		dispArea.setBackground(Color.BLACK);
		dispArea.setForeground(Color.WHITE);			
		dispArea.setSize(dispArea.getPreferredSize());
		dispArea.setEditable(false);
		dispArea.setLineWrap(true);
		dispArea.setWrapStyleWord(true);
		
		tries.setEditable(false);	
		tries.setBackground(Color.GREEN);
		tries.setForeground(Color.BLACK);
			
		lblPanel.setBackground(Color.WHITE);
		lblPanel.setForeground(Color.BLUE);
			
		mainPanel.add(menuPanel, BorderLayout.PAGE_START);	
		mainPanel.add(lblPanel, BorderLayout.CENTER);				
		mainPanel.add(keyPanel, BorderLayout.LINE_END);	
		mainPanel.add(dispArea, BorderLayout.LINE_START);	
		mainPanel.add(tries, BorderLayout.PAGE_END);	
			
		this.add(mainPanel);
		this.setSize(908,666); 
		this.setResizable(true);
		this.setBackground(Color.BLUE);
		this.setForeground(Color.RED);
			
		this.setVisible(true);
		//getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Iterator Design Pattern - Sending the array list of moves using an iterator
	public Iterator createIterator(){
		Iterator moveIterator = moveNum.iterator();
		return moveIterator;
	}
		
	//Get variables from controller to update the game board
	public void updateBoard(int playerNewXCoordinates, int playerNewYCoordinates, int playerOldXCoordinates, 
							int playerOldYCoordinates, int playerNewXFireCoordinates, int playerNewYFireCoordinates, int playerOldXFireCoordinates, 
							int playerOldYFireCoordinates, int playerLastMove, int cpuNewXCoordinates, int cpuNewYCoordinates, int cpuOldXCoordinates,
							int cpuOldYCoordinates, int cpuNewXFireCoordinates, int cpuNewYFireCoordinates, int cpuOldXFireCoordinates, 
							int cpuOldYFireCoordinates, int cpuLastMove, boolean playerWin, boolean cpuWin, int numberOfTries){
							//If there are fire coordinates value will be >= 0 else will be -1
		
		if(playerLastMove == 1){
				ImageIconPLAYER = PlayerMUp;	
		}
		else if(playerLastMove == 2){
				ImageIconPLAYER = PlayerMDown;	
				
		}
		else if(playerLastMove == 3){
				ImageIconPLAYER = PlayerMRight;	
				
		}
		else if(playerLastMove == 4){
				ImageIconPLAYER = PlayerMLeft;	
				
		}			
		else if(playerLastMove == 5){
				ImageIconPLAYER = PlayerSUp;	
				
		}
		else if(playerLastMove == 6){
				ImageIconPLAYER = PlayerSDown;	
				
		}
		else if(playerLastMove == 7){
				ImageIconPLAYER = PlayerSRight;	
				
		}
		else if(playerLastMove == 8){
				ImageIconPLAYER = PlayerSLeft;					
		}
		
		if(cpuLastMove == 1){
				ImageIconCPU = CpuMUp;					
		}
		else if(cpuLastMove == 2){
				ImageIconCPU = CpuMDown	;			
		}
		else if(cpuLastMove == 3){
				ImageIconCPU = CpuMRight;				
		}
		else if(cpuLastMove == 4){
				ImageIconCPU = CpuMLeft;					
		}
		else if(cpuLastMove == 5){
				ImageIconCPU = CpuSUp;				
		}
		else if(cpuLastMove == 6){
				ImageIconCPU = CpuSDown;				
		}
		else if(cpuLastMove == 7){
				ImageIconCPU = CpuSRight;					
		}
		else if(cpuLastMove == 8){
				ImageIconCPU = CpuSLeft;					
		}
		
		//Set the number of tries
		tries.setText("NUMBER OF TRIES :" + numberOfTries);
		
		//Clean up the explosions from the previous rounds
		if(playerOldXFireCoordinates >= 0 ){
			lb[playerOldXFireCoordinates][playerOldYFireCoordinates].setIcon(Tiles);
		}
		
		if(cpuOldXFireCoordinates >= 0 ){
			lb[cpuOldXFireCoordinates][cpuOldYFireCoordinates].setIcon(Tiles);	
		}	
		
		//Clear the old tiles
		lb[playerOldXCoordinates][playerOldYCoordinates].setIcon(Tiles);	
		lb[cpuOldXCoordinates][cpuOldYCoordinates].setIcon(Tiles);	
		
		//Set the new tiles
		lb[playerNewXCoordinates][playerNewYCoordinates].setIcon(ImageIconPLAYER);
		lb[cpuNewXCoordinates][cpuNewYCoordinates].setIcon(ImageIconCPU);
		
		//If both tanks switch coordinates after moving, this means they have passed each other and crashed. Return to main program.
		if((playerNewXCoordinates == cpuOldXCoordinates) && (playerNewYCoordinates == cpuOldYCoordinates) && (cpuNewXCoordinates == playerOldXCoordinates) && (cpuNewYCoordinates == playerOldYCoordinates)){
			lb[playerNewXCoordinates][playerNewYCoordinates].setIcon(Explosion);
			lb[cpuNewXCoordinates][cpuNewYCoordinates].setIcon(Explosion);
			return;
		}
		
		//Check if any shots were fired. If true, put the fire icon at the correct label. Repeat for CPU.
		if(playerNewXFireCoordinates >= 0 ){
			lb[playerNewXFireCoordinates][playerNewYFireCoordinates].setIcon(Explosion);
		}
		
		if(cpuNewXFireCoordinates >= 0 ){
			lb[cpuNewXFireCoordinates][cpuNewYFireCoordinates].setIcon(Explosion);	
		}
		
		/* If both of them end up on the same coordinates, an explosion will happen at the tile they're both in and player has not win. However one of 
		the tanks may have executed a FIRE and one of them may be moving so we have to display the shots before making the tanks blow up. Return to main
		program. */
		if( (playerNewXCoordinates == cpuNewXCoordinates && playerNewYCoordinates == cpuNewYCoordinates) ){
			lb[playerNewXCoordinates][playerNewYCoordinates].setIcon(Explosion);
			return;
		}
		
		//If any of them wins, one of the tank would have been in the fire coordinates of the other.
		if((cpuWin == true && playerWin == false) || (playerWin == true && cpuWin == false)){
			return;
		}
		
		//If none of them win and shots were fired, clean up the shots into tiles before progressing to next move
			
	}
	
	//Allows Controller to display a message via GameBoard
	public void message(String message){		
		JOptionPane.showMessageDialog(null, message);
	}	
		
	//Allows Controller to start a New Game
	public void addNewGameListener(ActionListener startListener ){			
		 btnmenu[0].addActionListener(startListener); 
						
	}	
	
	//Allows Controller to execute a game
	public void addStartListener(ActionListener newGameListener){			
		 Start.addActionListener(newGameListener); 		
	}	
	
	//Allows Controller to save a game
	public void addSaveListener(ActionListener saveListener){		
		 btnmenu[1].addActionListener(saveListener); 
	}
	
	//Allows Controller to load a game
	public void addLoadListener(ActionListener loadListener){
	     btnmenu[2].addActionListener(loadListener); 
	}
	
	//Allows Controller to exit the game
	public void addExitListener(ActionListener exitListener){
		btnmenu[3].addActionListener(exitListener);
	}
		
	//Resetting the board to its original state
	public void resetBoard(int numOfTries){
		for (int y=0;y<10;y++){
			for (int x=0;x<10;x++){									
				lb[y][x].setIcon(Tiles);				
			}	
		}			
			
		lb[0][0].setIcon(CpuMRight);		
		lb[9][9].setIcon(PlayerMUp);
			
		moveNum.clear();
		moveMessage.clear();
		Start.setEnabled(false);
		count = 0;
		dispArea.setText("");
		tries.setText("Number Of Tries: " + numOfTries);	
	}
	
	//Iterator Design Pattern - Loads the moves obtained from Player onto the gameBoard for human to view
	public void loadMoves(Iterator it){
		//To check if the loaded file is empty
		boolean hasData = true;
		
		while(it.hasNext()){
			int moveObtained = (int) it.next();

			if(moveObtained == 1){
				String message = "UP";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(1);
			}
			else if(moveObtained == 2){
				String message = "DOWN";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(2);
			}
			else if(moveObtained == 3){
				String message = "RIGHT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(3);
			}
			else if(moveObtained == 4){
				String message = "LEFT";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(4);
			}
			else if(moveObtained == 5){
				String message = "SHOOT UP";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(5);
			}
			else if(moveObtained == 6){
				String message = "SHOOT DOWN";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(6);	
			}
			else if(moveObtained == 7){
				String message = "SHOOT RIGHT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(7);
			}
			else if(moveObtained == 8){
				String message = "SHOOT LEFT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(8);
			}
			else if(moveObtained == -1){
				//If loads an empty file, it will be -1. Start button will not be enabled
				hasData = false;
				break;
			}
		}
		
		if(hasData == true){
			count = 18;
			Start.setEnabled(true);
		}
	}
	
	//Actions to be performed when the up, down, left, right, shoot up, shoot down, shoot left and shoot right button is pressed
	public void actionPerformed(ActionEvent evt){
		String in = evt.getActionCommand();
		
		if (count < 18){
			if(in.equals("^")){	
				String message = "UP";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(1);
				count++;	
			}
			else if(in.equals(">")){
				String message = "RIGHT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(3);
				count++;
			}
			else if(in.equals("<")){
				String message = "LEFT";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(4);
				count++;
			}
			else if(in.equals("v")){
				String message = "DOWN";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(2);
				count++;
			}
			if(in.equals("^^^")){
				String message = "SHOOT UP";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(5);
				count++;
			}
			else if(in.equals(">>>")){	
				String message = "SHOOT RIGHT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(7);
				count++;
			}
			else if(in.equals("<<<")){	
				String message = "SHOOT LEFT";
				moveMessage.add(message);
				dispArea.append(message + "\n");
				moveNum.add(8);
				count++;
			}
			else if(in.equals("vvv")){
				String message = "SHOOT DOWN";
				moveMessage.add(message);
				dispArea.append(message + "\n");			
				moveNum.add(6);
				count++;	
			}
		}
		
		if(count ==18){
			Start.setEnabled(true);
		}
		
		if(in.equals("DELETE")){
			if(count > 0){
				dispArea.setText("");
				moveMessage.remove(moveMessage.size()-1);
				moveNum.remove(moveNum.size()-1);
				for(int i = 0; i < moveMessage.size(); i++ ){				
					dispArea.append(moveMessage.get(i) + "\n");
				}
				count--;
				Start.setEnabled(false);
			}	

		}
		
		if(in.equals("START")){
			Start.setBackground(Color.GREEN);
			Start.setForeground(Color.BLACK);	
		}	
	}	
}