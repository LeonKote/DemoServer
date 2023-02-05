package com.leonkote.demoserver.net;

import com.leonkote.demoserver.Utils;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class Client
{
	public final WebSocket socket;
	public final int id;
	public boolean isAuth;
	public String name;
	public Room room;

	public Client(WebSocket socket)
	{
		this.socket = socket;
		id = Utils.random();
	}

	public void send(String key, Object value)
	{
		socket.send(new JSONObject().put(key, value).toString());
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		if (name == null)
			return socket.getRemoteSocketAddress().getAddress().getHostAddress();
		else
			return name;
	}
}
