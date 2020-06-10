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

public class GetVwMapManagementareaArea {

	// 에코뱅크 - 별도관리지역_면
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
				String query = DBConnection.getProperty("eco_post_eco02_query");
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
						String managementarea_id = " "; //관리지역_아이디
						String ntrmn = " "; //천연기념울
						String mtst_olfl = " "; //산림_유전
						String wldns_amplt = " "; //야생_동식물
						String marn_resrce = " "; //수산_자원
						String smld_prtc = " "; //습지_보호
						String bgts = " "; //백두대간
						String eclgy_scene = " "; //생태_경관
						String natn_park = " "; //국립_공원
						String prvc_park = " "; //도립_공원
						String cntry_park = " "; //군립_공원
						String ntfc_de_se = " "; //고시_일자_구분
						String geom = " "; //지오메트리
						
						managementarea_id = rs.getString(1);
						ntrmn = rs.getString(2);
						mtst_olfl = rs.getString(3);
						wldns_amplt = rs.getString(4);
						marn_resrce = rs.getString(5);
						smld_prtc = rs.getString(6);
						bgts = rs.getString(7);
						eclgy_scene = rs.getString(8);
						natn_park = rs.getString(9);
						prvc_park = rs.getString(10);
						cntry_park = rs.getString(11);
						ntfc_de_se = rs.getString(12);
						//geom = rs.getString(13);
						
						System.out.println("managementarea_id::" + managementarea_id + "::ntrmn::" + ntrmn + "::mtst_olfl::"
								+ mtst_olfl + "::wldns_amplt::" + wldns_amplt + "::marn_resrce::"
								+ marn_resrce + "::smld_prtc::" + smld_prtc + "::bgts::"
								+ bgts + "::eclgy_scene::" + eclgy_scene + "::natn_park::"
								+ natn_park + "::prvc_park::" + prvc_park + "::cntry_park::"
								+ cntry_park + "::ntfc_de_se::" + ntfc_de_se + "::geom::" + geom);
						
						
					}
					
					System.out.println("ECO_02 SELECT 프로세스 종료.");
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						// 전체 레코드 개수
						String managementarea_id = " "; //관리지역_아이디
						String ntrmn = " "; //천연기념울
						String mtst_olfl = " "; //산림_유전
						String wldns_amplt = " "; //야생_동식물
						String marn_resrce = " "; //수산_자원
						String smld_prtc = " "; //습지_보호
						String bgts = " "; //백두대간
						String eclgy_scene = " "; //생태_경관
						String natn_park = " "; //국립_공원
						String prvc_park = " "; //도립_공원
						String cntry_park = " "; //군립_공원
						String ntfc_de_se = " "; //고시_일자_구분
						String geom = " "; //지오메트리
						
						managementarea_id = JsonParser.colWrite_String_eic(rs.getString(1));
						ntrmn = JsonParser.colWrite_String_eic(rs.getString(2));
						mtst_olfl = JsonParser.colWrite_String_eic(rs.getString(3));
						wldns_amplt = JsonParser.colWrite_String_eic(rs.getString(4));
						marn_resrce = JsonParser.colWrite_String_eic(rs.getString(5));
						smld_prtc = JsonParser.colWrite_String_eic(rs.getString(6));
						bgts = JsonParser.colWrite_String_eic(rs.getString(7));
						eclgy_scene = JsonParser.colWrite_String_eic(rs.getString(8));
						natn_park = JsonParser.colWrite_String_eic(rs.getString(9));
						prvc_park = JsonParser.colWrite_String_eic(rs.getString(10));
						cntry_park = JsonParser.colWrite_String_eic(rs.getString(11));
						ntfc_de_se = JsonParser.colWrite_String_eic(rs.getString(12));
						//geom = JsonParser.colWrite_String_eic(rs.getString(13));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(managementarea_id);
							pw.write("|^");
							pw.write(ntrmn);
							pw.write("|^");
							pw.write(mtst_olfl);
							pw.write("|^");
							pw.write(wldns_amplt);
							pw.write("|^");
							pw.write(marn_resrce);
							pw.write("|^");
							pw.write(smld_prtc);
							pw.write("|^");
							pw.write(bgts);
							pw.write("|^");
							pw.write(eclgy_scene);
							pw.write("|^");
							pw.write(natn_park);
							pw.write("|^");
							pw.write(prvc_park);
							pw.write("|^");
							pw.write(cntry_park);
							pw.write("|^");
							pw.write(ntfc_de_se);
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

					System.out.println("ECO_02 SELECT 파일 생성 프로세스 종료.");
					
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
