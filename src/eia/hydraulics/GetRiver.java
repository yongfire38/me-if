package eia.hydraulics;

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

public class GetRiver {

	// 수리수문정보 서비스 - 하천사업 속성 조회
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
					String service_url = JsonParser.getProperty("hydraulics_getRiver_url");
					String service_key = JsonParser.getProperty("hydraulics_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_12.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("id"); // 아이디
							pw.write("|^");
							pw.write("dtlclfc"); // 사업세분류
							pw.write("|^");
							pw.write("lc"); // 사업위치
							pw.write("|^");
							pw.write("totalEt"); // 총연장
							pw.write("|^");
							pw.write("dstrcNm"); // 지구구분
							pw.write("|^");
							pw.write("gubun1"); // 사업구분1 (사업내용구분)
							pw.write("|^");
							pw.write("gubun2"); // 사업구분2 (사업내용구분)
							pw.write("|^");
							pw.write("gubun3"); // 사업구분3 (사업내용구분)
							pw.write("|^");
							pw.write("co"); // 개수
							pw.write("|^");
							pw.write("et"); // 연장
							pw.write("|^");
							pw.write("ar"); // 면적
							pw.write("|^");
							pw.write("vl"); // 부피
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

						JSONArray ids = (JSONArray) body.get("ids");

						// id 배열 안
						for (int i = 0; i < ids.size(); i++) {

							JSONObject id_Json = (JSONObject) ids.get(i);

							Set<String> key = id_Json.keySet();

							Iterator<String> iter = key.iterator();

							String id = " "; // 아이디
							String dtlclfc = " "; // 사업세분류
							String lc = " "; // 사업위치
							String totalEt = " "; // 총연장
							String dstrcNm = " "; // 지구구분
							String gubun1 = " "; // 사업구분1 (사업내용구분)
							String gubun2 = " "; // 사업구분2 (사업내용구분)
							String gubun3 = " "; // 사업구분3 (사업내용구분)
							String co = " "; // 개수
							String et = " "; // 연장
							String ar = " "; // 면적
							String vl = " "; // 부피

							while (iter.hasNext()) {

								String keyname = iter.next();

								if (keyname.equals("id")) {
									id = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("dtlclfc")) {
									dtlclfc = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("lc")) {
									lc = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("totalEt")) {
									totalEt = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("dstrcNm")) {
									dstrcNm = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("gubun1")) {
									gubun1 = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("gubun2")) {
									gubun2 = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("gubun3")) {
									gubun3 = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("co")) {
									co = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("et")) {
									et = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("ar")) {
									ar = id_Json.get(keyname).toString().trim();
								}
								if (keyname.equals("vl")) {
									vl = id_Json.get(keyname).toString().trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(mgtNo); // 사업 코드
								pw.write("|^");
								pw.write(id); // 아이디
								pw.write("|^");
								pw.write(dtlclfc); // 사업세분류
								pw.write("|^");
								pw.write(lc); // 사업위치
								pw.write("|^");
								pw.write(totalEt); // 총연장
								pw.write("|^");
								pw.write(dstrcNm); // 지구구분
								pw.write("|^");
								pw.write(gubun1); // 사업구분1 (사업내용구분)
								pw.write("|^");
								pw.write(gubun2); // 사업구분2 (사업내용구분)
								pw.write("|^");
								pw.write(gubun3); // 사업구분3 (사업내용구분)
								pw.write("|^");
								pw.write(co); // 개수
								pw.write("|^");
								pw.write(et); // 연장
								pw.write("|^");
								pw.write(ar); // 면적
								pw.write("|^");
								pw.write(vl); // 부피
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_12.dat", "EIA");

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
