package spc.taxons;

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

public class Taxons {

	// 한반도 생물다양성 통합관리시스템 - 분류군검색
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		try {

			if (args.length == 1 && !(args[0].equals("ALL"))) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.

				String spc_api_key = JsonParser.getProperty("spc_api_key");
				String spc_taxons_url = JsonParser.getProperty("spc_taxons_url");

				// step 1.파일의 작성

				File file = new File(JsonParser.getProperty("file_path") + "SPC/TIF_SPC_01.dat");

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

				String json = "";

				// step 2. 파라미터로 던진 페이지의 파싱
				json = JsonParser.parseSpcJson(spc_taxons_url, spc_api_key, String.valueOf(args[0]));

				// 테스트 출력
				// System.out.println(json);

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				String resultCode = obj.get("resultCode").toString().trim();

				if (!(resultCode.equals("0"))) {
					System.out.println(args[0] + "페이지 째 서버 비정상 응답..");
				} else {

					JSONArray items = (JSONArray) obj.get("item");

					if (items.size() == 0) {
						System.out.println("data not exist!! pageIndex :" + args[0]);
					} else {

						// 마지막 페이지만 제외하면 1000번
						for (int i = 0; i < items.size(); i++) {

							JSONObject item = (JSONObject) items.get(i);

							Set<String> key = item.keySet();

							Iterator<String> iter = key.iterator();

							String KTSN = " "; // KTSN
							String COMMONGROUP_NM = " "; // 관리분류군
							String TAXON_NM = " "; // 분류군명
							String TAXON_FULL_NM = " "; // 학명
							String TAXON_KNM = " "; // 국명
							String IDENTIERS = " "; // 명명자
							String EXTINCT = " "; // 멸종위기종
							String R200_KNM = " "; // phylum 국명
							String R200_NM = " "; // phylum
							String R300_KNM = " "; // class 국명
							String R300_NM = " "; // class
							String R400_KNM = " "; // order 국명
							String R400_NM = " "; // order
							String R500_KNM = " "; // family 국명
							String R500_NM = " "; // family
							String R600_KNM = " "; // genus 국명
							String R600_NM = " "; // genus
							String R610_KNM = " "; // subgenus 국명
							String R610_NM = " "; // subgenus
							String R700_KNM = " "; // species 국명
							String R700_NM = " "; // species
							String R710_KNM = " "; // subspecies 국명
							String R710_NM = " "; // subspecies
							String ORIGINAL_IDENTIERS = " "; // 최초명명자
							String IDENTIERS_YEAR = " "; // 최초명명년도

							while (iter.hasNext()) {
								String keyname = iter.next();

								if (keyname.equals("KTSN")) {
									KTSN = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("COMMONGROUP_NM")) {
									COMMONGROUP_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("TAXON_NM")) {
									TAXON_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("TAXON_FULL_NM")) {
									TAXON_FULL_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("TAXON_KNM")) {
									TAXON_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("IDENTIERS")) {
									IDENTIERS = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("EXTINCT")) {
									EXTINCT = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R200_KNM")) {
									R200_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R200_NM")) {
									R200_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R300_KNM")) {
									R300_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R300_NM")) {
									R300_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R400_KNM")) {
									R400_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R400_NM")) {
									R400_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R500_KNM")) {
									R500_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R500_NM")) {
									R500_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R600_KNM")) {
									R600_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R600_NM")) {
									R600_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R610_KNM")) {
									R610_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R610_NM")) {
									R610_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R700_KNM")) {
									R700_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R700_NM")) {
									R700_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R710_KNM")) {
									R710_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R710_NM")) {
									R710_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("ORIGINAL_IDENTIERS")) {
									ORIGINAL_IDENTIERS = item.get(keyname).toString()
											.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("IDENTIERS_YEAR")) {
									IDENTIERS_YEAR = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(KTSN); // KTSN
								pw.write("|^");
								pw.write(COMMONGROUP_NM); // 관리분류군
								pw.write("|^");
								pw.write(TAXON_NM); // 분류군명
								pw.write("|^");
								pw.write(TAXON_FULL_NM); // 학명
								pw.write("|^");
								pw.write(TAXON_KNM); // 국명
								pw.write("|^");
								pw.write(IDENTIERS); // 명명자
								pw.write("|^");
								pw.write(EXTINCT); // 멸종위기종
								pw.write("|^");
								pw.write(R200_KNM); // phylum 국명
								pw.write("|^");
								pw.write(R200_NM); // phylum
								pw.write("|^");
								pw.write(R300_KNM); // class 국명
								pw.write("|^");
								pw.write(R300_NM); // class
								pw.write("|^");
								pw.write(R400_KNM); // order 국명
								pw.write("|^");
								pw.write(R400_NM); // order
								pw.write("|^");
								pw.write(R500_KNM); // family 국명
								pw.write("|^");
								pw.write(R500_NM); // family
								pw.write("|^");
								pw.write(R600_KNM); // genus 국명
								pw.write("|^");
								pw.write(R600_NM); // genus
								pw.write("|^");
								pw.write(R610_KNM); // subgenus 국명
								pw.write("|^");
								pw.write(R610_NM); // subgenus
								pw.write("|^");
								pw.write(R700_KNM); // species 국명
								pw.write("|^");
								pw.write(R700_NM); // species
								pw.write("|^");
								pw.write(R710_KNM); // subspecies 국명
								pw.write("|^");
								pw.write(R710_NM); // subspecies
								pw.write("|^");
								pw.write(ORIGINAL_IDENTIERS); // 최초명명자
								pw.write("|^");
								pw.write(IDENTIERS_YEAR); // 최초명명년도
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

							System.out.println("진행도::::::" + i + "/" + items.size());

							// Thread.sleep(1000);

						}
					}

				}

				System.out.println("parsing complete!");

				// step 5. 대상 서버에 sftp로 보냄

				//TransSftp.transSftp(JsonParser.getProperty("file_path") + "SPC/TIF_SPC_01.dat", "SPC");

				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

			} else if(args.length == 1 && (args[0].equals("ALL"))){
				
				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.

				String spc_api_key = JsonParser.getProperty("spc_api_key");
				String spc_taxons_url = JsonParser.getProperty("spc_taxons_url");

				// step 1.파일의 작성

				File file = new File(JsonParser.getProperty("file_path") + "SPC/TIF_SPC_01.dat");

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

				String json = "";

				// step 2. 첫 페이지의 파싱
				json = JsonParser.parseSpcJson(spc_taxons_url, spc_api_key, "1");

				// 테스트 출력
				// System.out.println(json);

				JSONParser first_parser = new JSONParser();
				JSONObject first_obj = (JSONObject) first_parser.parse(json);
				String first_resultCode = first_obj.get("resultCode").toString().trim();

				int pageCount = 0;

				if (!(first_resultCode.equals("0"))) {
					System.out.println("1 페이지 째 서버 비정상 응답..");
				} else {

					JSONObject paginationInfo = (JSONObject) first_obj.get("paginationInfo");
					int totalRecordCount = ((Long) paginationInfo.get("totalRecordCount")).intValue();

					pageCount = (totalRecordCount / 1000) + 1;

					JSONArray items = (JSONArray) first_obj.get("item");

					if (items.size() == 0) {
						System.out.println("data not exist!! pageIndex : 1");
					} else {

						// 마지막 페이지만 제외하면 1000번
						for (int i = 0; i < items.size(); i++) {

							JSONObject item = (JSONObject) items.get(i);

							Set<String> key = item.keySet();

							Iterator<String> iter = key.iterator();

							String KTSN = " "; // KTSN
							String COMMONGROUP_NM = " "; // 관리분류군
							String TAXON_NM = " "; // 분류군명
							String TAXON_FULL_NM = " "; // 학명
							String TAXON_KNM = " "; // 국명
							String IDENTIERS = " "; // 명명자
							String EXTINCT = " "; // 멸종위기종
							String R200_KNM = " "; // phylum 국명
							String R200_NM = " "; // phylum
							String R300_KNM = " "; // class 국명
							String R300_NM = " "; // class
							String R400_KNM = " "; // order 국명
							String R400_NM = " "; // order
							String R500_KNM = " "; // family 국명
							String R500_NM = " "; // family
							String R600_KNM = " "; // genus 국명
							String R600_NM = " "; // genus
							String R610_KNM = " "; // subgenus 국명
							String R610_NM = " "; // subgenus
							String R700_KNM = " "; // species 국명
							String R700_NM = " "; // species
							String R710_KNM = " "; // subspecies 국명
							String R710_NM = " "; // subspecies
							String ORIGINAL_IDENTIERS = " "; // 최초명명자
							String IDENTIERS_YEAR = " "; // 최초명명년도

							while (iter.hasNext()) {
								String keyname = iter.next();

								if (keyname.equals("KTSN")) {
									KTSN = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("COMMONGROUP_NM")) {
									COMMONGROUP_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("TAXON_NM")) {
									TAXON_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("TAXON_FULL_NM")) {
									TAXON_FULL_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("TAXON_KNM")) {
									TAXON_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("IDENTIERS")) {
									IDENTIERS = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}
								if (keyname.equals("EXTINCT")) {
									EXTINCT = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R200_KNM")) {
									R200_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R200_NM")) {
									R200_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R300_KNM")) {
									R300_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R300_NM")) {
									R300_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R400_KNM")) {
									R400_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R400_NM")) {
									R400_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R500_KNM")) {
									R500_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R500_NM")) {
									R500_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R600_KNM")) {
									R600_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R600_NM")) {
									R600_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R610_KNM")) {
									R610_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R610_NM")) {
									R610_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R700_KNM")) {
									R700_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R700_NM")) {
									R700_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R710_KNM")) {
									R710_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("R710_NM")) {
									R710_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("ORIGINAL_IDENTIERS")) {
									ORIGINAL_IDENTIERS = item.get(keyname).toString()
											.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("IDENTIERS_YEAR")) {
									IDENTIERS_YEAR = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
											.trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(KTSN); // KTSN
								pw.write("|^");
								pw.write(COMMONGROUP_NM); // 관리분류군
								pw.write("|^");
								pw.write(TAXON_NM); // 분류군명
								pw.write("|^");
								pw.write(TAXON_FULL_NM); // 학명
								pw.write("|^");
								pw.write(TAXON_KNM); // 국명
								pw.write("|^");
								pw.write(IDENTIERS); // 명명자
								pw.write("|^");
								pw.write(EXTINCT); // 멸종위기종
								pw.write("|^");
								pw.write(R200_KNM); // phylum 국명
								pw.write("|^");
								pw.write(R200_NM); // phylum
								pw.write("|^");
								pw.write(R300_KNM); // class 국명
								pw.write("|^");
								pw.write(R300_NM); // class
								pw.write("|^");
								pw.write(R400_KNM); // order 국명
								pw.write("|^");
								pw.write(R400_NM); // order
								pw.write("|^");
								pw.write(R500_KNM); // family 국명
								pw.write("|^");
								pw.write(R500_NM); // family
								pw.write("|^");
								pw.write(R600_KNM); // genus 국명
								pw.write("|^");
								pw.write(R600_NM); // genus
								pw.write("|^");
								pw.write(R610_KNM); // subgenus 국명
								pw.write("|^");
								pw.write(R610_NM); // subgenus
								pw.write("|^");
								pw.write(R700_KNM); // species 국명
								pw.write("|^");
								pw.write(R700_NM); // species
								pw.write("|^");
								pw.write(R710_KNM); // subspecies 국명
								pw.write("|^");
								pw.write(R710_NM); // subspecies
								pw.write("|^");
								pw.write(ORIGINAL_IDENTIERS); // 최초명명자
								pw.write("|^");
								pw.write(IDENTIERS_YEAR); // 최초명명년도
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

							System.out.println("진행도::::::" + i + "/" + items.size());

							// Thread.sleep(1000);

						}
					}

				}
				
				System.out.println("::1페이지 완료::::::");

				// step 5. 나머지 페이지의 파싱
				for (int i = 2; i <= pageCount; i++) {

					json = JsonParser.parseSpcJson(spc_taxons_url, spc_api_key, String.valueOf(i));

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					String resultCode = obj.get("resultCode").toString().trim();

					if (!(resultCode.equals("0"))) {
						System.out.println(i + " 페이지 째 서버 비정상 응답..");
					} else {

						JSONArray items = (JSONArray) obj.get("item");

						if (items.size() == 0) {
							System.out.println("data not exist!! pageIndex :" + i);
						} else {

							// 마지막 페이지만 제외하면 1000번
							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								String KTSN = " "; // KTSN
								String COMMONGROUP_NM = " "; // 관리분류군
								String TAXON_NM = " "; // 분류군명
								String TAXON_FULL_NM = " "; // 학명
								String TAXON_KNM = " "; // 국명
								String IDENTIERS = " "; // 명명자
								String EXTINCT = " "; // 멸종위기종
								String R200_KNM = " "; // phylum 국명
								String R200_NM = " "; // phylum
								String R300_KNM = " "; // class 국명
								String R300_NM = " "; // class
								String R400_KNM = " "; // order 국명
								String R400_NM = " "; // order
								String R500_KNM = " "; // family 국명
								String R500_NM = " "; // family
								String R600_KNM = " "; // genus 국명
								String R600_NM = " "; // genus
								String R610_KNM = " "; // subgenus 국명
								String R610_NM = " "; // subgenus
								String R700_KNM = " "; // species 국명
								String R700_NM = " "; // species
								String R710_KNM = " "; // subspecies 국명
								String R710_NM = " "; // subspecies
								String ORIGINAL_IDENTIERS = " "; // 최초명명자
								String IDENTIERS_YEAR = " "; // 최초명명년도

								while (iter.hasNext()) {
									String keyname = iter.next();

									if (keyname.equals("KTSN")) {
										KTSN = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
									}
									if (keyname.equals("COMMONGROUP_NM")) {
										COMMONGROUP_NM = item.get(keyname).toString()
												.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
									}
									if (keyname.equals("TAXON_NM")) {
										TAXON_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("TAXON_FULL_NM")) {
										TAXON_FULL_NM = item.get(keyname).toString()
												.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
									}
									if (keyname.equals("TAXON_KNM")) {
										TAXON_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("IDENTIERS")) {
										IDENTIERS = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("EXTINCT")) {
										EXTINCT = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R200_KNM")) {
										R200_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R200_NM")) {
										R200_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R300_KNM")) {
										R300_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R300_NM")) {
										R300_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R400_KNM")) {
										R400_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R400_NM")) {
										R400_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R500_KNM")) {
										R500_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R500_NM")) {
										R500_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R600_KNM")) {
										R600_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R600_NM")) {
										R600_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R610_KNM")) {
										R610_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R610_NM")) {
										R610_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R700_KNM")) {
										R700_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R700_NM")) {
										R700_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R710_KNM")) {
										R710_KNM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("R710_NM")) {
										R710_NM = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.trim();
									}
									if (keyname.equals("ORIGINAL_IDENTIERS")) {
										ORIGINAL_IDENTIERS = item.get(keyname).toString()
												.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
									}
									if (keyname.equals("IDENTIERS_YEAR")) {
										IDENTIERS_YEAR = item.get(keyname).toString()
												.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
									}

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(KTSN); // KTSN
									pw.write("|^");
									pw.write(COMMONGROUP_NM); // 관리분류군
									pw.write("|^");
									pw.write(TAXON_NM); // 분류군명
									pw.write("|^");
									pw.write(TAXON_FULL_NM); // 학명
									pw.write("|^");
									pw.write(TAXON_KNM); // 국명
									pw.write("|^");
									pw.write(IDENTIERS); // 명명자
									pw.write("|^");
									pw.write(EXTINCT); // 멸종위기종
									pw.write("|^");
									pw.write(R200_KNM); // phylum 국명
									pw.write("|^");
									pw.write(R200_NM); // phylum
									pw.write("|^");
									pw.write(R300_KNM); // class 국명
									pw.write("|^");
									pw.write(R300_NM); // class
									pw.write("|^");
									pw.write(R400_KNM); // order 국명
									pw.write("|^");
									pw.write(R400_NM); // order
									pw.write("|^");
									pw.write(R500_KNM); // family 국명
									pw.write("|^");
									pw.write(R500_NM); // family
									pw.write("|^");
									pw.write(R600_KNM); // genus 국명
									pw.write("|^");
									pw.write(R600_NM); // genus
									pw.write("|^");
									pw.write(R610_KNM); // subgenus 국명
									pw.write("|^");
									pw.write(R610_NM); // subgenus
									pw.write("|^");
									pw.write(R700_KNM); // species 국명
									pw.write("|^");
									pw.write(R700_NM); // species
									pw.write("|^");
									pw.write(R710_KNM); // subspecies 국명
									pw.write("|^");
									pw.write(R710_NM); // subspecies
									pw.write("|^");
									pw.write(ORIGINAL_IDENTIERS); // 최초명명자
									pw.write("|^");
									pw.write(IDENTIERS_YEAR); // 최초명명년도
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

								System.out.println("진행도::::::" + r + "/" + items.size());

								// Thread.sleep(1000);

							}

						}

					}
					
					System.out.println(i+"페이지 완료::::::");

				}

				System.out.println("parsing complete!");

				// step 5. 대상 서버에 sftp로 보냄

				// TransSftp.transSftp(JsonParser.getProperty("file_path") + "SPC/TIF_SPC_01.dat", "SPC");

				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
	
			} else {
				System.out.println("파라미터 개수 에러!!");
				System.exit(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", pageIndex :" + args[0]);
			System.exit(-1);
		}

	}

}
