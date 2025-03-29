using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _2021510072_Kaan_Yılmaz
{
    internal class Program
    {
        static void Main(string[] args)
        {
            double x, y;
            string oprt;

            while (true)
            {
                Console.WriteLine("Please enter your x value between 2 and 50");
                x = Convert.ToDouble(Console.ReadLine());

                if (2 <= x && x <= 50)
                {
                    break;
                }
                Console.WriteLine("Please enter a number in the value range.");
            }

            while (true)
            {
                Console.WriteLine("Please enter your y value between 25 and 30");
                y = Convert.ToDouble(Console.ReadLine());

                if (25 <= y && y <= 30)
                {
                    break;
                }
                Console.WriteLine("Please enter a number in the value range.");
            }

            while (true)
            {
                Console.WriteLine("Please choose your operator * or +");
                oprt = Console.ReadLine();

                if (oprt == "*")
                {
                    break;
                }
                else if (oprt == "+")
                {
                    break;
                }
                else
                {
                    Console.WriteLine("You have entered incorrect. Try again.");
                }

            }
            //In the above lines, I specified the value ranges and got values from the user accordingly.
            double faktoriyel = 1;
            double result = 0;
            double mult = 0;
            double us = 1;

            if (oprt == "*") //I performed the operation according to the given operator using if else.
            {
                for (int i = 0; i <= 24; i++)
                {
                    for (double f = y - i; f > 0; f--) //I took the factorial with respect to y.
                    {
                        faktoriyel *= f;
                    }

                    for (double a = 3 * i + 2; a <= 3 * i + 2; a++)  //I printed the coefficients in front of x with the value of a.
                    {
                        if (a * x * (a + 3) * x < faktoriyel)  //I got the case of the x being small.
                        {
                            for (double j = 2 * i + 1; j <= 4 * i + 3; j = j + 2)  //I calculated by looping the exponents and numbers in my divisor.
                            {
                                for (double k = 1; k <= i + 1; k++)
                                {
                                    us = us * j;
                                }
                                mult = us + mult;
                                us = 1;
                            }

                            if (i == 0 || i % 2 == 1)  //I printed out that the result will be + or - by looking at the remainder of 2.
                            {
                                result = result + ((a * x * (a + 3) * x) / mult);
                            }
                            else
                            {
                                result = result - ((a * x * (a + 3) * x) / mult);
                            }
                        }

                        else
                        {
                            for (double j = 2 * i + 1; j <= 4 * i + 3; j = j + 2)
                            {

                                for (double k = 1; k <= i + 1; k++)
                                {
                                    us = us * j;
                                }
                                mult = us + mult;
                                us = 1;
                            }

                            if (i == 0 || i % 2 == 1)
                            {
                                result = result + (faktoriyel / mult);
                            }
                            else
                            {
                                result = result - (faktoriyel / mult);
                            }
                        }
                    }
                    mult = 0;
                }
            }

            else
            {
                for (int i = 0; i <= 24; i++)
                {
                    for (double f = y - i; f > 0; f--)
                    {
                        faktoriyel *= f;
                    }

                    for (double a = 3 * i + 2; a <= 3 * i + 2; a++)
                    {
                        if ((a * x) + (a + 3) * x < faktoriyel)
                        {
                            for (double j = 2 * i + 1; j <= 4 * i + 3; j = j + 2)
                            {

                                for (double k = 1; k <= i + 1; k++)
                                {
                                    us = us * j;
                                }
                                mult = us + mult;
                                us = 1;
                            }

                            if (i == 0 || i % 2 == 1)
                            {
                                result = result + ((a * x + (a + 3) * x) / mult);
                            }
                            else
                            {
                                result = result - ((a * x + (a + 3) * x) / mult);
                            }
                        }

                        else
                        {
                            for (double j = 2 * i + 1; j <= 4 * i + 3; j = j + 2)
                            {
                                for (double k = 1; k <= i + 1; k++)
                                {
                                    us = us * j;
                                }
                                mult = us + mult;
                                us = 1;
                            }

                            if (i == 0 || i % 2 == 1)
                            {
                                result = result + (faktoriyel / mult);
                            }
                            else
                            {
                                result = result - (faktoriyel / mult);
                            }
                        }
                    }
                    mult = 0;
                }
            }
            Console.WriteLine(result);
            Console.ReadLine();
        }
    }
}
