package com.leonkote.demoserver.net;

public class Auth
{
	public static void Execute(Client client, String name)
	{
		if (!name.matches("^[A-Za-zА-Яа-я0-9_]{3,18}$"))
		{
			client.send("authResult", "ERR_WRONGNAME");
			return;
		}

		client.isAuth = true;
		client.name = name;
		client.send("authResult", "OK");

		System.out.println(client.socket.getRemoteSocketAddress().getAddress().getHostAddress() + " authed as " + name);
	}
}
