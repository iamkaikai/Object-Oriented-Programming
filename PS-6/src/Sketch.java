import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Sketch {
    private Map shapes;
    private Integer id = 0;
    public Sketch(){
        shapes = new TreeMap<Integer, Shape>();
    }
    public void add(Shape shape, int id){
        shapes.put(id, shape);
    }
    public void remove(int id){
        shapes.remove(id);
    }
}
