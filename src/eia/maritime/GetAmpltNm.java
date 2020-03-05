package eia.maritime;

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
import common.TransSftp;

public class GetAmpltNm {

	

	// 해양환경정보조회 서비스 - 동식물명속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("maritime_getampltNm_url");
			String service_key = JsonParser.getProperty("maritime_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_16_" + mgtNo + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("ivstgSpotNm"); // 조사지점명
				pw.write("|^");
				pw.write("ivstgOdr"); // 조사차수
				pw.write("|^");
				pw.write("id"); // 아이디
				pw.write("|^");
				pw.write("mediolittoralKornm"); // 조간대 저서동물 출현종 국문명
				pw.write("|^");
				pw.write("mediolittoralScncenm"); // 조간대 저서동물 출현종 영문명
				pw.write("|^");
				pw.write("infralittoralKornm"); // 조하대 저서동물 우점종 국문명
				pw.write("|^");
				pw.write("infralittoralScncenm"); // 조하대 저서동물 우점종 영문명
				pw.write("|^");
				pw.write("seawidsKorname"); // 해조류 출현종 국문명
				pw.write("|^");
				pw.write("seawidsScncenm"); // 해조류 출현종 영문명
				pw.println();
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
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

					JSONArray ivstgs = (JSONArray) body.get("ivstgs");

					for (int i = 0; i < ivstgs.size(); i++) {

						JSONObject ivstgs_Json = (JSONObject) ivstgs.get(i);

						String ivstgSpotNm_str = " "; // 조사지점명

						if (ivstgs_Json.get("ivstgSpotNm") != null) {
							ivstgSpotNm_str = ivstgs_Json.get("ivstgSpotNm").toString().trim();
						} else {
							ivstgSpotNm_str = " ";
						}

						JSONArray odrs = (JSONArray) ivstgs_Json.get("odrs");

						for (int f = 0; f < odrs.size(); f++) {

							JSONObject odrs_Json = (JSONObject) odrs.get(f);

							String ivstgOdr_str = " "; // 조사차수

							if (odrs_Json.get("ivstgOdr") != null) {
								ivstgOdr_str = odrs_Json.get("ivstgOdr").toString().trim();
							} else {
								ivstgOdr_str = " ";
							}

							JSONArray ids = (JSONArray) odrs_Json.get("ids");

							for (int r = 0; r < ids.size(); r++) {

								JSONObject ids_Json = (JSONObject) ids.get(r);

								Set<String> key = ids_Json.keySet();

								Iterator<String> iter = key.iterator();

								String id = " "; // 아이디
								String mediolittoralKornm = " "; // 조간대 저서동물 출현종
																// 국문명
								String mediolittoralScncenm = " "; // 조간대 저서동물
																	// 출현종 영문명
								String infralittoralKornm = " "; // 조하대 저서동물 우점종
																// 국문명
								String infralittoralScncenm = " "; // 조하대 저서동물
																	// 우점종 영문명
								String seawidsKorname = " "; // 해조류 출현종 국문명
								String seawidsScncenm = " "; // 해조류 출현종 영문명

								while (iter.hasNext()) {

									String keyname = iter.next();

									if (keyname.equals("id")) {
										id = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("mediolittoralKornm")) {
										mediolittoralKornm = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("mediolittoralScncenm")) {
										mediolittoralScncenm = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("infralittoralKornm")) {
										infralittoralKornm = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("infralittoralScncenm")) {
										infralittoralScncenm = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("seawidsKorname")) {
										seawidsKorname = ids_Json.get(keyname).toString().trim();
									}
									if (keyname.equals("seawidsScncenm")) {
										seawidsScncenm = ids_Json.get(keyname).toString().trim();
									}
								}
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstgOdr_str); // 조사차수
									pw.write("|^");
									pw.write(id); // 아이디
									pw.write("|^");
									pw.write(mediolittoralKornm); // 조간대
																	// 저서동물
																	// 출현종
																	// 국문명
									pw.write("|^");
									pw.write(mediolittoralScncenm); // 조간대
																	// 저서동물
																	// 출현종
																	// 영문명
									pw.write("|^");
									pw.write(infralittoralKornm); // 조하대
																	// 저서동물
																	// 우점종
																	// 국문명
									pw.write("|^");
									pw.write(infralittoralScncenm); // 조하대
																	// 저서동물
																	// 우점종
																	// 영문명
									pw.write("|^");
									pw.write(seawidsKorname); // 해조류 출현종 국문명
									pw.write("|^");
									pw.write(seawidsScncenm); // 해조류 출현종 영문명
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

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_16_" + mgtNo + ".dat", "EIA");

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
