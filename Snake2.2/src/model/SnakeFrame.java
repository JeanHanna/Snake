package model;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * ��ɵĹ��ܣ�������Ϸ����dead����
 * */

public class SnakeFrame extends Frame{
	//����Ŀ��Ⱥͳ���
	public static final int BLOCK_WIDTH = 15 ;
	public static final int BLOCK_HEIGHT = 15 ;
	//����ķ��������������
	public static final int ROW = 40;
	public static final int COL = 40;
	
	//�÷�
	private int score = 0;
	
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	//��ͼ���̶߳���
	private MyPaintThread paintThread = new MyPaintThread();

	private Image offScreenImage = null;
	
	private Snake snake = new Snake(this);
	
	private Egg egg = new Egg();
	
	public static void main(String[] args) {
		new SnakeFrame().launch();
	}
	
	public void launch(){
		
		this.setTitle("Snake");
		this.setSize(ROW*BLOCK_HEIGHT, COL*BLOCK_WIDTH);
		this.setLocation(30, 40);
		this.setBackground(Color.WHITE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);
		this.setVisible(true);
		
		//Ϊ�������Ӽ����¼�
		this.addKeyListener(new KeyMonitor());
		
		new Thread(paintThread).start();
	}
	
	
	private boolean b_gameOver = false;
	
	public void gameOver(){
		b_gameOver = true;
	}
	
	/*
	 * ��дupdate����
	 * */
	@Override
	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage = this.createImage(ROW*BLOCK_HEIGHT, COL*BLOCK_WIDTH);
		}
		Graphics offg = offScreenImage.getGraphics();
		//�Ƚ����ݻ������⻭����
		paint(offg);
		//Ȼ�����⻭���ϵ�����һ���ڻ�����
		g.drawImage(offScreenImage, 0, 0, null);
		
		if(b_gameOver){
			g.drawString("��Ϸ����������", ROW/2*BLOCK_HEIGHT, COL/2*BLOCK_WIDTH);
			paintThread.dead();
		}
		
		snake.draw(g);
		boolean b_Success=snake.eatEgg(egg);
		//��һ����5��
		if(b_Success){
			score+=5;
		}
		egg.draw(g);
		displaySomeInfor(g);
		
		
	}
	/*
	 * �������ܣ��ڽ�������ʾһЩ��ʾ��Ϣ
	 * */
	public void displaySomeInfor(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.drawString("ʹ��˵��:�ո��---��ͣ������B---��ͣ��ʼ", 5*BLOCK_HEIGHT, 3*BLOCK_WIDTH);
		g.drawString("�÷�:"+score, 5*BLOCK_HEIGHT, 5*BLOCK_WIDTH);		
		g.setColor(c);
		
	}

	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		/*
		 * �����滭����ROW*COL�ķ��񹹳�,����forѭ�����ɽ��
		 * */
		for(int i = 0;i<ROW;i++){
			g.drawLine(0, i*BLOCK_HEIGHT, COL*BLOCK_WIDTH,i*BLOCK_HEIGHT );
		}
		for(int i=0;i<COL;i++){
			g.drawLine(i*BLOCK_WIDTH, 0 , i*BLOCK_WIDTH ,ROW*BLOCK_HEIGHT);
		}
		
		g.setColor(c);
	}
	
	
	/*
	 * �ػ��߳���
	 * */
	private class MyPaintThread implements Runnable{
		private boolean  running = true;
		private boolean  pause = false;
		@Override
		public void run() {
			while(running){
				//���pause Ϊtrue ������ͣ
				if(pause){
					continue;
				}
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		/*
		 * �������ܣ���ͣ
		 * */
		public void pause(){
			pause = true;
		}
		/*
		 * ����ͣ�����¿�ʼ
		 * */
		public void reStart(){
			pause = false;
		}
		/*
		 * ��Ϸ����������
		 * */
		public void dead(){
			running = false;
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_SPACE){
				paintThread.pause();
			}
			else if(key == KeyEvent.VK_B){//��ʼ
				paintThread.reStart();
			}
			snake.keyPressed(e);
		}
		
	}
	
}