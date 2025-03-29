import java.io.*;
import java.util.*;

public class PathMap {
    private Map<String, Map<String, Double[]>> adjacencyList = new HashMap<>();
    private List<String> landmarks = new ArrayList<>();
    private Map<String, Double> personalInterest = new HashMap<>();
    private Map<String, Double> visitorLoad = new HashMap<>();
    private Map<String, Map<String, Double>> storeScore = new HashMap<>();
    private Map<String, Map<String, String>> storePath = new HashMap<>();
    private Map<String, Map<String, Double>> storeTime = new HashMap<>();

    // Time complexity: O(n)
    public PathMap(String graphPath, String interestPath, String loadPath) throws IOException {
        // Check if all files exist, exit if any does not.
        if (!fileExists(interestPath) || !fileExists(loadPath) || !fileExists(graphPath)) {
            System.out.println("One or more files do not exist.");
            System.exit(1); // Exits the program with a status code 1 indicating an error.
        }
        readPersonalInterest(interestPath); // Time complexity: O(n), where n is the number of lines in the file
        readVisitorLoad(loadPath);          // Assumed similar to readPersonalInterest, thus O(n)
        readLandmarkData(graphPath);        // Assumed similar to readPersonalInterest, thus O(n)
    }

    // Time complexity: O(1)
    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    // Time complexity: O(n)
    private void readPersonalInterest(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header, constant time operation O(1)
            String line;
            while ((line = reader.readLine()) != null) { // For each line in the file, process it, total O(n)
                String[] parts = line.split("\\s+");
                personalInterest.put(parts[0], Double.parseDouble(parts[1]));
            }
        }
    }

    // Time complexity: O(n)
    private void readVisitorLoad(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header, constant time operation O(1)
            String line;
            while ((line = reader.readLine()) != null) { // For each line in the file, process it, total O(n)
                String[] parts = line.split("\\s+");
                visitorLoad.put(parts[0], Double.parseDouble(parts[1]));
            }
        }
    }

    // Time complexity: O(n)
    private void readLandmarkData(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header, constant time operation O(1)
            String line;
            while ((line = reader.readLine()) != null) { // Total time complexity for this loop is O(n)
                String[] parts = line.split("\\s+");
                String from = parts[0];
                String to = parts[1];
                double baseScore = Double.parseDouble(parts[2]); // Base attractiveness score
                double travelTime = Double.parseDouble(parts[3]); // Travel time between landmarks

                // Calculate attractiveness score based on personal interest and visitor load
                double interestFactor = personalInterest.getOrDefault(to, 0.0);
                double loadFactor = 1 - visitorLoad.getOrDefault(to, 0.0);
                double attractiveScore = baseScore * interestFactor * loadFactor;

                adjacencyList.putIfAbsent(from, new HashMap<>());
                adjacencyList.get(from).put(to, new Double[] {attractiveScore, travelTime});

                // Ensure both landmarks are noted in a list of all landmarks
                if (!landmarks.contains(from)) landmarks.add(from);
                if (!landmarks.contains(to)) landmarks.add(to);
            }
        }
    }

    // Time complexity: O(n^2 * 2^n)
    private double[] tsp(String current, Set<String> visited) {
        // Retrieve the score and travel time for returning to the hotel, or default to [0.0, 0.0] if not available
        if (visited.size() == landmarks.size()) {
            return new double[] {adjacencyList.get(current).getOrDefault("Hotel", new Double[]{0.0, 0.0})[0], adjacencyList.get(current).getOrDefault("Hotel", new Double[]{0.0, 0.0})[1]};
        }

        // Create a unique key for the current state using the current location and set of visited locations
        String visitedKey = String.join(",", visited);
        // Check memoization map for a previously calculated result for this state
        if (storeScore.containsKey(current) && storeScore.get(current).containsKey(visitedKey)) {
            return new double[] {storeScore.get(current).get(visitedKey), storeTime.get(current).get(visitedKey)}; // Return both score and travel time
        }

        // Initialize variables for finding the maximum score and the corresponding minimum travel time
        double maxAttractiveScore = Double.NEGATIVE_INFINITY;
        double minTravelTime = Double.POSITIVE_INFINITY;
        String bestNext = null;

        for (String next : landmarks) {
            if (!visited.contains(next)) {
                Set<String> newVisited = new HashSet<>(visited);
                newVisited.add(next);
                // Recursively calculate the score and time for the next step of the journey
                double[] result = tsp(next, newVisited);
                double score = adjacencyList.get(current).get(next)[0] + result[0];
                double time = adjacencyList.get(current).get(next)[1] + result[1];

                // Update the best score and travel time if this path is better
                if (score > maxAttractiveScore || (score == maxAttractiveScore && time < minTravelTime)) {
                    maxAttractiveScore = score;
                    minTravelTime = time;
                    bestNext = next;
                }
            }
        }

        // Memoize the best score and travel time for this state to optimize future calculations
        storeScore.putIfAbsent(current, new HashMap<>());
        storeScore.get(current).put(visitedKey, maxAttractiveScore);
        storeTime.putIfAbsent(current, new HashMap<>());
        storeTime.get(current).put(visitedKey, minTravelTime);
        storePath.putIfAbsent(current, new HashMap<>());
        storePath.get(current).put(visitedKey, bestNext);
        return new double[] {maxAttractiveScore, minTravelTime};
    }

    // Time complexity: O(1)
    public int getUserExpectedLandmarkCount() {
        // Get user input for the expected number of landmarks.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the total number of landmarks (including Hotel): ");
        return scanner.nextInt();
    }

    // Time complexity: O(n^2 * 2^n)
    public void findOptimalPath() {
        int userExpectedCount = getUserExpectedLandmarkCount();
        // Check if the number of landmarks matches the user's expectation.
        if (userExpectedCount != landmarks.size()) {
            System.out.println("Mismatch in number of landmarks: expected " + userExpectedCount + " but found " + landmarks.size());
            return;
        }
        System.out.println("\nThree input files are read.\n");
        System.out.println("The tour planning is now processing…\n");

        // Initialize sets and lists for tracking the tour.
        Set<String> visited = new HashSet<>();
        visited.add("Hotel");
        double[] result = tsp("Hotel", visited); // Call the recursive TSP function.

        // Follow the computed path from the memoization map.
        String current = "Hotel";
        List<String> path = new ArrayList<>();
        path.add(current);
        while (visited.size() < landmarks.size()) { // Time complexity: O(n) where n is the number of landmarks
            String next = storePath.get(current).get(String.join(",", visited));
            path.add(next);
            visited.add(next);
            current = next;
        }
        path.add("Hotel"); // Return to the starting point.

        // Output the visited landmarks and tour statistics.
        System.out.println("The visited landmarks:");
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.println((i + 1) + "- " + path.get(i));
        }
        System.out.println(path.size() + "- " + path.get(path.size() - 1));
        System.out.println("\nTotal attractiveness score: " + result[0] +"\n");
        System.out.println("Total travel time is: " + result[1] + " minutes");
    }

    // Main method to initiate the path finding process.
    public static void main(String[] args) {
        try {
            PathMap graph = new PathMap("src/landmark_map_data.txt", "src/personal_interest.txt", "src/visitor_load.txt");
            graph.findOptimalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}