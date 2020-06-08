package wri.excllncobsrvt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;

public class Mntrf_test {

	// 우량 관측정보 조회 서비스 - 우량 분강우량 조회
	// 전체 우량 관측소 대상
	// 전체 댐 코드 -> 전체 우량관측소 -> 그걸 가지고 1년치...의 순서
	
	// 로직은 이상이 없지만 실제로 돌려보면 http 에러로 한번에 추출이 불가능.. 전체 우량관측소까지만 구하고 그걸 수기로 넣어서 파일을 작성하는 식으로 처리함
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		System.out.println("firstLine start..");
		long start = System.currentTimeMillis(); // 시작시간

		// 댐코드 전체 파싱해서 맵에 넣기

		String damcode_json = "";

		String damcode_service_url = JsonParser.getProperty("dataPresent_damcode_url");
		String damcode_service_key = JsonParser.getProperty("dataPresent_service_key");
		damcode_json = JsonParser.parseWriJson(damcode_service_url, damcode_service_key);

		HashMap<String, String> damcodeMap = new HashMap<String, String>();

		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(damcode_json);
			JSONObject response = (JSONObject) obj.get("response");

			JSONObject body = (JSONObject) response.get("body");
			JSONObject header = (JSONObject) response.get("header");
			JSONObject items = (JSONObject) body.get("items");

			String resultCode = header.get("resultCode").toString().trim();
			String resultMsg = header.get("resultMsg").toString().trim();

			if (!(resultCode.equals("00"))) {
				System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
			} else if (resultCode.equals("00")) {

				JSONArray items_jsonArray = (JSONArray) items.get("item");

				for (int r = 0; r < items_jsonArray.size(); r++) {

					JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

					Set<String> key = item_obj.keySet();

					Iterator<String> iter = key.iterator();

					String damcode = " "; // 댐코드
					String damnm = " "; // 댐이름

					while (iter.hasNext()) {

						String keyname = iter.next();

						if (keyname.equals("damcode")) {
							damcode = item_obj.get(keyname).toString().trim();
						}
						if (keyname.equals("damnm")) {
							damnm = item_obj.get(keyname).toString().trim();
						}

					}

					damcodeMap.put(damcode, damnm);

				}

			} else {
				System.out.println("parsing error!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("damcodeMap.size()"+damcodeMap.size());
		
		//전체 댐 코드를 가지고 우량관측소 코드를 조회해서 맵에 넣기

		String excll_json = "";
		
		String excll_service_url = JsonParser.getProperty("excllncobsrvt_excllcode_url");
		String excll_service_key = JsonParser.getProperty("excllncobsrvt_service_key");
		
		HashMap<String, String> excllcodeMap = new HashMap<String, String>();
		
		Set<String> damcodeSet = damcodeMap.keySet();
		Iterator<String> damcode_itr = damcodeSet.iterator();
		
		while(damcode_itr.hasNext()){
			
			System.out.println("damcode:::"+damcode_itr.next().toString());
			
			excll_json = JsonParser.parseEiaJson(excll_service_url, excll_service_key, damcode_itr.next().toString());
			
			System.out.println("excll_json::"+excll_json);
				
			try{	
					
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(excll_json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject header = (JSONObject) response.get("header");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if (!(resultCode.equals("00"))) {
					System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
				} else if (resultCode.equals("00") && body.get("items") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
					
					JSONObject items = (JSONObject) body.get("items");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {
						
						String excllncobsrvtcode = " "; // 우량관측소코드
						String obsrvtNm = " "; // 관측소이름

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();
							
							excllncobsrvtcode = JsonParser.colWrite_String(excllncobsrvtcode, keyname, "excllncobsrvtcode", items_jsonObject);
							obsrvtNm = JsonParser.colWrite_String(obsrvtNm, keyname, "obsrvtNm", items_jsonObject);

						}
						
						//수위관측소 맵에 넣기
						excllcodeMap.put(excllncobsrvtcode, obsrvtNm);

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {
							
							String excllncobsrvtcode = " "; // 우량관측소코드
							String obsrvtNm = " "; // 관측소이름

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								excllncobsrvtcode = JsonParser.colWrite_String(excllncobsrvtcode, keyname, "excllncobsrvtcode", item_obj);
								obsrvtNm = JsonParser.colWrite_String(obsrvtNm, keyname, "obsrvtNm", item_obj);

							}

							//수위관측소 맵에 넣기
							excllcodeMap.put(excllncobsrvtcode, obsrvtNm);

						}

					}

				} else {
					System.out.println("parsing error!!");
				}
		
			} catch (Exception e) {
					e.printStackTrace();
			}
		
		}
		
		System.out.println("excllcodeMap.size()"+excllcodeMap.size());
		
		/*for ( String key : excllcodeMap.keySet() ) {
		    System.out.println("방법1) key : " + key +" / value : " + excllcodeMap.get(key));
		}*/

		
		//앞에서 구한 전체 우량관측소 코드를 가지고  우량 분강우량 조회
		
		// step 0.open api url과 서비스 키.
		String service_url = JsonParser.getProperty("excllncobsrvt_mntrf_url");
		String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

		// step 1.파일의 작성
		File file = new File("TIF_WRI_12.dat");
		
		// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 1으로 파싱
		String json = "";

		int pageNo = 1;
		int pageCount = 0;
		
		json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), "20190101",
				"00", "20191231", "24", "1012432");
		
		System.out.println("json::"+json);
		
		//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
		//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
		/*if(json.indexOf("</") > -1){
			json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
		}*/
		
		JSONParser count_parser = new JSONParser();
		JSONObject count_obj = (JSONObject) count_parser.parse(json);
		JSONObject count_response = (JSONObject) count_obj.get("response");

		JSONObject count_body = (JSONObject) count_response.get("body");
		JSONObject count_header = (JSONObject) count_response.get("header");

		String count_resultCode = count_header.get("resultCode").toString().trim();
		String count_resultMsg = count_header.get("resultMsg").toString().trim();

		if (!(count_resultCode.equals("00"))) {
			System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
					+ count_resultMsg);
		} else {
			int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
			int totalCount = ((Long) count_body.get("totalCount")).intValue();

			pageCount = (totalCount / numOfRows) + 1;
		}
		
		// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱
		
		Set<String> excllcodeSet = excllcodeMap.keySet();
		Iterator<String> excllcode_itr = excllcodeSet.iterator();
		
		while(excllcode_itr.hasNext()) {
			
			System.out.println("excllcode:::"+excllcode_itr.next().toString());
			
			for (int i = 1; i <= pageCount; i++) {
				
				json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), "20190101",
						"00", "20191231", "24", excllcode_itr.next().toString());
				
				//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
				//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
				/*if(json.indexOf("</") > -1){
					json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
				}*/

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject header = (JSONObject) response.get("header");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();
				
				if (!(resultCode.equals("00"))) {
					System.out.println(
							"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
				} else if (resultCode.equals("00") && body.get("items") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

					JSONObject items = (JSONObject) body.get("items");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {
						
						String acmtlprcptqy = " "; // 누적우량 현저수량
						String no = " "; // 순번
						String obsrdtmnt = " "; // 시간
						String prcptqy = " "; // 우량

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();
							
							acmtlprcptqy = JsonParser.colWrite_String(acmtlprcptqy, keyname, "acmtlprcptqy", items_jsonObject);
							no = JsonParser.colWrite_String(no, keyname, "no", items_jsonObject);
							obsrdtmnt = JsonParser.colWrite_String(obsrdtmnt, keyname, "obsrdtmnt", items_jsonObject);
							prcptqy = JsonParser.colWrite_String(prcptqy, keyname, "prcptqy", items_jsonObject);

						}
						
						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(
									new BufferedWriter(new FileWriter(file, true)));

							pw.write("20190101");
							pw.write("|^");
							pw.write("00");
							pw.write("|^");
							pw.write("20191231");
							pw.write("|^");
							pw.write("24");
							pw.write("|^");
							pw.write(excllcode_itr.next().toString());
							pw.write("|^");
							pw.write(acmtlprcptqy);
							pw.write("|^");
							pw.write(no);
							pw.write("|^");
							pw.write(obsrdtmnt);
							pw.write("|^");
							pw.write(prcptqy);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {
							
							String acmtlprcptqy = " "; // 누적우량 현저수량
							String no = " "; // 순번
							String obsrdtmnt = " "; // 시간
							String prcptqy = " "; // 우량

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								acmtlprcptqy = JsonParser.colWrite_String(acmtlprcptqy, keyname, "acmtlprcptqy", item_obj);
								no = JsonParser.colWrite_String(no, keyname, "no", item_obj);
								obsrdtmnt = JsonParser.colWrite_String(obsrdtmnt, keyname, "obsrdtmnt", item_obj);
								prcptqy = JsonParser.colWrite_String(prcptqy, keyname, "prcptqy", item_obj);

								

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write("20190101");
								pw.write("|^");
								pw.write("00");
								pw.write("|^");
								pw.write("20191231");
								pw.write("|^");
								pw.write("24");
								pw.write("|^");
								pw.write(excllcode_itr.next().toString());
								pw.write("|^");
								pw.write(acmtlprcptqy);
								pw.write("|^");
								pw.write(no);
								pw.write("|^");
								pw.write(obsrdtmnt);
								pw.write("|^");
								pw.write(prcptqy);
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					} else {
						System.out.println("parsing error!!");
					}

				} else {
					System.out.println("parsing error!!");
				}

				System.out.println("진행도::::::" + i + "/" + pageCount);

			}
			
		}
		
		System.out.println("parsing complete!");

		// step 5. 대상 서버에 sftp로 보냄

		//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_12.dat", "WRI");

		long end = System.currentTimeMillis();
		System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");


	}

}
