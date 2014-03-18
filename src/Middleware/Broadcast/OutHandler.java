package Middleware.Broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import Logger.Log;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class OutHandler extends Thread
{
	private BlockingQueue<Data> outQueue = null;
	private MulticastSocket multiSocket;
	private InetAddress group;
	private InetAddress broadcastAddr;
	private int port = 4446;
	private int id = 5;
	private boolean notStopped = true;
	
	public OutHandler(BlockingQueue<Data> outQueue) throws IOException, MiddlewareIOException
	{
		this.outQueue = outQueue;
		multiSocket = new MulticastSocket(this.port);
		this.group = InetAddress.getByName("230.0.0.1");
		multiSocket.joinGroup(this.group);
		this.broadcastAddr = getBroadcastAddress();
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
			Data data;
			try 
			{
				data = this.outQueue.take();
				byte[] dataBytes = Serializer.now(data);
				DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length,broadcastAddr,port);
				multiSocket.send(packet);
				Log.Output("Out packet sent", this);
			} 
			
			catch (InterruptedException | IOException e) 
			{
				
			}
		}	
	}
	
	private InetAddress getBroadcastAddress() throws MiddlewareIOException
	{
		try 
		{
			NetworkInterface networkInterface;
			networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();

	        for (InterfaceAddress interfaceAddress : addresses) 
	        {
	            if (interfaceAddress.getBroadcast() != null)
	            	return interfaceAddress.getBroadcast();
	        }
		} 
		
		catch (SocketException | UnknownHostException e) 
		{
			throw new MiddlewareIOException(e.getMessage());
		}

		return null;
	}
}