package eia.beffatStrtgySmallScaleDscssSttusInfoInqireService;

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

public class GetBsnsStrtgySmallScaleDscssBsnsDetailInfoInqire {

	// 사전/전략/소규모협의현황 정보 서비스 - 사전/전략/소규모 협의사업 상세 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 요청 파라미터 사전환경성검토코드 1개
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"beffatStrtgySmallScaleDscssSttusInfoInqireService_getBsnsStrtgySmallScaleDscssBsnsDetailInfoInqire_url");
					String service_key = JsonParser
							.getProperty("beffatStrtgySmallScaleDscssSttusInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_54.dat");

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
					}

					// step 2. 전체 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (resultCode.equals("00") && body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
						
						String perCd = " "; // 사전환경성검토코드
						String bizNm = " "; // 사업명
						String ccilOrganCd = " "; // 협의기관
						String ctcMemNm = " "; // 담당자
						String ctcMemTeam = " "; // 담당부서
						String ctcMemEmail = " "; // E-mail
						String ctcMemTel = " "; // 전화번호
						String ctcMemFax = " "; // Fax 번호
						String ccilResFl = " "; // 협의결과
						
						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								if(keyname.equals("perCd")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										perCd = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										perCd = " ";
									}
								}
								if(keyname.equals("bizNm")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizNm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizNm = " ";
									}
								}
								if(keyname.equals("ctcMemNm")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ctcMemNm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ctcMemNm = " ";
									}
								}
								if(keyname.equals("ctcMemTeam")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ctcMemTeam = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ctcMemTeam = " ";
									}
								}
								if(keyname.equals("ctcMemEmail")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ctcMemEmail = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ctcMemEmail = " ";
									}
								}
								if(keyname.equals("ctcMemTel")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ctcMemTel = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ctcMemTel = " ";
									}
								}
								if(keyname.equals("ctcMemFax")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ctcMemFax = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ctcMemFax = " ";
									}
								}
								if(keyname.equals("ccilResFl")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ccilResFl = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ccilResFl = " ";
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
								pw.write(perCd); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(ccilOrganCd); 
								pw.write("|^");
								pw.write(ctcMemNm); 
								pw.write("|^");
								pw.write(ctcMemTeam); 
								pw.write("|^");
								pw.write(ctcMemEmail); 
								pw.write("|^");
								pw.write(ctcMemTel); 
								pw.write("|^");
								pw.write(ctcMemFax); 
								pw.write("|^");
								pw.write(ccilResFl);  
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

									if(keyname.equals("perCd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											perCd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											perCd = " ";
										}
									}
									if(keyname.equals("bizNm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizNm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizNm = " ";
										}
									}
									if(keyname.equals("ctcMemNm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ctcMemNm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctcMemNm = " ";
										}
									}
									if(keyname.equals("ctcMemTeam")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ctcMemTeam = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctcMemTeam = " ";
										}
									}
									if(keyname.equals("ctcMemEmail")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ctcMemEmail = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctcMemEmail = " ";
										}
									}
									if(keyname.equals("ctcMemTel")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ctcMemTel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctcMemTel = " ";
										}
									}
									if(keyname.equals("ctcMemFax")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ctcMemFax = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctcMemFax = " ";
										}
									}
									if(keyname.equals("ccilResFl")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ccilResFl = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ccilResFl = " ";
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
									pw.write(perCd); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(ccilOrganCd); 
									pw.write("|^");
									pw.write(ctcMemNm); 
									pw.write("|^");
									pw.write(ctcMemTeam); 
									pw.write("|^");
									pw.write(ctcMemEmail); 
									pw.write("|^");
									pw.write(ctcMemTel); 
									pw.write("|^");
									pw.write(ctcMemFax); 
									pw.write("|^");
									pw.write(ccilResFl);  
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

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_54.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("perCd :" + args[0]);
			}


	}

}
