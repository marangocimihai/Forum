package categoryrelated;

import java.util.ArrayList;
import java.util.List;

public class Thread 
{
	public int ID;
	public String name;
	public boolean isSticky;
	public boolean isClosed;
	public List<Message> messagesList = new ArrayList<Message>();
	public int boardID;
	public int numberOfMessages = 1;
	
	public String getName()
	{
		return name;
	}
}
