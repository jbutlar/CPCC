import java.text.DecimalFormat;

public class City implements Comparable<City>
{
	private String city;	 // City name
	private double gpsX;	 // Longitide in degrees
	private double gpsY;	 // Latitide in degrees
	private int population;	 // Population size
	private int vertIndex;	 // index in the Vertex ArrayList

	/** Constructs a City object and initialize the instance variables */

	public City (String name, double x, double y, int size, int index)
	{
		city = name;
		gpsX = x;
		gpsY = y;
		population = size;
		vertIndex = index;
	}

	/** Returns the City name */

	public String getCity()
	{
		return city;
	}

	/** Returns the City longitude */

	public double getLongitude()
	{
		return gpsX;
	}

	/** Returns the City latitude */

	public double getLatitude()
	{
		return gpsY;
	}

	/** Returns the City poulation */

	public int getPopulation()
	{
		return population;
	}

	/** Returns the City index in the vertex ArrayList */

	public int getIndex()
	{
		return vertIndex;
	}

	/** Sets the City index of the vertex ArrayList */

	public void setIndex (int index)
	{
		vertIndex = index;
	}

	/** Compares the City populations */

	@Override
	public int compareTo (City c)
	{
		int thisPop = getPopulation();
		int thatPop = c.getPopulation();
      int compareValue = -2;
		if (thisPop > thatPop)
		{
			compareValue = 1;
		}
		else if (thisPop < thatPop)
		{
			compareValue = -1;
		}
		else if (thisPop == thatPop)
		{
			compareValue = 0;
		}
      
      return compareValue;
	}

	/** Return true, when the City names are the same */

	@Override
	public boolean equals (Object c)
	{
		
		boolean sameName = false;
		City otherCity = (City) c;
		if (this.city.equalsIgnoreCase(otherCity.getCity()))
		{
			sameName = true;
		}
	
      return sameName;
      
	}

	/**
	 * Return the City info as a String
	 * Example: Mars Hill: [1]:[82.55 W, 35.83 N]:(1869)
	 * Rounds the GPS coordinates to 2 decimal places for display
	 */

	public String printCity()
	{
        DecimalFormat df = new DecimalFormat("#.0#");
        String cityInfo =
        String.format(getCity() + ": [" + getIndex() + "]" + ":["
                        + "%s, %s" + "]:(" + getPopulation()
                        + ")", df.format(gpsX), df.format(gpsY));

        return cityInfo;

	}

	/** Return the City name as a String */

	public String toString()
	{
		return city;
	}
}

