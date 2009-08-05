/*
 * Created on Aug 9, 2006 
 */
package martijn.quoridor.ui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;

/**
 * Setup describes a complete game setup: board and controllers.
 * 
 * @author Martijn van Steenbergen
 */
public class Setup implements Iterable<Controller> {

	private BoardCanvas canvas;

	private Controller[] controllers;

	private List<SetupListener> listeners;

	public Setup(BoardCanvas canvas, Controller[] controllers) {
		Board board = canvas.getBoard();
		if (board.getPlayers().length != controllers.length) {
			throw new IllegalArgumentException("Player number mismatch.");
		}

		this.canvas = canvas;
		this.controllers = controllers;
		listeners = new LinkedList<SetupListener>();

		// Activate controllers.
		for (int i = 0; i < controllers.length; i++) {
			controllers[i].startControlling(i);
		}
	}

	public BoardCanvas getCanvas() {
		return canvas;
	}

	public Board getBoard() {
		return canvas.getBoard();
	}

	public Iterator<Controller> iterator() {
		return Arrays.asList(controllers).iterator();
	}

	public void addSetupListener(SetupListener l) {
		listeners.add(l);
	}

	public void removeSetupListener(SetupListener l) {
		listeners.remove(l);
	}

	protected void fireSetupChanged(int player) {
		for (SetupListener l : listeners) {
			l.setupChanged(player);
		}
	}

	public Controller getController(int player) {
		return controllers[player];
	}

	public void setController(int player, Controller controller) {
		if (controllers[player] != controller) {
			controllers[player].stopControlling(player);
			controllers[player] = controller;
			controller.startControlling(player);
			fireSetupChanged(player);
		}
	}

}
