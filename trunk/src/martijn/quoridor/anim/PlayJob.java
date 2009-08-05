/*
 * Created on Aug 17, 2006 
 */
package martijn.quoridor.anim;

/**
 * @author Martijn van Steenbergen
 */
public class PlayJob {

	public static final int LOOP = -1;

	/** The animation to play. */
	private Animation animation;

	/** The number of times the animation should be played. */
	private int times;

	/**
	 * The number of milliseconds that should pass before the animation is
	 * played for the first time.
	 */
	private long delayBefore;

	/**
	 * The number of milliseconds that should pass between the various
	 * animations.
	 */
	private long delayBetween;

	/** Whether the individual animations should be played forwards or backwards. */
	private boolean forward;

	/** Creates a new play job. */
	public PlayJob(Animation animation, int times, long delayBefore,
			long delayBetween, boolean forward) {
		if (animation == null) {
			throw new NullPointerException("Animation is null.");
		}
		this.animation = animation;
		this.times = times;
		this.delayBefore = delayBefore;
		this.delayBetween = delayBetween;
		this.forward = forward;
	}

	/** Creates a play job plays the animation once. */
	public static PlayJob playOnce(Animation animation, boolean forward) {
		return new PlayJob(animation, 1, 0, 0, forward);
	}

	/** Creates a play job that continuously loops the animation, with no delays. */
	public static PlayJob loop(Animation animation, boolean forward) {
		return new PlayJob(animation, LOOP, 0, 0, forward);
	}

	public Animation getAnimation() {
		return animation;
	}

	public long getDelayBefore() {
		return delayBefore;
	}

	public long getDelayBetween() {
		return delayBetween;
	}

	public boolean isForward() {
		return forward;
	}

	public int getTimes() {
		return times;
	}

	public boolean loops() {
		return times == LOOP;
	}

	/**
	 * Executes the play job in the current thread. Interrupting the thread will
	 * cause an {@code InterruptedException} to be thrown as soon as it exits
	 * any {@link Animation} method it might be in. If the animation is looping,
	 * interrupting the thread is the only way to stop it.
	 */
	public void execute() throws InterruptedException {
		for (int i = 0; i < times || loops(); i++) {
			Thread.sleep(i == 0 ? delayBefore : delayBetween);
			playOnce(forward);
		}
		animation.animationStopped();
	}

	/**
	 * Plays the animation once.
	 * 
	 * @param forward
	 *            whether the animation should be played forward.
	 */
	private void playOnce(boolean forward) throws InterruptedException {
		int n = animation.getFrameCount();
		for (int i = 0; i < n; i++) {
			int frame = forward ? i : n - i - 1;
			animation.showFrame(frame);
			if (Thread.interrupted()) {
				throw new InterruptedException("Play job interrupted.");
			}
			Thread.sleep(animation.getFrameDisplayTime(frame));
		}
	}

}
