package wri.dataPresent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class DamCode {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문제원현황정보 - 댐 코드
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("dataPresent_damcode_url");
					String service_key = JsonParser.getProperty("dataPresent_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_08.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("damcode"); // 댐코드
							pw.write("|^");
							pw.write("damnm"); // 댐이름
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					// step 2. 파싱
					String json = "";

					json = JsonParser.parseWriJson(service_url, service_key);

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
					JSONObject items = (JSONObject) body.get("items");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (resultCode.equals("00")) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							String damcode = " "; // 댐코드
							String damnm = " "; // 댐이름

							while (iter.hasNext()) {

								String keyname = iter.next();

								if (keyname.equals("damcode")) {
									damcode = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("damnm")) {
									damnm = item_obj.get(keyname).toString().trim();
								}

							}

							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(damcode); // 댐코드
								pw.write("|^");
								pw.write(damnm); // 댐이름
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					} else {
						System.out.println("parsing error!!");
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_08.dat", "WRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
