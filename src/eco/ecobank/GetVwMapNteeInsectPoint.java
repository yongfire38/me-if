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

			try {

				Class.forName(DBConnection.getProperty("eco_post_driver"));
				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco08_query");
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
				String[] wethr = new String[rowCount]; // 날씨
				String[] idmn_nm = new String[rowCount]; // 동정자_성명
				String[] al = new String[rowCount]; // 표고
				String[] observ_cn = new String[rowCount]; // 관찰_내용
				String[] ecsystm_ty = new String[rowCount]; // 생태계_유형
				String[] examin_mtn = new String[rowCount]; // 조사_방법
				String[] examin_time = new String[rowCount]; // 조사_시간
				String[] examin_area_lc = new String[rowCount]; // 조사_지역_위치
				String[] lvb_resrce_prcuse_at = new String[rowCount]; // 생물_자원_활용_여부
				String[] spcs_irds_trnd = new String[rowCount]; // 종_증감_추세
				String[] cllc_mth = new String[rowCount]; // 채집_방법
				String[] examin_area = new String[rowCount]; // 조사_지역
				String[] insect_observ_imago_qy = new String[rowCount]; // 곤충_과찰_성충_수
				String[] insect_observ_sbmg_qy = new String[rowCount]; // 곤충_관찰_아성충_수
				String[] insect_observ_egg_qy = new String[rowCount]; // 곤충_관찰_알_수
				String[] insect_observ_lavl_qy = new String[rowCount]; // 곤충_관찰_유충_수
				String[] fml_schlshp_ttle = new String[rowCount]; // 과_학술_명칭
				String[] fml_korean_ttle = new String[rowCount]; // 과_한글_명칭
				String[] wethr_clr_at = new String[rowCount]; // 날씨_맑음_여부
				String[] wethr_cldy_at = new String[rowCount]; // 날씨_흐림_여부
				String[] wethr_rain_at = new String[rowCount]; // 날씨_비_여부
				String[] ordr_schlshp_ttle = new String[rowCount]; // 목_학술_명칭
				String[] ordr_korean_ttle = new String[rowCount]; // 목_한글_명칭
				String[] updt_matter = new String[rowCount]; // 수정_사항
				String[] examin_pd = new String[rowCount]; // 조사_기간
				String[] examin_tpgrph = new String[rowCount]; // 조사_지형
				String[] examin_tpgrph_etc = new String[rowCount]; // 조사_지형_기타
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
						wethr[i] = rs.getString(12);
						idmn_nm[i] = rs.getString(13);
						al[i] = rs.getString(14);
						observ_cn[i] = rs.getString(15);
						ecsystm_ty[i] = rs.getString(16);
						examin_mtn[i] = rs.getString(17);
						examin_time[i] = rs.getString(18);
						examin_area_lc[i] = rs.getString(19);
						lvb_resrce_prcuse_at[i] = rs.getString(20);
						spcs_irds_trnd[i] = rs.getString(21);
						cllc_mth[i] = rs.getString(22);
						examin_area[i] = rs.getString(23);
						insect_observ_imago_qy[i] = rs.getString(24);
						insect_observ_sbmg_qy[i] = rs.getString(25);
						insect_observ_egg_qy[i] = rs.getString(26);
						insect_observ_lavl_qy[i] = rs.getString(27);
						fml_schlshp_ttle[i] = rs.getString(28);
						fml_korean_ttle[i] = rs.getString(29);
						wethr_clr_at[i] = rs.getString(30);
						wethr_cldy_at[i] = rs.getString(31);
						wethr_rain_at[i] = rs.getString(32);
						ordr_schlshp_ttle[i] = rs.getString(33);
						ordr_korean_ttle[i] = rs.getString(34);
						updt_matter[i] = rs.getString(35);
						examin_pd[i] = rs.getString(36);
						examin_tpgrph[i] = rs.getString(37);
						examin_tpgrph_etc[i] = rs.getString(38);
						spcs_schlshp_ttle[i] = rs.getString(39);
						geom[i] = rs.getString(40);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::spcs_korean_ttle::" + spcs_korean_ttle[i] + "::spcs_eng_ttle::" + spcs_eng_ttle[i]
								+ "::idvd_qy::" + idvd_qy[i] + "::partclr_matter::" + partclr_matter[i] + "::wethr::"
								+ wethr[i] + "::idmn_nm::" + idmn_nm[i] + "::al::" + al[i] + "::observ_cn::"
								+ observ_cn[i] + "::ecsystm_ty::" + ecsystm_ty[i] + "::examin_mtn::" + examin_mtn[i]
								+ "::examin_time::" + examin_time[i] + "::examin_area_lc::" + examin_area_lc[i]
								+ "::lvb_resrce_prcuse_at::" + lvb_resrce_prcuse_at[i] + "::spcs_irds_trnd::"
								+ spcs_irds_trnd[i] + "::cllc_mth::" + cllc_mth[i] + "::examin_area::" + examin_area[i]
								+ "::insect_observ_imago_qy::" + insect_observ_imago_qy[i] + "::insect_observ_sbmg_qy::"
								+ insect_observ_sbmg_qy[i] + "::insect_observ_egg_qy::" + insect_observ_egg_qy[i]
								+ "::insect_observ_lavl_qy::" + insect_observ_lavl_qy[i] + "::fml_schlshp_ttle::"
								+ fml_schlshp_ttle[i] + "::fml_korean_ttle::" + fml_korean_ttle[i] + "::wethr_clr_at::"
								+ wethr_clr_at[i] + "::wethr_cldy_at::" + wethr_cldy_at[i] + "::wethr_rain_at::"
								+ wethr_rain_at[i] + "::ordr_schlshp_ttle::" + ordr_schlshp_ttle[i]
								+ "::ordr_korean_ttle::" + ordr_korean_ttle[i] + "::updt_matter::" + updt_matter[i]
								+ "::examin_pd::" + examin_pd[i] + "::examin_tpgrph::" + examin_tpgrph[i]
								+ "::examin_tpgrph_etc::" + examin_tpgrph_etc[i] + "::spcs_schlshp_ttle::"
								+ spcs_schlshp_ttle[i] + "::geom::" + geom[i]);

						i++;

					}

					System.out.println("ECO_08 SELECT 프로세스 종료.");

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
						wethr[i] = JsonParser.colWrite_String_eic(rs.getString(12)); 
						idmn_nm[i] = JsonParser.colWrite_String_eic(rs.getString(13)); 
						al[i] = JsonParser.colWrite_String_eic(rs.getString(14)); 
						observ_cn[i] = JsonParser.colWrite_String_eic(rs.getString(15)); 
						ecsystm_ty[i] = JsonParser.colWrite_String_eic(rs.getString(16)); 
						examin_mtn[i] = JsonParser.colWrite_String_eic(rs.getString(17)); 
						examin_time[i] = JsonParser.colWrite_String_eic(rs.getString(18)); 
						examin_area_lc[i] = JsonParser.colWrite_String_eic(rs.getString(19)); 
						lvb_resrce_prcuse_at[i] = JsonParser.colWrite_String_eic(rs.getString(20)); 
						spcs_irds_trnd[i] = JsonParser.colWrite_String_eic(rs.getString(21)); 
						cllc_mth[i] = JsonParser.colWrite_String_eic(rs.getString(22)); 
						examin_area[i] = JsonParser.colWrite_String_eic(rs.getString(23)); 
						insect_observ_imago_qy[i] = JsonParser.colWrite_String_eic(rs.getString(24)); 
						insect_observ_sbmg_qy[i] = JsonParser.colWrite_String_eic(rs.getString(25)); 
						insect_observ_egg_qy[i] = JsonParser.colWrite_String_eic(rs.getString(26)); 
						insect_observ_lavl_qy[i] = JsonParser.colWrite_String_eic(rs.getString(27)); 
						fml_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(28)); 
						fml_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(29)); 
						wethr_clr_at[i] = JsonParser.colWrite_String_eic(rs.getString(30)); 
						wethr_cldy_at[i] = JsonParser.colWrite_String_eic(rs.getString(31)); 
						wethr_rain_at[i] = JsonParser.colWrite_String_eic(rs.getString(32)); 
						ordr_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(33)); 
						ordr_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(34)); 
						updt_matter[i] = JsonParser.colWrite_String_eic(rs.getString(35)); 
						examin_pd[i] = JsonParser.colWrite_String_eic(rs.getString(36)); 
						examin_tpgrph[i] = JsonParser.colWrite_String_eic(rs.getString(37)); 
						examin_tpgrph_etc[i] = JsonParser.colWrite_String_eic(rs.getString(38)); 
						spcs_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(39)); 
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(40)); 
						
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
							pw.write(wethr[i]);
							pw.write("|^");
							pw.write(idmn_nm[i]);
							pw.write("|^");
							pw.write(al[i]);
							pw.write("|^");
							pw.write(observ_cn[i]);
							pw.write("|^");
							pw.write(ecsystm_ty[i]);
							pw.write("|^");
							pw.write(examin_mtn[i]);
							pw.write("|^");
							pw.write(examin_time[i]);
							pw.write("|^");
							pw.write(examin_area_lc[i]);
							pw.write("|^");
							pw.write(lvb_resrce_prcuse_at[i]);
							pw.write("|^");
							pw.write(spcs_irds_trnd[i]);
							pw.write("|^");
							pw.write(cllc_mth[i]);
							pw.write("|^");
							pw.write(examin_area[i]);
							pw.write("|^");
							pw.write(insect_observ_imago_qy[i]);
							pw.write("|^");
							pw.write(insect_observ_sbmg_qy[i]);
							pw.write("|^");
							pw.write(insect_observ_egg_qy[i]);
							pw.write("|^");
							pw.write(insect_observ_lavl_qy[i]);
							pw.write("|^");
							pw.write(fml_schlshp_ttle[i]);
							pw.write("|^");
							pw.write(fml_korean_ttle[i]);
							pw.write("|^");
							pw.write(wethr_clr_at[i]);
							pw.write("|^");
							pw.write(wethr_cldy_at[i]);
							pw.write("|^");
							pw.write(wethr_rain_at[i]);
							pw.write("|^");
							pw.write(ordr_schlshp_ttle[i]);
							pw.write("|^");
							pw.write(ordr_korean_ttle[i]);
							pw.write("|^");
							pw.write(updt_matter[i]);
							pw.write("|^");
							pw.write(examin_pd[i]);
							pw.write("|^");
							pw.write(examin_tpgrph[i]);
							pw.write("|^");
							pw.write(examin_tpgrph_etc[i]);
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

					System.out.println("ECO_08 SELECT 파일 생성 프로세스 종료.");

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
