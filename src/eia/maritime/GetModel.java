package eia.maritime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
import common.TransSftp;

public class GetModel {

	

	// 해양환경정보조회 서비스 - 적용모델 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("maritime_getModel_url");
			String service_key = JsonParser.getProperty("maritime_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_13.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("mgtNo"); // 사업 코드
					pw.write("|^");
					pw.write("seawaterFlowModel"); // 해수유동 적용모델
					pw.write("|^");
					pw.write("bfeSusDffAr"); // 부유사확산 면적(1㎎/ℓ) 저감대책 전
					pw.write("|^");
					pw.write("aftSusDffAr"); // 부유사확산 면적(1㎎/ℓ) 저감대책 후
					pw.write("|^");
					pw.write("bfeSusDffLt"); // 부유사확산 거리(1㎎/ℓ) 저감대책 전
					pw.write("|^");
					pw.write("aftSusDffLt"); // 부유사확산 거리(1㎎/ℓ) 저감대책 후
					pw.write("|^");
					pw.write("rm"); // 비고
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			String json = "";

			json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);

			// step 3.필요에 맞게 파싱

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
				JSONObject header = (JSONObject) response.get("header");
				JSONObject body = (JSONObject) response.get("body");

				String resultCode = header.get("resultCode").toString().trim();

				if (resultCode.equals("00")) {

					Set<String> key = body.keySet();

					Iterator<String> iter = key.iterator();

					String seawaterFlowModel = " "; // 해수유동 적용모델
					String bfeSusDffAr = " "; // 부유사확산 면적(1㎎/ℓ) 저감대책 전
					String aftSusDffAr = " "; // 부유사확산 면적(1㎎/ℓ) 저감대책 후
					String bfeSusDffLt = " "; // 부유사확산 거리(1㎎/ℓ) 저감대책 전
					String aftSusDffLt = " "; // 부유사확산 거리(1㎎/ℓ) 저감대책 후
					String rm = " "; // 비고

					while (iter.hasNext()) {
						String keyname = iter.next();

						if (keyname.equals("seawaterFlowModel")) {
							seawaterFlowModel = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeSusDffAr")) {
							bfeSusDffAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftSusDffAr")) {
							aftSusDffAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeSusDffLt")) {
							bfeSusDffLt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftSusDffLt")) {
							aftSusDffLt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("rm")) {
							rm = body.get(keyname).toString().trim();
						}

					}

					// step 4. 파일에 쓰기
					try {
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.write(mgtNo); // 사업 코드
						pw.write("|^");
						pw.write(seawaterFlowModel); // 해수유동 적용모델
						pw.write("|^");
						pw.write(bfeSusDffAr); // 부유사확산 면적(1㎎/ℓ) 저감대책 전
						pw.write("|^");
						pw.write(aftSusDffAr); // 부유사확산 면적(1㎎/ℓ) 저감대책 후
						pw.write("|^");
						pw.write(bfeSusDffLt); // 부유사확산 거리(1㎎/ℓ) 저감대책 전
						pw.write("|^");
						pw.write(aftSusDffLt); // 부유사확산 거리(1㎎/ℓ) 저감대책 후
						pw.write("|^");
						pw.write(rm); // 비고
						pw.println();
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_13.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
					
				} else if (resultCode.equals("03")) {
					System.out.println("data not exist!! mgtNo :" + mgtNo);
				} else {
					System.out.println("parsing error!! mgtNo :" + mgtNo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + mgtNo);
			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
