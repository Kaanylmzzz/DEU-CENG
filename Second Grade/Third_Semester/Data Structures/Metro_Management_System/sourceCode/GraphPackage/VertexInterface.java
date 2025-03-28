package GraphPackage;
import java.util.Iterator;
public interface VertexInterface<T>
{
    public T getLabel();
    public void visit();
    public void unvisit();
    public boolean isVisited();
    public boolean connect(VertexInterface<T> endVertex, double edgeWeight, String line);
    public Iterator<VertexInterface<T>> getNeighborIterator();
    public Iterator<Double> getWeightIterator();
    public Iterator<String> getLineIterator();
    public void setPredecessor(VertexInterface<T> predecessor);
    public VertexInterface<T> getPredecessor();
    public boolean hasPredecessor();
    public void setCost(double newCost);
    public double getCost();
}