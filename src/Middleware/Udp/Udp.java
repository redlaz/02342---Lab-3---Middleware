package Middleware.Udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import Logger.Log;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;
import Middleware.Util.Data;
import Middleware.Util.Serializer;

public class Udp 
{
	private MulticastSocket multiSocket;
	private InetAddress group;
	private int port = 4446;
	private int id = 5;
	private IEventRaised middleware;
	private Queue<DatagramPacket> ingoingQueue;
	private Queue<byte[]> outgoingQueue;
	private List<Class<?>> subscriptionTypes;


	public Udp(IEventRaised middleware) throws IOException 
	{
		subscriptionTypes = new ArrayList<Class<?>>();
		
		ingoingQueue = new LinkedList<DatagramPacket>();
		outgoingQueue = new LinkedList<byte[]>();
	
		
		multiSocket = new MulticastSocket(this.port);
		this.group = InetAddress.getByName("230.0.0.1");
		multiSocket.joinGroup(this.group);
		this.middleware = middleware;
		
		// Start listening
		Read read = new Read();
		Thread readThread = new Thread(read);
		readThread.start();
	}
	
	public void addToSubscriptions(Class<?> classType)
	{
		subscriptionTypes.add(classType);
	}
	
	public void removeFromSubscriptions(Class<?> classType)
	{
		if (subscriptionTypes.contains(classType))
			subscriptionTypes.remove(classType);
	}
	
	public void broadcast(Object event, MessageType messageType) throws IOException
	{		
		Data data = new Data(messageType, id,  event);	
		byte[] outgoingMessage = Serializer.now(data);
		DatagramPacket outgoingPacket = new DatagramPacket(outgoingMessage, outgoingMessage.length, InetAddress.getByName("192.168.1.255"),this.port);
		multiSocket.send(outgoingPacket);
	}
	
	private class Read implements Runnable
	{
		private MulticastSocket socket;
		
		public Read() throws IOException
		{
			socket = new MulticastSocket(4446);
			socket.joinGroup(group);	
		}
		
		@Override
		public void run() 
		{
			while(true)
			{
				try 
				{
					DatagramPacket inputPacket = receiveDatagramPacket();
					
					// Add to queue
					ingoingQueue.add(inputPacket);
					
					Data data = (Data) Serializer.back(inputPacket.getData());
			
					
					switch(data.getMessageType())
					{								
						case EVENT:
							if (subscriptionTypes.contains(data.getPayload().getClass()))
								middleware.eventArrived(data.getPayload());
							break;
	
						default:
							break;
					}
				} 
				
				catch (IOException | ClassNotFoundException e) 
				{
					Log.Output(e.toString(), this);
				}
			}
		}
		
		private DatagramPacket receiveDatagramPacket() throws IOException
		{
			byte[] inputBuffer = new byte[1500];
			DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);	
			socket.receive(inputPacket);
			return inputPacket;
		}
	}
}