import ADTPackage.LinkedStack;
import GraphPackage.DirectedGraph;
import GraphPackage.VertexInterface;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class JourneyPlanner {
    private static DirectedGraph<String> transportGraph = new DirectedGraph<>();
    private static long indextime = 0;
    public JourneyPlanner() {
        Scanner scanner = new Scanner(System.in);
        // Read CSV file and populate the graph
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Paris_RER_Metro_v2.csv"), StandardCharsets.UTF_8))) {
            br.readLine(); // Skip the header line
            String line;
            String[] temp = br.readLine().split(",");

            String[] stopNamesArray = new String[1000]; // Array to keep track of stop names
            int currentIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String stopName = data[1];
                String arrivalTime = data[2];
                String prearrivalTime = temp[2];
                String stopsequences = data[3];
                String routeShortName = data[5];
                String directionID = data[4];

                // Add vertices to the graph
                if (temp[4].equals(directionID) && (Integer.parseInt(stopsequences) - Integer.parseInt(temp[3]) == 1)) {
                    if (Integer.parseInt(stopsequences) == 2) {
                        int stopIndex = findStopIndex(temp[1], stopNamesArray, currentIndex);
                        if (stopIndex == -1) {
                            transportGraph.addVertex(temp[1]);
                            stopNamesArray[currentIndex] = temp[1];
                            currentIndex++;
                        }

                    }

                    int stopIndex = findStopIndex(stopName, stopNamesArray, currentIndex);

                    double edgeWeight = Math.abs(Double.parseDouble(arrivalTime) - Double.parseDouble(prearrivalTime));
                    if (stopIndex != -1) {
                        transportGraph.addEdge(temp[1], stopName, edgeWeight, routeShortName);
                    } else {
                        // If the stop is not added, add it to the graph
                        transportGraph.addVertex(stopName);
                        transportGraph.addEdge(temp[1], stopName, edgeWeight, routeShortName);
                        stopNamesArray[currentIndex] = stopName;
                        currentIndex++;
                    }
                }
                temp = data;
            }
            readWalkEdge("walk_edges.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //printGraph(transportGraph);
        System.out.println("Search for from csv or are you going to enter manually(m):");
        String sc = scanner.nextLine();
        if (sc.equals("csv")) {
            readCsv("Test100.csv");
        } else if (sc.equals("m")) {
            System.out.print("Origin station: ");
            String originStation = scanner.nextLine();
            System.out.print("Destination station: ");
            String destinationStation = scanner.nextLine();
            LinkedStack<String> st = new LinkedStack<>();
            LinkedStack<String> ct = new LinkedStack<>();
            boolean flag = true;
            while (flag) {
                System.out.print("Preferetion (0 (Minimum time) or 1 (fewer stops)): ");
                String str = scanner.nextLine().toLowerCase();
                if (str.equals("0")) {
                    transportGraph.getCheapestPath(originStation, destinationStation, ct);
                    flag = false;
                } else if (str.equals("1")) {
                    transportGraph.getShortestPath(originStation, destinationStation, st);
                    flag = false;
                } else {
                    System.out.println("\nPlease choose right preferetion.");
                }
            }
        }
    }


    private int findStopIndex(String stopName, String[] stopNamesArray, int currentIndex) {
        for (int i = 0; i < currentIndex; i++) {
            if (stopName.equals(stopNamesArray[i])) {
                return i;
            }
        }
        return -1;
    }

    private void printGraph(DirectedGraph<String> graph) {
        System.out.println("Number of Vertices: " + graph.getNumberOfVertices());
        System.out.println("Number of Edges: " + graph.getNumberOfEdges());
        System.out.println("Edge List:");

        Iterator<String> vertexIterator = graph.vertices.getKeyIterator();
        while (vertexIterator.hasNext()) {
            String vertexLabel = vertexIterator.next();
            printVertexWithNeighbors(graph, vertexLabel);
        }
    }

    private void printVertexWithNeighbors(DirectedGraph<String> graph, String vertexLabel) {
        System.out.print(vertexLabel + ": ");

        VertexInterface<String> vertex = graph.vertices.getValue(vertexLabel);
        Iterator<VertexInterface<String>> neighbors = vertex.getNeighborIterator();

        while (neighbors.hasNext()) {
            VertexInterface<String> neighbor = neighbors.next();
            System.out.print(neighbor.getLabel() + " ");
        }

        System.out.println();
    }

    public static void readCsv(String filepath) {

        LinkedStack st = new LinkedStack<>();
        LinkedStack st1 = new LinkedStack<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            br.readLine(); // Skip the header line
            String line;
            long startTime= System.nanoTime();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String origin = data[0];
                String destination = data[1];
                String preferation = data[2];
                /*

                if (preferation.equals("0")) {
                    System.out.println("Origin Station: " + origin);
                    System.out.println("Destination Station: " + destination);
                    System.out.println("Preferation: Minimum Time");
                    transportGraph.getCheapestPath(origin, destination, st);
                } else {
                    System.out.println("Origin Station: " + origin);
                    System.out.println("Destination Station: " + destination);
                    System.out.println("Preferation: Fewer Stops");
                    transportGraph.getShortestPath(origin, destination, st1);
                }

                 */
            }
            long endTime = System.nanoTime();

            // Update the variable 'indextime' with the elapsed time in milliseconds
            indextime += (int) (endTime / 1000000 - startTime / 1000000);
            System.out.println("Time: "+ indextime+" ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readWalkEdge(String filepath){

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String origin = data[0];
                String destination = data[1];
                transportGraph.addEdge(origin,destination,300,"WalkLine");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}