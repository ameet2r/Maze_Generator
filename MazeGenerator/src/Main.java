
/*
 * Maze Generator
 * @author Ameet Toor
 * @version Winter 2016 (3/12/16)
 */

/**
 * This class creates two mazes, one that will show the steps of creating the maze, and one with only the final solution
 * visible, as well as testing the graph of the maze to make sure it created a proper graph.
 */
public class Main
{
    /**
     * Creates two mazes.
     * @param args Command-line arguments.
     */
    public static void main(String[] args)
    {
        Maze testMaze = new Maze( 5, 5, false);
        testMaze.display();

        Maze testMazeTwo = new Maze( 10, 10, false );
        testMazeTwo.display();


//        testMethod( testMaze );
//        testMethod( testMazeTwo );
    }

    /**
     * Prints the String representation of the given maze.
     * @param theMaze The maze to print out.
     */
    private static void testMethod(Maze theMaze)
    {
        //Testing graphs:
        //Shows nodes and neighbor nodes that it is connected to.
        System.out.println( theMaze.toString() );
    }

}
