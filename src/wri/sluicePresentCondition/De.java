package wri.sluicePresentCondition;

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

public class De {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문현황정보(일)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 댐코드 1개, 날짜를 yyyymmdd로 2개 받는다. 파라미터 유효성 체크는 파싱 때 체크
				if (args.length == 3) {

					if (args[1].length() == 8 && args[2].length() == 8) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("sluicePresentCondition_de_url");
						String service_key = JsonParser.getProperty("sluicePresentCondition_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_06.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;
						String numberOfRows_str = "";

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2]);
						
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
							numberOfRows_str = Integer.toString(numOfRows);

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; ++i) {

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2]);
							
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
								
								String obsryymtde = " "; // 일시
								String lowlevel = " "; // 댐수위
								String prcptqy = " "; // 강우량
								String inflowqy = " "; // 유입량
								String totdcwtrqy = " "; // 총방류량
								String rsvwtqy = " "; // 저수량
								String rsvwtrt = " "; // 저수율

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										if(keyname.equals("obsryymtde")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												obsryymtde = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												obsryymtde = " ";
											}
										}
										if(keyname.equals("lowlevel")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												lowlevel = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												lowlevel = " ";
											}
										}
										if(keyname.equals("prcptqy")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												prcptqy = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												prcptqy = " ";
											}
										}
										if(keyname.equals("inflowqy")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												inflowqy = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												inflowqy = " ";
											}
										}
										if(keyname.equals("totdcwtrqy")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												totdcwtrqy = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												totdcwtrqy = " ";
											}
										}
										if(keyname.equals("rsvwtqy")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												rsvwtqy = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												rsvwtqy = " ";
											}
										}
										if(keyname.equals("rsvwtrt")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												rsvwtrt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												rsvwtrt = " ";
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
										pw.write(obsryymtde);
										pw.write("|^");
										pw.write(lowlevel);
										pw.write("|^");
										pw.write(prcptqy);
										pw.write("|^");
										pw.write(inflowqy);
										pw.write("|^");
										pw.write(totdcwtrqy);
										pw.write("|^");
										pw.write(rsvwtqy);
										pw.write("|^");
										pw.write(rsvwtrt);
										pw.write("|^");
										pw.write(numberOfRows_str);
										pw.write("|^");
										pw.write(String.valueOf(i));
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

											if(keyname.equals("obsryymtde")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													obsryymtde = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													obsryymtde = " ";
												}
											}
											if(keyname.equals("lowlevel")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													lowlevel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													lowlevel = " ";
												}
											}
											if(keyname.equals("prcptqy")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													prcptqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													prcptqy = " ";
												}
											}
											if(keyname.equals("inflowqy")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													inflowqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													inflowqy = " ";
												}
											}
											if(keyname.equals("totdcwtrqy")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													totdcwtrqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													totdcwtrqy = " ";
												}
											}
											if(keyname.equals("rsvwtqy")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													rsvwtqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													rsvwtqy = " ";
												}
											}
											if(keyname.equals("rsvwtrt")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													rsvwtrt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													rsvwtrt = " ";
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
											pw.write(obsryymtde);
											pw.write("|^");
											pw.write(lowlevel);
											pw.write("|^");
											pw.write(prcptqy);
											pw.write("|^");
											pw.write(inflowqy);
											pw.write("|^");
											pw.write(totdcwtrqy);
											pw.write("|^");
											pw.write(rsvwtqy);
											pw.write("|^");
											pw.write(rsvwtrt);
											pw.write("|^");
											pw.write(numberOfRows_str);
											pw.write("|^");
											pw.write(String.valueOf(i));
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_06.dat", "WRI");

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
				System.out.println("damcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
			}


	}

}
