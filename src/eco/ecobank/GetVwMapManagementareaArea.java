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

				// 전체 레코드 개수만큼의 배열
				String[] managementarea_id = new String[rowCount]; //관리지역_아이디
				String[] ntrmn = new String[rowCount]; //천연기념울
				String[] mtst_olfl = new String[rowCount]; //산림_유전
				String[] wldns_amplt = new String[rowCount]; //야생_동식물
				String[] marn_resrce = new String[rowCount]; //수산_자원
				String[] smld_prtc = new String[rowCount]; //습지_보호
				String[] bgts = new String[rowCount]; //백두대간
				String[] eclgy_scene = new String[rowCount]; //생태_경관
				String[] natn_park = new String[rowCount]; //국립_공원
				String[] prvc_park = new String[rowCount]; //도립_공원
				String[] cntry_park = new String[rowCount]; //군립_공원
				String[] ntfc_de_se = new String[rowCount]; //고시_일자_구분
				String[] geom = new String[rowCount]; //지오메트리
				
				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;
				
				if (args[0].equals("_tset")) {
					
					while (rs.next()) {
						
						managementarea_id[i] = rs.getString(1);
						ntrmn[i] = rs.getString(2);
						mtst_olfl[i] = rs.getString(3);
						wldns_amplt[i] = rs.getString(4);
						marn_resrce[i] = rs.getString(5);
						smld_prtc[i] = rs.getString(6);
						bgts[i] = rs.getString(7);
						eclgy_scene[i] = rs.getString(8);
						natn_park[i] = rs.getString(9);
						prvc_park[i] = rs.getString(10);
						cntry_park[i] = rs.getString(11);
						ntfc_de_se[i] = rs.getString(12);
						geom[i] = rs.getString(13);
						
						System.out.println("managementarea_id::" + managementarea_id[i] + "::ntrmn::" + ntrmn[i] + "::mtst_olfl::"
								+ mtst_olfl[i] + "::wldns_amplt::" + wldns_amplt[i] + "::marn_resrce::"
								+ marn_resrce[i] + "::smld_prtc::" + smld_prtc[i] + "::bgts::"
								+ bgts[i] + "::eclgy_scene::" + eclgy_scene[i] + "::natn_park::"
								+ natn_park[i] + "::prvc_park::" + prvc_park[i] + "::cntry_park::"
								+ cntry_park[i] + "::ntfc_de_se::" + ntfc_de_se[i] + "::geom::" + geom[i]);
						
						i++;
					}
					
					System.out.println("ECO_02 SELECT 프로세스 종료.");
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						managementarea_id[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						ntrmn[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						mtst_olfl[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						wldns_amplt[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						marn_resrce[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						smld_prtc[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						bgts[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						eclgy_scene[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						natn_park[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						prvc_park[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						cntry_park[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						ntfc_de_se[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(managementarea_id[i]);
							pw.write("|^");
							pw.write(ntrmn[i]);
							pw.write("|^");
							pw.write(mtst_olfl[i]);
							pw.write("|^");
							pw.write(wldns_amplt[i]);
							pw.write("|^");
							pw.write(marn_resrce[i]);
							pw.write("|^");
							pw.write(smld_prtc[i]);
							pw.write("|^");
							pw.write(bgts[i]);
							pw.write("|^");
							pw.write(eclgy_scene[i]);
							pw.write("|^");
							pw.write(natn_park[i]);
							pw.write("|^");
							pw.write(prvc_park[i]);
							pw.write("|^");
							pw.write(cntry_park[i]);
							pw.write("|^");
							pw.write(ntfc_de_se[i]);
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
