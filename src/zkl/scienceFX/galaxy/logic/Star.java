package zkl.scienceFX.galaxy.logic;


import zkl.tools.math.InstantPoint3D;

public class Star {
	public InstantPoint3D position;

	/**
	 * 半径
	 */
	public double r;

	/** 距离银河中心的距离 */
	public double distanceFromCore;
	/** 距离银盘的距离（可正可负） */
	public double h;
	/** 转动的角速度 */
	public double w;

	public Object extra;
}
