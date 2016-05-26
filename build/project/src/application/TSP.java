package application;

/**
 * @author GladeJoa
 * 
 * Traveling Salesman Problem:
 * Find the shortest route among a set, that visits each node exactly once
 * and both start/end in the same node.
 */
public class TSP {

	/**
	 * The coordinates to find shortest path among.
	 */
	private Coordinate[] elements;
	
	/**
	 * The distance table.
	 * Must be updated each time a coordinate changes.
	 */
	private double[][] distances;
	
	/**
	 * The number of coordinates.
	 */
	private int n;
	
	private double currentLen, bestLen;
	private int[] currentPath, bestPath;
	private boolean[] visited;
	
	public TSP(Coordinate[] oe) {
		elements = oe;
		n = elements.length;
		distances = new double[n][n];
		currentPath = new int[n + 1];
		bestPath = new int[n + 1];
		visited = new boolean[n];
		bestLen = Double.MAX_VALUE;
		visited[0] = true;
		
		createDistances();
		calculateOptimalTour(0, 1);
	}
	
	/**
	 * Create a distance table that holds
	 * all distances between every pair of coordinates.
	 */
	private void createDistances() {
		for(int i = 0; i < n; i++) {
			distances[i] = new double[n];
			for(int j = 0; j < n; j++) {
				distances[i][j] = distances[j][i] = Math.abs(elements[i].getCoordinate().distance(elements[j].getCoordinate()));
			}
		}
	}
	
	/**
	 * Assuming no more than 10 elements will be used a simple
	 * brute force approach is just fine. 
	 */
	private void calculateOptimalTour(int from, int level) {
		if (level == n && currentLen + distances[from][0] < bestLen) {
			bestLen = currentLen + distances[from][0];
			System.arraycopy(currentPath, 0, bestPath, 0, n);
		} else {
			for (int i = 0; i < n; i++) {
				double dis = distances[from][i];
				if (dis != 0 && !visited[i] && currentLen + dis < bestLen) {
					currentLen += dis;
					currentPath[level] = i;
					visited[i] = true;
					calculateOptimalTour(i, level + 1);
					currentLen -= dis;
					visited[i] = false;
				}
			}
		}
	}
	
	/**
	 * Fetch the optimal route found among the given
	 * coordinates.
	 */
	public int[] getOptimalPath() {
		return bestPath;
	}
}