package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CharTest {
	
	public static String StringReplace(String str){
		
        String match = "[^\u30A0-\u30FF\u3040-\u309F\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str =str.replaceAll(match, "");
        return str;
}	

	public static void main(String[] args) {
		
		File file = new File("charTest.dat");
		
		String testStr = "韓国で新型コロナ感染拡大、首都圏を中心に「3次ピーク」迎える可能性も―中国メディア";
		//String testStr2 = "\u0001";
		
		try {

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			
			pw.write(StringReplace(testStr)); 
			pw.println(StringReplace(testStr));
			pw.flush();
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("파일 다씀!");
	}

}
