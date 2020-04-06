package businessPlan;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote
{
	public void encode() throws RemoteException;
	
	public ArrayList<BusinessPlan> decodePlans() throws RemoteException;
	
	public ArrayList<Person> decodeUsers() throws RemoteException;
	
	public void addUser(Person p) throws RemoteException;
	
	public void addBusinessPlan(BusinessPlan bp) throws RemoteException, NotAllowedException;
		
	public Person validateLogin(String username, String password) throws NothingFoundException, RemoteException;
	
	public BusinessPlan findPlan(Person p, int year) throws RemoteException, NothingFoundException;
	
}
