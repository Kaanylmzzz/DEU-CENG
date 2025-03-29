using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Runtime.InteropServices;

namespace Alg_Ödev_2
{
    internal class Program
    {
        static void Main(string[] args)
        {
            int[] number = new int[40];
            int[] grade = new int[40];
            int[] dpgrade = new int[40];
            string[,] c = new string[40, 3];
            string[] c1 = new string[40];
            string[] c2 = new string[40];
            string[] c3 = new string[40];
            int grd = 0;
            int temp;
            char[] key = { 'A', 'B', 'D', 'C', 'C', 'C', 'A', 'D', 'B', 'C', 'D', 'B', 'A', 'C', 'B', 'A', 'C', 'D', 'C', 'D', 'A', 'D', 'B', 'C', 'D' };
            string[] cand = File.ReadAllLines("candidates.txt"); //I set how to pull files assigned in debug.
            string[] dept = File.ReadAllLines("departments.txt");
            int[,] no_grade = new int[40, 40];
            string[,] dpt = new string[10, 11];

            if (cand.Length <= 40)
            { 
                Console.WriteLine("Number               Name & Surname              Grade");                
                for (int i = 0; i < cand.Length; i++)
                {
                    string[] words1 = cand[i].Split(',');

                    if (words1.Length <= 31)
                    {
                        for (int j = 6; j < words1.Length; j++)
                        {
                            if (key[j - 6].ToString() == words1[j])
                                grd += 4;

                            else if (words1[j] == " ")
                            { }
                            else
                                grd -= 3;
                        }
                        grade[i] = grd;
                        grd = 0;
                        //Here, I made the data I entered appear repetitively and neatly by using repeat.
                        Console.WriteLine(words1[0] + String.Concat(Enumerable.Repeat(" ", 21 - words1[0].Length)) + words1[1] + String.Concat(Enumerable.Repeat(" ", 28 - words1[1].Length)) + grade[i]);
                        number[i] = Convert.ToInt32(words1[0]);
                        dpgrade[i] = Convert.ToInt32(words1[2]);
                        
                        c1[i] = words1[3];
                        c2[i] = words1[4];
                        c3[i] = words1[5];
                    }
                    else
                        Console.WriteLine("The number of questions should not be more than 25.");                                             
                }
                int counter = 0;
                for (int i = 0; i < cand.Length; i++)
                {
                    if (grade[i] < 40)
                        counter++;
                }

                for (int i = 0; i < cand.Length; i++)
                {
                    c[i, 0] = c1[i];
                    c[i, 1] = c2[i];
                    c[i, 2] = c3[i];
                    
                }
                for (int i = 0; i < cand.Length; i++)
                {
                    if (grade[i] >= 40)
                    {
                        no_grade[i, 0] = number[i]; //Here I used the number, grade and diploma score to make my comparisons by assigning them to a double dimensional array.
                        no_grade[i, 1] = grade[i];
                        no_grade[i, 2] = dpgrade[i];
                    }
                }
                
                int temp1;
                int temp2;
                for (int s = 0; s < cand.Length - 1; s++)
                {
                    for (int j = s; j <= cand.Length; j++)
                    {
                        if (no_grade[s, 1] < no_grade[j, 1])  //Firstly, I ranked the students with equal grades according to their diploma scores.
                        {
                            temp = no_grade[j, 1];
                            temp1 = no_grade[j, 0];
                            temp2 = no_grade[j, 2];
                            no_grade[j, 2] = no_grade[s, 2];
                            no_grade[j, 1] = no_grade[s, 1];
                            no_grade[j, 0] = no_grade[s, 0];
                            no_grade[s, 2] = temp2;
                            no_grade[s, 1] = temp;
                            no_grade[s, 0] = temp1;
                        }
                        else if (no_grade[s, 1] == no_grade[j, 1])
                        {
                            if (no_grade[s, 2] < no_grade[j, 2])
                            {
                                temp = no_grade[j, 1];
                                temp1 = no_grade[j, 0];
                                temp2 = no_grade[j, 2];
                                no_grade[j, 2] = no_grade[s, 2];
                                no_grade[j, 1] = no_grade[s, 1];
                                no_grade[j, 0] = no_grade[s, 0];
                                no_grade[s, 2] = temp2;
                                no_grade[s, 1] = temp;
                                no_grade[s, 0] = temp1;
                            }
                        }
                    }
                }               
                if (dept.Length <= 10)
                {
                    for (int i = 0; i < dept.Length; i++)
                    {
                        string[] words2 = dept[i].Split(',');
                        dpt[i, 2] = words2[2];
                        if (Convert.ToInt32(dpt[i, 2]) < 8)
                        {    
                            dpt[i, 1] = words2[1];
                            dpt[i, 0] = words2[0];
                        }
                        else
                        {
                            Console.WriteLine("The maximum quota for any department is 8.");
                            Console.ReadLine();
                            Environment.Exit(0);
                        }
                    }
                }
                else
                {
                    Console.WriteLine("The number of departments is dynamic maximum 10.");
                    Console.ReadLine();
                    Environment.Exit(0);
                }
               
                for (int i = 0; i < cand.Length - counter; i++)
                {
                    for (int j = 0; j < cand.Length; j++)
                    {
                        if (no_grade[i, 0] == number[j]) //I used the order I made here by writing nested fores for assignment.
                        {
                            for (int d = 0; d < dept.Length; d++)
                            {                              
                                if (dpt[d, 0] == c[j, 0])
                                {
                                    if (dpt[d, 0] == c[j, 0] && Convert.ToInt32(dpt[d, 2]) != 0 && c[j, 0] != " ")
                                    {
                                        dpt[d, (11 - Convert.ToInt32(dpt[d, 2]))] = Convert.ToString(no_grade[i, 0]);
                                        dpt[d, 2] = Convert.ToString(Convert.ToInt32(dpt[d, 2]) - 1);
                                        break;
                                    }
                                    else
                                    {
                                        for (int k = 0; k < dept.Length; k++)
                                        {
                                            if (dpt[k, 0] == c[j, 1])
                                            {
                                                if (dpt[k, 0] == c[j, 1] && Convert.ToInt32(dpt[k, 2]) != 0 && c[j, 1] != " ")
                                                {
                                                    dpt[k, (11 - Convert.ToInt32(dpt[k, 2]))] = Convert.ToString(no_grade[i, 0]);
                                                    dpt[k, 2] = Convert.ToString(Convert.ToInt32(dpt[k, 2]) - 1);
                                                    break;
                                                }
                                                else
                                                {
                                                    for (int l = 0; l < dpt.Length; l++)
                                                    {
                                                        if (dpt[l, 0] == c[j, 2])
                                                        {
                                                            if (dpt[l, 0] == c[j, 2] && Convert.ToInt32(dpt[l, 2]) != 0 && c[j, 2] != " ")
                                                            {
                                                                dpt[l, (11 - Convert.ToInt32(dpt[l, 2]))] = Convert.ToString(no_grade[i, 0]);
                                                                dpt[l, 2] = Convert.ToString(Convert.ToInt32(dpt[l, 2]) - 1);
                                                                break;
                                                            }
                                                        }
                                                    }                                                   
                                                }
                                            }                                            
                                        }
                                    }                                  
                                }
                            }
                        }                                                    
                    }
                }
                Console.WriteLine();
                Console.WriteLine("No                   Department                  Students");
                for (int i = 0; i < dept.Length; i++)
                {
                    string[] words2 = dept[i].Split(',');
                    if (dept.Length <= 10 && Convert.ToInt32(dpt[i, 2]) <= 8)
                    {
                        int counter1 = 0;
                        Console.Write(dpt[i, 0]);
                        Console.CursorLeft = 21; //After fixing the settlements with the set cursor, I started printing.
                        Console.Write(dpt[i, 1]);
                        for (int k = 3; k < 11; k++)
                        {
                            if (Convert.ToInt32(dpt[i,k]) != 0)
                            {
                                Console.CursorLeft = 49 + counter1;
                                Console.Write(dpt[i, k]);
                                counter1 = counter1 + 4;
                            }
                        }
                        Console.WriteLine();
                    }
                    else
                    {
                        if (dept.Length > 10)
                        {
                            Console.WriteLine("The number of departments is dynamic maximum 10.");
                            Environment.Exit(0);
                        }
                        else
                        {
                            Console.WriteLine("The maximum quota for any department is 8.");
                            Environment.Exit(0);
                        }
                    }
                }
            }
            else
            {
                Console.WriteLine("The maximum number of candidate students can be 40.");
                Console.ReadLine();
                Environment.Exit(0);
            }
            Console.ReadLine(); 
        }
    }
}
