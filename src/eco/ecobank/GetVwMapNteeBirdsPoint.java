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

public class GetVwMapNteeBirdsPoint {

	// 에코뱅크 - 조류_점
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
				String query = DBConnection.getProperty("eco_post_eco04_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				if (args[0].equals("_tset")) {

					while (rs.next()) {
						
						// 전체 레코드 개수
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
						String examin_time = " "; // 조사_시간
						String prpgt_posblty = " "; // 번식_가능성
						String grup_prpgt_at = " "; // 집단_번식_여부
						String grid_no = " "; // 격자_번호
						String fml_korean_ttle = " "; // 과_한글_명칭
						String wethr = " "; // 날씨
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String format_ty = " "; // 서식_유형
						String examin_mth = " "; // 조사_방법
						String examin_odr = " "; // 조사_차수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String spot_no = " "; // 지점_번호
						String grup_slpn_spcs_at = " "; // 집단_수면_종_여부
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
						examin_time = rs.getString(12);
						prpgt_posblty = rs.getString(13);
						grup_prpgt_at = rs.getString(14);
						grid_no = rs.getString(15);
						fml_korean_ttle = rs.getString(16);
						wethr = rs.getString(17);
						ordr_korean_ttle = rs.getString(18);
						format_ty = rs.getString(19);
						examin_mth = rs.getString(20);
						examin_odr = rs.getString(21);
						spcs_schlshp_ttle = rs.getString(22);
						spot_no = rs.getString(23);
						grup_slpn_spcs_at = rs.getString(24);
						//geom = rs.getString(25);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::spcs_korean_ttle::" + spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle
								+ "::idvd_qy::" + idvd_qy + "::partclr_matter::" + partclr_matter
								+ "::examin_time::" + examin_time + "::prpgt_posblty::" + prpgt_posblty
								+ "::grup_prpgt_at::" + grup_prpgt_at + "::grid_no::" + grid_no
								+ "::fml_korean_ttle::" + fml_korean_ttle + "::wethr::" + wethr
								+ "::ordr_korean_ttle::" + ordr_korean_ttle + "::format_ty::" + format_ty
								+ "::examin_mth::" + examin_mth + "::examin_odr::" + examin_odr
								+ "::spcs_schlshp_ttle::" + spcs_schlshp_ttle + "::spot_no::" + spot_no
								+ "::grup_slpn_spcs_at::" + grup_slpn_spcs_at + "::geom::" + geom);


					}
					
					System.out.println("ECO_04 SELECT 프로세스 종료.");

				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						// 전체 레코드 개수
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
						String examin_time = " "; // 조사_시간
						String prpgt_posblty = " "; // 번식_가능성
						String grup_prpgt_at = " "; // 집단_번식_여부
						String grid_no = " "; // 격자_번호
						String fml_korean_ttle = " "; // 과_한글_명칭
						String wethr = " "; // 날씨
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String format_ty = " "; // 서식_유형
						String examin_mth = " "; // 조사_방법
						String examin_odr = " "; // 조사_차수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String spot_no = " "; // 지점_번호
						String grup_slpn_spcs_at = " "; // 집단_수면_종_여부
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
						examin_time = JsonParser.colWrite_String_eic(rs.getString(12));
						prpgt_posblty = JsonParser.colWrite_String_eic(rs.getString(13));
						grup_prpgt_at = JsonParser.colWrite_String_eic(rs.getString(14));
						grid_no = JsonParser.colWrite_String_eic(rs.getString(15));
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(16));
						wethr = JsonParser.colWrite_String_eic(rs.getString(17));
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(18));
						format_ty = JsonParser.colWrite_String_eic(rs.getString(19));
						examin_mth = JsonParser.colWrite_String_eic(rs.getString(20));
						examin_odr = JsonParser.colWrite_String_eic(rs.getString(21));
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(22));
						spot_no = JsonParser.colWrite_String_eic(rs.getString(23));
						grup_slpn_spcs_at = JsonParser.colWrite_String_eic(rs.getString(24));
						//geom = JsonParser.colWrite_String_eic(rs.getString(25));
						
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
							pw.write(examin_time);
							pw.write("|^");
							pw.write(prpgt_posblty);
							pw.write("|^");
							pw.write(grup_prpgt_at);
							pw.write("|^");
							pw.write(grid_no);
							pw.write("|^");
							pw.write(fml_korean_ttle);
							pw.write("|^");
							pw.write(wethr);
							pw.write("|^");
							pw.write(ordr_korean_ttle);
							pw.write("|^");
							pw.write(format_ty);
							pw.write("|^");
							pw.write(examin_mth);
							pw.write("|^");
							pw.write(examin_odr);
							pw.write("|^");
							pw.write(spcs_schlshp_ttle);
							pw.write("|^");
							pw.write(spot_no);
							pw.write("|^");
							pw.write(grup_slpn_spcs_at);
							pw.write("|^");
							pw.write(geom);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
						
						System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
						
					}
					
					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("ECO_04 SELECT 파일 생성 프로세스 종료.");

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
