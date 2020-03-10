package eia.geological;

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

public class GetSlr {

	

	// 지형지질 정보 조회 - 지층속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("geological_getSlr_url");
			String service_key = JsonParser.getProperty("geological_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_28.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("mgtNo"); // 사업 코드
					pw.write("|^");
					pw.write("ivstgSpotNm"); // 조사지점명
					pw.write("|^");
					pw.write("dllLc"); // 시추공위치
					pw.write("|^");
					pw.write("xcnts"); // X좌표
					pw.write("|^");
					pw.write("ydnts"); // Y좌표
					pw.write("|^");
					pw.write("slrNm"); // 지층명
					pw.write("|^");
					pw.write("slrDph"); // 지층심도
					pw.write("|^");
					pw.write("slrThick"); // 지층두께
					pw.write("|^");
					pw.write("slrCn"); // 지층구성상태
					pw.write("|^");
					pw.write("nVal"); // N치(TCR/RQD)
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

					JSONArray ivstgs = (JSONArray) body.get("ivstgs");

					for (int r = 0; r < ivstgs.size(); r++) {

						JSONObject ivstg = (JSONObject) ivstgs.get(r);

						String ivstg_ivstgSpotNm_str = " "; // 조사지점명
						String ivstg_dllLc_str = " "; // 시추공위치
						String ivstg_xcnts_str = " "; // X좌표
						String ivstg_ydnts_str = " "; // Y좌표

						if (ivstg.get("ivstgSpotNm") != null) {
							ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
						} else {
							ivstg_ivstgSpotNm_str = " ";
						}

						if (ivstg.get("dllLc") != null) {
							ivstg_dllLc_str = ivstg.get("dllLc").toString().trim();
						} else {
							ivstg_dllLc_str = " ";
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

						JSONArray slrs = (JSONArray) ivstg.get("slrs");

						for (int f = 0; f < slrs.size(); f++) {

							JSONObject slr = (JSONObject) slrs.get(f);

							String slrNm_str = " "; // 지층명

							if (slr.get("slrNm") != null) {
								slrNm_str = slr.get("slrNm").toString().trim();
							} else {
								slrNm_str = " ";
							}

							JSONArray slrDphs = (JSONArray) slr.get("slrDphs");

							for (int i = 0; i < slrDphs.size(); i++) {

								JSONObject slrDph_json = (JSONObject) slrDphs.get(i);

								Set<String> key = slrDph_json.keySet();

								Iterator<String> iter = key.iterator();

								String slrDph = " "; // 지층심도
								String slrThick = " "; // 지층두께
								String slrCn = " "; // 지층구성상태
								String nVal = " "; // N치(TCR/RQD)

								while (iter.hasNext()) {

									String keyname = iter.next();

									if (keyname.equals("slrDph")) {
										slrDph = slrDph_json.get(keyname).toString().trim();
									}
									if (keyname.equals("slrThick")) {
										slrThick = slrDph_json.get(keyname).toString().trim();
									}
									if (keyname.equals("slrCn")) {
										slrCn = slrDph_json.get(keyname).toString().trim();
									}
									if (keyname.equals("nVal")) {
										nVal = slrDph_json.get(keyname).toString().trim();
									}

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstg_dllLc_str); // 시추공위치
									pw.write("|^");
									pw.write(ivstg_xcnts_str); // X좌표
									pw.write("|^");
									pw.write(ivstg_ydnts_str); // Y좌표
									pw.write("|^");
									pw.write(slrNm_str); // 지층명
									pw.write("|^");
									pw.write(slrDph); // 지층심도
									pw.write("|^");
									pw.write(slrThick); // 지층두께
									pw.write("|^");
									pw.write(slrCn); // 지층구성상태
									pw.write("|^");
									pw.write(nVal); // N치(TCR/RQD)
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

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_28.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
					
					// step 6. 원본 파일은 삭제
					if(file.exists()){
						if(file.delete()){
							System.out.println("원본파일 삭제 처리 완료");
						}else{
							System.out.println("원본 파일 삭제 처리 실패");
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
