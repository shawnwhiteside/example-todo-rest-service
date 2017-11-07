package todoanator.controllers;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import todoanator.models.Todo;
import todoanator.services.TodoService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoService todoService;
	
    @Test
	public void getTodo() throws Exception {

    	final String expectedTitle = "Title";
    	final Todo expectedTodo = new Todo(expectedTitle, "Description", LocalDateTime.of(2018, 01, 01, 9, 50,00));
    	final Optional<Todo> expectedOptional = Optional.of(expectedTodo);
    	final String path = "/todo/" + expectedTodo.getId();
    	
    	given(this.todoService.get(expectedTodo.getId()))
    		.willReturn(expectedOptional);
    	this.mockMvc.perform(get(path).accept(MediaType.APPLICATION_JSON))
    	     .andExpect(status().isOk())
    	     .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedTitle)); 
	}
    
    @Test
	public void getNonExistantTodo() throws Exception {

    	final Todo missingTodo = new Todo("", "", LocalDateTime.of(2018, 01, 01, 9, 50,00));
    	final Optional<Todo> expectedOptional = Optional.empty();
    	final String path = "/todo/" + missingTodo.getId();
    	
    	given(this.todoService.get(missingTodo.getId()))
    		.willReturn(expectedOptional);
    	this.mockMvc.perform(get(path).accept(MediaType.APPLICATION_JSON))
    	     .andExpect(status().isNotFound());
	}

}



