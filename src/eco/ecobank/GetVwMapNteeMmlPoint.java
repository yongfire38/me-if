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

public class GetVwMapNteeMmlPoint {
	
	// 에코뱅크 - 표유류_점
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
				String query = DBConnection.getProperty("eco_post_eco09_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");
				
				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();
				
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
						String wethr = " "; // 날씨
						String photo_file_ttle = " "; // 사진_파일_명칭
						String observ_cn = " "; // 관찰_내용
						String observ_de = " "; // 관찰_일자
						String observ_place = " "; // 관찰_장소
						String ecsystm_ty = " "; // 생태계_유형
						String cnfirm_mth = " "; // 확인_방법
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String mml_observ_observ_qy = " "; // 표유류_관찰_관찰_수
						String mml_observ_den_qy = " "; // 표유류_관찰_굴_수
						String mml_observ_etc_marks_dc = " "; //표유류_관찰_기타_흔적_설명
						String mml_observ_exct_qy = " "; // 표유류_관찰_배설물_수
						String mml_observ_prar_qy = " "; // 표유류_관찰_번식지_수
						String mml_observ_crcs_qy = " "; // 표유류_관찰_사체_수
						String mml_observ_ankr_qy = " "; // 표유류_관찰_식흔_수
						String mml_observ_mark_qy = " "; // 표유류_관찰_족적_수
						String mml_observ_hair_qy = " "; // 표유류_관찰_털_수
						String mml_observ_capt_qy = " "; // 표유류_관찰_포획_수
						String mml_examin_cryn_qy = " "; // 표유류_조사_울음_수
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
						wethr = rs.getString(12); 
						photo_file_ttle = rs.getString(13); 
						observ_cn = rs.getString(14); 
						observ_de = rs.getString(15); 
						observ_place = rs.getString(16); 
						ecsystm_ty = rs.getString(17); 
						cnfirm_mth = rs.getString(18); 
						grid_no = rs.getString(19); 
						fml_eng_ttle = rs.getString(20); 
						fml_korean_ttle = rs.getString(21); 
						ordr_eng_ttle = rs.getString(22); 
						ordr_korean_ttle = rs.getString(23); 
						spcs_schlshp_ttle = rs.getString(24); 
						mml_observ_observ_qy = rs.getString(25); 
						mml_observ_den_qy = rs.getString(26); 
						mml_observ_etc_marks_dc = rs.getString(27); 
						mml_observ_exct_qy = rs.getString(28); 
						mml_observ_prar_qy = rs.getString(29); 
						mml_observ_crcs_qy = rs.getString(30); 
						mml_observ_ankr_qy = rs.getString(31); 
						mml_observ_mark_qy = rs.getString(32); 
						mml_observ_hair_qy = rs.getString(33); 
						mml_observ_capt_qy = rs.getString(34); 
						mml_examin_cryn_qy = rs.getString(35); 
						//geom = rs.getString(36); 
						
						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::spcs_korean_ttle::" + spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle
								+ "::idvd_qy::" + idvd_qy + "::partclr_matter::" + partclr_matter + "::wethr::"
								+ wethr + "::photo_file_ttle::" + photo_file_ttle + "::observ_cn::" + observ_cn + "::observ_de::"
								+ observ_de + "::observ_place::" + observ_place + "::ecsystm_ty::" + ecsystm_ty
								+ "::cnfirm_mth::" + cnfirm_mth + "::grid_no::" + grid_no
								+ "::fml_eng_ttle::" + fml_eng_ttle + "::fml_korean_ttle::"
								+ fml_korean_ttle + "::ordr_eng_ttle::" + ordr_eng_ttle + "::ordr_korean_ttle::" + ordr_korean_ttle
								+ "::spcs_schlshp_ttle::" + spcs_schlshp_ttle + "::mml_observ_observ_qy::"
								+ mml_observ_observ_qy + "::mml_observ_den_qy::" + mml_observ_den_qy
								+ "::mml_observ_etc_marks_dc::" + mml_observ_etc_marks_dc + "::mml_observ_exct_qy::"
								+ mml_observ_exct_qy + "::mml_observ_prar_qy::" + mml_observ_prar_qy + "::mml_observ_crcs_qy::"
								+ mml_observ_crcs_qy + "::mml_observ_ankr_qy::" + mml_observ_ankr_qy + "::mml_observ_mark_qy::"
								+ mml_observ_mark_qy + "::mml_observ_hair_qy::" + mml_observ_hair_qy
								+ "::mml_observ_capt_qy::" + mml_observ_capt_qy + "::mml_examin_cryn_qy::" + mml_examin_cryn_qy
								+ "::geom::" + geom);

					}
					
					System.out.println("ECO_09 SELECT 프로세스 종료.");
					
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
						String wethr = " "; // 날씨
						String photo_file_ttle = " "; // 사진_파일_명칭
						String observ_cn = " "; // 관찰_내용
						String observ_de = " "; // 관찰_일자
						String observ_place = " "; // 관찰_장소
						String ecsystm_ty = " "; // 생태계_유형
						String cnfirm_mth = " "; // 확인_방법
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
						String mml_observ_observ_qy = " "; // 표유류_관찰_관찰_수
						String mml_observ_den_qy = " "; // 표유류_관찰_굴_수
						String mml_observ_etc_marks_dc = " "; //표유류_관찰_기타_흔적_설명
						String mml_observ_exct_qy = " "; // 표유류_관찰_배설물_수
						String mml_observ_prar_qy = " "; // 표유류_관찰_번식지_수
						String mml_observ_crcs_qy = " "; // 표유류_관찰_사체_수
						String mml_observ_ankr_qy = " "; // 표유류_관찰_식흔_수
						String mml_observ_mark_qy = " "; // 표유류_관찰_족적_수
						String mml_observ_hair_qy = " "; // 표유류_관찰_털_수
						String mml_observ_capt_qy = " "; // 표유류_관찰_포획_수
						String mml_examin_cryn_qy = " "; // 표유류_조사_울음_수
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
						wethr = JsonParser.colWrite_String_eic(rs.getString(12)); 
						photo_file_ttle = JsonParser.colWrite_String_eic(rs.getString(13)); 
						observ_cn = JsonParser.colWrite_String_eic(rs.getString(14)); 
						observ_de = JsonParser.colWrite_String_eic(rs.getString(15)); 
						observ_place = JsonParser.colWrite_String_eic(rs.getString(16)); 
						ecsystm_ty = JsonParser.colWrite_String_eic(rs.getString(17)); 
						cnfirm_mth = JsonParser.colWrite_String_eic(rs.getString(18)); 
						grid_no = JsonParser.colWrite_String_eic(rs.getString(19)); 
						fml_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(20)); 
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(21)); 
						ordr_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(22)); 
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(23)); 
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(24)); 
						mml_observ_observ_qy = JsonParser.colWrite_String_eic(rs.getString(25)); 
						mml_observ_den_qy = JsonParser.colWrite_String_eic(rs.getString(26)); 
						mml_observ_etc_marks_dc = JsonParser.colWrite_String_eic(rs.getString(27)); 
						mml_observ_exct_qy = JsonParser.colWrite_String_eic(rs.getString(28)); 
						mml_observ_prar_qy = JsonParser.colWrite_String_eic(rs.getString(29)); 
						mml_observ_crcs_qy = JsonParser.colWrite_String_eic(rs.getString(30)); 
						mml_observ_ankr_qy = JsonParser.colWrite_String_eic(rs.getString(31)); 
						mml_observ_mark_qy = JsonParser.colWrite_String_eic(rs.getString(32)); 
						mml_observ_hair_qy = JsonParser.colWrite_String_eic(rs.getString(33)); 
						mml_observ_capt_qy = JsonParser.colWrite_String_eic(rs.getString(34)); 
						mml_examin_cryn_qy = JsonParser.colWrite_String_eic(rs.getString(35)); 
						//geom = JsonParser.colWrite_String_eic(rs.getString(36)); 
						
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
							pw.write(wethr); 
							pw.write("|^");
							pw.write(photo_file_ttle); 
							pw.write("|^");
							pw.write(observ_cn);
							pw.write("|^");
							pw.write(observ_de); 
							pw.write("|^");
							pw.write(observ_place); 
							pw.write("|^");
							pw.write(ecsystm_ty); 
							pw.write("|^");
							pw.write(cnfirm_mth); 
							pw.write("|^");
							pw.write(grid_no); 
							pw.write("|^");
							pw.write(fml_eng_ttle); 
							pw.write("|^");
							pw.write(fml_korean_ttle); 
							pw.write("|^");
							pw.write(ordr_eng_ttle); 
							pw.write("|^");
							pw.write(ordr_korean_ttle); 
							pw.write("|^");
							pw.write(spcs_schlshp_ttle);
							pw.write("|^");
							pw.write(mml_observ_observ_qy);
							pw.write("|^");
							pw.write(mml_observ_den_qy);
							pw.write("|^");
							pw.write(mml_observ_etc_marks_dc);
							pw.write("|^");
							pw.write(mml_observ_exct_qy);
							pw.write("|^");
							pw.write(mml_observ_prar_qy);
							pw.write("|^");
							pw.write(mml_observ_crcs_qy);
							pw.write("|^");
							pw.write(mml_observ_ankr_qy);
							pw.write("|^");
							pw.write(mml_observ_mark_qy); 
							pw.write("|^");
							pw.write(mml_observ_hair_qy); 
							pw.write("|^");
							pw.write(mml_observ_capt_qy);
							pw.write("|^");
							pw.write(mml_examin_cryn_qy);
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

					System.out.println("ECO_09 SELECT 파일 생성 프로세스 종료.");
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
