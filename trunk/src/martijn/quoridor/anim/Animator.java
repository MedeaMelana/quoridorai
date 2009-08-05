/*
 * Created on Aug 14, 2006 
 */
package martijn.quoridor.anim;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import martijn.quoridor.Core;

/**
 * An animator manages one thread that executes play jobs.
 * 
 * @author Martijn van Steenbergen
 */
public class Animator implements Runnable {

	/** The thread that executes the play jobs. */
	private Thread thread;

	/** The queue containing the play jobs to be run. */
	private BlockingQueue<PlayJob> queue;

	/** The currently running play job. */
	private PlayJob current;

	/** Creates a new animator. */
	public Animator() {
		queue = new LinkedBlockingQueue<PlayJob>();
		thread = new Thread(this, toString());
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	/**
	 * Returns the currently running play job, or {@code null} if the animator
	 * is idle.
	 */
	public synchronized PlayJob getCurrent() {
		return current;
	}

	/** Cancels the currently running play job, if there is one. */
	public synchronized void cancelCurrent() {
		if (current != null) {
			thread.interrupt();
		}
	}

	/** Adds the play job to the queue to be run soon. */
	public void play(PlayJob job) {
		queue.add(job);
	}

	public void run() {
		while (true) {
			// Retrieve next play job.
			PlayJob job = null;
			try {
				job = queue.take();
			} catch (InterruptedException e) {
				Core.LOGGER.log(Level.WARNING, this + " has died.", e);
				return;
			}
			synchronized (this) {
				current = job;
			}

			// Execute it.
			try {
				current.execute();
			} catch (InterruptedException e) {
				current.getAnimation().animationStopped();
				// The animation was cancelled.
			}

			// Mark as finished.
			synchronized (this) {
				current = null;
			}
		}
	}

}
