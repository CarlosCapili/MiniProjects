/*
This program finds a possible pathway from the top left corner 
to the bottom right corner using recursion.

0s are walls and 1s are possible pathways
*/
import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

class MazeSolver{
	public static void main(String[]arg){
		//MENU
		System.out.println("Welcome to Maze Solver!");
		System.out.println("\n1. When importing a textfile, make sure the rows and column lengths are equal");
		System.out.println("2. Make sure the all the values are either 1s or 0s, where 1s are walkable paths and 0s are walls");
		System.out.println("3. Values must be separated by commas. Ex. 0,1,0,0\n");

		do{
			System.out.println("Please enter the name of the file with the maze you want to be solved:");
			Scanner in = new Scanner(System.in);
			String fileName = in.nextLine();
			System.out.println("");
			Maze path = new Maze();

			if(path.initMaze(fileName)){
				if(path.recursiveSolver(0,0) == false){
					System.out.println("Couldn't find a solution...");
					path.displayMaze();
				}
				else{
					System.out.println("Found a SOLUTION!");
					path.displayMaze();	
				}
			}
			else{
				System.out.println("Please enter a valid txt file.");
			}

			System.out.println("Do you want to enter a file again? (y/n)");
			Scanner redo = new Scanner(System.in);
			String menu = redo.nextLine();

			if (menu.equals("n")){
				break;
			}

		} while(true);
	}
}// end of MazeSolver

class Maze{

	private int[][] maze;
	private java.util.ArrayList<Integer> numbers = new java.util.ArrayList<Integer>();
	private int mazeSize = 0;

	public boolean initMaze(String fileName){
	
		//open file and read each line as a row and place into maze
		try {
			Scanner read = new Scanner(new File(fileName));
			while(read.hasNextLine()){
				
				String[] line = read.nextLine().split(",");

				for (int i=0; i<line.length; i++){ 
					numbers.add(Integer.parseInt(line[i])); //add each element into numbers as an int
				}

				mazeSize++; //determines the size of 2d maze var
			
			}
			genMaze(); //populate the 2d array var maze
			read.close();
			return true;

		} catch (java.io.FileNotFoundException ex){
			System.out.println("File does not exist!\n");
			return false;
		}
	}// end of initMaze

	private void genMaze(){
		maze = new int[mazeSize][mazeSize];
		int length = 0;
		//transfer arraylist elements into 2d array maze
		for (int j=0; j<mazeSize; j++){
			for (int i=0; i<mazeSize; i++){
				maze[j][i] = numbers.get(length);
				length++;
			}
		}
	}// end of genMaze

	private boolean checkColBounds(int col){
		return (col >= 0) && (col < maze[0].length);
	}
	private boolean checkRowBounds(int row){
		return (row >= 0) && (row < maze.length);
	}

	public boolean recursiveSolver(int row, int col){
		
		if (checkRowBounds(row) && checkColBounds(col) && maze[row][col] == 1 && maze[row][col] == 1){
			//Change the maze path to an 8 indicating path taken
			maze[row][col] = 8;
			//Check if last element in 2d array is reached
			if (row == maze.length-1 && col == maze[0].length-1){
				return true;
			}
			//Path movements
			if (recursiveSolver(row, col+1)){
				return true; //Move right
			}
			if (recursiveSolver(row+1, col)){
				return true; //Move down
			}
			if (recursiveSolver(row, col-1)){
				return true;//Move left
			}
			if (recursiveSolver(row-1, col)){
				return true;//Move Up
			}
			maze[row][col] = 1; 
			return false; //If it makes it here then it is false
		}
		return false;
	}// end of recursiveSolver

	public void displayMaze(){
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[i].length; j++){
				System.out.print(maze[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}
	}//end of displayMaze
}//end of class Maze