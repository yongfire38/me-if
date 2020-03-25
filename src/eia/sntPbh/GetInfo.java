package eia.sntPbh;

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


public class GetInfo {

	

	// 위생공중보건정보조회 - 속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("sntPbh_getInfo_url");
			String service_key = JsonParser.getProperty("sntPbh_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_07.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("mgtNo"); // 사업 코드
					pw.write("|^");
					pw.write("ivstgSpotNm"); // 조사지점명
					pw.write("|^");
					pw.write("ivstgGb"); // 조사구분
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
					pw.write("c8h8Val"); // 스타이렌
					pw.write("|^");
					pw.write("hclVal"); // 염화수소(HCL)
					pw.write("|^");
					pw.write("nh3Val"); // 암모니아
					pw.write("|^");
					pw.write("h2sVal"); // 황화수소
					pw.write("|^");
					pw.write("bzVal"); // 벤젠
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
					pw.write("hcnVal"); // 시안화수소
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

								String ivstgOdr = " "; // 조사차수
								String ivstgBgnde = " "; // 조사시작일
								String ivstgEndde = " "; // 조사종료일
								String c8h8Val = " "; // 스타이렌
								String hclVal = " "; // 염화수소(HCL)
								String nh3Val = " "; // 암모니아
								String h2sVal = " "; // 황화수소
								String bzVal = " "; // 벤젠
								String hgVal = " "; // 수은(Hg)
								String niVal = " "; // 니켈(Ni)
								String cr6Val = " "; // 6가크롬(Cr6+)
								String cdVal = " "; // 카드뮴(Cd)
								String asVal = " "; // 비소(As)
								String hchoVal = " "; // 포름알데히드
								String vcVal = " "; // 염화비닐
								String hcnVal = " "; // 시안화수소

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
									if (keyname.equals("c8h8Val")) {
										c8h8Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("hclVal")) {
										hclVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("nh3Val")) {
										nh3Val = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("h2sVal")) {
										h2sVal = odr.get(keyname).toString().trim();
									}
									if (keyname.equals("bzVal")) {
										bzVal = odr.get(keyname).toString().trim();
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
									if (keyname.equals("hcnVal")) {
										hcnVal = odr.get(keyname).toString().trim();
									}

								}

								// step 4. 파일에 쓰기

								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstgGb_str); // 조사구분
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
									pw.write(c8h8Val); // 스타이렌
									pw.write("|^");
									pw.write(hclVal); // 염화수소(HCL)
									pw.write("|^");
									pw.write(nh3Val); // 암모니아
									pw.write("|^");
									pw.write(h2sVal); // 황화수소
									pw.write("|^");
									pw.write(bzVal); // 벤젠
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
									pw.write(hcnVal); // 시안화수소
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_07.dat", "EIA");

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
