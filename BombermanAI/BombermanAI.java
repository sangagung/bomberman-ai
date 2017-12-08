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

	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		String tmp[];

		while (true) {

			// arraylist yang di perbaharui setiap looping
			// reset publik tiap looping
			tembok = new ArrayList<Tembok>();
			bomArr = new ArrayList<Tembok>();
			String input = "";
			int turn = 0;
			int jmlPemain = 0;
                                 
			// Read board state
			// Read until "END" is detected
			while (!input.equals("END")) {
				input = scanner.nextLine();
				String [] inputarray =input.split(" ");
                    if (inputarray[0].equals("TURN")){
                   		turn = Integer.parseInt(inputarray[1]);
                    }
                	else if(inputarray[0].equals("PLAYER")){
                		jmlPemain = Integer.parseInt(inputarray[1]);
                		for(int i = 0 ; i < jmlPemain ; i++){
                			input = scanner.nextLine();
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
                    	board = new String[row][col];

                    	for (int i = 0 ; i < row ; i++){
                    		input = scanner.nextLine();

                    		// membuat inputan menjadi lebih mudah di olah
                    		input = input.replace(" ","").replace("]","").substring(1).replace("["," ") + " ^";
                    		inputarray = input.split(" ");

                    		for(int j = 0 ; j < col ; j++){
                    			input = inputarray[j];

                    			// cek jalan setapak biasa
                    			if(input.equals("")){
                    				board[i][j] = ".";
                    			}

                    			// cek apakah tembok yang bisa di hancurin atau tidak
                    			else if(input.substring(0,1).equals("#") || input.substring(0,1).equals("X")){
                    				if(input.substring(0,1).equals("X")){
                    					tembok.add(new Tembok(""+i , ""+j));
                    				}
                    				board[i][j] = input.substring(0,1);
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
                        						String powerBom = input.substring(1,input.length());
                        						String timeBom = input.substring(input.length());
                        						board[i][j] = "b";
                        						bomArr.add(new Tembok(""+i,""+j));
                        					}else if (awalInput.equals("F")){

                        						// cek apakah itu flare
                        						String timeFlare = input.substring(1);
                        						board[i][j] = "f";

                        					}else if(awalInput.equals("+")){

                        						//cek apakah itu power up
                        						String powerUP = input.substring(1);
                        						board[i][j] = "p";
                        					}else {
                        						//Player
                        						if(input.equals(player.nomorPlayer)){
                        							player.x = ""+i;
                        							player.y = ""+j;
                        						}
                        						board[i][j] = input;
                        					}
                        				}
                    				}
                    			}
                    		}
                		}
                   	}
			}
			if(bomArr.size() == 0){
				status = "AMAN";
				sementara = nearestTembok(tembok);
				moveToTembok(sementara);
			}else{
				sementara = nearestTembok(bomArr);
				kaburFromBom(sementara);
			}
			System.out.println(sementara.getX() + " " + sementara.getY() + status);
			System.out.println(player.x + " " + player.y + status);
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
        	default :
        		if(str != null && !str.isEmpty()) {
                	return new Bomb(PASSABLE, DESTRUCTIBLE, BOMBUP, POWERUP, DANGER, Integer.parseInt(str.substring(1,2)), Integer.parseInt(str.substring(2,3)));
            	}
        		return new CellObject(PASSABLE, DESTRUCTIBLE, BOMBUP, POWERUP, !DANGER);
        }
	}
}