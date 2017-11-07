package todoanator.models;

import java.time.LocalDateTime;
import java.util.UUID;


public class Todo {
	
	String id;
	String title;
	String description;
	
	//@DateTimeFormat(iso = ISO.DATE_TIME) //2017-11-15 09:10:11:777
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
	LocalDateTime dueDate; //would talk to product owner about the need to support TimeZones
	
    public Todo() {
        super();
    }
 
	
	public Todo(String title, String description, LocalDateTime dueDate) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
	}
	
	public Todo(String title) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.description = "";
		this.dueDate = LocalDateTime.now().plusDays(1);
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", title=" + title + ", description=" + description + ", dueDate=" + dueDate + "]";
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	
	
	
}
