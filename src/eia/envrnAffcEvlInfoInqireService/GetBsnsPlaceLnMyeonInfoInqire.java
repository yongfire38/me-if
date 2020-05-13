package eia.envrnAffcEvlInfoInqireService;

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

public class GetBsnsPlaceLnMyeonInfoInqire {

	// 환경영향평가 정보 서비스 - 환경영향 평가서(사업지, 면) 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 요청 파라미터 x,y 좌표의 2개
				// 마지막에 사업자 코드를 넣는다(실행시 집어넣는 아규먼트, 테이블에서 조회해 온다. api의 파라미터와는 무관)
				if (args.length == 3) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("envrnAffcEvlInfoInqireService_getBsnsPlaceLnMyeonInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_49.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseEiaJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\"}}}";
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

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseEiaJson(service_url, service_key, String.valueOf(i), args[0], args[1]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00") && body.get("items") instanceof String) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
							
							String numOfRows_str = body.get("numOfRows").toString().trim();
							String totalCount_str = body.get("totalCount").toString().trim();

							JSONObject items = (JSONObject) body.get("items");
							
							String rnum = " "; // 정렬순서
							String num = " "; // 고유번호
							String name = " "; // 사업명
							String centerx = " "; // 좌표 X
							String centery = " "; // 좌표 Y
							String distance = " "; // 반경

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									if(keyname.equals("rnum")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											rnum = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											rnum = " ";
										}
									}
									if(keyname.equals("num")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											num = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											num = " ";
										}
									}
									if(keyname.equals("name")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											name = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											name = " ";
										}
									}
									if(keyname.equals("centerx")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											centerx = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											centerx = " ";
										}
									}
									if(keyname.equals("centery")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											centery = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											centery = " ";
										}
									}
									if(keyname.equals("distance")) {
										if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
											distance = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											distance = " ";
										}
									}

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(numOfRows_str); 
									pw.write("|^");
									pw.write(Integer.toString(i)); 
									pw.write("|^");
									pw.write(totalCount_str); 
									pw.write("|^");
									pw.write(rnum); 
									pw.write("|^");
									pw.write(num); 
									pw.write("|^");
									pw.write(name); 
									pw.write("|^");
									pw.write(centerx); 
									pw.write("|^");
									pw.write(centery); 
									pw.write("|^");
									pw.write(distance); 
									pw.write("|^");
									pw.write(args[2]); 
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										if(keyname.equals("rnum")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												rnum = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												rnum = " ";
											}
										}
										if(keyname.equals("num")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												num = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												num = " ";
											}
										}
										if(keyname.equals("name")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												name = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												name = " ";
											}
										}
										if(keyname.equals("centerx")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												centerx = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												centerx = " ";
											}
										}
										if(keyname.equals("centery")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												centery = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												centery = " ";
											}
										}
										if(keyname.equals("distance")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												distance = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												distance = " ";
											}
										}	

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(resultCode); 
										pw.write("|^");
										pw.write(resultMsg); 
										pw.write("|^");
										pw.write(numOfRows_str); 
										pw.write("|^");
										pw.write(Integer.toString(i)); 
										pw.write("|^");
										pw.write(totalCount_str); 
										pw.write("|^");
										pw.write(rnum); 
										pw.write("|^");
										pw.write(num); 
										pw.write("|^");
										pw.write(name); 
										pw.write("|^");
										pw.write(centerx); 
										pw.write("|^");
										pw.write(centery); 
										pw.write("|^");
										pw.write(distance); 
										pw.write("|^");
										pw.write(args[2]); 
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
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(1000);
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_49.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("centerx :" + args[0] + ": centery :" + args[1]);
			}


	}

}
