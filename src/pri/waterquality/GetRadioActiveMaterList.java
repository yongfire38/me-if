package pri.waterquality;

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

public class GetRadioActiveMaterList {
	
	

	// 수질정보 DB 서비스 - 방사성물질측정망 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 서비스 키만 요구함, 실행시 필수 매개변수 없음(전체 데이터 수가 많지 않으므로..)
		if (args.length == 0) {
			
			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("PRI_WaterQualityService_getRadioActiveMaterList_url");
			String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_04.dat");
			
			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메시지
					pw.write("|^");
					pw.write("rn"); // 행번호
					pw.write("|^");
					pw.write("ptNo"); // 조사지점코드
					pw.write("|^");
					pw.write("ptNm"); // 조사지점명
					pw.write("|^");
					pw.write("wmcymd"); // 채취일
					pw.write("|^");
					pw.write("act1"); // 측정값 Cs-134(세슘)
					pw.write("|^");
					pw.write("act2"); // 측정값 Cs-137(세슘)
					pw.write("|^");
					pw.write("act3"); // 측정값 I-131(요드)
					pw.write("|^");
					pw.write("numOfRows"); // 한 페이지 결과 수
					pw.write("|^");
					pw.write("pageNo"); // 페이지 번호
					pw.write("|^");
					pw.write("totalCount"); // 전체 결과 수
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

				JSONObject getRealTimeWaterQualityList = (JSONObject) obj.get("getRadioActiveMaterList");

				int numOfRows = ((Long) getRealTimeWaterQualityList.get("numOfRows")).intValue();
				int totalCount = ((Long) getRealTimeWaterQualityList.get("totalCount")).intValue();

				pageCount = (totalCount / numOfRows) + 1;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

			StringBuffer resultSb = new StringBuffer("");
			
			StringBuffer resultCode_col = new StringBuffer(" "); // 결과코드
			StringBuffer resultMsg_col = new StringBuffer(" "); // 결과메시지
			StringBuffer rn = new StringBuffer(" "); // 행번호
			StringBuffer ptNo = new StringBuffer(" "); // 조사지점코드
			StringBuffer ptNm = new StringBuffer(" "); // 조사지점명
			StringBuffer wmcymd = new StringBuffer(" "); // 채취일
			StringBuffer act1 = new StringBuffer(" "); // 측정값 Cs-134(세슘)
			StringBuffer act2 = new StringBuffer(" "); // 측정값 Cs-137(세슘)
			StringBuffer act3 = new StringBuffer(" "); // 측정값 I-131(요드)
			StringBuffer numOfRows = new StringBuffer(" "); // 한 페이지 결과 수
			StringBuffer pageNo_str = new StringBuffer(" "); // 페이지 번호
			StringBuffer totalCount = new StringBuffer(" "); // 전체 결과 수
			
			for (int i = 1; i <= pageCount; i++) {
				
				json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
				
				try {
					
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);

					JSONObject getRealTimeWaterQualityList = (JSONObject) obj.get("getRadioActiveMaterList");

					JSONObject header = (JSONObject) getRealTimeWaterQualityList.get("header");
					
					resultCode_col.setLength(0);
					resultCode_col.append(header.get("code").toString().trim()); // 결과
																					// 코드
					resultMsg_col.setLength(0);
					resultMsg_col.append(header.get("message").toString().trim()); // 결과
																					// 메시지
					numOfRows.setLength(0);
					numOfRows.append(getRealTimeWaterQualityList.get("numOfRows").toString().trim());

					pageNo_str.setLength(0);
					pageNo_str.append(String.valueOf(i).trim());

					totalCount.setLength(0);
					totalCount.append(getRealTimeWaterQualityList.get("totalCount").toString().trim());
					
					if (resultCode_col.toString().equals("00")) {
						
						JSONArray items = (JSONArray) getRealTimeWaterQualityList.get("item");
						
						for (int r = 0; r < items.size(); r++) {
							
							JSONObject item = (JSONObject) items.get(r);

							Set<String> key = item.keySet();

							Iterator<String> iter = key.iterator();
							
							while (iter.hasNext()) {
								
								String keyname = iter.next();
								
								JsonParser.colWrite(rn, keyname, "RN", item);
								JsonParser.colWrite(ptNo, keyname, "PT_NO", item);
								JsonParser.colWrite(ptNm, keyname, "PT_NM", item);
								JsonParser.colWrite(act1, keyname, "ACT1", item);
								JsonParser.colWrite(act2, keyname, "ACT2", item);
								JsonParser.colWrite(act3, keyname, "ACT3", item);
								JsonParser.colWrite(wmcymd, keyname, "WMCYMD", item);
								JsonParser.colWrite(numOfRows, keyname, "numOfRows", item);
								JsonParser.colWrite(pageNo_str, keyname, "pageNo", item);
								JsonParser.colWrite(totalCount, keyname, "totalCount", item);
									
							}
							
							// 한번에 문자열 합침
							resultSb.append(resultCode_col);
							resultSb.append("|^");
							resultSb.append(resultMsg_col);
							resultSb.append("|^");
							resultSb.append(rn);
							resultSb.append("|^");
							resultSb.append(ptNo);
							resultSb.append("|^");
							resultSb.append(ptNm);
							resultSb.append("|^");
							resultSb.append(act1);
							resultSb.append("|^");
							resultSb.append(act2);
							resultSb.append("|^");
							resultSb.append(act3);
							resultSb.append("|^");
							resultSb.append(numOfRows);
							resultSb.append("|^");
							resultSb.append(pageNo_str);
							resultSb.append("|^");
							resultSb.append(totalCount);
							resultSb.append(System.getProperty("line.separator"));
							
						}
						
						
					} else if (resultCode_col.toString().equals("03")) {
						System.out.println("data not exist!!");
					} else {
						System.out.println("parsing error!! :::" + resultCode_col.toString());
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("진행도::::::"+i+"/"+ pageCount);
				
				Thread.sleep(2500);
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
	
			TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_04.dat", "PRI");
	
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
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
