package pt.technic.apps.minesfinder;		//리팩토링6, 리팩토링10

import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 *
 * @author Gabriel Massadas
 */
public class MultiMode extends javax.swing.JFrame {

	private ButtonMinefield[][] buttons;		//1p 버튼
	private ButtonMinefield[][] buttons2;		//2p 버튼
	private Minefield minefield1;				//1p 게임판
	private Minefield minefield2;				//2p 게임판

	JPanel mainPanel1 = new JPanel();			//"1"은 1p를 위함
	JPanel statePanel1 = new JPanel();			//"2"는 2p를 위함
	JPanel gridPanel1 = new JPanel();
	JPanel gridPanel2 = new JPanel();
	JPanel middlePanel = new JPanel();
	JPanel mainPanel2 = new JPanel();
	JPanel statePanel2 = new JPanel();
	JLabel time = new JLabel();
	JLabel timerLabel = new JLabel();
	JLabel clicknum = new JLabel();
	JLabel click = new JLabel();
	JLabel minesleft1 = new JLabel();
	JLabel minesleft2 = new JLabel();
	JLabel mines = new JLabel();

	GameSound sound = new GameSound();

	/**
	 * ` Creates new form GameWindow
	 */
	public MultiMode() {

		initComponents();
	}


	public MultiMode(Minefield minefield1, Minefield minefield2) {

		initComponents();

		this.minefield1 = minefield1;
		this.minefield2 = minefield2;

		int left1;
		int left2;
		left1 = minefield1.getNumMines();
		left2 = minefield2.getNumMines();
		minesleft1.setText(Integer.toString(left1));
		minesleft2.setText(Integer.toString(left2));

		buttons = new ButtonMinefield[minefield1.getWidth()][minefield1.getHeight()];
		buttons2 = new ButtonMinefield[minefield2.getWidth()][minefield2.getHeight()];

		gridPanel1.setLayout(new GridLayout(minefield1.getWidth(), 									// 여기에 +1하면 칸하나 늘어남
				minefield1.getHeight()));
		gridPanel2.setLayout(new GridLayout(minefield2.getWidth(), minefield2.getHeight()));

		statePanel1.setPreferredSize(new Dimension(700, 100));
		statePanel2.setPreferredSize(new Dimension(700, 100));

		middlePanel.setPreferredSize(new Dimension(100, 700));

		MouseListener mouseListener = new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent me) {
			}

			@Override
			public void mouseReleased(MouseEvent me) {
			}

			@Override
			public void mouseEntered(MouseEvent me) {
			}

			@Override
			public void mouseExited(MouseEvent me) {
			}
		};


		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				ButtonMinefield botao = (ButtonMinefield) e.getSource();

				int x = botao.getCol();
				int y = botao.getLine();

				if (e.getKeyCode() == KeyEvent.VK_S && y > 0) { // 1p
					buttons[x][y - 1].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_E && x > 0) {
					buttons[x - 1][y].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_F && y < minefield1.getHeight() - 1) {
					buttons[x][y + 1].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_D && x < minefield1.getWidth() - 1) {
					buttons[x + 1][y].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_R) {
					if (minefield1.getGridState(x, y) == Minefield.COVERED) {		//리팩토링12
						minesleft1.setText(String.valueOf(Integer.valueOf(minesleft1.getText()) - 1));
						minefield1.setMineMarked(x, y);
					} else if (minefield1.getGridState(x, y) == Minefield.MARKED) {
						minesleft1.setText(String.valueOf(Integer.valueOf(minesleft1.getText()) + 1));
						minefield1.setMineQuestion(x, y);
					} else if (minefield1.getGridState(x, y) == Minefield.QUESTION) {
						minefield1.setMineCovered(x, y);
					}
					updateButtonsStates1();
					sound.GameSound("flag_mine.wav");
					SwingUtilities.updateComponentTreeUI(gridPanel1);
				} else if (e.getKeyCode() == KeyEvent.VK_W) {
					for(int i=0; i<minefield1.getWidth(); i++) {
						for(int j=0; j<minefield1.getHeight();j++) {
							buttons[i][j].setFocusable(false);
						}
					}
					minefield1.revealGrid(x, y);
					for(int i=0; i<minefield2.getWidth(); i++) {
						for(int j=0; j<minefield2.getHeight();j++) {
							buttons2[i][j].setFocusable(true);
						}
					}
					sound.GameSound("click.wav");
					SwingUtilities.updateComponentTreeUI(gridPanel1);
					updateButtonsStates1();
					if (minefield1.isGameFinished()) {
						if (minefield1.isPlayerDefeated()) {
							minefield1.revealMines();
							updateButtonsStates1();
							sound.GameSound("bomb.wav");
							sound.GameSound("over.wav");
							JOptionPane.showMessageDialog(null, "2Player Win!", // 게임 실패
									"RESULT", JOptionPane.INFORMATION_MESSAGE);
						} else {
							sound.GameSound("win.wav");
							JOptionPane.showMessageDialog(null, "1Player Win! ", "RESULT",
									JOptionPane.INFORMATION_MESSAGE);
						}
						setVisible(false);
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_J && y > 0) { // 2p
					buttons2[x][y - 1].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_I && x > 0) {
					buttons2[x - 1][y].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_L && y < minefield2.getHeight() - 1) {
					buttons2[x][y + 1].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_K && x < minefield2.getWidth() - 1) {
					buttons2[x + 1][y].requestFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_O) {
					sound.GameSound("flag_mine.wav");
					if (minefield2.getGridState(x, y) == Minefield.COVERED) {			//리팩토링12
						minesleft2.setText(String.valueOf(Integer.valueOf(minesleft2.getText()) - 1));
						minefield2.setMineMarked(x, y);
					} else if (minefield2.getGridState(x, y) == Minefield.MARKED) {
						minesleft2.setText(String.valueOf(Integer.valueOf(minesleft2.getText()) + 1));
						minefield2.setMineQuestion(x, y);
					} else if (minefield2.getGridState(x, y) == Minefield.QUESTION) {
						minefield2.setMineCovered(x, y);
					}
					updateButtonsStates2();
					SwingUtilities.updateComponentTreeUI(gridPanel2);

				} else if (e.getKeyCode() == KeyEvent.VK_U) {
					sound.GameSound("click.wav");
					for(int i=0; i<minefield2.getWidth(); i++) {
						for(int j=0; j<minefield2.getHeight();j++) {
							buttons2[i][j].setFocusable(false);
						}
					}
					minefield2.revealGrid(x, y);
					for(int i=0; i<minefield1.getWidth(); i++) {
						for(int j=0; j<minefield1.getHeight();j++) {
							buttons[i][j].setFocusable(true);
						}
					}
					SwingUtilities.updateComponentTreeUI(gridPanel1);
					updateButtonsStates2();
					if (minefield2.isGameFinished()) {
						if (minefield2.isPlayerDefeated()) {
							minefield2.revealMines();
							updateButtonsStates2();
							sound.GameSound("bomb.wav");
							sound.GameSound("over.wav");
							JOptionPane.showMessageDialog(null, "1Player Win!", // 게임 실패
									"RESULT", JOptionPane.INFORMATION_MESSAGE);
						} else {
								sound.GameSound("win.wav");

							JOptionPane.showMessageDialog(null, "2Player Win! ", "RESULT",
									JOptionPane.INFORMATION_MESSAGE);
						}
						setVisible(false);
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent ke) {
			}

			@Override
			public void keyReleased(KeyEvent ke) {
			}
		};

		// Create buttons for the player
		for (int x = 0; x < minefield1.getWidth(); x++) {
			for (int y = 0; y < minefield1.getHeight(); y++) {
				buttons[x][y] = new ButtonMinefield(x, y);
				buttons[x][y].setPreferredSize(new Dimension(55, 55));
				buttons[x][y].addMouseListener(mouseListener);
				buttons[x][y].addKeyListener(keyListener);

				gridPanel1.add(buttons[x][y]);
			}
		}
		for (int x = 0; x < minefield2.getWidth(); x++) {
			for (int y = 0; y < minefield2.getHeight(); y++) {
				buttons2[x][y] = new ButtonMinefield(x, y);
				buttons2[x][y].setPreferredSize(new Dimension(55, 55));
				buttons2[x][y].addMouseListener(mouseListener);
				buttons2[x][y].addKeyListener(keyListener);

				gridPanel2.add(buttons2[x][y]);
			}
		}
	}

	private void updateButtonsStates1() {
		for (int x = 0; x < minefield1.getWidth(); x++) {
			for (int y = 0; y < minefield1.getHeight(); y++) {
				buttons[x][y].setEstado(minefield1.getGridState(x, y));
			}
		}
	}

	private void updateButtonsStates2() {
		for (int x = 0; x < minefield2.getWidth(); x++) {
			for (int y = 0; y < minefield2.getHeight(); y++) {
				buttons2[x][y].setEstado(minefield2.getGridState(x, y));
			}
		}
	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Game");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		javax.swing.GroupLayout layout1 = new javax.swing.GroupLayout(gridPanel1);
		layout1.setHorizontalGroup(layout1.createParallelGroup(Alignment.LEADING).addGap(0, 550, Short.MAX_VALUE));
		layout1.setVerticalGroup(layout1.createParallelGroup(Alignment.LEADING).addGap(0, 550, Short.MAX_VALUE));
		gridPanel1.setLayout(layout1);

		javax.swing.GroupLayout layout2 = new javax.swing.GroupLayout(gridPanel2);
		layout2.setHorizontalGroup(layout2.createParallelGroup(Alignment.LEADING).addGap(0, 550, Short.MAX_VALUE));
		layout2.setVerticalGroup(layout2.createParallelGroup(Alignment.LEADING).addGap(0, 550, Short.MAX_VALUE));
		gridPanel2.setLayout(layout2);

		mines.setText("Left : ");

		statePanel1.add(mines);
		statePanel1.add(minesleft1);
		statePanel2.add(new JLabel("Left : "));
		statePanel2.add(minesleft2);

		middlePanel.setPreferredSize(new Dimension(50, 550));
		middlePanel.setBackground(Color.GRAY);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setPreferredSize(new Dimension(1500,700));

		mainPanel1.setLayout(new BorderLayout());
		mainPanel2.setLayout(new BorderLayout());

		mainPanel1.add(statePanel1, "North");
		mainPanel1.add(gridPanel1, "Center");

		mainPanel2.add(statePanel2, "North");
		mainPanel2.add(gridPanel2, "Center");

		getContentPane().add(mainPanel1,"West");
		getContentPane().add(middlePanel,"Center");
		getContentPane().add(mainPanel2,"East");

		pack();

	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {		//리팩토링5
			java.util.logging.Logger.getLogger(MultiMode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MultiMode().setVisible(true);

			}
		});
	}

}
