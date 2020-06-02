package eia.population;

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
//import common.TransSftp;

public class GetPredict {

	// 인구주거 정보 서비스 - 예측속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("population_getPredict_url");
					String service_key = JsonParser.getProperty("population_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_35.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					if(json.indexOf("</") > -1){
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}

					// step 3.필요에 맞게 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {
						
						

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String aftFtePopltn = " "; // 사업 후 상근인구
						String aftUsePopltn = " "; // 사업 후 이용인구
						String aftHousePopltn = " "; // 사업 후 주택계획인구
						String nearbyAreaDevlopSttus = " "; // 인근지역 개발 현황(건수)

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("aftFtePopltn")) {
								aftFtePopltn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("aftUsePopltn")) {
								aftUsePopltn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("aftHousePopltn")) {
								aftHousePopltn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("nearbyAreaDevlopSttus")) {
								nearbyAreaDevlopSttus = body.get(keyname).toString().trim();
							}
						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업코드
							pw.write("|^");
							pw.write(aftFtePopltn); // 사업 후 상근인구
							pw.write("|^");
							pw.write(aftUsePopltn); // 사업 후 이용인구
							pw.write("|^");
							pw.write(aftHousePopltn); // 사업 후 주택계획인구
							pw.write("|^");
							pw.write(nearbyAreaDevlopSttus); // 인근지역 개발 현황(건수)
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_35.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
						//throw new Exception();
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", mgtNo :" + args[0]);
				System.exit(-1);
			}



	}

}
