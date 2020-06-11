package eco.ecobank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;
import common.JsonParser;

public class GetVwMapNteeBninPoint {

	// 에코뱅크 - 저서무척추동물_점
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco05_query");
				System.out.println("query :::" + query);

				conn.setAutoCommit(false);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(10000);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(10000);

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				/*rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();*/

				if (args[0].equals("_tset")) {

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String spce_id = " "; // 공간_아이디
						String examin_year = " "; // 조사_년도
						String tme = " "; // 회차
						String examin_begin_de = " "; // 조사_시작_일자
						String examin_end_de = " "; // 조사_종료_일자
						String gnrl_examin_charger_nm = " "; // 일반_조사_담당자_성명
						String rspnsbl_examin_charger_nm = " "; // 책임_조사_담당자_성명
						String spcs_korean_ttle = " "; // 종_한글_명칭
						String spcs_eng_ttle = " "; // 종_영문_명칭
						String idvd_qy = " "; // 개체_수
						String partclr_matter = " "; // 특이_사항
						String lvb_abndnc_degree = " "; // 생물_풍부_정도
						String wrssm_ar = " "; // 수계_면적
						String wtsd_grdnt = " "; // 수변_경사도
						String qltwtr_trbd = " "; // 수질_탁도
						String dgr_lad_use = " "; // 유역_토지_이용
						String river_ttle = " "; // 하천_명칭
						String river_stle = " "; // 하천_형태
						String ar = " "; // 면적
						String dpwt = " "; // 수심
						String dgr_rhtbnk_lad_use_sttus = " "; // 유역_우안_토지_이용
						String dgr_lftbnk_lad_use_sttus = " "; // 유역_좌안_토지_이용
						String river_rhtbnk_dike_ty = " "; // 하천_우안_제방_유형
						String river_lftbnk_dike_ty = " "; // 하천_좌안_제방_유형
						String tmprt = " "; // 기온
						String wttm = " "; // 수온
						String river_rhtbnk_grdnt = " "; // 하천_우안_경사도
						String river_lftbnk_grdnt = " "; // 하천_좌안_경사도
						String dstrb_fctr = " "; // 교란_요인
						String examin_mth = " "; // 조사_방법
						String rvbd_partcl = " "; // 하상_입자
						String rvbd_partcl_stn_lrge_rate = " "; // 하상_입자_돌_대_비율
						String rvbd_partcl_stn_middl_rate = " "; // 하상_입자_돌_중_비율
						String rvbd_partcl_sand_rate = " "; // 하상_입자_모래_비율
						String rvbd_partcl_pbbls_rate = " "; // 하상_입자_자갈_비율
						String rvbd_partcl_pbbls_small_rate = " "; // 하상_입자_자갈_소_비율
						String rvbd_partcl_all_rate = " "; // 하상_입자_전체_비율
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String dstrb_fctr_detail = " "; // 교란_요인_상세
						String etc = " "; // 기타
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String examin_de_frst = " "; // 조사_일자_1차
						String examin_de_scd = " "; // 조사_일자_2차
						String examin_area_ttle = " "; // 조사_지역_명칭
						String examin_odr = " "; // 조사_차수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String geom = " "; // 지오메트리

						spce_id = rs.getString(1);
						examin_year = rs.getString(2);
						tme = rs.getString(3);
						examin_begin_de = rs.getString(4);
						examin_end_de = rs.getString(5);
						gnrl_examin_charger_nm = rs.getString(6);
						rspnsbl_examin_charger_nm = rs.getString(7);
						spcs_korean_ttle = rs.getString(8);
						spcs_eng_ttle = rs.getString(9);
						idvd_qy = rs.getString(10);
						partclr_matter = rs.getString(11);
						lvb_abndnc_degree = rs.getString(12);
						wrssm_ar = rs.getString(13);
						wtsd_grdnt = rs.getString(14);
						qltwtr_trbd = rs.getString(15);
						dgr_lad_use = rs.getString(16);
						river_ttle = rs.getString(17);
						river_stle = rs.getString(18);
						ar = rs.getString(19);
						dpwt = rs.getString(20);
						dgr_rhtbnk_lad_use_sttus = rs.getString(21);
						dgr_lftbnk_lad_use_sttus = rs.getString(22);
						river_rhtbnk_dike_ty = rs.getString(23);
						river_lftbnk_dike_ty = rs.getString(24);
						tmprt = rs.getString(25);
						wttm = rs.getString(26);
						river_rhtbnk_grdnt = rs.getString(27);
						river_lftbnk_grdnt = rs.getString(28);
						dstrb_fctr = rs.getString(29);
						examin_mth = rs.getString(30);
						rvbd_partcl = rs.getString(31);
						rvbd_partcl_stn_lrge_rate = rs.getString(32);
						rvbd_partcl_stn_middl_rate = rs.getString(33);
						rvbd_partcl_sand_rate = rs.getString(34);
						rvbd_partcl_pbbls_rate = rs.getString(35);
						rvbd_partcl_pbbls_small_rate = rs.getString(36);
						rvbd_partcl_all_rate = rs.getString(37);
						grid_no = rs.getString(38);
						fml_eng_ttle = rs.getString(39);
						fml_korean_ttle = rs.getString(40);
						dstrb_fctr_detail = rs.getString(41);
						etc = rs.getString(42);
						ordr_eng_ttle = rs.getString(43);
						ordr_korean_ttle = rs.getString(44);
						examin_de_frst = rs.getString(45);
						examin_de_scd = rs.getString(46);
						examin_area_ttle = rs.getString(47);
						examin_odr = rs.getString(48);
						spcs_schlshp_ttle = rs.getString(49);
						//geom = rs.getString(50);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::spcs_korean_ttle::" + spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle
								+ "::idvd_qy::" + idvd_qy + "::partclr_matter::" + partclr_matter
								+ "::lvb_abndnc_degree::" + lvb_abndnc_degree + "::wrssm_ar::" + wrssm_ar
								+ "::wtsd_grdnt::" + wtsd_grdnt + "::qltwtr_trbd::" + qltwtr_trbd
								+ "::dgr_lad_use::" + dgr_lad_use + "::river_ttle::" + river_ttle
								+ "::river_stle::" + river_stle + "::ar::" + ar + "::dpwt::" + dpwt
								+ "::dgr_rhtbnk_lad_use_sttus::" + dgr_rhtbnk_lad_use_sttus
								+ "::dgr_lftbnk_lad_use_sttus::" + dgr_lftbnk_lad_use_sttus
								+ "::river_rhtbnk_dike_ty::" + river_rhtbnk_dike_ty + "::river_lftbnk_dike_ty::"
								+ river_lftbnk_dike_ty + "::tmprt::" + tmprt + "::wttm::" + wttm
								+ "::river_rhtbnk_grdnt::" + river_rhtbnk_grdnt + "::river_lftbnk_grdnt::"
								+ river_lftbnk_grdnt + "::dstrb_fctr::" + dstrb_fctr + "::examin_mth::"
								+ examin_mth + "::rvbd_partcl::" + rvbd_partcl + "::rvbd_partcl_stn_lrge_rate::"
								+ rvbd_partcl_stn_lrge_rate + "::rvbd_partcl_stn_middl_rate::"
								+ rvbd_partcl_stn_middl_rate + "::rvbd_partcl_sand_rate::" + rvbd_partcl_sand_rate
								+ "::rvbd_partcl_pbbls_rate::" + rvbd_partcl_pbbls_rate
								+ "::rvbd_partcl_pbbls_small_rate::" + rvbd_partcl_pbbls_small_rate
								+ "::rvbd_partcl_all_rate::" + rvbd_partcl_all_rate + "::grid_no::" + grid_no
								+ "::fml_eng_ttle::" + fml_eng_ttle + "::fml_korean_ttle::" + fml_korean_ttle
								+ "::dstrb_fctr_detail::" + dstrb_fctr_detail + "::etc::" + etc
								+ "::ordr_eng_ttle::" + ordr_eng_ttle + "::ordr_korean_ttle::" + ordr_korean_ttle
								+ "::examin_de_frst::" + examin_de_frst + "::examin_de_scd::" + examin_de_scd
								+ "::examin_area_ttle::" + examin_area_ttle + "::examin_odr::" + examin_odr
								+ "::spcs_schlshp_ttle::" + spcs_schlshp_ttle + "::geom::" + geom);


					}
					
					System.out.println("ECO_05 SELECT 프로세스 종료.");

				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String spce_id = " "; // 공간_아이디
						String examin_year = " "; // 조사_년도
						String tme = " "; // 회차
						String examin_begin_de = " "; // 조사_시작_일자
						String examin_end_de = " "; // 조사_종료_일자
						String gnrl_examin_charger_nm = " "; // 일반_조사_담당자_성명
						String rspnsbl_examin_charger_nm = " "; // 책임_조사_담당자_성명
						String spcs_korean_ttle = " "; // 종_한글_명칭
						String spcs_eng_ttle = " "; // 종_영문_명칭
						String idvd_qy = " "; // 개체_수
						String partclr_matter = " "; // 특이_사항
						String lvb_abndnc_degree = " "; // 생물_풍부_정도
						String wrssm_ar = " "; // 수계_면적
						String wtsd_grdnt = " "; // 수변_경사도
						String qltwtr_trbd = " "; // 수질_탁도
						String dgr_lad_use = " "; // 유역_토지_이용
						String river_ttle = " "; // 하천_명칭
						String river_stle = " "; // 하천_형태
						String ar = " "; // 면적
						String dpwt = " "; // 수심
						String dgr_rhtbnk_lad_use_sttus = " "; // 유역_우안_토지_이용
						String dgr_lftbnk_lad_use_sttus = " "; // 유역_좌안_토지_이용
						String river_rhtbnk_dike_ty = " "; // 하천_우안_제방_유형
						String river_lftbnk_dike_ty = " "; // 하천_좌안_제방_유형
						String tmprt = " "; // 기온
						String wttm = " "; // 수온
						String river_rhtbnk_grdnt = " "; // 하천_우안_경사도
						String river_lftbnk_grdnt = " "; // 하천_좌안_경사도
						String dstrb_fctr = " "; // 교란_요인
						String examin_mth = " "; // 조사_방법
						String rvbd_partcl = " "; // 하상_입자
						String rvbd_partcl_stn_lrge_rate = " "; // 하상_입자_돌_대_비율
						String rvbd_partcl_stn_middl_rate = " "; // 하상_입자_돌_중_비율
						String rvbd_partcl_sand_rate = " "; // 하상_입자_모래_비율
						String rvbd_partcl_pbbls_rate = " "; // 하상_입자_자갈_비율
						String rvbd_partcl_pbbls_small_rate = " "; // 하상_입자_자갈_소_비율
						String rvbd_partcl_all_rate = " "; // 하상_입자_전체_비율
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String dstrb_fctr_detail = " "; // 교란_요인_상세
						String etc = " "; // 기타
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String examin_de_frst = " "; // 조사_일자_1차
						String examin_de_scd = " "; // 조사_일자_2차
						String examin_area_ttle = " "; // 조사_지역_명칭
						String examin_odr = " "; // 조사_차수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String geom = " "; // 지오메트리
						
						spce_id = JsonParser.colWrite_String_eic(rs.getString(1)); 
						examin_year = JsonParser.colWrite_String_eic(rs.getString(2)); 
						tme = JsonParser.colWrite_String_eic(rs.getString(3)); 
						examin_begin_de = JsonParser.colWrite_String_eic(rs.getString(4)); 
						examin_end_de = JsonParser.colWrite_String_eic(rs.getString(5)); 
						gnrl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(6)); 
						rspnsbl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(7)); 
						spcs_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(8)); 
						spcs_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(9)); 
						idvd_qy = JsonParser.colWrite_String_eic(rs.getString(10)); 
						partclr_matter = JsonParser.colWrite_String_eic(rs.getString(11)); 
						lvb_abndnc_degree = JsonParser.colWrite_String_eic(rs.getString(12)); 
						wrssm_ar = JsonParser.colWrite_String_eic(rs.getString(13)); 
						wtsd_grdnt = JsonParser.colWrite_String_eic(rs.getString(14)); 
						qltwtr_trbd = JsonParser.colWrite_String_eic(rs.getString(15)); 
						dgr_lad_use = JsonParser.colWrite_String_eic(rs.getString(16)); 
						river_ttle = JsonParser.colWrite_String_eic(rs.getString(17)); 
						river_stle = JsonParser.colWrite_String_eic(rs.getString(18)); 
						ar = JsonParser.colWrite_String_eic(rs.getString(19)); 
						dpwt = JsonParser.colWrite_String_eic(rs.getString(20));
						dgr_rhtbnk_lad_use_sttus = JsonParser.colWrite_String_eic(rs.getString(21));
						dgr_lftbnk_lad_use_sttus = JsonParser.colWrite_String_eic(rs.getString(22));
						river_rhtbnk_dike_ty = JsonParser.colWrite_String_eic(rs.getString(23));
						river_lftbnk_dike_ty = JsonParser.colWrite_String_eic(rs.getString(24));
						tmprt = JsonParser.colWrite_String_eic(rs.getString(25));
						wttm = JsonParser.colWrite_String_eic(rs.getString(26));
						river_rhtbnk_grdnt = JsonParser.colWrite_String_eic(rs.getString(27));
						river_lftbnk_grdnt = JsonParser.colWrite_String_eic(rs.getString(28));
						dstrb_fctr = JsonParser.colWrite_String_eic(rs.getString(29));
						examin_mth = JsonParser.colWrite_String_eic(rs.getString(30));
						rvbd_partcl = JsonParser.colWrite_String_eic(rs.getString(31));
						rvbd_partcl_stn_lrge_rate = JsonParser.colWrite_String_eic(rs.getString(32));
						rvbd_partcl_stn_middl_rate = JsonParser.colWrite_String_eic(rs.getString(33));
						rvbd_partcl_sand_rate = JsonParser.colWrite_String_eic(rs.getString(34));
						rvbd_partcl_pbbls_rate = JsonParser.colWrite_String_eic(rs.getString(35));
						rvbd_partcl_pbbls_small_rate = JsonParser.colWrite_String_eic(rs.getString(36));
						rvbd_partcl_all_rate = JsonParser.colWrite_String_eic(rs.getString(37));
						grid_no = JsonParser.colWrite_String_eic(rs.getString(38));
						fml_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(39));
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(40));
						dstrb_fctr_detail = JsonParser.colWrite_String_eic(rs.getString(41));
						etc = JsonParser.colWrite_String_eic(rs.getString(42));
						ordr_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(43));
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(44));
						examin_de_frst = JsonParser.colWrite_String_eic(rs.getString(45));
						examin_de_scd = JsonParser.colWrite_String_eic(rs.getString(46));
						examin_area_ttle = JsonParser.colWrite_String_eic(rs.getString(47));
						examin_odr = JsonParser.colWrite_String_eic(rs.getString(48));
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(49));
						//geom = JsonParser.colWrite_String_eic(rs.getString(50));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(spce_id);
							pw.write("|^");
							pw.write(examin_year);
							pw.write("|^");
							pw.write(tme);
							pw.write("|^");
							pw.write(examin_begin_de);
							pw.write("|^");
							pw.write(examin_end_de);
							pw.write("|^");
							pw.write(gnrl_examin_charger_nm);
							pw.write("|^");
							pw.write(rspnsbl_examin_charger_nm);
							pw.write("|^");
							pw.write(spcs_korean_ttle);
							pw.write("|^");
							pw.write(spcs_eng_ttle);
							pw.write("|^");
							pw.write(idvd_qy);
							pw.write("|^");
							pw.write(partclr_matter);
							pw.write("|^");
							pw.write(lvb_abndnc_degree);
							pw.write("|^");
							pw.write(wrssm_ar);
							pw.write("|^");
							pw.write(wtsd_grdnt);
							pw.write("|^");
							pw.write(qltwtr_trbd);
							pw.write("|^");
							pw.write(dgr_lad_use);
							pw.write("|^");
							pw.write(river_ttle);
							pw.write("|^");
							pw.write(river_stle);
							pw.write("|^");
							pw.write(ar);
							pw.write("|^");
							pw.write(dpwt);
							pw.write("|^");
							pw.write(dgr_rhtbnk_lad_use_sttus);
							pw.write("|^");
							pw.write(dgr_lftbnk_lad_use_sttus);
							pw.write("|^");
							pw.write(river_rhtbnk_dike_ty);
							pw.write("|^");
							pw.write(river_lftbnk_dike_ty);
							pw.write("|^");
							pw.write(tmprt);
							pw.write("|^");
							pw.write(wttm);
							pw.write("|^");
							pw.write(river_rhtbnk_grdnt);
							pw.write("|^");
							pw.write(river_lftbnk_grdnt);
							pw.write("|^");
							pw.write(dstrb_fctr);
							pw.write("|^");
							pw.write(examin_mth);
							pw.write("|^");
							pw.write(rvbd_partcl);
							pw.write("|^");
							pw.write(rvbd_partcl_stn_lrge_rate);
							pw.write("|^");
							pw.write(rvbd_partcl_stn_middl_rate);
							pw.write("|^");
							pw.write(rvbd_partcl_sand_rate);
							pw.write("|^");
							pw.write(rvbd_partcl_pbbls_rate);
							pw.write("|^");
							pw.write(rvbd_partcl_pbbls_small_rate);
							pw.write("|^");
							pw.write(rvbd_partcl_all_rate);
							pw.write("|^");
							pw.write(grid_no);
							pw.write("|^");
							pw.write(fml_eng_ttle);
							pw.write("|^");
							pw.write(fml_korean_ttle);
							pw.write("|^");
							pw.write(dstrb_fctr_detail);
							pw.write("|^");
							pw.write(etc);
							pw.write("|^");
							pw.write(ordr_eng_ttle);
							pw.write("|^");
							pw.write(ordr_korean_ttle);
							pw.write("|^");
							pw.write(examin_de_frst);
							pw.write("|^");
							pw.write(examin_de_scd);
							pw.write("|^");
							pw.write(examin_area_ttle);
							pw.write("|^");
							pw.write(examin_odr);
							pw.write("|^");
							pw.write(spcs_schlshp_ttle);
							pw.write("|^");
							pw.write(geom);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
						
						//System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
						System.out.println("진행도 :::" + Integer.toString(rs.getRow()) +"번째 줄");
					}
					
					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("ECO_05 SELECT 파일 생성 프로세스 종료.");

				}

			} catch (SQLException sqle) {

				System.out.println("SELECT문에서 예외 발생");
				sqle.printStackTrace();

			} finally {
				// DB 연결을 종료한다.
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
