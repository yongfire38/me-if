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

public class GetVwMapEcologympArea {

	// 에코뱅크 - 생태자연도_면
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
				String query = DBConnection.getProperty("eco_post_eco01_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] eclgyzmp_id = new String[rowCount]; // 생태자연도_아이디
				String[] lclas = new String[rowCount]; // 대분류
				String[] clny_symbl = new String[rowCount]; // 군락_기호
				String[] plnt_clny_nm = new String[rowCount]; // 식물_군락_명칭
				String[] presv_grad = new String[rowCount]; // 보전_등급
				String[] vtn_evl_grad = new String[rowCount]; // 식생_평가_등급
				String[] amplt_evl_grad = new String[rowCount]; // 동식물_평가_등급
				String[] smld_evl_grad = new String[rowCount]; // 습지_평가_등급
				String[] tpgrph_evl_grad = new String[rowCount]; // 지형_평가_등급
				String[] tage_grad = new String[rowCount]; // 수령_등급
				String[] eclgyzmp_grad = new String[rowCount]; // 생태자연도_등급
				String[] tpgrph_nm = new String[rowCount]; // 지형_명칭
				String[] ntfc_no = new String[rowCount]; // 고시_번호
				String[] ntfc_de = new String[rowCount]; // 고시_일자
				String[] ntfc_de_se = new String[rowCount]; // 고시_일자_구분
				String[] mapdmc_no = new String[rowCount]; // 도엽_번호
				String[] geom = new String[rowCount]; // 지오메트리

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						eclgyzmp_id[i] = rs.getString(1);
						lclas[i] = rs.getString(2);
						clny_symbl[i] = rs.getString(3);
						plnt_clny_nm[i] = rs.getString(4);
						presv_grad[i] = rs.getString(5);
						vtn_evl_grad[i] = rs.getString(6);
						amplt_evl_grad[i] = rs.getString(7);
						smld_evl_grad[i] = rs.getString(8);
						tpgrph_evl_grad[i] = rs.getString(9);
						tage_grad[i] = rs.getString(10);
						eclgyzmp_grad[i] = rs.getString(11);
						tpgrph_nm[i] = rs.getString(12);
						ntfc_no[i] = rs.getString(13);
						ntfc_de[i] = rs.getString(14);
						ntfc_de_se[i] = rs.getString(15);
						mapdmc_no[i] = rs.getString(16);
						geom[i] = rs.getString(17);

						System.out.println("eclgyzmp_id::" + eclgyzmp_id[i] + "::lclas::" + lclas[i] + "::clny_symbl::"
								+ clny_symbl[i] + "::plnt_clny_nm::" + plnt_clny_nm[i] + "::presv_grad::"
								+ presv_grad[i] + "::vtn_evl_grad::" + vtn_evl_grad[i] + "::amplt_evl_grad::"
								+ amplt_evl_grad[i] + "::smld_evl_grad::" + smld_evl_grad[i] + "::tpgrph_evl_grad::"
								+ tpgrph_evl_grad[i] + "::tpgrph_evl_grad::" + tpgrph_evl_grad[i] + "::tage_grad::"
								+ tage_grad[i] + "::eclgyzmp_grad::" + eclgyzmp_grad[i] + "::tpgrph_nm::" + tpgrph_nm[i]
								+ "::ntfc_no::" + ntfc_no[i] + "::ntfc_de::" + ntfc_de[i] + "::ntfc_de_se::"
								+ ntfc_de_se[i] + "::mapdmc_no::" + mapdmc_no[i] + "::geom::" + geom[i]);
						i++;
					}

					System.out.println("ECO_01 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						eclgyzmp_id[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						lclas[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						clny_symbl[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						plnt_clny_nm[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						presv_grad[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						vtn_evl_grad[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						amplt_evl_grad[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						smld_evl_grad[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						tpgrph_evl_grad[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						tage_grad[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						eclgyzmp_grad[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						tpgrph_nm[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						ntfc_no[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						ntfc_de[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						ntfc_de_se[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						mapdmc_no[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(17));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(eclgyzmp_id[i]);
							pw.write("|^");
							pw.write(lclas[i]);
							pw.write("|^");
							pw.write(clny_symbl[i]);
							pw.write("|^");
							pw.write(plnt_clny_nm[i]);
							pw.write("|^");
							pw.write(presv_grad[i]);
							pw.write("|^");
							pw.write(vtn_evl_grad[i]);
							pw.write("|^");
							pw.write(amplt_evl_grad[i]);
							pw.write("|^");
							pw.write(smld_evl_grad[i]);
							pw.write("|^");
							pw.write(tpgrph_evl_grad[i]);
							pw.write("|^");
							pw.write(tage_grad[i]);
							pw.write("|^");
							pw.write(eclgyzmp_grad[i]);
							pw.write("|^");
							pw.write(tpgrph_nm[i]);
							pw.write("|^");
							pw.write(ntfc_no[i]);
							pw.write("|^");
							pw.write(ntfc_de[i]);
							pw.write("|^");
							pw.write(ntfc_de_se[i]);
							pw.write("|^");
							pw.write(mapdmc_no[i]);
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

					System.out.println("ECO_01 SELECT 파일 생성 프로세스 종료.");

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
