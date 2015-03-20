package fr.skyforce77.arch3ds.game2048;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import fr.skyforce77.arch3ds.api.Plugin;
import fr.skyforce77.arch3ds.api.graphics.ArchGraphics;
import fr.skyforce77.arch3ds.api.graphics.ArchScreen;
import fr.skyforce77.arch3ds.api.input.ArchAxis;
import fr.skyforce77.arch3ds.api.input.ArchInput;

public class Game2048 extends Plugin{
	
	private static int[][] table = new int[4][4];
	private Random rand = new Random();
	private Color background = new Color(227, 159, 91);

	@Override
	public void onEnable() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				table[i][j] = 0;
			}
		}
		table[rand.nextInt(4)][rand.nextInt(4)] = 1;
	}

	@Override
	public void onInput(ArchInput input, byte status) {}

	@Override
	public void onAxis(ArchAxis axis, double location) {}

	@Override
	public void drawScreen(ArchGraphics graphics) {
		Graphics2D g2d = graphics.getGraphics();
		g2d.setColor(background);
		g2d.fillRect(0, 0, graphics.getWidth(), graphics.getHeight());
		
		if(graphics.getScreen().equals(ArchScreen.BOTTOM_SCREEN)) {
			int width = graphics.getWidth()/5;
			int height = graphics.getHeight()/5;
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					g2d.setColor(new Color(255-table[i][j]*50, 255, 255-table[i][j]*50));
					g2d.fillRect(i*width+width/2, j*height+height/2, width, height);
				}
			}
		}
	}

}