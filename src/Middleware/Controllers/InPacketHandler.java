package Middleware.Controllers;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import Middleware.Interfaces.IInPacketHandlerCallback;
import Middleware.Models.Data;

public class InPacketHandler extends Thread
{
	private BlockingQueue<Data> ingoingPacketQueue;
	private List<Class<?>> subscriptionClasses;
	private IInPacketHandlerCallback controller;
	
	public InPacketHandler(BlockingQueue<Data> ingoingPacketQueue, List<Class<?>> subscriptionClasses,IInPacketHandlerCallback controller)
	{
		this.controller = controller;
		this.subscriptionClasses = subscriptionClasses;
		this.ingoingPacketQueue = ingoingPacketQueue;
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			Data data;
			try 
			{
				data = ingoingPacketQueue.take();
				System.out.println("Packet procssed");
				switch(data.getMessageType())
				{								
					case EVENT:
						if (subscriptionClasses.contains(data.getPayload().getClass()))
							controller.payloadReceived(data.getPayload());

						break;

					default:
						break;
				}
			} 
			
			catch (InterruptedException e) 
			{
				
			}
		}
	}	
}