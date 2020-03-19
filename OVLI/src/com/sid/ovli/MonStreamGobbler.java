package com.sid.ovli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MonStreamGobbler extends Thread{
	 InputStream is;
	    String type;
	    OutputStream os;
	    
	    String resultat = null;
	    
	    public MonStreamGobbler(InputStream is, String type)
	    {
	        this(is, type, null);
	    }
	    
	    public  MonStreamGobbler(InputStream is, String type, OutputStream redirect)
	    {
	        this.is = is;
	        this.type = type;
	        this.os = redirect;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            PrintWriter pw = null;
	            if (os != null)
	                pw = new PrintWriter(os);
	                
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            
	            String line=null;
	            while ( (line = br.readLine()) != null)
	            {
	                if (pw != null)
	                    pw.println(line);
	                System.out.println(type + ">" + line); 
	                
	                resultat = resultat + line + "\n";
	            }
	            if (pw != null)
	                pw.flush();
	        } catch (IOException ioe)
	            {
	            ioe.printStackTrace();  
	            }
	    }
	    
	    public String getResultat(){
	    	return resultat;
	    }

}
