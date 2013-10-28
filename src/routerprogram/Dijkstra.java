/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;

import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

class Vertex implements Serializable, Comparable<Vertex>
{
    public final String name;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge implements Serializable
{
    public final Vertex target;
    public final int weight;
    public Edge(Vertex argTarget, int argWeight)
    { target = argTarget; weight = argWeight; }
}

public class Dijkstra
{
    PacketList pl = PacketList.getInstance();
    Config c = Config.getInstance();
    
    public Dijkstra(){
        ArrayList<Vertex> vertices = new ArrayList();
        HashMap<String, Vertex> ver = new HashMap();

        for(LinkStatePacket lsp : pl.getList()){
           ver.put(lsp.name, new Vertex(lsp.name));     
        }
        
        for(LinkStatePacket lsp : pl.getList()){
            Vertex v = ver.get(lsp.name);
            ArrayList<Edge> edges = new ArrayList();
            for (Map.Entry<String, String> entry : lsp.neighbors.entrySet()){ 
                edges.add(new Edge(ver.get(entry.getKey()), Integer.parseInt(entry.getValue())));
            }
            v.adjacencies = edges.toArray(new Edge[edges.size()]);
        }
        
        computePaths(ver.get(c.ROUTER));
        for (Map.Entry<String, Vertex> entry : ver.entrySet())
        {  
            System.out.println("Distance to " + entry.getValue() + ": " + entry.getValue().minDistance);
            List<Vertex> path = getShortestPathTo(entry.getValue());
	    System.out.println("Path: " + path);
        }
    }
    
    public static void computePaths(Vertex source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
      	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
		if (distanceThroughU < v.minDistance) {
		    vertexQueue.remove(v);
		    v.minDistance = distanceThroughU ;
		    v.previous = u;
		    vertexQueue.add(v);
		}
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
}
