package eia.airquality;

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

public class GetIvstg {

	// 대기질 정보 조회 - 조사속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("airquality_getIvstg_url");
			String service_key = JsonParser.getProperty("airquality_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "TIF_EIA_02.dat");

			if (file.exists()) {

				System.out.println("파일이 이미 존재하므로 이어쓰기..");

			} else {

				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("mgtNo"); // 사업 코드
					pw.write("|^");
					pw.write("ivstgGb"); // 조사구분
					pw.write("|^");
					pw.write("ivstgSpotNm"); // 조사지점명
					pw.write("|^");
					pw.write("adres"); // 주소
					pw.write("|^");
					pw.write("xcnts"); // X좌표
					pw.write("|^");
					pw.write("ydnts"); // Y좌표
					pw.write("|^");
					pw.write("ivstgOdr"); // 조사차수
					pw.write("|^");
					pw.write("ivstgBgnde"); // 조사시작일
					pw.write("|^");
					pw.write("ivstgEndde"); // 조사종료일
					pw.write("|^");
					pw.write("pm10Val"); // 미세먼지(10)
					pw.write("|^");
					pw.write("pm25Val"); // 미세먼지(2.5)
					pw.write("|^");
					pw.write("no2Val"); // 이산화질소
					pw.write("|^");
					pw.write("so2Val"); // 아황산가스
					pw.write("|^");
					pw.write("coVal"); // 일산화탄소
					pw.write("|^");
					pw.write("o3Val"); // 오존
					pw.write("|^");
					pw.write("pbVal"); // 납
					pw.write("|^");
					pw.write("bzVal"); // 벤젠
					pw.write("|^");
					pw.write("hclVal"); // 염화수소(HCL)
					pw.write("|^");
					pw.write("hgVal"); // 수은(Hg)
					pw.write("|^");
					pw.write("niVal"); // 니켈(Ni)
					pw.write("|^");
					pw.write("cr6Val"); // 6가크롬(Cr6+)
					pw.write("|^");
					pw.write("cdVal"); // 카드뮴(Cd)
					pw.write("|^");
					pw.write("asVal"); // 비소(As)
					pw.write("|^");
					pw.write("hchoVal"); // 포름알데히드
					pw.write("|^");
					pw.write("vcVal"); // 염화비닐
					pw.write("|^");
					pw.write("dioxinVal"); // 다이옥신
					pw.write("|^");
					pw.write("beVal"); // 베릴륨(Be)
					pw.write("|^");
					pw.write("ebVal"); // 에틸벤젠
					pw.write("|^");
					pw.write("c6h14Val"); // n-헥산
					pw.write("|^");
					pw.write("c6h12Val"); // 시클로헥산
					pw.write("|^");
					pw.write("deVal"); // 1,-2디클로로에탄
					pw.write("|^");
					pw.write("cfVal"); // 클로로포름
					pw.write("|^");
					pw.write("tceVal"); // 트리클로로에틸렌
					pw.write("|^");
					pw.write("ctVal"); // 사염화탄소
					pw.write("|^");
					pw.write("hcnVal"); // 시안화수소
					pw.write("|^");
					pw.write("gmenoPm1024hr"); // 공사시 PM-10_24시간평균
					pw.write("|^");
					pw.write("gmenoPm10Year"); // 공사시 PM-10_연평균
					pw.write("|^");
					pw.write("gmenoPm2524hr"); // 공사시 PM-2.5_24시간평균
					pw.write("|^");
					pw.write("gmenoPm25Year"); // 공사시 PM-2.5_연평균
					pw.write("|^");
					pw.write("gmenoNo21hr"); // 공사시 NO2_1시간평균
					pw.write("|^");
					pw.write("gmenoNo224hr"); // 공사시 NO2_24시간평균
					pw.write("|^");
					pw.write("gmenoNo2Year"); // 공사시 NO2_연평균
					pw.write("|^");
					pw.write("umenoPm1024hr"); // 운영시 PM-10_24시간평균
					pw.write("|^");
					pw.write("umenoPm10Year"); // 운영시 PM-10_연평균
					pw.write("|^");
					pw.write("umenoPm2524hr"); // 운영시 PM-2.5_24시간평균
					pw.write("|^");
					pw.write("umenoPm25Year"); // 운영시 PM-2.5_연평균
					pw.write("|^");
					pw.write("umenoSo21hr"); // 운영시 SO2_1시간평균
					pw.write("|^");
					pw.write("umenoSo224hr"); // 운영시 SO2_24시간평균
					pw.write("|^");
					pw.write("umenoSo2Year"); // 운영시 SO2_연평균
					pw.write("|^");
					pw.write("umenoNo21hr"); // 운영시 NO2_1시간평균
					pw.write("|^");
					pw.write("umenoNo224hr"); // 운영시 NO2_24시간평균
					pw.write("|^");
					pw.write("umenoNo2Year"); // 운영시 NO2_연평균
					pw.write("|^");
					pw.write("umenoCo1hr"); // 운영시 CO_1시간평균
					pw.write("|^");
					pw.write("umenoCo8hr"); // 운영시 CO_8시간평균
					pw.write("|^");
					pw.write("envrnGrad"); // 환경기준등급
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

				if (resultCode.equals("00")) {

					JSONArray ivstgGbs = (JSONArray) body.get("ivstgGbs");

					// 공통으로 갖는 사업 코드
					for (int i = 0; i < ivstgGbs.size(); i++) {

						JSONObject ivstgGb_Json = (JSONObject) ivstgGbs.get(i);

						String ivstgGb_str = ivstgGb_Json.get("ivstgGb").toString().trim(); // 조사구분

						JSONArray ivstgs = (JSONArray) ivstgGb_Json.get("ivstgs");

						for (int r = 0; r < ivstgs.size(); r++) {

							JSONObject ivstg = (JSONObject) ivstgs.get(r);

							String ivstg_adres_str = " "; // 주소
							String ivstg_ivstgSpotNm_str = " "; // 조사지점명
							String ivstg_xcnts_str = " "; // X좌표
							String ivstg_ydnts_str = " "; // Y좌표

							if (ivstg.get("adres") != null) {
								ivstg_adres_str = ivstg.get("adres").toString().trim();
							} else {
								ivstg_adres_str = " ";
							}

							if (ivstg.get("ivstgSpotNm") != null) {
								ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
							} else {
								ivstg_ivstgSpotNm_str = " ";
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

								Set<String> key = odr.keySet();

								Iterator<String> iter = key.iterator();

								// 3개는 공통적인 바깥 배열의 값이므로 Iterator로 받아 올 수 없다..
								// String ivstgGb = " ";
								// String ivstgSpotNm = " ";
								// String adres = " ";
								String ivstgOdr = " "; // 조사차수
								String ivstgBgnde = " "; // 조사시작일
								String ivstgEndde = " "; // 조사종료일
								String pm10Val = " "; // 미세먼지(10)
								String pm25Val = " "; // 미세먼지(2.5)
								String no2Val = " "; // 이산화질소
								String so2Val = " "; // 아황산가스
								String coVal = " "; // 일산화탄소
								String o3Val = " "; // 오존
								String pbVal = " "; // 납
								String bzVal = " "; // 벤젠
								String hclVal = " "; // 염화수소(HCL)
								String hgVal = " "; // 수은(Hg)
								String niVal = " "; // 니켈(Ni)
								String cr6Val = " "; // 6가크롬(Cr6+)
								String cdVal = " "; // 카드뮴(Cd)
								String asVal = " "; // 비소(As)
								String hchoVal = " "; // 포름알데히드
								String vcVal = " "; // 염화비닐
								String dioxinVal = " "; // 다이옥신
								String beVal = " "; // 베릴륨(Be)
								String ebVal = " "; // 에틸벤젠
								String c6h14Val = " "; // n-헥산
								String c6h12Val = " "; // 시클로헥산
								String deVal = " "; // 1,-2디클로로에탄
								String cfVal = " "; // 클로로포름
								String tceVal = " "; // 트리클로로에틸렌
								String ctVal = " "; // 사염화탄소
								String hcnVal = " "; // 시안화수소
								String gmenoPm1024hr = " "; // 공사시 PM-10_24시간평균
								String gmenoPm10Year = " "; // 공사시 PM-10_연평균
								String gmenoPm2524hr = " "; // 공사시 PM-2.5_24시간평균
								String gmenoPm25Year = " "; // 공사시 PM-2.5_연평균
								String gmenoNo21hr = " "; // 공사시 NO2_1시간평균
								String gmenoNo224hr = " "; // 공사시 NO2_24시간평균
								String gmenoNo2Year = " "; // 공사시 NO2_연평균
								String umenoPm1024hr = " "; // 운영시 PM-10_24시간평균
								String umenoPm10Year = " "; // 운영시 PM-10_연평균
								String umenoPm2524hr = " "; // 운영시 PM-2.5_24시간평균
								String umenoPm25Year = " "; // 운영시 PM-2.5_연평균
								String umenoSo21hr = " "; // 운영시 SO2_1시간평균
								String umenoSo224hr = " "; // 운영시 SO2_24시간평균
								String umenoSo2Year = " "; // 운영시 SO2_연평균
								String umenoNo21hr = " "; // 운영시 NO2_1시간평균
								String umenoNo224hr = " "; // 운영시 NO2_24시간평균
								String umenoNo2Year = " "; // 운영시 NO2_연평균
								String umenoCo1hr = " "; // 운영시 CO_1시간평균
								String umenoCo8hr = " "; // 운영시 CO_8시간평균
								String envrnGrad = " "; // 환경기준등급

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
									if (keyname.equals("pm10Val")) {
										pm10Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("pm25Val")) {
										pm25Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("no2Val")) {
										no2Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("so2Val")) {
										so2Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("coVal")) {
										coVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("o3Val")) {
										o3Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("pbVal")) {
										pbVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("bzVal")) {
										bzVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("hclVal")) {
										hclVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("hgVal")) {
										hgVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("niVal")) {
										niVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("cr6Val")) {
										cr6Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("cdVal")) {
										cdVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("asVal")) {
										asVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("hchoVal")) {
										hchoVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("vcVal")) {
										vcVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("dioxinVal")) {
										dioxinVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("beVal")) {
										beVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("ebVal")) {
										ebVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("c6h14Val")) {
										c6h14Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("c6h12Val")) {
										c6h12Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("deVal")) {
										deVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("cfVal")) {
										cfVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("tceVal")) {
										tceVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("ctVal")) {
										ctVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("hcnVal")) {
										hcnVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoPm1024hr")) {
										gmenoPm1024hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoPm10Year")) {
										gmenoPm10Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoPm2524hr")) {
										gmenoPm2524hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoPm25Year")) {
										gmenoPm25Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoNo21hr")) {
										gmenoNo21hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoNo224hr")) {
										gmenoNo224hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("gmenoNo2Year")) {
										gmenoNo2Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoPm1024hr")) {
										umenoPm1024hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoPm10Year")) {
										umenoPm10Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoPm2524hr")) {
										umenoPm2524hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoPm25Year")) {
										umenoPm25Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoSo21hr")) {
										umenoSo21hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoSo224hr")) {
										umenoSo224hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoSo2Year")) {
										umenoSo2Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoNo21hr")) {
										umenoNo21hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoNo224hr")) {
										umenoNo224hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoNo2Year")) {
										umenoNo2Year = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoCo1hr")) {
										umenoCo1hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("umenoCo8hr")) {
										umenoCo8hr = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("envrnGrad")) {
										envrnGrad = odr.get(keyname).toString().trim();
									}
								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업코드
									pw.write("|^");
									pw.write(ivstgGb_str); // 조사구분
									pw.write("|^");
									pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstg_adres_str); // 주소
									pw.write("|^");
									pw.write(ivstg_xcnts_str); // X좌표
									pw.write("|^");
									pw.write(ivstg_ydnts_str); // Y좌표
									pw.write("|^");
									pw.write(ivstgOdr); // 조사차수
									pw.write("|^");
									pw.write(ivstgBgnde); // 조사시작일
									pw.write("|^");
									pw.write(ivstgEndde); // 조사종료일
									pw.write("|^");
									pw.write(pm10Val); // 미세먼지(10)
									pw.write("|^");
									pw.write(pm25Val); // 미세먼지(2.5)
									pw.write("|^");
									pw.write(no2Val); // 이산화질소
									pw.write("|^");
									pw.write(so2Val); // 아황산가스
									pw.write("|^");
									pw.write(coVal); // 일산화탄소
									pw.write("|^");
									pw.write(o3Val); // 오존
									pw.write("|^");
									pw.write(pbVal); // 납
									pw.write("|^");
									pw.write(bzVal); // 벤젠
									pw.write("|^");
									pw.write(hclVal); // 염화수소(HCL)
									pw.write("|^");
									pw.write(hgVal); // 수은(Hg)
									pw.write("|^");
									pw.write(niVal); // 니켈(Ni)
									pw.write("|^");
									pw.write(cr6Val); // 6가크롬(Cr6+)
									pw.write("|^");
									pw.write(cdVal); // 카드뮴(Cd)
									pw.write("|^");
									pw.write(asVal); // 비소(As)
									pw.write("|^");
									pw.write(hchoVal); // 포름알데히드
									pw.write("|^");
									pw.write(vcVal); // 염화비닐
									pw.write("|^");
									pw.write(dioxinVal); // 다이옥신
									pw.write("|^");
									pw.write(beVal); // 베릴륨(Be)
									pw.write("|^");
									pw.write(ebVal); // 에틸벤젠
									pw.write("|^");
									pw.write(c6h14Val); // n-헥산
									pw.write("|^");
									pw.write(c6h12Val); // 시클로헥산
									pw.write("|^");
									pw.write(deVal); // 1,-2디클로로에탄
									pw.write("|^");
									pw.write(cfVal); // 클로로포름
									pw.write("|^");
									pw.write(tceVal); // 트리클로로에틸렌
									pw.write("|^");
									pw.write(ctVal); // 사염화탄소
									pw.write("|^");
									pw.write(hcnVal); // 시안화수소
									pw.write("|^");
									pw.write(gmenoPm1024hr); // 공사시 PM-10_24시간평균
									pw.write("|^");
									pw.write(gmenoPm10Year); // 공사시 PM-10_연평균
									pw.write("|^");
									pw.write(gmenoPm2524hr); // 공사시
																// PM-2.5_24시간평균
									pw.write("|^");
									pw.write(gmenoPm25Year); // 공사시 PM-2.5_연평균
									pw.write("|^");
									pw.write(gmenoNo21hr); // 공사시 NO2_1시간평균
									pw.write("|^");
									pw.write(gmenoNo224hr); // 공사시 NO2_24시간평균
									pw.write("|^");
									pw.write(gmenoNo2Year); // 공사시 NO2_연평균
									pw.write("|^");
									pw.write(umenoPm1024hr); // 운영시 PM-10_24시간평균
									pw.write("|^");
									pw.write(umenoPm10Year); // 운영시 PM-10_연평균
									pw.write("|^");
									pw.write(umenoPm2524hr); // 운영시
																// PM-2.5_24시간평균
									pw.write("|^");
									pw.write(umenoPm25Year); // 운영시 PM-2.5_연평균
									pw.write("|^");
									pw.write(umenoSo21hr); // 운영시 SO2_1시간평균
									pw.write("|^");
									pw.write(umenoSo224hr); // 운영시 SO2_24시간평균
									pw.write("|^");
									pw.write(umenoSo2Year); // 운영시 SO2_연평균
									pw.write("|^");
									pw.write(umenoNo21hr); // 운영시 NO2_1시간평균
									pw.write("|^");
									pw.write(umenoNo224hr); // 운영시 NO2_24시간평균
									pw.write("|^");
									pw.write(umenoNo2Year); // 운영시 NO2_연평균
									pw.write("|^");
									pw.write(umenoCo1hr); // 운영시 CO_1시간평균
									pw.write("|^");
									pw.write(umenoCo8hr); // 운영시 CO_8시간평균
									pw.write("|^");
									pw.write(envrnGrad); // 환경기준등급
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

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_02.dat", "EIA");
					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					// step 6. 원본 파일은 삭제
					if (file.exists()) {
						if (file.delete()) {
							System.out.println("원본파일 삭제 처리 완료");
						} else {
							System.out.println("원본 파일 삭제 처리 시패");
						}

					} else {
						System.out.println("파일이 존재하지 않습니다.");
					}

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
