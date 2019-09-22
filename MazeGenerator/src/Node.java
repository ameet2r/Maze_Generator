/*
 * Maze Generator
 * @author Ameet Toor
 * @version Winter 2016 (3/12/16)
 */

import java.util.ArrayList;
import java.util.List;

class Node    //Help creating graph node from linkedGraph code given in class files.
{
    /**Name of the Node.*/
    private String myName;

    /**List that holds the adjacent nodes to this node.*/
    private List< Node > myNeighborNodes;

    /**boolean showing whether this Node has been visited by the createSpanningTree method.*/
    private boolean myVisited;

    /**WallNodes connected to this node.*/
    private WallNode left, right, top, bottom;

    /**boolean showing that this Node is the start or the end node of the maze.*/
    private boolean myIsThisStartNode, myIsThisEndNode;

    /**boolean showing this Node wall is broken for the starting or ending node.*/
    private boolean myNodeWallIsBroken;

    /**boolean showing if this node is a part of the solution path.*/
    private boolean myPartOfSolutionNode;

    /**boolean showing if this node was visited by the solution method.*/
    private boolean myVisitedBySolution;

    /**
     * Constructor setting the name of the Node to the given String.
     * @param theName The name of the Node.
     */
    Node(final String theName)
    {
        myName = theName;
        myNeighborNodes = new ArrayList<>();
        myVisited = false;
        left = null;
        right = null;
        top = null;
        bottom = null;
        myIsThisEndNode = false;
        myIsThisStartNode = false;
        myNodeWallIsBroken = false;
        myPartOfSolutionNode = false;
        myVisitedBySolution = false;
    }

    /**
     * Returns list of neighbors of the Node.
     * @return List of adjacent nodes.
     */
    List < Node > getNeighbors()
    {
        return myNeighborNodes;
    }

    /**
     * Returns name of this Node.
     * @return Name of the Node.
     */
    String getName()
    {
        return myName;
    }

    /**
     * Sets this Node as visited by the createSpanningTree method.
     */
    void setVisited()
    {
        myVisited = true;
    }

    /**
     * Returns true if this Node was visited by the createSpanningTree method. False if it was not visited.
     * @return True if visited, false if not.
     */
    boolean getVisited()
    {
        return myVisited;
    }

    /**
     * Returns list of all walls that are joining two Nodes.
     * @return List of walls joining two Nodes.
     */
    private ArrayList< WallNode > getListOfWalls()
    {
        ArrayList< WallNode > list = new ArrayList<>();

        if ( left != null )
        {
            list.add( left );
        }
        if ( right != null )
        {
            list.add( right );
        }
        if ( top != null )
        {
            list.add( top );
        }
        if ( bottom != null )
        {
            list.add( bottom );
        }
        return list;
    }

    /**
     * Sets the wall between this Node and the given node as broken.
     * @param theWallName The name of the Node to break this wall with.
     */
    void breakWallAssociatedWithThisNode(String theWallName)
    {
        ArrayList< WallNode > list = getListOfWalls();
        for ( WallNode wallNode : list )
            if ( theWallName.equals( wallNode.getNameOfWall() ) ) {
                //If names are the same break this wall.
                wallNode.breakThisWall();
                break;
            }
    }

    /**
     * Sets this Node to the given wall and on the given side.
     * @param theWallName The wall to this Node to.
     * @param theSide Which side to set this Node on.
     */
    void setThisNodeToThisWall(String theWallName, String theSide)
    {
        //Create wall between this node and the given node.
        //The correct wall should point at the given node.
        if ( left == null && theSide.equals( "left" ) )
            left = new WallNode( theWallName );
        else if ( right == null && theSide.equals( "right" ) )
            right = new WallNode( theWallName );
        else if ( top == null && theSide.equals( "top" ) )
            top  = new WallNode( theWallName );
        else if ( bottom == null && theSide.equals( "bottom" ) )
            bottom = new WallNode( theWallName );
    }

    /**
     * Return true if this Node is connected to the given Node. False if it is not connected.
     * @param theNode The node to check if this node is connected to it.
     * @return True if this node and the given node are connected. False if they are not connected.
     */
    boolean isThisNodeConnectedToThisNode(Node theNode)
    {
        boolean theseNodesAreConnected = false;
        ArrayList< WallNode > list = getListOfWalls();
        for ( WallNode wallNode : list )
        {
            if ( wallNode.getNameOfWall().equals( theNode.getName() ) && wallNode.isThisWallBroken() )
            {
                theseNodesAreConnected = true;
            }
        }
        return theseNodesAreConnected;
    }

    /**
     * Returns string with each node and its adjacent nodes.
     * @return String of node with adjacent nodes.
     */
    String toStringWithNeighbors()
    {
        //Print out node name and neighbors.

        StringBuilder nodeAsString = new StringBuilder();

        nodeAsString.append( "{ " );
        nodeAsString.append( "[" );
        nodeAsString.append( myName );
        nodeAsString.append( "] " );


        for ( Node myNeighborNode : myNeighborNodes ) {
            if (myNeighborNode != null) {
                nodeAsString.append(myNeighborNode.getName());
                nodeAsString.append(",");
            }
        }

        nodeAsString.delete( nodeAsString.length() - 1, nodeAsString.length() );

        nodeAsString.append( " }" );

        return nodeAsString.toString();
    }

    /**
     * Prints top of this node to console.
     */
    void printTopWall()
    {
        myNodeWallIsBroken = false;
        if (isThisEndNode() && isThisNodeATop() || isThisTheStartNode() && isThisNodeATop())
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( top );
        }
    }

    /**
     * Prints middle of this node to the console.
     */
    void printMiddleWalls()
    {
        if ( ( isThisEndNode() && isThisNodeALeft() && !myNodeWallIsBroken )
                || ( isThisTheStartNode() && isThisNodeALeft() && !myNodeWallIsBroken ) )
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( left );
        }

        if ( anyBrokenWalls() && !myPartOfSolutionNode )
        {
            System.out.print( "A " );
        }
        else
        {
            System.out.print( "  " );
        }

        if ( ( isThisEndNode() && isThisNodeARight() && !myNodeWallIsBroken )
                || ( isThisTheStartNode() && isThisNodeARight() && !myNodeWallIsBroken ) )
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( right );
        }
    }

    /**
     * Prints bottom of this node to console.
     */
    void printBottomWall()
    {
        if ( ( isThisEndNode() && isThisNodeABottom() && !myNodeWallIsBroken ) || ( isThisTheStartNode() && isThisNodeABottom() && !myNodeWallIsBroken ) )
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( bottom );
        }
    }

    /**
     * Prints middle of this node to console for the maze showing the solution path.
     */
    void printMiddleWallsForSolution()
    {
        if ( ( isThisEndNode() && isThisNodeALeft() && !myNodeWallIsBroken )
                || ( isThisTheStartNode() && isThisNodeALeft() && !myNodeWallIsBroken ) )
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( left );
        }

        if ( myPartOfSolutionNode )
        {
            System.out.print( "+ " );
        }
        else
        {
            System.out.print( "  " );
        }

        if ( ( isThisEndNode() && isThisNodeARight() && !myNodeWallIsBroken )
                || ( isThisTheStartNode() && isThisNodeARight() && !myNodeWallIsBroken ) )
        {
            System.out.print( "  " );
            myNodeWallIsBroken = true;
        }
        else
        {
            printOutThisNode( right );
        }
    }

    /**
     * Returns true if any of this Nodes wall are broken. False if none of this nodes walls are broken.
     * @return True if walls of this Node are broken, false if none of this nodes walls are broken.
     */
    private boolean anyBrokenWalls()
    {
        ArrayList< WallNode > list = getListOfWalls();
        boolean thereIsABrokenWall = false;

        for ( WallNode node : list )
        {
            if ( node.isThisWallBroken() )
            {
                thereIsABrokenWall = true;
                break;
            }
        }
        return thereIsABrokenWall;
    }

    /**
     * Returns true if this is the start Node, false if not the start node.
     * @return True if this is the start Node, false if not the start node.
     */
    private boolean isThisTheStartNode()
    {
        return myIsThisStartNode;
    }

    /**
     * Returns true if this is the end node, false if not the end node.
     * @return True if this is the end node, false if not the end node.
     */
    private boolean isThisEndNode()
    {
        return myIsThisEndNode;
    }

    /**
     * Sets this node to be the start node.
     */
    void setThisAsStartNode()
    {
        myIsThisStartNode = true;
    }

    /**
     * Sets this node to be the end node.
     */
    void setThisAsEndNode()
    {
        myIsThisEndNode = true;
    }

    /**
     * Checks to see if this node is on top of the maze. Returns true if it is, and false if it is not.
     * @return True if node is on top of maze, false if not on top.
     */
    private boolean isThisNodeATop()
    {
        boolean isThisATop = false;
        if ( top == null )
        {
            isThisATop = true;
        }
        return isThisATop;
    }

    /**
     * Returns true if this node is on the right side of the maze, false if it is not.
     * @return True if node is on the right side of maze, false if not on the right side.
     */
    private boolean isThisNodeARight()
    {
        boolean isThisARight = false;
        if ( right == null )
        {
            isThisARight = true;
        }
        return isThisARight;
    }

    /**
     * Returns true if this node is on the left of the maze, false if not on the left.
     * @return True if node is on the left of maze, false if not on the left of maze.
     */
    private boolean isThisNodeALeft()
    {
        boolean isThisALeft = false;
        if ( left == null )
        {
            isThisALeft = true;
        }
        return isThisALeft;
    }

    /**
     * Returns true if this node is on the bottom of the maze, false if not on the bottom.
     * @return True if this node is on the bottom, false if not on the bottom of maze.
     */
    private boolean isThisNodeABottom()
    {
        boolean isThisABottom = false;
        if ( bottom == null )
        {
            isThisABottom = true;
        }
        return isThisABottom;
    }

    /**
     * Returns true if this node is a corner or an edge of the maze, false otherwise.
     * @return True if corner or edge, false if not a corner or edge.
     */
    boolean isThisACornerOrEdge()
    {
        boolean aCornerOrEdge = false;
        //If corner
        if ( bottom == null && right == null )
        {
            aCornerOrEdge = true;
        }
        else if ( bottom == null && left == null )
        {
            aCornerOrEdge = true;
        }
        else if ( left == null && top == null )
        {
            aCornerOrEdge = true;
        }
        else if ( right == null && top == null )
        {
            aCornerOrEdge = true;
        }

        //If edge
        if ( isThisNodeATop() && !isThisNodeABottom() && !isThisNodeALeft() && !isThisNodeARight() )
        {
            aCornerOrEdge = true;
        }
        else if ( !isThisNodeATop() && isThisNodeABottom() && !isThisNodeALeft() && !isThisNodeARight() )
        {
            aCornerOrEdge = true;
        }
        else if ( !isThisNodeATop() && !isThisNodeABottom() && isThisNodeALeft() && !isThisNodeARight() )
        {
            aCornerOrEdge = true;
        }
        else if ( !isThisNodeATop() && !isThisNodeABottom() && !isThisNodeALeft() && isThisNodeARight() )
        {
            aCornerOrEdge = true;
        }
        return aCornerOrEdge;
    }

    /**
     * Prints out the given node.
     * @param theNode The node to print out.
     */
    private void printOutThisNode( WallNode theNode )
    {
        if ( theNode != null && theNode.isThisWallBroken() )
        //If wall is broken and not null.
        {
            System.out.print( "  " );
        }
        else
        //If wall is not broken or node is null.
        {
            System.out.print( "X " );
        }
    }

    /**
     * Sets this node to be a part of the solution path.
     */
    void setThisNodeAsPartOfSolution()
    {
        myPartOfSolutionNode = true;
    }

    /**
     * Removes this node from the solution path.
     */
    void setThisNodeAsNoLongerApartOfSolution()
    {
        myPartOfSolutionNode = false;
    }

    /**
     * Sets this node as visited by the solution method.
     */
    void setThisNodeAsVisitedBySolutionFinder()
    {
        myVisitedBySolution = true;
    }

    /**
     * Returns true if this node was visited by the solution method, false if not visited.
     * @return True if visited by the solution method, false if not visited.
     */
    boolean visitedBySolutionFinder()
    {
        return myVisitedBySolution;
    }
}
