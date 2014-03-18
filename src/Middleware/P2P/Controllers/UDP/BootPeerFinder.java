package Middleware.P2P.Controllers.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Util.Serializer;
import P2P.Enums.MessageType;

public class BootPeerFinder 
{
	private final int PORT = 4446;
	private final int MAXTIMEOUT = 100;
	
	public InetAddress go() throws MiddlewareIOException 
	{
		try 
		{
			DatagramSocket socket = new DatagramSocket();	
			socket.setBroadcast(true);
			socket.setSoTimeout(MAXTIMEOUT);

			// Request JOIN
			byte[] outBuffer = Serializer.now(MessageType.JOIN);
			DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length,getBroadcastAddress(),PORT);
			socket.send(outPacket);
			
			// Read 1 second (timeout) for GUID
			while(true)
			{
				byte[] inBuffer = new byte[1500];
				DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);	
				socket.receive(inPacket);
				MessageType messageType = (MessageType)Serializer.back(inPacket.getData());
				
				if (messageType.equals(MessageType.GUID))
					return inPacket.getAddress();
			}
		} 
		
		catch (IOException |  ClassNotFoundException  e) 
		{
			return null;
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