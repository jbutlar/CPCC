import java.util.*;
@SuppressWarnings("unchecked")
public class WeightedGraph<V> extends AbstractGraph<V>
{
	/** Construct an empty graph */

	public WeightedGraph()
	{
	}

	/** 
	 * Construct a WeightedGraph from vertex array
	 * and edges stored in an adjacency matrix 
	 */

	public WeightedGraph(V[] vertices, int[][] edges)
	{
		createWeightedGraph (java.util.Arrays.asList (vertices), edges);
	}

	/** 
	 * Construct a WeightedGraph from Integer vertices
	 * and edges stored in an adjacency matrix 
	 */

	public WeightedGraph (int[][] edges, int numVertices)
	{
		List<V> vertices = new ArrayList<>();
		for (int i = 0; i < numVertices; i++)
		{
			vertices.add ((V)(new Integer (i)));
		}
		createWeightedGraph (vertices, edges);
	}

	/** Construct a WeightedGraph vertices and edges stored in lists */

	public WeightedGraph (List<V> vertices, List<WeightedEdge> edges)
	{
		createWeightedGraph (vertices, edges);
	}

	/** Construct a WeightedGraph from Integer vertices and edge list */

	public WeightedGraph (List<WeightedEdge> edges, int numVertices)
	{
		List<V> vertices = new ArrayList<>();
		for (int i = 0; i < numVertices; i++)
		{
			vertices.add ((V)(new Integer (i)));
		}

		createWeightedGraph (vertices, edges);
	}

	/** 
	 * Create an edge adjacency list for each vertex
	 * from edges stored in an adjacency matrix 
	 */

	private void createWeightedGraph (List<V> vertices, int[][] edges)
	{
		this.vertices = vertices;     

		for (int i = 0; i < vertices.size(); i++)
		{
			// Create a list for vertices
			neighbors.add (new ArrayList<Edge>()); 
		}

		for (int i = 0; i < edges.length; i++)
		{
			neighbors.get (edges[i][0]).add (new WeightedEdge 
											 (edges[i][0], edges[i][1], edges[i][2]));
		}
	}

	/** 
	 * Create an edge adjacency list for each vertex in 
	 * the vertex list from edges stored in a list 
	 */

	private void createWeightedGraph (List<V> vertices, List<WeightedEdge> edges)
	{
		this.vertices = vertices;     

		for (int i = 0; i < vertices.size(); i++)
		{
			// Create a list for vertices
			neighbors.add (new ArrayList<Edge>()); 
		}

		for (WeightedEdge edge: edges)
		{
			// Add an edge into the list
			neighbors.get (edge.u).add (edge); 
		}
	}

	/** Return the weight on the edge (u, v) */

	public double getWeight (int u, int v) throws Exception 
	{
		for (Edge edge : neighbors.get (u))
		{
			if (edge.v == v)
			{
				return( (WeightedEdge) edge).weight;
			}
		}

		throw new Exception ("Edge does not exit");
	}

	/** Display edges with weights */

	public void printWeightedEdges()
	{
		for (int i = 0; i < getSize(); i++)
		{
			System.out.print (getVertex(i) + " (" + i + "): ");

			for (Edge edge : neighbors.get (i))
			{
				double wt = ((WeightedEdge)edge).weight;
				System.out.print ("(" + edge.u + ", " + edge.v + ", "
								  + Math.round(wt*100)/100.0 + ") ");
			}

			System.out.println();
		}
	}

	/** Add the edge to the weighted graph */  

	public boolean addEdge (int u, int v, double weight)
	{
		return addEdge (new WeightedEdge (u, v, weight));
	}

	/** Get a minimum spanning tree rooted at vertex 0 */

	public MST getMinimumSpanningTree()
	{
		return getMinimumSpanningTree (0);
	}

	/** Get a minimum spanning tree rooted at a specified vertex */

	public MST getMinimumSpanningTree (int startingVertex)
	{
		// The array element cost[v] stores the cost by adding v to the tree

		double[] cost = new double[getSize()];

		for (int i = 0; i < cost.length; i++)
		{
			cost[i] = Double.POSITIVE_INFINITY;	// Initial cost 
		}

		cost[startingVertex] = 0; // Cost of source is 0

		// Parent of a vertex
		int[] parent = new int[getSize()];

		// StartingVertex is the root
		parent[startingVertex] = -1;

		// Total weight of the tree thus far		
		double totalWeight = 0;             

		// T stores the vertices in MST found so far
		List<Integer> T = new ArrayList<>();

		// Expand T	until it has all the vertices
		while (T.size() < getSize())
		{
			// Vertex to be determined         
			int u = -1; 
			double currentMinCost = Double.POSITIVE_INFINITY;

			// Loop to find smallest cost v in set V-T
			for (int i = 0; i < getSize(); i++)
			{
				if (!T.contains(i) && cost[i] < currentMinCost)
				{
					currentMinCost = cost[i];
					u = i;
				}
			}

			// Add a new vertex to T and the cost to the total weight
			T.add (u); 
			totalWeight += cost[u]; 

			// Adjust cost[v] for each v that is adjacent to u using
			// the weight of the edge (u,v) when v is still in in V-T
			for (Edge e : neighbors.get (u))
			{
				if (!T.contains(e.v) && cost[e.v] > ((WeightedEdge)e).weight)
				{
					cost[e.v] = ((WeightedEdge)e).weight;
					parent[e.v] = u; 
				}
			}
		} // End of while

		return new MST (startingVertex, parent, T, totalWeight);
	}

	/** MST is an inner class in WeightedGraph */

	public class MST extends Tree
	{
		// Total weight of all edges in the tree
		private double totalWeight; 

		// Constructor updates parent Tree and sets weight
		public MST (int root, int[] parent, List<Integer> searchOrder,
					double totalWeight)
		{
			super (root, parent, searchOrder);
			this.totalWeight = totalWeight;
		}

		//Return weight of MST
		public double getTotalWeight()
		{
			return totalWeight;
		}
	}

	/** Find single source shortest paths */

	public ShortestPathTree getShortestPath (int sourceVertex)
	{
		// cost[v] stores the cost of the path from v to the source
		double[] cost = new double[getSize()];

		// Initial cost for each vertice is set to infinity
		for (int i = 0; i < cost.length; i++)
		{
			cost[i] = Double.POSITIVE_INFINITY; 
		}

		// Cost of source is 0
		cost[sourceVertex] = 0; 

		// parent[v] stores the previous vertex of v in the path
		int[] parent = new int[getSize()];

		// The parent of source is set to -1
		parent[sourceVertex] = -1; 

		// T stores the vertices whose path found so far
		List<Integer> T = new ArrayList<>();

		// Expand T
		while (T.size() < getSize())
		{
			// Vertex to be determined 
			int u = -1;         
			double currentMinCost = Double.POSITIVE_INFINITY;

			// Loop to find smallest cost v in set V-T
			for (int i = 0; i < getSize(); i++)
			{
				if (!T.contains(i) && cost[i] < currentMinCost)
				{
					currentMinCost = cost[i];
					u = i;
				}
			}

			// Add a new vertex to T
			T.add (u); 

			// Adjust cost[v] for v that is adjacent to u 
			// and v still in set V-T
			for (Edge e : neighbors.get (u))
			{
				if (!T.contains(e.v) 
					&& cost[e.v] > cost[u] + ((WeightedEdge)e).weight)
				{
					cost[e.v] = cost[u] + ((WeightedEdge)e).weight;
					parent[e.v] = u; 
				}
			}
		} // End of while

		// Create a ShortestPathTree
		return new ShortestPathTree (sourceVertex, parent, T, cost);
	}

	/** ShortestPathTree is an inner class in WeightedGraph */
	public class ShortestPathTree extends Tree
	{
		private double[] cost; // cost[v] is the cost from v to source

		/** Construct a path */

		public ShortestPathTree (int source, int[] parent, 
								 List<Integer> searchOrder, double[] cost)
		{
			super (source, parent, searchOrder);
			this.cost = cost;
		}

		/** Return the cost for a path from the root to vertex v */

		public double getCost (int v)
		{
			return cost[v];
		}

		/** Print paths from all vertices to the source */

		public String printAllPaths()
		{
			String str = "";
			str += "All shortest paths from " +
					   vertices.get (getRoot()) + " are: \n";

			for (int i = 0; i < cost.length; i++)
			{
				// Print a path from vertex i to the source
				str += printPath (i);
				str += " (cost: " + Math.round(cost[i]*100)/100.0 + ")\n";	// Path cost
			}

			return str;
		}
	}
}
