package businessPlan;

import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BPServer extends UnicastRemoteObject implements ServerInterface
{
	private ArrayList<Person> users;
	private ArrayList<BusinessPlan> plans;
	
	public BPServer() throws RemoteException
	{
		super();
		//the lists are instantiated by reading from the disk
		users = decodeUsers();
		plans = decodePlans();
		
	}
	
	public void addUser(Person p) throws RemoteException
	{
		for(Person e: users)
		{
			//Don't want to add duplicates
			if(e.getUsername().equals(p.getUsername()) && e.getPassword().equals(p.getPassword()))
				return;
		}
		
		users.add(p);
	}
	
	public void addBusinessPlan(BusinessPlan bp) throws RemoteException, NotAllowedException
	{
		for(int i = 0; i < plans.size(); i++)
		{
			BusinessPlan current = plans.get(i);
			
			//This plan already exists on the server (i.e. it is being replaced)
			if(current.getDepartment().equals(bp.getDepartment()) && current.year == bp.year)
			{
				//We don't want a plan to be replaced if it is not editable
				if(!current.isEditable)
					throw new NotAllowedException("This plan may not be edited");
				plans.set(i, bp);
				return;
			}
		}
		//This plan does not already exist on the server. Add it.
		plans.add(bp);
	}

	//Compares a username/password pair to the users list. Returns the cooresponding person if one exists
	public Person validateLogin(String username, String password) throws NothingFoundException, RemoteException
	{
		for(Person p: users)
		{
			if(p.getUsername().equals(username) && p.getPassword().equals(password))
				return p;
		}
		
		throw new NothingFoundException("There is no user with these credentials");
	}
	
	//Finds a plan, given a person and a year. The person parameter is used for its department attribute
	public BusinessPlan findPlan(Person p, int year) throws RemoteException, NothingFoundException
	{
		String department = p.getDepartment();
		
		for(BusinessPlan bp: plans)
		{
			if(bp.getDepartment().equals(department) && bp.getYear() == year)
				return bp;
		}
		
		throw new NothingFoundException("There is no buisness plan with these details");
	}
	
	//Saves the users and plans lists to the disk
	public void encode() throws RemoteException
	{
		
		for(int i = 0; i < plans.size(); i++)
		{
			BusinessPlan current = plans.get(i);
			String fileName = "plan" + i + ".xml";
			current.encodeToXML(fileName);
		}
		
		for(int i = 0; i < users.size(); i++)
		{
			Person current = users.get(i);
			String fileName = "user" + i + ".xml";
			current.encodeToXML(fileName);
		}
	}
	
	//Reads the BusinessPlan xml objects from the disk
	public ArrayList<BusinessPlan> decodePlans() throws RemoteException
	{
		ArrayList<BusinessPlan> plans = new ArrayList<BusinessPlan>();
		int i = 0;
		while(true)
		{
			String fileName = "plan" + i + ".xml";
			try
			{
				BusinessPlan bp = BusinessPlan.decodeFromXML(fileName);
				plans.add(bp);
			}
			catch(Exception e)
			{
				return plans;
			}
			
			i++;
		}
		
	}
	
	//Reads the Person xml objects from the disk
	public ArrayList<Person> decodeUsers() throws RemoteException
	{
		ArrayList<Person> users = new ArrayList<Person>();
		int i = 0;
		while(true)
		{
			String fileName = "user" + i + ".xml";
			try
			{
				Person usr = Person.decodeFromXML(fileName);
				users.add(usr);
			}
			catch(Exception e)
			{
				return users;
			}
			
			i++;
		}
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			BPServer service = new BPServer();
			Naming.rebind("Server", service);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
