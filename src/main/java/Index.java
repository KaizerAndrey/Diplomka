import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {
    protected static Index storage;
    protected Map<String, List<PageEntry>> wordIndexing = new HashMap<>();

    private Index() {
    }

    public static synchronized Index getIndexedStorage() {
        if (storage == null) {
            storage = new Index();
        }
        return storage;
    }

    public Map<String, List<PageEntry>> getStorage() {
        return wordIndexing;
    }
}