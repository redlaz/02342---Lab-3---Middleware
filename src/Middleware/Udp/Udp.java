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
import Middleware.Enums.DataType;
import Middleware.Enums.EventType;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;
import Middleware.Util.DataContainer;
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
	
	// Constants
	private final int ID_NUM_BYTES = 4;
	private final int TYPE_NUM_BYTES = 1;
	private final int LENGTH_NUM_BYTES = 4;

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
	
	public void broadcast(DataType dataType, Object event, MessageType messageType) throws IOException
	{			
		DataContainer dataContainer = new DataContainer();
		dataContainer.addId(id);
		dataContainer.addType(messageType);
		
		byte[] eventData = Serializer.now(event);
		  
		// Data
		if (dataType != null && dataType.equals(DataType.STRING))
		{
			String message = (String)event;
			//dataContainer.addLength(ID_NUM_BYTES + TYPE_NUM_BYTES + LENGTH_NUM_BYTES + message.length());
			dataContainer.addLength(ID_NUM_BYTES + TYPE_NUM_BYTES + LENGTH_NUM_BYTES + eventData.length);
			//dataContainer.addPayload(message);
			dataContainer.addPayload(eventData);
		}
		
		else if(dataType != null && dataType.equals(DataType.INTEGER))
		{
			int integer = (int)event;
			dataContainer.addLength(ID_NUM_BYTES + TYPE_NUM_BYTES + LENGTH_NUM_BYTES + 4);
			dataContainer.addPayload(integer);
		}
		
		byte[] outgoingMessage = dataContainer.toByteArray();
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
					// Receive datagram packet
					DatagramPacket inputPacket = receiveDatagramPacket();
					
					// Add to queue
					ingoingQueue.add(inputPacket);
					
					// Convert to data container
					DataContainer dataContainer = new DataContainer(inputPacket.getData());

					//if (!inputPacket.getAddress().equals(InetAddress.getLocalHost())) // Ignore loopback
					if (dataContainer.isValid())
					{
						MessageType messageType = MessageType.fromByte(dataContainer.getType());
						
						switch(messageType)
						{								
							case EVENT:
								middleware.eventArrived();
								break;
		
							default:
								break;
						}
						
						String data = new String(inputPacket.getData()).trim();
						Log.Output(data, this);
					}
					else
						System.out.println("Packet invalid");
				} 
				
				catch (IOException e) 
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