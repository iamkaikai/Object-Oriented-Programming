import java.util.*;

public class BFS {

    /**
     * Given a graph and a source, we create a PathTree Graph from the Source to child to grandchild etc
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
        System.out.println("\nBreadth First Search from " + source);
        Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
        Queue<V> queue = new LinkedList<V>(); //queue to implement BFS
        Graph<V, E> pathTree = new AdjacencyMapGraph<V, E>();

        queue.add(source); //enqueue start vertex
        visited.add(source); //add start to visited Set

        while (!queue.isEmpty()) { //loop until no more vertices
            V u = queue.remove(); //dequeue
            pathTree.insertVertex(u);
            for (V v : g.outNeighbors(u)) { //loop over out neighbors
                if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
                    visited.add(v); //add neighbor to visited Set
                    queue.add(v); //enqueue neighbor
                    pathTree.insertVertex(v);
                    pathTree.insertDirected(v, u, g.getLabel(u,v)); //save that this vertex was discovered from prior vertex
                }
            }
        }
        return pathTree;
    }

    /**
     *Returns a list for a path back to the center of the universe given a PathTree
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
        if(!tree.hasVertex(v) || tree.numVertices() == 0){
            System.out.println("No path with vertex");
            return new ArrayList<>();
        }

        System.out.println("Getting path!");
        List<V> path = new ArrayList<>();
        V current = v;
        Queue<V> queue = new LinkedList<>();
        queue.add(current);

        while(!queue.isEmpty()){
            V removed = queue.remove();
            path.add(removed);
            for (V vert: tree.outNeighbors(removed)){
                queue.add(vert);
            }
        }
        return path;
    }

    /**
     * Returns a set of missing vertice that are in subgraph but no graph
     *
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        Set<V> missingVertice = new TreeSet<>();
        for (V vert: graph.vertices()){
            missingVertice.add(vert);
        }
        for (V vert: subgraph.vertices()){
            if (missingVertice.contains(vert)){
                missingVertice.remove(vert);
            }
        }
        return missingVertice;
    }

    /**
     *returns a double that is the average path length
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
        double numerator = sum(tree, root, 0); //sums the path lengths
        System.out.println(numerator);
        double denominator = tree.numVertices();
        return numerator/ denominator;
    }

    /**
     *helper method for average separation that sums the path lengths
     */
    public static <V,E> double sum(Graph<V,E> tree, V root, double count){
        double sumNum = count;
        for(V vert: tree.inNeighbors(root)){
            sumNum += sum(tree, vert, count+1);
        }
        return sumNum;
    }

    public static void main(String[] args) {
        BFS graph = new BFS();
        Graph<String, String> test = new AdjacencyMapGraph<>();
        test.insertVertex("Chris");
        test.insertVertex("Joanne");
        test.insertVertex("Jimmy");
        test.insertVertex("Lauren");
        test.insertVertex("Kyle");
        test.insertVertex("Disconnect1");
        test.insertVertex("Disconnect2");

        test.insertUndirected("Chris", "Jimmy", "lol");
        test.insertUndirected("Chris", "Joanne", "lol");
        test.insertUndirected("Jimmy", "Lauren", "lol");
        test.insertUndirected("Lauren", "Chris", "lol");
        test.insertUndirected("Lauren", "Kyle", "lol");
        test.insertUndirected("Disconnect1","Disconnect2", "lol");

        System.out.println("\ntest:");
        System.out.println(test);

        Graph test2 = graph.bfs(test, "Jimmy");
        System.out.println("\nBFS:");
        System.out.println(test2);

//        System.out.println("\ngetPath:");
//        System.out.println(graph.getPath(test2, "Kyle"));

//        System.out.println("\nmissingVertices:");
//        System.out.println(graph.missingVertices(test, test2));

        System.out.println("\naverageSeparation:");
        System.out.println(graph.averageSeparation(test2, "Jimmy"));

    }
}