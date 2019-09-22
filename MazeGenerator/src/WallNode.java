/*
 * Maze Generator
 * @author Ameet Toor
 * @version Winter 2016 (3/12/16)
 */

/**
 * Class that holds information about a wall between nodes. The wall will hold the name of the node it is next to,
 * which for each Node will have the name of the adjacent node as the name of the wall between them. And this
 * wallNode holds information about whether this wallNode has been broken.
 */
class WallNode
{
    /**Name of wall.*/
    private String myWallName; //Wall name will be same as node that it is against.

    /**boolean showing whether this wall is broken.*/
    private boolean myIsThisWallBroken;

    /**
     * Constructor that sets the wall to the given String theName and sets the walls as not broken.
     * @param theName The name of the wall.
     */
    WallNode(String theName)
    {
        myWallName = theName;
        myIsThisWallBroken = false;
    }

    /**
     * Returns true if this wall is broken, and false if the wall is not broken.
     * @return True if the wall is broken, false if the wall is not broken.
     */
    boolean isThisWallBroken()
    {
        return myIsThisWallBroken;
    }

    /**Breaks this wall.*/
    void breakThisWall()
    {
        myIsThisWallBroken = true;
    }

    /**
     * Returns name of the wall.
     * @return Name of wall.
     */
    String getNameOfWall()
    {
        return myWallName;
    }
}
