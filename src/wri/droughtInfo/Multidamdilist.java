package wri.droughtInfo;

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

public class Multidamdilist {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 다목적댐 가뭄정보 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("droughtInfo_url");
					String service_key = JsonParser.getProperty("droughtInfo_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_01.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
					}

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {

						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; ++i) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00")) {
							
							String seqno = " "; // 순번
							String damcdcrd = " "; // 댐코드
							String damnm = " "; // 댐명칭
							String obsymd = " "; // 측정일자
							String rsqtysum = " "; // 저수량합계(백만m3)
							String stagenow = " "; // 현재저수지가뭄단계명
							String limobsymd = " "; // 용수공급가능일
							String stage_1 = " "; // 관심단계저수량
							String stage_2 = " "; // 주의단계저수량
							String stage_3 = " "; // 경계단계저수량
							String stage_4 = " "; // 심각단계저수량
							String stage_0 = " "; // 정상공급환원단계저수량

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									
									if(keyname.equals("seqno")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											seqno = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											seqno = " ";
										}
									}
									if(keyname.equals("damcdcrd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											damcdcrd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											damcdcrd = " ";
										}
									}
									if(keyname.equals("damnm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											damnm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											damnm = " ";
										}
									}
									if(keyname.equals("obsymd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											obsymd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											obsymd = " ";
										}
									}
									if(keyname.equals("rsqtysum")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											rsqtysum = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											rsqtysum = " ";
										}
									}
									if(keyname.equals("stagenow")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stagenow = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stagenow = " ";
										}
									}
									if(keyname.equals("limobsymd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											limobsymd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											limobsymd = " ";
										}
									}
									if(keyname.equals("stage_1")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stage_1 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stage_1 = " ";
										}
									}
									if(keyname.equals("stage_2")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stage_2 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stage_2 = " ";
										}
									}
									if(keyname.equals("stage_3")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stage_3 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stage_3 = " ";
										}
									}
									if(keyname.equals("stage_4")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stage_4 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stage_4 = " ";
										}
									}	
									if(keyname.equals("stage_0")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											stage_0 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											stage_0 = " ";
										}
									}

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(seqno);
									pw.write("|^");
									pw.write(damcdcrd);
									pw.write("|^");
									pw.write(damnm);
									pw.write("|^");
									pw.write(obsymd);
									pw.write("|^");
									pw.write(rsqtysum);
									pw.write("|^");
									pw.write(stagenow);
									pw.write("|^");
									pw.write(limobsymd);
									pw.write("|^");
									pw.write(stage_1);
									pw.write("|^");
									pw.write(stage_2);
									pw.write("|^");
									pw.write(stage_3);
									pw.write("|^");
									pw.write(stage_4);
									pw.write("|^");
									pw.write(stage_0);
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

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(1000);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_01.dat", "WRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
			}



	}

}
