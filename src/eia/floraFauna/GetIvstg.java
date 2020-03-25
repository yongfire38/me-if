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


public class GetIvstg {

	

	// 동식물상 정보 조회 - 조사속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("florafauna_getIvstg_url");
			String service_key = JsonParser.getProperty("florafauna_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_32.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("mgtNo"); // 사업 코드
					pw.write("|^");
					pw.write("ivstgSpotNm"); // 조사지점명
					pw.write("|^");
					pw.write("ivstgCl"); // 조사분류
					pw.write("|^");
					pw.write("adres"); // 주소
					pw.write("|^");
					pw.write("xcnts"); // X좌표
					pw.write("|^");
					pw.write("ydnts"); // Y좌표
					pw.write("|^");
					pw.write("ivstgOdr"); // 조사차수
					pw.write("|^");
					pw.write("id"); // 아이디
					pw.write("|^");
					pw.write("scnceNm"); // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물) 학명 목록
					pw.write("|^");
					pw.write("korNm"); // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물) 국명 목록
					pw.write("|^");
					pw.write("co"); // 개수
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
				String resultMsg = header.get("resultMsg").toString().trim();

				if (resultCode.equals("00")) {

					JSONArray ivstgs = (JSONArray) body.get("ivstgs");

					for (int i = 0; i < ivstgs.size(); i++) {

						JSONObject ivstg = (JSONObject) ivstgs.get(i);

						String ivstg_ivstgSpotNm_str = " "; // 조사지점명
						String ivstg_adres_str = " "; // 주소
						String ivstg_xcnts_str = " "; // X좌표
						String ivstg_ydnts_str = " "; // Y좌표

						if (ivstg.get("ivstgSpotNm") != null) {
							ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
						} else {
							ivstg_ivstgSpotNm_str = " ";
						}

						if (ivstg.get("adres") != null) {
							ivstg_adres_str = ivstg.get("adres").toString().trim();
						} else {
							ivstg_adres_str = " ";
						}

						if (ivstg.get("xcnts") != null) {
							ivstg_xcnts_str = ivstg.get("xcnts").toString().trim();
						} else {
							ivstg_xcnts_str = " ";
						}

						if (ivstg.get("ydnts") != null) {
							ivstg_ydnts_str = ivstg.get("ydnts").toString().trim();
						} else {
							ivstg_ydnts_str = " ";
						}

						JSONArray odrs = (JSONArray) ivstg.get("odrs");

						for (int f = 0; f < odrs.size(); f++) {

							JSONObject odr = (JSONObject) odrs.get(f);

							String ivstgOdr_str = " "; // 조사차수

							if (odr.get("ivstgOdr") != null) {
								ivstgOdr_str = odr.get("ivstgOdr").toString().trim();
							} else {
								ivstgOdr_str = " ";
							}

							JSONArray ids = (JSONArray) odr.get("ids");

							for (int r = 0; r < ids.size(); r++) {

								JSONObject id_json = (JSONObject) ids.get(r);

								Set<String> key = id_json.keySet();

								Iterator<String> iter = key.iterator();

								String id = " "; // 아이디
								String ivstgCl = " ";// 조사분류
								String scnceNm = " "; // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물)
														// 학명 목록
								String korNm = " "; // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물)
													// 국명 목록
								String co = " "; // 개수

								while (iter.hasNext()) {
									String keyname = iter.next();

									if (keyname.equals("id")) {
										id = id_json.get(keyname).toString().trim();
									}
									if (keyname.equals("ivgtgCl")) {
										ivstgCl = id_json.get(keyname).toString().trim();
									}
									if (keyname.equals("scnceNm")) {
										scnceNm = id_json.get(keyname).toString().trim();
									}
									if (keyname.equals("korNm")) {
										korNm = id_json.get(keyname).toString().trim();
									}
									if (keyname.equals("co")) {
										co = id_json.get(keyname).toString().trim();
									}
								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstgCl); // 조사분류
									pw.write("|^");
									pw.write(ivstg_adres_str); // 주소
									pw.write("|^");
									pw.write(ivstg_xcnts_str); // X좌표
									pw.write("|^");
									pw.write(ivstg_ydnts_str); // Y좌표
									pw.write("|^");
									pw.write(ivstgOdr_str); // 조사차수
									pw.write("|^");
									pw.write(id); // 아이디
									pw.write("|^");
									pw.write(scnceNm); // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물)
														// 학명 목록
									pw.write("|^");
									pw.write(korNm); // 식물상/포유류/조류/양서파충류/곤충류/어류/저서생물(동물)
														// 국명 목록
									pw.write("|^");
									pw.write(co); // 개수
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

							}

						}

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_32.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
					
				} else if (resultCode.equals("03")) {
					System.out.println("data not exist!! mgtNo :" + mgtNo);
				} else {
					System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg + "::mgtNo::" + mgtNo);
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
