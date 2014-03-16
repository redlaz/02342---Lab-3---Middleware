package Middleware.Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class OutPacketHandler extends Thread
{
	private BlockingQueue<Data> outgoingPacketQueue = null;
	private MulticastSocket multiSocket;
	private InetAddress group;
	private int port = 4446;
	private int id = 5;
	
	public OutPacketHandler(BlockingQueue<Data> outgoingPacketQueue) throws IOException
	{
		this.outgoingPacketQueue = outgoingPacketQueue;
		multiSocket = new MulticastSocket(this.port);
		this.group = InetAddress.getByName("230.0.0.1");
		multiSocket.joinGroup(this.group);
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			Data data;
			try 
			{
				data = this.outgoingPacketQueue.take();
				byte[] outgoingMessage = Serializer.now(data);
				DatagramPacket outgoingPacket = new DatagramPacket(outgoingMessage, outgoingMessage.length, InetAddress.getByName("192.168.1.255"),port);
				multiSocket.send(outgoingPacket);
				System.out.println("Sending packet");
			} 
			
			catch (InterruptedException | IOException e) 
			
			{
				
			}
			
		}	
	}
}