package com.ej.automation;

public class Test {

	public static void main(String[] args) {
		System.out.println("\\\\");
		
		byte[] bytesIn = new byte[4096];
		System.out.println(bytesIn.length);
		String inputString = "Hello World! Hello World! Hello World! Hello World!";
		bytesIn = inputString.getBytes();
		System.out.println(bytesIn.length);
		
	}
}
