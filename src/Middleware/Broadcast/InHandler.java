package Middleware.Broadcast;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import Logger.Log;
import Middleware.Interfaces.Broadcast.IBroadcastInPacketHandler;
import Middleware.Models.Data;

public class InHandler extends Thread
{
	private BlockingQueue<Data> inQueue;
	private IBroadcastInPacketHandler controller;
	private boolean notStopped = true;
	
	public InHandler(BlockingQueue<Data> inQueue, IBroadcastInPacketHandler controller)
	{
		this.controller = controller;
		this.inQueue = inQueue;
	}
	
	public void stopThread()
	{
		notStopped = false;
	}
	
	@Override
	public void run() 
	{
		while(notStopped)
		{
			try 
			{
				Data data = inQueue.take();
				Log.Output("In packet processed", this);
				controller.payloadReceived(data);
				
			} 
			
			catch (InterruptedException e) 
			{
				
			}
		}
	}	
}