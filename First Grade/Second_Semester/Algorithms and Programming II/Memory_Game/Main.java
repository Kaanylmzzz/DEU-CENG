import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    //Coloring parts.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static void main(String[] args) {
        //Sir, txt file names are defined in 3 places. You can change it from there as you wish, written in the game class and as a method in sll.
        Game(); //I play the game by calling only function inside main.
    }
    public static void Game(){
        //The part where I create my SLLs.
        SingleLinkList SLL3 = new SingleLinkList();
        SingleLinkList SLL4 = new SingleLinkList();
        SingleLinkList TempSLL3 = new SingleLinkList();
        SingleLinkList TempSLL4 = new SingleLinkList();
        String filePath1 = "D:\\highscoretable.txt";
        HighScoreTableRead(filePath1,TempSLL3,TempSLL4,SLL3,SLL4);

        System.out.println(CYAN_BOLD+"             --------------------------");
        System.out.println("             |" +BLUE_BOLD_BRIGHT+ " Welcome to MEMORY GAME " +CYAN_BOLD + "|");
        System.out.println("             --------------------------");
        //I created a menu and presented certain options to the user.
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println(CYAN_BOLD + "Please choose what you want to do.");
            System.out.println(CYAN_BOLD_BRIGHT + "1- Play the Memory Game"); //The part where the game is played.
            System.out.println("2- See the High Score Table"); //The part where the high score appears.
            System.out.println("3- General Information About MEMORY GAME"); //As an extra, the part where I inform the user about what the game is.
            System.out.println("4- Exit" + ANSI_RESET); //The part where the user can leave the general loop and end the game at any time.
            int s = sc.nextInt();
            while (s != 1 && s != 2 && s != 3 && s != 4) { //Situations where the entered value is not what we want.
                System.out.println(RED_BOLD_BRIGHT +"Invalid input!!! Please choose what you want to do.");
                s = sc.nextInt();
            }
            if (s== 4){
                System.out.println(PURPLE_BRIGHT +"See you in a new game..");
                break;
            }
            //I added the play again part as an extra
            boolean playAgain = true;

            if (s== 1){
                while (playAgain){
                    SingleLinkList AnimalSLL = new SingleLinkList();
                    SingleLinkList TempAnimalSLL = new SingleLinkList();
                    SingleLinkList SLL1 = new SingleLinkList();
                    SingleLinkList TempSLL1 = new SingleLinkList();
                    SingleLinkList SLL2 = new SingleLinkList();
                    int score = 0;


                    String filePath = "D:\\animals.txt";
                    AnimalstxtRead(filePath,AnimalSLL,TempAnimalSLL); //Animals Txt read part.
                    //The part where I get how many memory cards they want from the user at first.
                    System.out.println(YELLOW_BRIGHT +"Please write how many cards you would like to have at the beginning.");
                    int n = sc.nextInt();
                    while (n > AnimalSLL.size()) { //Situations where the entered value is not what we want.
                        System.out.println(RED_BOLD +"You have entered more than the maximum number of memory cards !!");
                        System.out.println(GREEN_BRIGHT +"Please enter a new value");
                        n = sc.nextInt();
                    }
                    //The part for the cards to look like the example given to the user.
                    Display(n,AnimalSLL,TempAnimalSLL,SLL1,TempSLL1,SLL2);
                    GamePlay(SLL1,SLL2,SLL3,SLL4,score,n,sc); //The part of the game play.

                    SeeHighScoreTable(SLL3,SLL4,sc); //The part where the high score table is shown to the user.
                    System.out.println();
                    playAgain = PlayAgain(playAgain,sc); //The part if the user wants to play again

                }
                if (playAgain == false)
                    break;
            } else if (s == 2) {
                System.out.println(YELLOW_BRIGHT);
                SLL4.displayScores(SLL3,SLL4);
                System.out.println(ANSI_RESET);
            } else { //The part where I give extra information about the game.
                System.out.println(BLUE_BOLD_BRIGHT + "\n-- The Beginning of the Game --\n" +CYAN_BOLD +
                        "There are two single linked-lists (SLL1 and SLL2) with n in size. The second SLL contains the matches of tiles in the first SLL.\n" +
                        "At the beginning of the game, take the value of n from the user.\n" +
                        "The game boards (SLL1 and SLL2) must be randomly filled with distinct pairs. You should randomly select n animals from animalSLL to fill game boards.\n" +
                        "Each element in a SLL should be different from the others. For example, a SLL doesn’t contain two dogs.\n" +
                        BLUE_BOLD_BRIGHT +"\n-- Game Playing --\n" +CYAN_BOLD +
                        "Playing is very simple the computer turns over two tiles randomly, one tile from the first SLL and one tile from the second SLL.\n"+
                        "If they are identical, the program deletes them from the game boards (SLLs), if not, it tries again.\n" +
                        BLUE_BOLD_BRIGHT +"\n-- The End of the Game --\n" +CYAN_BOLD +
                        "When all pairs are identified (when all tiles are deleted from the game boards (SLLs)), the game will be over.\n" +
                        "The program must display all steps until the game is over.\n" +
                        "The game can be played again if desired.\n" +
                        BLUE_BOLD_BRIGHT +"\n-- Scoring --\n" +CYAN_BOLD +
                        "The scoring principle is as follows:\n" +
                        "- Each time the computer makes a successful match, the score should be increased by 20 points.\n"+
                        "- If the computer fails to match, the score should be decreased by 1 point.\n" +
                        BLUE_BOLD_BRIGHT +"\n-- HIGH SCORE TABLE --\n" +CYAN_BOLD +
                        "High score table read from txt.\n" +
                        "This table can hold a maximum of 12 values.\n" +
                        "At the end of the game, the name taken from the user is added to the high score table.\n" + ANSI_RESET);
            }

        }
    }
    public static void GamePlay(SingleLinkList SLL1,SingleLinkList SLL2,SingleLinkList SLL3,SingleLinkList SLL4,int score,int n, Scanner sc){
        Random rnd = new Random();
        int counter = 1;
        boolean delete = false;
        int space = 30;
        int space1 =0;
        space1 = Space(SLL1,space1) + n + 4;


        while (SLL1.size() != 0){
            //Here, I had randoms assigned according to the card.
            int a = rnd.nextInt(SLL1.size()) + 1;
            int b = rnd.nextInt(SLL1.size()) + 1;
            System.out.println(); // I gave a nice screen output to the user by leaving a certain number of spaces according to the value of each random result.
            if ((a>=10 && b<10) || (a<10 && b>=10)){
                System.out.println(WHITE_BOLD + "Randomly generated numbers: " + ANSI_RESET+ a + " " + b + padLeft(GREEN_BOLD+"Step= ",space1-1) + GREEN_BOLD_BRIGHT+counter);
            } else if (a>=10 && b>=10) {
                System.out.println(WHITE_BOLD +"Randomly generated numbers: " + ANSI_RESET+a + " " + b + padLeft(GREEN_BOLD+"Step= ",space1-2) + GREEN_BOLD_BRIGHT+counter);
            } else
                System.out.println(WHITE_BOLD +"Randomly generated numbers: " + ANSI_RESET+a + " " + b + padLeft(GREEN_BOLD+"Step= ",space1) + GREEN_BOLD_BRIGHT+counter);

            counter++;


            Node temp1 = SLL1.getHead(); // Get the head node of AnimalSLL
            for (int i = 1; i <= a; i++){
                if (temp1 == null)
                    System.out.println("List is empty.");
                else if (a == i) {
                    temp1.getData();
                    break; // Stop the loop when a match is found
                } else {
                    temp1 = temp1.getLink(); // Move to the next node
                }
            }

            Node temp2 = SLL2.getHead(); // Get the head node of AnimalSLL
            for (int i = 1; i <= b; i++){
                if (temp2 == null)
                    System.out.println("List is empty.");
                else if (b == i) {
                    temp2.getData();
                    break; // Stop the loop when a match is found
                } else {
                    temp2 = temp2.getLink(); // Move to the next node
                }
            }
            if (temp1.getData().equals(temp2.getData())){ //Thanks to the data I found above, if they are equal, I performed the deletion.
                if(SLL1.size() != 1){
                    SLL1.delete(temp1.getData());
                    SLL2.delete(temp1.getData());
                    delete = true; //I kept the length of the deleted data and increased it accordingly to the number of spaces
                    String del = (String) temp1.getData();
                    space = space + del.length() + 1;
                }
                else {
                    System.out.print(CYAN_BOLD +"SLL1: " + BLUE_BRIGHT);
                    delete = true;
                    score = Score(delete,score); //I had the score calculated in a function.
                    String del = (String) temp1.getData(); //I kept the length of the deleted data and increased it accordingly to the number of spaces
                    space = space + del.length() + 1;
                    System.out.print(padLeft(PURPLE_BOLD +"Score: ",space) + PURPLE_BOLD_BRIGHT +score + ANSI_RESET);
                    System.out.println();
                    System.out.print(CYAN_BOLD +"SLL2: " + BLUE_BRIGHT);
                    System.out.println();
                    break;
                }
            }


            System.out.print(CYAN_BOLD +"SLL1: " + BLUE_BRIGHT);
            SLL1.display();
            score = Score(delete,score);
            delete = false;
            System.out.print(padLeft(PURPLE_BOLD +"Score: ",space) + PURPLE_BOLD_BRIGHT +score);
            System.out.println();
            System.out.print(CYAN_BOLD +"SLL2: " + BLUE_BRIGHT);
            SLL2.display();
            System.out.println(ANSI_RESET);
        }
        System.out.println( RED_BOLD_BRIGHT + "The game is over."); //Game Over..
        System.out.println();

        System.out.println(GREEN_BRIGHT +"What is your name ?" + ANSI_RESET); // Getting the player's name.
        String answer = sc.next();
        SLL4.update_highscore(score,answer,SLL3,SLL4);
    }
    static boolean PlayAgain(boolean playAgain, Scanner sc){
        while (true){ //Play again part.
            System.out.println(BLUE_BOLD_BRIGHT + "Do you want to play again? (Yes/No or Y/N)");
            String answer2 = sc.next();
            if (answer2.equalsIgnoreCase("Y") || answer2.equalsIgnoreCase("Yes")){
                return playAgain = true;
            }
            else if (answer2.equalsIgnoreCase("N") || answer2.equalsIgnoreCase("No")){
                System.out.println(PURPLE_BOLD_BRIGHT + "See you in a new game..");
                return playAgain = false;
            }else
                System.out.println(RED_BRIGHT+"Please write Yes or No (Y/N)" + ANSI_RESET);
        }
    }
    public static void SeeHighScoreTable(SingleLinkList SLL3, SingleLinkList SLL4,Scanner sc){
        while (true){ // If requested, displaying the final state of the high score table to the user.
            System.out.println(YELLOW_BOLD + "Do you want to see High Score Table? (Y/N)" + YELLOW_BRIGHT);
            String answer1 = sc.next();
            if (answer1.equalsIgnoreCase("Y") || answer1.equalsIgnoreCase("Yes")){
                SLL4.displayScores(SLL3,SLL4);
                break;
            }
            else if (answer1.equalsIgnoreCase("N") || answer1.equalsIgnoreCase("No")){
                break;
            }else
                System.out.println(RED_BRIGHT +"Please write Yes or No (Y/N)" + ANSI_RESET);
        }
    }
    public static void Display(int n , SingleLinkList AnimalSLL,SingleLinkList TempAnimalSLL, SingleLinkList SLL1, SingleLinkList TempSLL1, SingleLinkList SLL2){
        RandomlyFillCard(TempAnimalSLL,SLL1,TempSLL1,n);
        //Print to the console.
        System.out.print(YELLOW_BOLD +"Animal SLL: " + YELLOW_BRIGHT);
        AnimalSLL.display();
        System.out.println();
        System.out.println();

        System.out.print(CYAN_BOLD +"SLL1: " + BLUE_BRIGHT);
        SLL1.display();
        System.out.print(padLeft(PURPLE_BOLD +"Score: ",30) + PURPLE_BRIGHT + 0);
        RandomlyFillOtherCard(TempSLL1,SLL2,n);
        System.out.println();
        System.out.print(CYAN_BOLD +"SLL2: " + BLUE_BRIGHT);
        SLL2.display();
        System.out.println(ANSI_RESET);
    }
    public static void AnimalstxtRead(String filePath,SingleLinkList AnimalSLL, SingleLinkList TempAnimalSLL){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { //I provided error checks with try catches. Like file not found exception.
            String line;
            while ((line = reader.readLine()) != null) {
                AnimalSLL.add(line);
                TempAnimalSLL.add(line);
            }
        } catch (FileNotFoundException e) { //I made the error prints.
            System.out.println("File is not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error!!!");
            e.printStackTrace();
        }
    }
    public static void HighScoreTableRead(String filePath,SingleLinkList TempSLL3, SingleLinkList TempSLL4, SingleLinkList SLL3, SingleLinkList SLL4){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { //I provided error checks with try catches. Like file not found exception.
            String line;
            int count = 0; // To keep track of number of entries added.
            while ((line = reader.readLine()) != null && count < 12) { // After checking if the row is empty I have it add max 12 entries..
                String[] parts = line.split("\\s+"); // The situation where the number of spaces is more than one..
                if (parts.length == 2) {
                    TempSLL3.add(parts[0]);
                    TempSLL4.add(Integer.parseInt(parts[1]));
                    count++;
                } else
                    System.out.println("Invalid format in line: " + line);
            }
            SLL4.sorted_add(TempSLL4,SLL4,TempSLL3,SLL3);
        } catch (FileNotFoundException e) { //I made the error prints.
            System.out.println("File is not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error!!!");
            e.printStackTrace();
        }
    }
    public static void RandomlyFillCard(SingleLinkList TempAnimalSLL, SingleLinkList SLL1, SingleLinkList TempSSL1, int n){
        Random rnd = new Random();
        int size = TempAnimalSLL.size();

        while (n != 0){
            int a = rnd.nextInt(TempAnimalSLL.size()) + 1;
            Node temp = TempAnimalSLL.getHead(); // Get the head node of AnimalSLL
            for (int i = 1; i <= size; i++){
                if (temp == null)
                    System.out.println("List is empty.");
                else if (a == i) {
                    SLL1.add(temp.getData());
                    TempSSL1.add(temp.getData());
                    if (n != 1){
                        TempAnimalSLL.delete(temp.getData());
                    }
                    size--;
                    break; // Stop the loop when a match is found
                } else {
                    temp = temp.getLink(); // Move to the next node
                }
            }
            n--;
        }
    }
    public static void RandomlyFillOtherCard(SingleLinkList TempSSL1,SingleLinkList SLL2,int n){
        Random rnd = new Random();
        int size = TempSSL1.size();
        //Filling the next according to the first filled card.

        while (n != 0){
            int a = rnd.nextInt(TempSSL1.size()) + 1;
            Node temp = TempSSL1.getHead(); // Get the head node of TempSLL
            for (int i = 1; i <= size; i++){
                if (temp == null)
                    System.out.println("List is empty.");
                else if (a == i) {
                    SLL2.add(temp.getData());
                    if (n != 1){
                        TempSSL1.delete(temp.getData());
                    }
                    size--;
                    break; // Stop the loop when a match is found
                } else {
                    temp = temp.getLink(); // Move to the next node
                }
            }
            n--;
        }

    }
    public static int Score(boolean delete,int score){
        // I used a function to calculate user's scores.
        if (!delete) //I understand that if the last version of the stack is taken, if the data is deleted from the stack.
            score = score -1; //I added -5 or +10 depending on the situation.
        else
            score = score +20;

        return score;
    }
    public static int Space(SingleLinkList SLL1,int length){
        //The part I made sure to be properly spaced.
        Node temp = SLL1.getHead();
        while (temp != null){
            String lngt = (String) temp.getData();
            length = length + lngt.length();
            temp = temp.getLink();
        }
        return length;
    }
    public static String padLeft(String s, int n) { //Space to left.
        return String.format("%" + n + "s", s);
    }

}