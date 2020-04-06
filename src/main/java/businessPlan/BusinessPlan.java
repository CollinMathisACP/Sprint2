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

public class BusinessPlan implements Serializable
{
	private static final long serialVersionUID = 8506625811336059650L;
	public Section root;
	public String department;
	public int year;
	public boolean isEditable;

	public BusinessPlan()
	{
		root = null;
		department = "dep";
		year = -1;
		isEditable = true;
	}
	
	public BusinessPlan(String dep, int yr, boolean edit)
	{
		root = null;
		department = dep;
		year = yr;
		isEditable = edit;
	}
	
	public void addSection(Section parent) throws NotAllowedException
	{
		//overriden by subclasses
	}
	// the only abstract method
	//if needed in the future, we may change the abstract method and let it throw Exceptions, but for now
	//we think print out is enough. And we also may simplify it. 

	public void deleteSection(Section child) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		// check the node can be deleted or not
		// delete the whole branch
		// in order to keep a complete structure of the tree, we need to make sure that
		// except for the last section, all the other sections must have at least one
		// child.
		Section parent = child.getParent();
		ArrayList<Section> children = parent.getChildren();
		int size = children.size();
		if (size != 1)
		{
			parent.deleteChild(child);
		} else
		{
			System.out.println("ERROR: THIS SECTION CANNOT BE DELETED! ! !");
		}

	}

	// encode the business plan to XML file
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

	// decode the business plan from a XML file
	public static BusinessPlan decodeFromXML(String fileName)
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
		BusinessPlan plan = (BusinessPlan) decoder.readObject();
		return plan;
	}

	// getter of root for XML
	public Section getRoot()
	{
		return root;
	}

	// setter of root for XML, never use that.
	public void setRoot(Section root)
	{
		this.root = root;
	}
	
	public String getDepartment()
	{
		return department;
	}
	
	//might screw with xml
	public void setDepartment(String department) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		this.department = department;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		this.year = year;
	}

	public boolean isEditable()
	{
		return isEditable;
	}

	public void setEditable(boolean isEditable) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		this.isEditable = isEditable;
	}

}
