import java.util.Map;
import java.util.TreeMap;
/*
 * Maze Generator
 * @author Ameet Toor
 * @version Winter 2016 (3/12/16)
 */

/**
 * The graph class creates a graph of n * m dimensions.
 */
public class Graph // Got help on the graph from the linkedGraph and MatrixGraph code given in class files.
{
    /**
     * Number of rows.
     */
    private int myNumRows;

    /**
     * Number of columns.
     */
    private int myNumColumns;

    /**
     * Map of the nodes in this graph.
     */
    private Map< String, Node > myNodes;

    /**
     * Array of the nodes in this graph.
     */
    private Node[][] myArray;

    /**
     * Constructor that sets the size of the graph and calls the method to create the graph.
     * @param theNumRows int number of rows of the graph.
     * @param theNumColumns int number of columns of the graph.
     */
    Graph(int theNumRows, int theNumColumns)
    {
        setVariables( theNumRows, theNumColumns );
        createGraph();
    }

    /**
     * Sets the starting values for the fields of this method.
     * @param theNumRows int number of rows of the graph.
     * @param theNumColumns int number of columns of the graph.
     */
    private void setVariables( final int theNumRows, final int theNumColumns )
    {
        myNumRows = theNumRows;
        myNumColumns = theNumColumns;
        myNodes = new TreeMap<>();
        myArray = new Node[ myNumRows ][ myNumColumns ];
    }

    /**
     * Creates graph of nodes using a 2d array.
     */
    private void createGraph()
    {
        //Create nodes.
        int row, col, name;
        name = 0;
        for ( row = 0; row < myNumRows; row++ )
        {
            for ( col = 0; col < myNumColumns; col++ )
            {
                myArray[ row ][ col ] = new Node( "" + ( name ) );
                name++;
            }
        }

        setNeighbors();

        //Add all elements of array to a map.
        Node currentNode;
        for ( row = 0; row < myNumRows; row++ )
        {
            for ( col = 0; col < myNumColumns; col++ )
            {
                currentNode = myArray[ row ][ col ];
                myNodes.put( currentNode.getName(), currentNode );
            }
        }
    }

    /**
     * Connects each node with its adjacent nodes.
     */
    private void setNeighbors()
    {

        int row, col;
        for ( row = 0; row < myNumRows; row++ )
        {
            for ( col = 0; col < myNumColumns; col++ )
            {
                //If the neighbor we are looking at is not null and is within the range of the array.
                // Add as a neighbor.

                //Try to add four neighbors.

                //Right
                int right = col + 1;
                if ( right < myNumColumns && right >= 0 && myArray[ row ][ right ] != null )
                {
                    myArray[ row ][ col ].getNeighbors().add( myArray [ row ][ right ] );
                    myArray[ row ][ col ].setThisNodeToThisWall( myArray[ row ][ right ].getName(), "right" );
                }

                //Left
                int left = col - 1;
                if ( left < myNumColumns && left >= 0 && myArray[ row ][ left ] != null )
                {
                    myArray[ row ][ col ].getNeighbors().add( myArray [ row ][ left ] );
                    myArray[ row ][ col ].setThisNodeToThisWall( myArray[ row ][ left ].getName(), "left" );
                }

                //Bottom
                int bottom = row + 1;
                if ( bottom < myNumRows && bottom >= 0 && myArray[ bottom ][ col ] != null )
                {
                    myArray[ row ][ col ].getNeighbors().add( myArray [ bottom ][ col ] );
                    myArray[ row ][ col ].setThisNodeToThisWall( myArray[ bottom ][ col ].getName(), "bottom" );
                }

                //Top
                int top = row - 1;
                if ( top < myNumRows && top >= 0 && myArray[ top ][ col ] != null )
                {
                    myArray[ row ][ col ].getNeighbors().add( myArray [ top ][ col ] );
                    myArray[ row ][ col ].setThisNodeToThisWall( myArray[ top ][ col ].getName(), "top" );
                }
            }

        }
    }

    /**
     * Returns String representing the graph.
     * @return String of the graph.
     */
    public String toString()
    {
        StringBuilder graphAsString = new StringBuilder();

        int row, col;

        graphAsString.append( "[ " );

        for ( row = 0; row < myNumRows; row++ )
        {
            if ( row > 0 )
            {
                graphAsString.append( "\n" );
            }
            for ( col = 0; col < myNumColumns; col++ )
            {
                if ( myArray[ row ][ col ] != null )
                {
                    graphAsString.append( myArray[ row ][ col ].toStringWithNeighbors() );

                }
            }

        }

        graphAsString.append( " ]\n" );

        return graphAsString.toString();
    }

    /**
     * Returns the graph as a 2d array.
     * @return 2d Node array of the graph.
     */
    Node[][] getGraph()
    {
        return myArray;
    }

    /**
     * Returns Map of the graph. With Keys as String and Values as Nodes.
     * @return Map of the graph.
     */
    Map < String, Node > getMap()
    {
        return myNodes;
    }
}