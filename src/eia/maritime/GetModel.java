package eia.maritime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;

import common.JsonParser;
//import common.TransSftp;

public class GetModel {

	// 해양환경정보조회 서비스 - 적용모델 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("maritime_getModel_url");
					String service_key = JsonParser.getProperty("maritime_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_13.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}*/

					// step 3.필요에 맞게 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

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
							pw.write(seawaterFlowModel.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 해수유동 적용모델
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_13.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
						throw new Exception();
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}



	}

}
