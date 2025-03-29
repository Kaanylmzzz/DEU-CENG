using System;

class Program
{
    static Random random = new Random();

    static void Main(string[] args)
    {
        //Create the bag
        char[] bag = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\u263A' };
        char joker_letter = '\u263A'; 
        char[] vowels = { 'A', 'E', 'I', 'O', 'U' }; //Vowels in the bag
        int score_multiplier_value = 30;
        bool first_cinko_declared = false;

        // Initialize player cards for both players
        char[][] first_player_card = CreateAndInitializePlayerCard();
        char[][] second_player_card = CreateAndInitializePlayerCard();

        // Retrieve the characters with the minimum and maximum ASCII values for each row
        char[] player1_min_characters = GetCharactersInMinimumASCIIValue(first_player_card); 
        char[] player1_max_characters = GetCharactersInMaximumASCIIValue(first_player_card); 
        char[] player2_min_characters = GetCharactersInMinimumASCIIValue(second_player_card);
        char[] player2_max_characters = GetCharactersInMaximumASCIIValue(second_player_card); 

        // Boolean arrays to track whether the min and max characters have been removed
        bool[] player1_min_characters_removed = new bool[first_player_card.Length];
        bool[] player1_max_characters_removed = new bool[first_player_card.Length];
        bool[] player2_min_characters_removed = new bool[second_player_card.Length];
        bool[] player2_max_characters_removed = new bool[second_player_card.Length];

        // Boolean arrays to track whether extra 100 points have been awarded for each row
        bool[] player1_extra_award_100 = new bool[first_player_card.Length];
        bool[] player2_extra_award_100 = new bool[second_player_card.Length];

        // Define the size of the bag and initialize the players' scores
        int size_of_bag = bag.Length;
        int player1Score = 0, player2Score = 0;
        int number_of_games_played = 0;

        Console.WriteLine("Game Start:");
        PrintEachPlayerCards(first_player_card, player1Score, second_player_card, player2Score); // Print the initial cards and scores of both players

        while (size_of_bag > 0)
        {
            // Randomly select a letter from the bag and decrease the bag size
            char chosen_letter = RandomlySelectsLetterAndRemoveFromTheBag(bag, size_of_bag);
          
            number_of_games_played++;
            size_of_bag--;
            
            Console.WriteLine($"\n\n{number_of_games_played}. Selected Letter: {chosen_letter}");

            // Reset players' round scores to 0 at the start of each round
            int player1_score_for_each_round = 0, player2_score_for_each_round = 0;

            // Process the selected letter for Player 1 and Player 2
            player1_score_for_each_round = ProcessCard("Player 1", score_multiplier_value, first_player_card, chosen_letter, joker_letter, player1_min_characters_removed, player1_max_characters_removed, player1_min_characters, player1_max_characters, vowels);
            player2_score_for_each_round = ProcessCard("Player 2", score_multiplier_value, second_player_card, chosen_letter, joker_letter, player2_min_characters_removed, player2_max_characters_removed, player2_min_characters, player2_max_characters, vowels);

            // Check for extra 100 points for clearing both min and max characters on each row
            player1_score_for_each_round += CheckEachPlayerCardAndAwardExtraPoints("Player 1", player1_extra_award_100, player1_min_characters_removed, player1_max_characters_removed, player1_min_characters, player1_max_characters, 0);
            player1_score_for_each_round += CheckEachPlayerCardAndAwardExtraPoints("Player 1", player1_extra_award_100, player1_min_characters_removed, player1_max_characters_removed, player1_min_characters, player1_max_characters, 1);
            
            player2_score_for_each_round += CheckEachPlayerCardAndAwardExtraPoints("Player 2", player2_extra_award_100, player2_min_characters_removed, player2_max_characters_removed, player2_min_characters, player2_max_characters, 0);
            player2_score_for_each_round += CheckEachPlayerCardAndAwardExtraPoints("Player 2", player2_extra_award_100, player2_min_characters_removed, player2_max_characters_removed, player2_min_characters, player2_max_characters, 1);

            player1Score += player1_score_for_each_round;
            player2Score += player2_score_for_each_round;

            score_multiplier_value--;

            if (player1_score_for_each_round == 0 && player2_score_for_each_round == 0)
                Console.WriteLine("Neither player scored a point");

            if (!first_cinko_declared) // Check for the first çinko and award the bonus
            {
                bool player1ClearedRow = CheckFirstCinkoOccured(first_player_card);
                bool player2ClearedRow = CheckFirstCinkoOccured(second_player_card);

                if (player1ClearedRow && player2ClearedRow)
                {
                    first_cinko_declared = true;
                    Console.WriteLine("First Çinko - Both players deleted a row at the same time. No reward given.");
                }
                else if (player1ClearedRow)
                {
                    first_cinko_declared = true;
                    player1Score += 150;
                    Console.WriteLine("First Çinko - Player 1 wins the prize and gains 150 points");
                }
                else if (player2ClearedRow)
                {
                    first_cinko_declared = true;
                    player2Score += 150;
                    Console.WriteLine("First Çinko - Player 2 wins the prize and gains 150 points");
                }
            }
            // Print the current state of the players' cards and scores
            PrintEachPlayerCards(first_player_card, player1Score, second_player_card, player2Score);
            // End the game
            if (DeletedAllElementsOnTheCard(first_player_card) || DeletedAllElementsOnTheCard(second_player_card)) break;
        }

        string result;
        if (DeletedAllElementsOnTheCard(first_player_card) && DeletedAllElementsOnTheCard(second_player_card))
            result = "Tie! Both players finished their cards at the same time.";
        else if (DeletedAllElementsOnTheCard(first_player_card))
            result = "Player 1 wins the grand prize";
        else
            result = "Player 2 wins the grand prize";

        Console.WriteLine("\nTombala - " + result);
        Console.WriteLine($"\nThe game is over after {number_of_games_played} steps.");
        Console.WriteLine("\nGood Bye!");
        Console.ReadLine();
    }

    private static char[][] CreateAndInitializePlayerCard()
    {
        char[] A_to_M_English_Letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M' };
        char[] N_to_Z_English_Letters = { 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        char[][] tombala_card = new char[2][];

        // Initialize a 2x4 tombola card
        tombala_card[0] = new char[4];
        tombala_card[1] = new char[4];

        // Select 4 random letters for the first row
        for (int i = 0; i < 4; i++)
        {
            int index = random.Next(A_to_M_English_Letters.Length - i);
            char temp = A_to_M_English_Letters[index];

            tombala_card[0][i] = A_to_M_English_Letters[index];
            A_to_M_English_Letters[index] = A_to_M_English_Letters[A_to_M_English_Letters.Length - 1 - i];
            A_to_M_English_Letters[A_to_M_English_Letters.Length - 1 - i] = temp;
        }

        // Select 4 random letters for the second row
        for (int i = 0; i < 4; i++)
        {
            int index = random.Next(N_to_Z_English_Letters.Length - i);
            char temp = N_to_Z_English_Letters[index];

            tombala_card[1][i] = N_to_Z_English_Letters[index];
            N_to_Z_English_Letters[index] = N_to_Z_English_Letters[N_to_Z_English_Letters.Length - 1 - i];
            N_to_Z_English_Letters[N_to_Z_English_Letters.Length - 1 - i] = temp;
        }

        return tombala_card;
    }

    private static char RandomlySelectsLetterAndRemoveFromTheBag(char[] bag, int bagSize)
    {
        // Select the letter at the random index current size of the bag
        int randomIndex = random.Next(bagSize);
        char chosen_letter = bag[randomIndex];
        for (int i = randomIndex; i < bagSize - 1; i++)
            bag[i] = bag[i + 1]; // Shift remaining elements left
        bag[bagSize - 1] = '\0'; // Set the last position in the bag to empty
        return chosen_letter;
    }

    private static int ProcessCard(string playerName, int multiplier, char[][] card, char letter, char joker_letter, bool[] minRemoved, bool[] maxRemoved, char[] minValues, char[] maxValues, char[] Vowels)
    {
        int pointsGained = 0;
        char removedLetter = '\0';
        int row_index = -1, col_index = -1;

        // Process the joker letter
        if (letter == joker_letter)
        {
            removedLetter = '\0';
            // Find the highest ASCII value letter in the card
            for (int i = 0; i < card.Length; i++)
            {
                for (int j = 0; j < card[i].Length; j++)
                {
                    if (card[i][j] != ' ' && card[i][j] > removedLetter)
                    {
                        removedLetter = card[i][j];
                        row_index = i;
                        col_index = j;
                    }
                }
            }
        }
        // Process the normal letter
        else
        {
            for (int i = 0; i < card.Length; i++)
            {
                for (int j = 0; j < card[i].Length; j++)
                {
                    if (card[i][j] == letter) // Check if the current card position matches the letter
                    {
                        removedLetter = letter; 
                        row_index = i;
                        col_index = j;
                        break;
                    }
                }
                if (row_index != -1) break;
            }
        }
        // If a valid position is found update the game state
        if (row_index != -1 && col_index != -1)
        {
            CheckAndUpdateMaxMinSituation(minRemoved, maxRemoved, removedLetter, minValues, maxValues, row_index);
            RemoveLetterFromEachPlayerCard(row_index, col_index, card);
            pointsGained = AccountingScoreForEachPlayer(multiplier, removedLetter, Vowels);
        }
        if (pointsGained > 0)  // If points were gained, print the result
                Console.WriteLine($"{playerName} gained {pointsGained} points");
            return pointsGained;
    }
    
    private static int CheckEachPlayerCardAndAwardExtraPoints(string player_name, bool[] extra_100points_award, bool[] min_characters_removed, bool[] max_characters_removed, char[] min_value_of_characters, char[] max_value_of_characters, int rowIndex)
    {
        int award_extra_point = 0;

        // Check if both min and max characters have been removed and no extra points were awarded yet
        if (!extra_100points_award[rowIndex])
        {
            if (min_characters_removed[rowIndex] && max_characters_removed[rowIndex])
            {
                award_extra_point += 100;
                extra_100points_award[rowIndex] = true;
                Console.WriteLine($"{player_name} gained 100 points");
            }
        }

        return award_extra_point;
    }

    private static void CheckAndUpdateMaxMinSituation(bool[] min_characters_removed, bool[] max_characters_removed, char choosen_letter, char[] min_value_of_characters, char[] max_value_of_characters, int row_index)
    {
        // Check if the chosen letter matches the maximum value for the row
        if (choosen_letter == max_value_of_characters[row_index])
            max_characters_removed[row_index] = true;

        // Check if the chosen letter matches the minimum value for the row
        if (choosen_letter == min_value_of_characters[row_index])
            min_characters_removed[row_index] = true;
    }

    private static void RemoveLetterFromEachPlayerCard(int row_index, int column_index, char[][] player_card)
    {
        // Shift all characters in the row to the left
        for (int k = column_index; k < player_card[row_index].Length - 1; k++)
            player_card[row_index][k] = player_card[row_index][k + 1];
        player_card[row_index][player_card[row_index].Length - 1] = ' ';
    }

    private static int AccountingScoreForEachPlayer(int striking_factor_each_round, char distinct_drawn_letter, char[] Vowels)
    {
        for (int i = 0; i < Vowels.Length; i++) // Check if the chosen letter is a vowel
        {
            if (distinct_drawn_letter == Vowels[i])
                return striking_factor_each_round * 3;
        }
        return striking_factor_each_round * 2; // If not consonant
    }

    private static void PrintEachPlayerCards(char[][] player1Cards, int player1Score, char[][] player2Cards, int player2Score)
    {
        bool space_each_row = false;

        for (int i = 0; i < player1Cards.Length; i++)
        {
            if (i == 1)
                space_each_row = true;
            
            if (i == 0) 
                Console.Write("Player 1: "); // Print Player 1's Cards
            PrintingByArrangingTheCardsAndSpacesProperly(player1Cards[i], space_each_row,10);

            if (i == 0)
                Console.Write("          Player 2: "); // Print Player 2's Cards
            PrintingByArrangingTheCardsAndSpacesProperly(player2Cards[i], space_each_row,20);

            // Print Scores
            if (i == 0)
                Console.WriteLine($"          Player 1 Score: {player1Score,-20}");
            else if (i == 1)
                Console.WriteLine($"          Player 2 Score: {player2Score,-20}");
            else
                Console.WriteLine();
        }
    }

    private static void PrintingByArrangingTheCardsAndSpacesProperly(char[] cards_of_each_row, bool add_appropriate_amount_of_space, int space_amount)
    {
        if (add_appropriate_amount_of_space) // Print each card in the row with required amount of space
        {
            for (int i = 0; i < space_amount; i++)
                Console.Write(" ");
        }
        
        for (int j = 0; j < cards_of_each_row.Length; j++)
            Console.Write(cards_of_each_row[j] + " ");
    }

    private static bool CheckFirstCinkoOccured(char[][] player_card)
    {
        for (int i = 0; i < 2; i++) // Tracks if all letters in the row have been removed in the row
        {
            bool deletedAll = true;
            for (int j = 0; j < 4; j++)
            {
                if (player_card[i][j] != ' ')
                {
                    deletedAll = false;
                    break;
                }
            }
            if (deletedAll)
                return true;
        }
        return false;
    }

    private static char FindCharactersInMaximumASCIIValue(char[] sequence_of_card)
    {
        char temp_max = '\0';
        bool discover_max = false;

        for (int i = 0; i < sequence_of_card.Length; i++)
        {
            if (sequence_of_card[i] != ' ' && (!discover_max || sequence_of_card[i] > temp_max))
            {
                temp_max = sequence_of_card[i];
                discover_max = true;
            }
        }
        // Return the found maximum characte
        return discover_max ? temp_max : '\0';
    }

    private static char FindCharactersInMinimumASCIIValue(char[] sequence_of_card)
    {
        char temp_min = '\0';
        bool discover_min = false;

        for (int i = 0; i < sequence_of_card.Length; i++)
        {
            if (sequence_of_card[i] != ' ' && (!discover_min || sequence_of_card[i] < temp_min))
            {
                temp_min = sequence_of_card[i];
                discover_min = true;
            }
        }
        // Return the found minimum character
        return discover_min ? temp_min : '\0';
    }

    private static char[] GetCharactersInMaximumASCIIValue(char[][] player_card)
    {
        // Create an array to store the maximum values of each row in the card
        char[] maximum_values_of_each_card = new char[player_card.Length];
        for (int i = 0; i < player_card.Length; i++)
        {
            maximum_values_of_each_card[i] = FindCharactersInMaximumASCIIValue(player_card[i]);
        }
        return maximum_values_of_each_card;
    }

    private static char[] GetCharactersInMinimumASCIIValue(char[][] player_card)
    {
        // Create an array to store the minimum values of each row in the card
        char[] minimum_values_of_each_card = new char[player_card.Length];
        for (int i = 0; i < player_card.Length; i++)
        {
            minimum_values_of_each_card[i] = FindCharactersInMinimumASCIIValue(player_card[i]);
        }
        return minimum_values_of_each_card;
    }

    private static bool DeletedAllElementsOnTheCard(char[][] player_card)
    {
        // Check if all elements in the player's card are deleted
        for (int i = 0; i < player_card.Length; i++)
        {
            for (int j = 0; j < player_card[i].Length; j++)
            {
                if (player_card[i][j] != ' ')
                    return false;
            }
        }
        return true;
    }
}
