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

public class GetVwMapNteeVtnPyn {

	// 에코뱅크 - 식생_면
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco12_query");
				System.out.println("query :::" + query);

				conn.setAutoCommit(false);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(1);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(1);

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
						String partclr_matter = " "; // 특이_사항
						String lrge_dstrct = " "; // 대_권역
						String lclas = " "; // 대분류
						String small_dstrct = " "; // 소_권역
						String evl_unit = " "; // 평가_단위
						String cln_symbl = " "; // 군락_기호
						String plnt_cln_ttle = " "; // 식물_군락_명칭
						String presv_grad = " "; // 보전_등급
						String area = " "; // 지역
						String rm = " "; // 비고
						String geom = " "; // 지오메트리

						spce_id = rs.getString(1);
						examin_year = rs.getString(2);
						tme = rs.getString(3);
						examin_begin_de = rs.getString(4);
						examin_end_de = rs.getString(5);
						gnrl_examin_charger_nm = rs.getString(6);
						rspnsbl_examin_charger_nm = rs.getString(7);
						partclr_matter = rs.getString(8);
						lrge_dstrct = rs.getString(9);
						lclas = rs.getString(10);
						small_dstrct = rs.getString(11);
						evl_unit = rs.getString(12);
						cln_symbl = rs.getString(13);
						plnt_cln_ttle = rs.getString(14);
						presv_grad = rs.getString(15);
						area = rs.getString(16);
						rm = rs.getString(17);
						//geom = rs.getString(18);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm + "::partclr_matter::"
								+ partclr_matter + "::lrge_dstrct::" + lrge_dstrct + "::lclas::" + lclas
								+ "::small_dstrct::" + small_dstrct + "::evl_unit::" + evl_unit + "::cln_symbl::"
								+ cln_symbl + "::plnt_cln_ttle::" + plnt_cln_ttle + "::presv_grad::"
								+ presv_grad + "::area::" + area + "::rm::" + rm + "::geom::" + geom);

					}
					
					System.out.println("ECO_12 SELECT 프로세스 종료.");

				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						// 전체 레코드 개수만큼의 배열
						String spce_id = " "; // 공간_아이디
						String examin_year = " "; // 조사_년도
						String tme = " "; // 회차
						String examin_begin_de = " "; // 조사_시작_일자
						String examin_end_de = " "; // 조사_종료_일자
						String gnrl_examin_charger_nm = " "; // 일반_조사_담당자_성명
						String rspnsbl_examin_charger_nm = " "; // 책임_조사_담당자_성명
						String partclr_matter = " "; // 특이_사항
						String lrge_dstrct = " "; // 대_권역
						String lclas = " "; // 대분류
						String small_dstrct = " "; // 소_권역
						String evl_unit = " "; // 평가_단위
						String cln_symbl = " "; // 군락_기호
						String plnt_cln_ttle = " "; // 식물_군락_명칭
						String presv_grad = " "; // 보전_등급
						String area = " "; // 지역
						String rm = " "; // 비고
						String geom = " "; // 지오메트리
						
						spce_id = JsonParser.colWrite_String_eic(rs.getString(1));
						examin_year = JsonParser.colWrite_String_eic(rs.getString(2));
						tme = JsonParser.colWrite_String_eic(rs.getString(3)); 
						examin_begin_de = JsonParser.colWrite_String_eic(rs.getString(4)); 
						examin_end_de = JsonParser.colWrite_String_eic(rs.getString(5));
						gnrl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(6));
						rspnsbl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(7));
						partclr_matter = JsonParser.colWrite_String_eic(rs.getString(8));
						lrge_dstrct = JsonParser.colWrite_String_eic(rs.getString(9));
						lclas = JsonParser.colWrite_String_eic(rs.getString(10));
						small_dstrct = JsonParser.colWrite_String_eic(rs.getString(11));
						evl_unit = JsonParser.colWrite_String_eic(rs.getString(12));
						cln_symbl = JsonParser.colWrite_String_eic(rs.getString(13));
						plnt_cln_ttle = JsonParser.colWrite_String_eic(rs.getString(14));
						presv_grad = JsonParser.colWrite_String_eic(rs.getString(15));
						area = JsonParser.colWrite_String_eic(rs.getString(16));
						rm = JsonParser.colWrite_String_eic(rs.getString(17));
						//geom = JsonParser.colWrite_String_eic(rs.getString(18));
						
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
							pw.write(partclr_matter);
							pw.write("|^");
							pw.write(lrge_dstrct);
							pw.write("|^");
							pw.write(lclas);
							pw.write("|^");
							pw.write(small_dstrct);
							pw.write("|^");
							pw.write(evl_unit);
							pw.write("|^");
							pw.write(cln_symbl);
							pw.write("|^");
							pw.write(plnt_cln_ttle);
							pw.write("|^");
							pw.write(presv_grad);
							pw.write("|^");
							pw.write(area);
							pw.write("|^");
							pw.write(rm);
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
