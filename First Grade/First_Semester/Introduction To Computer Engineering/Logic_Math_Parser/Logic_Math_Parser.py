static_operators = ["**", "//", "%", "*", "/", "+", "-"]
static_operands = ["<", ">", "<=", ">=", "!=", "=="]
# NOTE: Sir, I couldn't fix the error only when there is a space between the numbers in my code (15 or 1 5). The rest work flawlessly.
input = open("input.txt", "r") #Here I take my input
f_output = open("2021510072_kaan_yilmaz_output.txt", "a")
for line in input.readlines(): #Here I take my input as line line.
    s_line = line.replace(" ", "")
    s_line = s_line.replace("\n", "")
#I continue my operations by removing the spaces above and the line signs that will appear next to the input.
    s_list = []
    for c in s_line:
        s_list.append(c)

    i = 0
    categorized_s = []
    c_index = 0

    error = False #I created a boolean variable to print the errors.

    while i < len(s_list):

        if s_list[i].isnumeric(): #First of all, I check whether it is a number or not.

            categorized_s.insert(c_index, s_list[i])
            i += 1

            j = i
            while j < len(s_list):

                if s_list[j].isnumeric(): #I put them in my index one by one, if they are even numbers, I combine them.
                    categorized_s[c_index] = categorized_s[c_index] + s_list[j]
                    j += 1
                    i = j
                else:
                    i = j
                    c_index += 1
                    break

        elif s_list[i] in static_operators:

            if i + 1 is not len(s_list) and (s_list[i] + s_list[i + 1] in static_operators): #Here, too, I'm throwing the operators into my index.
                categorized_s.insert(c_index, s_list[i] + s_list[i + 1])
                i += 2

            else:
                categorized_s.insert(c_index, s_list[i])
                i += 1

            c_index += 1

        elif s_list[i] in static_operands or s_list[i] == "!" or s_list[i] == "=":
        #In this part, if my operands are in even form, I make them pass to a single index when they satisfy these conditions.
            if s_list[i] + s_list[i + 1] in static_operands:
                categorized_s.insert(c_index, s_list[i] + s_list[i + 1])
                i += 2

            else:
                categorized_s.insert(c_index, s_list[i])
                i += 1

            c_index += 1

        else:
            error = True
            break
        #If it doesn't provide any of these, I make my boolean variable true and print "Error".
    if not error:
        for i in range(0, len(categorized_s)):
            for j in range(0, len(categorized_s)):#Here, too, I now perform my operations, starting with the operators, paying attention to the priority of the operation.
                if categorized_s[j] == "**":
                    try:
                        temp = float(categorized_s[j - 1]) ** float(categorized_s[j + 1]) #I calculate my result and throw it into temp.
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp) #I delete the numbers in its index and print my new result in the first index.
                        break
                    except:
                        error = True
                elif categorized_s[j] == "//": #I repeat these operations for all operators
                    try:
                        temp = float(categorized_s[j - 1]) // float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True
                elif categorized_s[j] == "%":
                    try:
                        temp = float(categorized_s[j - 1]) % float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True
                elif categorized_s[j] == "*":
                    try:
                        temp = float(categorized_s[j - 1]) * float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True

                elif categorized_s[j] == "/":
                    try:
                        temp = float(categorized_s[j - 1]) / float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True

            for j in range(0, len(categorized_s)): #Since + and - are lower priority, I examine them in a separate loop and process them last.
                if categorized_s[j] == "+":
                    try:
                        temp = float(categorized_s[j - 1]) + float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True
                elif categorized_s[j] == "-":
                    try:
                        temp = float(categorized_s[j - 1]) - float(categorized_s[j + 1])
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, temp)
                        break
                    except:
                        error = True

            for j in range(0, len(categorized_s)):
                if categorized_s[j] == "==": #Here, too, I perform my operations by controlling my operands.
                    if int(categorized_s[j - 1]) == int(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break
                elif categorized_s[j] == ">":
                    if float(categorized_s[j - 1]) > float(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break
                elif categorized_s[j] == "<":
                    if float(categorized_s[j - 1]) < float(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break
                elif categorized_s[j] == ">=":
                    if float(categorized_s[j - 1]) >= float(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break
                elif categorized_s[j] == "<=":
                    if float(categorized_s[j - 1]) <= float(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break
                elif categorized_s[j] == "!=":
                    if float(categorized_s[j - 1]) != float(categorized_s[j + 1]):
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "TRUE")
                        break
                    else:
                        del categorized_s[j - 1: j + 2]
                        categorized_s.insert(j - 1, "FALSE")
                        break

    if error: #Here I also print my results.
        f_output.write("ERROR!" + "\n")

    else:
        if categorized_s == []:
            f_output.write("\n")
        else:
            f_output.write(str(categorized_s[j-1]) + "\n")

input.close() #I finish my code by closing my input and printing.
f_output.close()