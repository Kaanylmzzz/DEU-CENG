public class HashEntry {

    private Object key;
    private Object value;

    HashEntry(Object key, Customer value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Customer getValue() {
        return (Customer) value;
    }


}