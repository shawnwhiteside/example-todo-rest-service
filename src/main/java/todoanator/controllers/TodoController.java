package todoanator.controllers;

import org.springframework.web.bind.annotation.RestController;

import todoanator.models.Todo;
import todoanator.services.TodoService;
import todoanator.util.StatusCodes;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class TodoController {
	
	@Autowired
	TodoService service;
	
	static final int maxBulkOpLimit = 100;
	
	@RequestMapping("/")
	public String getHealthStatus() {
		return "OK";
	}
	
	@RequestMapping(value="/todo/{id}",method = RequestMethod.GET)
	public ResponseEntity<?> getTodo(@PathVariable("id") String id) {
		
		//TODO: input parameter validation & sanitizing
		Optional<Todo> todo = service.get(id);
		   if (!todo.isPresent()) {
	            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	        }
		   return new ResponseEntity<Todo>(todo.get(), HttpStatus.OK);
	}
	
	@RequestMapping("/todo")
	public ResponseEntity<?> getTodosDefault() {
		
		int offset = 0;
		int count = 100;
		//TODO: input parameter validation & sanitizing
		List<Todo> todos = service.bulkGet(offset, count);
		   if (todos == null || todos.isEmpty()) {
	            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	        }
		   return new ResponseEntity<List<Todo>>(todos, HttpStatus.OK);
	}
	
	@RequestMapping("/todo/{offset}/{count}")
	public ResponseEntity<?> getTodos(@PathVariable("offset") int offset, @PathVariable("count") int count) {
		
		//TODO: input parameter validation & sanitizing
		List<Todo> todos = service.bulkGet(offset, count);
		   if (todos == null || todos.isEmpty()) {
	            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	        }
		   return new ResponseEntity<List<Todo>>(todos, HttpStatus.OK);
	}
	
	//{"title":"title45", "description":"description45", "dueDate":"2018-01-01T09:50:00"}
	@RequestMapping(value="/todo",method = RequestMethod.POST)
	public ResponseEntity<?> add (@RequestBody Todo todo) {
		
		//TODO: input parameter validation & sanitizing
		Todo newTodo = new Todo(todo.getTitle(), todo.getDescription(), todo.getDueDate());
		service.add(newTodo);
		//TODO: add error handling for HTTP response
		URI location = URI.create("/todo/"+newTodo.getId());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);
		return new ResponseEntity<Todo>(newTodo, responseHeaders, HttpStatus.CREATED);
	}
	
	/*
	[
	{"title":"title55", "description":"description55", "dueDate":"2018-01-01T09:50:00"},
	{"title":"title56", "description":"description56", "dueDate":"2018-01-01T09:50:00"},
	{"title":"title57", "description":"description57", "dueDate":"2018-01-01T09:50:00"}
	]
	*/
	@RequestMapping(value="/todo/bulk",method = RequestMethod.POST)
	public ResponseEntity<?> add (@RequestBody List<Todo> todos) {
		
		//TODO: input parameter validation & sanitizing
		
		if (todos == null || todos.isEmpty() || todos.size() >= maxBulkOpLimit) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		List<Todo> newTodos = todos.stream()
				.map(t -> new Todo(t.getTitle(), t.getDescription(), t.getDueDate()))
				.collect(Collectors.toList());
		
		String status = service.bulkAdd(newTodos);
		if (!status.equals(StatusCodes.SUCCESS)) {
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Todo>>(newTodos, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/todo/{id}",method = RequestMethod.PATCH)
	public ResponseEntity<?> patch (@PathVariable("id") String id, @RequestBody Todo todo) {
		
		//TODO: input parameter validation & sanitizing
		Todo newTodo = new Todo(todo.getTitle(), todo.getDescription(), todo.getDueDate());
		String status = service.update(id, newTodo);

		if (status.equals(StatusCodes.NOT_FOUND)) {
			return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		}
		else if (status.equals(StatusCodes.FAILURE)) {
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
		
		URI location = URI.create("/todo/"+newTodo.getId());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);
		return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
	}
}
