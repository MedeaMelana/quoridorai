/*
 * Created on Aug 14, 2006 
 */
package martijn.quoridor.anim;

/**
 * An animation. Upon creation, the animation should be in reset state.
 * 
 * @author Martijn van Steenbergen
 */
public interface Animation {

	/** Returns the number of frames in the animation. */
	public int getFrameCount();

	/**
	 * Returns the number of milliseconds the speficied frame should be visible
	 * before the next is displayed.
	 */
	public long getFrameDisplayTime(int frame);

	/** Causes the animation to show the next frame. */
	public void showFrame(int frame);

	/**
	 * Notification that the animation has stopped, either because there were no
	 * more frames to show, or because the animation was cancelled.
	 */
	public void animationStopped();

}
