//Naruth Kongurai (1429760)
//CSE 373 AB
//TA: Raquel Van Hofwegen
//03/03/17

import java.util.*;

/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    
    private final Map<Vertex, HashSet<Edge>> myGraphMap;
    private final Map<Vertex, HashSet<Vertex>> vertexAdjacencyList;
    private final Set<Edge> setOfEdges;

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
        if (v == null) {
            throw new IllegalArgumentException(
                    "Vertices cannot be null");
        }
        if (e == null) {
            throw new IllegalArgumentException(
                    "Edges cannot be null");
        }
        
        this.myGraphMap = new HashMap<Vertex, HashSet<Edge>>();
        this.vertexAdjacencyList = new HashMap<Vertex, HashSet<Vertex>>();
        this.setOfEdges = new HashSet<Edge>();
        
        for (Edge edge : e) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edge " + edge.toString() +
                        " must have a positive weight.");
            }
           
            Vertex sourceVertex = edge.getSource();
            Vertex destinationVertex = edge.getDestination();
            int weight = edge.getWeight();
            Edge copyEdge = new Edge(sourceVertex, destinationVertex, weight);

            if (v.contains(sourceVertex) && v.contains(destinationVertex)) {
                
                Vertex vCopy = new Vertex(destinationVertex.toString());
                
                // Any adjacent vertices corresponding to the particular vertex
                if (!this.vertexAdjacencyList.containsKey(sourceVertex)) {
                    this.vertexAdjacencyList.put(
                            sourceVertex, new HashSet<Vertex>());
                }
                this.vertexAdjacencyList.get(sourceVertex).add(vCopy);
                
                // Adds corresponding edges to an existing vertex
                if (!this.myGraphMap.containsKey(sourceVertex)) {
                    this.myGraphMap.put(sourceVertex, new HashSet<Edge>());
                }
                this.myGraphMap.get(sourceVertex).add(copyEdge);
                
                for (Edge item : this.setOfEdges) {
                    // check for same edges but different weight
                    if ((item.getSource() == sourceVertex) 
                            && (item.getDestination() == destinationVertex)
                            && (item.getWeight() != weight)) {
                        throw new IllegalArgumentException("Edge "
                                + edge.toString() + " with a different weight"
                                + " already exist.");  
                    }
                }
                this.setOfEdges.add(edge);
            } else {
                throw new IllegalArgumentException("No vertices in the graph "
                        + "have edge " + edge.toString() + ".");
            }
        }
        
        for (Vertex item : v) {
        	if (!this.myGraphMap.containsKey(item)) {
                this.myGraphMap.put(item, null);
            }
        }
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    @Override
    public Collection<Vertex> vertices() {
        Set<Vertex> verticesCollection = new HashSet<Vertex>();
        for (Vertex item : this.myGraphMap.keySet()) {
        	Vertex copyVertex = new Vertex(item.getLabel());
            verticesCollection.add(copyVertex);
        }
        return verticesCollection;
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    @Override
    public Collection<Edge> edges() {
        Set<Edge> edgesCollection = new HashSet<Edge>();
        for (Edge item : this.setOfEdges) {
        	Edge copyEdge = new Edge(item.getSource(), item.getDestination(), item.getWeight());
            edgesCollection.add(copyEdge);
        }
        return edgesCollection;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    @Override
    public Collection<Vertex> adjacentVertices(Vertex v) {
        this.checkVertexExists(v);
        Set<Vertex> adjacentVerticesSet = new HashSet<Vertex>();
        for (Vertex item : this.vertexAdjacencyList.get(v)) {
        	Vertex copyVertex = new Vertex(item.getLabel());
            adjacentVerticesSet.add(copyVertex);
        }
        return adjacentVerticesSet;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    @Override
    public int edgeCost(Vertex a, Vertex b) {
        this.checkVertexExists(a);
        this.checkVertexExists(b);
        
        int value = -1;
        for (Edge edge : this.myGraphMap.get(a)) {
            if (edge.getDestination().equals(b)) {
                return edge.getWeight();
            }
        }
        return value;
    }
    
    /*
     * Checks if the given vertex exists in the collection of vertices and edges
     * @param vertex the input vertex
     * @throws IllegalArgumentException if vertex does not exist.
     */
    private void checkVertexExists(Vertex vertex) {
        if (!this.myGraphMap.containsKey(vertex)) {
            throw new IllegalArgumentException("Vertex " + vertex.toString() +
                    " does not exist");
        }
    }
    
    
    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	this.checkVertexExists(a);
    	this.checkVertexExists(b);
    	
    	// EDGE CASE: Same vertices (have same edges)
    	if (a.equals(b)) {
            List<Vertex> tempList = new ArrayList<Vertex>();
            Vertex tempA = new Vertex(a.getLabel());
            tempList.add(tempA);
            return new Path(tempList, 0);
        }

    	// Stores vertex and its new vertex information
    	Map<Vertex, Vertex> vertexPointers = new HashMap<Vertex, Vertex>();
    	for (Vertex v : this.vertices()) {
    		vertexPointers.put(v, new Vertex(v.getLabel()));
    	}

    	// Queue that stores vertices and their respected values
    	PriorityQueue<Vertex> pq = new PriorityQueue<>();
    	
       	// Initialize start node
    	Vertex start = new Vertex(a.getLabel());
    	start.setCost(0);
    	start.setPath(null);
    	pq.add(start);
    	vertexPointers.put(a, start);
  
    	Vertex end = null; 	// our preliminary destination
    						// if unreachable, return null
    	
    	while (!pq.isEmpty()) {
    		Vertex currentItem = pq.remove();
    		
    		if (!currentItem.getKnown()) { 
	    		currentItem.setKnown(true);
	    		
	    		// Look at every vertex and update cost if necessary
	    		for (Vertex v : this.vertexAdjacencyList.get(currentItem)) {
	    			int c1 = vertexPointers.get(currentItem).getCost() + 
	    					this.edgeCost(currentItem, v);
	    			int c2 = vertexPointers.get(v).getCost();
	    			// Update cost via decreaseKey method
	    			if (c1 < c2) {
	    				Vertex updatedV = new Vertex(v.getLabel());
	    				updatedV.setCost(c1);
	    				updatedV.setPath(currentItem);
	    				vertexPointers.put(v, updatedV);
	    				pq.add(updatedV);
        				if (updatedV.equals(b)) {
        					end = updatedV;
        				}
	    			}
	    		}
	    		
    		}
    	}
    	
    	// Appends the path if we can find the destination
    	if (end != null) {
    		return this.appendsPath(end, vertexPointers);
    	} // Otherwise, return null
    	return null;
    }
    
    /* 
     * Returns a Path that contains all the vertices starting from the
     * destination vertex to the source vertex and the total cost. Assumes
     * that the returned Path is not null.
     * @param end the last vertex (destination)
     * @param vertexPointers association of vertices and their updated 
     *        vertices information
     */
    private Path appendsPath(Vertex end, Map<Vertex, Vertex> vertexPointers) {
		List<Vertex> pathTracker = new ArrayList<>();
		pathTracker.add(end);
    	int totalCost = vertexPointers.get(end).getCost();
    		
    	// Continue backtracking to find the original source while adding
    	// each path that is found to the pathTracker
    	while(end.getPath() != null) {
    		Vertex tempV = new Vertex(end.getPath().getLabel());
    		pathTracker.add(tempV);
    		end = end.getPath();
    	}
    	
    	Collections.reverse(pathTracker); // Reverse paths to make sense
    	return new Path(pathTracker, totalCost);
    }
     
}