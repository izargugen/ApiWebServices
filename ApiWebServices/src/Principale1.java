import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
 * Invocation d'un service web et obtention du résultat en JSON
 * 
 * 
 * 
 */

public class Principale1 {

	public static void main(String[] args) {
		String urlATraiter = "";
		int choixUrl = 3;
		
		switch (choixUrl){
		case 1: urlATraiter = "https://reqres.in/api/users"; break;
		case 2: urlATraiter = "http://dummy.restapiexample.com/api/v1/employees"; break;
		case 3: urlATraiter = "http://parabank.parasoft.com/parabank/services/bank/customers/12212"; break;
		}
			
		try {
			URL url =new URL(urlATraiter);
			//HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			String inline = "";
			Scanner scanner = new Scanner(url.openStream());
			
			while(scanner.hasNext()){
				inline+=scanner.next();
			}
			
			inline = inline.replaceAll("\\[", "\n\\[");
			inline = inline.replaceAll("\\]", "\n\\]");
			
			int compact = 0;
			
			if(compact==1){
				inline = inline.replaceAll("\\{", "\n\\{");
			}else{
				inline = inline.replaceAll("\\{", "\n\\{\n\t");
				inline = inline.replaceAll("\",\"", "\",\"\n\t");
			}
			
			System.out.println(inline);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
