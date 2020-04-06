package businessPlan;

public class BYBPlan extends BusinessPlan
{

	// create an empty tree of BYB plan
	public BYBPlan()
	{
		root = new Section("BYB Mission Statement");
		Section objective = new Section("BYB Objectives");
		Section plan = new Section("BYB Plan");
		plan.addChild(objective);
		root.addChild(plan);
		plan.setParent(root);
		objective.setParent(plan);
	}

	// BYB version of add a new Section to the business plan
	@Override
	public void addSection(Section parent) throws NotAllowedException
	{
		if(!isEditable)
			throw new NotAllowedException("This plan may not be edited");
		
		while (parent.name != "BYB Plan")
		{
			if (parent.name == "BYB Mission Statement")
			{
				Section child = new Section("BYB Objective");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} else if (parent.name == "BYB Objective")
			{
				Section child = new Section("BYB Plan");
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
