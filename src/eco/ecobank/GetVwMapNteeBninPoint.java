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

				Class.forName(DBConnection.getProperty("eco_post_driver"));
				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco05_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] spce_id = new String[rowCount]; // 공간_아이디
				String[] examin_year = new String[rowCount]; // 조사_년도
				String[] tme = new String[rowCount]; // 회차
				String[] examin_begin_de = new String[rowCount]; // 조사_시작_일자
				String[] examin_end_de = new String[rowCount]; // 조사_종료_일자
				String[] gnrl_examin_charger_nm = new String[rowCount]; // 일반_조사_담당자_성명
				String[] rspnsbl_examin_charger_nm = new String[rowCount]; // 책임_조사_담당자_성명
				String[] spcs_korean_ttle = new String[rowCount]; // 종_한글_명칭
				String[] spcs_eng_ttle = new String[rowCount]; // 종_영문_명칭
				String[] idvd_qy = new String[rowCount]; // 개체_수
				String[] partclr_matter = new String[rowCount]; // 특이_사항
				String[] lvb_abndnc_degree = new String[rowCount]; // 생물_풍부_정도
				String[] wrssm_ar = new String[rowCount]; // 수계_면적
				String[] wtsd_grdnt = new String[rowCount]; // 수변_경사도
				String[] qltwtr_trbd = new String[rowCount]; // 수질_탁도
				String[] dgr_lad_use = new String[rowCount]; // 유역_토지_이용
				String[] river_ttle = new String[rowCount]; // 하천_명칭
				String[] river_stle = new String[rowCount]; // 하천_형태
				String[] ar = new String[rowCount]; // 면적
				String[] dpwt = new String[rowCount]; // 수심
				String[] dgr_rhtbnk_lad_use_sttus = new String[rowCount]; // 유역_우안_토지_이용
				String[] dgr_lftbnk_lad_use_sttus = new String[rowCount]; // 유역_좌안_토지_이용
				String[] river_rhtbnk_dike_ty = new String[rowCount]; // 하천_우안_제방_유형
				String[] river_lftbnk_dike_ty = new String[rowCount]; // 하천_좌안_제방_유형
				String[] tmprt = new String[rowCount]; // 기온
				String[] wttm = new String[rowCount]; // 수온
				String[] river_rhtbnk_grdnt = new String[rowCount]; // 하천_우안_경사도
				String[] river_lftbnk_grdnt = new String[rowCount]; // 하천_좌안_경사도
				String[] dstrb_fctr = new String[rowCount]; // 교란_요인
				String[] examin_mth = new String[rowCount]; // 조사_방법
				String[] rvbd_partcl = new String[rowCount]; // 하상_입자
				String[] rvbd_partcl_stn_lrge_rate = new String[rowCount]; // 하상_입자_돌_대_비율
				String[] rvbd_partcl_stn_middl_rate = new String[rowCount]; // 하상_입자_돌_중_비율
				String[] rvbd_partcl_sand_rate = new String[rowCount]; // 하상_입자_모래_비율
				String[] rvbd_partcl_pbbls_rate = new String[rowCount]; // 하상_입자_자갈_비율
				String[] rvbd_partcl_pbbls_small_rate = new String[rowCount]; // 하상_입자_자갈_소_비율
				String[] rvbd_partcl_all_rate = new String[rowCount]; // 하상_입자_전체_비율
				String[] grid_no = new String[rowCount]; // 격자_번호
				String[] fml_eng_ttle = new String[rowCount]; // 과_영문_명칭
				String[] fml_korean_ttle = new String[rowCount]; // 과_한글_명칭
				String[] dstrb_fctr_detail = new String[rowCount]; // 교란_요인_상세
				String[] etc = new String[rowCount]; // 기타
				String[] ordr_eng_ttle = new String[rowCount]; // 목_영문_명칭
				String[] ordr_korean_ttle = new String[rowCount]; // 목_한글_명칭
				String[] examin_de_frst = new String[rowCount]; // 조사_일자_1차
				String[] examin_de_scd = new String[rowCount]; // 조사_일자_2차
				String[] examin_area_ttle = new String[rowCount]; // 조사_지역_명칭
				String[] examin_odr = new String[rowCount]; // 조사_차수
				String[] spcs_schlshp_ttle = new String[rowCount]; // 종_학술_명칭
				String[] geom = new String[rowCount]; // 지오메트리

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						spce_id[i] = rs.getString(1);
						examin_year[i] = rs.getString(2);
						tme[i] = rs.getString(3);
						examin_begin_de[i] = rs.getString(4);
						examin_end_de[i] = rs.getString(5);
						gnrl_examin_charger_nm[i] = rs.getString(6);
						rspnsbl_examin_charger_nm[i] = rs.getString(7);
						spcs_korean_ttle[i] = rs.getString(8);
						spcs_eng_ttle[i] = rs.getString(9);
						idvd_qy[i] = rs.getString(10);
						partclr_matter[i] = rs.getString(11);
						lvb_abndnc_degree[i] = rs.getString(12);
						wrssm_ar[i] = rs.getString(13);
						wtsd_grdnt[i] = rs.getString(14);
						qltwtr_trbd[i] = rs.getString(15);
						dgr_lad_use[i] = rs.getString(16);
						river_ttle[i] = rs.getString(17);
						river_stle[i] = rs.getString(18);
						ar[i] = rs.getString(19);
						dpwt[i] = rs.getString(20);
						dgr_rhtbnk_lad_use_sttus[i] = rs.getString(21);
						dgr_lftbnk_lad_use_sttus[i] = rs.getString(22);
						river_rhtbnk_dike_ty[i] = rs.getString(23);
						river_lftbnk_dike_ty[i] = rs.getString(24);
						tmprt[i] = rs.getString(25);
						wttm[i] = rs.getString(26);
						river_rhtbnk_grdnt[i] = rs.getString(27);
						river_lftbnk_grdnt[i] = rs.getString(28);
						dstrb_fctr[i] = rs.getString(29);
						examin_mth[i] = rs.getString(30);
						rvbd_partcl[i] = rs.getString(31);
						rvbd_partcl_stn_lrge_rate[i] = rs.getString(32);
						rvbd_partcl_stn_middl_rate[i] = rs.getString(33);
						rvbd_partcl_sand_rate[i] = rs.getString(34);
						rvbd_partcl_pbbls_rate[i] = rs.getString(35);
						rvbd_partcl_pbbls_small_rate[i] = rs.getString(36);
						rvbd_partcl_all_rate[i] = rs.getString(37);
						grid_no[i] = rs.getString(38);
						fml_eng_ttle[i] = rs.getString(39);
						fml_korean_ttle[i] = rs.getString(40);
						dstrb_fctr_detail[i] = rs.getString(41);
						etc[i] = rs.getString(42);
						ordr_eng_ttle[i] = rs.getString(43);
						ordr_korean_ttle[i] = rs.getString(44);
						examin_de_frst[i] = rs.getString(45);
						examin_de_scd[i] = rs.getString(46);
						examin_area_ttle[i] = rs.getString(47);
						examin_odr[i] = rs.getString(48);
						spcs_schlshp_ttle[i] = rs.getString(49);
						geom[i] = rs.getString(50);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::spcs_korean_ttle::" + spcs_korean_ttle[i] + "::spcs_eng_ttle::" + spcs_eng_ttle[i]
								+ "::idvd_qy::" + idvd_qy[i] + "::partclr_matter::" + partclr_matter[i]
								+ "::lvb_abndnc_degree::" + lvb_abndnc_degree[i] + "::wrssm_ar::" + wrssm_ar[i]
								+ "::wtsd_grdnt::" + wtsd_grdnt[i] + "::qltwtr_trbd::" + qltwtr_trbd[i]
								+ "::dgr_lad_use::" + dgr_lad_use[i] + "::river_ttle::" + river_ttle[i]
								+ "::river_stle::" + river_stle[i] + "::ar::" + ar[i] + "::dpwt::" + dpwt[i]
								+ "::dgr_rhtbnk_lad_use_sttus::" + dgr_rhtbnk_lad_use_sttus[i]
								+ "::dgr_lftbnk_lad_use_sttus::" + dgr_lftbnk_lad_use_sttus[i]
								+ "::river_rhtbnk_dike_ty::" + river_rhtbnk_dike_ty[i] + "::river_lftbnk_dike_ty::"
								+ river_lftbnk_dike_ty[i] + "::tmprt::" + tmprt[i] + "::wttm::" + wttm[i]
								+ "::river_rhtbnk_grdnt::" + river_rhtbnk_grdnt[i] + "::river_lftbnk_grdnt::"
								+ river_lftbnk_grdnt[i] + "::dstrb_fctr::" + dstrb_fctr[i] + "::examin_mth::"
								+ examin_mth[i] + "::rvbd_partcl::" + rvbd_partcl[i] + "::rvbd_partcl_stn_lrge_rate::"
								+ rvbd_partcl_stn_lrge_rate[i] + "::rvbd_partcl_stn_middl_rate::"
								+ rvbd_partcl_stn_middl_rate[i] + "::rvbd_partcl_sand_rate::" + rvbd_partcl_sand_rate[i]
								+ "::rvbd_partcl_pbbls_rate::" + rvbd_partcl_pbbls_rate[i]
								+ "::rvbd_partcl_pbbls_small_rate::" + rvbd_partcl_pbbls_small_rate[i]
								+ "::rvbd_partcl_all_rate::" + rvbd_partcl_all_rate[i] + "::grid_no::" + grid_no[i]
								+ "::fml_eng_ttle::" + fml_eng_ttle[i] + "::fml_korean_ttle::" + fml_korean_ttle[i]
								+ "::dstrb_fctr_detail::" + dstrb_fctr_detail[i] + "::etc::" + etc[i]
								+ "::ordr_eng_ttle::" + ordr_eng_ttle[i] + "::ordr_korean_ttle::" + ordr_korean_ttle[i]
								+ "::examin_de_frst::" + examin_de_frst[i] + "::examin_de_scd::" + examin_de_scd[i]
								+ "::examin_area_ttle::" + examin_area_ttle[i] + "::examin_odr::" + examin_odr[i]
								+ "::spcs_schlshp_ttle::" + spcs_schlshp_ttle[i] + "::geom::" + geom[i]);

						i++;

					}
					
					System.out.println("ECO_05 SELECT 프로세스 종료.");

				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						spce_id[i] = JsonParser.colWrite_String_eic(rs.getString(1)); 
						examin_year[i] = JsonParser.colWrite_String_eic(rs.getString(2)); 
						tme[i] = JsonParser.colWrite_String_eic(rs.getString(3)); 
						examin_begin_de[i] = JsonParser.colWrite_String_eic(rs.getString(4)); 
						examin_end_de[i] = JsonParser.colWrite_String_eic(rs.getString(5)); 
						gnrl_examin_charger_nm[i] = JsonParser.colWrite_String_eic(rs.getString(6)); 
						rspnsbl_examin_charger_nm[i] = JsonParser.colWrite_String_eic(rs.getString(7)); 
						spcs_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(8)); 
						spcs_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(9)); 
						idvd_qy[i] = JsonParser.colWrite_String_eic(rs.getString(10)); 
						partclr_matter[i] = JsonParser.colWrite_String_eic(rs.getString(11)); 
						lvb_abndnc_degree[i] = JsonParser.colWrite_String_eic(rs.getString(12)); 
						wrssm_ar[i] = JsonParser.colWrite_String_eic(rs.getString(13)); 
						wtsd_grdnt[i] = JsonParser.colWrite_String_eic(rs.getString(14)); 
						qltwtr_trbd[i] = JsonParser.colWrite_String_eic(rs.getString(15)); 
						dgr_lad_use[i] = JsonParser.colWrite_String_eic(rs.getString(16)); 
						river_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(17)); 
						river_stle[i] = JsonParser.colWrite_String_eic(rs.getString(18)); 
						ar[i] = JsonParser.colWrite_String_eic(rs.getString(19)); 
						dpwt[i] = JsonParser.colWrite_String_eic(rs.getString(20));
						dgr_rhtbnk_lad_use_sttus[i] = JsonParser.colWrite_String_eic(rs.getString(21));
						dgr_lftbnk_lad_use_sttus[i] = JsonParser.colWrite_String_eic(rs.getString(22));
						river_rhtbnk_dike_ty[i] = JsonParser.colWrite_String_eic(rs.getString(23));
						river_lftbnk_dike_ty[i] = JsonParser.colWrite_String_eic(rs.getString(24));
						tmprt[i] = JsonParser.colWrite_String_eic(rs.getString(25));
						wttm[i] = JsonParser.colWrite_String_eic(rs.getString(26));
						river_rhtbnk_grdnt[i] = JsonParser.colWrite_String_eic(rs.getString(27));
						river_lftbnk_grdnt[i] = JsonParser.colWrite_String_eic(rs.getString(28));
						dstrb_fctr[i] = JsonParser.colWrite_String_eic(rs.getString(29));
						examin_mth[i] = JsonParser.colWrite_String_eic(rs.getString(30));
						rvbd_partcl[i] = JsonParser.colWrite_String_eic(rs.getString(31));
						rvbd_partcl_stn_lrge_rate[i] = JsonParser.colWrite_String_eic(rs.getString(32));
						rvbd_partcl_stn_middl_rate[i] = JsonParser.colWrite_String_eic(rs.getString(33));
						rvbd_partcl_sand_rate[i] = JsonParser.colWrite_String_eic(rs.getString(34));
						rvbd_partcl_pbbls_rate[i] = JsonParser.colWrite_String_eic(rs.getString(35));
						rvbd_partcl_pbbls_small_rate[i] = JsonParser.colWrite_String_eic(rs.getString(36));
						rvbd_partcl_all_rate[i] = JsonParser.colWrite_String_eic(rs.getString(37));
						grid_no[i] = JsonParser.colWrite_String_eic(rs.getString(38));
						fml_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(39));
						fml_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(40));
						dstrb_fctr_detail[i] = JsonParser.colWrite_String_eic(rs.getString(41));
						etc[i] = JsonParser.colWrite_String_eic(rs.getString(42));
						ordr_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(43));
						ordr_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(44));
						examin_de_frst[i] = JsonParser.colWrite_String_eic(rs.getString(45));
						examin_de_scd[i] = JsonParser.colWrite_String_eic(rs.getString(46));
						examin_area_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(47));
						examin_odr[i] = JsonParser.colWrite_String_eic(rs.getString(48));
						spcs_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(49));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(50));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(spce_id[i]);
							pw.write("|^");
							pw.write(examin_year[i]);
							pw.write("|^");
							pw.write(tme[i]);
							pw.write("|^");
							pw.write(examin_begin_de[i]);
							pw.write("|^");
							pw.write(examin_end_de[i]);
							pw.write("|^");
							pw.write(gnrl_examin_charger_nm[i]);
							pw.write("|^");
							pw.write(rspnsbl_examin_charger_nm[i]);
							pw.write("|^");
							pw.write(spcs_korean_ttle[i]);
							pw.write("|^");
							pw.write(spcs_eng_ttle[i]);
							pw.write("|^");
							pw.write(idvd_qy[i]);
							pw.write("|^");
							pw.write(partclr_matter[i]);
							pw.write("|^");
							pw.write(lvb_abndnc_degree[i]);
							pw.write("|^");
							pw.write(wrssm_ar[i]);
							pw.write("|^");
							pw.write(wtsd_grdnt[i]);
							pw.write("|^");
							pw.write(qltwtr_trbd[i]);
							pw.write("|^");
							pw.write(dgr_lad_use[i]);
							pw.write("|^");
							pw.write(river_ttle[i]);
							pw.write("|^");
							pw.write(river_stle[i]);
							pw.write("|^");
							pw.write(ar[i]);
							pw.write("|^");
							pw.write(dpwt[i]);
							pw.write("|^");
							pw.write(dgr_rhtbnk_lad_use_sttus[i]);
							pw.write("|^");
							pw.write(dgr_lftbnk_lad_use_sttus[i]);
							pw.write("|^");
							pw.write(river_rhtbnk_dike_ty[i]);
							pw.write("|^");
							pw.write(river_lftbnk_dike_ty[i]);
							pw.write("|^");
							pw.write(tmprt[i]);
							pw.write("|^");
							pw.write(wttm[i]);
							pw.write("|^");
							pw.write(river_rhtbnk_grdnt[i]);
							pw.write("|^");
							pw.write(river_lftbnk_grdnt[i]);
							pw.write("|^");
							pw.write(dstrb_fctr[i]);
							pw.write("|^");
							pw.write(examin_mth[i]);
							pw.write("|^");
							pw.write(rvbd_partcl[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_stn_lrge_rate[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_stn_middl_rate[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_sand_rate[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_pbbls_rate[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_pbbls_small_rate[i]);
							pw.write("|^");
							pw.write(rvbd_partcl_all_rate[i]);
							pw.write("|^");
							pw.write(grid_no[i]);
							pw.write("|^");
							pw.write(fml_eng_ttle[i]);
							pw.write("|^");
							pw.write(fml_korean_ttle[i]);
							pw.write("|^");
							pw.write(dstrb_fctr_detail[i]);
							pw.write("|^");
							pw.write(etc[i]);
							pw.write("|^");
							pw.write(ordr_eng_ttle[i]);
							pw.write("|^");
							pw.write(ordr_korean_ttle[i]);
							pw.write("|^");
							pw.write(examin_de_frst[i]);
							pw.write("|^");
							pw.write(examin_de_scd[i]);
							pw.write("|^");
							pw.write(examin_area_ttle[i]);
							pw.write("|^");
							pw.write(examin_odr[i]);
							pw.write("|^");
							pw.write(spcs_schlshp_ttle[i]);
							pw.write("|^");
							pw.write(geom[i]);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						i++;
						
						
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
