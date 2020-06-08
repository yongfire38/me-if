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

import common.JsonParser;
//import common.TransSftp;

public class GetAmpltNm {

	// 해양환경정보조회 서비스 - 동식물명속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("maritime_getampltNm_url");
					String service_key = JsonParser.getProperty("maritime_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_16.dat");
					
					//String json = "";

					//json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답, mgtNo :" + mgtNo);
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
									String mediolittoralKornm = " "; // 조간대 저서동물
																		// 출현종
																		// 국문명
									String mediolittoralScncenm = " "; // 조간대
																		// 저서동물
																		// 출현종
																		// 영문명
									String infralittoralKornm = " "; // 조하대 저서동물
																		// 우점종
																		// 국문명
									String infralittoralScncenm = " "; // 조하대
																		// 저서동물
																		// 우점종
																		// 영문명
									String seawidsKorname = " "; // 해조류 출현종 국문명
									String seawidsScncenm = " "; // 해조류 출현종 영문명

									while (iter.hasNext()) {

										String keyname = iter.next();

										if (keyname.equals("id")) {
											id = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("mediolittoralKornm")) {
											mediolittoralKornm = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("mediolittoralScncenm")) {
											mediolittoralScncenm = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("infralittoralKornm")) {
											infralittoralKornm = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("infralittoralScncenm")) {
											infralittoralScncenm = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("seawidsKorname")) {
											seawidsKorname = ids_Json.get(keyname).toString();
										}
										if (keyname.equals("seawidsScncenm")) {
											seawidsScncenm = ids_Json.get(keyname).toString();
										}
									}
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(mgtNo); // 사업 코드
										pw.write("|^");
										pw.write(ivstgSpotNm_str); // 조사지점명
										pw.write("|^");
										pw.write(ivstgOdr_str); // 조사차수
										pw.write("|^");
										pw.write(id); // 아이디
										pw.write("|^");
										pw.write(mediolittoralKornm.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조간대
																		// 저서동물
																		// 출현종
																		// 국문명
										pw.write("|^");
										pw.write(mediolittoralScncenm.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조간대
																		// 저서동물
																		// 출현종
																		// 영문명
										pw.write("|^");
										pw.write(infralittoralKornm.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조하대
																		// 저서동물
																		// 우점종
																		// 국문명
										pw.write("|^");
										pw.write(infralittoralScncenm.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조하대
																		// 저서동물
																		// 우점종
																		// 영문명
										pw.write("|^");
										pw.write(seawidsKorname.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 해조류 출현종 국문명
										pw.write("|^");
										pw.write(seawidsScncenm.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 해조류 출현종 영문명
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_16.dat", "EIA");

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
