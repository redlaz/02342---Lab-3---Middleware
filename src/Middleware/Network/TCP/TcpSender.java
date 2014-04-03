package Middleware.Network.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;
import Middleware.Enums.MessageType;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Util.Serializer;

public class TcpSender 
{
	private int port = 4446;
	private Socket socket;
	
	public Hashtable<Integer, PeerReference> requestRoutingTable(InetAddress bootPeerAddress)  
	{
		try 
		{
			socket = new Socket(bootPeerAddress, port);
			
			// Request routing table
			Message data = new Message();
			data.setMessageType(MessageType.GET_COPY_OF_ROUTING_TABLE);
			write(data);         

			// Get routing table
			byte[] message = read();
			Message messageIn = (Message)Serializer.back(message);
			return (Hashtable<Integer, PeerReference>)messageIn.getPayload();
		} 
		
		catch (IOException | ClassNotFoundException e) 
		{
			System.out.println("Rquester: "  + e.toString());
		}	
		
		return null;
	}
	
	private byte[] read() throws IOException
	{
		DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
		int length = dataInputStream.readInt();
		byte[] message = new byte[length];
		dataInputStream.readFully(message, 0, message.length); 
		return message;
	}
	
	private void write(Object object) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
		byte[] message = Serializer.now(object);
		dataOutputStream.writeInt(message.length);
		dataOutputStream.write(message);   
		dataOutputStream.flush();
	}
}