package Middleware.Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import Logger.Log;
import Middleware.Enums.DataType;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;

public class Udp 
{
	private DatagramSocket socket;
	private int port = 80;
	private int id = 5;
	private IEventRaised middleware;
	private Queue<byte[]> ingoingQueue;
	private Queue<byte[]> outgoingQueue;

	public Udp(IEventRaised middleware) throws IOException 
	{
		ingoingQueue = new LinkedList<byte[]>();
		outgoingQueue = new LinkedList<byte[]>();
		
		this.socket = new DatagramSocket();
		this.socket.setBroadcast(true);
		this.middleware = middleware;
		
		// Start listening
		Read read = new Read();
		Thread readThread = new Thread(read);
		readThread.start();
	}
	
	public void broadcast(DataType dataType, Object event, MessageType messageType) throws IOException
	{	
		List<Byte> data = new ArrayList<Byte>();
		
		data.add((byte)id); 
		data.add((byte)messageType.getByte()); 
		
		// Data
		if (dataType != null && dataType.equals(DataType.STRING))
		{
			String message = (String)event;
			for (byte stringByte : message.getBytes()) 
			{
				data.add(stringByte);
			}
		}
		
		else if(dataType != null && dataType.equals(DataType.INTEGER))
		{
			//byte[] integerByteArray = ((Integer)event).;
		}
	
		
		byte[] outgoingMessage = new byte[data.size()];
		
		for (int i = 0; i < outgoingMessage.length; i++) 
		{
			outgoingMessage[i] = data.get(i);
		}

		outgoingQueue.add(outgoingMessage);
		DatagramPacket outgoingPacket = new DatagramPacket(outgoingMessage, outgoingMessage.length, InetAddress.getByName("255.255.255.255"),port);
		socket.send(outgoingPacket);
	}
	
	private class Read implements Runnable
	{
		private DatagramSocket readSocket;
		
		public Read() throws IOException
		{
			this.readSocket = new DatagramSocket(null); // Udp socket
			this.readSocket.setReuseAddress(true); // Make room for multiple listeners
			this.readSocket.setBroadcast(true); // 
			this.readSocket.bind(new InetSocketAddress(port));
		}
		
		@Override
		public void run() 
		{
			while(true)
			{
				try 
				{
					byte[] inputBuffer = new byte[1500];
					DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
					readSocket.receive(inputPacket);
					byte[] ingoingMessage = inputPacket.getData();
					ingoingQueue.add(ingoingMessage);
					
					if (inputPacket.getAddress().equals(InetAddress.getLocalHost())) // Ignore loopback
					{
						MessageType messageType = MessageType.fromByte(ingoingMessage[1]);
						
						switch(messageType)
						{
							case DISCOVER:
								System.out.println("Discover");
								broadcast(null, null, MessageType.DISCOVERY_REPONSE);
								break;
								
							case DISCOVERY_REPONSE:
								System.out.println("Discover repons");
								break;
								
							case PUBLISHMENT:
								System.out.println("Publishment received");
								break;
								
							case EVENT:
								middleware.eventArrived();
								break;
		
							default:
								break;
						}
						
						String data = new String(inputPacket.getData()).trim();
						Log.Output(data, this);
					}
				} 
				
				catch (IOException e) 
				{
					Log.Output(e.toString(), this);
				}
			}
		}
	}
}