package eia.envrnAffcEvlDecsnCnInfoInqireService;

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


public class GetComCodeInfoInqire {

	// 환경영향평가 결정내용정보 서비스 - 공통코드 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// 요청 파라미터 없음
		if (args.length == 0) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser
					.getProperty("envrnAffcEvlDecsnCnInfoInqireService_getComCodeInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcEvlDecsnCnInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_48.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메시지
					pw.write("|^");
					pw.write("comCd"); // 공통코드
					pw.write("|^");
					pw.write("jongCd"); // 코드종류
					pw.write("|^");
					pw.write("cdNm"); // 코드명
					pw.write("|^");
					pw.write("gubunFl"); // 구분상태
					pw.write("|^");
					pw.write("sortSeq"); // 소팅 일련번호
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			

			String json = "";

			json = JsonParser.parseWriJson(service_url, service_key);
			
			// step 2. 전체 파싱

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer comCd = new StringBuffer(" "); // 공통코드
			StringBuffer jongCd = new StringBuffer(" "); // 코드종류
			StringBuffer cdNm = new StringBuffer(" "); // 코드명
			StringBuffer gubunFl = new StringBuffer(" "); // 구분상태
			StringBuffer sortSeq = new StringBuffer(" "); // 소팅 일련번호

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject header = (JSONObject) response.get("header");
				JSONObject body = (JSONObject) response.get("body");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if (body.get("items") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00")) {

					JSONObject items = (JSONObject) body.get("items");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite(comCd, keyname, "comCd", items_jsonObject);
							JsonParser.colWrite(jongCd, keyname, "jongCd", items_jsonObject);
							JsonParser.colWrite(cdNm, keyname, "cdNm", items_jsonObject);
							JsonParser.colWrite(gubunFl, keyname, "gubunFl", items_jsonObject);
							JsonParser.colWrite(sortSeq, keyname, "sortSeq", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(resultCode);
						resultSb.append("|^");
						resultSb.append(resultMsg);
						resultSb.append("|^");
						resultSb.append(comCd);
						resultSb.append("|^");
						resultSb.append(jongCd);
						resultSb.append("|^");
						resultSb.append(cdNm);
						resultSb.append("|^");
						resultSb.append(gubunFl);
						resultSb.append("|^");
						resultSb.append(sortSeq);
						resultSb.append(System.getProperty("line.separator"));

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(comCd, keyname, "comCd", item_obj);
								JsonParser.colWrite(jongCd, keyname, "jongCd", item_obj);
								JsonParser.colWrite(cdNm, keyname, "cdNm", item_obj);
								JsonParser.colWrite(gubunFl, keyname, "gubunFl", item_obj);
								JsonParser.colWrite(sortSeq, keyname, "sortSeq", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(comCd);
							resultSb.append("|^");
							resultSb.append(jongCd);
							resultSb.append("|^");
							resultSb.append(cdNm);
							resultSb.append("|^");
							resultSb.append(gubunFl);
							resultSb.append("|^");
							resultSb.append(sortSeq);
							resultSb.append(System.getProperty("line.separator"));

						}

					} else {
						System.out.println("parsing error!!");
					}

				} else if (resultCode.equals("03")) {
					System.out.println("data not exist!!");
				} else {
					System.out.println("parsing error!!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 4. 파일에 쓰기
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write(resultSb.toString());
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("parsing complete!");

			// step 5. 대상 서버에 sftp로 보냄

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_48.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
