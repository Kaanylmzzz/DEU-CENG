package GraphPackage;
import java.util.Iterator;
import ADTPackage.*;

public class Vertex<T> implements VertexInterface<T>{
    private T label;
    private ListWithIteratorInterface<Edge> edgeList;
    private boolean visited;
    private VertexInterface<T> previousVertex;
    private double cost;

    public Vertex(T vertexLabel){
        label=vertexLabel;
        edgeList= new LinkedListWithIterator<>();
        visited=false;
        previousVertex=null;
        cost=0;

    }

    @Override
    public T getLabel() {
        return label;
    }

    @Override
    public void visit() {
        visited = true;
    }

    @Override
    public void unvisit() {
        visited = false;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    public boolean connect(VertexInterface<T> endVertex, double edgeWeight, String line)
    {
        boolean result = false;

        if (!this.equals(endVertex))
        {  // Vertices are distinct
            Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
            boolean duplicateEdge = false;

            while (!duplicateEdge && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    duplicateEdge = true;
            } // end while

            edgeList.add(new Edge(endVertex, edgeWeight, line));
            result = true; // end if
        } // end if

        return result;
    } // end connect

    @Override
    public Iterator<VertexInterface<T>> getNeighborIterator() {
        Iterator<Edge> edgeIterator = edgeList.getIterator();
        return new Iterator<VertexInterface<T>>() {
            @Override
            public boolean hasNext() {
                return edgeIterator.hasNext();
            }

            @Override
            public VertexInterface<T> next() {
                return edgeIterator.next().getEndVertex();
            }
        };
    }

    @Override
    public Iterator<Double> getWeightIterator() {
        Iterator<Edge> edgeIterator = edgeList.getIterator();
        return new Iterator<Double>() {
            @Override
            public boolean hasNext() {
                return edgeIterator.hasNext();
            }

            @Override
            public Double next() {
                return edgeIterator.next().getWeight();
            }
        };
    }

    @Override
    public Iterator<String> getLineIterator() {
        Iterator<Edge> edgeIterator = edgeList.getIterator();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return edgeIterator.hasNext();
            }

            @Override
            public String next() {
                return edgeIterator.next().getLine();
            }
        };
    }

    @Override
    public void setPredecessor(VertexInterface<T> predecessor) {
        previousVertex = predecessor;
    }

    @Override
    public VertexInterface<T> getPredecessor() {
        return previousVertex;
    }

    @Override
    public boolean hasPredecessor() {
        return previousVertex != null;
    }

    @Override
    public void setCost(double newCost) {
        cost = newCost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    protected class Edge
    {
        private VertexInterface<T> vertex;
        private double weight;
        private String line;
        protected Edge(VertexInterface<T> endVertex,double edgeWeight, String line){
            vertex=endVertex;
            weight=edgeWeight;
            this.line = line;
        }
        protected VertexInterface<T> getEndVertex(){
            return vertex;
        }
        protected double getWeight(){
            return weight;
        }

        protected String getLine() { return line;}
    }

}