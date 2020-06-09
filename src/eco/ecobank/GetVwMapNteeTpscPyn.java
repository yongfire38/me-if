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
				
				Class.forName(DBConnection.getProperty("eco_post_driver"));
				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco11_query");
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
				String[] width = new String[rowCount]; // 가로
				String[] scene_sttus_se = new String[rowCount]; // 경관_현황_구분
				String[] scale = new String[rowCount]; // 규모
				String[] ncm = new String[rowCount]; // 별칭
				String[] vrticl = new String[rowCount]; // 세로
				String[] land_beach_se = new String[rowCount]; // 육지_해안_구분
				String[] tpgrph_scene_ttle = new String[rowCount]; // 지형_경관_명칭
				String[] grad = new String[rowCount]; // 등급
				String[] eclgy_valu_dc = new String[rowCount]; // 생태_가치_설명
				String[] lc = new String[rowCount]; // 위치
				String[] frmatn_era = new String[rowCount]; // 형성_시기
				String[] frmatn_actn = new String[rowCount]; // 형성_작용
				String[] spclty_examin_charger_nm = new String[rowCount]; // 전문_조사_담당자_성명
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
						width[i] = rs.getString(9);  
						scene_sttus_se[i] = rs.getString(10);  
						scale[i] = rs.getString(11);  
						ncm[i] = rs.getString(12);  
						vrticl[i] = rs.getString(13);  
						land_beach_se[i] = rs.getString(14);  
						tpgrph_scene_ttle[i] = rs.getString(15);  
						grad[i] = rs.getString(16);  
						eclgy_valu_dc[i] = rs.getString(17);  
						lc[i] = rs.getString(18);  
						frmatn_era[i] = rs.getString(19);  
						frmatn_actn[i] = rs.getString(20);  
						spclty_examin_charger_nm[i] = rs.getString(21);  
						geom[i] = rs.getString(22);  
						
						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::partclr_matter::" + partclr_matter[i] + "::width::" + width[i]
								+ "::scene_sttus_se::" + scene_sttus_se[i] + "::scale::" + scale[i] + "::ncm::"
								+ ncm[i] + "::vrticl::" + vrticl[i] + "::land_beach_se::" + land_beach_se[i] + "::tpgrph_scene_ttle::"
								+ tpgrph_scene_ttle[i] + "::grad::" + grad[i] + "::eclgy_valu_dc::" + eclgy_valu_dc[i]
								+ "::lc::" + lc[i] + "::frmatn_era::" + frmatn_era[i]
								+ "::frmatn_actn::" + frmatn_actn[i] + "::spclty_examin_charger_nm::"
								+ spclty_examin_charger_nm[i] + "::geom::" + geom[i]);

						i++;
					}
					
					System.out.println("ECO_11 SELECT 프로세스 종료.");
					
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
						width[i] = JsonParser.colWrite_String_eic(rs.getString(9));  
						scene_sttus_se[i] = JsonParser.colWrite_String_eic(rs.getString(10));  
						scale[i] = JsonParser.colWrite_String_eic(rs.getString(11));  
						ncm[i] = JsonParser.colWrite_String_eic(rs.getString(12));  
						vrticl[i] = JsonParser.colWrite_String_eic(rs.getString(13));  
						land_beach_se[i] = JsonParser.colWrite_String_eic(rs.getString(14));  
						tpgrph_scene_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(15));  
						grad[i] = JsonParser.colWrite_String_eic(rs.getString(16));  
						eclgy_valu_dc[i] = JsonParser.colWrite_String_eic(rs.getString(17));  
						lc[i] = JsonParser.colWrite_String_eic(rs.getString(18));  
						frmatn_era[i] = JsonParser.colWrite_String_eic(rs.getString(19));  
						frmatn_actn[i] = JsonParser.colWrite_String_eic(rs.getString(20));  
						spclty_examin_charger_nm[i] = JsonParser.colWrite_String_eic(rs.getString(21));  
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(22));  
						
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
							pw.write(width[i]);
							pw.write("|^");
							pw.write(scene_sttus_se[i]);
							pw.write("|^");
							pw.write(scale[i]);
							pw.write("|^");
							pw.write(ncm[i]);
							pw.write("|^");
							pw.write(vrticl[i]);
							pw.write("|^");
							pw.write(land_beach_se[i]);
							pw.write("|^");
							pw.write(tpgrph_scene_ttle[i]);
							pw.write("|^");
							pw.write(grad[i]);
							pw.write("|^");
							pw.write(eclgy_valu_dc[i]);
							pw.write("|^");
							pw.write(lc[i]);
							pw.write("|^");
							pw.write(frmatn_era[i]);
							pw.write("|^");
							pw.write(frmatn_actn[i]);
							pw.write("|^");
							pw.write(spclty_examin_charger_nm[i]);
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
