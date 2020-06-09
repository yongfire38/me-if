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

				Class.forName(DBConnection.getProperty("eco_post_driver"));
				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco12_query");
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
				String[] partclr_matter = new String[rowCount]; // 특이_사항
				String[] lrge_dstrct = new String[rowCount]; // 대_권역
				String[] lclas = new String[rowCount]; // 대분류
				String[] small_dstrct = new String[rowCount]; // 소_권역
				String[] evl_unit = new String[rowCount]; // 평가_단위
				String[] cln_symbl = new String[rowCount]; // 군락_기호
				String[] plnt_cln_ttle = new String[rowCount]; // 식물_군락_명칭
				String[] presv_grad = new String[rowCount]; // 보전_등급
				String[] area = new String[rowCount]; // 지역
				String[] rm = new String[rowCount]; // 비고
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
						partclr_matter[i] = rs.getString(8);
						lrge_dstrct[i] = rs.getString(9);
						lclas[i] = rs.getString(10);
						small_dstrct[i] = rs.getString(11);
						evl_unit[i] = rs.getString(12);
						cln_symbl[i] = rs.getString(13);
						plnt_cln_ttle[i] = rs.getString(14);
						presv_grad[i] = rs.getString(15);
						area[i] = rs.getString(16);
						rm[i] = rs.getString(17);
						geom[i] = rs.getString(18);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i] + "::partclr_matter::"
								+ partclr_matter[i] + "::lrge_dstrct::" + lrge_dstrct[i] + "::lclas::" + lclas[i]
								+ "::small_dstrct::" + small_dstrct[i] + "::evl_unit::" + evl_unit[i] + "::cln_symbl::"
								+ cln_symbl[i] + "::plnt_cln_ttle::" + plnt_cln_ttle[i] + "::presv_grad::"
								+ presv_grad[i] + "::area::" + area[i] + "::rm::" + rm[i] + "::geom::" + geom[i]);

						i++;

					}
					
					System.out.println("ECO_12 SELECT 프로세스 종료.");

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
						partclr_matter[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						lrge_dstrct[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						lclas[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						small_dstrct[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						evl_unit[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						cln_symbl[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						plnt_cln_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						presv_grad[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						area[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						rm[i] = JsonParser.colWrite_String_eic(rs.getString(17));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(18));
						
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
							pw.write(partclr_matter[i]);
							pw.write("|^");
							pw.write(lrge_dstrct[i]);
							pw.write("|^");
							pw.write(lclas[i]);
							pw.write("|^");
							pw.write(small_dstrct[i]);
							pw.write("|^");
							pw.write(evl_unit[i]);
							pw.write("|^");
							pw.write(cln_symbl[i]);
							pw.write("|^");
							pw.write(plnt_cln_ttle[i]);
							pw.write("|^");
							pw.write(presv_grad[i]);
							pw.write("|^");
							pw.write(area[i]);
							pw.write("|^");
							pw.write(rm[i]);
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
