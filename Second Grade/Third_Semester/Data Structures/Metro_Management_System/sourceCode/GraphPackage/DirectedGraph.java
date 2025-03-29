package GraphPackage;
import java.util.*;

import ADTPackage.*;
public class DirectedGraph<T> implements GraphInterface<T> {
    public DictionaryInterface<T, VertexInterface<T>> vertices;
    private int edgeCount;
    public List<String> differentLines = new ArrayList<>();

    public DirectedGraph() {
        vertices= new UnsortedLinkedDictionary<>();
        edgeCount = 0;
    }
    @Override
    public boolean addVertex(T vertexLabel) {
        VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<T>(vertexLabel));
        return addOutcome == null;
    }
    @Override
    public boolean addEdge(T begin, T end, double edgeWeight, String line) {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        if ((beginVertex != null) && (endVertex != null)) {
            result = beginVertex.connect(endVertex, edgeWeight, line);
        }
        if (result)
            edgeCount++;
        return result;
    }
    @Override
    public boolean isEmpty(){
        return vertices.isEmpty();
    }
    @Override
    public void clear(){
        vertices.clear();
        edgeCount=0;
    }
    @Override
    public int getNumberOfVertices(){
        return vertices.getSize();
    }
    @Override
    public int getNumberOfEdges(){
        return edgeCount;
    }

    @Override
    public QueueInterface<T> getBreadthFirstTraversal(T begin) {
        resetVertices();
        boolean isDestinationFound = false;
        QueueInterface<T> traversalOrder = new LinkedQueue<>(); // Queue of vertex labels
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>(); // Queue of Vertex objects

        VertexInterface<T> originVertex = vertices.getValue(begin);
        originVertex.visit();

        traversalOrder.enqueue(begin);    // Enqueue vertex label
        vertexQueue.enqueue(originVertex); // Enqueue vertex

        while (!isDestinationFound && !vertexQueue.isEmpty()) {
            VertexInterface<T> currentVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<T>> neighbors = currentVertex.getNeighborIterator();

            while (!isDestinationFound && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel()); // Enqueue vertex label
                    vertexQueue.enqueue(nextNeighbor);
                } // end if
            } // end while
        } // end while

        return printQueue(traversalOrder);
    }
    @Override
    public QueueInterface<T> printQueue(QueueInterface<T> queue) {
        while (!queue.isEmpty()) {
            System.out.print(queue.dequeue() + " ");
        }
        return queue;
    }

    @Override
    public QueueInterface<T> getDepthFirstTraversal(T origin) {
        resetVertices();
        boolean isDestinationFound=false;

        QueueInterface<T> travelsalOrder= new LinkedQueue<>();
        StackInterface<VertexInterface<T>> vertexStack= new LinkedStack<>();

        VertexInterface<T> originVertex = vertices.getValue(origin);
        //VertexInterface<T> endVertex = vertices.getValue(end);
        originVertex.visit();
        //travelsalOrder.enqueue(end);
        vertexStack.push(originVertex);

        while(!isDestinationFound && !vertexStack.isEmpty()){
            VertexInterface<T> currentVertex= vertexStack.pop();

            Iterator<VertexInterface<T>> neighbors= currentVertex.getNeighborIterator();
            travelsalOrder.enqueue(currentVertex.getLabel());

            while(!isDestinationFound && neighbors.hasNext()){
                VertexInterface<T> nextNeighbor = neighbors.next();

                if(!nextNeighbor.isVisited()){
                    nextNeighbor.visit();
                    vertexStack.push(nextNeighbor);
                }
                /*
                if(nextNeighbor.equals(endVertex))
                    isDestinationFound=true;

                 */
            }
        }
        return travelsalOrder;
    }

    @Override
    public StackInterface<T> getShortestPath(T begin, T end, StackInterface<T> path) {
        int n = 1;
        differentLines.clear();
        while(n <= 1){
            resetVertices();
            QueueInterface<VertexInterface<T>> vertexQueue= new LinkedQueue<VertexInterface<T>>();
            VertexInterface<T> originVertex= vertices.getValue(begin);
            VertexInterface<T> endVertex= vertices.getValue(end);

            boolean flag= false;
            originVertex.visit();
            vertexQueue.enqueue(originVertex);

            while (!flag && !vertexQueue.isEmpty()) {
                VertexInterface<T> frontVertex = vertexQueue.dequeue();
                Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

                while (!flag && neighbors.hasNext()) {
                    VertexInterface<T> nextNeighbor = neighbors.next();
                    Iterator<String> nextLines = nextNeighbor.getLineIterator();
                    String nextLine = nextLines.next();
                    if (!nextNeighbor.isVisited()) {

                        if (n > 1 && !differentLines.get(n-1).equals(nextLine)){
                            nextNeighbor.visit();
                            nextNeighbor.setCost(1 + frontVertex.getCost());
                            nextNeighbor.setPredecessor(frontVertex);
                            vertexQueue.enqueue(nextNeighbor);
                        }if (n == 1){
                            nextNeighbor.visit();
                            nextNeighbor.setCost(1 + frontVertex.getCost());
                            nextNeighbor.setPredecessor(frontVertex);
                            vertexQueue.enqueue(nextNeighbor);
                        }


                    }
                    if (nextNeighbor.equals(endVertex))
                        flag = true;
                }
            }

            int pathLength = (int)endVertex.getCost() + 1;
            path.push(endVertex.getLabel());
            VertexInterface<T> vertex = endVertex;
            while (vertex.hasPredecessor())
            {
                vertex = vertex.getPredecessor();
                path.push(vertex.getLabel());
            }
            n++;

            if (pathLength != 1) {
                System.out.println();
                System.out.print("Total station is: " + pathLength);
                printPath(path);
                System.out.println("\n");
            }else {
                while (!path.isEmpty())
                    path.pop();
            }


        }
        return path;
    }
    @Override
    public StackInterface<T> printPath(StackInterface<T> path) {
        T currentStation = path.pop();
        List<String> previousCommonLines = null;
        int count = 0;

        while (!path.isEmpty()) {
            T nextStation = path.peek();
            VertexInterface<T> currentVertex = vertices.getValue(currentStation);
            VertexInterface<T> nextVertex = vertices.getValue(nextStation);

            Iterator<String> currentLines = currentVertex.getLineIterator();
            // Find common lines between the current and next stations
            List<String> commonLines = new ArrayList<>();
            boolean flag = false;

            while (currentLines.hasNext()){

                String currentLine = currentLines.next();
                Iterator<String> nextLines = nextVertex.getLineIterator();
                while (nextLines.hasNext()){
                    String nextLine = nextLines.next();
                    if (currentLine.equals(nextLine)) {
                        commonLines.add(currentLine);
                        count++;
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;

            }
            // Print common lines header only if it's the first iteration or common lines have changed
            if (previousCommonLines == null || !Objects.equals(commonLines, previousCommonLines)) {
                if(previousCommonLines != null){
                    System.out.print(currentStation);
                    System.out.print(" ("+count + " stations)");
                    count = 1;
                }

                System.out.print("\n---- Line: ");//ilk istasyon için no
                for (String line : commonLines) {
                    System.out.print(line + " ");
                    if(!differentLines.contains(line))
                        differentLines.add(line);
                }
                System.out.println();
            }

            // Print stations whenever common lines change
            if (!Objects.equals(commonLines, previousCommonLines)) {
                System.out.print("Stations: " + currentStation + " - ");

            }

            previousCommonLines = commonLines;
            currentStation = path.pop();
            if (path.isEmpty()){
                System.out.println(currentStation + " (" + (count+1) + " stations)");
            }
        }
        System.out.println(); // Move to the next line after printing stations
        return path;
    }
    @Override
    public StackInterface<T> getCheapestPath(T begin, T end, StackInterface<T> path) {
        differentLines.clear();
        int n = 1;
        while(n<2){
            resetVertices(); // reset all vertices before starting
            boolean flag = false; // to stop searching if exit spotted
            // using priority queue to get cheapest path
            PriorityQueueInterface<EntryPQ> priorityQueue = new HeapPriorityQueue<>();
            // converting start and end position those user gave as parameters to vertex
            VertexInterface<T> originVertex = vertices.getValue(begin);
            VertexInterface<T> endVertex = vertices.getValue(end);
            priorityQueue.add(new EntryPQ(originVertex, 0, null));
            // Start traversal
            while (!flag && !priorityQueue.isEmpty()) {

                EntryPQ frontEntry =  priorityQueue.remove();
                VertexInterface<T> frontVertex = frontEntry.vertex;
                //check current vertex
                if (!frontVertex.isVisited()) {
                    frontVertex.visit();
                    frontVertex.setCost(frontEntry.getCost()); // sum weights
                    frontVertex.setPredecessor(frontEntry.getPredecessor());
                    // if exit founded
                    if (frontVertex.equals(endVertex)) {
                        flag = true;
                    }
                    else {
                        //creating iterator for current neighbors and weights
                        Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
                        Iterator<Double> weights = frontVertex.getWeightIterator();
                        Iterator<String> nextLines = frontVertex.getLineIterator();
                        String nextLine = nextLines.next();
                        frontVertex.getCost();
                        // walking in neighbors
                        while (neighbors.hasNext() && (n == 1 || !differentLines.get(n-2).equals(nextLine))) {
                            VertexInterface<T> nextNeighbor = neighbors.next();
                            double weightOfEdgeToNeighbor = weights.next();
                            if (!nextNeighbor.isVisited()) {
                                double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
                                priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                            }
                        }
                    }
                }
            }
            n++;
            // storing the path
            int pathCost = (int) endVertex.getCost();
            path.push(endVertex.getLabel());
            VertexInterface<T> vertex = endVertex;
            int vertexCounter = 0;
            while (vertex.hasPredecessor()) {
                vertex = vertex.getPredecessor();
                path.push(vertex.getLabel());
                vertexCounter++;
            }
            System.out.println();
            System.out.print("Total station is: " + (vertexCounter + 1));
            printPath(path);
            System.out.println("Time: " + pathCost / 60 + " minutes.\n\n");
        }
        return path;

    }
    protected void resetVertices() {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext())
        {
            VertexInterface<T> nextVertex = vertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        } // end while
    }

    private class EntryPQ implements Comparable<EntryPQ> {
        private VertexInterface<T> vertex;
        private VertexInterface<T> previousVertex;
        private double cost; // cost to nextVertex

        private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
        {
            this.vertex = vertex;
            this.previousVertex = previousVertex;
            this.cost = cost;
        } // end constructor

        public VertexInterface<T> getPredecessor()
        {
            return previousVertex;
        } // end getPredecessor
        public double getCost()
        {
            return cost;
        } // end getCost
        public int compareTo(EntryPQ otherEntry) {
            return (int)Math.signum(otherEntry.cost - cost);
        } // end compareTo

    } // end EntryPQ

}