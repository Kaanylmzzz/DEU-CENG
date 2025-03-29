import java.util.List;
public interface Market<K, V> {
    void put(K key, V value);
    static Object get(Object key) {
        return null;
    }
    void remove(K key);
    List<K> keySet();
    int linearProbing(int originalHash, HashEntry[] table);

    int doubleHashing(String k, int originalHash, HashEntry[] table);

}
