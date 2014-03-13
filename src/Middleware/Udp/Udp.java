package Middleware.Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;


import Logger.Log;
import Middleware.Enums.DataType;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;

public class Udp 
{
	private MulticastSocket multiSocket;
	private int port = 8888;
	private String group = "225.4.5.6";
	private int id = 5;
	private IEventRaised middleware;

	public Udp(IEventRaised middleware) throws IOException 
	{
		multiSocket = new MulticastSocket(port);
		multiSocket.joinGroup(InetAddress.getByName(group));
		this.middleware = middleware;
		
		Read read = new Read();
		Thread readThread = new Thread(read);
		readThread.start();
		
	}
	
	public void multicast(DataType dataType, Object event, MessageType messageType) throws IOException
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
	
		
		byte[] sendData = new byte[data.size()];
		
		for (int i = 0; i < sendData.length; i++) 
		{
			sendData[i] = data.get(i);
		}

		DatagramPacket outputPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"),8888);
		multiSocket.send(outputPacket);
	}
	
	private class Read implements Runnable
	{
		private MulticastSocket readSocket;
		
		public Read() throws IOException
		{
			readSocket = new MulticastSocket(port);
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
					byte[] inputData = inputPacket.getData();
				
					
					if (inputPacket.getAddress().equals(InetAddress.getLocalHost())) // Ignore multicast loopback
					{
						MessageType messageType = MessageType.fromByte(inputData[1]);
						
						switch(messageType)
						{
							case DISCOVER:
								System.out.println("Discover");
								multicast(null, null, MessageType.DISCOVERY_REPONSE);
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