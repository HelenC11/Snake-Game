/*
 * Helen Chen
 * 6/17/2018
 * final project snake game
 */
package com.game;

public class Peg {
	int col;
	int row;
	
	//pegs and coords of pegs
	public Peg () {

	}
	public Peg (int row, int col) {
		this.col = col;
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}

}
