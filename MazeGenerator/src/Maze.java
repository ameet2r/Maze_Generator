/*
 * Maze Generator
 * @author Ameet Toor
 * @version Winter 2016 (3/12/16)
 */

import java.util.*;

/**
 * This class creates a maze the size of the given width and depth.
 * This class will create the maze, show the steps of creating the maze if the given value debug is true.
 * And will solve the maze.
 */
public class Maze
{
    /**Width of the maze.*/
    private final int myWidth;

    /**Depth of maze.*/
    private final int myDepth;

    /**Flag for whether to show how the maze is created.*/
    private final boolean myDebug;

    /**The graph of the maze.*/
    private Graph myGraph;

    /**
     * Constructor that assigns the width and depth to the given values.
     * And calls the methods to create the graph, create the path and to solve the maze.
     *
     * @param width The int width of the maze.
     * @param depth The int depth of the maze
     * @param debug The boolean flag deciding whether to debug or not. True means to debug, false not to debug.
     */
    Maze(int width, int depth, boolean debug)
    {
        myWidth = width;
        myDepth = depth;
        myDebug = debug;
        myGraph = new Graph( width, depth );
        createPath();
    }

    /**
     * Creates spanning tree inside of the maze as well as calling the method to solve the maze.
     * Before calling the spanning tree method and the maze solving method, this method chooses the starting and ending
     * point of the maze.
     *
     */
    private void createPath()
    {
        //Get list of all edges.
        final Node array[][] = myGraph.getGraph();
        Node currentNode;
        ArrayList< Node > listOfEdges = new ArrayList<>();
        for ( int row = 0; row < myWidth; row++ )
        {
            for ( int col = 0; col < myDepth; col++ )
            {
                currentNode = array[ row ][ col ];
                if ( currentNode.getNeighbors().size()  == 3 ) //Meaning it is an edge.
                {
                    listOfEdges.add( currentNode );
                }
            }
        }

        //Go through list of edges and decide an entrance and exit edge.
        final int numberOfEdges = listOfEdges.size();

        //Choose a random edge.
        Random randomNodeChooser = new Random();
        int randomEdge = randomNodeChooser.nextInt(numberOfEdges);


        //Choose end node that is not the same as the starting node.
        int endEdge = randomNodeChooser.nextInt( numberOfEdges );

        while ( endEdge == randomEdge )
        {
            endEdge = randomNodeChooser.nextInt( numberOfEdges );
        }

        //Use array or arrayList or map to implement depth first search.
        //USE MAP
        Map < String, Node > nodeMap = myGraph.getMap();

        //Use stack to hold visited nodes.
        Stack < Node > stack = new Stack<>();


        //Create spanning tree.

        //Had help with depth first maze generation from these websites:
        //www.algosome.com/articles/maze-generation-depth-first.html
        //https://en.wikipedia.org/wiki/Maze_generation_algorithm

        int nameOfNextNode;
        nameOfNextNode = randomEdge;

        Node endNode = nodeMap.get( "" + endEdge );
        while ( !endNode.isThisACornerOrEdge() )
        {
            endEdge = randomNodeChooser.nextInt( numberOfEdges );

            while ( endEdge == randomEdge )
            {
                endEdge = randomNodeChooser.nextInt( numberOfEdges );
            }
            endNode = nodeMap.get( "" + endEdge );
        }

        //Start at entrance.
        currentNode = nodeMap.get( "" + nameOfNextNode );

        int difference = Math.abs( numberOfEdges - endEdge );

        while ( !currentNode.isThisACornerOrEdge() && difference != numberOfEdges  )
        {
            nameOfNextNode = randomNodeChooser.nextInt( numberOfEdges );

            while ( nameOfNextNode == endEdge )
            {
                nameOfNextNode = randomNodeChooser.nextInt( numberOfEdges );
            }
            currentNode = nodeMap.get( "" + nameOfNextNode );
            difference = Math.abs( numberOfEdges - endEdge );
        }
        createSpanningTree( currentNode,  endNode, stack );
        solution( currentNode, endNode );
    }

    /**
     * Creates spanning tree inside of maze. Starts at the given startingNode and goes through each node of the maze
     * until there are no more nodes to go through. As it goes through the maze once it reaches the given ending node
     * the program will mark the node to let the rest of the program know that this is where the ending node is.
     *
     * @param theStartingNode The Node to start creating the maze from, should be one of the edge nodes.
     * @param theEndingNode The Node to mark as the end of the maze, should be one of the edge nodes.
     * @param theStack The stack to add the nodes to.
     */
    private void createSpanningTree( Node theStartingNode, Node theEndingNode, Stack< Node > theStack ) {
        Random randomNodeChooser = new Random();
        Node currentNode = theStartingNode;
        int nameOfNextNode;
        ArrayList<Node> currentNodeNeighbors;

        //Mark cell as visited.
        currentNode.setVisited();
        //Mark first cell as starting cell.
        currentNode.setThisAsStartNode();

        while ( anyUnvisitedNeighborsInMaze() )
        {
            if ( areThereUnvisitedNeighborsForThisNode( currentNode, "visited" ) ) {

                //Choose neighbor randomly
                currentNodeNeighbors = getUnvisitedNeighborsForThisNode( currentNode, "visited" );
                nameOfNextNode = randomNodeChooser.nextInt( currentNodeNeighbors.size() );

                //Push node onto stack.
                theStack.push(currentNode);

                //Remove wall between the current Cell and the chosen cell.
                currentNode.breakWallAssociatedWithThisNode(currentNodeNeighbors.get(nameOfNextNode).getName());

                //Remove wall between chosen cell and current cell.
                currentNodeNeighbors.get( nameOfNextNode ).breakWallAssociatedWithThisNode( currentNode.getName() );

                //Make chosen cell the current cell and mark as visited.
                currentNode = currentNodeNeighbors.get(nameOfNextNode);
                currentNode.setVisited();


                //theCurrentNode is the endNode mark as end node.
                if ( currentNode.getName().equals( theEndingNode.getName() ) )
                {
                    currentNode.setThisAsEndNode();
                }

            } else if ( !theStack.isEmpty() ) {
                currentNode = theStack.pop();
            }

            //If debug active show steps
            if ( myDebug )
                displayWithOrWithoutSolution(true);
        }
    }

    /**
     * Looks at the given node's adjacent nodes to see if any of them have been visited by the createSpanningTree
     * method, or the solution method. If there are any nodes which have not been visited by one of these methods
     * depending on which method is being checked this method will return true, false if all adjacent nodes have
     * been visited.
     *
     * @param theNode The Node who's adjacent nodes will be checked.
     * @param whichMethod The String deciding which method to check against, for the createSpanningTree method use
     *                    "visited" for the solution method any other string is okay. I used "solution".
     * @return Returns true if, depending on which method, there are unvisited nodes adjacent to the given node.
     */
    private boolean areThereUnvisitedNeighborsForThisNode( Node theNode, String whichMethod )
    {
        if ( whichMethod.equals( "visited" ) )
        {
            boolean thereAreUnvisitedNeighbors = false;
            for ( int i = 0; i < theNode.getNeighbors().size(); i++ ) {
                if ( !theNode.getNeighbors().get(i).getVisited() )
                //If there is a neighbor that has not been visited.
                {
                    thereAreUnvisitedNeighbors = true;
                    break;
                } else
                    thereAreUnvisitedNeighbors = false;
            }
            return thereAreUnvisitedNeighbors;
        }
        else
        {
            boolean thereAreUnvisitedNeighbors = false;
            Node currentNode;
            for ( int i = 0; i < theNode.getNeighbors().size(); i++ ) {
                currentNode = theNode.getNeighbors().get( i );
                if ( !currentNode.visitedBySolutionFinder() && theNode.isThisNodeConnectedToThisNode( currentNode ) )
                //If there is a neighbor that has not been visited by the solution finder
                // and is connected to theNode by a broken wall
                {
                    thereAreUnvisitedNeighbors = true;
                    break;
                } else
                    thereAreUnvisitedNeighbors = false;
            }
            return thereAreUnvisitedNeighbors;
        }

    }

    /**
     * Looks through the entire maze and checks if any of the nodes have not been visited by the createSpanningTree
     * method. If there are any unvisited nodes this method will return true, if all methods have been visited this
     * method will return false.
     *
     * @return False if all nodes have been visited, true if there is at least one node that has not been visited.
     */
    private boolean anyUnvisitedNeighborsInMaze()
    {
        boolean thereAreUnvisitedNeighbors = false;
        final Node array[][] = myGraph.getGraph();
        for ( int row = 0; row < myWidth; row++ )
        {
            for ( int col = 0; col < myDepth; col++ )
            {
                if ( !array[ row ][ col ].getVisited() )
                //If this node has not been visited.
                {
                    thereAreUnvisitedNeighbors = true;
                    break;
                }
            }
            if ( thereAreUnvisitedNeighbors )
                break;
        }
        return thereAreUnvisitedNeighbors;
    }

    /**
     * Returns the neighbors for the given node. If whichMethod is equal to "visited" this method will return a list
     * of all nodes adjacent to the given node that have not been visited by the createSpanningTree method. If
     * whichMethod is equal to any other string, this method will return a list of all nodes that have not been
     * visited by the solution method.
     *
     * @param theNode The Node who's adjacent nodes will be checked.
     * @param whichMethod String deciding which method to check against whether or not it has visited this node. Use
     *                    "visited" to check against the creatingSpanningTree method and use any other String to
     *                    check against the solution method.
     * @return Return a list of nodes that, depending on the method, have not been visited.
     */
    private ArrayList< Node > getUnvisitedNeighborsForThisNode( Node theNode, String whichMethod )
    {
        ArrayList< Node > theList = new ArrayList<>();
        Node currentNode;

        if ( whichMethod.equals( "visited" ) )
        {
            for ( int i = 0; i < theNode.getNeighbors().size(); i++ )
            {
                currentNode = theNode.getNeighbors().get( i );
                if ( !currentNode.getVisited() )
                {
                    theList.add( currentNode );
                }
            }
            return theList;
        }
        else
        {
            for ( int i = 0; i < theNode.getNeighbors().size(); i++ )
            {
                currentNode = theNode.getNeighbors().get( i );
                if ( !currentNode.visitedBySolutionFinder() && theNode.isThisNodeConnectedToThisNode( currentNode ) )
                {
                    theList.add( currentNode );
                }
            }
            return theList;
        }


    }

    /**
     * Solves the maze. This method solves the maze by using a depth first search. As it goes through the maze it will
     * mark nodes that are a part of the solution path.
     *
     * @param theStartingNode The node to start solving the maze from.
     * @param theEndingNode The node that when reached during the solving of the maze will stop the method from going
     *                      any farther through the maze.
     */
    private void solution( Node theStartingNode, Node theEndingNode )
    {
        //Got idea for using depth first from this website:
        //https://en.wikipedia.org/wiki/Maze_solving_algorithm

        //Do depth first search.
        //Add nodes to stack as you go,
        //When back tracking remove nodes.
        //Stop looking once node that matches theEndingNode.
        //Mark each of these nodes as part of solution.

        Stack < Node > stack = new Stack<>();
        Node currentNode = theStartingNode;
        currentNode.setThisNodeAsVisitedBySolutionFinder();
        currentNode.setThisNodeAsPartOfSolution();

        ArrayList<Node> currentNodeNeighbors;
        int nameOfNextNode;
        Random randomNodeChooser = new Random();

        boolean endNodeFound = false;
        while ( !endNodeFound )
        {
            if ( areThereUnvisitedNeighborsForThisNode( currentNode, "solution" ) ) {

                //Choose neighbor randomly
                currentNodeNeighbors = getUnvisitedNeighborsForThisNode( currentNode, "solution" );
                nameOfNextNode = randomNodeChooser.nextInt( currentNodeNeighbors.size() );

                //Push node onto stack.
                stack.push(currentNode);

                //Make chosen cell the current cell and mark as visited.
                currentNode = currentNodeNeighbors.get(nameOfNextNode);
                currentNode.setThisNodeAsVisitedBySolutionFinder();
                currentNode.setThisNodeAsPartOfSolution();

                //theCurrentNode is the endNode mark as end node.
                if ( currentNode.getName().equals( theEndingNode.getName() ) )
                {
                    endNodeFound = true;
                }

            } else if ( !stack.isEmpty() ) {
                currentNode.setThisNodeAsNoLongerApartOfSolution();
                currentNode = stack.pop();
            }
        }
    }

    /**
     * Prints out the maze. If the given boolean is true this method will print out the maze with the nodes that were
     * visisted by the createSpanningTree method with an A. If the given boolean is false this method will print out the
     * maze with the nodes that were marked as part of the solution by the solution method with a plus.
     *
     * @param theDebugOrNot The boolean deciding whether or not to show the maze with the nodes that have been visited
     *                      by the createSpanningTree method as A's or as +'s with the node that has been visited by the
     *                      solution method. True will show the nodes as A's, false will show
     *                      the maze with +'s for the nodes that are a part of the solution path.
     */
    private void displayWithOrWithoutSolution( boolean theDebugOrNot )
    {
        Node array[][] = myGraph.getGraph();

        if ( theDebugOrNot )
        {
            for ( int row = 0; row < myWidth; row++ )
            {
                for ( int col = 0; col < myDepth; col++ )
                {
                    //Print tops of all nodes on this row.
                    System.out.print( "X " );
                    array[ row ][ col ].printTopWall();
                    System.out.print( "X " );
                }
                System.out.print( "\n" );

                for ( int col = 0; col < myDepth; col++ )
                {
                    //Then print middles of all nodes on this row.
                    array[ row ][ col ].printMiddleWalls();
                }
                System.out.print( "\n" );
                for ( int col = 0; col < myDepth; col++ )
                {
                    //Then print bottoms of all nodes on this row.
                    System.out.print( "X " );
                    array[ row ][ col ].printBottomWall();
                    System.out.print( "X " );
                }
                System.out.print( "\n" );
            }
            System.out.println();
        }
        else
        {
            for ( int row = 0; row < myWidth; row++ )
            {
                for ( int col = 0; col < myDepth; col++ )
                {
                    //Print tops of all nodes on this row.
                    System.out.print( "X " );
                    array[ row ][ col ].printTopWall();
                    System.out.print( "X " );
                }
                System.out.print( "\n" );

                for ( int col = 0; col < myDepth; col++ )
                {
                    //Then print middles of all nodes on this row.
                    array[ row ][ col ].printMiddleWallsForSolution();
                }
                System.out.print( "\n" );
                for ( int col = 0; col < myDepth; col++ )
                {
                    //Then print bottoms of all nodes on this row.
                    System.out.print( "X " );
                    array[ row ][ col ].printBottomWall();
                    System.out.print( "X " );
                }
                System.out.print( "\n" );
            }
            System.out.println();

        }

    }

    /**
     * This method calls the displayWithOrWithoutSolution method with false as the given value for the
     * displayWithOrWithoutSolution method, printing out the maze with +'s for nodes in the solution path.
     */
    void display()
    {
        displayWithOrWithoutSolution( false );

    }

    /**
     * Returns String of the maze.
     * @return String of the maze.
     */
    public String toString()
    {
        return myGraph.toString();
    }

}
