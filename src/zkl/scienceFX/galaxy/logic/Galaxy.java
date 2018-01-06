package zkl.scienceFX.galaxy.logic;


import java.util.ArrayList;
import java.util.Iterator;

import zkl.tools.math.MT;

public class Galaxy {
	private ArrayList<Star> stars = new ArrayList<>();
	
	synchronized public Iterator<Star> getStarsIterator() {
		return stars.iterator();
	}
	synchronized public void addStar(Star star) {
		stars.add(star);
	}
	synchronized public void removeStar(int index) {
		stars.remove(index);
	}

	synchronized public void process(double time){
		for (Star star : stars) {
			processStar(star, time);
		}
	}
	private void processStar(Star star, double time) {
		star.position.set(MT.INSTANCE.getRotatedPoint(star.position, star.w * time));
	}
}
