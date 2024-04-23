// === CS400 File Header Information ===
// Name: Emma Glaser
// Email: eeglaser@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader: wergh
package backend;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referenced by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new PlaceholderMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end)
      throws NoSuchElementException {
    // TODO you probably have to debug the shit outta this
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start and end nodes must be in the graph!");
    }

    PriorityQueue<SearchNode> edgeQueue = new PriorityQueue<SearchNode>();
    PlaceholderMap<Node, Node> visitedNodesMap = new PlaceholderMap<Node, Node>();

    edgeQueue.add(new SearchNode(nodes.get(start), 0, null));
    SearchNode resultNode = null;
    while (!edgeQueue.isEmpty()) {
      SearchNode currNode = edgeQueue.poll(); // this queue has weird method names
      // If destination is unvisited...
      if (!visitedNodesMap.containsKey(currNode.node)) { // TODO is this what they wanted
        // Mark as visited
        visitedNodesMap.put(currNode.node, currNode.node);
        // If destination is our end node...
        if (currNode.node.data.equals(end)) {
          // We found it yippee!
          resultNode = currNode;
          break;
        }
        // For each edge leading to an unvisited neighbor...
        for (Edge e : currNode.node.edgesLeaving) {
          if (!visitedNodesMap.containsKey(e.successor)) {
            // Add potential edge to queue
            edgeQueue.add(new SearchNode(nodes.get(e.successor.data),
                ((Number) currNode.cost).doubleValue() + ((Number) e.data).doubleValue(),
                currNode));
          }
        }
      }
    }

    // If resultNode was never set, then we never found a path from start to end
    if (resultNode == null) {
      throw new NoSuchElementException("No path between start and end exists!");
    }

    return resultNode;
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end)
      throws NoSuchElementException {
    // Initialize and run shortest path algorithm
    SearchNode resultNode = computeShortestPath(start, end);
    ArrayList<NodeType> resultList = new ArrayList<NodeType>();

    // Populate our list of nodes in the path
    resultList.add(resultNode.node.data);
    SearchNode pred = resultNode.predecessor;

    while (pred != null) {
      resultList.add(pred.node.data);
      pred = pred.predecessor;
    }

    // Put list in proper order and return
    Collections.reverse(resultList);
    return resultList;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  public double shortestPathCost(NodeType start, NodeType end) throws NoSuchElementException {
    // Initialize and run shortest path algorithm
    SearchNode resultNode = computeShortestPath(start, end);
    // Return cumulative cost, which is stored cumulatively in SearchNodes already
    return resultNode.cost;
  }

}
