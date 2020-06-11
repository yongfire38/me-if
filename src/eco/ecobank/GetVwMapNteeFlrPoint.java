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

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco07_query");
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
						String pmntn_dstnc = " "; // 등산로_거리
						String format_envrn = " "; // 서식_환경
						String plnt_stle = " "; // 식물_형태
						String idnt_charge_nm = " "; // 동정_담당자_성명
						String idnt_de = " "; // 동정_일자
						String dfnse = " "; // 방위
						String distrb_aspc = " "; // 분포_양상
						String host = " "; // 숙주
						String plnt_prpgt_mth = " "; // 식물_번식_방법
						String plt_at = " "; // 식재_여부
						String rsrch_prjct_ttle = " "; // 연구_프로젝트_명칭
						String soil_plntn_cnd = " "; // 토양_수분_조건
						String ntsp_plnt_at = " "; // 토종_식물_여부
						String format_cln_ttle = " "; // 서식_군락_명칭
						String evnfm_dgree = " "; // 환경훼손_정도
						String vertcl_distrb_cnds = " "; // 수직_분포_연속성
						String hrzntlty_distrb_cnds = " "; // 수평_분포_연속성
						String plnt_distrb_ar = " "; // 식물_분포_면적
						String plnt_distrb_al = " "; // 식물_분포_표고
						String etc_examin_charger_nm = " "; // 기타_조사_담당자_성명
						String reprsnt_mntn = " "; // 대표_산
						String ecsystm_ty = " "; // 생태계_유형
						String examin_cours_adres_input = " "; // 조사_경로_주소_입력
						String examin_cours_direct_input = " "; // 조사_경로_직접_입력
						String resrce_use = " "; // 자원_이용
						String mtrty_indvd_qy = " "; // 성숙_개체_수
						String cllc_charger_nm = " "; // 채집_담당자_성명
						String antcty = " "; // 고도
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String note_examin = " "; // 노트_조사
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String flr_flwr_flan_at = " "; // 식물상_꽃_개화_여부
						String flr_frt_mtrty_at = " "; // 식물상_열매_성숙_여부
						String flr_spr_at = " "; // 식물상_포자_여부
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
						pmntn_dstnc = rs.getString(12);
						format_envrn = rs.getString(13);
						plnt_stle = rs.getString(14);
						idnt_charge_nm = rs.getString(15);
						idnt_de = rs.getString(16);
						dfnse = rs.getString(17);
						distrb_aspc = rs.getString(18);
						host = rs.getString(19);
						plnt_prpgt_mth = rs.getString(20);
						plt_at = rs.getString(21);
						rsrch_prjct_ttle = rs.getString(22);
						soil_plntn_cnd = rs.getString(23);
						ntsp_plnt_at = rs.getString(24);
						format_cln_ttle = rs.getString(25);
						evnfm_dgree = rs.getString(26);
						vertcl_distrb_cnds = rs.getString(27);
						hrzntlty_distrb_cnds = rs.getString(28);
						plnt_distrb_ar = rs.getString(29);
						plnt_distrb_al = rs.getString(30);
						etc_examin_charger_nm = rs.getString(31);
						reprsnt_mntn = rs.getString(32);
						ecsystm_ty = rs.getString(33);
						examin_cours_adres_input = rs.getString(34);
						examin_cours_direct_input = rs.getString(35);
						resrce_use = rs.getString(36);
						mtrty_indvd_qy = rs.getString(37);
						cllc_charger_nm = rs.getString(38);
						antcty = rs.getString(39);
						fml_eng_ttle = rs.getString(40);
						fml_korean_ttle = rs.getString(41);
						note_examin = rs.getString(42);
						ordr_eng_ttle = rs.getString(43);
						ordr_korean_ttle = rs.getString(44);
						flr_flwr_flan_at = rs.getString(45);
						flr_frt_mtrty_at = rs.getString(46);
						flr_spr_at = rs.getString(47);
						spcs_schlshp_ttle = rs.getString(48);
						//geom = rs.getString(49);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::spcs_korean_ttle::" + spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle
								+ "::idvd_qy::" + idvd_qy + "::partclr_matter::" + partclr_matter
								+ "::pmntn_dstnc::" + pmntn_dstnc + "::format_envrn::" + format_envrn
								+ "::plnt_stle::" + plnt_stle + "::idnt_charge_nm::" + idnt_charge_nm
								+ "::idnt_de::" + idnt_de + "::dfnse::" + dfnse + "::distrb_aspc::"
								+ distrb_aspc + "::host::" + host + "::plnt_prpgt_mth::" + plnt_prpgt_mth
								+ "::plt_at::" + plt_at + "::rsrch_prjct_ttle::" + rsrch_prjct_ttle
								+ "::soil_plntn_cnd::" + soil_plntn_cnd + "::ntsp_plnt_at::" + ntsp_plnt_at
								+ "::format_cln_ttle::" + format_cln_ttle + "::evnfm_dgree::" + evnfm_dgree
								+ "::vertcl_distrb_cnds::" + vertcl_distrb_cnds + "::hrzntlty_distrb_cnds::"
								+ hrzntlty_distrb_cnds + "::plnt_distrb_ar::" + plnt_distrb_ar
								+ "::plnt_distrb_al::" + plnt_distrb_al + "::etc_examin_charger_nm::"
								+ etc_examin_charger_nm + "::reprsnt_mntn::" + reprsnt_mntn + "::ecsystm_ty::"
								+ ecsystm_ty + "::examin_cours_adres_input::" + examin_cours_adres_input
								+ "::examin_cours_direct_input::" + examin_cours_direct_input + "::resrce_use::"
								+ resrce_use + "::mtrty_indvd_qy::" + mtrty_indvd_qy + "::cllc_charger_nm::"
								+ cllc_charger_nm + "::antcty::" + antcty + "::fml_eng_ttle::" + fml_eng_ttle
								+ "::fml_korean_ttle::" + fml_korean_ttle + "::note_examin::" + note_examin
								+ "::ordr_eng_ttle::" + ordr_eng_ttle + "::ordr_korean_ttle::" + ordr_korean_ttle
								+ "::flr_flwr_flan_at::" + flr_flwr_flan_at + "::flr_frt_mtrty_at::"
								+ flr_frt_mtrty_at + "::flr_spr_at::" + flr_spr_at + "::spcs_schlshp_ttle::"
								+ spcs_schlshp_ttle + "::geom::" + geom);

					}

					System.out.println("ECO_07 SELECT 프로세스 종료.");

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
						String pmntn_dstnc = " "; // 등산로_거리
						String format_envrn = " "; // 서식_환경
						String plnt_stle = " "; // 식물_형태
						String idnt_charge_nm = " "; // 동정_담당자_성명
						String idnt_de = " "; // 동정_일자
						String dfnse = " "; // 방위
						String distrb_aspc = " "; // 분포_양상
						String host = " "; // 숙주
						String plnt_prpgt_mth = " "; // 식물_번식_방법
						String plt_at = " "; // 식재_여부
						String rsrch_prjct_ttle = " "; // 연구_프로젝트_명칭
						String soil_plntn_cnd = " "; // 토양_수분_조건
						String ntsp_plnt_at = " "; // 토종_식물_여부
						String format_cln_ttle = " "; // 서식_군락_명칭
						String evnfm_dgree = " "; // 환경훼손_정도
						String vertcl_distrb_cnds = " "; // 수직_분포_연속성
						String hrzntlty_distrb_cnds = " "; // 수평_분포_연속성
						String plnt_distrb_ar = " "; // 식물_분포_면적
						String plnt_distrb_al = " "; // 식물_분포_표고
						String etc_examin_charger_nm = " "; // 기타_조사_담당자_성명
						String reprsnt_mntn = " "; // 대표_산
						String ecsystm_ty = " "; // 생태계_유형
						String examin_cours_adres_input = " "; // 조사_경로_주소_입력
						String examin_cours_direct_input = " "; // 조사_경로_직접_입력
						String resrce_use = " "; // 자원_이용
						String mtrty_indvd_qy = " "; // 성숙_개체_수
						String cllc_charger_nm = " "; // 채집_담당자_성명
						String antcty = " "; // 고도
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String note_examin = " "; // 노트_조사
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String flr_flwr_flan_at = " "; // 식물상_꽃_개화_여부
						String flr_frt_mtrty_at = " "; // 식물상_열매_성숙_여부
						String flr_spr_at = " "; // 식물상_포자_여부
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
						pmntn_dstnc = JsonParser.colWrite_String_eic(rs.getString(12));
						format_envrn = JsonParser.colWrite_String_eic(rs.getString(13));
						plnt_stle = JsonParser.colWrite_String_eic(rs.getString(14));
						idnt_charge_nm = JsonParser.colWrite_String_eic(rs.getString(15));
						idnt_de = JsonParser.colWrite_String_eic(rs.getString(16));
						dfnse = JsonParser.colWrite_String_eic(rs.getString(17));
						distrb_aspc = JsonParser.colWrite_String_eic(rs.getString(18));
						host = JsonParser.colWrite_String_eic(rs.getString(19));
						plnt_prpgt_mth = JsonParser.colWrite_String_eic(rs.getString(20));
						plt_at = JsonParser.colWrite_String_eic(rs.getString(21));
						rsrch_prjct_ttle = JsonParser.colWrite_String_eic(rs.getString(22));
						soil_plntn_cnd = JsonParser.colWrite_String_eic(rs.getString(23));
						ntsp_plnt_at = JsonParser.colWrite_String_eic(rs.getString(24));
						format_cln_ttle = JsonParser.colWrite_String_eic(rs.getString(25));
						evnfm_dgree = JsonParser.colWrite_String_eic(rs.getString(26));
						vertcl_distrb_cnds = JsonParser.colWrite_String_eic(rs.getString(27));
						hrzntlty_distrb_cnds = JsonParser.colWrite_String_eic(rs.getString(28));
						plnt_distrb_ar = JsonParser.colWrite_String_eic(rs.getString(29));
						plnt_distrb_al = JsonParser.colWrite_String_eic(rs.getString(30));
						etc_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(31));
						reprsnt_mntn = JsonParser.colWrite_String_eic(rs.getString(32));
						ecsystm_ty = JsonParser.colWrite_String_eic(rs.getString(33));
						examin_cours_adres_input = JsonParser.colWrite_String_eic(rs.getString(34));
						examin_cours_direct_input = JsonParser.colWrite_String_eic(rs.getString(35));
						resrce_use = JsonParser.colWrite_String_eic(rs.getString(36));
						mtrty_indvd_qy = JsonParser.colWrite_String_eic(rs.getString(37));
						cllc_charger_nm = JsonParser.colWrite_String_eic(rs.getString(38));
						antcty = JsonParser.colWrite_String_eic(rs.getString(39));
						fml_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(40));
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(41));
						note_examin = JsonParser.colWrite_String_eic(rs.getString(42));
						ordr_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(43));
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(44));
						flr_flwr_flan_at = JsonParser.colWrite_String_eic(rs.getString(45));
						flr_frt_mtrty_at = JsonParser.colWrite_String_eic(rs.getString(46));
						flr_spr_at = JsonParser.colWrite_String_eic(rs.getString(47));
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(48));
						//geom = JsonParser.colWrite_String_eic(rs.getString(49));
						
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
							pw.write(pmntn_dstnc);
							pw.write("|^");
							pw.write(format_envrn);
							pw.write("|^");
							pw.write(plnt_stle);
							pw.write("|^");
							pw.write(idnt_charge_nm);
							pw.write("|^");
							pw.write(idnt_de);
							pw.write("|^");
							pw.write(dfnse);
							pw.write("|^");
							pw.write(distrb_aspc);
							pw.write("|^");
							pw.write(host);
							pw.write("|^");
							pw.write(plnt_prpgt_mth);
							pw.write("|^");
							pw.write(plt_at);
							pw.write("|^");
							pw.write(rsrch_prjct_ttle);
							pw.write("|^");
							pw.write(soil_plntn_cnd);
							pw.write("|^");
							pw.write(ntsp_plnt_at);
							pw.write("|^");
							pw.write(format_cln_ttle);
							pw.write("|^");
							pw.write(evnfm_dgree);
							pw.write("|^");
							pw.write(vertcl_distrb_cnds);
							pw.write("|^");
							pw.write(hrzntlty_distrb_cnds);
							pw.write("|^");
							pw.write(plnt_distrb_ar);
							pw.write("|^");
							pw.write(plnt_distrb_al);
							pw.write("|^");
							pw.write(etc_examin_charger_nm);
							pw.write("|^");
							pw.write(reprsnt_mntn);
							pw.write("|^");
							pw.write(ecsystm_ty);
							pw.write("|^");
							pw.write(examin_cours_adres_input);
							pw.write("|^");
							pw.write(examin_cours_direct_input);
							pw.write("|^");
							pw.write(resrce_use);
							pw.write("|^");
							pw.write(mtrty_indvd_qy);
							pw.write("|^");
							pw.write(cllc_charger_nm);
							pw.write("|^");
							pw.write(antcty);
							pw.write("|^");
							pw.write(fml_eng_ttle);
							pw.write("|^");
							pw.write(fml_korean_ttle);
							pw.write("|^");
							pw.write(note_examin);
							pw.write("|^");
							pw.write(ordr_eng_ttle);
							pw.write("|^");
							pw.write(ordr_korean_ttle);
							pw.write("|^");
							pw.write(flr_flwr_flan_at);
							pw.write("|^");
							pw.write(flr_frt_mtrty_at);
							pw.write("|^");
							pw.write(flr_spr_at);
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
