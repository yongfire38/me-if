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

public class GetVwMapNteeInsectPoint {

	// 에코뱅크 - 곤충_점
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			// sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco08_query");
				System.out.println("query :::" + query);

				conn.setAutoCommit(false);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(1);

				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");

				rs.setFetchSize(1);

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				/*
				 * rs.last();
				 * 
				 * int rowCount = rs.getRow();
				 * 
				 * System.out.println("전체 건 수 :::" + Integer.toString(rowCount)
				 * + " 건");
				 * 
				 * // 다시 처음부터 조회해야 하므로 커서는 초기화 rs.beforeFirst();
				 */

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
						String wethr = " "; // 날씨
						String idmn_nm = " "; // 동정자_성명
						String al = " "; // 표고
						String observ_cn = " "; // 관찰_내용
						String ecsystm_ty = " "; // 생태계_유형
						String examin_mtn = " "; // 조사_방법
						String examin_time = " "; // 조사_시간
						String examin_area_lc = " "; // 조사_지역_위치
						String lvb_resrce_prcuse_at = " "; // 생물_자원_활용_여부
						String spcs_irds_trnd = " "; // 종_증감_추세
						String cllc_mth = " "; // 채집_방법
						String examin_area = " "; // 조사_지역
						String insect_observ_imago_qy = " "; // 곤충_과찰_성충_수
						String insect_observ_sbmg_qy = " "; // 곤충_관찰_아성충_수
						String insect_observ_egg_qy = " "; // 곤충_관찰_알_수
						String insect_observ_lavl_qy = " "; // 곤충_관찰_유충_수
						String fml_schlshp_ttle = " "; // 과_학술_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String wethr_clr_at = " "; // 날씨_맑음_여부
						String wethr_cldy_at = " "; // 날씨_흐림_여부
						String wethr_rain_at = " "; // 날씨_비_여부
						String ordr_schlshp_ttle = " "; // 목_학술_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String updt_matter = " "; // 수정_사항
						String examin_pd = " "; // 조사_기간
						String examin_tpgrph = " "; // 조사_지형
						String examin_tpgrph_etc = " "; // 조사_지형_기타
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
						wethr = rs.getString(12);
						idmn_nm = rs.getString(13);
						al = rs.getString(14);
						observ_cn = rs.getString(15);
						ecsystm_ty = rs.getString(16);
						examin_mtn = rs.getString(17);
						examin_time = rs.getString(18);
						examin_area_lc = rs.getString(19);
						lvb_resrce_prcuse_at = rs.getString(20);
						spcs_irds_trnd = rs.getString(21);
						cllc_mth = rs.getString(22);
						examin_area = rs.getString(23);
						insect_observ_imago_qy = rs.getString(24);
						insect_observ_sbmg_qy = rs.getString(25);
						insect_observ_egg_qy = rs.getString(26);
						insect_observ_lavl_qy = rs.getString(27);
						fml_schlshp_ttle = rs.getString(28);
						fml_korean_ttle = rs.getString(29);
						wethr_clr_at = rs.getString(30);
						wethr_cldy_at = rs.getString(31);
						wethr_rain_at = rs.getString(32);
						ordr_schlshp_ttle = rs.getString(33);
						ordr_korean_ttle = rs.getString(34);
						updt_matter = rs.getString(35);
						examin_pd = rs.getString(36);
						examin_tpgrph = rs.getString(37);
						examin_tpgrph_etc = rs.getString(38);
						spcs_schlshp_ttle = rs.getString(39);
						// geom = rs.getString(40);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::" + tme
								+ "::examin_begin_de::" + examin_begin_de + "::examin_end_de::" + examin_end_de
								+ "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm + "::spcs_korean_ttle::"
								+ spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle + "::idvd_qy::" + idvd_qy
								+ "::partclr_matter::" + partclr_matter + "::wethr::" + wethr + "::idmn_nm::" + idmn_nm
								+ "::al::" + al + "::observ_cn::" + observ_cn + "::ecsystm_ty::" + ecsystm_ty
								+ "::examin_mtn::" + examin_mtn + "::examin_time::" + examin_time + "::examin_area_lc::"
								+ examin_area_lc + "::lvb_resrce_prcuse_at::" + lvb_resrce_prcuse_at
								+ "::spcs_irds_trnd::" + spcs_irds_trnd + "::cllc_mth::" + cllc_mth + "::examin_area::"
								+ examin_area + "::insect_observ_imago_qy::" + insect_observ_imago_qy
								+ "::insect_observ_sbmg_qy::" + insect_observ_sbmg_qy + "::insect_observ_egg_qy::"
								+ insect_observ_egg_qy + "::insect_observ_lavl_qy::" + insect_observ_lavl_qy
								+ "::fml_schlshp_ttle::" + fml_schlshp_ttle + "::fml_korean_ttle::" + fml_korean_ttle
								+ "::wethr_clr_at::" + wethr_clr_at + "::wethr_cldy_at::" + wethr_cldy_at
								+ "::wethr_rain_at::" + wethr_rain_at + "::ordr_schlshp_ttle::" + ordr_schlshp_ttle
								+ "::ordr_korean_ttle::" + ordr_korean_ttle + "::updt_matter::" + updt_matter
								+ "::examin_pd::" + examin_pd + "::examin_tpgrph::" + examin_tpgrph
								+ "::examin_tpgrph_etc::" + examin_tpgrph_etc + "::spcs_schlshp_ttle::"
								+ spcs_schlshp_ttle + "::geom::" + geom);

					}

					System.out.println("ECO_08 SELECT 프로세스 종료.");

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
						String wethr = " "; // 날씨
						String idmn_nm = " "; // 동정자_성명
						String al = " "; // 표고
						String observ_cn = " "; // 관찰_내용
						String ecsystm_ty = " "; // 생태계_유형
						String examin_mtn = " "; // 조사_방법
						String examin_time = " "; // 조사_시간
						String examin_area_lc = " "; // 조사_지역_위치
						String lvb_resrce_prcuse_at = " "; // 생물_자원_활용_여부
						String spcs_irds_trnd = " "; // 종_증감_추세
						String cllc_mth = " "; // 채집_방법
						String examin_area = " "; // 조사_지역
						String insect_observ_imago_qy = " "; // 곤충_과찰_성충_수
						String insect_observ_sbmg_qy = " "; // 곤충_관찰_아성충_수
						String insect_observ_egg_qy = " "; // 곤충_관찰_알_수
						String insect_observ_lavl_qy = " "; // 곤충_관찰_유충_수
						String fml_schlshp_ttle = " "; // 과_학술_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String wethr_clr_at = " "; // 날씨_맑음_여부
						String wethr_cldy_at = " "; // 날씨_흐림_여부
						String wethr_rain_at = " "; // 날씨_비_여부
						String ordr_schlshp_ttle = " "; // 목_학술_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String updt_matter = " "; // 수정_사항
						String examin_pd = " "; // 조사_기간
						String examin_tpgrph = " "; // 조사_지형
						String examin_tpgrph_etc = " "; // 조사_지형_기타
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
						wethr = JsonParser.colWrite_String_eic(rs.getString(12));
						idmn_nm = JsonParser.colWrite_String_eic(rs.getString(13));
						al = JsonParser.colWrite_String_eic(rs.getString(14));
						observ_cn = JsonParser.colWrite_String_eic(rs.getString(15));
						ecsystm_ty = JsonParser.colWrite_String_eic(rs.getString(16));
						examin_mtn = JsonParser.colWrite_String_eic(rs.getString(17));
						examin_time = JsonParser.colWrite_String_eic(rs.getString(18));
						examin_area_lc = JsonParser.colWrite_String_eic(rs.getString(19));
						lvb_resrce_prcuse_at = JsonParser.colWrite_String_eic(rs.getString(20));
						spcs_irds_trnd = JsonParser.colWrite_String_eic(rs.getString(21));
						cllc_mth = JsonParser.colWrite_String_eic(rs.getString(22));
						examin_area = JsonParser.colWrite_String_eic(rs.getString(23));
						insect_observ_imago_qy = JsonParser.colWrite_String_eic(rs.getString(24));
						insect_observ_sbmg_qy = JsonParser.colWrite_String_eic(rs.getString(25));
						insect_observ_egg_qy = JsonParser.colWrite_String_eic(rs.getString(26));
						insect_observ_lavl_qy = JsonParser.colWrite_String_eic(rs.getString(27));
						fml_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(28));
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(29));
						wethr_clr_at = JsonParser.colWrite_String_eic(rs.getString(30));
						wethr_cldy_at = JsonParser.colWrite_String_eic(rs.getString(31));
						wethr_rain_at = JsonParser.colWrite_String_eic(rs.getString(32));
						ordr_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(33));
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(34));
						updt_matter = JsonParser.colWrite_String_eic(rs.getString(35));
						examin_pd = JsonParser.colWrite_String_eic(rs.getString(36));
						examin_tpgrph = JsonParser.colWrite_String_eic(rs.getString(37));
						examin_tpgrph_etc = JsonParser.colWrite_String_eic(rs.getString(38));
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(39));
						// geom =
						// JsonParser.colWrite_String_eic(rs.getString(40));

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
							pw.write(wethr);
							pw.write("|^");
							pw.write(idmn_nm);
							pw.write("|^");
							pw.write(al);
							pw.write("|^");
							pw.write(observ_cn);
							pw.write("|^");
							pw.write(ecsystm_ty);
							pw.write("|^");
							pw.write(examin_mtn);
							pw.write("|^");
							pw.write(examin_time);
							pw.write("|^");
							pw.write(examin_area_lc);
							pw.write("|^");
							pw.write(lvb_resrce_prcuse_at);
							pw.write("|^");
							pw.write(spcs_irds_trnd);
							pw.write("|^");
							pw.write(cllc_mth);
							pw.write("|^");
							pw.write(examin_area);
							pw.write("|^");
							pw.write(insect_observ_imago_qy);
							pw.write("|^");
							pw.write(insect_observ_sbmg_qy);
							pw.write("|^");
							pw.write(insect_observ_egg_qy);
							pw.write("|^");
							pw.write(insect_observ_lavl_qy);
							pw.write("|^");
							pw.write(fml_schlshp_ttle);
							pw.write("|^");
							pw.write(fml_korean_ttle);
							pw.write("|^");
							pw.write(wethr_clr_at);
							pw.write("|^");
							pw.write(wethr_cldy_at);
							pw.write("|^");
							pw.write(wethr_rain_at);
							pw.write("|^");
							pw.write(ordr_schlshp_ttle);
							pw.write("|^");
							pw.write(ordr_korean_ttle);
							pw.write("|^");
							pw.write(updt_matter);
							pw.write("|^");
							pw.write(examin_pd);
							pw.write("|^");
							pw.write(examin_tpgrph);
							pw.write("|^");
							pw.write(examin_tpgrph_etc);
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

						// System.out.println("진행도 :::" +
						// Integer.toString(rs.getRow()) + "/" +
						// Integer.toString(rowCount) + " 건");
						System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
					}

					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("ECO_08 SELECT 파일 생성 프로세스 종료.");

				}

			} catch (SQLException sqle) {

				System.out.println("SELECT문에서 예외 발생");

				cf = "Y";
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

				// 쿼리에서 에러 발생시에는 종료로 빠진다.
				if (cf.equals("Y")) {
					System.exit(-1);
				}

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
