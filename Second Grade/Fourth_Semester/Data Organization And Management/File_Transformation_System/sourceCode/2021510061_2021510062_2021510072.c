#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
#include <libxml/tree.h>
#include <libxml/parser.h>
#include <libxml/xmlschemas.h>
#include <json-c/json.h>

#define MAX_LINE_LENGTH 256
char globalFilename[MAX_LINE_LENGTH];

// Define a structure to hold setup parameters
typedef struct {
    char dataFileName[256];
    int keyStart;
    int keyEnd;
    char order[4]; // "ASC" or "DESC"
} SetupParams;

// Define Student struct
typedef struct {
    char name[20];
    char surname[30];
    char stuID[11];
    char gender[2];
    char email[40];
    char phone[18];
    char letter_grade[3];
    int midterm;
    int project;
    int final_grade;
    char regularStudent[8];
    int course_surveyRating;
}Student;

// Function to convert CSV to binary
int convertCSVtoBinary(const char *csv_filename, const char *binary_filename) {
    setlocale(LC_ALL, "Turkish"); 

    FILE *csv_file = fopen(csv_filename, "r");
    FILE *binary_file = fopen(binary_filename, "wb");

    if (csv_file == NULL || binary_file == NULL) {
        printf("Error opening files.\n");
        return 1;
    }

    char line[MAX_LINE_LENGTH];
    Student student;

    // Skip the header line
    fgets(line, sizeof(line), csv_file);

    // Read each line from the CSV file and write to the binary file
    while (fgets(line, sizeof(line), csv_file)) {
        sscanf(line, "%19[^,],%29[^,],%10[^,],%c,%39[^,],%17[^,],%2[^,],%d,%d,%d,%7[^,],%d",
               student.name, student.surname, student.stuID, &student.gender,
               student.email, student.phone, student.letter_grade,
               &student.midterm, &student.project, &student.final_grade,
               student.regularStudent, &student.course_surveyRating);

        // Write student structure to binary file
        fwrite(&student, sizeof(Student), 1, binary_file);
    }

    fclose(csv_file);
    fclose(binary_file);

    printf("CSV file has been successfully converted to binary.\n");
    // Read setup parameters from JSON file
    SetupParams params = readSetupParams("setupParams.json");
    sortBinaryFile(binary_filename, params.keyStart, params.keyEnd, params.order);
    // Store the filename in global variable for further reference
    strncpy(globalFilename, binary_filename, MAX_LINE_LENGTH);
    return 0;
}

// Function to read setup parameters from a JSON file.
SetupParams readSetupParams(const char *filename) {
    SetupParams params; // Declare a variable of type SetupParams to hold the setup parameters.
    
    // Open the JSON file for reading.
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        printf("Error opening setupParams file.\n"); // Print an error message if the file cannot be opened.
        exit(EXIT_FAILURE); // Terminate the program with a failure status.
    }

    // Determine the size of the file.
    fseek(file, 0, SEEK_END);
    long fileSize = ftell(file);
    fseek(file, 0, SEEK_SET);

    // Allocate memory to store the contents of the file.
    char *fileContent = (char *)malloc(fileSize + 1);
    fread(fileContent, 1, fileSize, file);
    fileContent[fileSize] = '\0'; // Null-terminate the string to ensure it is properly formatted.

    fclose(file); // Close the file after reading its contents.

    // Parse the JSON content.
    json_object *json = json_tokener_parse(fileContent);
    free(fileContent); // Free the allocated memory for file content as it is no longer needed.

    // Extract setup parameters from the JSON object.
    json_object *dataFileName, *keyStart, *keyEnd, *order;
    if (!json_object_object_get_ex(json, "dataFileName", &dataFileName) || 
        !json_object_object_get_ex(json, "keyStart", &keyStart) || 
        !json_object_object_get_ex(json, "keyEnd", &keyEnd) || 
        !json_object_object_get_ex(json, "order", &order)) { 
        fprintf(stderr, "Invalid JSON format.\n"); // Print an error message if any of the required keys are missing.
        exit(EXIT_FAILURE); // Terminate the program with a failure status.
    }

    // Check the types of the extracted data and ensure they are of the expected types.
    if (json_object_get_type(dataFileName) != json_type_string || 
        json_object_get_type(keyStart) != json_type_int || 
        json_object_get_type(keyEnd) != json_type_int || 
        json_object_get_type(order) != json_type_string) { 
        fprintf(stderr, "Invalid JSON format.\n"); // Print an error message if any of the values have unexpected types.
        exit(EXIT_FAILURE); // Terminate the program with a failure status.
    }

    // Assign the extracted values to the corresponding fields in the SetupParams structure.
    strcpy(params.dataFileName, json_object_get_string(dataFileName)); 
    params.keyStart = json_object_get_int(keyStart); 
    params.keyEnd = json_object_get_int(keyEnd); 
    strcpy(params.order, json_object_get_string(order)); 

    // Clean up memory used by the JSON object.
    json_object_put(json);

    return params; // Return the setup parameters.
}

// Helper function for sorting ASC.
int compare_students_ASC(const void *a, const void *b) {
    const Student *student_a = (const Student *)a;
    const Student *student_b = (const Student *)b;
    return strcmp(student_a->stuID, student_b->stuID);
}

// Helper function for sorting DESC.
int compare_students_DESC(const void *a, const void *b) {
    const Student *student_a = (const Student *)a;
    const Student *student_b = (const Student *)b;
    return strcmp(student_b->stuID, student_a->stuID);
}

// Function to sort binary file
void sortBinaryFile(const char *binary_filename, int keyStart, int keyEnd, const char *order) {
    // Open the binary file.
    FILE *binary_file = fopen(binary_filename, "r+b");
    if (binary_file == NULL) {
        printf("Error: Unable to open the binary file.\n");
        exit(1);
    }

    // Determine the file size.
    fseek(binary_file, 0, SEEK_END);
    long int fileSize = ftell(binary_file);
    rewind(binary_file);

    // Calculate the record size and the number of records.
    int recordSize = sizeof(Student);
    int numRecords = fileSize / recordSize;

    // Allocate memory for records.
    Student *records = malloc(numRecords * recordSize);
    if (records == NULL) {
        printf("Error: Memory allocation failed.\n");
        exit(1);
    }

    // Read the records.
    fread(records, recordSize, numRecords, binary_file);

    // Sort the records based on the specified order.
    if (strcmp(order, "ASC") == 0) {
        qsort(records, numRecords, recordSize, compare_students_ASC);
    } else if (strcmp(order, "DESC") == 0) {
        qsort(records, numRecords, recordSize, compare_students_DESC);
    } else {
        printf("Invalid order specified.\n");
        exit(1);
    }

    // Rewind the binary file and write the sorted records.
    rewind(binary_file);
    fwrite(records, recordSize, numRecords, binary_file);

    // Free allocated memory and close the file.
    free(records);
    fclose(binary_file);

    printf("Binary file has been successfully sorted.\n");
}

int swap_Endians(int value) {
    // Initialize variables
    int result;

    int leftmost_byte = (value & 0x000000FF) << 24; // move byte 0 to byte 3
    int left_middle_byte = (value & 0x0000FF00) << 8; // move byte 1 to byte 2
    int right_middle_byte = (value & 0x00FF0000) >> 8; // move byte 2 to byte 1
    int rightmost_byte = (value & 0xFF000000) >> 24; // move byte 3 to byte 0

    // Finally, apply OR operation on each value to concatenate them into result.
    
    result = (leftmost_byte | left_middle_byte | right_middle_byte | rightmost_byte);

    return result;
}

int convertBinarytoXML(char *arg1,char *arg2){
    int max_char = 1000;
    char row[max_char];
    xmlDocPtr doc = NULL;
    xmlNodePtr root_node = NULL, mainNode = NULL, xmlrow = NULL, student_info = NULL, grade_info = NULL, regular_Student = NULL, course_surveyRating = NULL ,node = NULL, node1 = NULL; /* node pointers */

    FILE *read_bin;
    read_bin = fopen(arg1, "rb");   // open binary file

    if (!read_bin) // check if binary file is found and opened
    {
        printf("Couldn't open binary file\n");
        return 1;
    }
    else
    {
        doc = xmlNewDoc(BAD_CAST "1.0");
        root_node = xmlNewNode(NULL, BAD_CAST "records");
        char row[1000];
        fseek(read_bin, 0, SEEK_SET);
        for (int i = 1; i < 51; i++)
        {

            Student student;
            fread(&student, sizeof(Student), 1, read_bin); // read  binary file

            xmlDocSetRootElement(doc, root_node);      // set root node
            xmlrow = xmlNewNode(NULL, BAD_CAST "row"); // set row node
            char id[6];
            snprintf(id, sizeof(id), "%d", i);
            const xmlChar *strId = (const xmlChar *)id; // cast id to const xmlChar
            xmlNewProp(xmlrow, BAD_CAST "id", BAD_CAST strId);
            
   
            student_info = xmlNewNode(NULL, BAD_CAST "student_info");
            grade_info = xmlNewNode(NULL, BAD_CAST "grade_info");
            regular_Student = xmlNewNode(NULL, BAD_CAST "regularStudent");
            course_surveyRating = xmlNewNode(NULL, BAD_CAST "course_surveyRating");

            // Add child to main root
            
            xmlAddChild(root_node, xmlrow);
            xmlAddChild(xmlrow, student_info);
            xmlAddChild(xmlrow, grade_info);
            
            // Add child to student_info
            xmlNewChild(student_info, NULL, BAD_CAST "name", student.name);
            xmlNewChild(student_info, NULL, BAD_CAST "surname", student.surname);
            xmlNewChild(student_info, NULL, BAD_CAST "stuID", student.stuID);
            xmlNewChild(student_info, NULL, BAD_CAST "gender", student.gender);
            xmlNewChild(student_info, NULL, BAD_CAST "email", student.email);
            xmlNewChild(student_info, NULL, BAD_CAST "phone", student.phone);
            
            char midterm_grade_str[1000];
            char project_grade_str[1000];
            char final_grade_str[1000];
            
            // Add child to grade_info
            snprintf(midterm_grade_str, sizeof(midterm_grade_str), "%d", student.midterm);
            snprintf(project_grade_str, sizeof(project_grade_str), "%d", student.project);
            snprintf(final_grade_str, sizeof(final_grade_str), "%d", student.final_grade);
            

            xmlNewProp(grade_info, BAD_CAST "letter_grade", student.letter_grade);
            xmlNewChild(grade_info, NULL, BAD_CAST "midterm", BAD_CAST midterm_grade_str);
            xmlNewChild(grade_info, NULL, BAD_CAST "project", BAD_CAST project_grade_str);
            xmlNewChild(grade_info, NULL, BAD_CAST "final", BAD_CAST final_grade_str);
            xmlNodePtr regularStudent_node = xmlNewChild(xmlrow, NULL, BAD_CAST "regularStudent", student.regularStudent);
            
            char course_surveyRating_str[1000];
            snprintf(course_surveyRating_str, sizeof(course_surveyRating_str), "%d", student.course_surveyRating);

            // Convert integer to little endian and big endian
            
            int big_endian = student.course_surveyRating;
            int little_endian = swap_Endians(big_endian);
            int decimal_value = swap_Endians(student.course_surveyRating);
            // Cast integers to constant strings 
            char big_endian_str[1000];
            snprintf(big_endian_str, sizeof(big_endian_str),"%08X", big_endian);
            const xmlChar *xmlBig_endian_str = (const xmlChar *)big_endian_str;

            char little_endian_str[1000];
            snprintf(little_endian_str, sizeof(little_endian_str), "%08X", little_endian);
            const xmlChar *xmlLittle_endian_str = (const xmlChar *)little_endian_str;

            char decimal_value_str[1000];
            snprintf(decimal_value_str, sizeof(decimal_value_str), "%d", decimal_value);
            const xmlChar *xmlDecimal_value_str = (const xmlChar *)decimal_value_str;

            const xmlChar *xmlSurvey_rating = (const xmlChar *)course_surveyRating_str;
            
            xmlNodePtr course_surveyRating = xmlNewChild(xmlrow, NULL, BAD_CAST "course_surveyRating", course_surveyRating_str);
            // Add the properties that big endian, little endian and deciaml.
            xmlNodePtr big_endian_node = xmlNewProp(course_surveyRating, BAD_CAST "hexVal_bigEnd", BAD_CAST big_endian_str);
            xmlNodePtr little_endian_node = xmlNewProp(course_surveyRating, BAD_CAST "hexVal_littleEnd", BAD_CAST little_endian_str);
            xmlNodePtr decimal_node = xmlNewProp(course_surveyRating, BAD_CAST "decimal", BAD_CAST decimal_value_str);

        }
    }

    // Save xml file
    xmlSaveFormatFileEnc(arg2, doc, "UTF-8", 1);
    xmlFreeDoc(doc);
    xmlCleanupParser();
    fclose(read_bin);
    printf("Binary file converted to XML successfully.\n");

    return (0);
}

int validation(char *arg1, char *arg2) {
    xmlDocPtr doc;
    xmlSchemaPtr schema = NULL;
    xmlSchemaParserCtxtPtr ctxt;

    char *XMLFileName = arg1;
    char *XSDFileName = arg2;

    // Set line numbers default to 1 for clearer error messages
    xmlLineNumbersDefault(1);

    // Create a new XML schema parser context
    ctxt = xmlSchemaNewParserCtxt(XSDFileName);
    schema = xmlSchemaParse(ctxt);
    xmlSchemaFreeParserCtxt(ctxt);

    // Read the XML file
    doc = xmlReadFile(XMLFileName, NULL, 0);
    if (doc == NULL) {
        // Error handling if XML file couldn't be parsed
        fprintf(stderr, "Could not parse %s\n", XMLFileName);
        return 1; // Return error code indicating failure
    } else {
        // Validation process
        xmlSchemaValidCtxtPtr ctxt;
        int ret;

        ctxt = xmlSchemaNewValidCtxt(schema);
        ret = xmlSchemaValidateDoc(ctxt, doc);
        if (ret == 0) {
            // XML file validates successfully
            printf("%s validates successfully.\n", XMLFileName);
        } else if (ret > 0) {
            // XML file fails to validate
            printf("%s fails to validate.\n", XMLFileName);
        } else {
            // Validation generated an internal error
            printf("%s validation generated an internal error.\n", XMLFileName);
        }
        xmlSchemaFreeValidCtxt(ctxt);
        xmlFreeDoc(doc);
    }

    // Free the XML schema
    if (schema != NULL)
        xmlSchemaFree(schema);

    // Cleanup
    xmlSchemaCleanupTypes();
    xmlCleanupParser();
    xmlMemoryDump();
    
    return 0;
}

int main(int argc, char *argv[]) {
    
    // Check if the correct number of command-line arguments is provided
    if (argc < 4) {
        printf("Usage: %s <input_file> <output_file> <type>\n", argv[0]);
        return 1; // Return error code indicating incorrect usage
    }

    // Extract command-line arguments
    const char *input_file = argv[1];
    const char *output_file = argv[2];
    int type = atoi(argv[3]);

    // Perform action based on the specified type
    switch(type) {
        case 1:
            // Convert CSV to binary format
            if (convertCSVtoBinary(input_file, output_file) != 0) {
                // If conversion fails, sort the binary file
                printf("Conversion failed.\n");
                return 1; // Return error code indicating failure
            }
            break;
        case 2:
            // Convert binary to XML
            if (convertBinarytoXML(input_file, output_file) != 0) {
                printf("Conversion failed.\n");
                return 1; // Return error code indicating failure
            }
            break;
        case 3:
            // Validate XML file against schema
            if(validation(input_file, output_file) != 0) {
                printf("Validation failed.\n");
                return 1; // Return error code indicating failure
            }
            break;
        default:
            // Handle invalid type
            printf("Invalid type. Please enter a valid option.\n");
            return 1; // Return error code indicating failure
    }
    return 0; // Return success code
}