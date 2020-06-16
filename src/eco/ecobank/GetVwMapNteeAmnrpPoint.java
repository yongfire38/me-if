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

public class GetVwMapNteeAmnrpPoint {

	// 에코뱅크 - 양서파충류_점
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			// sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {

				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco03_query");
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
						String respond_nm = " "; // 응답자_성명
						String al = " "; // 표고
						String observ_cn = " "; // 관찰_내용
						String dstrb_fctr = " "; // 교란_요인
						String site = " "; // 사이트
						String ecsystm_ty = " "; // 생태계_유형
						String lc = " "; // 위치
						String examin_pd = " "; // 조사_기간
						String cnfirm_mth = " "; // 확인_방법
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String dstrb_fctr_detail = " "; // 교란_요인_상세
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String rm = " "; // 비고
						String ecsystm_ty_detail = " "; // 생태계_유형_상세
						String amnrp_observ_egms_qy = " "; // 양서파충류_관찰_난괴_수
						String amnrp_observ_crcs_qy = " "; // 양서파충류_관찰_사체_수
						String amnrp_observ_adlt_qy = " "; // 양서파충류_관찰_성체_수
						String amnrp_observ_sound_qy = " "; // 양서파충류_관찰_소리_수
						String amnrp_observ_lrv_qy = " "; // 양서파충류_관찰_유생_수
						String amnrp_observ_exvm_qy = " "; // 양서파충류_관찰_허물_수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
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
						respond_nm = rs.getString(12);
						al = rs.getString(13);
						observ_cn = rs.getString(14);
						dstrb_fctr = rs.getString(15);
						site = rs.getString(16);
						ecsystm_ty = rs.getString(17);
						lc = rs.getString(18);
						examin_pd = rs.getString(19);
						cnfirm_mth = rs.getString(20);
						grid_no = rs.getString(21);
						fml_eng_ttle = rs.getString(22);
						fml_korean_ttle = rs.getString(23);
						dstrb_fctr_detail = rs.getString(24);
						ordr_eng_ttle = rs.getString(25);
						ordr_korean_ttle = rs.getString(26);
						rm = rs.getString(27);
						ecsystm_ty_detail = rs.getString(28);
						amnrp_observ_egms_qy = rs.getString(29);
						amnrp_observ_crcs_qy = rs.getString(30);
						amnrp_observ_adlt_qy = rs.getString(31);
						amnrp_observ_sound_qy = rs.getString(32);
						amnrp_observ_lrv_qy = rs.getString(33);
						amnrp_observ_exvm_qy = rs.getString(34);
						spcs_schlshp_ttle = rs.getString(35);
						//geom = rs.getString(36);

						System.out.println("spce_id::" + spce_id + "::examin_year::" + examin_year + "::tme::"
								+ tme + "::examin_begin_de::" + examin_begin_de + "::examin_end_de::"
								+ examin_end_de + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm
								+ "::spcs_korean_ttle::" + spcs_korean_ttle + "::spcs_eng_ttle::" + spcs_eng_ttle
								+ "::idvd_qy::" + idvd_qy + "::partclr_matter::" + partclr_matter
								+ "::respond_nm::" + respond_nm + "::al::" + al + "::observ_cn::" + observ_cn
								+ "::dstrb_fctr::" + dstrb_fctr + "::site::" + site + "::ecsystm_ty::"
								+ ecsystm_ty + "::lc::" + lc + "::examin_pd::" + examin_pd + "::cnfirm_mth::"
								+ cnfirm_mth + "::grid_no::" + grid_no + "::fml_eng_ttle::" + fml_eng_ttle
								+ "::fml_korean_ttle::" + fml_korean_ttle + "::dstrb_fctr_detail::"
								+ dstrb_fctr_detail + "::ordr_eng_ttle::" + ordr_eng_ttle + "::ordr_korean_ttle::"
								+ ordr_korean_ttle + "::rm::" + rm + "::ecsystm_ty_detail::"
								+ ecsystm_ty_detail + "::amnrp_observ_egms_qy::" + amnrp_observ_egms_qy
								+ "::amnrp_observ_crcs_qy::" + amnrp_observ_crcs_qy + "::amnrp_observ_adlt_qy::"
								+ amnrp_observ_adlt_qy + "::amnrp_observ_sound_qy::" + amnrp_observ_sound_qy
								+ "::amnrp_observ_lrv_qy::" + amnrp_observ_lrv_qy + "::amnrp_observ_exvm_qy::"
								+ amnrp_observ_exvm_qy + "::spcs_schlshp_ttle::" + spcs_schlshp_ttle + "::geom::"
								+ geom);
						

					}

					System.out.println("ECO_03 SELECT 프로세스 종료.");

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
						String respond_nm = " "; // 응답자_성명
						String al = " "; // 표고
						String observ_cn = " "; // 관찰_내용
						String dstrb_fctr = " "; // 교란_요인
						String site = " "; // 사이트
						String ecsystm_ty = " "; // 생태계_유형
						String lc = " "; // 위치
						String examin_pd = " "; // 조사_기간
						String cnfirm_mth = " "; // 확인_방법
						String grid_no = " "; // 격자_번호
						String fml_eng_ttle = " "; // 과_영문_명칭
						String fml_korean_ttle = " "; // 과_한글_명칭
						String dstrb_fctr_detail = " "; // 교란_요인_상세
						String ordr_eng_ttle = " "; // 목_영문_명칭
						String ordr_korean_ttle = " "; // 목_한글_명칭
						String rm = " "; // 비고
						String ecsystm_ty_detail = " "; // 생태계_유형_상세
						String amnrp_observ_egms_qy = " "; // 양서파충류_관찰_난괴_수
						String amnrp_observ_crcs_qy = " "; // 양서파충류_관찰_사체_수
						String amnrp_observ_adlt_qy = " "; // 양서파충류_관찰_성체_수
						String amnrp_observ_sound_qy = " "; // 양서파충류_관찰_소리_수
						String amnrp_observ_lrv_qy = " "; // 양서파충류_관찰_유생_수
						String amnrp_observ_exvm_qy = " "; // 양서파충류_관찰_허물_수
						String spcs_schlshp_ttle = " "; // 종_학술_명칭
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
						respond_nm = JsonParser.colWrite_String_eic(rs.getString(12));  
						al = JsonParser.colWrite_String_eic(rs.getString(13));  
						observ_cn = JsonParser.colWrite_String_eic(rs.getString(14));  
						dstrb_fctr = JsonParser.colWrite_String_eic(rs.getString(15));  
						site = JsonParser.colWrite_String_eic(rs.getString(16)); 
						ecsystm_ty = JsonParser.colWrite_String_eic(rs.getString(17)); 
						lc = JsonParser.colWrite_String_eic(rs.getString(18));  
						examin_pd = JsonParser.colWrite_String_eic(rs.getString(19)); 
						cnfirm_mth = JsonParser.colWrite_String_eic(rs.getString(20)); 
						grid_no = JsonParser.colWrite_String_eic(rs.getString(21));  
						fml_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(22));  
						fml_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(23));  
						dstrb_fctr_detail = JsonParser.colWrite_String_eic(rs.getString(24));  
						ordr_eng_ttle = JsonParser.colWrite_String_eic(rs.getString(25));  
						ordr_korean_ttle = JsonParser.colWrite_String_eic(rs.getString(26));  
						rm = JsonParser.colWrite_String_eic(rs.getString(27));  
						ecsystm_ty_detail = JsonParser.colWrite_String_eic(rs.getString(28));  
						amnrp_observ_egms_qy = JsonParser.colWrite_String_eic(rs.getString(29));  
						amnrp_observ_crcs_qy = JsonParser.colWrite_String_eic(rs.getString(30));  
						amnrp_observ_adlt_qy = JsonParser.colWrite_String_eic(rs.getString(31));  
						amnrp_observ_sound_qy = JsonParser.colWrite_String_eic(rs.getString(32));  
						amnrp_observ_lrv_qy = JsonParser.colWrite_String_eic(rs.getString(33));  
						amnrp_observ_exvm_qy = JsonParser.colWrite_String_eic(rs.getString(34));  
						spcs_schlshp_ttle = JsonParser.colWrite_String_eic(rs.getString(35));  
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
							pw.write(respond_nm);
							pw.write("|^");
							pw.write(al);
							pw.write("|^");
							pw.write(observ_cn);
							pw.write("|^");
							pw.write(dstrb_fctr);
							pw.write("|^");
							pw.write(site);
							pw.write("|^");
							pw.write(ecsystm_ty);
							pw.write("|^");
							pw.write(lc);
							pw.write("|^");
							pw.write(examin_pd);
							pw.write("|^");
							pw.write(cnfirm_mth);
							pw.write("|^");
							pw.write(grid_no);
							pw.write("|^");
							pw.write(fml_eng_ttle);
							pw.write("|^");
							pw.write(fml_korean_ttle);
							pw.write("|^");
							pw.write(dstrb_fctr_detail);
							pw.write("|^");
							pw.write(ordr_eng_ttle);
							pw.write("|^");
							pw.write(ordr_korean_ttle);
							pw.write("|^");
							pw.write(rm);
							pw.write("|^");
							pw.write(ecsystm_ty_detail);
							pw.write("|^");
							pw.write(amnrp_observ_egms_qy);
							pw.write("|^");
							pw.write(amnrp_observ_crcs_qy);
							pw.write("|^");
							pw.write(amnrp_observ_adlt_qy);
							pw.write("|^");
							pw.write(amnrp_observ_sound_qy);
							pw.write("|^");
							pw.write(amnrp_observ_lrv_qy);
							pw.write("|^");
							pw.write(amnrp_observ_exvm_qy);
							pw.write("|^");
							pw.write(spcs_schlshp_ttle);
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

					System.out.println("ECO_03 SELECT 파일 생성 프로세스 종료.");

				}

			} catch (SQLException sqle) {

				System.out.println("SELECT문에서 예외 발생");
				
				cf = "Y";
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
				
				// 쿼리에서 에러 발생시에는 종료로 빠진다.
				if (cf.equals("Y")) {
					System.exit(-1);
				}

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
