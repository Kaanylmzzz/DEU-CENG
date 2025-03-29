package GraphPackage;

import ADTPackage.QueueInterface;
import ADTPackage.StackInterface;

public interface BasicGraphInterface <T>{

    public boolean addVertex(T vertexLabel);
    public boolean addEdge(T begin, T end, double edgeWeight, String line);
    public boolean isEmpty();
    public int getNumberOfVertices();
    public int getNumberOfEdges();
    public QueueInterface<T> printQueue(QueueInterface<T> queue);
    public StackInterface<T> printPath(StackInterface<T> path);
    public void clear();
}
