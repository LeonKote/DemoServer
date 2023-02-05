package com.leonkote.demoserver;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.PemUtils;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.nio.file.Paths;

public class Main
{
	public static void main(String[] args)
	{
		int port = 8887;
		Server server = new Server(port);
		server.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(getContext()));
		server.setConnectionLostTimeout(30);
		server.start();
		System.out.println("Server started on port: " + port);
	}

	private static SSLContext getContext()
	{
		X509ExtendedKeyManager keyManager = PemUtils.loadIdentityMaterial(Paths.get("/etc/ssl/rtflegion.crt"), Paths.get("/etc/ssl/private.key"));
		X509ExtendedTrustManager trustManager = PemUtils.loadTrustMaterial(Paths.get("/etc/ssl/chain.crt"));

		SSLFactory sslFactory = SSLFactory.builder()
				.withIdentityMaterial(keyManager)
				.withTrustMaterial(trustManager)
				.build();

		return sslFactory.getSslContext();
	}
}