package todoanator.controllers;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import todoanator.models.Todo;
import todoanator.services.TodoService;
import todoanator.util.StatusCodes;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

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
    
    @Test
	public void addTodo() throws Exception {

    	final Todo newTodo = new Todo("titleNew", "DescriptionNew", LocalDateTime.of(2018, 01, 01, 9, 50,00));
    	final String path = "/todo/";
    	final String jsonInput = "{\"title\":\"titleNew\", \"description\":\"DescriptionNew\", \"dueDate\":\"2018-01-01T09:50:00\"}";
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(path)
				.accept(MediaType.APPLICATION_JSON).content(jsonInput)
				.contentType(MediaType.APPLICATION_JSON);
    	
    	given(this.todoService.add(newTodo))
    		.willReturn(StatusCodes.SUCCESS);
    	this.mockMvc.perform(requestBuilder)
    	     .andExpect(status().isCreated())
    	     .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(newTodo.getTitle()));
    }

    @Test
 	public void patchTodo() throws Exception {

     	final Todo newTodo = new Todo("titleNew", "DescriptionNew", LocalDateTime.of(2018, 01, 01, 9, 50,00));
     	final String path = "/todo/"+newTodo.getId();
     	final String jsonInput = "{\"title\":\"titleNew\", \"description\":\"DescriptionNew\", \"dueDate\":\"2018-01-01T09:50:00\"}";
     	
     	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.patch(path)
 				.accept(MediaType.APPLICATION_JSON).content(jsonInput)
 				.contentType(MediaType.APPLICATION_JSON);
     	
     	given(this.todoService.update(any(), any()))
     		.willReturn(StatusCodes.SUCCESS);
     	this.mockMvc.perform(requestBuilder)
     	     .andExpect(status().isNoContent())
     	     .andExpect(header().string("Location", containsString("todo")));
    }
    
    @Test
 	public void patchTodoMissingTodo() throws Exception {

     	final Todo newTodo = new Todo("titleNew", "DescriptionNew", LocalDateTime.of(2018, 01, 01, 9, 50,00));
     	final String path = "/todo/"+newTodo.getId();
     	final String jsonInput = "{\"title\":\"titleNew\", \"description\":\"DescriptionNew\", \"dueDate\":\"2018-01-01T09:50:00\"}";
     	
     	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.patch(path)
 				.accept(MediaType.APPLICATION_JSON).content(jsonInput)
 				.contentType(MediaType.APPLICATION_JSON);
     	
     	given(this.todoService.update(any(), any()))
     		.willReturn(StatusCodes.NOT_FOUND);
     	this.mockMvc.perform(requestBuilder)
     	     .andExpect(status().isNotFound());
    }
    
    @Test
 	public void patchTodoFailure() throws Exception {

     	final Todo newTodo = new Todo("titleNew", "DescriptionNew", LocalDateTime.of(2018, 01, 01, 9, 50,00));
     	final String path = "/todo/"+newTodo.getId();
     	final String jsonInput = "{\"title\":\"titleNew\", \"description\":\"DescriptionNew\", \"dueDate\":\"2018-01-01T09:50:00\"}";
     	
     	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.patch(path)
 				.accept(MediaType.APPLICATION_JSON).content(jsonInput)
 				.contentType(MediaType.APPLICATION_JSON);
     	
     	given(this.todoService.update(any(), any()))
     		.willReturn(StatusCodes.FAILURE);
     	this.mockMvc.perform(requestBuilder)
     	     .andExpect(status().isBadRequest());
    }
}



