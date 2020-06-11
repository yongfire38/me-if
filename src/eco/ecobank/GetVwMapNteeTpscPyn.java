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

public class GetVwMapNteeTpscPyn {
	
	// 에코뱅크 - 지형경관_면
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {
			
			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco11_query");
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
						String partclr_matter = " "; // 특이_사항
						String width = " "; // 가로
						String scene_sttus_se = " "; // 경관_현황_구분
						String scale = " "; // 규모
						String ncm = " "; // 별칭
						String vrticl = " "; // 세로
						String land_beach_se = " "; // 육지_해안_구분
						String tpgrph_scene_ttle = " "; // 지형_경관_명칭
						String grad = " "; // 등급
						String eclgy_valu_dc = " "; // 생태_가치_설명
						String lc = " "; // 위치
						String frmatn_era = " "; // 형성_시기
						String frmatn_actn = " "; // 형성_작용
						String spclty_examin_charger_nm = " "; // 전문_조사_담당자_성명
						String geom = " "; // 지오메트리
						
						spce_id = rs.getString(1);  
						examin_year = rs.getString(2);  
						tme = rs.getString(3);  
						examin_begin_de = rs.getString(4);  
						examin_end_de = rs.getString(5);  
						gnrl_examin_charger_nm = rs.getString(6);  
						rspnsbl_examin_charger_nm = rs.getString(7);  
						partclr_matter = rs.getString(8);  
						width = rs.getString(9);  
						scene_sttus_se = rs.getString(10);  
						scale = rs.getString(11);  
						ncm = rs.getString(12);  
						vrticl = rs.getString(13);  
						land_beach_se = rs.getString(14);  
						tpgrph_scene_ttle = rs.getString(15);  
						grad = rs.getString(16);  
						eclgy_valu_dc = rs.getString(17);  
						lc = rs.getString(18);  
						frmatn_era = rs.getString(19);  
						frmatn_actn = rs.getString(20);  
						spclty_examin_charger_nm = rs.getString(21);  
						//geom = rs.getString(22); 
						
						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::partclr_matter::" + partclr_matter + "::width::" + width
								+ "::scene_sttus_se::" + scene_sttus_se + "::scale::" + scale + "::ncm::"
								+ ncm + "::vrticl::" + vrticl + "::land_beach_se::" + land_beach_se + "::tpgrph_scene_ttle::"
								+ tpgrph_scene_ttle + "::grad::" + grad + "::eclgy_valu_dc::" + eclgy_valu_dc
								+ "::lc::" + lc + "::frmatn_era::" + frmatn_era
								+ "::frmatn_actn::" + frmatn_actn + "::spclty_examin_charger_nm::"
								+ spclty_examin_charger_nm + "::geom::" + geom);

					}
					
					System.out.println("ECO_11 SELECT 프로세스 종료.");
					
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
						String partclr_matter = " "; // 특이_사항
						String width = " "; // 가로
						String scene_sttus_se = " "; // 경관_현황_구분
						String scale = " "; // 규모
						String ncm = " "; // 별칭
						String vrticl = " "; // 세로
						String land_beach_se = " "; // 육지_해안_구분
						String tpgrph_scene_ttle = " "; // 지형_경관_명칭
						String grad = " "; // 등급
						String eclgy_valu_dc = " "; // 생태_가치_설명
						String lc = " "; // 위치
						String frmatn_era = " "; // 형성_시기
						String frmatn_actn = " "; // 형성_작용
						String spclty_examin_charger_nm = " "; // 전문_조사_담당자_성명
						String geom = " "; // 지오메트리
						
						spce_id = JsonParser.colWrite_String_eic(rs.getString(1));  
						examin_year = JsonParser.colWrite_String_eic(rs.getString(2));  
						tme = JsonParser.colWrite_String_eic(rs.getString(3));  
						examin_begin_de = JsonParser.colWrite_String_eic(rs.getString(4));  
						examin_end_de = JsonParser.colWrite_String_eic(rs.getString(5));  
						gnrl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(6));  
						rspnsbl_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(7));  
						partclr_matter = JsonParser.colWrite_String_eic(rs.getString(8));  
						width = JsonParser.colWrite_String_eic(rs.getString(9));  
						scene_sttus_se = JsonParser.colWrite_String_eic(rs.getString(10));  
						scale = JsonParser.colWrite_String_eic(rs.getString(11));  
						ncm = JsonParser.colWrite_String_eic(rs.getString(12));  
						vrticl = JsonParser.colWrite_String_eic(rs.getString(13));  
						land_beach_se = JsonParser.colWrite_String_eic(rs.getString(14));  
						tpgrph_scene_ttle = JsonParser.colWrite_String_eic(rs.getString(15));  
						grad = JsonParser.colWrite_String_eic(rs.getString(16));  
						eclgy_valu_dc = JsonParser.colWrite_String_eic(rs.getString(17));  
						lc = JsonParser.colWrite_String_eic(rs.getString(18));  
						frmatn_era = JsonParser.colWrite_String_eic(rs.getString(19));  
						frmatn_actn = JsonParser.colWrite_String_eic(rs.getString(20));  
						spclty_examin_charger_nm = JsonParser.colWrite_String_eic(rs.getString(21));  
						//geom = JsonParser.colWrite_String_eic(rs.getString(22));  
						
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
							pw.write(width);
							pw.write("|^");
							pw.write(scene_sttus_se);
							pw.write("|^");
							pw.write(scale);
							pw.write("|^");
							pw.write(ncm);
							pw.write("|^");
							pw.write(vrticl);
							pw.write("|^");
							pw.write(land_beach_se);
							pw.write("|^");
							pw.write(tpgrph_scene_ttle);
							pw.write("|^");
							pw.write(grad);
							pw.write("|^");
							pw.write(eclgy_valu_dc);
							pw.write("|^");
							pw.write(lc);
							pw.write("|^");
							pw.write(frmatn_era);
							pw.write("|^");
							pw.write(frmatn_actn);
							pw.write("|^");
							pw.write(spclty_examin_charger_nm);
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

					System.out.println("ECO_11 SELECT 파일 생성 프로세스 종료.");
					
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
