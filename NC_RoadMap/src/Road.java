import java.text.DecimalFormat;

public class Road extends WeightedEdge implements Comparable<WeightedEdge>
{
	private City startCity;	  // The city at the start of the road
	private City endCity;	  // The city at the end of the road
	private double distance;  // The distance in miles from startCity to endCity 
	private String direction; // The direction of travel on the road
							  // from startCity to endCity

	/** Construct a Road object and initialize all the instance variables */

	public Road (City c1, City c2)
	{
		// Pass the indices of the vertices from the 
		// Vertex List to the grandparent Edge
		super (c1.getIndex(), c2.getIndex(), 0);

		// Initialize the endpoint cities
        startCity = c1;
        endCity = c2;
		// add code here

		// Calling this method computes the direction of the Road
		// object and the distance between the City vertices and
		// sets the corresponding instance variables and the 
		// weight instance variable of the parent WeightedEdge
		computeDirection();
	}

	/** Return the distance */

	public double getDistance()
	{
		return distance;
	}

	/** Return the direction */

	public String getDirection()
	{
		return direction;
	}

	/** Compare two Road edges by their weights */

	public int compareTo (Road edge)
	{
		double thisRoad = getDistance();
		double thatRoad = edge.getDistance();
		int compareValue = -2;
		
		if(thisRoad > thatRoad)
		{
			compareValue = 1;
		}
		else if (thisRoad < thatRoad)
		{
			compareValue = -1;
		}
		else if (thisRoad == thatRoad)
		{

			compareValue = 0;
		}

		return compareValue;
	}

	/** 
	 * Calling computeDirection computes the direction of the Road
	 * object and the distance between the City vertices and
	 * sets the corresponding instance variables:
	 *      distance              
	 *      weight              
	 *      direction
	 */

	private void computeDirection()
	{
		// Converts the startCity (PointA) and endCity (PointB) 
		// GPS coordinates from degrees into radians

		// Point A: (x1, y1): startCity
		double x1 = Math.toRadians (startCity.getLongitude());
		double y1 = Math.toRadians (startCity.getLatitude());

		// Point B: (x2, y2): endCity
		double x2 = Math.toRadians (endCity.getLongitude());
		double y2 = Math.toRadians (endCity.getLatitude());

		// Find quadrant
		int quad = findQuadrant (x1, y1, x2, y2);

		// Uses quadrant the determine coordinates of Point C: (x3, y3)
		// These are for Quad 2 or Quad 4
		double x3 = x1;
		double y3 = y2;

		if (quad == 1 || quad == 3)
		{
			// These are for Quad 1 or Quad 3
			x3 = x2;
			y3 = y1;
		}

		// Compute length of sides a, b, and c which are across 
		// from points (and angles) A, B and C, respectively

		double sideA = distance (x2, y2, x3, y3);
		double sideB = distance (x1, y1, x3, y3);
		double sideC = distance (x1, y1, x2, y2);

		// Compute the angle at pointA in degrees.
		// The order of these three sides is important:
		//    sideA: across from angle A - the direction being calculated
		//    sideB: across from angle B
		//    sideC: across from angle C which is the right angle
		//           and the side that is the path from PointA (x1, y1)
		//           to PointB (x2, y2)

		double angleA = compAngle (sideA, sideB, sideC);

		// Set distance, weight and direction
		// Note: Nothing rounded here to ensure precision


		// Convert the sideC length (distance from PtA to PtB) into miles
		distance = sideC * 3956;                  

		// The distance is also the weight
		weight = distance;

		// Use angleA and the quadrant to find 
		// the direction of travel from PointA to PointB
		direction = compDirection (angleA, quad);
	}

	/** 
	 * Assuming that PointA (x1, y1) is at the origin,
	 * Find the Quadrant containing PointB (x2, y2)
	 * This is accomplished by comparing both x1 to x2 and y1 to y2
	 */

	private int findQuadrant (double x1, double y1, double x2, double y2)
	{
		int quad = 0;
		if (x1 < x2 && y1 <= y2)
		{
			quad = 1;
		}
		else if (x1 >= x2 && y1 < y2)
		{
			quad = 2;
		}
		else if (x1 > x2 && y1 >= y2)
		{
			quad = 3;
		}
		else if (x1 <= x2 && y1 > y2)
		{
			quad = 4;
		}
		return quad;
	}

	/** 
	 * Compute the distance between PointA (x1,y1) and PointB (x2,y2) 
	 *  Return the distance rounded to two decimal places
	 */

	private double distance (double x1, double y1, double x2, double y2)
	{
		return Math.sqrt ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	/** Compute AngleA at PointA from the sides of the triangle */

	private double compAngle (double sideA, double sideB, double sideC)
	{
		return Math.toDegrees (Math.acos(((sideB * sideB) + 
										  (sideC * sideC) - (sideA * sideA)) / 
										 (2 * sideB * sideC)));
	}

	/** 
	  * Compute the direction of the line between PointA to PointB,
	  * Using the angle at PointA and the quadrant PointA is in
	  */

	private String compDirection (double angle, int quadrant)
	{
		String dirStr = "None";

		switch (quadrant)
		{
		case 1:
			{
				if (angle <= 11.25)
				{
					dirStr = "E";
				}
				else if (angle <= 33.75)
				{
					dirStr = "ENE";
				}
				else if (angle <= 56.25)
				{
					dirStr = "NE";
				}
				else if (angle <= 78.75)
				{
					dirStr = "NNE";
				}
				else // angle 78.75 -> 90
				{
					dirStr = "N";
				}
				break;
			}
		case 2:
			{
				if (angle <= 11.25)
				{
					dirStr = "N";
				}
				else if (angle <= 33.75)
				{
					dirStr = "NNW";
				}
				else if (angle <= 56.25)
				{
					dirStr = "NW";
				}
				else if (angle <= 78.75)
				{
					dirStr = "WNW";
				}
				else // angle 78.75 -> 90
				{
					dirStr = "W";
				}
				break;
			}
		case 3:
			{
				if (angle <= 11.25)
				{
					dirStr = "W";
				}
				else if (angle <= 33.75)
				{
					dirStr = "WSW";
				}
				else if (angle <= 56.25)
				{
					dirStr = "SW";
				}
				else if (angle <= 78.75)
				{
					dirStr = "SSW";
				}
				else // angle 78.75 -> 90
				{
					dirStr = "S";
				}
				break;
			}
		case 4:
			{
				if (angle <= 11.25)
				{
					dirStr = "S";
				}
				else if (angle <= 33.75)
				{
					dirStr = "SSE";
				}
				else if (angle <= 56.25)
				{
					dirStr = "SE";
				}
				else if (angle <= 78.75)
				{
					dirStr = "ESE";
				}
				else // angle 78.75 -> 90
				{
					dirStr = "E";
				}
				break;
			}
		}
		return dirStr;
	}

	/**
	 * Return the Road info as a String:
	 * Round distance to 2 places for display.
	 */

	public String printRoad()
	{
		DecimalFormat df = new DecimalFormat("#.0#");
		String beginCity = startCity.getCity();
		String endPoint = endCity.getCity();
		String theDistance = String.format("%s miles", df.format(distance));
		String theDirection = getDirection();
		String roadInfo =
		beginCity + " to " + endPoint + " traveling " + theDirection +
		" for " + theDistance;

		return roadInfo;

		
	}

	/**
	 * Return the Road info as a String containing City names
	 * Road: Charlotte to Greensboro
	 */

	public String toString()
	{
		
		String beginCity = startCity.getCity();
		String endPoint = endCity.getCity();
		String aRoad = "Road: " + beginCity + " to " + endPoint;

		return aRoad;
	}
}

