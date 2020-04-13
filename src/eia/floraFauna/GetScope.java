package eia.floraFauna;

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

public class GetScope {

	// 동식물상 정보 조회 - 조사범위 속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("florafauna_getScope_url");
					String service_key = JsonParser.getProperty("florafauna_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_30.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("ivstgOdr"); // 조사차수
							pw.write("|^");
							pw.write("ivstgBgnde"); // 조사시작일
							pw.write("|^");
							pw.write("ivstgEndde"); // 조사종료일
							pw.write("|^");
							pw.write("plntIvstgYn"); // 식물상조사유무
							pw.write("|^");
							pw.write("animalIvstgYn"); // 포유류조사유무
							pw.write("|^");
							pw.write("birdsIvstgYn"); // 조류조사유무
							pw.write("|^");
							pw.write("herptileIvstgYn"); // 양서파충류조사유무
							pw.write("|^");
							pw.write("insectIvstgYn"); // 곤충류조사유무
							pw.write("|^");
							pw.write("fishesIvstgYn"); // 어류조사유무
							pw.write("|^");
							pw.write("benthosIvstgYn"); // 저서생물(동물)유무
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

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {

						JSONArray ivstgs = (JSONArray) body.get("ivstgs");

						for (int r = 0; r < ivstgs.size(); r++) {

							JSONObject ivstg = (JSONObject) ivstgs.get(r);

							Set<String> key = ivstg.keySet();

							Iterator<String> iter = key.iterator();

							String ivstgOdr = " "; // 조사차수
							String ivstgBgnde = " "; // 조사시작일
							String ivstgEndde = " "; // 조사종료일
							String plntIvstgYn = " "; // 식물상조사유무
							String animalIvstgYn = " "; // 포유류조사유무
							String birdsIvstgYn = " "; // 조류조사유무
							String herptileIvstgYn = " "; // 양서파충류조사유무
							String insectIvstgYn = " "; // 곤충류조사유무
							String fishesIvstgYn = " "; // 어류조사유무
							String benthosIvstgYn = " "; // 저서생물(동물)유무

							while (iter.hasNext()) {
								String keyname = iter.next();

								if (keyname.equals("ivstgOdr")) {
									ivstgOdr = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("ivstgBgnde")) {
									ivstgBgnde = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("ivstgEndde")) {
									ivstgEndde = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("plntIvstgYn")) {
									plntIvstgYn = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("animalIvstgYn")) {
									animalIvstgYn = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("herptileIvstgYn")) {
									herptileIvstgYn = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("insectIvstgYn")) {
									insectIvstgYn = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("fishesIvstgYn")) {
									fishesIvstgYn = ivstg.get(keyname).toString().trim();
								}
								if (keyname.equals("benthosIvstgYn")) {
									benthosIvstgYn = ivstg.get(keyname).toString().trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(mgtNo); // 사업 코드
								pw.write("|^");
								pw.write(ivstgOdr); // 조사차수
								pw.write("|^");
								pw.write(ivstgBgnde); // 조사시작일
								pw.write("|^");
								pw.write(ivstgEndde); // 조사종료일
								pw.write("|^");
								pw.write(plntIvstgYn); // 식물상조사유무
								pw.write("|^");
								pw.write(animalIvstgYn); // 포유류조사유무
								pw.write("|^");
								pw.write(birdsIvstgYn); // 조류조사유무
								pw.write("|^");
								pw.write(herptileIvstgYn); // 양서파충류조사유무
								pw.write("|^");
								pw.write(insectIvstgYn); // 곤충류조사유무
								pw.write("|^");
								pw.write(fishesIvstgYn); // 어류조사유무
								pw.write("|^");
								pw.write(benthosIvstgYn); // 저서생물(동물)유무
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_30.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
					}

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
