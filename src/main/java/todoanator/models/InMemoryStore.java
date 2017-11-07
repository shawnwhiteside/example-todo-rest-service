package todoanator.models;

import java.util.ArrayList;

public class InMemoryStore {
	
	ArrayList<Todo> todoList;

	public ArrayList<Todo> getTodoList() {
		return todoList;
	}

	public InMemoryStore() {
		todoList = new ArrayList<Todo>();
	}
}
