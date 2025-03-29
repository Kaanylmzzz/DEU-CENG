import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Queue name = new Queue(1000);
        Queue score = new Queue(1000);

        String filePath = "D:\\highscoretable.txt"; //I created the path of the high score table that we will pull from the txt and had it read.
        HighScoreTableRead(name,score,filePath); //I wrote a function to send the high score table to queues after being read as name and score.


        System.out.println("             ---------------------------");
        System.out.println("             | Welcome to LOTTERY GAME |");
        System.out.println("             ---------------------------");



        while(true){ //I returned a generic loop.
            //Thanks to the general loop, the user will be able to perform game-related operations without the need to run the game over and over.
            System.out.println();
            System.out.println("Please choose what you want to do.");
            System.out.println("1- Play the Lottery Game"); //The part where the game is played.
            System.out.println("2- See the High Score Table"); //The part where the high score appears.
            System.out.println("3- General Information About LOTTERY GAME"); //As an extra, the part where I inform the user about what the game is.
            System.out.println("4- Exit"); //The part where the user can leave the general loop and end the game at any time.
            int s = sc.nextInt();
            while (s != 1 && s != 2 && s != 3 && s != 4) { //Situations where the entered value is not what we want.
                System.out.println("Invalid input!!! Please choose what you want to do.");
                s = sc.nextInt();
            }
            if (s== 4){
                System.out.println("See you in a new game..");
                break;
            }
            boolean playAgain = true; //The part that makes the game play again.

            if (s == 1){

                while (playAgain){ //Loop if the player wants to play the game again at the end of the round.
                    //Score variables.
                    int score1 = 0;
                    int score2 = 0;
                    int count = 1;

                    System.out.print("Enter the number of values on each card between 7 and 10: ");
                    int n = sc.nextInt(); //The amount of the card deck we will create by taking the value from the user.
                    while (n < 7 || n > 10) { ///Situations where the entered value is not what we want.
                        System.out.println("Invalid input!!! Please enter a number between 7 and 10.");
                        n = sc.nextInt();
                    }
                    //Stack and queue variables.
                    Stack player1Card = new Stack(13);
                    Stack player2Card = new Stack(13);
                    Queue bag1 = new Queue(1000);
                    Queue bag2 = new Queue(1000);
                    Queue selected = new Queue(1000);
                    int space = n*2; //The space variable I use to set the space between player and score.
                    Object select;  //Selected card.

                    // Filling the bag1 with values.
                    // Filling the bag2 with random, distinct values.
                    FillingBags(bag1,bag2);

                    //Variable that checks whether 10 is selected on the cards.
                    boolean available1 = false;
                    boolean available2 = false;

                    // Filling the cards with random, distinct values
                    FillingCards(player1Card,n);
                    sortStack(player1Card); //I'm sorting to get the card printed in a sequential manner.
                    available1 = avalible10(player1Card,available1); //Here is the function where I check if the card has 10 in it.

                    FillingCards(player2Card,n);
                    sortStack(player2Card);
                    available2 = avalible10(player2Card,available2);


                    //PRINT PARTS

                    //The part that is printed before the game starts without the value selected in the first part of the game.
                    WritePlayer1andBag1(player1Card,bag1,score1,available1,space);

                    WritePlayer2andBag2(player2Card,selected,score2,available2,space);

                    while (true){
                        //Here's where I got the first piece of zinc needed to complete the first tournament.
                        if (player1Card.size() != n-4 && player2Card.size() != n-4){//Whenever 4 is missing from your card size, it means "birinci çinko".
                            //Extraction of the selected card and score calculations.
                            //player1
                            int s1 = player1Card.size();
                            PlayerCardRemove(player1Card,bag2);
                            score1 = Score(player1Card,s1,score1);

                            // player2
                            int s2 = player2Card.size();
                            PlayerCardRemove(player2Card,bag2);
                            score2 = Score(player2Card,s2,score2);


                            //bag1
                            BagRemove(bag1,bag2);
                            //Suppression of the selected part with the count I keep.
                            System.out.println();
                            System.out.println();
                            System.out.println( count +". selected value: " + bag2.peek());
                            select = bag2.peek();
                            selected.enqueue(bag2.dequeue());
                            count++;
                            if (select.equals(10)){
                                available1 = false;
                                available2 = false;
                            }
                            //Print parts of Player 1 and 2.
                            WritePlayer1andBag1(player1Card,bag1,score1,available1,space);

                            WritePlayer2andBag2(player2Card,selected,score2,available2,space);

                        }
                        else {
                            System.out.println();
                            System.out.println("First tournament is completed.");
                            break;
                        }

                    }


                    //After first tournament
                    //After the end of the first tournament, point increases according to the situation.
                    if (player1Card.size() == n-4 && player2Card.size() == n-4){
                        score1 += 15;
                        score2 += 15;
                    }
                    else if (player1Card.size() == n-4) {
                        score1 += 30;
                    }
                    else
                        score2 += 30;

                    //Print parts of Player 1 and 2.
                    System.out.println();
                    WritePlayer1andBag1(player1Card,bag1,score1,available1,space);

                    WritePlayer2andBag2(player2Card,selected,score2,available2,space);


                    //Second tournament

                    while (true){
                        if (player1Card.size() != 0 && player2Card.size() != 0){ //The start of the second tournament where whoever ends up first actually wins.
                            //Extraction of the selected card and score calculations.
                            //player1
                            int s1 = player1Card.size();
                            PlayerCardRemove(player1Card,bag2);
                            score1 = Score(player1Card,s1,score1);

                            // player2
                            int s2 = player2Card.size();
                            PlayerCardRemove(player2Card,bag2);
                            score2 = Score(player2Card,s2,score2);


                            //bag1
                            BagRemove(bag1,bag2);
                            //Suppression of the selected part with the count I keep.
                            System.out.println();
                            System.out.println();
                            System.out.println( count +". selected value: " + bag2.peek());
                            select = bag2.peek();
                            selected.enqueue(bag2.dequeue());
                            count++;
                            if (select.equals(10)){
                                available1 = false;
                                available2 = false;
                            }
                            //Print parts of Player 1 and 2.
                            WritePlayer1andBag1(player1Card,bag1,score1,available1,space);

                            WritePlayer2andBag2(player2Card,selected,score2,available2,space);

                        }
                        else {
                            System.out.println();
                            System.out.println("Game OVER!");
                            break;
                        }

                    }
                    //After the end of the second tournament, point increases according to the situation.
                    if (player1Card.size() == 0 && player2Card.size() == 0){
                        score1 += 25;
                        score2 += 25;
                    }
                    else if (player1Card.size() == 0) {
                        score1 += 50;
                    }
                    else
                        score2 += 50;

                    System.out.println();
                    if (score1 > score2){
                        System.out.println("Winner: Player1 with " + score1 + " points");
                        System.out.println("What is your name ?"); // Getting the winner's name.
                        String answer = sc.next();
                        //Updating the high score table according to the situation.
                        updateHighScore(name,score,filePath,answer,score1);
                    }

                    else if (score2 > score1) {
                        System.out.println("Winner: Player2 with " + score2 + " points");
                        System.out.println("What is your name ?");
                        String answer = sc.next();
                        updateHighScore(name,score,filePath,answer,score2);

                    }
                    else{
                        System.out.println("Player1 and Player2 have equal score " + score1 + ". Tie.");
                        System.out.println("Player1 What is your name ?");
                        String answer1 = sc.next();
                        updateHighScore(name,score,filePath,answer1,score1);
                        sortQueue(score,name);
                        HighScoreTable(name,score);
                        System.out.println("Player2 What is your name ?");
                        String answer2 = sc.next();
                        updateHighScore(name,score,filePath,answer2,score2);
                    }


                    while (true){ // If requested, displaying the final state of the high score table to the user.
                        System.out.println("Do you want to see High Score Table? (Y/N)");
                        String answer1 = sc.next();
                        if (answer1.equalsIgnoreCase("Y") || answer1.equalsIgnoreCase("Yes")){
                            sortQueue(score,name);
                            HighScoreTablePrint(name,score);
                            break;
                        }
                        else if (answer1.equalsIgnoreCase("N") || answer1.equalsIgnoreCase("No")){
                            break;
                        }else
                            System.out.println("Please write Yes or No (Y/N)");
                    }

                    // If desired, the game can be played again.
                    while (true){
                        System.out.println("Do you want to play again? (Y/N)");
                        String answer2 = sc.next();
                        if (answer2.equalsIgnoreCase("Y") || answer2.equalsIgnoreCase("Yes")){
                            playAgain = true;
                            break;
                        }
                        else if (answer2.equalsIgnoreCase("N") || answer2.equalsIgnoreCase("No")){
                            System.out.println("See you in a new game..");
                            playAgain = false;
                            break;
                        }else
                            System.out.println("Please write Yes or No (Y/N)");
                    }

                }
                if (playAgain == false)
                    break;

            } else if (s == 2) { // The part where the high score table is shown.
                sortQueue(score,name);
                HighScoreTablePrint(name,score);


            } else { // The part where you can get information about the game if requested as an extra.
                System.out.println("\nLOTTERY GAME \n" +
                        "\n-- GAME PLAYING --\n" +
                        "Two players play the game with:\n" +
                        "- cards\n" +
                        "- bags including lottery balls\n" +
                        "Each player selects a lottery card and tries to be the first player that matches randomly selected values with all the values on the card.\n" +
                        "Each card consists of n values, where n is ranged from 7 to 10.\n" +
                        "The cards contain values from a suit of a deck, including A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, and K.\n" +
                        "\n-- SCORE --\n" +
                        "Each player deletes the selected value from his/her stack if it exists and gets 10 score points or losses 5 points.\n" +
                        "The first player that deletes 4 elements from his/her stack completes the first tournament and gets the award score 30. (“birinci çinko”)\n" +
                        "When a player deletes all elements from his/her stack, he/she gets the award score 50.\n" +
                        "If both players delete their last elements at the same time, they share the score.\n" +
                        "\n-- HIGH SCORE TABLE --\n" +
                        "High score table read from txt.\n" +
                        "This table can hold a maximum of 12 values.\n" +
                        "The user can write his name here by entering his name at the end of each match.\n");
            }
        }
    }
    //Function part that is both easy to read and understandable.
    static void HighScoreTableRead(Queue name, Queue score, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { //I provided error checks with try catches. Like file not found exception.
            String line;
            int count = 0; // To keep track of number of entries added.
            while ((line = reader.readLine()) != null && count < 12) { // After checking if the row is empty I have it add max 12 entries.
                String[] parts = line.split("\\s+"); // The situation where the number of spaces is more than one.
                if (parts.length == 2) {
                    name.enqueue(parts[0]); //I added name and score to the queues.
                    score.enqueue(Integer.parseInt(parts[1]));
                    count++;
                } else {
                    System.out.println("Invalid format in line: " + line);
                }
            }
        } catch (FileNotFoundException e) { //I made the error prints.
            System.out.println("File is not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error!!!");
            e.printStackTrace();
        }
    }
    static void FillingCards(Stack player_cards, int n){
        Stack temp_card = new Stack(1000);
        Stack temp = new Stack(1000);

        //I push each value as a number so that it is easy to sort.
        for (int i = 1; i <= 13; i++) {
            temp_card.push(i);
        }

        Random rnd = new Random();
        int stackSize = 13;

        //I perform the filling of cards in a mixed manner with randoms.
        while (n>0){
            int randIndex = rnd.nextInt(stackSize);
            for (int i =0; i<stackSize; i++){
                if (i == randIndex) { //I push when the random incoming value is equal to every i.
                    player_cards.push(temp_card.pop());
                    n--;
                } else {
                    temp.push(temp_card.pop());
                }
            }
            while (!temp.isEmpty()) {
                temp_card.push(temp.pop());
            }
            stackSize--;
        }
    }
    static void FillingBags(Queue bag1,Queue bag2){
        Queue temp = new Queue(1000);
        Queue temp2 = new Queue(1000);

        // I add a direct numbers and face cards on the bags.
        temp.enqueue("A");
        bag1.enqueue("A");

        // numbers
        for (int i = 2; i <= 10; i++) {
            temp.enqueue(i);
            bag1.enqueue(i);
        }

        // face cards
        temp.enqueue("J");
        bag1.enqueue("J");

        temp.enqueue("Q");
        bag1.enqueue("Q");

        temp.enqueue("K");
        bag1.enqueue("K");

        //I perform the filling of bags in a mixed manner with randoms.
        Random rnd = new Random();
        int bagSize = temp.Size();
        int a = 13;
        while (a > 0){
            int randIndex = rnd.nextInt(bagSize);
            for (int i = 0; i < bagSize; i++){
                if (i == randIndex){ // I push when the random incoming value is equal to every i.
                    bag2.enqueue(temp.dequeue());
                    a--;
                }
                else
                    temp2.enqueue(temp.dequeue());
            }
            while (!temp2.isEmpty())
                temp.enqueue(temp2.dequeue());
            bagSize--;
        }
        while (!temp2.isEmpty())
            temp2.dequeue();
    }
    static boolean avalible10(Stack player, boolean avalible){
        // The function I checked if there is 10 in the player card.
        avalible = false; // I provide this control by returning a boolean value.
        Stack temp_playerCard = new Stack(player.size());
        while (!player.isEmpty()){
            if (player.peek().equals(10))
                avalible = true; // If there are 10, it returns true.
            temp_playerCard.push(player.pop());
        }
        while (!temp_playerCard.isEmpty())
            player.push(temp_playerCard.pop());
        return avalible;
    }
    static void  sortStack (Stack player){
        Stack temp_player = new Stack(1000);
        // Here I am sorting through the numbers I push to the stacks.

        while(!player.isEmpty()) {

            int tmp = (int)player.pop();

            while(!temp_player.isEmpty() && (int)temp_player.peek() > tmp) {
                player.push(temp_player.pop());
            }
            temp_player.push(tmp);
        }
        // After finishing the sorting process, I push the face cards that need to be replaced by numbers.
        while (!temp_player.isEmpty()){
            if ((int)temp_player.peek() == 1){
                temp_player.pop();
                player.push("A");
            } else if ((int)temp_player.peek() == 11) {
                temp_player.pop();
                player.push("J");

            }else if ((int)temp_player.peek() == 12) {
                temp_player.pop();
                player.push("Q");

            }else if ((int)temp_player.peek() == 13) {
                temp_player.pop();
                player.push("K");

            }
            else
                player.push(temp_player.pop());
        }
    }
    static void sortQueue(Queue score_queue, Queue name_queue){
        // Here I am sorting the data we read from the txt.
        Stack name = new Stack(name_queue.Size());
        Stack score = new Stack(score_queue.Size());
        Stack tempStack_name = new Stack(score_queue.Size());
        Stack tempStack_score = new Stack(score_queue.Size());

        // First of all, I make it easier to sort these values by transferring them to the stack.
        while (!name_queue.isEmpty()){
            name.push(name_queue.dequeue());
            score.push(score_queue.dequeue());
        }

        while(!score.isEmpty()) {

            int tmp = (int)score.pop();
            String tmps = (String)name.pop();

            while(!tempStack_score.isEmpty() && (int)tempStack_score.peek() > tmp) {
                score.push(tempStack_score.pop());
                name.push(tempStack_name.pop());
            }

            tempStack_score.push(tmp);
            tempStack_name.push(tmps);
        }
        //Then, after sorting these values, I transfer them back to the queues.
        while (!tempStack_score.isEmpty()){
            score_queue.enqueue(tempStack_score.pop());
            name_queue.enqueue(tempStack_name.pop());
        }
    }
    static void PlayerCardRemove(Stack playerCard, Queue bag){
        // Here, with the selected value, if there is that value in the cards, I am removing it.
        Stack temp_playerCard = new Stack(playerCard.size());
        int count1 = 0;
        //Since there can be a maximum of 10 cards, I start from 10 and look at the cases of coming equal with the loop.
        for (int i = 0; i < 10 + count1; i++) {
            if (bag.peek() == playerCard.peek()) { //If they are equal, I remove those parts and fill my stacks again.
                playerCard.pop();
                while (!playerCard.isEmpty()) {
                    temp_playerCard.push(playerCard.pop());
                    break;
                }
                break;
            } else {
                temp_playerCard.push(playerCard.pop());
                count1++; //I increase the count so that the loop is not interrupted every time I do not come.
                if (playerCard.isEmpty()) {
                    break;
                }
            }
        }
        while (!temp_playerCard.isEmpty()) {
            playerCard.push(temp_playerCard.pop());
        }
    }
    static void BagRemove(Queue bag1,Queue bag2){
        // Here, with the selected value, if there is that value in the bag1, I am removing it.
        Queue temp_bag = new Queue(bag1.Size());
        int count1 = 0;
        for (int i =0; i < 10 + count1; i++){
            if (bag2.peek() == bag1.peek()){
                bag1.dequeue(); //If they are equal, I remove those parts and fill my queues again.

                while (!bag1.isEmpty()){
                    temp_bag.enqueue(bag1.dequeue());
                }
                break;
            }
            else{
                temp_bag.enqueue(bag1.dequeue());
                count1++; //I increase the count so that the loop is not interrupted every time I do not come.
                if (bag1.isEmpty()){
                    break;
                }
            }
        }
        while (!temp_bag.isEmpty()){
            bag1.enqueue(temp_bag.dequeue());
        }
    }
    static void BagPrint(Queue selected){
        //I ensure that the queues are printed with the help of loops.
        Queue temp_bag = new Queue(selected.Size());
        while (!selected.isEmpty()){
            System.out.print(selected.peek() + " ");
            temp_bag.enqueue(selected.dequeue());
        }
        while (!temp_bag.isEmpty())
            selected.enqueue(temp_bag.dequeue());
    }
    static void HighScoreTablePrint(Queue name, Queue score){
        Queue temp_bag_name = new Queue(1000);
        Queue temp_bag_score = new Queue(1000);
        //Function that prints to both txt and console of high score table.

        try {
            // Delete existing contents before writing.
            FileWriter myWriter = new FileWriter("D:\\highscoretable.txt",false);
            int c =0;
            while (!name.isEmpty()){
                if(c<12){ //I make it print with a maximum of 12.
                    // With the help of format, I ensure that tx is printed in a neat writing format.
                    myWriter.write(String.format("%-10s %-6s\n",name.peek(), score.peek()));
                    temp_bag_score.enqueue(score.dequeue());
                    temp_bag_name.enqueue(name.dequeue());
                    c++;
                }
                else {
                    name.dequeue();
                    score.dequeue();
                }

            }
            while (!temp_bag_score.isEmpty()){
                name.enqueue(temp_bag_name.dequeue());
                score.enqueue(temp_bag_score.dequeue());
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        int c =0;
        System.out.println("- HIGH SCORE TABLE -");
        while(!name.isEmpty()){
            if(c<12){ ////I make it print with a maximum of 12.
                // With the help of format, I ensure that the console is printed in a neat writing format.
                System.out.println(String.format("%-10s %-6s",name.peek(), score.peek()));
                temp_bag_score.enqueue(score.dequeue());
                temp_bag_name.enqueue(name.dequeue());
                c++;
            }
            else {
                name.dequeue();
                score.dequeue();
            }
        }
        if (name.isEmpty() && c == 0){ // It also informs the user if the txt is empty.
            System.out.println("Let's play some lottery games and create our Scoreboard...");
        }
        while (!temp_bag_score.isEmpty()){
            name.enqueue(temp_bag_name.dequeue());
            score.enqueue(temp_bag_score.dequeue());
        }
    }
    static void HighScoreTable(Queue name, Queue score){
        Queue temp_bag_name = new Queue(1000);
        Queue temp_bag_score = new Queue(1000);
        //Function that prints to both txt and console of high score table.

        try {
            // Delete existing contents before writing.
            FileWriter myWriter = new FileWriter("D:\\highscoretable.txt",false);
            int c =0;
            while (!name.isEmpty()){
                if(c<12){ //I make it print with a maximum of 12.
                    // With the help of format, I ensure that tx is printed in a neat writing format.
                    myWriter.write(String.format("%-10s %-6s\n",name.peek(), score.peek()));
                    temp_bag_score.enqueue(score.dequeue());
                    temp_bag_name.enqueue(name.dequeue());
                    c++;
                }
                else {
                    name.dequeue();
                    score.dequeue();
                }
            }
            while (!temp_bag_score.isEmpty()){
                name.enqueue(temp_bag_name.dequeue());
                score.enqueue(temp_bag_score.dequeue());
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    static void PlayerPrint(Stack playerCard){
        //I ensure that the stacks are printed with the help of loops.
        Stack temp_playerCard = new Stack(playerCard.size());
        while (!playerCard.isEmpty()){
            System.out.print(playerCard.peek() + " ");
            temp_playerCard.push(playerCard.pop());
        }
        while (!temp_playerCard.isEmpty())
            playerCard.push(temp_playerCard.pop());
    }
    static int Score(Stack stack,int size_of_cards,int score){
        // I used a function to calculate users' scores.
        if (stack.size() == size_of_cards) //I understand that if the last version of the stack is taken, if the data is deleted from the stack.
            score = score -5; //I added -5 or +10 depending on the situation.
        else if (stack.size() != size_of_cards) {
            score = score +10;
        }
        return score;
    }
    public static void updateHighScore(Queue queue_name, Queue queue_score, String filePath, String name, int score) {
        Queue temp_name = new Queue(1000);
        Queue temp_score = new Queue(1000);
        File inputFile = new File(filePath);
        try { // I did error checks with try catch.
            int count = 0;
            Scanner scanner = new Scanner(inputFile); // I got the final version of the txt.
            boolean nameFound = false; //I kept a boolean variable for whether Name is in or not.
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                if (parts[0].equalsIgnoreCase(name)) {
                    nameFound = true; //I understood this if the name entered by the user was previously added to the scoreboard.
                    int existingScore = Integer.parseInt(parts[1]);
                    if (score > existingScore) { //I checked whether the last score is greater than the previous score.
                        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(); //I also used methods to edit the entered fog and present it to the user better.
                        for (int i =0; i<count; i++){
                            temp_name.enqueue(queue_name.dequeue());
                            temp_score.enqueue(queue_score.dequeue());
                        }
                        queue_name.dequeue();
                        queue_score.dequeue();
                        while (!temp_score.isEmpty()){
                            queue_name.enqueue(temp_name.dequeue());
                            queue_score.enqueue(temp_score.dequeue());
                        }
                        queue_name.enqueue(capitalized);
                        queue_score.enqueue(score);
                    }
                }
                count++;
            }
            scanner.close();
            if (!nameFound) { //If the name is not found, I added it directly.
                String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(); //I also used methods to edit the entered fog and present it to the user better.
                queue_name.enqueue(capitalized);
                queue_score.enqueue(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SpaceBetweenPlayerandScore(Stack player, boolean avalible, int score, int space){
        //Adding spaces according to console 10 when pushing to make it look nice to the user here
        int s = 0;
        if (avalible == true)
            s = player.size() + player.size();
        else
            s = player.size() + player.size()-1;
        for (int m=0; m<space-s; m++){
            System.out.print(" ");
        }
        System.out.print("        Score: " +score);
    }
    public static void SpaceBetweenScoreandBag(int score){
        int space = 9;
        //Here, too, by adjusting the space additions according to the space occupied by the scores, the user can access the console.
        if (score<0 && score>-10 || score>9 && score<100 )
            for (int m=0; m<space; m++){
                System.out.print(" ");
            }
        else if (score == 0 || (score<10 && score>0) ) {
            for (int m=0; m<space+1; m++){
                System.out.print(" ");
            }
        }
        else {
            for (int m=0; m<space-1; m++){
                System.out.print(" ");
            }
        }
    }
    public static void WritePlayer1andBag1(Stack player, Queue bag, int score, boolean avalible, int space){
        // This is the part where I wrote using my functions before.
        // This part is for player 1.
        System.out.print("Player1: ");
        PlayerPrint(player);

        SpaceBetweenPlayerandScore(player, avalible, score,space);

        SpaceBetweenScoreandBag(score);

        System.out.print("Bag 1: ");
        BagPrint(bag);

    }
    public static void WritePlayer2andBag2(Stack player, Queue bag, int score, boolean avalible, int space){
        // This is the part where I wrote using my functions before.
        // This part is for player 2.
        System.out.println();
        System.out.print("Player2: ");
        PlayerPrint(player);

        SpaceBetweenPlayerandScore(player, avalible, score,space);

        SpaceBetweenScoreandBag(score);

        System.out.print("Bag 2: ");
        BagPrint(bag);
    }

}