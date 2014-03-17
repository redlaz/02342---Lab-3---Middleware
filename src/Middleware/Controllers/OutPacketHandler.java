package Middleware.Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class OutPacketHandler extends Thread
{
	private BlockingQueue<Data> outgoingPacketQueue = null;
	private MulticastSocket multiSocket;
	private InetAddress group;
	private InetAddress broadcast;
	private int port = 4446;
	private int id = 5;
	
	public OutPacketHandler(BlockingQueue<Data> outgoingPacketQueue) throws IOException
	{
		this.outgoingPacketQueue = outgoingPacketQueue;
		multiSocket = new MulticastSocket(this.port);
		this.group = InetAddress.getByName("230.0.0.1");
		multiSocket.joinGroup(this.group);
		this.broadcast = getBroadcastAddress();
		
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
				DatagramPacket outgoingPacket = new DatagramPacket(outgoingMessage, outgoingMessage.length,broadcast,port);
				multiSocket.send(outgoingPacket);
				System.out.println("Sending packet");
				
				
				
			} 
			
			catch (InterruptedException | IOException e) 
			
			{
				
			}
			
		}	
	}
	
	private InetAddress getBroadcastAddress()
	{
		Enumeration<NetworkInterface> interfaces;
		try 
		{
			interfaces = NetworkInterface.getNetworkInterfaces();
			
			while (interfaces.hasMoreElements()) 
			{
			  NetworkInterface networkInterface = interfaces.nextElement();
			  
			  if (networkInterface.isLoopback())
			    continue;    // Don't want to broadcast to the loopback interface
			  
			  for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) 
			  {
			    InetAddress broadcast = interfaceAddress.getBroadcast();
			    
			    System.out.println(broadcast);
			    if (broadcast != null)
			    {
			    	System.out.println(broadcast.getHostName());
			    	//return broadcast;
			    }
			  }
			}
		} 
		
		catch (SocketException e) 
		{
		
		}
		
		return null;
	}
}