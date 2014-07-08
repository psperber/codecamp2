package de.uks.challenger.model;

/**
 * A set of a unit
 * 
 * @author philipp
 * 
 */
public class Workset {
	private long id;

	/**
	 * Count of the repeats
	 */
	int count;

	/**
	 * The amount of repeats to do
	 */
	int todo;

	public int getTodo() {
		return todo;
	}

	public void setTodo(int todo) {
		this.todo = todo;
	}

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
