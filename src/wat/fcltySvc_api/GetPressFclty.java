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

import common.JsonParser;
//import common.TransSftp;

public class GetPressFclty {

	// 국가 상수도 정보 시스템 - 가압시설정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 서비스 키만 요구함, 실행시 추가 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("fcltySvc_getPressFclty_url");
					String service_key = JsonParser.getProperty("fcltySvc_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_03.dat");
						

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

					int pageNo = 0;
					int pageCount = 0;
					String numberOfRows_str = "";
					String totalCount_str = "";
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"operation\":\"getPressFclty\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"0\",\"numberOfRows\":0},\"items\":[]},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
					}*/

					JSONObject count_obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(pageNo));
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_itemsInfo = (JSONObject) count_body.get("itemsInfo");

					JSONObject count_header = (JSONObject) count_response.get("header");
					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						throw new Exception();
					} else {

						// json 값에서 가져온 전체 데이터 개수와 한 페이지 당 개수
						int totalCount = ((Long) count_itemsInfo.get("totalCount")).intValue();
						int numberOfRows = ((Long) count_itemsInfo.get("numberOfRows")).intValue();
						totalCount_str = Integer.toString(totalCount);
						numberOfRows_str = Integer.toString(numberOfRows);

						pageCount = (totalCount / numberOfRows) + 1;

					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; ++i) {
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"operation\":\"getPressFclty\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"0\",\"numberOfRows\":0},\"items\":[]},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						}*/

						JSONObject obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(i));
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if((resultCode.equals("03"))){
							System.out.println("data not exist!!");
						} else if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
							System.out.println(
									"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							throw new Exception();
						} else if (resultCode.equals("00")) {

							JSONArray items = (JSONArray) body.get("items");

							for (int r = 0; r < items.size(); r++) {
								
								String RNUM = " ";
								String WBIZ_NAM = " ";
								String FCLT_NAM = " ";
								String DTL_ADR = " ";
								String PHONE_NUM = " ";
								String DSGNF_VOL = " ";
								String COMPL_DAT = " ";
								String MNTRG_CTRL_YN = " ";
								String EMGNC_DVLP_TY_NM = " ";

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									RNUM = JsonParser.colWrite_String(RNUM, keyname, "RNUM", item);
									WBIZ_NAM = JsonParser.colWrite_String(WBIZ_NAM, keyname, "WBIZ_NAM", item);
									FCLT_NAM = JsonParser.colWrite_String(FCLT_NAM, keyname, "FCLT_NAM", item);
									DTL_ADR = JsonParser.colWrite_String(DTL_ADR, keyname, "DTL_ADR", item);
									PHONE_NUM = JsonParser.colWrite_String(PHONE_NUM, keyname, "PHONE_NUM", item);
									DSGNF_VOL = JsonParser.colWrite_String(DSGNF_VOL, keyname, "DSGNF_VOL", item);
									COMPL_DAT = JsonParser.colWrite_String(COMPL_DAT, keyname, "COMPL_DAT", item);
									MNTRG_CTRL_YN = JsonParser.colWrite_String(MNTRG_CTRL_YN, keyname, "MNTRG_CTRL_YN", item);
									EMGNC_DVLP_TY_NM = JsonParser.colWrite_String(EMGNC_DVLP_TY_NM, keyname, "EMGNC_DVLP_TY_NM", item);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(numberOfRows_str);
									pw.write("|^");
									pw.write(String.valueOf(i));
									pw.write("|^");
									pw.write(totalCount_str);
									pw.write("|^");
									pw.write(RNUM);
									pw.write("|^");
									pw.write(WBIZ_NAM);
									pw.write("|^");
									pw.write(FCLT_NAM);
									pw.write("|^");
									pw.write(DTL_ADR);
									pw.write("|^");
									pw.write(PHONE_NUM);
									pw.write("|^");
									pw.write(DSGNF_VOL);
									pw.write("|^");
									pw.write(COMPL_DAT);
									pw.write("|^");
									pw.write(MNTRG_CTRL_YN);
									pw.write("|^");
									pw.write(EMGNC_DVLP_TY_NM);
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

						//Thread.sleep(1000);

						 System.out.println("진행도::::"+String.valueOf(i) + "/" + pageCount);
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_03.dat", "WAT");

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
