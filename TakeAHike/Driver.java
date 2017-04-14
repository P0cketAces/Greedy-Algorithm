package TakeAHike;

import java.awt.*;
import java.io.IOException;

public class Driver
{
    
    public static void main(String[] args) throws IOException{
        
        //Test Step 1 - construct mountain map data
        MapDataDrawer map = new MapDataDrawer("NevadaToCalifornia.txt");
        
        //Test Step 2 - min, max, minRow in col
        int min = map.findMinValue();
        System.out.println("Min value in map: "+min);
        
        int max = map.findMaxValue();
        System.out.println("Max value in map: "+max);
        
        int minRow = map.indexOfMinInCol(0);
        System.out.println("Row with lowest val in col 0: "+minRow);
        
        //construct DrawingPanel, and get its Graphics context
        DrawingPanel panel = new DrawingPanel(map.getColumns(), map.getRows());
        Graphics g = panel.getGraphics();
        
        //Test Step 3 - draw the map
        map.drawMap(g);
        
        //Test Step 4 - draw a greedy path
        g.setColor(Color.RED); //can set the color of the 'brush' before drawing, then method doesn't need to worry about it
        int totalChange = map.drawLowestElevPath(g, minRow, 0); //use minRow from Step 2 as starting point
        System.out.println("Lowest-Elevation-Change Path starting at row "+minRow+" gives total change of: "+totalChange);
        
        //Test Step 5 - draw the best path
        g.setColor(Color.RED);
        int bestRow = map.indexOfLowestElevPath(g, 0);
        int bestRowOptimized = map.indexOfLowestElevPath(g, 2);
        
        boolean isRightPath = (bestRowOptimized == map.indexOfLowestElevPath(g, 0));
        
        map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.RED);
        totalChange = map.drawLowestElevPath(g, minRow, 0);
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        totalChange = map.drawLowestElevPath(g, bestRow, 0);
        g.setColor(Color.BLUE);
        
        String leftOrRight;
        
        if(isRightPath){
            leftOrRight = "left";
            totalChange = map.drawLowestElevPath(g, bestRowOptimized, 0);
        }
        else{
            leftOrRight = "right";
            totalChange = map.drawLowestElevPath(g, bestRowOptimized, 1);
        }
        
        System.out.println("The Lowest-Elevation-Change Path starts at row: "+bestRow+" (on the "+leftOrRight+") and gives a total change of: "+totalChange);
        
        
    }


}
