import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.jupiter.api.Test;

import businessPlan.BPClient;
import businessPlan.BPServer;
import businessPlan.NotAllowedException;
import businessPlan.Person;
import businessPlan.ServerInterface;

class Sprint2Tester
{

	@Test
	void test1() throws NotBoundException
	{
		try
		{
			//PART 1: Test initial connection
			BPServer obj = new BPServer();
			Naming.rebind("Server", obj);
			String hostName = "127.0.0.1";
			Registry registry = LocateRegistry.getRegistry(hostName);
			ServerInterface stub = (ServerInterface) registry.lookup("Server");
			
			//Test login
			BPClient client = new BPClient(stub, "A1", "A1");
			
			//PART 2: Test admin privileges with admin
			client.addPerson("D4", "D4", "D4", false);
			
			client.currentPlan.setEditable(true);
			//Replace the plan
			client.sendPlan();
			//Save changes. Note:this kind of access is just for testing purposes
			client.server.encode();
			
			//PART 3: Run this multiple times to see that the encoding/decoding work
			
			//Part 4: Wait two minutes (or change the source code to make it faster) to see that the auto-save works
							
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (NotAllowedException e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	void test2() throws NotBoundException
	{
		try
		{
			BPServer obj = new BPServer();
			Naming.rebind("Server", obj);
			String hostName = "127.0.0.1";
			Registry registry = LocateRegistry.getRegistry(hostName);
			ServerInterface stub = (ServerInterface) registry.lookup("Server");
			
			BPClient client = new BPClient(stub, "D4", "D4");
			
			//PART 5: Test admin privileges with non-admin
			assertFalse(client.loggedIn.isAdmin);
			assertThrows(NotAllowedException.class, () -> {client.changeEditable(true);});		
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
	}

}
