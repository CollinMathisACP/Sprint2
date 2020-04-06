package businessPlan;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BPClient
{
	public ServerInterface server;
	public Person loggedIn;
	public BusinessPlan currentPlan;
	
	public BPClient(ServerInterface s, String username, String password)
	{
		server = s;
		loggedIn = new Person();
		currentPlan = new BusinessPlan();
		askForLogin(username, password);
		
		//Make sure nothing gets overwritten prematurely
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		//Tells the server to save data to the disk every two minutes
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable()
		{
		  @Override
		  public void run()
		  {
			
			try
			{
				server.encode();
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
			
		  }
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	//Attempt to log in
	public void askForLogin(String username, String password)
	{
		try
		{
			loggedIn = server.validateLogin(username, password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Add a user to the server's list. Only works if an admin is logged in
	public void addPerson(String username, String password, String department, boolean isAdmin) throws NotAllowedException
	{
		if(!loggedIn.isAdmin)
			throw new NotAllowedException("The current user may not perform this action");
		
		Person p = new Person(username, password, department, isAdmin);
		try
		{
			server.addUser(p);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}	
	}
	
	//Sends a plan to be added to the server's list. Will not work if trying to overwrite a non-editable plan
	public void sendPlan() throws NotAllowedException
	{	
		try
		{
			server.addBusinessPlan(currentPlan);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	//Gives the user a blank plan to work with
	public void blankPlan()
	{
		currentPlan = new BusinessPlan("", 0, true);
	}
	
	//Change whether the current plan can be edited. Only available to administrators.
	public void changeEditable(boolean val) throws NotAllowedException
	{
		if(!loggedIn.isAdmin)
			throw new NotAllowedException("The current user may not perform this action");
		
		currentPlan.setEditable(val);
	}
	
	//Asks the server for the BusinessPlan from the user's department and a given year
	public void askForBP(int year)
	{
		try
		{
			BusinessPlan plan = server.findPlan(loggedIn, year);
			currentPlan = plan;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
