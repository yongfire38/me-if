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

		if (args.length == 2) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco01_query") + "'" + args[1] +"'";
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
						
						// 전체 레코드 개수
						String eclgyzmp_id = " "; // 생태자연도_아이디
						String lclas = " "; // 대분류
						String clny_symbl = " "; // 군락_기호
						String plnt_clny_nm = " "; // 식물_군락_명칭
						String presv_grad = " "; // 보전_등급
						String vtn_evl_grad = " "; // 식생_평가_등급
						String amplt_evl_grad = " "; // 동식물_평가_등급
						String smld_evl_grad = " "; // 습지_평가_등급
						String tpgrph_evl_grad = " "; // 지형_평가_등급
						String tage_grad = " "; // 수령_등급
						String eclgyzmp_grad = " "; // 생태자연도_등급
						String tpgrph_nm = " "; // 지형_명칭
						String ntfc_no = " "; // 고시_번호
						String ntfc_de = " "; // 고시_일자
						String ntfc_de_se = " "; // 고시_일자_구분
						String mapdmc_no = " "; // 도엽_번호
						String geom = " "; // 지오메트리

						eclgyzmp_id = rs.getString(1);
						lclas = rs.getString(2);
						clny_symbl = rs.getString(3);
						plnt_clny_nm = rs.getString(4);
						presv_grad = rs.getString(5);
						vtn_evl_grad = rs.getString(6);
						amplt_evl_grad = rs.getString(7);
						smld_evl_grad = rs.getString(8);
						tpgrph_evl_grad = rs.getString(9);
						tage_grad = rs.getString(10);
						eclgyzmp_grad = rs.getString(11);
						tpgrph_nm = rs.getString(12);
						ntfc_no = rs.getString(13);
						ntfc_de = rs.getString(14);
						ntfc_de_se = rs.getString(15);
						mapdmc_no = rs.getString(16);
						//geom = rs.getString(17);

						System.out.println("eclgyzmp_id::" + eclgyzmp_id + "::lclas::" + lclas + "::clny_symbl::"
								+ clny_symbl + "::plnt_clny_nm::" + plnt_clny_nm + "::presv_grad::"
								+ presv_grad + "::vtn_evl_grad::" + vtn_evl_grad + "::amplt_evl_grad::"
								+ amplt_evl_grad + "::smld_evl_grad::" + smld_evl_grad + "::tpgrph_evl_grad::"
								+ tpgrph_evl_grad + "::tpgrph_evl_grad::" + tpgrph_evl_grad + "::tage_grad::"
								+ tage_grad + "::eclgyzmp_grad::" + eclgyzmp_grad + "::tpgrph_nm::" + tpgrph_nm
								+ "::ntfc_no::" + ntfc_no + "::ntfc_de::" + ntfc_de + "::ntfc_de_se::"
								+ ntfc_de_se + "::mapdmc_no::" + mapdmc_no + "::geom::" + geom);
						
					}

					System.out.println("ECO_01 SELECT 프로세스 종료.");

				} else {
					
					System.out.println("ECO_01 파일 작성 시작");

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {
						
						// 전체 레코드 개수
						String eclgyzmp_id = " "; // 생태자연도_아이디
						String lclas = " "; // 대분류
						String clny_symbl = " "; // 군락_기호
						String plnt_clny_nm = " "; // 식물_군락_명칭
						String presv_grad = " "; // 보전_등급
						String vtn_evl_grad = " "; // 식생_평가_등급
						String amplt_evl_grad = " "; // 동식물_평가_등급
						String smld_evl_grad = " "; // 습지_평가_등급
						String tpgrph_evl_grad = " "; // 지형_평가_등급
						String tage_grad = " "; // 수령_등급
						String eclgyzmp_grad = " "; // 생태자연도_등급
						String tpgrph_nm = " "; // 지형_명칭
						String ntfc_no = " "; // 고시_번호
						String ntfc_de = " "; // 고시_일자
						String ntfc_de_se = " "; // 고시_일자_구분
						String mapdmc_no = " "; // 도엽_번호
						String geom = " "; // 지오메트리
						
						eclgyzmp_id = JsonParser.colWrite_String_eic(rs.getString(1));
						lclas = JsonParser.colWrite_String_eic(rs.getString(2));
						clny_symbl = JsonParser.colWrite_String_eic(rs.getString(3));
						plnt_clny_nm = JsonParser.colWrite_String_eic(rs.getString(4));
						presv_grad = JsonParser.colWrite_String_eic(rs.getString(5));
						vtn_evl_grad = JsonParser.colWrite_String_eic(rs.getString(6));
						amplt_evl_grad = JsonParser.colWrite_String_eic(rs.getString(7));
						smld_evl_grad = JsonParser.colWrite_String_eic(rs.getString(8));
						tpgrph_evl_grad = JsonParser.colWrite_String_eic(rs.getString(9));
						tage_grad = JsonParser.colWrite_String_eic(rs.getString(10));
						eclgyzmp_grad = JsonParser.colWrite_String_eic(rs.getString(11));
						tpgrph_nm = JsonParser.colWrite_String_eic(rs.getString(12));
						ntfc_no = JsonParser.colWrite_String_eic(rs.getString(13));
						ntfc_de = JsonParser.colWrite_String_eic(rs.getString(14));
						ntfc_de_se = JsonParser.colWrite_String_eic(rs.getString(15));
						mapdmc_no = JsonParser.colWrite_String_eic(rs.getString(16));
						//geom = JsonParser.colWrite_String_eic(rs.getString(17));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(eclgyzmp_id);
							pw.write("|^");
							pw.write(lclas);
							pw.write("|^");
							pw.write(clny_symbl);
							pw.write("|^");
							pw.write(plnt_clny_nm);
							pw.write("|^");
							pw.write(presv_grad);
							pw.write("|^");
							pw.write(vtn_evl_grad);
							pw.write("|^");
							pw.write(amplt_evl_grad);
							pw.write("|^");
							pw.write(smld_evl_grad);
							pw.write("|^");
							pw.write(tpgrph_evl_grad);
							pw.write("|^");
							pw.write(tage_grad);
							pw.write("|^");
							pw.write(eclgyzmp_grad);
							pw.write("|^");
							pw.write(tpgrph_nm);
							pw.write("|^");
							pw.write(ntfc_no);
							pw.write("|^");
							pw.write(ntfc_de);
							pw.write("|^");
							pw.write(ntfc_de_se);
							pw.write("|^");
							pw.write(mapdmc_no);
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
