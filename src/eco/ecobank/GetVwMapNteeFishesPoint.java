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

public class GetVwMapNteeFishesPoint {

	// 에코뱅크 - 어류_점
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
				String query = DBConnection.getProperty("eco_post_eco06_query");
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
				String[] photo_dc = new String[rowCount]; // 사진_설명
				String[] photo_file_ttle = new String[rowCount]; // 사진_파일_명칭
				String[] ar = new String[rowCount]; // 면적
				String[] sml_envrn = new String[rowCount]; // 미소_환경
				String[] wrssm_ar = new String[rowCount]; // 수계_면적
				String[] wtsd_grdnt = new String[rowCount]; // 수변_경사도
				String[] wtsd_vtn = new String[rowCount]; // 수변_식생
				String[] dpwt = new String[rowCount]; // 수심
				String[] undw_plnt = new String[rowCount]; // 수중_식물
				String[] qltwtr_trbd = new String[rowCount]; // 수질_탁도
				String[] dgr_rhtbnk_lad_use_sttus = new String[rowCount]; // 유역_우안_토지_이용
				String[] dgr_lftbnk_lad_use_sttus = new String[rowCount]; // 유역_좌안_토지_이용
				String[] dgr_lad_use = new String[rowCount]; // 유역_토지_이용
				String[] floor_sml_envrn = new String[rowCount]; // 층_미소_환경
				String[] river_ttle = new String[rowCount]; // 하천_명칭
				String[] river_rhtbnk_dike_ty = new String[rowCount]; // 하천_우안_제방_유형
				String[] river_lftbnk_dike_ty = new String[rowCount]; // 하천_좌안_제방_유형
				String[] river_stle = new String[rowCount]; // 하천_형태
				String[] dstrb_fctr = new String[rowCount]; // 교란_요인
				String[] capt_etc = new String[rowCount]; // 포획_기타
				String[] capt_sknt = new String[rowCount]; // 포획_족대
				String[] capt_csnt = new String[rowCount]; // 포획_투망
				String[] rvbd_sml_envrn = new String[rowCount]; // 하상_미소_환경
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
				String[] ordr_eng_ttle = new String[rowCount]; // 목_영문_명칭
				String[] ordr_korean_ttle = new String[rowCount]; // 목_한글_명칭
				String[] runwt_bt = new String[rowCount]; // 유수_폭
				String[] dgr_river_bt = new String[rowCount]; // 유역_하천_폭
				String[] examin_de_frst = new String[rowCount]; // 조사_일자_1차
				String[] examin_de_scd = new String[rowCount]; // 조사_일자_2차
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
						photo_dc[i] = rs.getString(12);
						photo_file_ttle[i] = rs.getString(13);
						ar[i] = rs.getString(14);
						sml_envrn[i] = rs.getString(15);
						wrssm_ar[i] = rs.getString(16);
						wtsd_grdnt[i] = rs.getString(17);
						wtsd_vtn[i] = rs.getString(18);
						dpwt[i] = rs.getString(19);
						undw_plnt[i] = rs.getString(20);
						qltwtr_trbd[i] = rs.getString(21);
						dgr_rhtbnk_lad_use_sttus[i] = rs.getString(22);
						dgr_lftbnk_lad_use_sttus[i] = rs.getString(23);
						dgr_lad_use[i] = rs.getString(24);
						floor_sml_envrn[i] = rs.getString(25);
						river_ttle[i] = rs.getString(26);
						river_rhtbnk_dike_ty[i] = rs.getString(27);
						river_lftbnk_dike_ty[i] = rs.getString(28);
						river_stle[i] = rs.getString(29);
						dstrb_fctr[i] = rs.getString(30);
						capt_etc[i] = rs.getString(31);
						capt_sknt[i] = rs.getString(32);
						capt_csnt[i] = rs.getString(33);
						rvbd_sml_envrn[i] = rs.getString(34);
						rvbd_partcl[i] = rs.getString(35);
						rvbd_partcl_stn_lrge_rate[i] = rs.getString(36);
						rvbd_partcl_stn_middl_rate[i] = rs.getString(37);
						rvbd_partcl_sand_rate[i] = rs.getString(38);
						rvbd_partcl_pbbls_rate[i] = rs.getString(39);
						rvbd_partcl_pbbls_small_rate[i] = rs.getString(40);
						rvbd_partcl_all_rate[i] = rs.getString(41);
						grid_no[i] = rs.getString(42);
						fml_eng_ttle[i] = rs.getString(43);
						fml_korean_ttle[i] = rs.getString(44);
						ordr_eng_ttle[i] = rs.getString(45);
						ordr_korean_ttle[i] = rs.getString(46);
						runwt_bt[i] = rs.getString(47);
						dgr_river_bt[i] = rs.getString(48);
						examin_de_frst[i] = rs.getString(49);
						examin_de_scd[i] = rs.getString(50);
						examin_odr[i] = rs.getString(51);
						spcs_schlshp_ttle[i] = rs.getString(52);
						geom[i] = rs.getString(53);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::spcs_korean_ttle::" + spcs_korean_ttle[i] + "::spcs_eng_ttle::" + spcs_eng_ttle[i]
								+ "::idvd_qy::" + idvd_qy[i] + "::partclr_matter::" + partclr_matter[i] + "::photo_dc::"
								+ photo_dc[i] + "::photo_file_ttle::" + photo_file_ttle[i] + "::ar::" + ar[i]
								+ "::sml_envrn::" + sml_envrn[i] + "::wrssm_ar::" + wrssm_ar[i] + "::wtsd_grdnt::"
								+ wtsd_grdnt[i] + "::wtsd_vtn::" + wtsd_vtn[i] + "::dpwt::" + dpwt[i] + "::undw_plnt::"
								+ undw_plnt[i] + "::qltwtr_trbd::" + qltwtr_trbd[i] + "::dgr_rhtbnk_lad_use_sttus::"
								+ dgr_rhtbnk_lad_use_sttus[i] + "::dgr_lftbnk_lad_use_sttus::"
								+ dgr_lftbnk_lad_use_sttus[i] + "::dgr_lad_use::" + dgr_lad_use[i]
								+ "::floor_sml_envrn::" + floor_sml_envrn[i] + "::river_ttle::" + river_ttle[i]
								+ "::river_rhtbnk_dike_ty::" + river_rhtbnk_dike_ty[i] + "::river_lftbnk_dike_ty::"
								+ river_lftbnk_dike_ty[i] + "::river_stle::" + river_stle[i] + "::dstrb_fctr::"
								+ dstrb_fctr[i] + "::capt_etc::" + capt_etc[i] + "::capt_sknt::" + capt_sknt[i]
								+ "::capt_csnt::" + capt_csnt[i] + "::rvbd_sml_envrn::" + rvbd_sml_envrn[i]
								+ "::rvbd_partcl::" + rvbd_partcl[i] + "::rvbd_partcl_stn_lrge_rate::"
								+ rvbd_partcl_stn_lrge_rate[i] + "::rvbd_partcl_stn_middl_rate::"
								+ rvbd_partcl_stn_middl_rate[i] + "::rvbd_partcl_sand_rate::" + rvbd_partcl_sand_rate[i]
								+ "::rvbd_partcl_pbbls_rate::" + rvbd_partcl_pbbls_rate[i]
								+ "::rvbd_partcl_pbbls_small_rate::" + rvbd_partcl_pbbls_small_rate[i]
								+ "::rvbd_partcl_all_rate::" + rvbd_partcl_all_rate[i] + "::grid_no::" + grid_no[i]
								+ "::fml_eng_ttle::" + fml_eng_ttle[i] + "::fml_korean_ttle::" + fml_korean_ttle[i]
								+ "::ordr_eng_ttle::" + ordr_eng_ttle[i] + "::ordr_korean_ttle::" + ordr_korean_ttle[i]
								+ "::runwt_bt::" + runwt_bt[i] + "::dgr_river_bt::" + dgr_river_bt[i]
								+ "::examin_de_frst::" + examin_de_frst[i] + "::examin_de_scd::" + examin_de_scd[i]
								+ "::examin_odr::" + examin_odr[i] + "::spcs_schlshp_ttle::" + spcs_schlshp_ttle[i]
								+ "::geom::" + geom[i]);

						i++;

					}
					
					System.out.println("ECO_06 SELECT 프로세스 종료.");

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
						photo_dc[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						photo_file_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						ar[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						sml_envrn[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						wrssm_ar[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						wtsd_grdnt[i] = JsonParser.colWrite_String_eic(rs.getString(17));
						wtsd_vtn[i] = JsonParser.colWrite_String_eic(rs.getString(18));
						dpwt[i] = JsonParser.colWrite_String_eic(rs.getString(19));
						undw_plnt[i] = JsonParser.colWrite_String_eic(rs.getString(20));
						qltwtr_trbd[i] = JsonParser.colWrite_String_eic(rs.getString(21));
						dgr_rhtbnk_lad_use_sttus[i] = JsonParser.colWrite_String_eic(rs.getString(22));
						dgr_lftbnk_lad_use_sttus[i] = JsonParser.colWrite_String_eic(rs.getString(23));
						dgr_lad_use[i] = JsonParser.colWrite_String_eic(rs.getString(24));
						floor_sml_envrn[i] = JsonParser.colWrite_String_eic(rs.getString(25));
						river_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(26));
						river_rhtbnk_dike_ty[i] = JsonParser.colWrite_String_eic(rs.getString(27));
						river_lftbnk_dike_ty[i] = JsonParser.colWrite_String_eic(rs.getString(28));
						river_stle[i] = JsonParser.colWrite_String_eic(rs.getString(29));
						dstrb_fctr[i] = JsonParser.colWrite_String_eic(rs.getString(30));
						capt_etc[i] = JsonParser.colWrite_String_eic(rs.getString(31));
						capt_sknt[i] = JsonParser.colWrite_String_eic(rs.getString(32));
						capt_csnt[i] = JsonParser.colWrite_String_eic(rs.getString(33));
						rvbd_sml_envrn[i] = JsonParser.colWrite_String_eic(rs.getString(34));
						rvbd_partcl[i] = JsonParser.colWrite_String_eic(rs.getString(35));
						rvbd_partcl_stn_lrge_rate[i] = JsonParser.colWrite_String_eic(rs.getString(36));
						rvbd_partcl_stn_middl_rate[i] = JsonParser.colWrite_String_eic(rs.getString(37));
						rvbd_partcl_sand_rate[i] = JsonParser.colWrite_String_eic(rs.getString(38));
						rvbd_partcl_pbbls_rate[i] = JsonParser.colWrite_String_eic(rs.getString(39));
						rvbd_partcl_pbbls_small_rate[i] = JsonParser.colWrite_String_eic(rs.getString(40));
						rvbd_partcl_all_rate[i] = JsonParser.colWrite_String_eic(rs.getString(41));
						grid_no[i] = JsonParser.colWrite_String_eic(rs.getString(42));
						fml_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(43));
						fml_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(44));
						ordr_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(45));
						ordr_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(46));
						runwt_bt[i] = JsonParser.colWrite_String_eic(rs.getString(47));
						dgr_river_bt[i] = JsonParser.colWrite_String_eic(rs.getString(48));
						examin_de_frst[i] = JsonParser.colWrite_String_eic(rs.getString(49));
						examin_de_scd[i] = JsonParser.colWrite_String_eic(rs.getString(50));
						examin_odr[i] = JsonParser.colWrite_String_eic(rs.getString(51));
						spcs_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(52));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(53));
						
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
							pw.write(photo_dc[i]);
							pw.write("|^");
							pw.write(photo_file_ttle[i]);
							pw.write("|^");
							pw.write(ar[i]);
							pw.write("|^");
							pw.write(sml_envrn[i]);
							pw.write("|^");
							pw.write(wrssm_ar[i]);
							pw.write("|^");
							pw.write(wtsd_grdnt[i]);
							pw.write("|^");
							pw.write(wtsd_vtn[i]);
							pw.write("|^");
							pw.write(dpwt[i]);
							pw.write("|^");
							pw.write(undw_plnt[i]);
							pw.write("|^");
							pw.write(qltwtr_trbd[i]);
							pw.write("|^");
							pw.write(dgr_rhtbnk_lad_use_sttus[i]);
							pw.write("|^");
							pw.write(dgr_lftbnk_lad_use_sttus[i]);
							pw.write("|^");
							pw.write(dgr_lad_use[i]);
							pw.write("|^");
							pw.write(floor_sml_envrn[i]);
							pw.write("|^");
							pw.write(river_ttle[i]);
							pw.write("|^");
							pw.write(river_rhtbnk_dike_ty[i]);
							pw.write("|^");
							pw.write(river_lftbnk_dike_ty[i]);
							pw.write("|^");
							pw.write(river_stle[i]);
							pw.write("|^");
							pw.write(dstrb_fctr[i]);
							pw.write("|^");
							pw.write(capt_etc[i]);
							pw.write("|^");
							pw.write(capt_sknt[i]);
							pw.write("|^");
							pw.write(capt_csnt[i]);
							pw.write("|^");
							pw.write(rvbd_sml_envrn[i]);
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
							pw.write(ordr_eng_ttle[i]);
							pw.write("|^");
							pw.write(ordr_korean_ttle[i]);
							pw.write("|^");
							pw.write(runwt_bt[i]);
							pw.write("|^");
							pw.write(dgr_river_bt[i]);
							pw.write("|^");
							pw.write(examin_de_frst[i]);
							pw.write("|^");
							pw.write(examin_de_scd[i]);
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

					System.out.println("ECO_06 SELECT 파일 생성 프로세스 종료.");

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
