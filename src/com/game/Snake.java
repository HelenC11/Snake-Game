/*
 * Helen Chen
 * 6/17/2018
 * final project snake game
 */
package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Snake extends JPanel implements ActionListener {

	private int sizeWidth;
	private int sizeHeight;
	private int offsetWidth;
	private int offsetHeight;
	private int scale;
	private ArrayList<Peg> snakeLocation;
	private static Point food;
	private String direction = "RIGHT";
	private String tmpDirection = "RIGHT";

	private static final Snake snake = new Snake();

	private Integer delay;
	private Boolean isPaused = false;
	private Boolean isAlive = false;
	private Timer timer;
	private Board board;
	private Peg peg = null;

	private Integer score = 0;
	private Integer highScore = 0;
	private int speed = 1;

	private Snake() {

	}

	/*
	 * get the single instance of the snake game class
	 */
	public static Snake getInstance() {
		return snake;
	}

	/*
	 * This method creates the board
	 */
	public void createBoard() {
		snakeLocation = new ArrayList<>();
		snakeLocation.add(new Peg());
		// food=new Point(-100,-100);

		// create 15x15 board
		board = new Board(15, 15);
		sizeWidth = board.getOriginalWidth();
		sizeHeight = board.getOriginalWidth();

		offsetHeight = 30;
		offsetWidth = 30;
		scale = 10;

		board.displayMessage("Press Space to Start");

		// input from keyboard
		InputMap im = board.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = board.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RightArrow");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LeftArrow");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UpArrow");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DownArrow");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Space");

		am.put("RightArrow", new GamePlay("RightArrow"));
		am.put("LeftArrow", new GamePlay("LeftArrow"));
		am.put("UpArrow", new GamePlay("UpArrow"));
		am.put("DownArrow", new GamePlay("DownArrow"));
		am.put("Space", new GamePlay("Space"));

	}

	/*
	 * the method used to start the game, it is the entry point for the game
	 */
	public void startGame() {
		// speed of snake
		delay = 100 + (5 - speed) * 15;
		timer = new Timer(delay, this);
		if (this.peg != null) {
			board.removePeg(peg.getRow(), peg.getCol());
		}
		if (snakeLocation != null && snakeLocation.size() > 0) {
			for (int j = 0; j < snakeLocation.size(); j++) {
				board.removePeg(snakeLocation.get(j).getRow(), snakeLocation.get(j).getCol());
			}
		}
		score = 0;
		direction = "RIGHT";
		snakeLocation.clear();
		// when game starts snake is in this position
		for (int i = 0; i < 6; i++) {
			snakeLocation.add(new Peg(1, 7 - i));
		}

		newFood();
		board.displayMessage("      Score: " + getScore() + "   Highscore: " + highscore());
		// buttons.blockButtons();
		isAlive = true;
		isPaused = false;
		timer.start();
	}

	// snake head coords
	public ArrayList<Peg> getSnakeLocation() {
		return snakeLocation;
	}

	// food coords
	public Point getFoodLocation() {
		return food;
	}

	public Boolean getIsAlive() {
		return isAlive;
	}

	// direction snake is going
	public void setDirection(String dir) {
		snake.direction = dir;
	}

	public String getDirection() {
		return snake.direction;
	}

	public String getTmpDirection() {
		return snake.tmpDirection;
	}

	/*
	 * This method allows space starts game if snake is dead and pauses if snake is
	 * alive
	 */
	public void spacePressed() {

		if (!isAlive) {
			board.displayMessage("game started...");
			snake.startGame();
			// isAlive=true;
		} else {
			board.displayMessage("game paused...");
			isPaused ^= true;
			// isAlive=false;
		}
	}

	// pause game
	public Boolean getPause() {
		return isPaused;
	}

	/*
	 * This method creates a new peg in the direction the snake moves, removes one
	 * from the other end
	 */
	public void move() {
		if (direction.equals("RIGHT")) {
			snakeLocation.add(0, new Peg(snakeLocation.get(0).getRow() + 0, snakeLocation.get(0).getCol() + 1));
		} else if (direction.equals("LEFT")) {
			snakeLocation.add(0, new Peg(snakeLocation.get(0).getRow() + 0, snakeLocation.get(0).getCol() - 1));
		} else if (direction.equals("UP")) {
			snakeLocation.add(0, new Peg(snakeLocation.get(0).getRow() - 1, snakeLocation.get(0).getCol() + 0));

		} else if (direction.equals("DOWN")) {
			snakeLocation.add(0, new Peg(snakeLocation.get(0).getRow() + 1, snakeLocation.get(0).getCol() + 0));
		}
	}

	/*
	 * This method makes the snake move in the right direction if it is alive
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (!isPaused && isAlive) {
			tmpDirection = direction;
			snake.move();

			snake.checkPosition();

			// refresh();
			// board.repaint();

		} else if (!isAlive) {
			timer.stop();
		}

	}

	/*
	 * This method generates a random food peg on the board
	 */
	public void newFood() {

		Random random = new Random();
		peg = new Peg();
		peg.setCol(random.nextInt(board.getColumns()));
		peg.setRow(random.nextInt(board.getRows()));

		while (Arrays.asList(getSnakeLocation()).contains(peg)) {
			peg.setCol(random.nextInt(board.getColumns()));
			peg.setRow(random.nextInt(board.getRows()));
		}
	}

	public void increaseScore() {
		score = score + speed;
	}

	// returns current score
	public int getScore() {
		return score;
	}

	// returns current speed
	public int getSpeed() {
		return speed;
	}

	public void refresh() {

		board.repaint();
	}

	/*
	 * This method check the snake position and process to trigger next action
	 * The game ends if the snake hits it's own tail or the wall
	 *  high score and score will be updated into a file and displayed on the death screen
	 */
	public void checkPosition() {

		for (int j = 1; j < snakeLocation.size() - 1; j++) {
			if (snakeLocation.get(0).getRow() == (snakeLocation.get(j).getRow())
					&& snakeLocation.get(0).getCol() == (snakeLocation.get(j).getCol())) {
				isAlive = false;
				saveScore(getScore());
				board.displayMessage("Game over, you hit your tail!" + "      Score: " + getScore() + "    Highscore: "
						+ highscore());

			}
		}

		
		
		if (snakeLocation.get(0).getRow() == board.getRows() || snakeLocation.get(0).getCol() == board.getColumns()
				|| snakeLocation.get(0).getRow() < 0 || snakeLocation.get(0).getCol() < 0) {

			snakeLocation.remove(0);
			isAlive = false;
			saveScore(getScore());
			board.displayMessage("Game over! Press space to start again..." + "      Score: " + getScore()
					+ "    highscore: " + highscore());

		}
		/*
		 * This method generates a new food peg if the snake hits original one. It also
		 * removes the old one and allows the snake to grow bigger
		 */
		else {
			if (snakeLocation.get(0).getRow() == peg.getRow() && snakeLocation.get(0).getCol() == peg.getCol()) {
				newFood();
				increaseScore();
				board.displayMessage("      Score: " + getScore());
			} else {
				board.removePeg(snakeLocation.get(snakeLocation.size() - 1).getRow(),
						snakeLocation.get(snakeLocation.size() - 1).getCol());
				snakeLocation.remove(snakeLocation.size() - 1);
			}
		}
	}

	// generates new food peg
	public Peg getPeg() {
		return peg;
	}

	/*
	 * This method saves the scores in a file and the keep track of the high score
	 */
	private void saveScore(int score) {

		String pathway = "output.txt";

		if (score > highScore) {
			highScore = score;
		}

		try {
			FileWriter writer = new FileWriter(pathway, true);
			writer.write("score:" + score + " ;highScore:" + highScore + "\r\n");
			writer.close();
		} catch (IOException ex) {

		}

	}

	/*
	 * This method returns the highscore
	 */
	private Integer highscore() {
		return highScore;
	}
}
