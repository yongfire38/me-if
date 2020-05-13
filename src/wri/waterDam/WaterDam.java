package wri.waterDam;

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

public class WaterDam {

	// 용수댐 관리현황 - 용수댐관리현황 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 8 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("waterDam_url");
						String service_key = JsonParser.getProperty("waterDam_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_10.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json = "{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
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

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SRVICE\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
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
								
								String suge = " "; // 수계
								String damnm = " "; // 댐이름
								String zerosevenhourprcptqy = " "; // 강우량(금일)
								String prcptqy = " "; // 강우량(전일)
								String nowthsyracmtlrf = " "; // 전일누계(금년)
								String thsyracmtlrf = " "; // 전일누계(전년)
								String nyearavrgacmtlrf = " "; // 전일누계(예년)
								String inflowqy = " "; // 전일유입량
								String total = " "; // 전일
																			// 방류량(전체)
								String sangwater = " "; // 전일
																				// 방류량(생공용수)
								String river = " "; // 전일
																			// 방류량(하천,관개)
								String spilldcwtrqy = " "; // 전일
																					// 방류량(여수로)
								String lvlhindstryuswtrplan = " "; // 영수공급계획
								String nowlowlevel = " "; // 저수위(현재)
								String lastlowlevel = " "; // 저수위(전년)
								String nyearlowlevel = " "; // 저수위(예년)
								String nowrsvwtqy = " "; // 현저수량
								String nowrsvwtqy2 = " "; // 저수율(현재)
								String lastrsvwtqy = " "; // 저수율(전년)
								String nyearrsvwtqy = " "; // 저수율(예년)
								String totrf = " "; // 총저수량

								JSONObject items = (JSONObject) body.get("items");

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										if(keyname.equals("suge")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												suge = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												suge = " ";
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
										if(keyname.equals("zerosevenhourprcptqy")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												zerosevenhourprcptqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												zerosevenhourprcptqy = " ";
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
										if(keyname.equals("nowthsyracmtlrf")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nowthsyracmtlrf = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nowthsyracmtlrf = " ";
											}
										}
										if(keyname.equals("thsyracmtlrf")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												thsyracmtlrf = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												thsyracmtlrf = " ";
											}
										}
										if(keyname.equals("nyearavrgacmtlrf")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nyearavrgacmtlrf = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nyearavrgacmtlrf = " ";
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
										if(keyname.equals("total")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												total = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												total = " ";
											}
										}
										if(keyname.equals("sangwater")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												sangwater = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												sangwater = " ";
											}
										}
										if(keyname.equals("river")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												river = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												river = " ";
											}
										}
										if(keyname.equals("spilldcwtrqy")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												spilldcwtrqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												spilldcwtrqy = " ";
											}
										}
										if(keyname.equals("lvlhindstryuswtrplan")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												lvlhindstryuswtrplan = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												lvlhindstryuswtrplan = " ";
											}
										}
										if(keyname.equals("nowlowlevel")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nowlowlevel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nowlowlevel = " ";
											}
										}
										if(keyname.equals("lastlowlevel")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												lastlowlevel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												lastlowlevel = " ";
											}
										}
										if(keyname.equals("nyearlowlevel")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nyearlowlevel = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nyearlowlevel = " ";
											}
										}
										if(keyname.equals("nowrsvwtqy")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nowrsvwtqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nowrsvwtqy = " ";
											}
										}
										if(keyname.equals("nowrsvwtqy2")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nowrsvwtqy2 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nowrsvwtqy2 = " ";
											}
										}
										if(keyname.equals("lastrsvwtqy")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												lastrsvwtqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												lastrsvwtqy = " ";
											}
										}
										if(keyname.equals("nyearrsvwtqy")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												nyearrsvwtqy = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												nyearrsvwtqy = " ";
											}
										}
										if(keyname.equals("totrf")) {
											if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
												totrf = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												totrf = " ";
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
										pw.write(suge);
										pw.write("|^");
										pw.write(damnm);
										pw.write("|^");
										pw.write(zerosevenhourprcptqy);
										pw.write("|^");
										pw.write(prcptqy);
										pw.write("|^");
										pw.write(nowthsyracmtlrf);
										pw.write("|^");
										pw.write(thsyracmtlrf);
										pw.write("|^");
										pw.write(nyearavrgacmtlrf);
										pw.write("|^");
										pw.write(inflowqy);
										pw.write("|^");
										pw.write(total);
										pw.write("|^");
										pw.write(sangwater);
										pw.write("|^");
										pw.write(river);
										pw.write("|^");
										pw.write(spilldcwtrqy);
										pw.write("|^");
										pw.write(lvlhindstryuswtrplan);
										pw.write("|^");
										pw.write(nowlowlevel);
										pw.write("|^");
										pw.write(lastlowlevel);
										pw.write("|^");
										pw.write(nyearlowlevel);
										pw.write("|^");
										pw.write(nowrsvwtqy);
										pw.write("|^");
										pw.write(nowrsvwtqy2);
										pw.write("|^");
										pw.write(lastrsvwtqy);
										pw.write("|^");
										pw.write(nyearrsvwtqy);
										pw.write("|^");
										pw.write(totrf);
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_10.dat", "WRI");

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
						"tdate :" + args[0] + ": ldate :" + args[1] + ": vdate :" + args[2] + ": vtime :" + args[3]);
			}



	}

}
