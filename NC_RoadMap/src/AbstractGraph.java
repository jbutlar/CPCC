import java.util.*;
@SuppressWarnings("unchecked")
public abstract class AbstractGraph<V> implements Graph<V>
{
    // Store vertices
    protected List<V> vertices = new ArrayList<>();

    // Store edges in adjacency lists
    protected List<List<Edge>> neighbors = new ArrayList<>();

    /**
     * Construct an empty graph
     */

    protected AbstractGraph()
    {
    }

    /**
     * Construct a graph from vertex array
     * and edges stored in an adjacency matrix
     */

    protected AbstractGraph(V[] vertices, int[][] edges)
    {
        for (int i = 0; i < vertices.length; i++)
        {
            addVertex(vertices[i]);
        }

        createAdjacencyLists(edges, vertices.length);
    }

    /**
     * Construct a graph from vertices and edges stored in lists
     */

    protected AbstractGraph(List<V> vertices, List<Edge> edges)
    {
        for (int i = 0; i < vertices.size(); i++)
        {
            addVertex(vertices.get(i));
        }

        createAdjacencyLists(edges, vertices.size());
    }

    /**
     * Construct a graph from Integer vertices and edge list
     */

    protected AbstractGraph(List<Edge> edges, int numVertices)
    {
        for (int i = 0; i < numVertices; i++)
        {
            addVertex((V) (new Integer(i)));
        }

        createAdjacencyLists(edges, numVertices);
    }

    /**
     * Construct a graph from Integer vertices
     * and edges stored in an adjacency matrix
     */

    protected AbstractGraph(int[][] edges, int numVertices)
    {
        for (int i = 0; i < numVertices; i++)
        {
            addVertex((V) (new Integer(i))); // vertices is {0, 1, ...}
        }

        createAdjacencyLists(edges, numVertices);
    }

    /**
     * Create an edge adjacency list for each vertex
     * from edges stored in an adjacency matrix
     */

    private void createAdjacencyLists(int[][] edges, int numVertices)
    {
        for (int i = 0; i < edges.length; i++)
        {
            addEdge(edges[i][0], edges[i][1]);
        }
    }

    /**
     * Create an edge adjacency list for each
     * vertex from edges stored in a list
     */

    private void createAdjacencyLists(List<Edge> edges, int numVertices)
    {
        for (Edge edge : edges)
        {
            addEdge(edge.u, edge.v);
        }
    }

    /**
     * Return the number of vertices in the graph
     */

    @Override
    public int getSize()
    {
        return vertices.size();
    }

    /**
     * Return the vertices in the graph
     */

    @Override
    public List<V> getVertices()
    {
        return vertices;
    }

    /**
     * Return the object for the specified vertex
     */

    @Override
    public V getVertex(int index)
    {
        return vertices.get(index);
    }

    /**
     * Return the index for the specified vertex object
     */

    @Override
    public int getIndex(V v)
    {
        return vertices.indexOf(v);
    }

    /**
     * Return the neighbors of the vertex specified by the index
     */

    @Override
    public List<Integer> getNeighbors(int index)
    {
        List<Integer> result = new ArrayList<>();
        for (Edge e : neighbors.get(index))
        {
            result.add(e.v);
        }

        return result;
    }

    /**
     * Return the degree for a specified vertex
     */

    @Override
    public int getDegree(int v)
    {
        return neighbors.get(v).size();
    }

    /**
     * Print the edges by calling Vertex toString()
     */

    @Override
    public void printEdges()
    {
        for (int u = 0; u < neighbors.size(); u++)
        {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for (Edge e : neighbors.get(u))
            {
                System.out.print("(" + getVertex(e.u) + ", " +
                        getVertex(e.v) + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Clear the graph: clear vertex and edge lists
     */

    @Override
    public void clear()
    {
        vertices.clear();
        neighbors.clear();
    }

    /**
     * Add a vertex to the graph
     */

    @Override
    public boolean addVertex(V vertex)
    {
        if (!vertices.contains(vertex))
        {
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Add an edge to the graph
     */

    protected boolean addEdge(Edge e)
    {
        if (e.u < 0 || e.u > getSize() - 1)
        {
            throw new IllegalArgumentException("No such index: " + e.u);
        }

        if (e.v < 0 || e.v > getSize() - 1)
        {
            throw new IllegalArgumentException("No such index: " + e.v);
        }

        if (!neighbors.get(e.u).contains(e))
        {
            neighbors.get(e.u).add(e);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Add an edge to the graph using the indices of the vertices
     */

    @Override
    public boolean addEdge(int u, int v)
    {
        return addEdge(new Edge(u, v));
    }

    /**
     * Edge inner class inside the AbstractGraph class
     */

    public static class Edge
    {
        public int u; // Starting vertex of the edge
        public int v; // Ending vertex of the edge

        /**
         * Construct an edge for (u, v)
         */

        public Edge(int u, int v)
        {
            this.u = u;
            this.v = v;
        }

        /**
         * Use equals to traverse edge
         */

        public boolean equals(Object o)
        {
            return (u == ((Edge) o).u && v == ((Edge) o).v);
        }

        /**
         * Create a String verison of the edge
         */

        public String toString()
        {
            return ("[(" + u + "), (" + v + ")]");
        }
    }

    /**
     * Obtain a DFS spanning tree starting from vertex v
     */

    @Override
    public Tree dfs(int v)
    {
        // Create searchOrder list for storing the vertices
        // visited during the traversal
        List<Integer> searchOrder = new ArrayList<>();

        // The parent array is used by the Tree
        int[] parent = new int[vertices.size()];

        for (int i = 0; i < parent.length; i++)
        {
            parent[i] = -1;    // Initialize parent[i] to -1
        }

        // Create a vertex visited list
        boolean[] isVisited = new boolean[vertices.size()];

        // Recursively search
        dfs(v, parent, searchOrder, isVisited);

        // Return the Tree for display
        return new Tree(v, parent, searchOrder);
    }

    /**
     * Recursive method for DFS search
     */

    private void dfs(int u, int[] parent,
                     List<Integer> searchOrder,
                     boolean[] isVisited)
    {
        // Store the visited vertex
        searchOrder.add(u);

        // Vertex v is visited
        isVisited[u] = true;

        // Traverse the edge adjacency list for vertex u
        for (Edge e : neighbors.get(u))
        {
            // Use recursion to go deeper when the other
            // end of the edge has not yet been visited
            if (!isVisited[e.v])
            {
                // The parent of vertex e.v is u
                parent[e.v] = u;

                // Recursive search
                dfs(e.v, parent, searchOrder, isVisited);
            }
        }
    }

    /**
     * Obtain a BFS spanning tree starting from vertex v
     */

    @Override
    public Tree bfs(int v)
    {
        // Create searchOrder list for storing the vertices
        // visited during the traversal
        List<Integer> searchOrder = new ArrayList<>();

        // The parent array is used by the Tree
        int[] parent = new int[vertices.size()];

        for (int i = 0; i < parent.length; i++)
        {
            parent[i] = -1;    // Initialize parent[i] to -1
        }

        // Create a queue to use during the BFS
        MyQueue<Integer> queue = new MyQueue<Integer>();

        // Create a vertex visited list
        boolean[] isVisited = new boolean[vertices.size()];

        queue.enqueue(v);     // Enqueue v
        isVisited[v] = true; // Mark it visited

        // Loop until each vertex added to the queue is gone
        while (!queue.isEmpty())
        {
            int u = queue.dequeue(); // Dequeue to u
            searchOrder.add(u);     // u searched

            // Traverse the edge adjacency list for vertex u
            for (Edge e : neighbors.get(u))
            {
                // Use the queue to go broader when the other
                // end of the edge has not yet been visited
                if (!isVisited[e.v])
                {
                    queue.enqueue(e.v);    // Enqueue w
                    parent[e.v] = u;        // The parent of w is u
                    isVisited[e.v] = true;    // Mark it visited
                }
            }
        }

        // Return the Tree for display
        return new Tree(v, parent, searchOrder);
    }

    /**
     * Tree inner class inside the AbstractGraph class
     */

    public class Tree
    {
        private int root;            // The root of the tree
        private int[] parent;        // Store the parent of each vertex
        private List<Integer> searchOrder; // Store the search order

        /**
         * Construct a tree with root, parent, and searchOrder
         */

        public Tree(int root, int[] parent, List<Integer> searchOrder)
        {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        /**
         * Return the root of the tree
         */

        public int getRoot()
        {
            return root;
        }

        /**
         * Return the parent of vertex v
         */

        public int getParent(int v)
        {
            return parent[v];
        }

        /**
         * Return an array representing search order
         */

        public List<Integer> getSearchOrder()
        {
            return searchOrder;
        }

        /**
         * Return number of vertices found
         */

        public int getNumberOfVerticesFound()
        {
            return searchOrder.size();
        }

        /**
         * Return the path of vertices from a vertex
         * whose index is given to the root
         */

        public List<V> getPath(int index)
        {
            // Create a vertex list to hold the path
            ArrayList<V> path = new ArrayList<>();

            // Use the parent array to build the path
            // to the root from the starting vertex
            do
            {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);

            return path;
        }

        /**
         * Print a path of vertices from the
         * root to vertex v whose index is given
         */

        public String printPath(int index)
        {
            String str = "";
            List<V> path = getPath(index);
            str += "A path from " + vertices.get(root) +
                    " to " + vertices.get(index) + ": \n";
            int count = 0;
            // Print the path in reverse from root to index
            for (int i = path.size() - 1; i > 0; i--)
            {
                if (i < path.size() - 1 && count % 5 == 0)
                {
                    str += "\n" + path.get(i) + "--> ";
                }
                else
                {
                    str += path.get(i) + "--> ";
                }
                count++;
            }
            str += path.get(0);

            return str;
        }

        /**
         * Print the path of edges from the root to the
         * starting vertex given when the Tree was built
         */

        public String printTree()
        {
            String str = "";
            str += "Root is " + vertices.get(root) + "\n";
            str += "Edges:";
            int count = 0;

            for (int i = 0; i < parent.length; i++)
            {
                if (parent[i] != -1)
                {
                    // Display an edge
                    if (count % 5 == 0)
                    {
                        str += "\n" + "(" + vertices.get(parent[i]) + ", " +
                                vertices.get(i) + ") ";
                    }
                    else
                    {
                        str += "  " + "(" + vertices.get(parent[i]) + ", " +
                                vertices.get(i) + ") ";
                    }

                    count++;
                }
            }
            str += "\n";

            return str;
        }
    }
}
