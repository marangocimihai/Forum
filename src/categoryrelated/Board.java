package categoryrelated;

import java.util.ArrayList;
import java.util.List;

public class Board 
{
	public int ID;
	public String name;
	public List<categoryrelated.Thread> threadsList = new ArrayList<categoryrelated.Thread>();
	
	public String getName()
	{
		return name;
	}
}
