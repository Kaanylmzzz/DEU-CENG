import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashTable<ID, Cs> implements Market<String,Cs> {

    private final static int INITIAL_CAPACITY = 13;
    private final static double LOAD_FACTOR_THRESHOLD = 0.8;
    public static int TRANSACTIONS = 0;
    static HashEntry[] table;
    private static int size;
    public static int collision=0;
    private static double indextime=0;
    private static long totalSearchTime = 0;
    private static long maxSearchtime =Long.MIN_VALUE;
    private static long minSearchtime = Long.MAX_VALUE;
    private static int searchCounter = 0;
    private static  long timeElapsed=0;
    public HashTable() {
        table = new HashEntry[INITIAL_CAPACITY];
        size = 0;
    }

    public static int hashFunction(String key, int tableSize) {
        //We ask what type of transaction the user wants to do and we calculate a table for you according to it.
        if(MarketManagement.g1) {
            return simpleSummationFunction(key) % tableSize;
        } else
            return polynomialAccumulationFunction(key,33) % tableSize;
    }
    @Override
    public void put(String k, Cs v) {
        // Record the start time to measure the execution time of the method
        long startTime= System.nanoTime();
        int hash = hashFunction(k,table.length);
        // Check if the load factor exceeds the threshold, and if so, resize the table
        if (!((double) size / table.length < LOAD_FACTOR_THRESHOLD)) {
            resize(table.length * 2);
        }
        // If the calculated hash position is already occupied, handle collisions
        if (table[hash] != null) {
            collision++;
            if (MarketManagement.g2) {
                hash = linearProbing(hash, table);
            } else
                hash = doubleHashing(k, hash, table);
        }
        // Place the key-value pair into the hash table at the calculated hash position
        table[hash] = new HashEntry(k, (Customer) v);
        size++;
        // Record the end time to measure the execution time of the method
        long endTime = System.nanoTime();

        // Update the variable 'indextime' with the elapsed time in milliseconds
        indextime += (int) (endTime / 1000000 - startTime / 1000000);
    }
    public void resize(int newCapacity) {
        // Find the next prime number greater than or equal to the new capacity
        int newTableSize = findNextPrime(newCapacity);
        // Create a new hash table with the updated size
        HashEntry[] newTable = new HashEntry[newTableSize];
        // Initialize the new table with null entries
        Arrays.fill(newTable, null);
        int exsize = size;
        size = 0;

        // Iterate through the entries in the current table
        for (HashEntry entry : table)
            if (entry != null) {
                int newHash = hashFunction((String) entry.getKey(), newTableSize);
                // Handle collisions in the new table
                if (newTable[newHash] != null) {
                    if (MarketManagement.g2) {
                        newHash = linearProbing(newHash, newTable);
                    } else
                        newHash = doubleHashing((String) entry.getKey(), newHash, newTable);
                }
                // Insert the rehashed entry into the new table and increment the size
                newTable[newHash] =  new HashEntry(entry.getKey(), entry.getValue());
                size++;
            } else if (size == exsize)
                // Break the loop if the new size reaches the original size
                break;
        table = newTable;
    }
    public static int simpleSummationFunction(String customerID) {
        int hash = 0;
        // Remove dashes from the customer ID
        customerID = customerID.replace("-","");
        // Iterate through each character in the modified customer ID
        for (int k = 0; k < customerID.length(); k++) {
            // Add the ASCII value of each character to the hash value
            hash += customerID.charAt(k);
        }
        return hash ;
    }
    public static int polynomialAccumulationFunction(String k, int z) {
        int hash = 0;
        int n = k.length();

        // Iterate through each character in the string
        for (int i = 0; i < n; i++) {
            // Calculate the numerical value of the character (assuming lowercase letters)
            int charValue = k.charAt(i) - 'a' + 1;
            // Update the hash using the polynomial accumulation formula
            hash = (hash * z) + charValue;
        }
        // Ensure the hash value is non-negative
        if (hash < 0) {
            return -hash;
        } else {
            return hash;
        }
    }
    @Override
    public int linearProbing(int originalHash, HashEntry[] table) {
        // Initialize the new hash using linear probing (incrementing by 1)
        int hash = (originalHash + 1) % table.length;
        // Keep probing until an empty slot is found in the table
        while (table[hash] != null) {
            // Update the hash by incrementing it and wrapping around if needed
            hash = (hash + 1) % table.length;
        }
        // Return the final hash after finding an empty slot
        return hash;
    }
    @Override
    public int doubleHashing(String k, int originalHash, HashEntry[] table) {
        // Find the next prime number less than half of the table length
        int prime = findNextPrime(table.length / 2);
        // Calculate the second hash using either simple summation or polynomial accumulation
        int dK;
        if(MarketManagement.g1){
            dK = prime - (simpleSummationFunction(k) % prime);
        }
        else{
            dK = prime - (polynomialAccumulationFunction(k, 33) % prime);
        }
        // Keep probing until an empty slot is found in the table
        while (table[originalHash] != null) {
            // Update the hash by incrementing it using the second hash value and wrapping around if needed
            originalHash = (originalHash + dK) % table.length;
        }
        return originalHash;
    }


    public static Object get(String key) {
        // ArrayList to store matching entries
        ArrayList<HashEntry> matchingEntries = new ArrayList<>();
        long searchTimeElapsed = 0;
        long start = System.nanoTime();

        // Iterate through the hash table to find matching entries
        for (HashEntry hashEntry : table) {
            if (hashEntry != null && hashEntry.getKey().equals(key)) {
                // Record the finish time when a matching entry is found
                long finish = System.nanoTime();
                // Add the matching entry to the list
                matchingEntries.add(hashEntry);
                TRANSACTIONS++;
                searchCounter++;
                searchTimeElapsed = finish - start;
            }
        }
        // Update min and max search times
        if (searchTimeElapsed > maxSearchtime) {
            maxSearchtime = searchTimeElapsed;
        }
        else if (searchTimeElapsed < minSearchtime) {
            minSearchtime = searchTimeElapsed;
        }
        // Update the total search time
        totalSearchTime += searchTimeElapsed;
        return matchingEntries;
    }

    @Override
    public List<String> keySet() {
        // Create a List to store keys
        List<String> keyList = new ArrayList<>();

        // Iterate through the hash table to extract keys
        for (HashEntry entry : table) {
            if (entry != null) {
                // Add the key to the list
                keyList.add((String) entry.getKey());
            }
        }

        return keyList;
    }
    @Override
    public void remove(String key) {
        // Calculate the hash for the given key
        int hash = hashFunction(String.valueOf(key), table.length);
        boolean removed = false;

        // Iterate through the table to find and remove the entry with the specified key
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].getKey().equals(key)) {
                // Customer found, remove the entry
                table[i] = null;
                removed = true;
            }
        }
        // Display a message based on whether the entry was removed or not
        if (removed) {
            System.out.println("All entries with UUID " + key + " removed.");
        } else {
            // Customer not found, display a message
            System.out.println("Customer with UUID " + key + " not found! Cannot remove.");
        }
    }
    public static boolean isPrime(int num) {
        // Check if the number is less than or equal to 1
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            // If the number is divisible by any integer in this range, it's not prime
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int findNextPrime(int num) {
        // If the given number is less than 2, return 2 (the smallest prime)
        if (num < 2) {
            return 2;
        }
        // Start searching for the next prime number by incrementing the input
        int nextNum = num + 1;
        // Continue searching until a prime number is found
        while (!isPrime(nextNum)) {
            nextNum++;
        }
        return nextNum;
    }

    public static double getIndextime(){
        return indextime;
    }
    public static double getminSearchTime(){
            return minSearchtime;
    }
    public static double getmaxSearchTime(){
            return  maxSearchtime;
    }
    public static double getAverage(){
        return totalSearchTime/searchCounter;
    }
}
