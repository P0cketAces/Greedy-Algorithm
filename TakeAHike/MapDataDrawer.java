/*
Take a Hike Program Using Greedy Algorithm

Joey Nelson

This program takes in data from NevadaToCalifornia.txt

The driver class is named Driver.java and such is the
entry point to this program. This overall program,
however, is meant to find an optimal path of least
elevation change from one side of a map to another.
*/

package TakeAHike;

import java.util.*;
import java.io.*;
import java.awt.*;
import static java.lang.Math.abs;

public class MapDataDrawer
{

  private final int[][] grid;

  /*
  This is the constructor. It takes in a file as a parameter
  and it uses this file to set up the private grid variable.
  */
  public MapDataDrawer(String filename) throws FileNotFoundException{
      // initialize grid 
      //read the data from the file into the grid
    File file = new File(filename);
    Scanner fileScanner = new Scanner(file);
    
    int rows = fileScanner.nextInt();
    int columns = fileScanner.nextInt();

    grid = new int[rows][columns];
    int rowIndex = 0;
    int colIndex = 0;
    while(fileScanner.hasNextInt()){
        grid[rowIndex][colIndex] = fileScanner.nextInt();
        colIndex++;
        if(colIndex == columns){
            colIndex = 0;
            rowIndex++;
        }
    }
    
}
  
  /**
   * @return the min value in the entire grid
   * 
   * Iterates through the entire grid keeping
   * track of the lowest value as it goes and
   * returns the value
   */
  public int findMinValue(){
      
      int rowIndex, colIndex;
      int min = 999999;
      int rows = grid.length;
      int columns = grid[0].length;
      for(rowIndex = 0; rowIndex < rows; rowIndex++){
          for(colIndex = 0; colIndex < columns; colIndex++){
              int coordinate = grid[rowIndex][colIndex];
              if(coordinate < min){
                  min = coordinate;
              }
          }
      }
      
      return min;
  }
  /**
   * @return the max value in the entire grid
   * Iterates through the entire grid keeping
   * track of the largest value as it goes and
   * returns the value
   */
  public int findMaxValue(){
      int rowIndex, colIndex;
      int max = 0;
      int rows = grid.length;
      int columns = grid[0].length;
      for(rowIndex = 0; rowIndex < rows; rowIndex++){
          for(colIndex = 0; colIndex < columns; colIndex++){
              int coordinate = grid[rowIndex][colIndex];
              if(coordinate > max){
                  max = coordinate;
              }
          }
      }
      
      return max;
  }
  
  /**
   * @param col the column of the grid to check
   * @return the index of the row with the lowest value in the given col for the grid
   * 
   * Given a random column, this method iterates through
   * every row using the column parameter as a constant index.
   * The minimum value for the row is saved as it is iterated
   * through.
   */
  public  int indexOfMinInCol(int col){
      int rowIndex;
      int minRow = -1;
      int min = 999999;
      int rows = grid.length;
      for(rowIndex = 0; rowIndex < rows; rowIndex++){
          int coordinate = grid[rowIndex][col];
          if(coordinate < min){
              min = coordinate;
              minRow = rowIndex;
          }
      }
      
      return minRow;
  }
  
  /**
   * Draws the grid using the given Graphics object.
   * Colors should be gray-scale values 0-255, scaled based on min/max values in grid
     * @param g
     * 
     * Utilizes both findMinValue and findMaxValue to set the extremes
     * on the scale of elevation. This method then uses those extremes to 
     * construct the ratio between elevation and the 0 - 255 color scale 
     * to shift all other values accordingly, while printing out the map
     * in the process.
   */
  public void drawMap(Graphics g){
      
      int min = this.findMinValue();
      int max = this.findMaxValue();
      int scaledMax = max - min;
      double scale = 255 / (double)scaledMax;
      
      int rows = this.grid.length;
      int columns = this.grid[0].length;
      
      int rowIndex, colIndex;
      for(rowIndex = 0; rowIndex < rows; rowIndex++){
          for(colIndex = 0; colIndex < columns; colIndex++){
              int coordinate = this.grid[rowIndex][colIndex];
              double dblC = (coordinate - min) * scale;
              int c = (int)dblC;
              g.setColor(new Color(c, c, c));
              g.fillRect(colIndex,rowIndex,1,1);
          }
      }
  }

   /**
   * Find a path from West-to-East starting at given row.
   * Choose a forward step out of 3 possible forward locations, using greedy method described in assignment.
   * @return the total change in elevation traveled from West-to-East
   * 
   * This method actually does little work. It is a wrapper
   * for 2 other methods, drawLowestElevPathLeft and ' 'Right. This method
   * is useful in determining if the path should be taken from the right
   * or left hand side. The right to left path proved to be my optimized
   * solution, and so depending on the parameter 'whichPath', the path
   * to be displayed is chosen and elevation returned.
   */
  public int drawLowestElevPath(Graphics g, int row, int whichPath){
      int elevation;
      int elevationRight;
      int elevationLeft;
      switch (whichPath) {
          case 0:
              elevationRight = drawLowestElevPathRight(g, row);
              return elevationRight;
          case 1:
              elevationLeft = drawLowestElevPathLeft(g, row);
              return elevationLeft;
          default:
              elevationRight = drawLowestElevPathRight(g, row);
              elevationLeft = drawLowestElevPathLeft(g, row);
              break;
      }
      
      if(elevationRight < elevationLeft){
          elevation = elevationRight;
      }
      
      else{
          elevation = elevationLeft;
      }
        
      return elevation;
  }
  
  /*
  This function performs the greedy algorithm setup going RIGHT. It
  takes in the data for all potential paths and displays
  them once they return from the greedy algorithm. The actual 
  greedy algorithm path chooser is being called in this function and
  it's called 'findLowestElevationRow'. This method returns the
  final elevation change and visually displays the path on the way.
  */
  public int drawLowestElevPathRight(Graphics g, int row){
      int rows = this.grid.length;
      int columns = this.grid[0].length;
      int elevationChange = 0;
      int position = this.grid[row][0];
      int colIndex = 0;
      int rowIndex = row;
      int rowNumber;
      int topRight = 0;int right;int bottomRight = 0;
      int elevationTopRight = 0;int elevationRight;int elevationBottomRight = 0;
      
      g.fillRect(colIndex, row, 1, 1);
      
      while(colIndex < columns - 1){
          
          right = grid[rowIndex][colIndex + 1];
          elevationRight = abs(position - right);
          
          if(rowIndex -1 < 0){
              bottomRight = grid[rowIndex + 1][colIndex + 1];
              elevationBottomRight = abs(position - bottomRight);
              
              rowNumber = findLowestElevationRow(999999, elevationRight, elevationBottomRight);
          }
          else if(rowIndex + 1 >= rows){
              topRight = grid[rowIndex - 1][colIndex + 1];
              elevationTopRight = abs(position - topRight);
              
              rowNumber = findLowestElevationRow(elevationTopRight, elevationRight, 999999);
          }
          else{
              bottomRight = grid[rowIndex + 1][colIndex + 1];
              elevationBottomRight = abs(position - bottomRight);
              
              topRight = grid[rowIndex - 1][colIndex + 1];
              elevationTopRight = abs(position - topRight);
              
              rowNumber = findLowestElevationRow(elevationTopRight, elevationRight, elevationBottomRight);
          }
          
          switch (rowNumber) {
              case 1:
                  elevationChange += elevationTopRight;
                  position = topRight;
                  rowIndex = rowIndex - 1;
                  g.fillRect(colIndex + 1, rowIndex, 1, 1);
                  break;
              case 2:
                  elevationChange += elevationRight;
                  position = right;
                  g.fillRect(colIndex + 1, rowIndex, 1, 1);
                  break;
              default:
                  elevationChange += elevationBottomRight;
                  position = bottomRight;
                  rowIndex = rowIndex + 1;
                  g.fillRect(colIndex + 1, rowIndex, 1, 1);
                  break;
          }
          
          colIndex++;
      }
      
      return elevationChange;
  }
  
  /*
  This function performs the greedy algorithm setup going LEFT. It
  takes in the data for all potential paths and displays
  them once they return from the greedy algorithm. The actual 
  greedy algorithm path chooser is being called in this function and
  it's called 'findLowestElevationRow'. This method returns the
  final elevation change and visually displays the path on the way.
  
  This was my optimized algorithm. I modified it from the right
  path by starting at the index of max columns and moving down
  in number of column indexes.
  */
  public int drawLowestElevPathLeft(Graphics g, int row){
      int rows = this.grid.length;
      int columns = this.grid[0].length;
      int elevationChange = 0;
      int position = this.grid[row][columns - 1];
      int colIndex = columns - 1;
      int rowIndex = row;
      int rowNumber;
      int topLeft = 0;int left;int bottomLeft = 0;
      int elevationTopLeft = 0;int elevationLeft;int elevationBottomLeft = 0;
      
      g.fillRect(colIndex, row, 1, 1);
      
      while(colIndex > 0){
          
          left = grid[rowIndex][colIndex - 1];
          elevationLeft = abs(position - left);
          
          if(rowIndex - 1 < 0){
              bottomLeft = grid[rowIndex + 1][colIndex - 1];
              elevationBottomLeft = abs(position - bottomLeft);
              
              rowNumber = findLowestElevationRow(999999, elevationLeft, elevationBottomLeft);
          }
          else if(rowIndex + 1 >= rows){
              topLeft = grid[rowIndex - 1][colIndex - 1];
              elevationTopLeft = abs(position - topLeft);
              
              rowNumber = findLowestElevationRow(elevationTopLeft, elevationLeft, 999999);
          }
          else{
              bottomLeft = grid[rowIndex + 1][colIndex - 1];
              elevationBottomLeft = abs(position - bottomLeft);
              
              topLeft = grid[rowIndex - 1][colIndex - 1];
              elevationTopLeft = abs(position - topLeft);
              
              rowNumber = findLowestElevationRow(elevationTopLeft, elevationLeft, elevationBottomLeft);
          }
          
          switch (rowNumber) {
              case 1:
                  elevationChange += elevationTopLeft;
                  position = topLeft;
                  rowIndex = rowIndex - 1;
                  g.fillRect(colIndex - 1, rowIndex, 1, 1);
                  break;
              case 2:
                  elevationChange += elevationLeft;
                  position = left;
                  g.fillRect(colIndex - 1, rowIndex, 1, 1);
                  break;
              default:
                  elevationChange += elevationBottomLeft;
                  position = bottomLeft;
                  rowIndex = rowIndex + 1;
                  g.fillRect(colIndex - 1, rowIndex, 1, 1);
                  break;
          }
          
          colIndex--;
      }
      
      return elevationChange;
  }
  
  /*
  This is the greedy algorithm. I seeded the random aspect with random values
  to incorporate in three way ties (threeRand) and two way ties (twoRand). Otherwise,
  the algorithm is a bunch of nested if then statements and switch statements.
  Passed are the 3 parameters corresponding to the elevation changes and returned
  is a simple number corresponding to the rows: 1 for top, 2 for middle, 3 for bottom.
  */
  public int findLowestElevationRow(int elevationTop, int elevationMiddle, int elevationBottom){
      Random rand = new Random(1);
      int twoRand = rand.nextInt(2);
      int threeRand = rand.nextInt(3);
      
      if(elevationTop == elevationMiddle){
          if(elevationTop == elevationBottom){
              switch(threeRand){
                  case 0: 
                      return 1;
                  case 1:
                      return 2;
                  case 2:
                      return 3;
              }
          }
          else if(elevationTop < elevationBottom){
              return (twoRand == 1)? 1 : 2;
          }
          else{
              return 3;
          }
      }
      else if(elevationTop < elevationMiddle){
          if(elevationTop == elevationBottom){
              return (twoRand == 1)? 1 : 3;
          }
          else if(elevationTop < elevationBottom){
              return 1;
          }
          else{
              return 3;
          }
      }
      else if(elevationTop > elevationMiddle){
          if(elevationMiddle == elevationBottom){
              return (twoRand == 1)? 2 : 3;
          }
          else if(elevationMiddle < elevationBottom){
              return 2;
          }
          else{
              return 3;
          }
      }
      
      System.out.println("Error");
      return -1;
  }
  
  /**
   * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
   * 
   * Iterates through all rows, placing them all in 'drawLowestElevPath', to see
   * which one has the lowest amount of elevation change. The index of the row
   * is returned.
   */
  public int indexOfLowestElevPath(Graphics g, int whichPath){
    int rows = grid.length;
    
    int lowestRow = -1;
    int minChange = 999999;
    int currentChange;
    int rowIndex;
    for(rowIndex = 0; rowIndex < rows; rowIndex++){
        currentChange = drawLowestElevPath(g, rowIndex, whichPath);
        if(currentChange < minChange){
            minChange = currentChange;
            lowestRow = rowIndex;
        }
    }
    
    return lowestRow;
  }
  
  /*
  returns rows from grid if needed.
  */
  public int getRows(){
      return grid.length;
  }
  
  /*
  returns columns from grid if needed.
  */
  public int getColumns(){
      return grid[0].length;
  }
  
  
}