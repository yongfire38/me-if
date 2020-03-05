package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;



public class FileEdit {
	
	

	/**
	 * 파일내의 모든 라인의 마지막 두 글자(특정 구분자) 삭제
	 * 
	 * @param inFile
	 *            : 입력 파일명
	 * @throws Exception
	 */
	public void editStringInFile(File inFile) throws Exception {

		try {
			// 파일에서 읽은 한라인을 저장하는 임시변수
			String resultCol = "";

			// 임시파일을 만듭니다.
			File outFile = new File("$$$$$$$$.tmp");

			// 아규먼트로 받은 입력 파일
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			// output 파일
			FileOutputStream fos = new FileOutputStream(outFile);
			PrintWriter out = new PrintWriter(fos);

			// 파일 내용을 한라인씩 읽어 마지막 두 글자인 구분자를 삭제
			while ((resultCol = in.readLine()) != null) {

				//전체 데이터를 읽었음에도 불구하고 전부 응답코드가 00 외의 것들이어서 데이터가 나오지 않았을 경우의 에러 방지용
				if(!(resultCol.equals(""))){
					resultCol = resultCol.substring(0, resultCol.length() - 2);
				}
				

				out.write(resultCol + "\r\n");
				

			}
			
			out.flush();
			out.close();
			in.close();

			inFile.delete();

			// 임시파일을 원래 파일명으로 변경
			outFile.renameTo(inFile);

			System.out.println("CHANGE OK~~~");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
