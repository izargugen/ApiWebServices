package com.sid.ovli.model;

public class TestChannelClass {

	public static void main(String[] args) {
		Channel channel = new Channel("test","http://iptv7.premium-stv.com:25461/gnsportfrshtf1/fqzo522Da264/4");
		channel.display();
		
		System.out.println();
		
		channel = new Channel("test2","http://iptv7.premium-stv.com:25461/gnsportfrshtf1/fqzo522Da264/5");
		channel.display();
		System.out.println(channel.getChannelHeaderList().get("Server"));
		System.out.println(channel.getChannelHeaderList().get("Connection"));
		
		
		
		
		
		
		
		
		

	}

}
