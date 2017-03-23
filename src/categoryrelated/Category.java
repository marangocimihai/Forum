package categoryrelated;

import java.util.ArrayList;
import java.util.List;

public class Category 
{
	public int ID;
	public String name;
	public List<Board> boardList = new ArrayList<Board>();
	
	public String getName()
	{
		return name;
	}
}
