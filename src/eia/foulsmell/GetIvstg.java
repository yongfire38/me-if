package eia.foulsmell;

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

public class GetIvstg {

	// 악취정보 서비스 -조사속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("foulsmell_getIvstg_url");
					String service_key = JsonParser.getProperty("foulsmell_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_05.dat");
					
					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}*/

					// step 3.필요에 맞게 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {
						
						

						JSONArray ivstgGbs = (JSONArray) body.get("ivstgGbs");

						// 공통으로 갖는 사업 코드
						for (int i = 0; i < ivstgGbs.size(); i++) {

							JSONObject ivstgGb_Json = (JSONObject) ivstgGbs.get(i);

							String ivstgGb_str = ivstgGb_Json.get("ivstgGb").toString().trim(); // 조사구분

							JSONArray ivstgs = (JSONArray) ivstgGb_Json.get("ivstgs");

							for (int r = 0; r < ivstgs.size(); r++) {

								JSONObject ivstg = (JSONObject) ivstgs.get(r);

								String ivstg_adres_str = " "; // 주소
								String ivstg_ivstgSpotNm_str = " "; // 조사지점명
								String ivstg_xcnts_str = " "; // X좌표
								String ivstg_ydnts_str = " "; // Y좌표

								if (ivstg.get("adres") != null) {
									ivstg_adres_str = ivstg.get("adres").toString().trim();
								} else {
									ivstg_adres_str = " ";
								}

								if (ivstg.get("ivstgSpotNm") != null) {
									ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
								} else {
									ivstg_ivstgSpotNm_str = " ";
								}

								if (ivstg.get("xcnts") != null) {
									ivstg_xcnts_str = ivstg.get("xcnts").toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((ivstg_xcnts_str.indexOf("°") > -1)) {
										ivstg_xcnts_str = JsonParser.dmsTodecimal_split(ivstg_xcnts_str);
									}
								} else {
									ivstg_xcnts_str = " ";
								}

								if (ivstg.get("ydnts") != null) {
									ivstg_ydnts_str = ivstg.get("ydnts").toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((ivstg_ydnts_str.indexOf("°") > -1)) {
										ivstg_ydnts_str = JsonParser.dmsTodecimal_split(ivstg_ydnts_str);
									}
								} else {
									ivstg_ydnts_str = " ";
								}

								JSONArray odrs = (JSONArray) ivstg.get("odrs");

								for (int f = 0; f < odrs.size(); f++) {

									JSONObject odr = (JSONObject) odrs.get(f);

									Set<String> key = odr.keySet();

									Iterator<String> iter = key.iterator();

									String ivstgOdr = " "; // 조사차수
									String ivstgBgnde = " "; // 조사시작일
									String ivstgEndde = " "; // 조사종료일
									String cmpndBsmlVal = " "; // 복합악취
									String nh3Val = " "; // 암모니아
									String ch4sVal = " "; // 메틸메르캅탄
									String h2sVal = " "; // 황화수소
									String ch3sch3Val = " "; // 다이메틸설파이드
									String ch3ssch3Val = " "; // 다이메틸다이설파이드
									String ch33nVal = " "; // 트라이메틸아민
									String c2h4oVal = " "; // 아세트알데하이드
									String c8h8Val = " "; // 스타이렌
									String c3h6oVal = " "; // 프로피온알데하이드
									String ch3ch22choVal = " "; // 뷰틸알데하이드
									String ch3ch23choVal = " "; // n-발레르알데하이드
									String ch32chch2choVal = " "; // i-발레르알데하이드
									String c7h8Val = " "; // 톨루엔
									String c8h10Val = " "; // 자일렌
									String c4h8oVal = " "; // 메틸에틸케톤
									String c6h12oVal = " "; // 메틸아이소뷰틸케톤
									String c6h12o2Val = " "; // 뷰틸아세테이트
									String c2h5coohVal = " "; // 프로피온산
									String ch3ch22choohVal = " "; // n-뷰틸산
									String ch3ch23coohVal = " "; // n-발레르산
									String ch32chch2coohVal = " "; // i-발레르산
									String ch32chch2ohVal = " "; // i-뷰틸알코올

									while (iter.hasNext()) {
										String keyname = iter.next();

										if (keyname.equals("ivstgOdr")) {
											ivstgOdr = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ivstgBgnde")) {
											ivstgBgnde = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ivstgEndde")) {
											ivstgEndde = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("cmpndBsmlVal")) {
											cmpndBsmlVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("nh3Val")) {
											nh3Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch4sVal")) {
											ch4sVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("h2sVal")) {
											h2sVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3sch3Val")) {
											ch3sch3Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3ssch3Val")) {
											ch3ssch3Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch33nVal")) {
											ch33nVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c2h4oVal")) {
											c2h4oVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c8h8Val")) {
											c8h8Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c3h6oVal")) {
											c3h6oVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3ch22choVal")) {
											ch3ch22choVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3ch23choVal")) {
											ch3ch23choVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch32chch2choVal")) {
											ch32chch2choVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c7h8Val")) {
											c7h8Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c8h10Val")) {
											c8h10Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c4h8oVal")) {
											c4h8oVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c6h12oVal")) {
											c6h12oVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c6h12o2Val")) {
											c6h12o2Val = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("c2h5coohVal")) {
											c2h5coohVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3ch22choohVal")) {
											ch3ch22choohVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch3ch23coohVal")) {
											ch3ch23coohVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch32chch2coohVal")) {
											ch32chch2coohVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ch32chch2ohVal")) {
											ch32chch2ohVal = odr.get(keyname).toString().trim();
										}

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(mgtNo); // 사업코드
										pw.write("|^");
										pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
										pw.write("|^");
										pw.write(ivstgGb_str); // 조사구분
										pw.write("|^");
										pw.write(ivstg_adres_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 주소
										pw.write("|^");
										pw.write(ivstg_xcnts_str); // X좌표
										pw.write("|^");
										pw.write(ivstg_ydnts_str); // Y좌표
										pw.write("|^");
										pw.write(ivstgOdr); // 조사차수
										pw.write("|^");
										pw.write(ivstgBgnde); // 조사시작일
										pw.write("|^");
										pw.write(ivstgEndde); // 조사종료일
										pw.write("|^");
										pw.write(cmpndBsmlVal); // 복합악취
										pw.write("|^");
										pw.write(nh3Val); // 암모니아
										pw.write("|^");
										pw.write(ch4sVal); // 메틸메르캅탄
										pw.write("|^");
										pw.write(h2sVal); // 황화수소
										pw.write("|^");
										pw.write(ch3sch3Val); // 다이메틸설파이드
										pw.write("|^");
										pw.write(ch3ssch3Val); // 다이메틸다이설파이드
										pw.write("|^");
										pw.write(ch33nVal); // 트라이메틸아민
										pw.write("|^");
										pw.write(c2h4oVal); // 아세트알데하이드
										pw.write("|^");
										pw.write(c8h8Val); // 스타이렌
										pw.write("|^");
										pw.write(c3h6oVal); // 프로피온알데하이드
										pw.write("|^");
										pw.write(ch3ch22choVal); // 뷰틸알데하이드
										pw.write("|^");
										pw.write(ch3ch23choVal); // n-발레르알데하이드
										pw.write("|^");
										pw.write(ch32chch2choVal); // i-발레르알데하이드
										pw.write("|^");
										pw.write(c7h8Val); // 톨루엔
										pw.write("|^");
										pw.write(c8h10Val); // 자일렌
										pw.write("|^");
										pw.write(c4h8oVal); // 메틸에틸케톤
										pw.write("|^");
										pw.write(c6h12oVal); // 메틸아이소뷰틸케톤
										pw.write("|^");
										pw.write(c6h12o2Val); // 뷰틸아세테이트
										pw.write("|^");
										pw.write(c2h5coohVal); // 프로피온산
										pw.write("|^");
										pw.write(ch3ch22choohVal); // n-뷰틸산
										pw.write("|^");
										pw.write(ch3ch23coohVal); // n-발레르산
										pw.write("|^");
										pw.write(ch32chch2coohVal); // i-발레르산
										pw.write("|^");
										pw.write(ch32chch2ohVal); // i-뷰틸알코올
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								}

							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_05.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
						throw new Exception();
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}


	}

}
