package edu.gordon.atm.physical.event;

import java.util.EventObject;

public class RetainCardEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	
	public RetainCardEvent(Object source) 
	{
		super(source);
	}
}
