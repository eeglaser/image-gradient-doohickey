package main.java.structures.graph;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class represents a Complete Graph, with each node connected to all other nodes. The NodeType
 * is the object or data used as a node in the graph. The EdgeType is the type of Number used to
 * weight the edges.
 */
public class CompleteGraph<NodeType, EdgeType extends Number> {

  /** This Map maps each node's data to its edges. */
  Map<NodeType, Map<NodeType, EdgeType>> map;

  /**
   * Constructor
   * 
   * @param map The Map used to construct this complete graph.
   */
  public CompleteGraph(Map<NodeType, Map<NodeType, EdgeType>> map) {
    this.map = map;
  }

  /**
   * Adds a list of nodes to the complete graph. Does not add duplicates.
   * 
   * @param nodes        The List of nodes to add to the graph
   * @param weightFinder A BiFunction used to find the weight between any two nodes. Takes two
   *                     objects of NodeType as input, and outputs an EdgeType
   */
  public void addNodes(List<NodeType> nodes,
      BiFunction<NodeType, NodeType, EdgeType> weightFinder) {
    // TODO
  }

  /**
   * Removes a list of nodes from the graph, if they exist.
   * 
   * @param nodes The nodes to try and remove from the graph
   */
  public void removeNodes(List<NodeType> nodes) {
    // TODO
  }

  /**
   * Checks whether the graph contains the specified node already
   * 
   * @param node The node to check for
   * @return True if the node is in the graph, false otherwise
   */
  public boolean containsNode(NodeType node) {
    return map.containsKey(node);
  }
  
  /**
   * Reports the number of nodes currently in the graph.
   * @return number of nodes in the graph
   */
  public int getNodeCount() {
    return map.size();
  }
  
  /**
   * Reports the number of edges currently in the complete graph.
   * @return number of edges in the graph
   */
  public int getEdgeCount() {
    int size = map.size();
    return (size * (size-1))/2;
  }
}
