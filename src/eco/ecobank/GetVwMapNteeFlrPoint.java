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

public class GetVwMapNteeFlrPoint {

	// 에코뱅크 - 식물상_점
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
				String query = DBConnection.getProperty("eco_post_eco07_query");
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
				String[] pmntn_dstnc = new String[rowCount]; // 등산로_거리
				String[] format_envrn = new String[rowCount]; // 서식_환경
				String[] plnt_stle = new String[rowCount]; // 식물_형태
				String[] idnt_charge_nm = new String[rowCount]; // 동정_담당자_성명
				String[] idnt_de = new String[rowCount]; // 동정_일자
				String[] dfnse = new String[rowCount]; // 방위
				String[] distrb_aspc = new String[rowCount]; // 분포_양상
				String[] host = new String[rowCount]; // 숙주
				String[] plnt_prpgt_mth = new String[rowCount]; // 식물_번식_방법
				String[] plt_at = new String[rowCount]; // 식재_여부
				String[] rsrch_prjct_ttle = new String[rowCount]; // 연구_프로젝트_명칭
				String[] soil_plntn_cnd = new String[rowCount]; // 토양_수분_조건
				String[] ntsp_plnt_at = new String[rowCount]; // 토종_식물_여부
				String[] format_cln_ttle = new String[rowCount]; // 서식_군락_명칭
				String[] evnfm_dgree = new String[rowCount]; // 환경훼손_정도
				String[] vertcl_distrb_cnds = new String[rowCount]; // 수직_분포_연속성
				String[] hrzntlty_distrb_cnds = new String[rowCount]; // 수평_분포_연속성
				String[] plnt_distrb_ar = new String[rowCount]; // 식물_분포_면적
				String[] plnt_distrb_al = new String[rowCount]; // 식물_분포_표고
				String[] etc_examin_charger_nm = new String[rowCount]; // 기타_조사_담당자_성명
				String[] reprsnt_mntn = new String[rowCount]; // 대표_산
				String[] ecsystm_ty = new String[rowCount]; // 생태계_유형
				String[] examin_cours_adres_input = new String[rowCount]; // 조사_경로_주소_입력
				String[] examin_cours_direct_input = new String[rowCount]; // 조사_경로_직접_입력
				String[] resrce_use = new String[rowCount]; // 자원_이용
				String[] mtrty_indvd_qy = new String[rowCount]; // 성숙_개체_수
				String[] cllc_charger_nm = new String[rowCount]; // 채집_담당자_성명
				String[] antcty = new String[rowCount]; // 고도
				String[] fml_eng_ttle = new String[rowCount]; // 과_영문_명칭
				String[] fml_korean_ttle = new String[rowCount]; // 과_한글_명칭
				String[] note_examin = new String[rowCount]; // 노트_조사
				String[] ordr_eng_ttle = new String[rowCount]; // 목_영문_명칭
				String[] ordr_korean_ttle = new String[rowCount]; // 목_한글_명칭
				String[] flr_flwr_flan_at = new String[rowCount]; // 식물상_꽃_개화_여부
				String[] flr_frt_mtrty_at = new String[rowCount]; // 식물상_열매_성숙_여부
				String[] flr_spr_at = new String[rowCount]; // 식물상_포자_여부
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
						pmntn_dstnc[i] = rs.getString(12);
						format_envrn[i] = rs.getString(13);
						plnt_stle[i] = rs.getString(14);
						idnt_charge_nm[i] = rs.getString(15);
						idnt_de[i] = rs.getString(16);
						dfnse[i] = rs.getString(17);
						distrb_aspc[i] = rs.getString(18);
						host[i] = rs.getString(19);
						plnt_prpgt_mth[i] = rs.getString(20);
						plt_at[i] = rs.getString(21);
						rsrch_prjct_ttle[i] = rs.getString(22);
						soil_plntn_cnd[i] = rs.getString(23);
						ntsp_plnt_at[i] = rs.getString(24);
						format_cln_ttle[i] = rs.getString(25);
						evnfm_dgree[i] = rs.getString(26);
						vertcl_distrb_cnds[i] = rs.getString(27);
						hrzntlty_distrb_cnds[i] = rs.getString(28);
						plnt_distrb_ar[i] = rs.getString(29);
						plnt_distrb_al[i] = rs.getString(30);
						etc_examin_charger_nm[i] = rs.getString(31);
						reprsnt_mntn[i] = rs.getString(32);
						ecsystm_ty[i] = rs.getString(33);
						examin_cours_adres_input[i] = rs.getString(34);
						examin_cours_direct_input[i] = rs.getString(35);
						resrce_use[i] = rs.getString(36);
						mtrty_indvd_qy[i] = rs.getString(37);
						cllc_charger_nm[i] = rs.getString(38);
						antcty[i] = rs.getString(39);
						fml_eng_ttle[i] = rs.getString(40);
						fml_korean_ttle[i] = rs.getString(41);
						note_examin[i] = rs.getString(42);
						ordr_eng_ttle[i] = rs.getString(43);
						ordr_korean_ttle[i] = rs.getString(44);
						flr_flwr_flan_at[i] = rs.getString(45);
						flr_frt_mtrty_at[i] = rs.getString(46);
						flr_spr_at[i] = rs.getString(47);
						spcs_schlshp_ttle[i] = rs.getString(48);
						geom[i] = rs.getString(49);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::spcs_korean_ttle::" + spcs_korean_ttle[i] + "::spcs_eng_ttle::" + spcs_eng_ttle[i]
								+ "::idvd_qy::" + idvd_qy[i] + "::partclr_matter::" + partclr_matter[i]
								+ "::pmntn_dstnc::" + pmntn_dstnc[i] + "::format_envrn::" + format_envrn[i]
								+ "::plnt_stle::" + plnt_stle[i] + "::idnt_charge_nm::" + idnt_charge_nm[i]
								+ "::idnt_de::" + idnt_de[i] + "::dfnse::" + dfnse[i] + "::distrb_aspc::"
								+ distrb_aspc[i] + "::host::" + host[i] + "::plnt_prpgt_mth::" + plnt_prpgt_mth[i]
								+ "::plt_at::" + plt_at[i] + "::rsrch_prjct_ttle::" + rsrch_prjct_ttle[i]
								+ "::soil_plntn_cnd::" + soil_plntn_cnd[i] + "::ntsp_plnt_at::" + ntsp_plnt_at[i]
								+ "::format_cln_ttle::" + format_cln_ttle[i] + "::evnfm_dgree::" + evnfm_dgree[i]
								+ "::vertcl_distrb_cnds::" + vertcl_distrb_cnds[i] + "::hrzntlty_distrb_cnds::"
								+ hrzntlty_distrb_cnds[i] + "::plnt_distrb_ar::" + plnt_distrb_ar[i]
								+ "::plnt_distrb_al::" + plnt_distrb_al[i] + "::etc_examin_charger_nm::"
								+ etc_examin_charger_nm[i] + "::reprsnt_mntn::" + reprsnt_mntn[i] + "::ecsystm_ty::"
								+ ecsystm_ty[i] + "::examin_cours_adres_input::" + examin_cours_adres_input[i]
								+ "::examin_cours_direct_input::" + examin_cours_direct_input[i] + "::resrce_use::"
								+ resrce_use[i] + "::mtrty_indvd_qy::" + mtrty_indvd_qy[i] + "::cllc_charger_nm::"
								+ cllc_charger_nm[i] + "::antcty::" + antcty[i] + "::fml_eng_ttle::" + fml_eng_ttle[i]
								+ "::fml_korean_ttle::" + fml_korean_ttle[i] + "::note_examin::" + note_examin[i]
								+ "::ordr_eng_ttle::" + ordr_eng_ttle[i] + "::ordr_korean_ttle::" + ordr_korean_ttle[i]
								+ "::flr_flwr_flan_at::" + flr_flwr_flan_at[i] + "::flr_frt_mtrty_at::"
								+ flr_frt_mtrty_at[i] + "::flr_spr_at::" + flr_spr_at[i] + "::spcs_schlshp_ttle::"
								+ spcs_schlshp_ttle[i] + "::geom::" + geom[i]);

						i++;

					}

					System.out.println("ECO_07 SELECT 프로세스 종료.");

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
						pmntn_dstnc[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						format_envrn[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						plnt_stle[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						idnt_charge_nm[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						idnt_de[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						dfnse[i] = JsonParser.colWrite_String_eic(rs.getString(17));
						distrb_aspc[i] = JsonParser.colWrite_String_eic(rs.getString(18));
						host[i] = JsonParser.colWrite_String_eic(rs.getString(19));
						plnt_prpgt_mth[i] = JsonParser.colWrite_String_eic(rs.getString(20));
						plt_at[i] = JsonParser.colWrite_String_eic(rs.getString(21));
						rsrch_prjct_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(22));
						soil_plntn_cnd[i] = JsonParser.colWrite_String_eic(rs.getString(23));
						ntsp_plnt_at[i] = JsonParser.colWrite_String_eic(rs.getString(24));
						format_cln_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(25));
						evnfm_dgree[i] = JsonParser.colWrite_String_eic(rs.getString(26));
						vertcl_distrb_cnds[i] = JsonParser.colWrite_String_eic(rs.getString(27));
						hrzntlty_distrb_cnds[i] = JsonParser.colWrite_String_eic(rs.getString(28));
						plnt_distrb_ar[i] = JsonParser.colWrite_String_eic(rs.getString(29));
						plnt_distrb_al[i] = JsonParser.colWrite_String_eic(rs.getString(30));
						etc_examin_charger_nm[i] = JsonParser.colWrite_String_eic(rs.getString(31));
						reprsnt_mntn[i] = JsonParser.colWrite_String_eic(rs.getString(32));
						ecsystm_ty[i] = JsonParser.colWrite_String_eic(rs.getString(33));
						examin_cours_adres_input[i] = JsonParser.colWrite_String_eic(rs.getString(34));
						examin_cours_direct_input[i] = JsonParser.colWrite_String_eic(rs.getString(35));
						resrce_use[i] = JsonParser.colWrite_String_eic(rs.getString(36));
						mtrty_indvd_qy[i] = JsonParser.colWrite_String_eic(rs.getString(37));
						cllc_charger_nm[i] = JsonParser.colWrite_String_eic(rs.getString(38));
						antcty[i] = JsonParser.colWrite_String_eic(rs.getString(39));
						fml_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(40));
						fml_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(41));
						note_examin[i] = JsonParser.colWrite_String_eic(rs.getString(42));
						ordr_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(43));
						ordr_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(44));
						flr_flwr_flan_at[i] = JsonParser.colWrite_String_eic(rs.getString(45));
						flr_frt_mtrty_at[i] = JsonParser.colWrite_String_eic(rs.getString(46));
						flr_spr_at[i] = JsonParser.colWrite_String_eic(rs.getString(47));
						spcs_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(48));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(49));
						
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
							pw.write(pmntn_dstnc[i]);
							pw.write("|^");
							pw.write(format_envrn[i]);
							pw.write("|^");
							pw.write(plnt_stle[i]);
							pw.write("|^");
							pw.write(idnt_charge_nm[i]);
							pw.write("|^");
							pw.write(idnt_de[i]);
							pw.write("|^");
							pw.write(dfnse[i]);
							pw.write("|^");
							pw.write(distrb_aspc[i]);
							pw.write("|^");
							pw.write(host[i]);
							pw.write("|^");
							pw.write(plnt_prpgt_mth[i]);
							pw.write("|^");
							pw.write(plt_at[i]);
							pw.write("|^");
							pw.write(rsrch_prjct_ttle[i]);
							pw.write("|^");
							pw.write(soil_plntn_cnd[i]);
							pw.write("|^");
							pw.write(ntsp_plnt_at[i]);
							pw.write("|^");
							pw.write(format_cln_ttle[i]);
							pw.write("|^");
							pw.write(evnfm_dgree[i]);
							pw.write("|^");
							pw.write(vertcl_distrb_cnds[i]);
							pw.write("|^");
							pw.write(hrzntlty_distrb_cnds[i]);
							pw.write("|^");
							pw.write(plnt_distrb_ar[i]);
							pw.write("|^");
							pw.write(plnt_distrb_al[i]);
							pw.write("|^");
							pw.write(etc_examin_charger_nm[i]);
							pw.write("|^");
							pw.write(reprsnt_mntn[i]);
							pw.write("|^");
							pw.write(ecsystm_ty[i]);
							pw.write("|^");
							pw.write(examin_cours_adres_input[i]);
							pw.write("|^");
							pw.write(examin_cours_direct_input[i]);
							pw.write("|^");
							pw.write(resrce_use[i]);
							pw.write("|^");
							pw.write(mtrty_indvd_qy[i]);
							pw.write("|^");
							pw.write(cllc_charger_nm[i]);
							pw.write("|^");
							pw.write(antcty[i]);
							pw.write("|^");
							pw.write(fml_eng_ttle[i]);
							pw.write("|^");
							pw.write(fml_korean_ttle[i]);
							pw.write("|^");
							pw.write(note_examin[i]);
							pw.write("|^");
							pw.write(ordr_eng_ttle[i]);
							pw.write("|^");
							pw.write(ordr_korean_ttle[i]);
							pw.write("|^");
							pw.write(flr_flwr_flan_at[i]);
							pw.write("|^");
							pw.write(flr_frt_mtrty_at[i]);
							pw.write("|^");
							pw.write(flr_spr_at[i]);
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

					System.out.println("ECO_07 SELECT 파일 생성 프로세스 종료.");

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
