int total = 0; // Tracks the total score across all games
bool playAgain = true; // Flag to control the game loop

while (playAgain)
{
    Random rnd = new Random();

    // Generate random numbers for the three reels (1 to 6)
    int reel1 = rnd.Next(1, 7);
    int reel2 = rnd.Next(1, 7);
    int reel3 = rnd.Next(1, 7);

    // Generate random color codes for each reel (1 for Red, 2 for Blue, 3 for Green)
    int color_num1 = rnd.Next(1, 4);  
    int color_num2 = rnd.Next(1, 4);  
    int color_num3 = rnd.Next(1, 4);

    // Print each reel with its corresponding color
    PrintWithColor(reel1, color_num1);
    PrintWithColor(reel2, color_num2);
    PrintWithColor(reel3, color_num3);
    Console.WriteLine();

    // Calculate the points for the current spin based on reel numbers and colors
    int point = CalculatePoints(reel1, reel2, reel3, color_num1, color_num2, color_num3);

    // Check if all reels are either even or odd
    bool allEvenOrOdd = (reel1 % 2 == reel2 % 2) && (reel2 % 2 == reel3 % 2);

    // Update the total score with the points from this round
    total += point;
    if (allEvenOrOdd)
        total += 3; // Add a bonus of 3 if all reels are even or odd

    // Display the result of the spin
    if (point > 0)
    {
        Console.WriteLine("Congratulations! ");
        Console.WriteLine("You win $" + point + ".");
        if (allEvenOrOdd)
            Console.WriteLine("You win $3 bonus.");
    }
    else
    {
        Console.WriteLine("You lost :(");
        if (allEvenOrOdd)
            Console.WriteLine("You win $3 bonus.");
    }

    Console.WriteLine();
    string response;

    // Loop to ensure valid input from the player
    while (true)
    {
        Console.WriteLine("Do you want to play again? (Y or N)");
        response = Console.ReadLine().ToUpper(); // Convert input to uppercase for consistent comparison

        if (response == "Y")
        {
            playAgain = true; // Continue the game
            Console.WriteLine("-----------------------------------");
            break;
        }
        else if (response == "N")
        {
            playAgain = false; // End the game
            Console.WriteLine("...................................");
            break;
        }
        else
        {
            Console.WriteLine(); // Inform the user of invalid input and prompt again
            Console.WriteLine("Invalid input! Please enter 'Y' to play again or 'N' to quit.");
        }
    }
    Console.WriteLine();
}

// Display the final message and total score after the game ends
Console.WriteLine("The game is finished!");
Console.WriteLine("Total score obtained is $" + total);

static void PrintWithColor(int reel, int color_num)
{
    Console.ForegroundColor = GetConsoleColor(color_num); // Set the console text color based on the given color number.
    Console.Write(reel + " ");
    Console.ResetColor(); // Reset the console color to default to avoid affecting subsequent console output.
}

static ConsoleColor GetConsoleColor(int color_num)
{
    if (color_num == 1) // If the color number is 1, return Red.
        return ConsoleColor.Red;
    
    else if (color_num == 2) // If the color number is 2, return Blue.
        return ConsoleColor.Blue;

    else // If the color number is neither 1 nor 2, return Green.
        return ConsoleColor.Green;
}

static int CalculatePoints(int r1, int r2, int r3, int c1, int c2, int c3)
{
    bool sameNumbers = r1 == r2 && r2 == r3; // Check if all three reel numbers are the same
    bool sameColors = c1 == c2 && c2 == c3; // Check if all three reels have the same color

    bool allDifferentNumbers = r1 != r2 && r2 != r3 && r1 != r3; // Check if all reel numbers are different
    bool differentColors = c1 != c2 && c2 != c3 && c1 != c3;  // Check if all reel colors are different

    bool consecutiveNumbers = IsConsecutive(r1, r2, r3); // Check if the reel numbers form a consecutive sequenc

    if (sameNumbers)  // Check for same numbers
    {
        if (sameColors)
            return 30;  // Same numbers and same colors
        else if (differentColors)
            return 28; // Same numbers and all different colors
        else
            return 26; // Same numbers but mixed colors
    }
    if (consecutiveNumbers)  // Check for consecutive numbers
    {
        if (sameColors)
            return 20; // Consecutive numbers and same colors
        else if (differentColors)
            return 18; // Consecutive numbers and all different colors
        else
            return 16; // Consecutive numbers but mixed colors
    }
    if (allDifferentNumbers)  // Check for different numbers
    {
        if (sameColors)
            return 10; // All different numbers and same colors
        else if (differentColors)
            return 8; // All different numbers and all different colors
    }
    if (sameColors)  
        return 6; // Same color but not all equal or consecutive
    return 0; // Otherwise, 0 points
}

static bool IsConsecutive(int a, int b, int c)
{
    // Identify min, max, and middle values
    int min = Math.Min(a, Math.Min(b, c));
    int max = Math.Max(a, Math.Max(b, c));
    int middle = a + b + c - min - max;

    // Check if they form consecutive numbers
    return (middle - min == 1) && (max - middle == 1);
}

Console.ReadLine();