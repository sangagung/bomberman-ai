import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class  AI_1406573766_SangAgungRP
{
	private static final boolean PASSABLE = true;

	private static final boolean DESTRUCTIBLE = true;

	private static final boolean BOMBUP = true;

	private static final boolean POWERUP = true;

	private static final boolean DANGER = true;

	public static Player player;

	public static String nickname = "LordProtector";

	private static CellObject[][] board;

	private static List<CellObject> wallDest;

	private static List<CellObject> wallIndest;

	private static List<CellObject> bombs;

	private static List<CellObject> ups;

	public static String status = "SAFE";

	public static String move= "";

	public static String debug = "debug.txt";

	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		CellObject temp = new CellObject(0, 0, true, true, true, true, true);
		player = new Player(1,1,"0");

		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter(debug, true));
			while (true) {

				// arraylist yang di perbaharui setiap looping
				// reset publik tiap looping
				ups = new ArrayList<CellObject>();
				bombs = new ArrayList<CellObject>();
				wallDest = new ArrayList<CellObject>();
				wallIndest = new ArrayList<CellObject>();
				String input = "";
				int turn = 0;
				int jmlPemain = 0;
	                                 
				// Read board state
				// Read until "END" is detected
				while (!input.equals("END")) {
					input = in.nextLine();
					String [] inputArray =input.split(" ");
	                    if (inputArray[0].equals("TURN")){
	                   		turn = Integer.parseInt(inputArray[1]);
	                    }
	                	else if(inputArray[0].equals("PLAYER")){
	                		jmlPemain = Integer.parseInt(inputArray[1]);
	                		for(int i = 0 ; i < jmlPemain ; i++){
	                			input = in.nextLine();
	                			String [] inputArrayC = input.split(" ");
	                			if(inputArrayC[1].equals(nickname)){
	                				player.nomorPlayer = inputArrayC[0].substring(1);
	                			}
	                		}
	                	}
	                	else if(inputArray[0].equals("BOARD")){

	                		// masukin peta ke board dari outputnya
	                		int row = Integer.parseInt(inputArray[1]);
	                    	int col = Integer.parseInt(inputArray[2]);
	                    	board = new CellObject[row][col];

	                    	for(int i = 0 ; i < row ; i++){
	                    		for(int j = 0 ; j < row ; j++){
	                    			board[i][j] = new CellObject(0, 0, true, true, true, true, true);
	                    		}
	                    	}

	                    	for (int i = 0 ; i < row ; i++){
	                    		input = in.nextLine();

	                    		// membuat inputan menjadi lebih mudah di olah
	                    		input = input.replace(" ","").replace("]","").substring(1).replace("["," ") + " ^";
	                    		inputArray = input.split(" ");

	                    		for(int j = 0 ; j < col ; j++){
	                    			input = inputArray[j];

	                    			// cek jalan setapak biasa
	                    			if(input.equals("")){
	                    				board[i][j] = createCell(i, j, input);
	                    			}

	                    			// cek apakah tembok yang bisa di hancurin atau tidak
	                    			else if(input.substring(0,1).equals("#") || input.substring(0,1).equals("X")){
	                    				board[i][j] = createCell(i, j, input);
	                    				if(input.substring(0,1).equals("X")){
	                    					wallDest.add(board[i][j]);
	                    				}
	                    				else{
	                    					wallIndest.add(board[i][j]);
	                    				}
	                    			}else {
	                    				// mode isi yang seperti 0;1;2;3
	                    				String [] inputArrayB = input.split(";");
	                    				if(inputArrayB.length > 0){
	                        				for(int k = 0 ; k < inputArrayB.length ; k++){
	                        					input = inputArrayB[k];
	                        					String awalInput = input.substring(0,1);

	                        					// setting untuk bom, ada power dan juga waktu ledakannya
	                        					// dan masukkan kedalam array bom
	                        					if(awalInput.equals("B")){
	                        						board[i][j] = createCell(i, j, inputArrayB[k]);
	                        						bombs.add((Bomb) board[i][j]);
	                        					}else if (awalInput.equals("F")){

	                        						// cek apakah itu flare
	                        						board[i][j] = createCell(i, j, input);

	                        					}else if(awalInput.equals("+")){
	                        						//cek apakah itu power up
	                        						board[i][j] = createCell(i, j, input);;
	                        					}else {
	                        						//Player
	                        						if(input.equals(player.nomorPlayer)){
	                        							player.x = i;
	                        							player.y = j;
	                        						}
	                        					}
	                        				}
	                    				}
	                    			}
	                    		}
	                		}
	                   	}
				}

				//checkReachable(player.x, player.y);
				if(bombs.size() == 0 || !board[player.x][player.y].isDangerous()){
					status = "AMAN";
					System.out.println("to wall");
					temp = nearestWall(wallDest);
					moveToWall(temp);
				}
				else if(ups.size() > 0){
					System.out.println("to powerup");
					temp = nearestWall(ups);
					moveToWall(temp);
				}
				else {
					System.out.println("flees");
					temp = nearestWall(bombs);
					fleeFromBomb(temp);
				}
				System.out.println(temp.getX() + " " + temp.getY());
				System.out.println(player.x + " " + player.y);
			}
		}
		catch (IOException e){

		}
	}

	public static CellObject createCell(int row, int col, String str){
		switch (str) {
        	case "###":
        		return new CellObject(row, col, !PASSABLE, !DESTRUCTIBLE, !BOMBUP, !POWERUP, !DANGER);
        	case "XXX":
        		return new CellObject(row, col, !PASSABLE, DESTRUCTIBLE, !BOMBUP, !POWERUP, !DANGER);
        	case "+B":
        		return new CellObject(row, col, PASSABLE, !DESTRUCTIBLE, BOMBUP, !POWERUP, !DANGER);
        	case "+P":
        		return new CellObject(row, col, PASSABLE, !DESTRUCTIBLE, !BOMBUP, POWERUP, !DANGER);
        	case "F2":
        		return new CellObject(row, col, !PASSABLE, !DESTRUCTIBLE, !BOMBUP, !POWERUP, DANGER);
        	case "XBX":
        		return new CellObject(row, col, !PASSABLE, DESTRUCTIBLE, BOMBUP, !POWERUP, !DANGER);
        	case "XPX":
        		return new CellObject(row, col, !PASSABLE, DESTRUCTIBLE, !BOMBUP, POWERUP, !DANGER);
        	case "":
        		return new CellObject(row, col, PASSABLE, !DESTRUCTIBLE, BOMBUP, POWERUP, !DANGER);
        	default :
        		if(str.charAt(0) == 'B'){
	        		Bomb bom = new Bomb(row, col, PASSABLE, DESTRUCTIBLE, BOMBUP, POWERUP, DANGER, Integer.parseInt(str.substring(1,2)), Integer.parseInt(str.substring(2,3)));
	               	for(int i = 1; i < bom.power() - bom.count() + 1; i++){
	               		System.out.println("iterasi ke " + i);
	               		System.out.println("bom di " + row + ", " + col);
	               		if(row-i >= 0){
	               			System.out.println("atas");
	               			board[row-i][col].setDangerous();
	               		} if(row+i < board.length){
	               			board[row+i][col].setDangerous();
	               			System.out.println("bawah");
	               		} if(col-i >= 0){
	               			board[row][col-i].setDangerous();
	               			System.out.println("kiri");
	               		} if(col+i < board.length){
	               			board[row][col+i].setDangerous();
	               			System.out.println("kanan");
	               		}
	               	}
               		return (CellObject) bom;
            	}
        return new CellObject(row, col, PASSABLE, !DESTRUCTIBLE, BOMBUP, POWERUP, !DANGER);
        }
	}

	// public static void setDangerous(Bomb bomb){
	// 	for(int i = 0; i < bomb.power() - bomb.count() + 1; i++){
	// 		if(withinBoard(bomb.getX() + i, bomb.getY())){
	// 			board[bomb.getX() + i][bomb.getY()].setDangerous();
	// 		}
	// 		if(withinBoard(bomb.getX() - i, bomb.getY())){
	// 			board[bomb.getX() - i][bomb.getY()].setDangerous();
	// 		}
	// 		if(withinBoard(bomb.getX(), bomb.getY() + i)){
	// 			board[bomb.getX()][bomb.getY() + i].setDangerous();
	// 		}
	// 		if(withinBoard(bomb.getX(), bomb.getY() - i)){
	// 			board[bomb.getX()][bomb.getY() - i].setDangerous();
	// 		}
	// 	}
	// }

	//rekursif untuk mencari cell mana saja yang dapat dicapai oleh player
	public static void checkReachable(int x, int y){
		if(board[x][y].isPassable()){
			board[x][y].setReachable();

			if(board[x][y].hasPowerUp() || board[x][y].hasBombUp()){
				ups.add(board[x][y]);
			}
			
			//bawah
			if(validMove(x+1, y) && !board[x+1][y].isReachable()){
				checkReachable(x+1, y);	
			}
			//atas
			if(validMove(x-1, y) && !board[x-1][y].isReachable()){
				checkReachable(x-1, y);	
			}
			//kanan
			if(validMove(x, y+1) && !board[x][y+1].isReachable()){
				checkReachable(x, y+1);	
			}
			//kiri
			if(validMove(x, y-1) && !board[x][y-1].isReachable()){
				checkReachable(x, y-1);	
			}
		}

		return;
	}


	// cek siapakah yang paling dekat dari arraylist yang diberikan
	// dibandingkan dengan metode MD sehingga diketahui jarak terdekatnya
	public static CellObject nearestWall(List<CellObject> walls){
		if(walls.size() < 1){
			return null;
		}
		CellObject tmp = walls.get(0);
		int resultTmp = Math.abs(tmp.getX() - player.x) + Math.abs(tmp.getY() - player.y);
		for(int i = 1 ; i < walls.size() ; i++){
			int resultComp = Math.abs(walls.get(i).getX() - player.x) + Math.abs(walls.get(i).getY() - player.y);
			
			if(resultComp < resultTmp){
				tmp = walls.get(i);
				resultTmp = resultComp;
			}
		}
		try
		{
		    FileWriter fw = new FileWriter(debug,true); 
		    fw.write("to " + tmp.getX() + " " + tmp.getY());
		    fw.close();
		}
		catch(IOException ioe)
		{
		}
		return tmp;
	}

	// ini bekerja dengan cek dari 2 method didownnya
	// di cek move mana yang terbaik dari up - left - down - right 
	// menggunakan MD untuk menentukan jarak terdekat atau terjauh
	public static void moveToWall(CellObject wall){
		// DLS deep 1 LOL + MD
		int up = 9000;
		int right = 9000;
		int left = 9000;
		int down = 9000;
		int xP = player.x;
		int yP = player.y;
		int xW = wall.getX();
		int yW = wall.getY();

		if(Math.abs(xP-xW) + Math.abs(yP-yW) == 1){
			move = "DROP";
			System.out.println(">> DROP BOMB");
			return;
		}
		if(validMove(xP-1,yP)){
			up = Math.abs(xP-1-xW) + Math.abs(yP-yW);
			if(move.equals("DOWN")) up +=2;
		}
		if(validMove(xP+1,yP)){
			down = Math.abs(xP+1-xW) + Math.abs(yP-yW);
			if(move.equals("UP")) down +=2;
		}
		if(validMove(xP,yP+1)){
			right = Math.abs(xP-xW) + Math.abs(yP-yW+1);
			if(move.equals("LEFT")) right +=2;
		}
		if(validMove(xP,yP-1)){
			left = Math.abs(xP-xW) + Math.abs(yP-yW-1);
			if(move.equals("RIGHT")) left +=2;
		}
		if(Math.abs(xP-xW) + Math.abs(yP-yW) == 1){
			move = "DROP";
			System.out.println(">> DROP BOMB");
			return;
		}

		int hasil = Math.min(up,Math.min(down,Math.min(right,left)));
		
		if(down == hasil){
			move = "DOWN";
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(up == hasil){
			move = "UP";
			System.out.println(">> MOVE UP");
			return;
		}
		if(left == hasil){
			move = "LEFT";
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(right == hasil){
			move = "RIGHT";
			System.out.println(">> MOVE RIGHT");
			return;
		}
		move = "STAY";
		System.out.println(">> STAY");
		return;
	}

	public static void fleeFromBomb(CellObject bom){
		// DLS deep 1 LOL + MD
		int up = -1;
		int right = -1;
		int left = -1;
		int down = -1;
		int xP = player.x;
		int yP = player.y;
		int xW = bom.getX();
		int yW = bom.getY();
		System.out.println("masuk method");
		if(validMove(xP-1,yP)){
			if(!board[xP-1][yP].isDangerous()){
				up = 1000;
			}
			else{
				up = Math.abs(xP-1-xW) + Math.abs(yP-yW);
				if(move.equals("DOWN")) up -=2;
			}
		}
		if(validMove(xP+1,yP)){
			if(!board[xP+1][yP].isDangerous()){
				down = 1000;
			}
			else{
				down = Math.abs(xP+1-xW) + Math.abs(yP-yW);
				if(move.equals("UP")) down -=2;
			}
		}
		if(validMove(xP,yP-1)){
			if(!board[xP][yP-1].isDangerous()){
				left = 1000;
			}
			else{
				left = Math.abs(xP-xW) + Math.abs(yP-1-yW);
				if(move.equals("RIGHT")) left -=2;
			}
		}
		if(validMove(xP,yP+1)){
			if(!board[xP][yP+1].isDangerous()){
				right = 1000;
			}
			else{
				right = Math.abs(xP-xW) + Math.abs(yP-yW+1);
				if(move.equals("LEFT")) right -=2;
			}
		}
		int hasil = Math.max(up,Math.max(down,Math.max(right,left)));
		if(hasil <= 1 && xP != xW && yP != yW){
			move = "STAY";
			System.out.println(">> STAY");
			return;
		}
		if(up == hasil){
			move = "UP";
			System.out.println(">> MOVE UP");
			return;
		}
		if(left == hasil){
			move = "LEFT";
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(down == hasil){
			move = "DOWN";
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(right == hasil){
			move = "RIGHT";
			System.out.println(">> MOVE RIGHT");
			return;
		}
	}


	public static boolean validMove(int x, int y){
		if(x < 0 || x >= board.length || y < 0 || y >= board[0].length){
			return false;
		} else if(!board[x][y].isPassable()){
			return false;
		}
		return true;
	}

	public static boolean withinBoard(int x, int y){
		if(x < 0 || x >= board.length || y < 0 || y >= board[0].length){
			return false;
		}
		return true;
	}
}

class Player{
	public int x;
	public int y;
	public String nomorPlayer;

	public Player(int x, int y, String nomorPlayer){
		this.x = x;
		this.y = y;
		this.nomorPlayer = nomorPlayer;
	}
}

class CellObject{
	protected boolean passable;

	protected boolean destructible;

	protected boolean bombUp;

	protected boolean powerUp;

	protected boolean danger;

	protected boolean reachable;

	protected int xAxis;

	protected int yAxis;

	public CellObject(int row, int col, boolean passable, boolean destructible, boolean bombUp, boolean powerUp, boolean danger){
		this.xAxis = row;
		this.yAxis = col;
		this.passable = passable;
		this.destructible = destructible;
		this.bombUp = bombUp;
		this.powerUp = powerUp;
		this.danger = danger;
		this.reachable = false;
	}

	public boolean isPassable(){
		return passable;
	}

	public boolean isDestructible(){
		return destructible;
	}

	public boolean hasBombUp(){
		return bombUp;
	}

	public boolean hasPowerUp(){
		return powerUp;
	}

	public boolean isDangerous(){
		return danger;
	}

	public void setDangerous(){
		this.danger = true;
	}

	public int getX(){
		return xAxis;
	}

	public int getY(){
		return xAxis;
	}

	public void setReachable(){
		this.reachable = true;
	}

	public boolean isReachable(){
		return reachable;
	}
}

class Bomb extends CellObject{

	private int power;

	private int count;

	public Bomb(int row, int col, boolean passable, boolean destructible, boolean bombUp, boolean powerUp, boolean danger, int power, int count){
		super(row, col, passable, destructible, bombUp, powerUp, danger);

		this.power = power;
		this.count = count;	
	}

	public int power()
	{
		return power;
	}

	public int count()
	{
		return count;
	}	
}