package wrs.dailwater;

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

public class Dmntwater {

	// 광역정수장 수질정보 조회 서비스 - 광역월간 수돗물 수질 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 요청 파라미터는 조회시작일(yyyyMMdd), 조회종료일(yyyyMMdd), 정수장 코드의 3개
				// 정수장 코드는 정수장 코드 조회 api에서 조회 가능
				if (args.length == 3) {

					if (args[1].length() == 6 && args[2].length() == 6) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("dailwater_Dmntwater_url");
						String service_key = JsonParser.getProperty("dailwater_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_22.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWriJson_month(service_url, service_key, String.valueOf(pageNo), args[0],
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

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson_month(service_url, service_key, String.valueOf(i), args[0],
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
								
								String mesurede = " ";
								String item1 = " ";
								String item2 = " ";
								String item3 = " ";
								String item5 = " ";
								String item6 = " ";
								String item7 = " ";
								String item8 = " ";
								String item9 = " ";
								String item10 = " ";
								String item11 = " ";
								String item12 = " ";
								String item13 = " ";
								String item14 = " ";
								String item15 = " ";
								String item16 = " ";
								String item17 = " ";
								String item18 = " ";
								String item19 = " ";
								String item20 = " ";
								String item21 = " ";
								String item22 = " ";
								String item23 = " ";
								String item24 = " ";
								String item25 = " ";
								String item26 = " ";
								String item27 = " ";
								String item28 = " ";
								String item29 = " ";
								String item30 = " ";
								String item31 = " ";
								String item32 = " ";
								String item33 = " ";
								String item34 = " ";
								String item35 = " ";
								String item36 = " ";
								String item37 = " ";
								String item38 = " ";
								String item39 = " ";
								String item40 = " ";
								String item41 = " ";
								String item42 = " ";
								String item43 = " ";
								String item44 = " ";
								String item45 = " ";
								String item46 = " ";
								String item47 = " ";
								String item48 = " ";
								String item49 = " ";
								String item50 = " ";
								String item51 = " ";
								String item52 = " ";
								String item53 = " ";
								String item54 = " ";
								String item55 = " ";
								String item56 = " ";
								String item57 = " ";
								String item58 = " ";
								String item59 = " ";
								String item60 = " ";
								
								String numOfRows_str = body.get("numOfRows").toString();

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										if(keyname.equals("mesurede")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												mesurede = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												mesurede = " ";
											}
										}
										if(keyname.equals("item1")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item1 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item1 = " ";
											}
										}
										if(keyname.equals("item2")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item2 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item2 = " ";
											}
										}
										if(keyname.equals("item3")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item3 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item3 = " ";
											}
										}
										if(keyname.equals("item5")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item5 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item5 = " ";
											}
										}
										if(keyname.equals("item6")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item6 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item6 = " ";
											}
										}
										if(keyname.equals("item7")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item7 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item7 = " ";
											}
										}
										if(keyname.equals("item8")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item8 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item8 = " ";
											}
										}
										if(keyname.equals("item9")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item9 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item9 = " ";
											}
										}
										if(keyname.equals("item10")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item10 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item10 = " ";
											}
										}
										if(keyname.equals("item11")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item11 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item11 = " ";
											}
										}
										if(keyname.equals("item12")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item12 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item12 = " ";
											}
										}
										if(keyname.equals("item13")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item13 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item13 = " ";
											}
										}
										if(keyname.equals("item14")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item14 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item14 = " ";
											}
										}
										if(keyname.equals("item15")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item15 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item15 = " ";
											}
										}
										if(keyname.equals("item16")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item16 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item16 = " ";
											}
										}
										if(keyname.equals("item17")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item17 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item17 = " ";
											}
										}
										if(keyname.equals("item18")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item18 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item18 = " ";
											}
										}
										if(keyname.equals("item19")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item19 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item19 = " ";
											}
										}
										if(keyname.equals("item20")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item20 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item20 = " ";
											}
										}
										if(keyname.equals("item21")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item21 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item21 = " ";
											}
										}
										if(keyname.equals("item22")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item22 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item22 = " ";
											}
										}
										if(keyname.equals("item23")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item23 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item23 = " ";
											}
										}
										if(keyname.equals("item24")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item24 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item24 = " ";
											}
										}
										if(keyname.equals("item25")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item25 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item25 = " ";
											}
										}
										if(keyname.equals("item26")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item26 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item26 = " ";
											}
										}
										if(keyname.equals("item27")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item27 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item27 = " ";
											}
										}
										if(keyname.equals("item28")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item28 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item28 = " ";
											}
										}
										if(keyname.equals("item29")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item29 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item29 = " ";
											}
										}
										if(keyname.equals("item30")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item30 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item30 = " ";
											}
										}
										if(keyname.equals("item31")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item31 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item31 = " ";
											}
										}
										if(keyname.equals("item32")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item32 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item32 = " ";
											}
										}
										if(keyname.equals("item33")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item33 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item33 = " ";
											}
										}
										if(keyname.equals("item34")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item34 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item34 = " ";
											}
										}
										if(keyname.equals("item35")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item35 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item35 = " ";
											}
										}
										if(keyname.equals("item36")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item36 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item36 = " ";
											}
										}
										if(keyname.equals("item37")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item37 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item37 = " ";
											}
										}
										if(keyname.equals("item38")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item38 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item38 = " ";
											}
										}
										if(keyname.equals("item39")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item39 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item39 = " ";
											}
										}
										if(keyname.equals("item40")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item40 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item40 = " ";
											}
										}
										if(keyname.equals("item41")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item41 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item41 = " ";
											}
										}
										if(keyname.equals("item42")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item42 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item42 = " ";
											}
										}
										if(keyname.equals("item43")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item43 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item43 = " ";
											}
										}
										if(keyname.equals("item44")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item44 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item44 = " ";
											}
										}
										if(keyname.equals("item45")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item45 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item45 = " ";
											}
										}
										if(keyname.equals("item46")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item46 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item46 = " ";
											}
										}
										if(keyname.equals("item47")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item47 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item47 = " ";
											}
										}
										if(keyname.equals("item48")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item48 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item48 = " ";
											}
										}
										if(keyname.equals("item49")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item49 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item49 = " ";
											}
										}
										if(keyname.equals("item50")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item50 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item50 = " ";
											}
										}
										if(keyname.equals("item51")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item51 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item51 = " ";
											}
										}
										if(keyname.equals("item52")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item52 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item52 = " ";
											}
										}
										if(keyname.equals("item53")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item53 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item53 = " ";
											}
										}
										if(keyname.equals("item54")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item54 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item54 = " ";
											}
										}
										if(keyname.equals("item55")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item55 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item55 = " ";
											}
										}
										if(keyname.equals("item56")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item56 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item56 = " ";
											}
										}
										if(keyname.equals("item57")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item57 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item57 = " ";
											}
										}
										if(keyname.equals("item58")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item58 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item58 = " ";
											}
										}
										if(keyname.equals("item59")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item59 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item59 = " ";
											}
										}
										if(keyname.equals("item60")) {
											if(!(JsonParser.isEmpty(items_jsonObject.get(keyname)))){
												item5 = items_jsonObject.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												item60 = " ";
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
										pw.write(mesurede);
										pw.write("|^");
										pw.write(item1);
										pw.write("|^");
										pw.write(item2);
										pw.write("|^");
										pw.write(item3);
										pw.write("|^");
										pw.write(item5);
										pw.write("|^");
										pw.write(item6);
										pw.write("|^");
										pw.write(item7);
										pw.write("|^");
										pw.write(item8);
										pw.write("|^");
										pw.write(item9);
										pw.write("|^");
										pw.write(item10);
										pw.write("|^");
										pw.write(item11);
										pw.write("|^");
										pw.write(item12);
										pw.write("|^");
										pw.write(item13);
										pw.write("|^");
										pw.write(item14);
										pw.write("|^");
										pw.write(item15);
										pw.write("|^");
										pw.write(item16);
										pw.write("|^");
										pw.write(item17);
										pw.write("|^");
										pw.write(item18);
										pw.write("|^");
										pw.write(item19);
										pw.write("|^");
										pw.write(item20);
										pw.write("|^");
										pw.write(item21);
										pw.write("|^");
										pw.write(item22);
										pw.write("|^");
										pw.write(item23);
										pw.write("|^");
										pw.write(item24);
										pw.write("|^");
										pw.write(item25);
										pw.write("|^");
										pw.write(item26);
										pw.write("|^");
										pw.write(item27);
										pw.write("|^");
										pw.write(item28);
										pw.write("|^");
										pw.write(item29);
										pw.write("|^");
										pw.write(item30);
										pw.write("|^");
										pw.write(item31);
										pw.write("|^");
										pw.write(item32);
										pw.write("|^");
										pw.write(item33);
										pw.write("|^");
										pw.write(item34);
										pw.write("|^");
										pw.write(item35);
										pw.write("|^");
										pw.write(item36);
										pw.write("|^");
										pw.write(item37);
										pw.write("|^");
										pw.write(item38);
										pw.write("|^");
										pw.write(item39);
										pw.write("|^");
										pw.write(item40);
										pw.write("|^");
										pw.write(item41);
										pw.write("|^");
										pw.write(item42);
										pw.write("|^");
										pw.write(item43);
										pw.write("|^");
										pw.write(item44);
										pw.write("|^");
										pw.write(item45);
										pw.write("|^");
										pw.write(item46);
										pw.write("|^");
										pw.write(item47);
										pw.write("|^");
										pw.write(item48);
										pw.write("|^");
										pw.write(item49);
										pw.write("|^");
										pw.write(item50);
										pw.write("|^");
										pw.write(item51);
										pw.write("|^");
										pw.write(item52);
										pw.write("|^");
										pw.write(item53);
										pw.write("|^");
										pw.write(item54);
										pw.write("|^");
										pw.write(item55);
										pw.write("|^");
										pw.write(item56);
										pw.write("|^");
										pw.write(item57);
										pw.write("|^");
										pw.write(item58);
										pw.write("|^");
										pw.write(item59);
										pw.write("|^");
										pw.write(item60);
										pw.write("|^");
										pw.write(numOfRows_str);
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

											if(keyname.equals("mesurede")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													mesurede = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													mesurede = " ";
												}
											}
											if(keyname.equals("item1")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item1 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item1 = " ";
												}
											}
											if(keyname.equals("item2")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item2 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item2 = " ";
												}
											}
											if(keyname.equals("item3")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item3 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item3 = " ";
												}
											}
											if(keyname.equals("item5")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item5 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item5 = " ";
												}
											}
											if(keyname.equals("item6")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item6 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item6 = " ";
												}
											}
											if(keyname.equals("item7")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item7 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item7 = " ";
												}
											}
											if(keyname.equals("item8")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item8 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item8 = " ";
												}
											}
											if(keyname.equals("item9")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item9 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item9 = " ";
												}
											}
											if(keyname.equals("item10")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item10 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item10 = " ";
												}
											}
											if(keyname.equals("item11")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item11 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item11 = " ";
												}
											}
											if(keyname.equals("item12")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item12 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item12 = " ";
												}
											}
											if(keyname.equals("item13")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item13 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item13 = " ";
												}
											}
											if(keyname.equals("item14")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item14 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item14 = " ";
												}
											}
											if(keyname.equals("item15")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item15 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item15 = " ";
												}
											}
											if(keyname.equals("item16")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item16 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item16 = " ";
												}
											}
											if(keyname.equals("item17")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item17 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item17 = " ";
												}
											}
											if(keyname.equals("item18")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item18 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item18 = " ";
												}
											}
											if(keyname.equals("item19")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item19 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item19 = " ";
												}
											}
											if(keyname.equals("item20")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item20 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item20 = " ";
												}
											}
											if(keyname.equals("item21")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item21 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item21 = " ";
												}
											}
											if(keyname.equals("item22")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item22 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item22 = " ";
												}
											}
											if(keyname.equals("item23")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item23 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item23 = " ";
												}
											}
											if(keyname.equals("item24")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item24 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item24 = " ";
												}
											}
											if(keyname.equals("item25")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item25 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item25 = " ";
												}
											}
											if(keyname.equals("item26")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item26 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item26 = " ";
												}
											}
											if(keyname.equals("item27")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item27 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item27 = " ";
												}
											}
											if(keyname.equals("item28")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item28 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item28 = " ";
												}
											}
											if(keyname.equals("item29")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item29 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item29 = " ";
												}
											}
											if(keyname.equals("item30")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item30 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item30 = " ";
												}
											}
											if(keyname.equals("item31")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item31 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item31 = " ";
												}
											}
											if(keyname.equals("item32")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item32 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item32 = " ";
												}
											}
											if(keyname.equals("item33")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item33 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item33 = " ";
												}
											}
											if(keyname.equals("item34")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item34 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item34 = " ";
												}
											}
											if(keyname.equals("item35")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item35 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item35 = " ";
												}
											}
											if(keyname.equals("item36")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item36 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item36 = " ";
												}
											}
											if(keyname.equals("item37")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item37 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item37 = " ";
												}
											}
											if(keyname.equals("item38")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item38 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item38 = " ";
												}
											}
											if(keyname.equals("item39")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item39 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item39 = " ";
												}
											}
											if(keyname.equals("item40")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item40 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item40 = " ";
												}
											}
											if(keyname.equals("item41")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item41 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item41 = " ";
												}
											}
											if(keyname.equals("item42")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item42 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item42 = " ";
												}
											}
											if(keyname.equals("item43")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item43 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item43 = " ";
												}
											}
											if(keyname.equals("item44")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item44 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item44 = " ";
												}
											}
											if(keyname.equals("item45")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item45 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item45 = " ";
												}
											}
											if(keyname.equals("item46")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item46 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item46 = " ";
												}
											}
											if(keyname.equals("item47")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item47 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item47 = " ";
												}
											}
											if(keyname.equals("item48")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item48 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item48 = " ";
												}
											}
											if(keyname.equals("item49")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item49 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item49 = " ";
												}
											}
											if(keyname.equals("item50")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item50 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item50 = " ";
												}
											}
											if(keyname.equals("item51")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item51 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item51 = " ";
												}
											}
											if(keyname.equals("item52")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item52 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item52 = " ";
												}
											}
											if(keyname.equals("item53")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item53 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item53 = " ";
												}
											}
											if(keyname.equals("item54")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item54 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item54 = " ";
												}
											}
											if(keyname.equals("item55")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item55 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item55 = " ";
												}
											}
											if(keyname.equals("item56")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item56 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item56 = " ";
												}
											}
											if(keyname.equals("item57")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item57 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item57 = " ";
												}
											}
											if(keyname.equals("item58")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item58 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item58 = " ";
												}
											}
											if(keyname.equals("item59")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item59 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item59 = " ";
												}
											}
											if(keyname.equals("item60")) {
												if(!(JsonParser.isEmpty(item_obj.get(keyname)))){
													item5 = item_obj.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													item60 = " ";
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
											pw.write(mesurede);
											pw.write("|^");
											pw.write(item1);
											pw.write("|^");
											pw.write(item2);
											pw.write("|^");
											pw.write(item3);
											pw.write("|^");
											pw.write(item5);
											pw.write("|^");
											pw.write(item6);
											pw.write("|^");
											pw.write(item7);
											pw.write("|^");
											pw.write(item8);
											pw.write("|^");
											pw.write(item9);
											pw.write("|^");
											pw.write(item10);
											pw.write("|^");
											pw.write(item11);
											pw.write("|^");
											pw.write(item12);
											pw.write("|^");
											pw.write(item13);
											pw.write("|^");
											pw.write(item14);
											pw.write("|^");
											pw.write(item15);
											pw.write("|^");
											pw.write(item16);
											pw.write("|^");
											pw.write(item17);
											pw.write("|^");
											pw.write(item18);
											pw.write("|^");
											pw.write(item19);
											pw.write("|^");
											pw.write(item20);
											pw.write("|^");
											pw.write(item21);
											pw.write("|^");
											pw.write(item22);
											pw.write("|^");
											pw.write(item23);
											pw.write("|^");
											pw.write(item24);
											pw.write("|^");
											pw.write(item25);
											pw.write("|^");
											pw.write(item26);
											pw.write("|^");
											pw.write(item27);
											pw.write("|^");
											pw.write(item28);
											pw.write("|^");
											pw.write(item29);
											pw.write("|^");
											pw.write(item30);
											pw.write("|^");
											pw.write(item31);
											pw.write("|^");
											pw.write(item32);
											pw.write("|^");
											pw.write(item33);
											pw.write("|^");
											pw.write(item34);
											pw.write("|^");
											pw.write(item35);
											pw.write("|^");
											pw.write(item36);
											pw.write("|^");
											pw.write(item37);
											pw.write("|^");
											pw.write(item38);
											pw.write("|^");
											pw.write(item39);
											pw.write("|^");
											pw.write(item40);
											pw.write("|^");
											pw.write(item41);
											pw.write("|^");
											pw.write(item42);
											pw.write("|^");
											pw.write(item43);
											pw.write("|^");
											pw.write(item44);
											pw.write("|^");
											pw.write(item45);
											pw.write("|^");
											pw.write(item46);
											pw.write("|^");
											pw.write(item47);
											pw.write("|^");
											pw.write(item48);
											pw.write("|^");
											pw.write(item49);
											pw.write("|^");
											pw.write(item50);
											pw.write("|^");
											pw.write(item51);
											pw.write("|^");
											pw.write(item52);
											pw.write("|^");
											pw.write(item53);
											pw.write("|^");
											pw.write(item54);
											pw.write("|^");
											pw.write(item55);
											pw.write("|^");
											pw.write(item56);
											pw.write("|^");
											pw.write(item57);
											pw.write("|^");
											pw.write(item58);
											pw.write("|^");
											pw.write(item59);
											pw.write("|^");
											pw.write(item60);
											pw.write("|^");
											pw.write(numOfRows_str);
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_22.dat", "WRS");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else {
						System.out.println("파라미터 개수 에러!!");
						System.exit(-1);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("fcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
			}



	}

}
