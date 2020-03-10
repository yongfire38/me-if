package eia.envrnAffcEvlDraftDsplayInfoInqireService;

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

public class GetDraftPblancDsplayListInfoInqire {

	// 환경영향평가 초안공람 정보 서비스 - 초안공람 환경영향평가 목록 정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 서비스 키만 요구함, 실행시 필수 매개변수 없음
		if (args.length == 0) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser
					.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_getDraftPblancDsplayListInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_39.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메세지
					pw.write("|^");
					pw.write("numOfRows"); // 한 페이지 결과 수
					pw.write("|^");
					pw.write("pageNo"); // 페이지 번호
					pw.write("|^");
					pw.write("totalCount"); // 전체 결과 수
					pw.write("|^");
					pw.write("rnum"); // 정렬순서
					pw.write("|^");
					pw.write("eiaCd"); // 환경영향평가코드
					pw.write("|^");
					pw.write("eiaSeq"); // 환경영향평가고유번호
					pw.write("|^");
					pw.write("bizNm"); // 사업명
					pw.write("|^");
					pw.write("bizGubunNm"); // 사업구분
					pw.write("|^");
					pw.write("drfopTmdt"); // 초안공람 기간
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}	

			// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
			String json = "";

			int pageNo = 0;
			int pageCount = 0;

			json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");

				int numOfRows = ((Long) body.get("numOfRows")).intValue();
				int totalCount = ((Long) body.get("totalCount")).intValue();

				pageCount = (totalCount / numOfRows) + 1;

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer rnum = new StringBuffer(" "); // 정렬순서
			StringBuffer eiaCd = new StringBuffer(" "); // 환경영향평가코드
			StringBuffer eiaSeq = new StringBuffer(" "); // 환경영향평가고유번호
			StringBuffer bizNm = new StringBuffer(" "); // 사업명
			StringBuffer bizGubunNm = new StringBuffer(" "); // 사업구분
			StringBuffer drfopTmdt = new StringBuffer(" "); // 초안공람 기간

			for (int i = 1; i <= pageCount; i++) {

				json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
		
					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					String numOfRows_str = body.get("numOfRows").toString();
					String totalCount_str = body.get("totalCount").toString();

					if (resultCode.equals("00")) {

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (body.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(rnum, keyname, "rnum", items_jsonObject);
								JsonParser.colWrite(eiaCd, keyname, "eiaCd", items_jsonObject);
								JsonParser.colWrite(eiaSeq, keyname, "eiaSeq", items_jsonObject);
								JsonParser.colWrite(bizNm, keyname, "bizNm", items_jsonObject);
								JsonParser.colWrite(bizGubunNm, keyname, "bizGubunNm", items_jsonObject);
								JsonParser.colWrite(drfopTmdt, keyname, "drfopTmdt", items_jsonObject);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(numOfRows_str);
							resultSb.append("|^");
							resultSb.append(Integer.toString(i));
							resultSb.append("|^");
							resultSb.append(totalCount_str);
							resultSb.append("|^");
							resultSb.append(rnum);
							resultSb.append("|^");
							resultSb.append(eiaCd);
							resultSb.append("|^");
							resultSb.append(eiaSeq);
							resultSb.append("|^");
							resultSb.append(bizNm);
							resultSb.append("|^");
							resultSb.append(bizGubunNm);
							resultSb.append("|^");
							resultSb.append(drfopTmdt);
							resultSb.append(System.getProperty("line.separator"));

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(rnum, keyname, "rnum", item_obj);
									JsonParser.colWrite(eiaCd, keyname, "eiaCd", item_obj);
									JsonParser.colWrite(eiaSeq, keyname, "eiaSeq", item_obj);
									JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
									JsonParser.colWrite(bizGubunNm, keyname, "bizGubunNm", item_obj);
									JsonParser.colWrite(drfopTmdt, keyname, "drfopTmdt", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(resultCode);
								resultSb.append("|^");
								resultSb.append(resultMsg);
								resultSb.append("|^");
								resultSb.append(numOfRows_str);
								resultSb.append("|^");
								resultSb.append(Integer.toString(i));
								resultSb.append("|^");
								resultSb.append(totalCount_str);
								resultSb.append("|^");
								resultSb.append(rnum);
								resultSb.append("|^");
								resultSb.append(eiaCd);
								resultSb.append("|^");
								resultSb.append(eiaSeq);
								resultSb.append("|^");
								resultSb.append(bizNm);
								resultSb.append("|^");
								resultSb.append(bizGubunNm);
								resultSb.append("|^");
								resultSb.append(drfopTmdt);
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

				System.out.println("진행도::::::" + i + "/" + pageCount);

				Thread.sleep(1000);

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

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_39.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
