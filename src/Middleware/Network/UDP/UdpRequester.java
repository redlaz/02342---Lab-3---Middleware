package Middleware.Network.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import Middleware.Enums.MessageType;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.BootPeerRepons;
import Middleware.Util.Serializer;


public class UdpRequester 
{
	private final int PORT = 4446;
	private final int MAXTIMEOUT = 100;
	private DatagramSocket socket;
	
	public UdpRequester()
	{
		try 
		{
			this.socket = new DatagramSocket();
			socket.setBroadcast(true);
			socket.setSoTimeout(MAXTIMEOUT);
		} 
		
		catch (SocketException e) 
		{
			System.out.println(e.getMessage());
		}	
		
	}
	
	public BootPeerRepons join() throws MiddlewareIOException 
	{
		try 
		{
			// Request JOIN
			send(MessageType.JOIN);
			
			// Read 1 second (timeout) for GUID
			while(true)
			{
				DatagramPacket inPacket = receive();
				MessageType messageType = (MessageType)Serializer.back(inPacket.getData());
				
				if (messageType.equals(MessageType.GUID))
					return new BootPeerRepons(inPacket.getAddress(), 1);

			}
		} 
		
		catch (IOException |  ClassNotFoundException  e) 
		{
			return null;
		}
	}
	
	private DatagramPacket receive() throws IOException
	{
		byte[] inBuffer = new byte[1500];
		DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);	
		socket.receive(inPacket);
		return inPacket;
	}
	
	private void send(Object object) throws IOException, MiddlewareIOException
	{
		byte[] outBuffer = Serializer.now(object);
		DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length,getBroadcastAddress(),PORT);
		socket.send(outPacket);
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