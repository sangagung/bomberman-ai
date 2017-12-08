import java.util.Random;
import java.util.Scanner;

public class LordProtector
{
	private static final boolean PASSABLE = true;

	private static final boolean DESTRUCTIBLE = true;

	private static final boolean BOMBUP = true;

	private static final boolean POWERUP = true;

	private static final boolean DANGER = true;

	public static Player player;

	public static String nickname = "LordProtector";

	private static CellObject[][] board;

	private List<CellObject> wallsDest;

	private List<CellObject> wallsIndest;

	private List<Bomb> bombs;

	public static String status = "SAFE";

	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		CellObject temp = new CellObject(true, true, true, true, true);
		player = new Player("1","1","0");

		while (true) {

			// arraylist yang di perbaharui setiap looping
			// reset publik tiap looping
			bombs = new ArrayList<Bomb>();
			walls = new ArrayList<CellObjects>();
			String input = "";
			int turn = 0;
			int jmlPemain = 0;
                                 
			// Read board state
			// Read until "END" is detected
			while (!input.equals("END")) {
				input = scanner.nexWLine();
				String [] inputarray =input.split(" ");
                    if (inputarray[0].equals("TURN")){
                   		turn = Integer.parseInt(inputarray[1]);
                    }
                	else if(inputarray[0].equals("PLAYER")){
                		jmlPemain = Integer.parseInt(inputarray[1]);
                		for(int i = 0 ; i < jmlPemain ; i++){
                			input = scanner.nexWLine();
                			String [] inputarrayC = input.split(" ");
                			if(inputarrayC[1].equals(nickname)){
                				player.nomorPlayer = inputarrayC[0].substring(1);
                			}
                		}
                	}
                	else if(inputarray[0].equals("BOARD")){

                		// masukin peta ke board dari outputnya
                		int row = Integer.parseInt(inputarray[1]);
                    	int col = Integer.parseInt(inputarray[2]);
                    	board = new CellObject[row][col];

                    	for (int i = 0 ; i < row ; i++){
                    		input = scanner.nexWLine();

                    		// membuat inputan menjadi lebih mudah di olah
                    		input = input.replace(" ","").replace("]","").substring(1).replace("["," ") + " ^";
                    		inputarray = input.split(" ");

                    		for(int j = 0 ; j < col ; j++){
                    			input = inputarray[j];

                    			// cek jalan setapak biasa
                    			if(input.equals("")){
                    				board[i][j] = createCell(input);
                    			}

                    			// cek apakah tembok yang bisa di hancurin atau tidak
                    			else if(input.substring(0,1).equals("#") || input.substring(0,1).equals("X")){
                    				board[i][j] = createCell(input);
                    				if(input.substring(0,1).equals("X")){
                    					wallDest.add(board[i][j]);
                    				}
                    				else{
                    					wallIndest.add(board[i][j]);
                    				}
                    			}else {
                    				// mode isi yang seperti 0;1;2;3
                    				String [] inputarrayB = input.split(";");
                    				if(inputarrayB.length > 0){
                        				for(int k = 0 ; k < inputarrayB.length ; k++){
                        					input = inputarrayB[k];
                        					String awalInput = input.substring(0,1);

                        					// setting untuk bom, ada power dan juga waktu ledakannya
                        					// dan masukkan kedalam array bom
                        					if(awalInput.equals("B")){
                        						board[i][j] = createCell(inputArrayB[k]);
                        						bombs.add((Bomb) board[i][j]);
                        					}else if (awalInput.equals("F")){

                        						// cek apakah itu flare
                        						board[i][j] = createCell(input);

                        					}else if(awalInput.equals("+")){
                        						//cek apakah itu power up
                        						board[i][j] = createCell(input);;
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
			if(bombs.size() == 0){
				status = "AMAN";
				temp = nearestWall(walls);
				moveToWall(temp);
			}else{
				temp = nearestWall((CellObject) bombs);
				fleeFromBomb(temp);
			}
			System.out.println(temp.getX() + " " + temp.getY() + " " + status);
			System.out.println(player.x + " " + player.y + " " + status);
		}
	}

	public static CellObject createCell(String str){
		switch (str) {
        	case "###":
        		return new CellObject(!PASSABLE, !DESTRUCTIBLE, !BOMBUP, !POWERUP, !DANGER);
        	case "XXX":
        		return new CellObject(!PASSABLE, DESTRUCTIBLE, !BOMBUP, !POWERUP, !DANGER);
        	case "+B":
        		return new CellObject(PASSABLE, DESTRUCTIBLE, BOMBUP, !POWERUP, !DANGER);
        	case "+P":
        		return new CellObject(PASSABLE, DESTRUCTIBLE, !BOMBUP, POWERUP, !DANGER);
        	case "F2":
        		return new CellObject(!PASSABLE, !DESTRUCTIBLE, !BOMBUP, !POWERUP, DANGER);
        	case "XBX":
        		return new CellObject(!PASSABLE, DESTRUCTIBLE, BOMBUP, !POWERUP, !DANGER);
        	case "XPX":
        		return new CellObject(!PASSABLE, DESTRUCTIBLE, !BOMBUP, POWERUP, !DANGER);
        	case "":
        		return new CellObject(PASSABLE, DESTRUCTIBLE, BOMBUP, POWERUP, !DANGER);
        	default :
               	return (CellObject) new Bomb(PASSABLE, DESTRUCTIBLE, BOMBUP, POWERUP, DANGER, Integer.parseInt(str.substring(1,2)), Integer.parseInt(str.substring(2,3)));
        }
	}

	// ini bekerja dengan cek dari 2 method dibawahnya
	// di cek move mana yang terbaik dari atas - kiri - bawah - kanan 
	// menggunakan MD untuk menentukan jarak terdekat atau terjauh
	public static void moveToTembok(Tembok tembok){
		// DLS deep 1 LOL + MD
		int up = 9000;
		int right = 9000;
		int left = 9000;
		int down = 9000;
		int xP = Integer.parseInt(player.x);
		int yP = Integer.parseInt(player.y);
		int xW = Integer.parseInt(tembok.x);
		int yW = Integer.parseInt(tembok.y);
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
		if(status.equals("PASANG")){
			move = "DROP";
			System.out.println(">> DROP BOMB");
			return;
		}
		int hasil = Math.min(up,Math.min(down,Math.min(right,left)));
    		if(hasil == 9000){
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

	public static void fleeFromBomb(CellObject wall){
		// DLS deep 1 LOL + MD
		int up = -1;
		int right = -1;
		int left = -1;
		int down = -1;
		int xP = Integer.parseInt(player.x);
		int yP = Integer.parseInt(player.y);
		int xW = Integer.parseInt(wall.getX());
		int yW = Integer.parseInt(wall.getY());
		if(validMove(xP-1,yP)){
			up = Math.abs(xP-1-xW) + Math.abs(yP-yW);
			//if(move.equals("DOWN")) up -=2;
		}
		if(validMove(xP+1,yP)){
			down = Math.abs(xP+1-xW) + Math.abs(yP-yW);
			//if(move.equals("UP")) down -=2;
		}
		if(validMove(xP,yP+1)){
			right = Math.abs(xP-xW) + Math.abs(yP-yW+1);
			//if(move.equals("LEFT")) right -=2;
		}
		if(validMove(xP,yP-1)){
			left = Math.abs(xP-xW) + Math.abs(yP-yW-1);
			//if(move.equals("RIGHT")) left -=2;
		}
		int hasil = Math.max(up,Math.max(down,Math.max(right,left)));
		if(hasil <= 1 && xP != xW && yP != yW){
			move = "STAY";
			System.out.println(">> STAY");
			return;
		}
		if(atas == hasil){
			move = "UP";
			System.out.println(">> MOVE UP");
			return;
		}
		if(kiri == hasil){
			move = "LEFT";
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(bawah == hasil){
			move = "DOWN";
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(kanan == hasil){
			move = "RIGHT";
			System.out.println(">> MOVE RIGHT");
			return;
		}
	}


	public static boolean validMove(int x, int y){
		if(x < 0 || x >= board.length || y < 0 || y >= board[0].length){
			return false;
		} else if(!board[x][y].isPassable() || !board[x][y].isDangerous()){
			return false;
		}
		return true;
	}
}