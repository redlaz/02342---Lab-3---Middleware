package Middleware.Broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

import Logger.Log;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class Receiver extends Thread
{
	private MulticastSocket socket;
	private BlockingQueue<Data> inQueue;
	private int port = 4446;
	private boolean notStopped = true;
	
	public Receiver(BlockingQueue<Data> inQueue) throws IOException
	{
		this.inQueue = inQueue;
		socket = new MulticastSocket(port);
		socket.joinGroup(InetAddress.getByName("230.0.0.1"));		
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
				DatagramPacket inputPacket = receivePacket();
				Data data = (Data) Serializer.back(inputPacket.getData());
				inQueue.add(data);	
				Log.Output("In packet queued", this);
			} 
			
			catch (IOException | ClassNotFoundException e) 
			{
				Log.Output(e.toString(), this);
			}
		}
	}
	
	private DatagramPacket receivePacket() throws IOException
	{
		byte[] inBuffer = new byte[1500];
		DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);	
		socket.receive(packet);
		return packet;
	}
}
