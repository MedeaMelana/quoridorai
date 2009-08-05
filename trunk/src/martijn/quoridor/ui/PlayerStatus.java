/*
 * Created on Aug 8, 2006 
 */
package martijn.quoridor.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Player;

/**
 * @author Martijn van Steenbergen
 */
public class PlayerStatus {

	// Model.

	private GameCard game;

	private int playerIndex;

	// Components.

	private PlayerIcon icon;

	private JComboBox controller;

	private JLabel walls;

	/**
	 * @param board
	 * @param setup
	 * @param player
	 * @param gbc
	 *            the constraints with the gridy value set.
	 */
	public PlayerStatus(GameCard game, int player) {
		this.game = game;
		this.playerIndex = player;
	}

	public void createGUI(Container container, GridBagConstraints gbc) {
		icon = new PlayerIcon(getBoard(), playerIndex);
		ControllerModel model = new ControllerModel();
		getSetup().addSetupListener(model);
		controller = new JComboBox(model);
		walls = new JLabel();

		gbc.gridx = 0;
		container.add(icon, gbc);
		gbc.gridx++;
		container.add(controller, gbc);
		gbc.gridx++;
		container.add(walls, gbc);

		update();
		walls.setPreferredSize(walls.getPreferredSize());
	}

	private Board getBoard() {
		return game.getBoard();
	}

	public Setup getSetup() {
		return game.getSetup();
	}

	void update() {
		icon.update();

		// Find the active player.
		Player activePlayer = getBoard().getTurn();
		if (getBoard().isGameOver()) {
			activePlayer = getBoard().getWinner();
		}
		boolean active = activePlayer == getPlayer();

		// Set icon solidness.
		icon.setSolid(active);

		icon.stopFlipping();
		if (getPlayer().isWinner()) {
			icon.startFlippingContinuously();
		} else if (active && !getSetup().getController(playerIndex).isHuman()) {
			icon.startFlippingSlowly();
		}

		walls.setText(getWallText());

	}

	private String getWallText() {
		StringBuffer buf = new StringBuffer();
		if (getBoard().isGameOver()) {
			if (getPlayer().isWinner()) {
				buf.append("Winner!");
			}
		} else {
			buf.append("Walls: ");
			if (getPlayer().getWallCount() == 0) {
				buf.append("none");
			} else {
				for (int i = 0; i < getPlayer().getWallCount(); i++) {
					buf.append('|');
					if (i % 5 == 4) {
						buf.append(' ');
					}
				}
			}
		}
		return buf.toString();
	}

	private Player getPlayer() {
		return getBoard().getPlayers()[playerIndex];
	}

	private class ControllerModel extends AbstractListModel implements
			ComboBoxModel, SetupListener {

		public Object getSelectedItem() {
			return getSetup().getController(playerIndex);
		}

		public void setSelectedItem(Object controller) {
			getSetup().setController(playerIndex, (Controller) controller);
		}

		public Object getElementAt(int index) {
			return game.getControllers()[index];
		}

		public int getSize() {
			return game.getControllers().length;
		}

		public void setupChanged(int player) {
			fireContentsChanged(this, 0, getSize());
		}

	}

}
