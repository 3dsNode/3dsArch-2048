package fr.skyforce77.arch3ds.game2048;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import fr.skyforce77.arch3ds.api.ArchGame;
import fr.skyforce77.arch3ds.api.GameManager;
import fr.skyforce77.arch3ds.api.graphics.ArchGraphics;
import fr.skyforce77.arch3ds.api.graphics.ArchScreen;
import fr.skyforce77.arch3ds.api.input.ArchInput;
import fr.skyforce77.arch3ds.api.listener.GraphicsListener;
import fr.skyforce77.arch3ds.api.listener.InputListener;

public class Game2048 extends ArchGame implements InputListener,GraphicsListener{

	private static int[][] table = new int[4][4];
	private Random rand = new Random();
	private Color background = new Color(187, 173, 160);
	private Color[] tile = new Color[]{
			new Color(205, 193, 181),
			new Color(236, 226, 216),
			new Color(237, 222, 199),
			new Color(238, 175, 122),
			new Color(245, 149, 99),
			new Color(246, 123, 92),
			new Color(233, 89, 55),
			new Color(241, 218, 106),
			new Color(237, 204, 99),
			new Color(236, 200, 80),
			new Color(235, 195, 61),
			new Color(234, 192, 44),
			new Color(58, 60, 49)};

	@Override
	public void onInit() {
		this.addInputListener(this);
		this.addGraphicsListener(this);
	}
	
	@Override
	public void onEnable() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				table[i][j] = 0;
			}
		}
		createCase();
	}

	@Override
	public void onInput(ArchInput input, byte status) {
		if(status == 0)
			return;
		
		if(input == ArchInput.BUTTON_UP || input == ArchInput.BUTTON_X
				 || input == ArchInput.BUTTON_DOWN || input == ArchInput.BUTTON_B
				 || input == ArchInput.BUTTON_LEFT || input == ArchInput.BUTTON_Y
				 ||  input == ArchInput.BUTTON_RIGHT || input == ArchInput.BUTTON_A) {
			move(input);
			createCase();

			ArchGraphics.push(ArchScreen.BOTTOM_SCREEN);
		}
	}

	@Override
	public void onScreenUpdated(ArchGraphics graphics) {
		Graphics2D g2d = graphics.getGraphics();
		g2d.setColor(background);
		g2d.fillRect(0, 0, graphics.getWidth(), graphics.getHeight());

		if(graphics.getScreen().equals(ArchScreen.BOTTOM_SCREEN)) {
			int width = graphics.getWidth()/5;
			int height = graphics.getHeight()/5;
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					g2d.setColor(tile[table[i][j]]);
					g2d.fillRoundRect(i*width+(i+1)*width/5, j*height+(j+1)*height/5, width, height, 20, 20);
				}
			}
		} else if(graphics.getScreen().equals(ArchScreen.TOP_SCREEN)) {
			Image im = new ImageIcon(Game2048.class.getResource("/resources/2048.png")).getImage();
			g2d.drawImage(im, 0, 0, graphics.getWidth(), graphics.getHeight(), null);
		}
	}

	public void createCase() {
		boolean place = false;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(table[i][j] == 0) {
					place = true;
				}
			}
		}

		if(!place)
			return;

		boolean made = false;
		while(!made) {
			int i = rand.nextInt(4);
			int j = rand.nextInt(4);
			if(table[i][j] == 0) {
				table[i][j] = 1;
				made = true;
			}
		}
	}

	private void move(ArchInput input) {
		if(input == ArchInput.BUTTON_UP || input == ArchInput.BUTTON_X) {
			moveUp();
		} else if(input == ArchInput.BUTTON_DOWN || input == ArchInput.BUTTON_B) {
			reverse(true);
			moveUp();
			reverse(true);
		} else if(input == ArchInput.BUTTON_LEFT || input == ArchInput.BUTTON_Y) {
			moveLeft();
		} else if(input == ArchInput.BUTTON_RIGHT || input == ArchInput.BUTTON_A) {
			reverse(false);
			moveLeft();
			reverse(false);
		}
		if(full()) {
			GameManager.callGameMethod("displayPopup", "Game Over");
			table = new int[4][4];
		}
	}

	private void moveUp() {
		for(int i = 0; i < 4; i++) {
			for(int j = 3; j > 0; j--) {
				boolean up = false;
				int u = 0;
				while(!up) {
					u++;
					int k = j-u;
					int l = j-(u-1);
					if(k < 0) {
						up = true;
					} else {
						if(table[i][k] == 0) {
							table[i][k] = table[i][l];
							table[i][l] = 0;
						} else if(table[i][k] == table[i][l]) {
							table[i][k]++;
							table[i][l] = 0;
						} else {
							up = true;
						}
					}
				}
			}
		}
	}

	private void moveLeft() {
		for(int i = 3; i > 0; i--) {
			for(int j = 0; j < 4; j++) {
				boolean left = false;
				int u = 0;
				while(!left) {
					u++;
					int k = i-u;
					int l = i-(u-1);
					if(k < 0) {
						left = true;
					} else {
						if(table[k][j] == 0) {
							table[k][j] = table[l][j];
							table[l][j] = 0;
						} else if(table[k][j] == table[l][j]) {
							table[k][j]++;
							table[l][j] = 0;
						} else {
							left = true;
						}
					}
				}
			}
		}
	}

	private void reverse(boolean vertical) {
		int[][] reverse = new int[4][4];
		if(vertical) {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					reverse[i][j] = table[i][3-j];
				}
			}
		} else {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					reverse[i][j] = table[3-i][j];
				}
			}
		}
		table = reverse;
	}

	private boolean full() {
		boolean full = true;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(table[i][j] == 0) {
					full = false;
				}
			}
		}
		return full;
	}

}