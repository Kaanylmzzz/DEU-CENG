using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;
using System.Runtime.ConstrainedExecution;
using System.Security.Policy;
using System.Data;
using System.Globalization;

namespace Alg_ödev_3
{
    internal class Program
    {
        static void Main(string[] args)
        {
            string[] corp = File.ReadAllLines("corpus.txt"); //I set how to pull files assigned in debug.
            string[] question = File.ReadAllLines("questions.txt");
            string[] stop_words = {"a", "after", "again", "all", "am", "and", "any", "are", "as", "at", "be", "been", "before", "between", "both", "but", "by", "can", "could", "for",
                                   "from", "had", "has", "he", "her", "here", "him", "in", "into", "I", "is", "it", "me", "my", "of", "on", "our", "she", "so", "such", "than", "that",
                                   "the", "then", "they", "this", "to", "until", "we", "was", "were", "with", "you"};
            string question1 = "What is the result of expression"; //Here I have assigned the question that is necessary for me to do rule 1.
            string question2 = "What are the top10 words in the pattern"; //Here I have assigned the question that is necessary for me to do rule 2.
            char[] separatingStrings = { '=', '?' };
            double result = 0;
            double sum = 0;

            for (int i = 0; i < question.Length; i++) //I started my generic loop with the length question.
            {
                if (question[i].Contains(question1)) //Using the Contains method, I figured out whether the os contains the or not.
                {
                    string[] words = question[i].Split(' ');
                    string[] words2 = words[8].Split(separatingStrings); //Since I always know that the 8th expression is equal to x or any other variable, I split x to get its value.

                    if (question[i].Contains("+")) //One by one, I first checked which process it contains.
                    {
                        string[] words1 = words[6].Split('+'); //Since my 6th term is the transactional one, I split it according to that transaction.
                        for (int j = 0; j < words1.Length; j++)
                        {
                            string[] words11 = words1[j].Split(Convert.ToChar(words2[0]));

                            for (int k = 0; k < words11.Length - 1; k = k + 2) //Here, too, I created a loop and made the process with the math library and equalized it to my sum.
                            {
                                result = Convert.ToInt32(words11[k]) * Math.Pow(Convert.ToInt32(words2[1]), Convert.ToInt32(words11[k + 1]));
                                sum = result + sum;
                            }
                        }
                        Console.WriteLine("The result is " + sum); //Here I also printed my sum and did the same for other operations.
                    }
                    else if (question[i].Contains("-"))
                    {
                        string[] words1 = words[6].Split('-');
                        for (int j = 0; j < words1.Length; j++)
                        {
                            string[] words11 = words1[j].Split(Convert.ToChar(words2[0]));

                            for (int k = 0; k < words11.Length - 1; k = k + 2)
                            {
                                result = Convert.ToInt32(words11[k]) * Math.Pow(Convert.ToInt32(words2[1]), Convert.ToInt32(words11[k + 1]));
                                sum = sum - result;
                            }
                        }
                        Console.WriteLine("The result is " + sum);
                    }
                    else if (question[i].Contains("*"))
                    {
                        string[] words1 = words[6].Split('*');
                        sum = 1;
                        for (int j = 0; j < words1.Length; j++)
                        {
                            string[] words11 = words1[j].Split(Convert.ToChar(words2[0]));

                            for (int k = 0; k < words11.Length - 1; k = k + 2)
                            {
                                result = Convert.ToInt32(words11[k]) * Math.Pow(Convert.ToInt32(words2[1]), Convert.ToInt32(words11[k + 1]));
                                sum = sum * result;
                            }
                        }
                        Console.WriteLine("The result is " + sum);
                    }
                    else if (question[i].Contains("/"))
                    {
                        string[] words1 = words[6].Split('/');
                        sum = 1;
                        for (int j = 0; j < words1.Length; j++)
                        {
                            string[] words11 = words1[j].Split(Convert.ToChar(words2[0]));

                            for (int k = 0; k < words11.Length - 1; k = k + 2)
                            {
                                result = Convert.ToInt32(words11[k]) * Math.Pow(Convert.ToInt32(words2[1]), Convert.ToInt32(words11[k + 1]));
                                if (j == 0)
                                    sum = result;
                                else
                                    sum = sum / result;
                            }
                        }
                        Console.WriteLine("The result is " + sum);
                    }
                    Console.WriteLine();
                }
                else if (question[i].Contains(question2)) //Using the Contains method, I figured out whether the os contains the or not.
                {
                    string[] pattern = new string[100]; //I created a string to print the pattern.
                    string[] words1 = question[i].Split(' ');
                    string[] words2 = words1[8].Split(separatingStrings);
                    char[] chars = new char[words2[0].Length];
                    chars = words2[0].ToLower().ToCharArray(); //Here, I lowered the searched word or the - expression and dropped it into a char array to check if there are letters in it one by one.
                    char[] chars1 = new char[words2[0].Length];

                    int count = 0;
                    for (int j = 0; j < corp.Length; j++)
                    {
                        string[] words = corp[j].ToLower().Replace(",", "").Replace(";", "").Replace(".", "").Replace("?", "").Split(' ');

                        for (int k = 0; k < words.Length; k++)
                        {
                            if (words2[0].Length == words[k].Length && pattern.Contains(words[k]) == false && count < 10) //Here I looked at the equality of the length of the words, not to print the same word more than once and to print it no more than 10 times.
                            {
                                chars1 = words[k].ToCharArray();

                                for (int s = 0; s < words2[0].Length; s++)
                                {
                                    if (chars[s] != '-' && chars[s] != chars1[s])
                                        break;

                                    else if (s == words2[0].Length - 1)
                                    {
                                        pattern[count] = words[k]; //Here, I performed the printing process by making my pattern assignments.
                                        Console.Write(pattern[count] + " ");
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                    Console.WriteLine();
                    Console.WriteLine();
                }
                else
                {
                    bool flag = true;
                    int temp = 0;
                    string[,] crp1 = new string[corp.Length, 2];
                    question[i] = question[i].Replace("?", "").Replace(",", "").Replace(";", "").Replace(".", "");
                    string[] words1 = question[i].Split(' ');
                    int crp = 0; //I managed to control the number of words in the corpus.

                    for (int j = 0; j < corp.Length; j++)
                    {
                        string corpus = corp[j];
                        string[] words2 = corp[j].Replace(",", "").Replace(";", "").Replace(".", "").Replace("?", "").Split(' ');

                        crp1[j, 0] = corpus; //Assigning my texts in the corpus to a double dimensional array.

                        for (int k = 0; k < words1.Length; k++)
                        {
                            for (int m = 0; m < words2.Length; m++)
                            {
                                if (words1[k] == words2[m]) //here I compared the equality of words in corpus and questions.
                                {
                                    for (int n = 0; n < stop_words.Length; n++)
                                    {
                                        if (words1[k] == stop_words[n]) //If it is equal to one of the stop words, I broke the loop and made it pass to the new word.                   
                                            break;
                                        
                                        else if (n == stop_words.Length - 1) //If there is no equality, I increased the number of equal words in the corpus by one.
                                        {
                                            crp++;
                                            flag = false;
                                        }
                                    }
                                }
                                if (!flag)
                                {
                                    flag = true;
                                    break;
                                }
                            }
                            if (temp <= crp && crp >= 2)
                            {
                                temp = crp;
                                crp1[j, 1] = Convert.ToString(crp); //If it satisfies the 3rd rule, I printed the number of words it matched next to the text that satisfies it.
                            }
                        }
                        crp = 0;
                    }
                    for (int j = 0; j < crp1.GetLength(0); j++)
                    {
                        if (crp1[j, 1] == Convert.ToString(temp)) //Here too, I ensured that all texts equal to the tempin max value I found at the top would be printed.
                            Console.WriteLine(crp1[j, 0]);
                        
                        else if (temp < 2 && question[i] != "")
                        {
                            Console.WriteLine("No answer.");
                            break;
                        }
                    }
                    Console.WriteLine();
                }
            }
            Console.ReadLine();
        }
    }
}
