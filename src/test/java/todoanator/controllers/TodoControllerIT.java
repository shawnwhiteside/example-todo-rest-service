package todoanator.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import todoanator.models.Todo;
import todoanator.services.TodoService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT {
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Autowired
	private TodoService service;
	
	@LocalServerPort
    private int port;
	
	HttpHeaders headers = new HttpHeaders();
	
	private List<Todo> testTodos;
	
    @Before
    public void setUp() {
    	
    	testTodos = new ArrayList<Todo>();
    	Random random = new Random();
		final int min = 1;
		final int max = 10;	
		
		for (int i = 1; i <= 100; i++)
		{
			final Todo todo = new Todo("Title"+i, "Description"+i, LocalDateTime.now().plusDays(random.nextInt(max - min + 1) + min));
			testTodos.add(todo);
		}
		
		service.bulkAdd(testTodos);
    }
	
	
    @Test
	public void getNonExistantTodo() throws Exception {
    	
    	final HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	final String missingTodoId = "Missing";
    	final String path = "/todo/" + missingTodoId;
    	
    	ResponseEntity<String> responseEntity =
                restTemplate.exchange(path, HttpMethod.GET, entity, String.class);
    	
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
    
    @Test
	public void getTodo() throws Exception {
    	
    	final HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	final String path = "/todo/" + testTodos.get(0).getId();
 
    	ResponseEntity<String> responseEntity =
                restTemplate.exchange(path, HttpMethod.GET, entity, String.class);
    	
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains(testTodos.get(0).getId()));   
	}

    @After
    public void tearDown() {
    	//TODO: Add bulk delete functionality
    	for (Todo todo: testTodos) {
    		service.remove(todo.getId());
    	}
    }
}



