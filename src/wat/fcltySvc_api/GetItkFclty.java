package wat.fcltySvc_api;

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


public class GetItkFclty {

	

	// 국가 상수도 정보 시스템 - 취수시설정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 서비스 키만 요구함, 실행시 추가 매개변수 없음
		if (args.length == 0) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); //시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("fcltySvc_getItkFclty_url");
			String service_key = JsonParser.getProperty("fcltySvc_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_01.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("numOfRows"); // 한 페이지 결과 수
					pw.write("|^");
					pw.write("pageNo"); // 페이지 수
					pw.write("|^");
					pw.write("totalCount"); // 데이터 총 개수
					pw.write("|^");
					pw.write("RNUM"); // 순번
					pw.write("|^");
					pw.write("WILO_NAM"); // 수도구분
					pw.write("|^");
					pw.write("WBIZ_NAM"); // 수도사업자
					pw.write("|^");		
					pw.write("FCLT_NAM"); // 취수장명
					pw.write("|^");				
					pw.write("DTL_ADR"); // 취수장 주소
					pw.write("|^");	
					pw.write("WSYS_NM"); // 대표수계
					pw.write("|^");
					pw.write("WSRC_NM"); // 수원(취수원형태)
					pw.write("|^");
					pw.write("PHONE_NUM"); // 전화번호
					pw.write("|^");
					pw.write("COMPL_DAT"); // 준공일
					pw.write("|^");
					pw.write("FCLT_VOL"); // 시설용량(㎥/일)
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
			String numberOfRows_str = "";
			String totalCount_str = "";

			json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject itemsInfo = (JSONObject) body.get("itemsInfo");

				// json 값에서 가져온 전체 데이터 캐수와 한 페이지 당 개수
				int totalCount = ((Long) itemsInfo.get("totalCount")).intValue();
				int numberOfRows = ((Long) itemsInfo.get("numberOfRows")).intValue();
				totalCount_str = Integer.toString(totalCount);
				numberOfRows_str = Integer.toString(numberOfRows);

				pageCount = (totalCount / numberOfRows) + 1;

				// System.out.println("pageCount:::::" + pageCount);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱
			
			StringBuffer resultSb = new StringBuffer("");
			
			StringBuffer RNUM = new StringBuffer(" "); // 순번
			StringBuffer WILO_NAM = new StringBuffer(" "); // 수도구분
			StringBuffer WBIZ_NAM = new StringBuffer(" "); // 수도사업자
			StringBuffer FCLT_NAM = new StringBuffer(" "); // 취수장명
			StringBuffer DTL_ADR = new StringBuffer(" "); // 취수장명
			StringBuffer WSYS_NM = new StringBuffer(" "); // 대표수계
			StringBuffer WSRC_NM = new StringBuffer(" "); // 수원(취수원형태)
			StringBuffer PHONE_NUM = new StringBuffer(" "); // 전화번호
			StringBuffer COMPL_DAT = new StringBuffer(" "); // 준공일
			StringBuffer FCLT_VOL = new StringBuffer(" "); // 시설용량(㎥/일)
			
			for (int i = 1; i <= pageCount; ++i) {

				json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();

					if (resultCode.equals("00")) {

						JSONArray items = (JSONArray) body.get("items");

						for (int r = 0; r < items.size(); r++) {

							JSONObject item = (JSONObject) items.get(r);

							Set<String> key = item.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(RNUM, keyname, "RNUM", item);
								JsonParser.colWrite(WILO_NAM, keyname, "WILO_NAM", item);
								JsonParser.colWrite(WBIZ_NAM, keyname, "WBIZ_NAM", item);
								JsonParser.colWrite(FCLT_NAM, keyname, "FCLT_NAM", item);
								JsonParser.colWrite(DTL_ADR, keyname, "DTL_ADR", item);
								JsonParser.colWrite(WSYS_NM, keyname, "WSYS_NM", item);
								JsonParser.colWrite(WSRC_NM, keyname, "WSRC_NM", item);
								JsonParser.colWrite(PHONE_NUM, keyname, "PHONE_NUM", item);
								JsonParser.colWrite(COMPL_DAT, keyname, "COMPL_DAT", item);
								JsonParser.colWrite(FCLT_VOL, keyname, "FCLT_VOL", item);

							}

							//한번에 문자열 합침
							resultSb.append(numberOfRows_str);
							resultSb.append("|^");
							resultSb.append(String.valueOf(i));
							resultSb.append("|^");
							resultSb.append(totalCount_str);
							resultSb.append("|^");
							resultSb.append(RNUM);
							resultSb.append("|^");
							resultSb.append(WILO_NAM);
							resultSb.append("|^");
							resultSb.append(WBIZ_NAM);
							resultSb.append("|^");
							resultSb.append(FCLT_NAM);
							resultSb.append("|^");
							resultSb.append(DTL_ADR);
							resultSb.append("|^");
							resultSb.append(WSYS_NM);
							resultSb.append("|^");
							resultSb.append(WSRC_NM);
							resultSb.append("|^");
							resultSb.append(PHONE_NUM);
							resultSb.append("|^");
							resultSb.append(COMPL_DAT);
							resultSb.append("|^");
							resultSb.append(FCLT_VOL);
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

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_01.dat", "WAT");
			
			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + ( end - start )/1000.0 +"초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
