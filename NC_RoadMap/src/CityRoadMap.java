import java.util.*;

public class CityRoadMap<V> extends WeightedGraph<V>
{
	/** Constructs an empty road map */

	public CityRoadMap()
	{
	}

    /** Constructs a CityRoadMap using cities and roads stored in lists */

	public CityRoadMap (List<V> vertices, List<WeightedEdge> edges)
	{
        // Let the WeightedGraph superclass build the graph/map
        // Call the superclass constructor passing in parameters
        super(vertices, edges);
	}

    /**
     * Returns the neighbors of the City object vertex
     *    as an ArrayList of City objects
     */

	public List<V> getNeighbors (V v)
	{
        // Creates the ArrayList to return
        ArrayList<V> result = new ArrayList<>();

        // Finds the index of City v
        int index = this.getIndex(v);

        // Loops through the neighbors adjacency list of Edges
        // Then adds the adjacent City to the ArrayList to return
        neighbors.get(index).forEach(e -> result.add(this.getVertex(e.v)));

        // Returns the ArrayList of Vertices (Cities)
        return result;
	}

    /** Displays cities and roads with distances and direction */

	public String printRoads() 
	{
        // Initialize String to return
        StringBuilder roads = new StringBuilder();

        // Loops through the vertices ArrayList, retrieving the City
        // vertex and then the corresponding neighbors adjacency list
        this.getVertices().forEach(v ->
        {
            // Retrieves the vertex and cast it to a City object
            // Calls the printCity method of the City object and adds to String
            roads.append('[').append(((City)v).printCity()).append("]: \n");

            // Loops through the neighbors adjacency list for the adjacent City
            // retrieving each edge
            // Casts the Edge to a Road and calls the Road printRoad method
            // Adds the method output to the string to return
            for (Edge e : neighbors.get(this.getIndex(v)))
            {
                roads.append("  ").append(((Road)e).printRoad()).append('\n');
            }

            roads.append('\n');
        });

        return roads.toString();
	}

    /** Displays cities with GPS coordinates and population */

	public String printCities()
	{
        // Initialize String to return
        StringBuilder cities = new StringBuilder();

        // Loop through the vertices ArrayList, retrieving the City
        this.getVertices().forEach(v ->
        {
            // Retrieve the vertex and cast it to a City object
            // Call the printCity method of the City object	to get
            cities.append(((City)v).printCity()).append('\n');
        });

        return cities.toString();
	}


}