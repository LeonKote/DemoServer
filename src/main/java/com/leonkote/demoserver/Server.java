package com.leonkote.demoserver;

import com.leonkote.demoserver.net.Auth;
import com.leonkote.demoserver.net.Client;
import com.leonkote.demoserver.net.Room;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server extends org.java_websocket.server.WebSocketServer
{
	public static Map<Integer, Room> rooms = new HashMap<>();

	public Server(int port)
	{
		super(new InetSocketAddress(port));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake)
	{
		Client client = new Client(conn);
		conn.setAttachment(client);

		System.out.println(client.getName() + " entered the server!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		Client client = conn.getAttachment();

		if (client.room != null)
			client.room.onClientLeave(client);

		System.out.println(client.getName() + " has left the server!");
	}

	@Override
	public void onMessage(WebSocket conn, String message)
	{
		JSONObject request;
		try
		{
			request = new JSONObject(message);
		}
		catch (JSONException e)
		{
			return;
		}

		Client client = conn.getAttachment();

		switch (request.names().getString(0))
		{
			case "auth":
				Auth.Execute(client, request.getString("auth"));
				break;
			case "create":
				if (client.room == null)
				{
					int code = Utils.randomRange(100000, 1000000);
					Room room = new Room(code, client);
					rooms.put(code, room);
				}
				break;
			case "join":
				if (client.isAuth && request.get("join") instanceof Integer
						&& rooms.containsKey(request.getInt("join")))
					rooms.get(request.getInt("join")).onClientJoin(client);
				break;
			case "leave":
				if (client.room != null)
					client.room.onClientLeave(client);
				break;
			case "message":
				if (client.room != null)
					client.room.onClientMessage(client.id, request.getString("message"));
				break;
		}
		System.out.println(client.getName() + ": " + message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex)
	{
		ex.printStackTrace();
		if (conn != null)
		{
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart()
	{
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}
}
