package businessPlan;
import java.io.Serializable;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class Person implements Serializable
{
	private static final long serialVersionUID = -993955748228741007L;
	public String username;
	public String password;
	public String department;
	public boolean isAdmin;
	
	public Person()
	{
		username = "name";
		password = "pw";
		department = "dep";
		isAdmin = true;
	}
	
	public Person(String usr, String pw, String dep, boolean adm)
	{
		username = usr;
		password = pw;
		department = dep;
		isAdmin = adm;
	}
	
	public void encodeToXML(String fileName)
	{
		final String SERIALIZED_FILE_NAME = fileName;
		XMLEncoder encoder = null;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
		} 
		catch (NullPointerException | FileNotFoundException fileNotFound)
		{
			System.out.println("ERROR: While Creating or Opening the File");
		}
		encoder.writeObject(this);
		encoder.close();
	}
	
	public static Person decodeFromXML(String fileName)
	{

		final String SERIALIZED_FILE_NAME = fileName;
		XMLDecoder decoder = null;
		try
		{
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
		} 
		catch (NullPointerException | FileNotFoundException e)
		{

		}
		Person person = (Person) decoder.readObject();
		return person;
	}
		
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getDepartment()
	{
		return department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}
}
