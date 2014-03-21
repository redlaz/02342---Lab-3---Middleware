package Middleware.Network.TCP;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Hashtable;

import javax.xml.crypto.Data;

import Middleware.Enums.MessageType;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Util.Serializer;

public class TcpRequester 
{
	private int port = 4446;
	
	public Hashtable<Integer, PeerReference> requestRoutingTable(InetAddress bootPeerAddress)  
	{
		try 
		{
			Socket socket = new Socket(bootPeerAddress, port);
			
			// Send
			Message data = new Message();
			data.setMessageType(MessageType.GET_COPY_OF_ROUTING_TABLE);
			byte[] dataBytes = Serializer.now(data);
			
			
			// Out
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeInt(dataBytes.length); // write length of the message
			dataOutputStream.write(dataBytes);           // write the message

			// In
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			int length = dataInputStream.readInt();
			byte[] message = new byte[length];
			dataInputStream.readFully(message, 0, message.length); // read the message
			Message messageIn = (Message)Serializer.back(message);
			return (Hashtable<Integer, PeerReference>)messageIn.getPayload();
		} 
		
		catch (IOException | ClassNotFoundException e) 
		{
			System.out.println("Rquester: "  + e.toString());
		}	
		
		return null;
	}
	
	private byte[] trim(byte[] bytes)
	{
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}
}