package Middleware.Interfaces.Broadcast;

import Middleware.Models.Data;

public interface IBroadcastInPacketHandler 
{
	public void payloadReceived(Data data);
}
