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

public class WaterQualityList {

	// 실시간 수도정보 수질(시간) - 1시간 수질정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd),
				// 조회종료 시각
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 2 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("waterQuality_waterQualityList_url");
						String service_key = JsonParser.getProperty("waterQuality_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_07.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}

						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");

						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
						} else {

							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;

						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
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
								
								String no = " "; // 번호
								String occrrncDt = " "; // 발생일시
								String fcltyMngNm = " "; // 시설관리명
								String fcltyMngNo = " "; // 시설관리번호
								String fcltyAddr = " "; // 시설주소
								String liIndDivName = " "; // 용수구분명
								String clVal = " "; // 잔류염소
								String phVal = " "; // pH
								String tbVal = " "; // 탁도
								String phUnit = " "; // pH단위
								String tbUnit = " "; // 탁도단위
								String clUnit = " "; // 잔류단위

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										if(keyname.equals("no")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												no = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												no = " ";
											}
										}
										if(keyname.equals("occrrncDt")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												occrrncDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												occrrncDt = " ";
											}
										}
										if(keyname.equals("fcltyMngNm")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												fcltyMngNm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												fcltyMngNm = " ";
											}
										}
										if(keyname.equals("fcltyMngNo")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												fcltyMngNo = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												fcltyMngNo = " ";
											}
										}
										if(keyname.equals("fcltyAddr")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												fcltyAddr = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												fcltyAddr = " ";
											}
										}
										if(keyname.equals("liIndDivName")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												liIndDivName = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												liIndDivName = " ";
											}
										}
										if(keyname.equals("clVal")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												clVal = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												clVal = " ";
											}
										}
										if(keyname.equals("phVal")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												phVal = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												phVal = " ";
											}
										}
										if(keyname.equals("tbVal")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												tbVal = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												tbVal = " ";
											}
										}
										if(keyname.equals("phUnit")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												phUnit = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												phUnit = " ";
											}
										}
										if(keyname.equals("tbUnit")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												tbUnit = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												tbUnit = " ";
											}
										}
										if(keyname.equals("clUnit")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												clUnit = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												clUnit = " ";
											}
										}

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(args[2]);
										pw.write("|^");
										pw.write(args[3]);
										pw.write("|^");
										pw.write(no);
										pw.write("|^");
										pw.write(occrrncDt);
										pw.write("|^");
										pw.write(fcltyMngNm);
										pw.write("|^");
										pw.write(fcltyMngNo);
										pw.write("|^");
										pw.write(fcltyAddr);
										pw.write("|^");
										pw.write(liIndDivName);
										pw.write("|^");
										pw.write(clVal);
										pw.write("|^");
										pw.write(phVal);
										pw.write("|^");
										pw.write(tbVal);
										pw.write("|^");
										pw.write(phUnit);
										pw.write("|^");
										pw.write(tbUnit);
										pw.write("|^");
										pw.write(clUnit);
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

											if(keyname.equals("no")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													no = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													no = " ";
												}
											}
											if(keyname.equals("occrrncDt")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													occrrncDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													occrrncDt = " ";
												}
											}
											if(keyname.equals("fcltyMngNm")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													fcltyMngNm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													fcltyMngNm = " ";
												}
											}
											if(keyname.equals("fcltyMngNo")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													fcltyMngNo = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													fcltyMngNo = " ";
												}
											}
											if(keyname.equals("fcltyAddr")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													fcltyAddr = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													fcltyAddr = " ";
												}
											}
											if(keyname.equals("liIndDivName")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													liIndDivName = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													liIndDivName = " ";
												}
											}
											if(keyname.equals("clVal")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													clVal = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													clVal = " ";
												}
											}
											if(keyname.equals("phVal")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													phVal = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													phVal = " ";
												}
											}
											if(keyname.equals("tbVal")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													tbVal = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													tbVal = " ";
												}
											}
											if(keyname.equals("phUnit")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													phUnit = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													phUnit = " ";
												}
											}
											if(keyname.equals("tbUnit")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													tbUnit = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													tbUnit = " ";
												}
											}
											if(keyname.equals("clUnit")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													clUnit = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													clUnit = " ";
												}
											}

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(args[0]);
											pw.write("|^");
											pw.write(args[1]);
											pw.write("|^");
											pw.write(args[2]);
											pw.write("|^");
											pw.write(args[3]);
											pw.write("|^");
											pw.write(no);
											pw.write("|^");
											pw.write(occrrncDt);
											pw.write("|^");
											pw.write(fcltyMngNm);
											pw.write("|^");
											pw.write(fcltyMngNo);
											pw.write("|^");
											pw.write(fcltyAddr);
											pw.write("|^");
											pw.write(liIndDivName);
											pw.write("|^");
											pw.write(clVal);
											pw.write("|^");
											pw.write(phVal);
											pw.write("|^");
											pw.write(tbVal);
											pw.write("|^");
											pw.write(phUnit);
											pw.write("|^");
											pw.write(tbUnit);
											pw.write("|^");
											pw.write(clUnit);
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

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_07.dat", "WRS");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else {
						System.out.println("파라미터 형식 에러!!");
						System.exit(-1);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(
						"stDt :" + args[0] + ": stTm :" + args[1] + ": edDt :" + args[2] + ": edTm :" + args[3]);
			}



	}

}
