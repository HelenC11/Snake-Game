/*
 * Helen Chen
 * 6/17/2018
 * final project snake game
 */
package com.game;

public class Coordinate
{
  private int row;
  private int col;
  
  public Coordinate(int theRow, int theCol)
  {
    row = theRow;
    col = theCol;
  }
  
  public int getRow()
  {
    return row;
  }
    
  public int getCol()
  {
    return col;
  }
} 