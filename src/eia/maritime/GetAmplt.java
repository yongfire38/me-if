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
//import common.TransSftp;

public class GetAmplt {

	// 해양환경정보조회 서비스 - 동식물속성 조회
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
					String service_url = JsonParser.getProperty("maritime_getamplt_url");
					String service_key = JsonParser.getProperty("maritime_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_15.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("ivstgSpotNm"); // 조사지점명
							pw.write("|^");
							pw.write("ivstgOdr"); // 조사차수
							pw.write("|^");
							pw.write("ivstgBgnde"); // 조사시작일
							pw.write("|^");
							pw.write("ivstgEndde"); // 조사종료일
							pw.write("|^");
							pw.write("mediolittoralKnd"); // 조간대 저서동물 출현종수
							pw.write("|^");
							pw.write("mediolittoralDn"); // 조간대 저서동물 서식밀도
							pw.write("|^");
							pw.write("mediolittoralBiomass"); // 조간대 저서동물 생체량
							pw.write("|^");
							pw.write("infralittoralKnd"); // 조하대 저서동물 출현종수
							pw.write("|^");
							pw.write("infralittoralDn"); // 조하대 저서동물 서식밀도
							pw.write("|^");
							pw.write("infralittoralBiomass"); // 조하대 저서동물 생체량
							pw.write("|^");
							pw.write("seawidsKnd"); // 해조류 출현종수
							pw.write("|^");
							pw.write("ascidiansYn"); // 해초류(잘피) 서식여부(사업지 주변)
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

								JSONObject odr = (JSONObject) odrs.get(f);

								Set<String> key = odr.keySet();

								Iterator<String> iter = key.iterator();

								String ivstgOdr = " "; // 조사차수
								String ivstgBgnde = " "; // 조사시작일
								String ivstgEndde = " "; // 조사종료일
								String mediolittoralKnd = " "; // 조간대 저서동물 출현종수
								String mediolittoralDn = " "; // 조간대 저서동물 서식밀도
								String mediolittoralBiomass = " "; // 조간대 저서동물
																	// 생체량
								String infralittoralKnd = " "; // 조하대 저서동물 출현종수
								String infralittoralDn = " "; // 조하대 저서동물 서식밀도
								String infralittoralBiomass = " "; // 조하대 저서동물
																	// 생체량
								String seawidsKnd = " "; // 해조류 출현종수
								String ascidiansYn = " "; // 해초류(잘피) 서식여부(사업지
															// 주변)

								while (iter.hasNext()) {

									String keyname = iter.next();

									if (keyname.equals("ivstgOdr")) {
										ivstgOdr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("ivstgBgnde")) {
										ivstgBgnde = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("ivstgEndde")) {
										ivstgEndde = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("mediolittoralKnd")) {
										mediolittoralKnd = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("mediolittoralDn")) {
										mediolittoralDn = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("mediolittoralBiomass")) {
										mediolittoralBiomass = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("infralittoralKnd")) {
										infralittoralKnd = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("infralittoralDn")) {
										infralittoralDn = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("infralittoralBiomass")) {
										infralittoralBiomass = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("seawidsKnd")) {
										seawidsKnd = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("ascidiansYn")) {
										ascidiansYn = odr.get(keyname).toString().trim();
									}

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstgOdr); // 조사차수
									pw.write("|^");
									pw.write(ivstgBgnde); // 조사시작일
									pw.write("|^");
									pw.write(ivstgEndde); // 조사종료일
									pw.write("|^");
									pw.write(mediolittoralKnd); // 조간대 저서동물 출현종수
									pw.write("|^");
									pw.write(mediolittoralDn); // 조간대 저서동물 서식밀도
									pw.write("|^");
									pw.write(mediolittoralBiomass); // 조간대 저서동물
																	// 생체량
									pw.write("|^");
									pw.write(infralittoralKnd); // 조하대 저서동물 출현종수
									pw.write("|^");
									pw.write(infralittoralDn); // 조하대 저서동물 서식밀도
									pw.write("|^");
									pw.write(infralittoralBiomass); // 조하대 저서동물
																	// 생체량
									pw.write("|^");
									pw.write(seawidsKnd); // 해조류 출현종수
									pw.write("|^");
									pw.write(ascidiansYn); // 해초류(잘피) 서식여부(사업지
															// 주변)
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_15.dat", "EIA");

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
