package todoanator.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import todoanator.models.InMemoryStore;
import todoanator.models.Todo;
import todoanator.util.StatusCodes;

@Component("TodoService")
public class TodoService {

	InMemoryStore inMemoryStore;
	
	public TodoService() {
		inMemoryStore = new InMemoryStore();
	}
	
	public String add(Todo todo) {
		inMemoryStore.getTodoList().add(todo);
		return StatusCodes.SUCCESS;
	}
	
	public String bulkAdd(List<Todo> todos) {
		inMemoryStore.getTodoList().addAll(todos);
		return StatusCodes.SUCCESS;
	}
	
	public String update(String id, Todo todo) {
		Optional<Todo> existingTodo = findMatchingTodoById(inMemoryStore.getTodoList(), id);
		String status = StatusCodes.FAILURE;
		if(existingTodo.isPresent()) {
			if(todo.getTitle() == null || todo.getTitle().isEmpty()) {
				todo.setTitle(existingTodo.get().getTitle());
			}
			if(todo.getDescription() == null || todo.getDescription().isEmpty()) {
				todo.setDescription(existingTodo.get().getDescription());
			}
			if(todo.getDueDate() == null) {
				todo.setDueDate(existingTodo.get().getDueDate());
			}
			if (remove(todo.getId()).equals(StatusCodes.SUCCESS) 
					&& add(todo).equals(StatusCodes.SUCCESS)) {
				status = StatusCodes.SUCCESS;
			}
		}
		else {
			status=StatusCodes.NOT_FOUND;
		}
		
		return status;
	}
	
	public String remove(String id) {
		return removeFromList(inMemoryStore.getTodoList(), id);
	}

	public String removeFromList(List<Todo> list, String id) {
		final Optional<Todo> todoToDelete = findMatchingTodoById(list, id);
		
		String status = StatusCodes.FAILURE;
		if (todoToDelete.isPresent()) {
			list.remove(todoToDelete.get());
			status = StatusCodes.SUCCESS;
		}
		else {
			status= StatusCodes.NOT_FOUND;
		}
		return status;
	}
	
	public Optional<Todo> get(String id) {
		return findMatchingTodoById(inMemoryStore.getTodoList(), id);
	}
	
	public List<Todo> bulkGet(int offset, int count) {
		return bulkGetFromList(inMemoryStore.getTodoList(), offset, count);
	}
	
	private List<Todo> bulkGetFromList(List<Todo> todos, int offset, int count) {
		return  todos.stream()
				.sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()))
				.skip(offset)
				.limit(count + offset)
				.collect(Collectors.toList());
	}
	
	private Optional<Todo> findMatchingTodoById (List<Todo> list , String id) {
		final Optional<Todo> foundTodo = list.stream()
				.filter(isMatchingId(id))
				.findFirst();
		
		return foundTodo;
	}
	
	private static Predicate<Todo> isMatchingId(String id) {
	    return p -> p.getId().equals(id);
	}
}
