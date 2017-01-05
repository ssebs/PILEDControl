import java.io.*;
/**
* Static class that sends commands to the terminal in a friendly way
*
*/
public class GPIOUtil
{
	
	/*
	* Wll change the mode. (Will set pin to in/out/etc)
	*/
	public static void setMode(int pin, String mode)
	{
		cmd("gpio mode " + pin + " " + mode); 
	}
	
	/*
	* Will output a value to a pin *With a sleep duration*
	*/
	public static void out(int pin, int val, long millis) throws Exception
	{
		cmd("gpio write " + pin + " " + val);
		Thread.sleep(millis);
	}
	
	/*
	* Will output a value to a pin
	*/
	public static void out(int pin, int val)
	{
		cmd("gpio write " + pin + " " + val);
	}
	
	/*
	* WIll read the logical value of pin
	*/
	public static int in(int pin)
	{
		String s = cmd("gpio read " + pin);
		return Integer.parseInt(s);
	}
	
	/*
	* This will run a command from bash
	*****		TODO: Fix this so that in() can be used more than once		*****
	*/
	private static String cmd(String command) 
	{
		String out = "";
		try {			
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
			BufferedReader read=new BufferedReader(new InputStreamReader(pr.getInputStream()));

			while(read.ready())
			{
				out+=read.readLine();
			}
			read.close();
		} catch (Exception e) { e.printStackTrace(); }
		
		return out;
	}
	
}