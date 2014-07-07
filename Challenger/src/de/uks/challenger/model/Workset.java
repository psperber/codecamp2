package de.uks.challenger.model;

/**
 * A set of a unit
 * @author philipp
 *
 */
public class Workset {
	private long id;

	/**
	 * Count of the repeats
	 */
	int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
