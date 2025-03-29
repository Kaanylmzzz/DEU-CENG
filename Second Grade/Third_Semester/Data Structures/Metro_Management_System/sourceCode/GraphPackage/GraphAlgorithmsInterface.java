package GraphPackage;
import ADTPackage.*;
public interface GraphAlgorithmsInterface <T>{
    public QueueInterface<T> getBreadthFirstTraversal(T origin);
    public QueueInterface<T> getDepthFirstTraversal(T origin);
    public StackInterface<T> getShortestPath(T begin, T end, StackInterface<T> path);
    public StackInterface<T> getCheapestPath(T begin, T end, StackInterface<T> path);

}
