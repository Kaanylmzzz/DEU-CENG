import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MarketManagement {
    public static boolean g1 = false;
    public static boolean g2 = false;

    public MarketManagement() {

        List<Customer> customers = readCsv("src/supermarket_dataset_50K.csv");
        HashTable<String, Customer> customerMap = new HashTable<>();

        // Get user input for hash function and collision resolution method
        Scanner scanner = new Scanner(System.in);
        System.out.print("How to generate your hash function ? (SSF or PAF) ");
        String generate1 = scanner.nextLine();
        System.out.print("How to avoid collusions ? (LP or DH) ");
        String generate2 = scanner.nextLine();

        if ("SSF".equalsIgnoreCase(generate1))
            g1= true;
         else if ("PAF".equalsIgnoreCase(generate1)) {
        } else
            throw new IllegalArgumentException("Invalid hash function type");

        if ("LP".equalsIgnoreCase(generate2))
            g2= true;
         else if ("DH".equalsIgnoreCase(generate2)) {
        } else
            throw new IllegalArgumentException("Invalid collision resolution type");


        for (Customer customer : customers) {
            customerMap.put(customer.getCustomerId(),customer);
        }
        // Display hash table statistics
        System.out.println("Time: "+ HashTable.getIndextime()+" ms");
        System.out.println("Collisions: "+HashTable.collision);
        System.out.println("Table size: "+HashTable.table.length);

        System.out.print("How would you like to make a search ? (ID or TXT) ");
        String search = scanner.nextLine();

        if ("ID".equalsIgnoreCase(search)){
            searchById(scanner);
        }
        else if ("TXT".equalsIgnoreCase(search)) {
            searchByTxt();
        } else
            throw new IllegalArgumentException("Invalid collision resolution type");

        System.out.println("\nMax search time: " + (int) HashTable.getmaxSearchTime() + " ns");
        System.out.println("Min search time: " +  HashTable.getminSearchTime() + " ns");
        System.out.println("Average search time: " + (int) HashTable.getAverage() + " ns");
    }
    private static List<Customer> readCsv(String filePath) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Customer customer = new Customer(
                        values[0].trim(),                       // Customer ID
                        values[1].trim(),                       // Customer Name
                        values[2].trim(),                       // Date
                        values[3].trim()                        // Product Name
                );
                customers.add(customer);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static List<String> readTxt(String filepath) {
        List<String> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private static void searchById(Scanner scanner) {
        System.out.print("Enter the search key: ");
        String searchKey = scanner.nextLine();

        ArrayList<HashEntry> searchResults = (ArrayList<HashEntry>) HashTable.get(searchKey);
        searchResults.sort((entry2, entry1) -> entry1.getValue().getDate().compareTo(entry2.getValue().getDate()));

        displaySearchResults(searchResults);
    }

    private static void searchByTxt() {
        List<String> searchKeys = readTxt("src/customer_1K.txt");

        for (String searchKey : searchKeys) {
            ArrayList<HashEntry> searchResults = (ArrayList<HashEntry>) HashTable.get(searchKey);
            searchResults.sort((entry2, entry1) -> entry1.getValue().getDate().compareTo(entry2.getValue().getDate()));

            if (!searchResults.isEmpty()) {
                displaySearchResults(searchResults);
            } else {
                System.out.println("Customer not found");
                break;
            }
        }
    }

    private static void displaySearchResults(ArrayList<HashEntry> searchResults) {
        if (!searchResults.isEmpty()) {
            System.out.println("\nSearch Results:");
            boolean printedTransaction = false;
            // Iterate through each HashEntry in the search results
            for (HashEntry entry : searchResults) {
                if (!printedTransaction) {
                    System.out.println(HashTable.TRANSACTIONS + " transactions found for " + entry.getValue().getName());
                    // Reset the transaction counter
                    HashTable.TRANSACTIONS = 0;
                    printedTransaction = true;
                }
                System.out.println(entry.getValue().getDate() + ", " + entry.getValue().getProduct_name());
            }
        } else {
            System.out.println("\n Customer not found");
        }
    }
}
