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

			try {

				Class.forName(DBConnection.getProperty("eco_post_driver"));
				conn = DBConnection.getPostConnection("eco");
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("eco_post_eco03_query");
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
				String[] spcs_korean_ttle = new String[rowCount]; // 종_한글_명칭
				String[] spcs_eng_ttle = new String[rowCount]; // 종_영문_명칭
				String[] idvd_qy = new String[rowCount]; // 개체_수
				String[] partclr_matter = new String[rowCount]; // 특이_사항
				String[] respond_nm = new String[rowCount]; // 응답자_성명
				String[] al = new String[rowCount]; // 표고
				String[] observ_cn = new String[rowCount]; // 관찰_내용
				String[] dstrb_fctr = new String[rowCount]; // 교란_요인
				String[] site = new String[rowCount]; // 사이트
				String[] ecsystm_ty = new String[rowCount]; // 생태계_유형
				String[] lc = new String[rowCount]; // 위치
				String[] examin_pd = new String[rowCount]; // 조사_기간
				String[] cnfirm_mth = new String[rowCount]; // 확인_방법
				String[] grid_no = new String[rowCount]; // 격자_번호
				String[] fml_eng_ttle = new String[rowCount]; // 과_영문_명칭
				String[] fml_korean_ttle = new String[rowCount]; // 과_한글_명칭
				String[] dstrb_fctr_detail = new String[rowCount]; // 교란_요인_상세
				String[] ordr_eng_ttle = new String[rowCount]; // 목_영문_명칭
				String[] ordr_korean_ttle = new String[rowCount]; // 목_한글_명칭
				String[] rm = new String[rowCount]; // 비고
				String[] ecsystm_ty_detail = new String[rowCount]; // 생태계_유형_상세
				String[] amnrp_observ_egms_qy = new String[rowCount]; // 양서파충류_관찰_난괴_수
				String[] amnrp_observ_crcs_qy = new String[rowCount]; // 양서파충류_관찰_사체_수
				String[] amnrp_observ_adlt_qy = new String[rowCount]; // 양서파충류_관찰_성체_수
				String[] amnrp_observ_sound_qy = new String[rowCount]; // 양서파충류_관찰_소리_수
				String[] amnrp_observ_lrv_qy = new String[rowCount]; // 양서파충류_관찰_유생_수
				String[] amnrp_observ_exvm_qy = new String[rowCount]; // 양서파충류_관찰_허물_수
				String[] spcs_schlshp_ttle = new String[rowCount]; // 종_학술_명칭
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
						spcs_korean_ttle[i] = rs.getString(8);
						spcs_eng_ttle[i] = rs.getString(9);
						idvd_qy[i] = rs.getString(10);
						partclr_matter[i] = rs.getString(11);
						respond_nm[i] = rs.getString(12);
						al[i] = rs.getString(13);
						observ_cn[i] = rs.getString(14);
						dstrb_fctr[i] = rs.getString(15);
						site[i] = rs.getString(16);
						ecsystm_ty[i] = rs.getString(17);
						lc[i] = rs.getString(18);
						examin_pd[i] = rs.getString(19);
						cnfirm_mth[i] = rs.getString(20);
						grid_no[i] = rs.getString(21);
						fml_eng_ttle[i] = rs.getString(22);
						fml_korean_ttle[i] = rs.getString(23);
						dstrb_fctr_detail[i] = rs.getString(24);
						ordr_eng_ttle[i] = rs.getString(25);
						ordr_korean_ttle[i] = rs.getString(26);
						rm[i] = rs.getString(27);
						ecsystm_ty_detail[i] = rs.getString(28);
						amnrp_observ_egms_qy[i] = rs.getString(29);
						amnrp_observ_crcs_qy[i] = rs.getString(30);
						amnrp_observ_adlt_qy[i] = rs.getString(31);
						amnrp_observ_sound_qy[i] = rs.getString(32);
						amnrp_observ_lrv_qy[i] = rs.getString(33);
						amnrp_observ_exvm_qy[i] = rs.getString(34);
						spcs_schlshp_ttle[i] = rs.getString(35);
						geom[i] = rs.getString(36);

						System.out.println("spce_id::" + spce_id[i] + "::examin_year::" + examin_year[i] + "::tme::"
								+ tme[i] + "::examin_begin_de::" + examin_begin_de[i] + "::examin_end_de::"
								+ examin_end_de[i] + "::gnrl_examin_charger_nm::" + gnrl_examin_charger_nm[i]
								+ "::rspnsbl_examin_charger_nm::" + rspnsbl_examin_charger_nm[i]
								+ "::spcs_korean_ttle::" + spcs_korean_ttle[i] + "::spcs_eng_ttle::" + spcs_eng_ttle[i]
								+ "::idvd_qy::" + idvd_qy[i] + "::partclr_matter::" + partclr_matter[i]
								+ "::respond_nm::" + respond_nm[i] + "::al::" + al[i] + "::observ_cn::" + observ_cn[i]
								+ "::dstrb_fctr::" + dstrb_fctr[i] + "::site::" + site[i] + "::ecsystm_ty::"
								+ ecsystm_ty[i] + "::lc::" + lc[i] + "::examin_pd::" + examin_pd[i] + "::cnfirm_mth::"
								+ cnfirm_mth[i] + "::grid_no::" + grid_no[i] + "::fml_eng_ttle::" + fml_eng_ttle[i]
								+ "::fml_korean_ttle::" + fml_korean_ttle[i] + "::dstrb_fctr_detail::"
								+ dstrb_fctr_detail[i] + "::ordr_eng_ttle::" + ordr_eng_ttle[i] + "::ordr_korean_ttle::"
								+ ordr_korean_ttle[i] + "::rm::" + rm[i] + "::ecsystm_ty_detail::"
								+ ecsystm_ty_detail[i] + "::amnrp_observ_egms_qy::" + amnrp_observ_egms_qy[i]
								+ "::amnrp_observ_crcs_qy::" + amnrp_observ_crcs_qy[i] + "::amnrp_observ_adlt_qy::"
								+ amnrp_observ_adlt_qy[i] + "::amnrp_observ_sound_qy::" + amnrp_observ_sound_qy[i]
								+ "::amnrp_observ_lrv_qy::" + amnrp_observ_lrv_qy[i] + "::amnrp_observ_exvm_qy::"
								+ amnrp_observ_exvm_qy[i] + "::spcs_schlshp_ttle::" + spcs_schlshp_ttle[i] + "::geom::"
								+ geom[i]);
						
						i++;

					}

					System.out.println("ECO_03 SELECT 프로세스 종료.");

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
						spcs_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(8));  
						spcs_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(9));  
						idvd_qy[i] = JsonParser.colWrite_String_eic(rs.getString(10));  
						partclr_matter[i] = JsonParser.colWrite_String_eic(rs.getString(11));  
						respond_nm[i] = JsonParser.colWrite_String_eic(rs.getString(12));  
						al[i] = JsonParser.colWrite_String_eic(rs.getString(13));  
						observ_cn[i] = JsonParser.colWrite_String_eic(rs.getString(14));  
						dstrb_fctr[i] = JsonParser.colWrite_String_eic(rs.getString(15));  
						site[i] = JsonParser.colWrite_String_eic(rs.getString(16)); 
						ecsystm_ty[i] = JsonParser.colWrite_String_eic(rs.getString(17)); 
						lc[i] = JsonParser.colWrite_String_eic(rs.getString(18));  
						examin_pd[i] = JsonParser.colWrite_String_eic(rs.getString(19)); 
						cnfirm_mth[i] = JsonParser.colWrite_String_eic(rs.getString(20)); 
						grid_no[i] = JsonParser.colWrite_String_eic(rs.getString(21));  
						fml_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(22));  
						fml_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(23));  
						dstrb_fctr_detail[i] = JsonParser.colWrite_String_eic(rs.getString(24));  
						ordr_eng_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(25));  
						ordr_korean_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(26));  
						rm[i] = JsonParser.colWrite_String_eic(rs.getString(27));  
						ecsystm_ty_detail[i] = JsonParser.colWrite_String_eic(rs.getString(28));  
						amnrp_observ_egms_qy[i] = JsonParser.colWrite_String_eic(rs.getString(29));  
						amnrp_observ_crcs_qy[i] = JsonParser.colWrite_String_eic(rs.getString(30));  
						amnrp_observ_adlt_qy[i] = JsonParser.colWrite_String_eic(rs.getString(31));  
						amnrp_observ_sound_qy[i] = JsonParser.colWrite_String_eic(rs.getString(32));  
						amnrp_observ_lrv_qy[i] = JsonParser.colWrite_String_eic(rs.getString(33));  
						amnrp_observ_exvm_qy[i] = JsonParser.colWrite_String_eic(rs.getString(34));  
						spcs_schlshp_ttle[i] = JsonParser.colWrite_String_eic(rs.getString(35));  
						geom[i] = JsonParser.colWrite_String_eic(rs.getString(36));  
						
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
							pw.write(spcs_korean_ttle[i]);
							pw.write("|^");
							pw.write(spcs_eng_ttle[i]);
							pw.write("|^");
							pw.write(idvd_qy[i]);
							pw.write("|^");
							pw.write(partclr_matter[i]);
							pw.write("|^");
							pw.write(respond_nm[i]);
							pw.write("|^");
							pw.write(al[i]);
							pw.write("|^");
							pw.write(observ_cn[i]);
							pw.write("|^");
							pw.write(dstrb_fctr[i]);
							pw.write("|^");
							pw.write(site[i]);
							pw.write("|^");
							pw.write(ecsystm_ty[i]);
							pw.write("|^");
							pw.write(lc[i]);
							pw.write("|^");
							pw.write(examin_pd[i]);
							pw.write("|^");
							pw.write(cnfirm_mth[i]);
							pw.write("|^");
							pw.write(grid_no[i]);
							pw.write("|^");
							pw.write(fml_eng_ttle[i]);
							pw.write("|^");
							pw.write(fml_korean_ttle[i]);
							pw.write("|^");
							pw.write(dstrb_fctr_detail[i]);
							pw.write("|^");
							pw.write(ordr_eng_ttle[i]);
							pw.write("|^");
							pw.write(ordr_korean_ttle[i]);
							pw.write("|^");
							pw.write(rm[i]);
							pw.write("|^");
							pw.write(ecsystm_ty_detail[i]);
							pw.write("|^");
							pw.write(amnrp_observ_egms_qy[i]);
							pw.write("|^");
							pw.write(amnrp_observ_crcs_qy[i]);
							pw.write("|^");
							pw.write(amnrp_observ_adlt_qy[i]);
							pw.write("|^");
							pw.write(amnrp_observ_sound_qy[i]);
							pw.write("|^");
							pw.write(amnrp_observ_lrv_qy[i]);
							pw.write("|^");
							pw.write(amnrp_observ_exvm_qy[i]);
							pw.write("|^");
							pw.write(spcs_schlshp_ttle[i]);
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

					System.out.println("ECO_03 SELECT 파일 생성 프로세스 종료.");

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
