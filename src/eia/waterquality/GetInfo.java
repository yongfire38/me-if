package eia.waterquality;

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

public class GetInfo {

	// 수질정보 서비스 - 개요 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("waterquality_getInfo_url");
					String service_key = JsonParser.getProperty("waterquality_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_08.dat");

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

						String bfeBodPlulod = " "; // 사업시행전 BOD 배출부하량
						String bfeTpPlulod = " "; // 사업시행전 T-P 배출부하량
						String aftBodPlulod = " "; // 사업시행후 BOD 배출부하량
						String aftTpPlulod = " "; // 사업시행후 T-P 배출부하량
						String wtrReuseYn = " "; // 물재이용여부
						String ptfctCntcYn = " "; // 공공처리시설 연계 처리 여부
						String itfctBodDnsty = " "; // 개별처리시설의 오염원 배출농도(BOD)
						String itfctBodQy = " "; // 개별처리시설의 오염원 배출량(BOD)
						String itfctCodDnsty = " "; // 개별처리시설의 오염원 배출농도(COD)
						String itfctCodQy = " "; // 별처리시설의 오염원 배출량(COD)
						String itfctSsDnsty = " "; // 개별처리시설의 오염원 배출농도(SS)
						String itfctSsQy = " "; // 개별처리시설의 오염원 배출량(SS)
						String itfctTnDnsty = " "; // 개별처리시설의 오염원 배출농도(T-N)
						String itfctTnQy = " "; // 개별처리시설의 오염원 배출량(T-N)
						String itfctTpDnsty = " "; // 개별처리시설의 오염원 배출농도(T-P)
						String itfctTpQy = " "; // 개별처리시설의 오염원 배출량(T-P)
						String jrjYn = " "; // 저류지 유무
						String jjrjYn = " "; // 지하저류조 유무
						String igsjYn = " "; // 인공습지 유무
						String ygpjYn = " "; // 유공포장(투수성포장) 유무
						String ctjrjYn = " "; // 침투저류지 유무
						String ctdrYn = " "; // 침투도랑 유무
						String ssygdYn = " "; // 식생여과대 유무
						String sssrYn = " "; // 식생수로 유무
						String sscrjYn = " "; // 식생체류지 유무
						String smjbhbYn = " "; // 식물재배화분 유무
						String nmygsjYn = " "; // 나무여과상자 유무
						String yghssYn = " "; // 여과형시설 유무
						String yrhssYn = " "; // 와류형시설 유무
						String scrhssYn = " "; // 스크린형 시설 유무
						String ajcjcrhssYn = " "; // 응집 침전 처리형 시설 유무
						String ugrwtrQy = " "; // 지하수함양량
						String ugrwtrDevlopqy = " "; // 지하수개발가능량
						String ugrwtrWpqy = " "; // 지하수 적정채수량(양수량)
						String ugrwtrAffcra = " "; // 영향반경
						String ugrwtrAnalsYn = " "; // 지하수모델링유무
						String ugrwtrRm = " "; // 지하수 비고

						while (iter.hasNext()) {

							String keyname = iter.next();

							if (keyname.equals("bfeBodPlulod")) {
								bfeBodPlulod = body.get(keyname).toString().trim();
							}
							if (keyname.equals("bfeTpPlulod")) {
								bfeTpPlulod = body.get(keyname).toString().trim();
							}
							if (keyname.equals("aftBodPlulod")) {
								aftBodPlulod = body.get(keyname).toString().trim();
							}
							if (keyname.equals("aftTpPlulod")) {
								aftTpPlulod = body.get(keyname).toString().trim();
							}
							if (keyname.equals("wtrReuseYn")) {
								wtrReuseYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ptfctCntcYn")) {
								ptfctCntcYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctBodDnsty")) {
								itfctBodDnsty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctBodQy")) {
								itfctBodQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctCodDnsty")) {
								itfctCodDnsty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctCodQy")) {
								itfctCodQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctSsDnsty")) {
								itfctSsDnsty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctSsQy")) {
								itfctSsQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctTnDnsty")) {
								itfctTnDnsty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctTnQy")) {
								itfctTnQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctTpDnsty")) {
								itfctTpDnsty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("itfctTpQy")) {
								itfctTpQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("jrjYn")) {
								jrjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("jjrjYn")) {
								jjrjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("igsjYn")) {
								igsjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ygpjYn")) {
								ygpjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ctjrjYn")) {
								ctjrjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ctdrYn")) {
								ctdrYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ssygdYn")) {
								ssygdYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("sssrYn")) {
								sssrYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("sscrjYn")) {
								sscrjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("smjbhbYn")) {
								smjbhbYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("nmygsjYn")) {
								nmygsjYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("yghssYn")) {
								yghssYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("yrhssYn")) {
								yrhssYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("scrhssYn")) {
								scrhssYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ajcjcrhssYn")) {
								ajcjcrhssYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrQy")) {
								ugrwtrQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrDevlopqy")) {
								ugrwtrDevlopqy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrWpqy")) {
								ugrwtrWpqy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrAffcra")) {
								ugrwtrAffcra = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrAnalsYn")) {
								ugrwtrAnalsYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ugrwtrRm")) {
								ugrwtrRm = body.get(keyname).toString().trim();
							}
						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(bfeBodPlulod); // 사업시행전 BOD 배출부하량
							pw.write("|^");
							pw.write(bfeTpPlulod); // 사업시행전 T-P 배출부하량
							pw.write("|^");
							pw.write(aftBodPlulod); // 사업시행후 BOD 배출부하량
							pw.write("|^");
							pw.write(aftTpPlulod); // 사업시행후 T-P 배출부하량
							pw.write("|^");
							pw.write(wtrReuseYn); // 물재이용여부
							pw.write("|^");
							pw.write(ptfctCntcYn); // 공공처리시설 연계 처리 여부
							pw.write("|^");
							pw.write(itfctBodDnsty); // 개별처리시설의 오염원 배출농도(BOD)
							pw.write("|^");
							pw.write(itfctBodQy); // 개별처리시설의 오염원 배출량(BOD)
							pw.write("|^");
							pw.write(itfctCodDnsty); // 개별처리시설의 오염원 배출농도(COD)
							pw.write("|^");
							pw.write(itfctCodQy); // 개별처리시설의 오염원 배출량(COD)
							pw.write("|^");
							pw.write(itfctSsDnsty); // 개별처리시설의 오염원 배출농도(SS)
							pw.write("|^");
							pw.write(itfctSsQy); // 개별처리시설의 오염원 배출량(SS)
							pw.write("|^");
							pw.write(itfctTnDnsty); // 개별처리시설의 오염원 배출농도(T-N)
							pw.write("|^");
							pw.write(itfctTnQy); // 개별처리시설의 오염원 배출량(T-N)
							pw.write("|^");
							pw.write(itfctTpDnsty); // 개별처리시설의 오염원 배출농도(T-P)
							pw.write("|^");
							pw.write(itfctTpQy); // 개별처리시설의 오염원 배출량(T-P)
							pw.write("|^");
							pw.write(jrjYn); // 저류지 유무
							pw.write("|^");
							pw.write(jjrjYn); // 지하저류조 유무
							pw.write("|^");
							pw.write(igsjYn); // 인공습지 유무
							pw.write("|^");
							pw.write(ygpjYn); // 유공포장(투수성포장) 유무
							pw.write("|^");
							pw.write(ctjrjYn); // 침투저류지 유무
							pw.write("|^");
							pw.write(ctdrYn); // 침투도랑 유무
							pw.write("|^");
							pw.write(ssygdYn); // 식생여과대 유무
							pw.write("|^");
							pw.write(sssrYn); // 식생수로 유무
							pw.write("|^");
							pw.write(sscrjYn); // 식생체류지 유무
							pw.write("|^");
							pw.write(smjbhbYn); // 식물재배화분 유무
							pw.write("|^");
							pw.write(nmygsjYn); // 나무여과상자 유무
							pw.write("|^");
							pw.write(yghssYn); // 여과형시설 유무
							pw.write("|^");
							pw.write(yrhssYn); // 와류형시설 유무
							pw.write("|^");
							pw.write(scrhssYn); // 스크린형 시설 유무
							pw.write("|^");
							pw.write(ajcjcrhssYn); // 응집 침전 처리형 시설 유무
							pw.write("|^");
							pw.write(ugrwtrQy); // 지하수함양량
							pw.write("|^");
							pw.write(ugrwtrDevlopqy); // 지하수개발가능량
							pw.write("|^");
							pw.write(ugrwtrWpqy); // 지하수 적정채수량(양수량)
							pw.write("|^");
							pw.write(ugrwtrAffcra); // 영향반경
							pw.write("|^");
							pw.write(ugrwtrAnalsYn); // 지하수모델링유무
							pw.write("|^");
							pw.write(ugrwtrRm); // 지하수 비고
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_08.dat", "EIA");

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


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}



	}

}
