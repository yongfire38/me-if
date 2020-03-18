package wrs.waterQuality;

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


public class SupplyLgldCodeList {

	

	// 실시간 수도정보 수질 -공급지역 정수장 코드 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터 없음
		if (args.length == 0) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("waterQuality_supplyLgldCodeList_url");
			String service_key = JsonParser.getProperty("waterQuality_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_09.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("addrName"); // 법정동명
					pw.write("|^");
					pw.write("fcltyMngNm"); // 시설관리명
					pw.write("|^");
					pw.write("fcltyMngNo"); // 시설관리번호
					pw.write("|^");
					pw.write("lgldCode"); // 법정동코드
					pw.write("|^");
					pw.write("lgldFullAddr"); // 법정동 상세 주소
					pw.write("|^");
					pw.write("sujCode"); // 사업장코드
					pw.write("|^");
					pw.write("upprLgldCode"); // 상위법정동코드
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

			StringBuffer addrName = new StringBuffer(" ");
			StringBuffer fcltyMngNm = new StringBuffer(" ");
			StringBuffer fcltyMngNo = new StringBuffer(" ");
			StringBuffer lgldCode = new StringBuffer(" ");
			StringBuffer lgldFullAddr = new StringBuffer(" ");
			StringBuffer sujCode = new StringBuffer(" ");
			StringBuffer upprLgldCode = new StringBuffer(" ");

			for (int i = 1; i <= pageCount; i++) {

				json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
					JSONObject items = (JSONObject) body.get("items");

					String resultCode = header.get("resultCode").toString().trim();

					if (resultCode.equals("00")) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(addrName, keyname, "addrName", item_obj);
								JsonParser.colWrite(fcltyMngNm, keyname, "fcltyMngNm", item_obj);
								JsonParser.colWrite(fcltyMngNo, keyname, "fcltyMngNo", item_obj);
								JsonParser.colWrite(lgldCode, keyname, "lgldCode", item_obj);
								JsonParser.colWrite(lgldFullAddr, keyname, "lgldFullAddr", item_obj);
								JsonParser.colWrite(sujCode, keyname, "sujCode", item_obj);
								JsonParser.colWrite(upprLgldCode, keyname, "upprLgldCode", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(addrName);
							resultSb.append("|^");
							resultSb.append(fcltyMngNm);
							resultSb.append("|^");
							resultSb.append(fcltyMngNo);
							resultSb.append("|^");
							resultSb.append(lgldCode);
							resultSb.append("|^");
							resultSb.append(lgldFullAddr);
							resultSb.append("|^");
							resultSb.append(sujCode);
							resultSb.append("|^");
							resultSb.append(upprLgldCode);
							resultSb.append(System.getProperty("line.separator"));

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

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_09.dat", "WRS");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
