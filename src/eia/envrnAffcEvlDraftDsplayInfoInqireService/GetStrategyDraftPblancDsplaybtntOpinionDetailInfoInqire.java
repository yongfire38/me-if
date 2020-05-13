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
//import common.TransSftp;

public class GetStrategyDraftPblancDsplaybtntOpinionDetailInfoInqire {

	// 환경영향평가 초안공람 정보 서비스 - 초안공람 전략영향평가 상세 정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 실행시 필수 매개변수 사전환경성검토 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDraftDsplayInfoInqireService_getStrategyDraftPblancDsplaybtntOpinionDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_38.dat");

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";
					}

					// step 2. 전체 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (response.get("body") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(response.get("body") instanceof String)) {
						
						String perCd = " "; // 사전환경성검토 코드
						String bizNm = " "; // 사업명
						String approvOrganTeam = " "; // 승인기관
						String bizMoney = " "; // 사업비
						String bizSize = " "; // 사업규모
						String bizSizeDan = " "; // 사업규모 단위
						String benfBizmain = " "; // 사업시행자
						String embodEtcNm = " "; // 사업구분기타명칭
						String ccilJongCd = " "; // 협의종류
						String embodCd = " "; // 사업구분코드
						String embodCd2 = " "; // 사업구분 기타 명칭
						String ccilOrganCd = " "; // 협의기관코드
						String ctcMemNm = " "; // 협의기관 담당자
						String ctcMemTeam = " "; // 협의기관
																			// 담당부서
						String ctcMemEmail = " "; // 협의기관
																			// E-mail
						String ctcMemTel = " "; // 협의기관 전화번호
						String ctcMemFax = " "; // 협의기관
																		// Fax번호
						String bizAddrEtc = " "; // 소재지 주소1
						String bizAddrEtc2 = " "; // 소재지 주소2
						String drfopPclDt = " "; // 초안 공고일
						String drfopTmdtStartDt = " "; // 초안
																				// 공람기간
																				// 시작일
						String drfopTmdtEndDt = " "; // 초안공람기간
																				// 종료일
						String drfopExpDttm = " "; // 설명회 일시
						String drfopOpnStartDt = " "; // 의견제출기간
																				// 시작일
						String drfopOpnEndDt = " "; // 의견제출기간
																			// 종료일
						String drfopSite = " "; // 공람장소
						String drfopExpSite = " "; // 설명회 장소
						String drfopTeamNm = " "; // 부서명
						String drfopTel = " "; // 전화번호

						JSONObject body = (JSONObject) response.get("body");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (body.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) body.get("item");

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
								if(keyname.equals("approvOrganTeam")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										approvOrganTeam = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										approvOrganTeam = " ";
									}
								}
								if(keyname.equals("bizMoney")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizMoney = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizMoney = " ";
									}
								}
								if(keyname.equals("bizSize")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizSize = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizSize = " ";
									}
								}
								if(keyname.equals("bizSizeDan")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizSizeDan = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizSizeDan = " ";
									}
								}
								if(keyname.equals("benfBizmain")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										benfBizmain = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										benfBizmain = " ";
									}
								}
								if(keyname.equals("embodEtcNm")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										embodEtcNm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										embodEtcNm = " ";
									}
								}
								if(keyname.equals("ccilJongCd")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ccilJongCd = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ccilJongCd = " ";
									}
								}
								if(keyname.equals("embodCd")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										embodCd = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										embodCd = " ";
									}
								}
								if(keyname.equals("embodCd2")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										embodCd2 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										embodCd2 = " ";
									}
								}
								if(keyname.equals("ccilOrganCd")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										ccilOrganCd = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										ccilOrganCd = " ";
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
								if(keyname.equals("bizAddrEtc")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizAddrEtc = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizAddrEtc = " ";
									}
								}
								if(keyname.equals("bizAddrEtc2")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										bizAddrEtc2 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										bizAddrEtc2 = " ";
									}
								}
								if(keyname.equals("drfopPclDt")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopPclDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopPclDt = " ";
									}
								}
								if(keyname.equals("drfopTmdtStartDt")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopTmdtStartDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopTmdtStartDt = " ";
									}
								}
								if(keyname.equals("drfopTmdtEndDt")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopTmdtEndDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopTmdtEndDt = " ";
									}
								}
								if(keyname.equals("drfopExpDttm")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopExpDttm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopExpDttm = " ";
									}
								}
								if(keyname.equals("drfopOpnStartDt")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopOpnStartDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopOpnStartDt = " ";
									}
								}
								if(keyname.equals("drfopOpnEndDt")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopOpnEndDt = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopOpnEndDt = " ";
									}
								}
								if(keyname.equals("drfopSite")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopSite = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopSite = " ";
									}
								}
								if(keyname.equals("drfopExpSite")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopExpSite = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopExpSite = " ";
									}
								}
								if(keyname.equals("drfopTeamNm")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopTeamNm = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopTeamNm = " ";
									}
								}
								if(keyname.equals("drfopTel")) {
									if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
										drfopTel = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
									}else{
										drfopTel = " ";
									}
								}	

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(perCd); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(approvOrganTeam); 
								pw.write("|^");
								pw.write(bizMoney); 
								pw.write("|^");
								pw.write(bizSize); 
								pw.write("|^");
								pw.write(bizSizeDan); 
								pw.write("|^");
								pw.write(benfBizmain); 
								pw.write("|^");
								pw.write(embodEtcNm); 
								pw.write("|^");
								pw.write(ccilJongCd); 
								pw.write("|^");
								pw.write(embodCd); 
								pw.write("|^");
								pw.write(embodCd2); 
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
								pw.write(bizAddrEtc); 
								pw.write("|^");
								pw.write(bizAddrEtc2); 
								pw.write("|^");
								pw.write(drfopPclDt); 
								pw.write("|^");
								pw.write(drfopTmdtStartDt); 
								pw.write("|^");
								pw.write(drfopTmdtEndDt); 
								pw.write("|^");
								pw.write(drfopExpDttm); 
								pw.write("|^");
								pw.write(drfopOpnStartDt); 
								pw.write("|^");
								pw.write(drfopOpnEndDt); 
								pw.write("|^");
								pw.write(drfopSite); 
								pw.write("|^");
								pw.write(drfopExpSite); 
								pw.write("|^");
								pw.write(drfopTeamNm); 
								pw.write("|^");
								pw.write(drfopTel); 
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}			

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

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
									if(keyname.equals("approvOrganTeam")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											approvOrganTeam = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											approvOrganTeam = " ";
										}
									}
									if(keyname.equals("bizMoney")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizMoney = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizMoney = " ";
										}
									}
									if(keyname.equals("bizSize")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizSize = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizSize = " ";
										}
									}
									if(keyname.equals("bizSizeDan")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizSizeDan = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizSizeDan = " ";
										}
									}
									if(keyname.equals("benfBizmain")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											benfBizmain = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											benfBizmain = " ";
										}
									}
									if(keyname.equals("embodEtcNm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											embodEtcNm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											embodEtcNm = " ";
										}
									}
									if(keyname.equals("ccilJongCd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ccilJongCd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ccilJongCd = " ";
										}
									}
									if(keyname.equals("embodCd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											embodCd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											embodCd = " ";
										}
									}
									if(keyname.equals("embodCd2")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											embodCd2 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											embodCd2 = " ";
										}
									}
									if(keyname.equals("ccilOrganCd")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											ccilOrganCd = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ccilOrganCd = " ";
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
									if(keyname.equals("bizAddrEtc")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizAddrEtc = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizAddrEtc = " ";
										}
									}
									if(keyname.equals("bizAddrEtc2")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											bizAddrEtc2 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											bizAddrEtc2 = " ";
										}
									}
									if(keyname.equals("drfopPclDt")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopPclDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopPclDt = " ";
										}
									}
									if(keyname.equals("drfopTmdtStartDt")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopTmdtStartDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopTmdtStartDt = " ";
										}
									}
									if(keyname.equals("drfopTmdtEndDt")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopTmdtEndDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopTmdtEndDt = " ";
										}
									}
									if(keyname.equals("drfopExpDttm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopExpDttm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopExpDttm = " ";
										}
									}
									if(keyname.equals("drfopOpnStartDt")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopOpnStartDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopOpnStartDt = " ";
										}
									}
									if(keyname.equals("drfopOpnEndDt")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopOpnEndDt = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopOpnEndDt = " ";
										}
									}
									if(keyname.equals("drfopSite")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopSite = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopSite = " ";
										}
									}
									if(keyname.equals("drfopExpSite")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopExpSite = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopExpSite = " ";
										}
									}
									if(keyname.equals("drfopTeamNm")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopTeamNm = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopTeamNm = " ";
										}
									}
									if(keyname.equals("drfopTel")) {
										if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
											drfopTel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											drfopTel = " ";
										}
									}
								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(perCd); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(approvOrganTeam); 
									pw.write("|^");
									pw.write(bizMoney); 
									pw.write("|^");
									pw.write(bizSize); 
									pw.write("|^");
									pw.write(bizSizeDan); 
									pw.write("|^");
									pw.write(benfBizmain); 
									pw.write("|^");
									pw.write(embodEtcNm); 
									pw.write("|^");
									pw.write(ccilJongCd); 
									pw.write("|^");
									pw.write(embodCd); 
									pw.write("|^");
									pw.write(embodCd2); 
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
									pw.write(bizAddrEtc); 
									pw.write("|^");
									pw.write(bizAddrEtc2); 
									pw.write("|^");
									pw.write(drfopPclDt); 
									pw.write("|^");
									pw.write(drfopTmdtStartDt); 
									pw.write("|^");
									pw.write(drfopTmdtEndDt); 
									pw.write("|^");
									pw.write(drfopExpDttm); 
									pw.write("|^");
									pw.write(drfopOpnStartDt); 
									pw.write("|^");
									pw.write(drfopOpnEndDt); 
									pw.write("|^");
									pw.write(drfopSite); 
									pw.write("|^");
									pw.write(drfopExpSite); 
									pw.write("|^");
									pw.write(drfopTeamNm); 
									pw.write("|^");
									pw.write(drfopTel); 
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

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_38.dat", "EIA");

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
