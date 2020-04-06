package businessPlan;

public class CNTRAssessment extends BusinessPlan
{

	// create an empty tree for CNTR Assessment
	public CNTRAssessment()
	{
		root = new Section("Centre College Institutional Mission Statement");
		Section objective = new Section("Program Goals and Student Learning Objective");
		Section progMission = new Section("Program Mission Statement");
		progMission.addChild(objective);
		root.addChild(progMission);
		progMission.setParent(root);
		objective.setParent(progMission);
	}

	// add new section to the Assessment
	@Override
	public void addSection(Section parent) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		while (parent.name != "Program Goals and Student Learning Objective")
		{
			if (parent.name == "Centre College Institutional Mission Statement")
			{
				Section child = new Section("Program Mission Statement");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} else if (parent.name == "Program Mission Statement")
			{
				Section child = new Section("Program Goals and Student Learning Objective");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} else
			{
				System.out.println("ERROR: INVALID SECTION! ! !");
				return;
			}

		}

	}

}
