import java.util.Random;
import java.util.Scanner;
class SortingClass {
    static void dualPivotQuickSort(int[] array, int low, int high) {
        if (low < high) {
            if (array[low] > array[high]) {
                swap(array, low, high);
            }
            int[] pivots = sort(array, low, high);
            dualPivotQuickSort(array, low, pivots[0] - 1);
            dualPivotQuickSort(array, pivots[0] + 1, pivots[1] - 1);
            dualPivotQuickSort(array, pivots[1] + 1, high);
        }
    }
    static int[] sort(int[] array, int low, int high){

        int LP = array[low];
        int RP = array[high];
        int low_ex= low;
        int high_ex = high;

        int i = low + 1;
        while (i <= high -1){
            if (array[i] < LP) {
                swap(array, i, low + 1);
                low++; //I make the correct replacement by keeping the low value updated every time.
                i++; // Since it reaches a number it has already looked at, I am increasing it.

            }else if (array[i] > RP) {
                swap(array, i, high -1);
                high--; //I make the correct replacement by keeping the high value updated every time.
            } else
                i++;
        }
        swap(array,low ,low_ex);
        swap(array,high ,high_ex);

        return new int[]{low, high};

    }
    static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    static void shellSort (int[] arrayToSort) {
        int length = arrayToSort.length;

        // Start with a large part, then reduce the part
        for (int part = length / 2; part > 0; part /= 2) {

            for (int i = part; i < length; i++) {
                int temp = arrayToSort[i];
                int j; // Shift earlier part-sorted elements up until the correct location for array[i] is found
                for (j = i; j >= part && arrayToSort[j - part] > temp; j -= part) {
                    arrayToSort[j] = arrayToSort[j - part];
                }
                // Put temp (the original array[i]) in its correct location
                arrayToSort[j] = temp;
            }
        }
    }
}
public class Main {
    public static void main(String[] args) {

        SortingClass sorter = new SortingClass();
        Scanner scanner = new Scanner(System.in);

        // Selects what size the array should be.
        System.out.println("What size array do you want to create?");
        System.out.println("1. 1.000");
        System.out.println("2. 10.000");
        System.out.println("3. 100.000");
        System.out.print("Make your choice (1/2/3): ");
        int choice = scanner.nextInt();
        int size = 0;

        // Sets the array according to the selected size.
        switch (choice) {
            case 1:
                size = 1000;
                break;
            case 2:
                size = 10000;
                break;
            case 3:
                size = 100000;
                break;
        }

        // Ask the user to choose the order type.
        System.out.println("\nWhat kind of array would you like to create?");
        System.out.println("1. Equal Order");
        System.out.println("2. Random Order");
        System.out.println("3. Increasing Order");
        System.out.println("4. Decreasing Order");
        System.out.print("Make your choice (1/2/3/4): ");
        int createChoice = scanner.nextInt();

        // Ask the user to choose the sorting type.
        System.out.println("\nWhat kind of sorting method do you want?");
        System.out.println("1. Dual Pivot Quick Sort Method");
        System.out.println("2. Shell Sort Method");
        System.out.print("Make your choice (1/2): ");
        int sortChoice = scanner.nextInt();

        // It ensures that the index is created according to the selected order type.
        int[] array = createArray(size, createChoice);

        switch (sortChoice){
            case 1:
                long startTime_a = System.nanoTime();
                sorter.dualPivotQuickSort(array,0, array.length - 1);
                long endTime_a = System.nanoTime();
                System.out.println("Time taken to sort: " + (double)(endTime_a - startTime_a) / 1000000 + " milliseconds");
                break;
            case 2:
                long startTime_b = System.nanoTime();
                sorter.shellSort(array);
                long endTime_b = System.nanoTime();
                System.out.println("Time taken to sort: " + (double)(endTime_b - startTime_b) / 1000000 + " milliseconds");
                break;
        }

    }
    public static int[] createArray(int size, int createChoice) {
        int[] array = new int[size];
        Random random = new Random();

        switch (createChoice) {
            case 1: // Equal Order
                int equalValue = random.nextInt(Integer.MAX_VALUE);
                for (int i = 0; i < size; i++) {
                    array[i] = equalValue;
                }
                break;
            case 2: // Random Order
                for (int i = 0; i < size; i++) {
                    array[i] = random.nextInt(Integer.MAX_VALUE);
                }
                break;
            case 3: // Increasing Order
                for (int i = 0; i < size; i++) {
                    array[i] = i + 1;
                }
                break;
            case 4: // Decreasing Order
                for (int i = 0; i < size; i++) {
                    array[i] = size + 1 - i;
                }
                break;
        }
        return array;
    }
}

