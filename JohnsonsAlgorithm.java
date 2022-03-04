/**
 * Write a description of class JohnsonsAlgorithm here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class JohnsonsAlgorithm
{
    private final int source;
    private final int nodes;
    private final int[][] augmentedMatrix;
    private int[] potential;
    private final BellmanFord bellmanFord;
    private final Dijkstra dijsktra;
    private final int[][] allPairShortestPath;

    public static final int max = 999;

    public JohnsonsAlgorithm(int nodes)
    {
        this.nodes = nodes;
        augmentedMatrix = new int[nodes + 2][nodes + 2];
        source = nodes + 1;
        potential = new int[nodes + 2];
        bellmanFord = new BellmanFord(nodes + 1);
        dijsktra = new Dijkstra(nodes);
        allPairShortestPath = new int[nodes + 1][nodes + 1];
    }

    public void johnsonsAlgorithms(int[][] adjacencyMatrix)
    {
        computeAugmentedGraph(adjacencyMatrix);

        bellmanFord.BellmanFordEvaluation(source, augmentedMatrix);
        potential = bellmanFord.getDistances();

        int[][] reweightedGraph = reweightGraph(adjacencyMatrix);
        for (int i = 1; i <= nodes; i++)
        {
            for (int j = 1; j <= nodes; j++)
            {
                System.out.print(reweightedGraph[i][j] + "\t");
            }
            System.out.println();
        }

        for (int source = 1; source <= nodes; source++)
        {
            dijsktra.dijkstra(source, reweightedGraph);
            int[] result = dijsktra.getDistances();
            for (int destination = 1; destination <= nodes; destination++)
            {
                allPairShortestPath[source][destination] = result[destination]
                        + potential[destination] - potential[source];
            }
        }

        System.out.println();
        for (int i = 1; i<= nodes; i++)
        {
            System.out.print("\t"+i);
        }
        System.out.println();
        for (int source = 1; source <= nodes; source++)
        {
            System.out.print( source +"\t" );
            for (int destination = 1; destination <= nodes; destination++)
            {
                System.out.print(allPairShortestPath[source][destination]+ "\t");
            }
            System.out.println();
        }
    }

    private void computeAugmentedGraph(int[][] adjacencyMatrix)
    {
        for (int source = 1; source <= nodes; source++)
        {
            System.arraycopy(adjacencyMatrix[source], 1, augmentedMatrix[source], 1, nodes);
        }
        for (int destination = 1; destination <= nodes; destination++)
        {
            augmentedMatrix[source][destination] = 0;
        }
    }

    private int[][] reweightGraph(int[][] adjacencyMatrix)
    {
        int[][] result = new int[nodes + 1][nodes + 1];
        for (int source = 1; source <= nodes; source++)
        {
            for (int destination = 1; destination <= nodes; destination++)
            {
                result[source][destination] = adjacencyMatrix[source][destination]
                        + potential[source] - potential[destination];
            }
        }
        return result;
    }

    public static void main(String... arg)
    {
        int[][] adjacency_matrix;
        int number_of_vertices;
        Scanner scan = new Scanner(System.in);

        try
        {
            System.out.println("Enter the number of planets");
            number_of_vertices = scan.nextInt();
            adjacency_matrix = new int[number_of_vertices + 1][number_of_vertices + 1];

            System.out.println("Enter the Weighted Matrix for the known distances between planets");
            for (int i = 1; i <= number_of_vertices; i++)
            {
                for (int j = 1; j <= number_of_vertices; j++)
                {
                    adjacency_matrix[i][j] = scan.nextInt();
                    if (i == j)
                    {
                        adjacency_matrix[i][j] = 0;
                        continue;
                    }
                    if (adjacency_matrix[i][j] == 0)
                    {
                        adjacency_matrix[i][j] = max;
                    }
                }
            }

            JohnsonsAlgorithm johnsonsAlgorithm = new JohnsonsAlgorithm(number_of_vertices);
            johnsonsAlgorithm.johnsonsAlgorithms(adjacency_matrix);
        } catch (InputMismatchException inputMismatch)
        {
            System.out.println("Wrong Input Format");
        }
        scan.close();
    }
}

class BellmanFord
{
    private final int[] distances;
    private final int numberofvertices;

    public static final int MAX_VALUE = 999;

    public BellmanFord(int numberofvertices)
    {
        this.numberofvertices = numberofvertices;
        distances = new int[numberofvertices + 1];
    }

    public void BellmanFordEvaluation(int source, int[][] adjacencymatrix)
    {
        for (int node = 1; node <= numberofvertices; node++)
        {
            distances[node] = MAX_VALUE;
        }

        distances[source] = 0;

        for (int node = 1; node <= numberofvertices - 1; node++)
        {
            for (int sourcenode = 1; sourcenode <= numberofvertices; sourcenode++)
            {
                for (int destinationnode = 1; destinationnode <= numberofvertices; destinationnode++)
                {
                    if (adjacencymatrix[sourcenode][destinationnode] != MAX_VALUE)
                    {
                        if (distances[destinationnode] > distances[sourcenode]
                                + adjacencymatrix[sourcenode][destinationnode])
                        {
                            distances[destinationnode] = distances[sourcenode]
                                    + adjacencymatrix[sourcenode][destinationnode];
                        }
                    }
                }
            }
        }

        for (int sourcenode = 1; sourcenode <= numberofvertices; sourcenode++)
        {
            for (int destinationnode = 1; destinationnode <= numberofvertices; destinationnode++)
            {
                if (adjacencymatrix[sourcenode][destinationnode] != MAX_VALUE)
                {
                    if (distances[destinationnode] > distances[sourcenode]
                            + adjacencymatrix[sourcenode][destinationnode])
                        System.out.println("The Graph contains negative edge cycle");
                }
            }
        }
    }

    public int[] getDistances()
    {
        return distances;
    }
}

class Dijkstra
{
    private boolean[] settled;
    private boolean[] unsettled;
    private int[] distances;
    private int[][] adjacencymatrix;
    private final int numberofvertices;

    public static final int MAX_VALUE = 999;

    public Dijkstra(int numberofvertices)
    {
        this.numberofvertices = numberofvertices;
    }

    public void dijkstra(int source, int[][] adjacencymatrix)
    {
        this.settled = new boolean[numberofvertices + 1];
        this.unsettled = new boolean[numberofvertices + 1];
        this.distances = new int[numberofvertices + 1];
        this.adjacencymatrix = new int[numberofvertices + 1][numberofvertices + 1];

        int evaluationnode;
        for (int vertex = 1; vertex <= numberofvertices; vertex++)
        {
            distances[vertex] = MAX_VALUE;
        }

        for (int sourcevertex = 1; sourcevertex <= numberofvertices; sourcevertex++)
        {
            System.arraycopy(adjacencymatrix[sourcevertex], 1, this.adjacencymatrix[sourcevertex], 1, numberofvertices);
        }

        unsettled[source] = true;
        distances[source] = 0;
        while (getUnsettledCount(unsettled) != 0)
        {
            evaluationnode = getNodeWithMinimumDistanceFromUnsettled(unsettled);
            unsettled[evaluationnode] = false;
            settled[evaluationnode] = true;
            evaluateNeighbours(evaluationnode);
        }
    }

    public int getUnsettledCount(boolean[] unsettled)
    {
        int count = 0;
        for (int vertex = 1; vertex <= numberofvertices; vertex++)
        {
            if (unsettled[vertex])
            {
                count++;
            }
        }
        return count;
    }

    public int getNodeWithMinimumDistanceFromUnsettled(boolean[] unsettled)
    {
        int min = MAX_VALUE;
        int node = 0;
        for (int vertex = 1; vertex <= numberofvertices; vertex++)
        {
            if (unsettled[vertex] && distances[vertex] < min)
            {
                node = vertex;
                min = distances[vertex];
            }
        }
        return node;
    }

    public void evaluateNeighbours(int evaluationNode)
    {
        int edgeDistance;
        int newDistance;

        for (int destinationNode = 1; destinationNode <= numberofvertices; destinationNode++)
        {
            if (!settled[destinationNode])
            {
                if (adjacencymatrix[evaluationNode][destinationNode] != MAX_VALUE)
                {
                    edgeDistance = adjacencymatrix[evaluationNode][destinationNode];
                    newDistance = distances[evaluationNode] + edgeDistance;
                    if (newDistance < distances[destinationNode])
                    {
                        distances[destinationNode] = newDistance;
                    }
                    unsettled[destinationNode] = true;
                }
            }
        }
    }

    public int[] getDistances()
    {
        return distances;
    }
}
