package databaserelated;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import categoryrelated.*;

import userrelated.Moderator;
import userrelated.User;

public class Database 
{
	Connection connection = null;
	
	public Database()
	{
		
	}
	
	public void connectionToDB()
	{
		Object o1 = new Object();
		
		synchronized (o1) 
		{
			// load the sqlite-JDBC driver using the current class loader
			try 
			{
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:D:\\Projects\\RSA80Workspace\\Forum\\forumdb.db");
			} 
			catch (ClassNotFoundException e1) 
			{
				e1.printStackTrace();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	//check if the typed nickname exists in the database and the password for it is correct
	public boolean checkLoginFormDetails(String nickname, String password)
	{
		connectionToDB();
		
		boolean userExists = false;
		
		try
		{
			//String query1 = "CREATE TABLE Thread (`ID` INTEGER PRIMARY KEY AUTOINCREMENT, `Name` TEXT, `BoardID` INTEGER, `Sticky` BOOLEAN, `Closed` BOOLEAN)";
			//PreparedStatement ps1 = connection.prepareStatement(query1);
			
			//ps1.executeUpdate();
			
			String query = "SELECT ID FROM User WHERE nickname LIKE ? AND password LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, nickname);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				userExists = true;
			}
			else
			{
				userExists = false;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return userExists;
	}
	
	//check the correctness of the Sign Up form details
	public int checkRegisterFormDetails(User user) 
	{
		connectionToDB();
		
		try
		{
			List<String> allUsers = new ArrayList<String>();
			
			String query = "SELECT Nickname FROM User";
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next())
			{
				allUsers.add(rs.getString(1));
			}
			
			Iterator<String> it1 = allUsers.iterator();
			
			//check if the nickname is already used
			while (it1.hasNext())
			{
				if (user.nickname.equals(it1.next()))
				{
					User.wrongRegisterDetails = 1;
					
					return 1;
				}
			}
			
			//check if both of the password typed match
			if (!(user.password.equals(user.retypedPassword)))
			{
				User.wrongRegisterDetails = 2;
				
				return 2;
			}
			
			//check email format
			if (!(user.email.matches("\\b[A-Za-z0-9._%+-]+(@)[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")))
			{
				User.wrongRegisterDetails = 3;
				
				return 3;
			}
			
			//check username length
			if (user.nickname.trim().length() == 0)
			{
				User.wrongRegisterDetails = 4;
				
				return 4;
			}
			
			//check password length
			if (user.password.trim().length() == 0)
			{
				User.wrongRegisterDetails = 5;
				
				return 5;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return 0 ;
	}
	
	//register a new user
	public void getRegisterFormDetails(User user)
	{
		connectionToDB();
			
		try
		{	
			String query = "INSERT INTO User(Nickname, Password, Email, Role) VALUES(?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);
				
			ps.setString(1, user.nickname);
			ps.setString(2, user.password);
			ps.setString(3, user.email);
			ps.setString(4, "User");
				
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
	            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
	}
	
	public List<Category> getCategories()
	{
		connectionToDB();
		List<Category> listOfCategories = new ArrayList<Category>();
		
		try
		{
			String query = "SELECT id, name FROM Category";
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			//retrieve all categories from database
			while (rs.next())
			{
				Category category = new Category();
				category.ID = rs.getInt(1);
				category.name = rs.getString(2);
				
				listOfCategories.add(category);
			}		
			
			//iterator
			Iterator<Category> it1 = listOfCategories.iterator();
			
			//iterate through the categories
			while(it1.hasNext())
			{
				Category currentCategory = new Category();
				currentCategory = (Category) it1.next();
				
				String query1 = "SELECT name FROM Board WHERE CategoryID = ?";
				PreparedStatement ps = connection.prepareStatement(query1);
				ps.setInt(1, currentCategory.ID);
				
				ResultSet rs1 = ps.executeQuery();
				
				//and add the name of each board of the current category into a List
				while (rs1.next())
				{
					Board currentBoard = new Board();
					currentBoard.name = rs1.getString(1);
					
					currentCategory.boardList.add(currentBoard);
				}
			}	
		
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		return listOfCategories;
	}
	
	public List<categoryrelated.Thread> getThreads(int boardID)
	{
		List<categoryrelated.Thread> currentStickyThreadsList = new ArrayList<categoryrelated.Thread>();
		List<categoryrelated.Thread> currentNormalThreadsList = new ArrayList<categoryrelated.Thread>();
		
		connectionToDB();
		
		try
		{		
			String query = "SELECT ID, Name, Sticky, Closed FROM Thread WHERE BoardID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, boardID);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				categoryrelated.Thread currentThread = new categoryrelated.Thread();
				
				//check if current thread is sticky
				if (rs.getBoolean(3))
				{
					currentThread.ID = rs.getInt(1);
					currentThread.name = rs.getString(2);
					currentThread.isSticky = rs.getBoolean(3);
					currentThread.isClosed = rs.getBoolean(4);
					currentThread.messagesList = getMessagesByThread(rs.getInt(1));
					
					currentStickyThreadsList.add(currentThread);	
				}
				else
				{
					currentThread.ID = rs.getInt(1);
					currentThread.name = rs.getString(2);
					currentThread.isSticky = rs.getBoolean(3);
					currentThread.isClosed = rs.getBoolean(4);
					currentThread.messagesList = getMessagesByThread(rs.getInt(1));
					
					currentNormalThreadsList.add(currentThread);	
				}
			}
			
			Iterator<categoryrelated.Thread> it1 = currentStickyThreadsList.iterator();
			
			List<Message> messages = new ArrayList<Message>();
			
			//order sticky threads by the date of the last message posted in each
			while (it1.hasNext())
			{
				categoryrelated.Thread currentThread = it1.next();
				
				if (currentThread.messagesList.size() != 0)
				{
					messages.add(currentThread.messagesList.get(currentThread.messagesList.size()-1));
				}		
			}
			
			Collections.sort(messages, new Comparator<Message>() 
			{
				@Override
				public int compare(Message m1, Message m2) 
				{
					return Long.valueOf(m1.date).compareTo(Long.valueOf(m2.date));
				}
			});
			
			Collections.sort(currentStickyThreadsList, new Comparator<categoryrelated.Thread>() 
			{
				@Override
				public int compare(categoryrelated.Thread t1, categoryrelated.Thread t2) 
				{
					int i = 0;
					
					if (!t1.messagesList.isEmpty())
					{
						if (!t2.messagesList.isEmpty())
						{
							i =  Long.valueOf(t1.messagesList.get(t1.messagesList.size()-1).date).compareTo(Long.valueOf(t2.messagesList.get(t2.messagesList.size()-1).date));
						}
						else
						{
							i = 1;
						}
					}
					
					else
					{
						if (!t2.messagesList.isEmpty())
						{
							i = -1;						
						}
						else
						{
							i = 0;
						}
					}
					
							
					return i;
				}
			});
			
			Collections.sort(currentNormalThreadsList, new Comparator<categoryrelated.Thread>() 
			{
				@Override
				public int compare(categoryrelated.Thread t1, categoryrelated.Thread t2) 
				{
					int i = 0;
							
					if (!t1.messagesList.isEmpty())
					{
						if (!t2.messagesList.isEmpty())
						{
							i =  Long.valueOf(t1.messagesList.get(t1.messagesList.size()-1).date).compareTo(Long.valueOf(t2.messagesList.get(t2.messagesList.size()-1).date));
						}
						else
						{
							i = 1;
						}
					}			
					else
					{
						if (!t2.messagesList.isEmpty())
						{
							i = -1;						
						}
						else
						{
							i = 0;
						}
					}				
					return i;
				}
			});			
			
			
			Collections.reverse(currentStickyThreadsList);
			Collections.reverse(currentNormalThreadsList);
			currentStickyThreadsList.addAll(currentNormalThreadsList);
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return currentStickyThreadsList;
	}
	
	public int getUserIDByNickname(String nickname)
	{
		int nicknameID = 0;
		connectionToDB();
		
		try
		{
			String query = "SELECT ID FROM User WHERE Nickname LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, nickname);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				nicknameID = rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
	        }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return nicknameID;
	}

	public String getUserNicknameByID(int nicknameID)
	{
		String nickname = "";
		connectionToDB();
		
		try
		{
			String query = "SELECT Nickname FROM User WHERE ID LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, nicknameID);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				nickname = rs.getString(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
	        }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return nickname;
	}
	
	public int getCategoryIDByName(String categoryName)
	{
		int categoryID = 0;
		connectionToDB();
		
		try
		{
			String query1 = "SELECT ID FROM Category WHERE Name LIKE ?";
			PreparedStatement ps1 = connection.prepareStatement(query1);
			ps1.setString(1, categoryName);
			
			ResultSet rs1 = ps1.executeQuery();
			
			if (rs1.next())
			{
				categoryID = rs1.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
	        }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return categoryID;
	}

	public int getBoardIDByName(String boardName)
	{
		connectionToDB();
		int boardID = 0;
	        
		try
		{
			String query = "SELECT ID FROM Board WHERE Name LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, boardName);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				boardID = rs.getInt(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
	        }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return boardID;
	}

	public int getThreadIDByName(String threadName)
	{
		connectionToDB();
		int threadID = 0;
		
		try
		{
			String query = "SELECT ID FROM Thread WHERE Name LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, threadName);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				threadID = rs.getInt(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return threadID;
	}
	
	public int getMessageID(Message message)
	{
		int messageID = 0;
		
		connectionToDB();
		
		try
		{
			String query = "SELECT ID FROM Message WHERE Author LIKE ? AND Content LIKE ? AND Date LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, message.author);
			ps.setString(2, message.content);
			ps.setString(3, message.date);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				messageID = rs.getInt(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return messageID;
	}
	
	public List<Message> getMessagesByThread(int threadID)
	{
		connectionToDB();
		List<Message> currentMessagesList = new ArrayList<Message>();
		
		try
		{
			//String query1 = "CREATE TABLE Message ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `Author` TEXT, `Content` TEXT, `ThreadID` TEXT, `Date` DATE )";
			//PreparedStatement ps1 = connection.prepareStatement(query1);
			
			//ps1.executeUpdate();
			String query = "SELECT * FROM Message WHERE ThreadID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, threadID);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				Message M = new Message();

				M.ID = rs.getInt(1);
				M.author = rs.getString(2);
				M.content = rs.getString(3);
				M.threadID = rs.getInt(4);
				M.date = rs.getString(5);
				M.editorID = rs.getInt(6);
				M.editDate = rs.getString(7);
				
				currentMessagesList.add(M);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return currentMessagesList;
	}
	
	public Message getMostRecentMessageDetails(int threadID)
	{
		connectionToDB();
		Message mostRecentMessage = new Message();
		
		try
		{
			String query = "SELECT Author, Date FROM Message WHERE ThreadID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, threadID);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				mostRecentMessage.author = rs.getString(1);
				mostRecentMessage.date = rs.getString(2);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return mostRecentMessage;
	}
	
	public List<User> getNormalUsersAndModerators(boolean getModeratorsAlso)
	{
		List<User> users = new ArrayList<User>();
		
		connectionToDB();
		
		try
		{
			String query = "";
			
			if (!getModeratorsAlso)
			{
				query = "SELECT Nickname FROM User WHERE Role LIKE 'User'";
			}
			else
			{
				query = "SELECT Nickname FROM User WHERE Role LIKE 'User' OR Role LIKE 'Moderator'";
			}
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next())
			{
				User currentUser = new User();
				
				currentUser.nickname = rs.getString(1);
				
				users.add(currentUser);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return users;
	}
	
	public List<Moderator> getModeratorsByBoardID(int boardID)
	{
		List<Moderator> moderators = new ArrayList<Moderator>();
		
		connectionToDB();
		
		try
		{
			String query = "SELECT DISTINCT NicknameID FROM Moderator WHERE BoardID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, boardID);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				Moderator currentModerator = new Moderator();
				
				currentModerator.nickname = getUserNicknameByID(rs.getInt(1));
				
				moderators.add(currentModerator);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return moderators;
	}
	
	public List<Board> getModeratedBoardsByNicknameID(int currentLoggedInUserID)
	{
		List<Board> boards = new ArrayList<Board>();
		connectionToDB();
		
		try
		{
			String query = "SELECT DISTINCT BoardID FROM Moderator WHERE NicknameID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, currentLoggedInUserID);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				Board currentBoard = new Board();
				
				currentBoard.ID = rs.getInt(1);
				
				boards.add(currentBoard);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		
		return boards;
	}
	
	public String getUserRole(String nickname)
	{
		String role = "";
		connectionToDB();
		
		try
		{
			String query = "SELECT Role FROM User WHERE Nickname LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, nickname);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				role = rs.getString(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
		return role;
	}
	
	public boolean checkIfCategoryAlreadyExists(String categoryName)
	{
		boolean categoryAlreadyExists = false;
		
		connectionToDB();
		
		try
		{
			String query3 = "SELECT * FROM Category WHERE Name LIKE ?";
			PreparedStatement ps3 = connection.prepareStatement(query3);
			ps3.setString(1, categoryName.trim());
				
			ResultSet rs3 = ps3.executeQuery();
				
			if (rs3.next())
			{
				categoryAlreadyExists = true;
			}
			else
			{
				categoryAlreadyExists = false;
			}		
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		return categoryAlreadyExists;
	}
	
	public boolean checkIfCategoryHasBoards(int categoryID)
	{
		boolean hasBoards = false;
		
		connectionToDB();
		
		try
		{
			String query3 = "SELECT Name FROM Board WHERE CategoryID = ?";
			PreparedStatement ps3 = connection.prepareStatement(query3);
			ps3.setInt(1, categoryID);
				
			ResultSet rs3 = ps3.executeQuery();
				
			if (rs3.next())
			{
				hasBoards = true;
			}
			else
			{
				hasBoards = false;
			}		
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		return hasBoards;
	}
	
	public boolean checkIfBoardHasThreads(int boardID)
	{
		boolean hasThreads = false;
		
		connectionToDB();
		
		try
		{
			String query3 = "SELECT Name FROM Thread WHERE BoardID = ?";
			PreparedStatement ps3 = connection.prepareStatement(query3);
			ps3.setInt(1, boardID);
				
			ResultSet rs3 = ps3.executeQuery();
				
			if (rs3.next())
			{
				hasThreads = true;
			}
			else
			{
				hasThreads = false;
			}		
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		return hasThreads;
	}
	
	public void deleteCategory(int categoryID)
	{
		connectionToDB();
		
		try
		{
			String query = "DELETE FROM Category WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, categoryID);
					
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void deleteBoard(int boardID)
	{
		connectionToDB();
		
		try
		{
			String query = "DELETE FROM Board WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, boardID);
					
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	
	public void deleteMessage(Message messageToDelete)
	{
		connectionToDB();
		
		try
		{
			String query = "DELETE FROM Message WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, messageToDelete.ID);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void editMessage(Message currentEditedMessage, int editorNicknameID, String newContent, String editDate)
	{
		connectionToDB();
		
		try
		{
			String query = "UPDATE Message SET Content = ?, EditorID = ?, Editdate = ? WHERE ID LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, newContent);
			ps.setInt(2, editorNicknameID);
			ps.setString(3, editDate);
			ps.setInt(4, currentEditedMessage.ID);
		
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewCategory(String categoryName)
	{
		connectionToDB();
		
		try
		{
			String query = "INSERT INTO Category(Name) VALUES(?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, categoryName);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewBoard(String boardName, int categoryID)
	{
		connectionToDB();
		
		try
		{
			String query = "INSERT INTO Board(Name, CategoryID) VALUES(?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, boardName);
			ps.setInt(2, categoryID);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewMessage(Message newMessage)
	{
		connectionToDB();
		
		try
		{		
			String query = "INSERT INTO Message(Author, Content, ThreadID, Date) VALUES(?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, newMessage.author);
			ps.setString(2, newMessage.content);
			ps.setInt(3, newMessage.threadID);
			ps.setString(4, newMessage.date);
			
			ps.executeUpdate();
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewModerator(String nickname, int currentBoardID)
	{
		connectionToDB();
		
		try
		{	
			//first modify the role of the user in the User table
	        String query1 = "UPDATE User SET Role = 'Moderator' WHERE Nickname LIKE ?";
		    PreparedStatement ps1 = connection.prepareStatement(query1);
		    ps1.setString(1, nickname);
		    
		    ps1.executeUpdate();
		    
		    
		    //second, add his ID in the Moderator table alongside the board it has to moderate 
		    String query2 = "INSERT INTO Moderator(NicknameID, BoardID) VALUES(?, ?)";
		    PreparedStatement ps2 = connection.prepareStatement(query2);
		    ps2.setInt(1, getUserIDByNickname(nickname));
		    ps2.setInt(2, currentBoardID);
		    
		    ps2.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void removeModerator(String nickname, int currentBoardID)
	{
		connectionToDB();
		
		try
		{	
			//first modify the role of the user in the User table
	        String query1 = "UPDATE User SET Role = 'User' WHERE Nickname LIKE ?";
		    PreparedStatement ps1 = connection.prepareStatement(query1);
		    ps1.setString(1, nickname);
		    
		    ps1.executeUpdate();
		    
		    
		    //second, add his ID in the Moderator table alongside the board it has to moderate 
		    String query2 = "DELETE FROM Moderator WHERE NicknameID = ? AND BoardID = ?";
		    PreparedStatement ps2 = connection.prepareStatement(query2);
		    ps2.setInt(1, getUserIDByNickname(nickname));
		    ps2.setInt(2, currentBoardID);
		    
		    ps2.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewAdmin(String nickname)
	{
		connectionToDB();
		
		try
		{
	        String query = "UPDATE User SET Role = 'Admin' WHERE Nickname LIKE ?";
		    PreparedStatement ps = connection.prepareStatement(query);
		    ps.setString(1, nickname);
		    
		    ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void addNewThread(categoryrelated.Thread newThread)
	{
		connectionToDB();
		
		try
		{
			String query = "INSERT INTO Thread(Name, BoardID, Sticky, Closed) " +
								 "VALUES(?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, newThread.name);
			ps.setInt(2, newThread.boardID);
			ps.setBoolean(3, newThread.isSticky);
			ps.setBoolean(4, newThread.isClosed);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void moveBoardToAnotherCategory(String boardName, String newCategoryName)
	{
		int newCategoryID = getCategoryIDByName(newCategoryName);
		int currentBoardID = getBoardIDByName(boardName);
		
		connectionToDB();
		
		try
		{	
			String query = "UPDATE Board SET CategoryID = ? WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, newCategoryID);
			ps.setInt(2, currentBoardID);
			
			ps.executeUpdate();
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void setStickyThread(String selectedThreadName, boolean isSticky)
	{
		int selectedThreadID = getThreadIDByName(selectedThreadName); 
		connectionToDB();
		
		try
		{
			String query = "UPDATE Thread SET Sticky = ? WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			if (isSticky)
			{
				ps.setBoolean(1, true);
			}
			else
			{
				ps.setBoolean(1, false);
			}
			
			ps.setInt(2, selectedThreadID);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}
	}
	
	public void modifyThreadTitle(categoryrelated.Thread thread)
	{
		connectionToDB();
		
		try
		{
			String query = "UPDATE Thread SET Name = ? WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, thread.name);
			ps.setInt(2, thread.ID);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
	}
	
	public void closeThread(categoryrelated.Thread thread)
	{
		connectionToDB();
		
		try
		{
			String query = "UPDATE Thread SET Closed = 1 WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, thread.ID);
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
	}
	
	public boolean checkIfThreadIsClosed(categoryrelated.Thread thread)
	{
		boolean isClosed = true;
		int currentThreadID = getThreadIDByName(thread.name);
		
		connectionToDB();
		
		try
		{
			String query = "SELECT Closed FROM Thread WHERE ID = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, currentThreadID);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				isClosed = rs.getBoolean(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		
		return isClosed;
	}
	
	public boolean checkIfNewThreadNameIsAlreadyTaken(String newTitle)
	{
		boolean isTaken = false;
		
		connectionToDB();
		
		try
		{
			String query = "SELECT * FROM Thread WHERE Name LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, newTitle);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				isTaken = true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		
		return isTaken;
	}
	
	public boolean checkIfNewBoardNameIsAlreadyTaken(String newTitle)
	{
		boolean isTaken = false;
		
		connectionToDB();
		
		try
		{
			String query = "SELECT * FROM Board WHERE Name LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, newTitle);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				isTaken = true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(connection != null)
				{
					connection.close();
				}
            }
			catch(SQLException e) 
			{
				e.printStackTrace(); 
			}
		}	
		
		return isTaken;
	}
}
