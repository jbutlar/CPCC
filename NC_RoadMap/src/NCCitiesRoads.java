import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * The NC RoadMap Project:
 * The NCCitiesRoads class creates a graph from city
 * and road information given to it from a file
 */
public class NCCitiesRoads
{
	// Static constants for file names
	private static final String COMMAND_FILE = "src//commands.txt";
	private static final String OUTPUT_FILE = "src//NCRoutesOut.txt";
	private static final String NCMAP_FILE = "src//NCRoadMap.csv";
	private static final String NCCITIES_FILE = "src//NCCities.csv";

	// Static constants for file records
	private static final String CITY_REC = "CITY";
	private static final String ROAD_REC = "ROAD";

	// Static data structures to hold the cities, roads, map and such ...

	// Holds city name and vertex index pairs to facilitate building roads
	private static HashMap<String, Integer> cityMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> citySubMap = new HashMap<String, Integer>();

	// Holds full map, cities and roads
	private static CityRoadMap<City> roadMap = null;	 // full map, cities and roads
	private static List<City> cities = new ArrayList<>(52);
	private static List<WeightedEdge> roads = new ArrayList<>(204);

	// Holds subset map, cities and roads
	private static CityRoadMap<City> subRoadMap = null;	 // partial map
	private static List<City> subCities = new ArrayList<>();
	private static List<WeightedEdge> subRoads = new ArrayList<>();

	/**
	 * Creates an output PrintWriter file and an input Scanner file
	 * Loops through each record in the Command File reading the record into a string
	 * Splits the String into an array of Strings at the ':'
	 * Passes the array into the processCmd method for processing
	 * Each of the commands produce output in the form of a String
	 * This String is sent to the PrintWriter object for output to the file
	 * It is also sent to std out for display on the console
	 */
	public static void main (String[] args) throws Exception
	{
		// Create an output File writer
		File outFile = new File (OUTPUT_FILE);
		PrintWriter writer = new PrintWriter (outFile);

		// Create Scanner for reading in Command File
		File cmdFile = new File (COMMAND_FILE);
		Scanner fin = new Scanner (cmdFile);

		String cmdLine = null;

		System.out.println ("Begin NC Routes Program ");
		System.out.println ("Input: " + cmdFile.getAbsolutePath() + "\n");

		// Loop for each line in the Command File and process it
		// The command output is placed in the result String
		while (fin.hasNext())
		{
			cmdLine = fin.nextLine();
			if (!cmdLine.isEmpty())
			{
				String cmdArray[] = cmdLine.split (":");

				// Each command produces output that is sent to the file

				String result = processCmd (cmdArray);
				writer.println (result);
				System.out.println (result);
			}
		}

		System.out.println (" End NC Routes Program ");
		System.out.println (" Output: " + outFile.getAbsolutePath());

		writer.close();
		fin.close();
	}

	// Calls the appropriate method to process the command.
	public static String processCmd (String[] cmdArray) throws Exception
	{
		String city = "";	 // Holds starting city name for traversals

		String cmd = cmdArray[0].trim();

		// Echo the command
		String result = "Command: " + cmd + "\n";

		if (cmd.equalsIgnoreCase ("BuildMap"))
		{
			result += buildMap() + "\n";
		}
		else if (cmd.equalsIgnoreCase ("BuildSubMap"))
		{
			result += buildSubMap() + "\n";
		}
		else if (cmd.equalsIgnoreCase ("PrintMap"))
		{
			result += roadMap.printRoads();
		}
		else if (cmd.equalsIgnoreCase ("PrintSubMap"))
		{
			result += subRoadMap.printRoads();
		}
		else if (cmd.equalsIgnoreCase ("PrintCities"))
		{
			result += roadMap.printCities() + "\n";
		}
		else if (cmd.equalsIgnoreCase ("PrintSubCities"))
		{
			result += subRoadMap.printCities() + "\n";
		}
		else if (cmd.equalsIgnoreCase ("DFSMap"))
		{
			city = cmdArray[1].trim();
			result += dfs (roadMap, cityMap, city);
		}
		else if (cmd.equalsIgnoreCase ("DFSSubMap"))
		{
			city = cmdArray[1].trim();
			result += dfs (subRoadMap, citySubMap, city);
		}
		else if (cmd.equalsIgnoreCase ("BFSMap"))
		{
			city = cmdArray[1].trim();
			result += bfs (roadMap, cityMap, city);
		}
		else if (cmd.equalsIgnoreCase ("BFSSubMap"))
		{
			city = cmdArray[1].trim();
			result += bfs (subRoadMap, citySubMap, city);
		}
		else if (cmd.equalsIgnoreCase ("MSTMap"))
		{
			result += mst (roadMap)+ "\n";
		}
		else if (cmd.equalsIgnoreCase ("MSTSubMap"))
		{
			result += mst (subRoadMap)+ "\n";
		}
		else if (cmd.equalsIgnoreCase ("ShortPathMap"))
		{
			city = cmdArray[1].trim();
			result += shortestPath (roadMap, cityMap, city)+ "\n";
		}
		else if (cmd.equalsIgnoreCase ("ShortPathSubMap"))
		{
			city = cmdArray[1].trim();
			result += shortestPath (subRoadMap, citySubMap, city)+ "\n";
		}
		else if (cmd.equalsIgnoreCase ("SortCities"))
		{
			result += sortCities (cities);
		}
		else
		{
			result += "Unknown command.";
		}

		return result;
	}

	// Builds CityRoadMap roadMap graph object from the CITY and ROAD records in a file
	public static String buildMap() throws Exception
	{
		int cityIndex = 0;	 // City index in cities ArrayList
		int roadIndex = 0;	 // Road index in roads ArrayList
		String result = "";	 // Message about City and Roads processed
		String line   = "";	 // For reading file record

		// Create Scanner for reading in road map information
		File mapFile = new File (NCMAP_FILE);
		Scanner in = new Scanner (mapFile);


		// Loop: Reads each line in mapFile:
		// First field has either CITY or ROAD to distinguish the record type
		// Uses nextLine() method to read full record from file
		// Uses split method to delimit by comma
		while (in.hasNext())
		{
			line = in.nextLine();

			// Split the CSV file into fields
			String fields[] = line.split (",");

			// The first field indicates either a City or a Road record
			String field1 = fields[0];

			// If the first field says "CITY", then create City object

			if (field1.equals (CITY_REC))
			{
				// City name
				String name = fields[1].trim();

				// City GPS: longitude (X)
				double lon = Double.parseDouble (fields[2].trim());

				// City GPS: latitude (Y)
				double lat = Double.parseDouble (fields[3].trim());

				// City population
				int population = Integer.parseInt (fields[4].trim());

				// Add City object to Vertex ArrayList
				cities.add (new City (name, lon, lat, population, cityIndex));

				// Add City name and index to cityMap HashMap
				cityMap.put (name, cityIndex);

				cityIndex++; // next vertex
			}
			// If the first field says "ROAD", then create Road object
			else if (field1.equals (ROAD_REC))
			{
				// StartCity name
				String startCityName = fields[1].trim();

				// EndCity name
				String endCityName = fields[2].trim();

				// Use cityMap to retrieve vertex index
				// and vertex index to retrieve City object
				City startCity = cities.get (cityMap.get (startCityName));
				City endCity = cities.get (cityMap.get (endCityName));

				// Add Road object to Road Edge ArrayList
				roads.add (new Road (startCity, endCity));
				roadIndex++;
			}
		}
		// Add the processing message to the String result to return
		result += "Processed " + cityIndex + " Cities and " + roadIndex + " Roads";

		// Close the Scanner
		in.close();

		// Build a roadMap CityRaodMap graph object of the cities and roads
		roadMap = new CityRoadMap<City> (cities, roads);

		return result;
	}

    /**
     * Build CityRoadMap subRoadMap graph object from the list of city names in a file
     * 
     *  Reads each line containing a City name and uses city name index from cityMap
     *  uses City index to retrieve City object from cities ArrayList
     *  
     *  Finds the roads for these cities in the adjacency lists for each City
     *        which is an ArrayList of WeightedEdge (Roads) and uses nested loop
     *        for each City in the subCities ArrayList to get its adjList
     *        from the roadMap full graph (getNeighbors method)
     *        for each Road in the adjList, retrieve the adjacent City
     *         
     *         Uses the City name to make sure it is a key in the citySubMap
     *           If so, then it
     *              sets the index of the adjacent City using the citySubMap
     *              Creates a new Road object from the two City objects
     *              Adds the new Road object to the subRoads ArrayList
     *              Increments the roadIndex
     *   Also, uses the subCities ArrayList and the subRoads ArrayList to
     *     build a new subRoadMap CityRoadMap graph object
     */

	public static String buildSubMap() throws Exception
	{
		// City index in new subCities ArrayList
		// This is not the old City index from the full map
		int cityIndex = 0;

		// Road index in new subRoads ArrayList: used printing message 
		int roadIndex = 0;    

		String result = "";	  // Message about success
		String line   = "";	  // For reading file record

		// Create Scanner for reading in the cities 
		// file (NCCITIES_FILE) for this sub map
		File citiesFile = new File (NCCITIES_FILE);
		Scanner sc = new Scanner (citiesFile);

		// Reads each record in mapFile: 
		// Each record should have one City name
		// Use nextLine() method, as City names can have more than one word
		
		while (sc.hasNext())
		{
			

			line = sc.nextLine();


			// Grab the city name from the line, trimming whitespace

			String cityName = line.trim();


			// Use the cityName as a key into the cityMap to retrieve 
			// the old City index in the cities ArrayList

			int oldCityIndex = cityMap.get(cityName);
			
			// Use the old City index to retrieve the City object
			//     from the cities ArrayList                     

			City currentCity = cities.get(oldCityIndex);


			// Set the new City index of the City object 
			// in the subCities ArrayList

			currentCity.setIndex(cityIndex);


			// Add the City to the subCities ArrayList

			subCities.add(cityIndex, currentCity);


			// Add the City name and index to the citySubMap HashMap

			citySubMap.put(cityName, cityIndex);

			// Increment the next new city index         

			cityIndex++;

		// Loop ends
		}

		// Finds the roads for these cities in the adjacency lists of the old roadMap graph.  
		// Skips over the Cities/Roads from the City adjacency lists        
		// Loop for each City in the subCities ArrayList, grabbing the City object

		for (City city: subCities) 
		{
			// Use the City object to retrieve the adjacency list
			// containing an ArrayList of adjacent City objects
			// Use the CityRoadMap getNeighbors method
			List<City> adjList = roadMap.getNeighbors(city);
			
			// Loop for each adjacent City in the adjList, grabbing the City object

			for (City adjCity: adjList)
			{
				// Retrieve the adjacent City name
				String adjCityName = adjCity.getCity();

				// Check the citySubMap to see if the City name is a key
				// Use containsKey method

				if (citySubMap.containsKey(adjCityName)) 
				{
					// Use the adjacent City name as a key into the
					// citySubMap HashMap to retrieve the new City index

					int newCityIndex = citySubMap.get(adjCityName);

					// Set this new index in the adjacent City object

					adjCity.setIndex(newCityIndex);
					// Create a new Road object from the two cities	(city and adjCity)
					//  and add it to the subRoads ArrayList

					Road newRoad = new Road(city, adjCity);
					subRoads.add(newRoad);
					// Increment the Road index

					roadIndex++;
				}

			}			

		}		

		// Both Loops end here
		// Adds the processing message to the String result to return
		result += "Processed " + cityIndex + " Cities and " + roadIndex + " Roads";

		// Close the Scanner
		sc.close();
		// Build a graph using the subCities and subRoads
		subRoadMap = new CityRoadMap<City>(subCities, subRoads);

		return result;
	}

	/**
	 * Depth first search:
	 * This method follows mostly the same algorithm as in the TestDFS class
	 * The HashMap is used to retrieve the City index from the passed in City name
	 * The WeightedGraph (CityRoadMap) methods cannot retrieve the City vertex
	 * object from the city name.
	 */

	public static String dfs (CityRoadMap map, HashMap<String, Integer> indexMap,
							  String cityName) throws Exception
	{
		String result = "";		   // Message to return

		// Call the dfs method of the AbstractGraph<V>.Tree class
		// passing in the index of the City in the vertices array
		AbstractGraph<String>.Tree dfs = map.dfs (indexMap.get(cityName));


		// Retrieve the City search order into an ArrayList of indexes

		List<Integer> searchOrders = dfs.getSearchOrder();


		// Output the number of Cities found

		result += dfs.getNumberOfVerticesFound() +
                " cities are searched in this DFS order starting from " +
                map.getVertex(searchOrders.get(0)) + "\n\n";

		// Loop through the search order ArrayList
		// Output each city name: only displaying 5 cities per line

		for (int i = 0; i < searchOrders.size(); i++)
		{
			result += map.getVertex (searchOrders.get (i));

			if ((i+1) % 5 == 0 || i == searchOrders.size() - 1)
            {
                result += '\n';
            }
            else
            {
                result += " : ";
            }
		}

		result += "\n";

		// Loop through the parent array to display the parent of each City

		for (int i = 0; i < searchOrders.size(); i++)
		{
			if (dfs.getParent (i) != -1)
			{
				result += "Parent of " + map.getVertex(i) +
						" is " + map.getVertex(dfs.getParent(i)) + '\n';
			}
		}


		return result;
	}

	/**
	 * Breadth first search:
	 * This method follows mostly the same algorithm as in the TestBFS class
	 * The HashMap is used to retrieve the City index from the passed in City name
	 * The WeightedGraph (CityRoadMap) methods cannot retrieve the City vertex
	 * object from the city name.
	 */

	public static String bfs (CityRoadMap map, HashMap<String, Integer> indexMap,
							  String cityName) throws Exception
	{
		String result = "";		   // Message to return

		// Call the bfs method of the  AbstractGraph<V>.Tree class
		// passing in the index of the City in the vertices array

		AbstractGraph<String>.Tree bfs = map.bfs (indexMap.get(cityName));


		// Retrieve the City search order into an ArrayList of indexes

		List<Integer> searchOrders = bfs.getSearchOrder();


		// Output the number of Cities found

        result += bfs.getNumberOfVerticesFound() +
                " cities are searched in this BFS order starting from " +
                map.getVertex(searchOrders.get(0)) + "\n\n";


		// Loop through the search order ArrayList
		// Output each city name: only display 5 cities per line

		for (int i = 0; i < searchOrders.size(); i++)
		{
			result += map.getVertex(searchOrders.get(i));

			if ((i+1) % 5 == 0 || i == searchOrders.size() - 1)
            {
                result += '\n';
            }
            else
            {
                result += " : ";
            }
		}

		result += "\n";
		// Loop through the parent array to display the parent of each City

		for (int i = 0; i < searchOrders.size(); i++)
		{
			if (bfs.getParent(i) != -1)
			{
				result += "Parent of " + map.getVertex(i) +
						" is " + map.getVertex(bfs.getParent(i)) + '\n';
			}
		}




		return result;
	}

	/**
	 * Minimum Spanning Tree:
	 * Minimum distance Road travel path going through all the Cities
	 * This method follows mostly the same algorithm as in the TestMinimumSpanningTree class
	 */

	public static String mst (CityRoadMap map) throws Exception
	{
		String result = "";		   // Message to return

		// Build Minimum Spanning Tree

		WeightedGraph<String>.MST tree1 = map.getMinimumSpanningTree();


		// Output the total weight and the tree
		// Round weight to 2 places

		result += "Total weight is " +
                Math.round(tree1.getTotalWeight()* 100.0) / 100.0 + "\n\n" +
                tree1.printTree();

		return result;
	}

    /**
     * Shortest Path:
     * This method follows mostly the same algorithm as in the TestShortestPath class
     * The HashMap is used to retrieve the City index from the passed in City name
     * The WeightedGraph (CityRoadMap) methods cannot retrieve the City vertex
     * object from the city name.
     */

	public static String shortestPath (CityRoadMap map, HashMap<String, Integer> indexMap, String fromCityName)
	{
         String result = "";		   // Message to return

		// Call the getShortestPath method of the WeightedGraph class
		// passing in the index of the fromCity in the vertices array

		WeightedGraph<String>.ShortestPathTree tree1 =
                map.getShortestPath(indexMap.get(fromCityName));


		// Output all the paths from the fromCity

		result = tree1.printAllPaths();


		return result;

	}

	/**
	 * Sorts the cities by population and displays them
	 */

	public static String sortCities (List<City> sortCities)
	{
		String result = "";		   // Message to return

		// Use the Collections.sort() method to do the sort

		Collections.sort(sortCities);
		
		System.out.println("Cities sorted by size:");

		for (int l = 0; l < sortCities.size(); l++)
		{
			result = result + sortCities.get(l).printCity() + "\n";
		}
		
		return result;
	}



}
