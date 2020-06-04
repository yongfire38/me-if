package epe.epoumb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import common.JsonParser;

public class GetEpeEpoumb {

	// 국민신문고 샘플 xml 파싱 
	// 파라미터로 xml 파일의 절대경로를 받으면 파일을 생성한다.
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		if (args.length == 1) {
			
			System.out.println("xml 파싱 프로세스 시작.");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			//파일의 절대경로가 파라미터
			Document doc = documentBuilder.parse(args[0]);

			doc.getDocumentElement().normalize();
			
			try {
				
				File file = null;

				//수집서버의 절대경로
				file = new File("/data1/if_data/EPE/TIF_EPE_01.dat");

				// 루트 엘레멘트인 <PaidPublicComplaint> :: 최상단
				Element root = doc.getDocumentElement();

				// 루트 엘레멘트의 모든 자식 엘레멘트 :: 이걸 첫번째 자식 엘레멘트라고 한다. 총 50개 (#text 제외)
				// <Petition>, <Petition>, <Receipt> ...
				NodeList firstChilderenNodes = root.getChildNodes();

				// 첫번째 자식 엘레멘트를 순회하면서...(#text 제외하고 50번)
				for (int i = 0; i < firstChilderenNodes.getLength(); i++) {
					
					String petition_opType = " "; // 민원신청_자료변환유형코드
					String petition_key_petiNo = " "; //민원신청_수정,삭제시 키_신청번호
					String petition_data_petiNo = " "; //민원신청_입력,수정시 자료_신청번호
					String petition_data_title = " "; //민원신청_입력,수정시 자료_신청제목
					String petition_data_reason = " "; //민원신청_입력,수정시 자료_내용
					String petition_data_returnMethodCode = " "; //민원신청_입력,수정시 자료_결과통보방법코드
					String petition_data_returnMethodName = " "; //민원신청_입력,수정시 자료_결과통보방법명
					String petition_data_receivePathCode = " "; //민원신청_입력,수정시 자료_접수경로기관코드
					String petition_data_receivePathName = " "; //민원신청_입력,수정시 자료_접수경로기관명
					String petition_data_parentPetiNo = " "; //민원신청_입력,수정시 자료_모신청번호
					String petition_data_parentPetiRelation = " "; //민원신청_입력,수정시 자료_모신청과의관계
					String petition_data_oldPetiNo = " "; //민원신청_입력,수정시 자료_기존신청번호
					String petition_data_regDate = " "; //민원신청_입력,수정시 자료_등록일(YYYYMMDD)
					String petition_data_regDateTime = " "; //민원신청_입력,수정시 자료_등록일시(YYYYMMDDHH24MISS)
					String petition_data_isOpened = " "; //민원신청_입력,수정시 자료_공개여부(Y,N)
					String petition_data_complexCode = " "; //민원신청_입력,수정시 자료_복합처리
					String petition_data_isHighst = " "; //민원신청_입력,수정시 자료_지정민원여부(Y,N)
					String petition_data_isHurry = " "; //민원신청_입력,수정시 자료_긴급민원여부(Y,N)
					String petition_data_statusCode = " "; //민원신청_입력,수정시 자료_처리상태코드
					String petition_data_statusName = " "; //민원신청_입력,수정시 자료_처리상태명
					String petition_data_typeCode = " "; //민원신청_입력,수정시 자료_민원유형코드
					String petition_data_typeName = " "; //민원신청_입력,수정시 자료_민원유형명
					String petition_data_isGroup = " "; //민원신청_입력,수정시 자료_집단민원여부(Y,N)
					String petition_data_isManyPeople = " "; //민원신청_입력,수정시 자료_다수인민원여부(Y,N)
					String petition_data_catCode = " "; //민원신청_입력,수정시 자료_민원카테고리코드
					String petition_data_catName = " "; //민원신청_입력,수정시 자료_민원카테고리명
					String petition_data_menuName = " "; //민원신청_입력,수정시 자료_민원신청프레임메뉴명
					String petition_data_deptOpenYn = " "; //민원신청_입력,수정시 자료_타부서공개여부(Y,N)
					String petition_data_entCivilYnC = " "; //민원신청_입력,수정시 자료_기업민원여부(Y,N)
					String petition_data_grp3PetiYnC = " "; //민원신청_입력,수정시 자료_단체민원여부(Y,N,C)
					String petition_data_corpName = " "; //민원신청_입력,수정시 자료_기업명(상호)
					String petition_data_corpCode = " "; //민원신청_입력,수정시 자료_사업자등록번호
					String petition_data_addCorpName = " "; //민원신청_입력,수정시 자료_기업명(상호)(보정)
					String petition_data_addCorpCode = " "; //민원신청_입력,수정시 자료_사업자등록번호(보정)
					String petition_data_mailAttchYnC = " "; //민원신청_입력,수정시 자료_메일답변첨부여부(Y,N)
					String petition_data_petitioner_name = " "; //민원신청_입력,수정시 자료_신청인_신청인명
					String petition_data_petitioner_jobCode = " "; //민원신청_입력,수정시 자료_신청인_신청인직업코드
					String petition_data_petitioner_jobName = " "; //민원신청_입력,수정시 자료_신청인_신청인직업명
					String petition_data_petitioner_zipCode = " "; //민원신청_입력,수정시 자료_신청인_우편번호(2015년8월부터는 5자리로 변경예정)
					String petition_data_petitioner_address1 = " "; //민원신청_입력,수정시 자료_신청인_기본주소
					String petition_data_petitioner_address2 = " "; //민원신청_입력,수정시 자료_신청인_상세주소
					String petition_data_petitioner_email = " "; //민원신청_입력,수정시 자료_신청인_이메일주소
					String petition_data_petitioner_cellPhone = " "; //민원신청_입력,수정시 자료_신청인_핸드폰번호
					String petition_data_petitioner_linePhone = " "; //민원신청_입력,수정시 자료_신청인_전화번호
					String petition_data_petitioner_birthDate = " "; //민원신청_입력,수정시 자료_신청인_생일(YYYYMMDD)
					String petition_data_petitioner_sex = " "; //민원신청_입력,수정시 자료_신청인_성별(1:남,2:여)
					String petition_data_petitioner_isKorean = " "; //민원신청_입력,수정시 자료_신청인_내국인여부(Y,N)
					String petition_data_cancel_cancel_yn_c = " "; //민원신청_입력,수정시 자료_취하_취하여부
					String petition_data_cancel_date = " "; //민원신청_입력,수정시 자료_취하_취하일(YYYYMMDD)
					String petition_data_cancel_dateTime = " "; //민원신청_입력,수정시 자료_취하_취하일시(YYYYMMDDHH24MISS)
					String petition_data_cancel_reason = " "; //민원신청_입력,수정시 자료_취하_사유
					
					String receipt_opType = " "; //민원접수_자료변환유형코드
					String receipt_key_petiNo = " "; //민원접수_수정,삭제시 키_신청번호
					String receipt_key_civilNo = " "; //민원접수_수정,삭제시 키_접수번호
					String receipt_data_petiNo = " "; //민원접수_입력,수정시 자료_신청번호
					String receipt_data_civilNo = " "; //민원접수_입력,수정시 자료_접수번호
					String receipt_data_regDate = " "; //민원접수_입력,수정시 자료_등록일(YYYYMMDD)
					String receipt_data_regDateTime = " "; //민원접수_입력,수정시 자료_등록일시(YYYYMMDDHH24MISS)
					String receipt_data_isMainAnc = " "; //민원접수_입력,수정시 자료_주무부처여부
					String receipt_data_abstract = " "; //민원접수_입력,수정시 자료_요지
					String receipt_data_planEndDate = " "; //민원접수_입력,수정시 자료_처리종료예정일(YYYMMDD)
					String receipt_data_planEndDateTime = " "; //민원접수_입력,수정시 자료_처리종료예정일시(YYYYMMDDHH24MISS)
					String receipt_data_statusCode = " "; //민원접수_입력,수정시 자료_기관별처리상태코드
					String receipt_data_statusName = " "; //민원접수_입력,수정시 자료_기관별처리상태명
					String receipt_data_ancId = " "; //민원접수_입력,수정시 자료_접수담당자ID
					String receipt_data_ancName = " "; //민원접수_입력,수정시 자료_접수담당자명(*)
					String receipt_data_ancRegDate = " "; //민원접수_입력,수정시 자료_접수일(YYYYMMDD)
					String receipt_data_ancRegDateTime = " "; //민원접수_입력,수정시 자료_접수일시(YYYYMMDDHH24MISS)
					String receipt_data_firstEndDate = " "; //민원접수_입력,수정시 자료_최초종료예정일(YYYYMMDD)
					String receipt_data_firstEndDateTime = " "; //민원접수_입력,수정시 자료_최초종료예정일시(YYYYMMDDHH24MISS)
					String receipt_data_doResultDate = " "; //민원접수_입력,수정시 자료_처리결과통보일(YYYYMMDD)
					String receipt_data_doResultDateTime = " "; //민원접수_입력,수정시 자료_처리결과통보일시(YYYYMMDDHH24MISS)
					String receipt_data_activeYN = " "; //민원접수_입력,수정시 자료_자료유효여부(Y:유효,N:비활성된자료)
					String receipt_data_busiBranCode = " "; //민원접수_입력,수정시 자료_분류코드
					String receipt_data_busiBranCodeName = " "; //민원접수_입력,수정시 자료_분류코드이름
					String receipt_data_orgOpenYn = " "; //민원접수_입력,수정시 자료_타기관공개여부(Y,N)
					String receipt_data_expand_title = " "; //민원접수_입력,수정시 자료_처리연장_제목
					String receipt_data_expand_reason = " "; //민원접수_입력,수정시 자료_처리연장_사유
					String receipt_data_satisfy_content_code = " "; //민원접수_입력,수정시 자료_만족도_내용_만족도코드
					String receipt_data_satisfy_content_name = " "; //민원접수_입력,수정시 자료_만족도_내용_만족도명
					String receipt_data_satisfy_moreWord = " "; //민원접수_입력,수정시 자료_만족도_한마디더
					String receipt_data_satisfy_regDate = " "; //민원접수_입력,수정시 자료_만족도_만족도입력일(YYYYMMDD)
					String receipt_data_satisfy_regDateTime = " "; //민원접수_입력,수정시 자료_만족도_만족도입력일시(YYYYMMDDHH24MISS)
					
					String process_opType = " "; //처리(부서별)자료변환유형코드
					String process_key_petiNo = " "; //처리(부서별)_수정,삭제시 키_신청번호
					String process_key_civilNo = " "; //처리(부서별)_수정,삭제시 키_접수번호
					String process_key_deptCode = " "; //처리(부서별)_수정,삭제시 키_부서코드
					String process_key_seq = " "; //처리(부서별)_수정,삭제시 키_처리부서일렬번호
					String process_data_petiNo = " "; //처리(부서별)_입력,수정시 자료_신청번호
					String process_data_civilNo = " "; //처리(부서별)_입력,수정시 자료_접수번호
					String process_data_deptCode = " "; //처리(부서별)_입력,수정시 자료_부서코드
					String process_data_seq = " "; //처리(부서별)_입력,수정시 자료_처리부서일렬번호
					String process_data_deptName = " "; //처리(부서별)_입력,수정시 자료_부서명(*)
					String process_data_dutyId = " "; //처리(부서별)_입력,수정시 자료_담당자아이디
					String process_data_dutyDeptCode = " "; //처리(부서별)_입력,수정시 자료_담당자부서코드
					String process_data_dutyName = " "; //처리(부서별)_입력,수정시 자료_담당자명(*)
					String process_data_isMainDept = " "; //처리(부서별)_입력,수정시 자료_주무부서여부
					String process_data_regDate = " "; //처리(부서별)_입력,수정시 자료_처리등록일(YYYYMMDD)
					String process_data_regDateTime = " "; //처리(부서별)_입력,수정시 자료_처리등록일시(YYYYMMDDHH24MISS)
					String process_data_doDate = " "; //처리(부서별)_입력,수정시 자료_처리일(YYYYMMDD)
					String process_data_doDateTime = " "; //처리(부서별)_입력,수정시 자료_처리일시(YYYYMMDDHH24MISS)
					String process_data_content = " "; //처리(부서별)_입력,수정시 자료_처리결과
					String process_data_statusCode = " "; //처리(부서별)_입력,수정시 자료_부서별처리상태코드
					String process_data_statusName = " "; //처리(부서별)_입력,수정시 자료_부서별처리상태명
					String process_data_abstract = " "; //처리(부서별)_입력,수정시 자료_요약
					String process_data_restatusCode = " "; //처리(부서별)_입력,수정시 자료_재분류지정상태코드
					String process_data_restatusName = " "; //처리(부서별)_입력,수정시 자료_재분류지정상태명
					String process_data_activeYN = " "; //처리(부서별)_입력,수정시 자료_자료유효여부(Y:유효,N:비활성된자료)
					
					String extitem_opType = " "; //민원 추가수집항목_자료변환유형코드
					String extitem_key_petiNo = " "; //민원 추가수집항목_수정,삭제시 키_민원신청번호
					String extitem_key_deptCode = " "; //민원 추가수집항목_수정,삭제시 키_기관코드
					String extitem_key_channel = " "; //민원 추가수집항목_수정,삭제시 키_채널코드
					String extitem_key_menuCode = " "; //민원 추가수집항목_수정,삭제시 키_메뉴코드
					String extitem_key_code = " "; //민원 추가수집항목_수정,삭제시 키_항목코드
					String extitem_key_orderNum = " "; //민원 추가수집항목_수정,삭제시 키_표현순서
					String extitem_data_petiNo = " "; //민원 추가수집항목_입력,수정시 자료_민원신청번호
					String extitem_data_deptCode = " "; //민원 추가수집항목_입력,수정시 자료_기관코드
					String extitem_data_channel = " "; //민원 추가수집항목_입력,수정시 자료_채널코드
					String extitem_data_menuCode = " "; //민원 추가수집항목_입력,수정시 자료_메뉴코드
					String extitem_data_code = " "; //민원 추가수집항목_입력,수정시 자료_항목코드
					String extitem_data_orderNum = " "; //민원 추가수집항목_입력,수정시 자료_표현순서
					String extitem_data_title = " "; //민원 추가수집항목_입력,수정시 자료_제목
					String extitem_data_extDesc = " "; //민원 추가수집항목_입력,수정시 자료_추가수집항목설명
					String extitem_data_extType = " "; //민원 추가수집항목_입력,수정시 자료_질문속성타입(테스트,라디오버튼,체크박스...)
					String extitem_data_txtLen = " "; //민원 추가수집항목_입력,수정시 자료_글자수(길이)
					String extitem_data_ansTxt = " "; //민원 추가수집항목_입력,수정시 자료_질문속성타입이텍스트일경우응답내용
					String extitem_data_ansChoose0 = " "; //민원 추가수집항목_입력,수정시 자료_radio,selectbox결과값
					String extitem_data_ansChoose0_nm = " "; //민원 추가수집항목_입력,수정시 자료_radio,selectbox결과값의응답내용
					String extitem_data_ansChoose1 = " "; //민원 추가수집항목_입력,수정시 자료_checkbox첫번째선택결과값
					String extitem_data_ansChoose1_nm = " "; //민원 추가수집항목_입력,수정시 자료_checkbox첫번째선택결과값내용
					String extitem_data_ansChoose2 = " "; //민원 추가수집항목_입력,수정시 자료_checkbox두번째선택결과값
					String extitem_data_ansChoose2_nm = " "; //민원 추가수집항목_입력,수정시 자료_checkbox두번째선택결과값내용
					String extitem_data_ansChoose3 = " "; //민원 추가수집항목_입력,수정시 자료_checkbox세번째선택결과값
					String extitem_data_ansChoose3_nm = " "; //민원 추가수집항목_입력,수정시 자료_checkbox세번째선택결과값내용
					String extitem_data_ansChoose4 = " "; //민원 추가수집항목_입력,수정시 자료_checkbox네번째선택결과값
					String extitem_data_ansChoose4_nm = " "; //민원 추가수집항목_입력,수정시 자료_checkbox네번째선택결과값내용
					String extitem_data_ansChoose5 = " "; //민원 추가수집항목_입력,수정시 자료_checkbox다섯번째선택결과값
					String extitem_data_ansChoose5_nm = " "; //민원 추가수집항목_입력,수정시 자료_checkbox다섯번째선택결과값내용
					String extitem_data_ansDt = " "; //민원 추가수집항목_입력,수정시 자료_날짜결과값
					String extitem_data_ansZipCd = " "; //민원 추가수집항목_입력,수정시 자료_우편번호(2015년8월부터는5자리로변경예정)
					String extitem_data_ansAddr1 = " "; //민원 추가수집항목_입력,수정시 자료_기본주소
					String extitem_data_ansAddr2 = " "; //민원 추가수집항목_입력,수정시 자료_상세주소
					String extitem_data_essntlYn = " "; //민원 추가수집항목_입력,수정시 자료_필수여부(Y,N)
					String extitem_data_regDate = " "; //민원 추가수집항목_입력,수정시 자료_응답일자(YYYYMMDDHH24MISS)
					String extitem_data_regNm = " "; //민원 추가수집항목_입력,수정시 자료_응답자

					Node firstChilderenNode = firstChilderenNodes.item(i);

					// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
					if ("#text".equals(firstChilderenNodes.item(i).getNodeName())) {
						continue;
					}

					//System.out.println("childeren.item(i).getNodeName():::" + firstChilderenNodes.item(i).getNodeName());

					Element ele = (Element) firstChilderenNode;
					String firstNodeName = ele.getNodeName();
					// System.out.println("node name: " + nodeName);

					// 첫번째 자식 노드 리스트 중 <Petition>이면...(이건 총 18개)
					if (firstNodeName.equals("Petition")) {

						NodeList secondChildrenNodes = ele.getChildNodes();

						//System.out.println("Petition의 차일드: " + secondChildrenNodes.getLength());

						for (int j = 0; j < secondChildrenNodes.getLength(); j++) {

							Node secondChilderenNode = secondChildrenNodes.item(j);

							// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
							if ("#text".equals(secondChildrenNodes.item(j).getNodeName())) {
								continue;
							}

							String secondchildrenNodeName = secondChilderenNode.getNodeName();

							if (secondchildrenNodeName.equals("opType")) {
								petition_opType = secondChilderenNode.getTextContent();
								//System.out.println("opType:::" + secondChilderenNode.getTextContent());
								
							  // <Petition> 밑의 <Key>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Key")) {

								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("key의 차일드: " + thirdChilderenNodes.getLength());

								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {

									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();

									if (thirdChilderenNodeName.equals("petiNo")) {
										petition_key_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} 

								}

								// <Petition> 밑의 <Data>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Data")){
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("Data의 차일드: " + thirdChilderenNodes.getLength());
								
								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();
									
									if (thirdChilderenNodeName.equals("petiNo")) {
										petition_data_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("title")) {
										petition_data_title = thirdChilderenNode.getTextContent();
										//System.out.println("title:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("reason")) {
										petition_data_reason = thirdChilderenNode.getTextContent();
										//System.out.println("reason:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("returnMethodCode")) {
										petition_data_returnMethodCode = thirdChilderenNode.getTextContent();
										//System.out.println("returnMethodCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("returnMethodName")) {
										petition_data_returnMethodName = thirdChilderenNode.getTextContent();
										//System.out.println("returnMethodName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("receivePathCode")) {
										petition_data_receivePathCode = thirdChilderenNode.getTextContent();
										//System.out.println("receivePathCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("receivePathName")) {
										petition_data_receivePathName = thirdChilderenNode.getTextContent();
										//System.out.println("receivePathName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("parentPetiNo")) {
										petition_data_parentPetiNo = thirdChilderenNode.getTextContent();
										//System.out.println("parentPetiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("parentPetiRelation")) {
										petition_data_parentPetiRelation = thirdChilderenNode.getTextContent(); 
										//System.out.println("parentPetiRelation:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("oldPetiNo")) {
										petition_data_oldPetiNo = thirdChilderenNode.getTextContent();
										//System.out.println("oldPetiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDate")) {
										petition_data_regDate = thirdChilderenNode.getTextContent(); 
										//System.out.println("regDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDateTime")) {
										petition_data_regDateTime = thirdChilderenNode.getTextContent(); 
										//System.out.println("regDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isOpened")) {
										petition_data_isOpened = thirdChilderenNode.getTextContent(); 
										//System.out.println("isOpened:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("complexCode")) {
										petition_data_complexCode = thirdChilderenNode.getTextContent(); 
										//System.out.println("complexCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isHighst")) {
										petition_data_isHighst = thirdChilderenNode.getTextContent(); 
										//System.out.println("isHighst:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isHurry")) {
										petition_data_isHurry = thirdChilderenNode.getTextContent(); 
										//System.out.println("isHurry:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusCode")) {
										petition_data_statusCode = thirdChilderenNode.getTextContent(); 
										//System.out.println("statusCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusName")) {
										petition_data_statusName = thirdChilderenNode.getTextContent(); 
										//System.out.println("statusName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("typeCode")) {
										petition_data_typeCode = thirdChilderenNode.getTextContent();
										//System.out.println("typeCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("typeName")) {
										petition_data_typeName = thirdChilderenNode.getTextContent();
										//System.out.println("typeName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isGroup")) {
										petition_data_isGroup = thirdChilderenNode.getTextContent(); 
										//System.out.println("isGroup:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isManyPeople")) {
										petition_data_isManyPeople = thirdChilderenNode.getTextContent(); 
										//System.out.println("isManyPeople:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("catCode")) {
										petition_data_catCode = thirdChilderenNode.getTextContent(); 
										//System.out.println("catCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("catName")) {
										petition_data_catName = thirdChilderenNode.getTextContent(); 
										//System.out.println("catName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("menuName")) {
										petition_data_menuName = thirdChilderenNode.getTextContent(); 
										//System.out.println("menuName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptOpenYn")) {
										petition_data_deptOpenYn = thirdChilderenNode.getTextContent();
										//System.out.println("deptOpenYn:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("entCivilYnC")) {
										petition_data_entCivilYnC = thirdChilderenNode.getTextContent();
										//System.out.println("entCivilYnC:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("grp3PetiYnC")) {
										petition_data_grp3PetiYnC = thirdChilderenNode.getTextContent();
										//System.out.println("grp3PetiYnC:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("corpName")) {
										petition_data_corpName = thirdChilderenNode.getTextContent(); 
										//System.out.println("corpName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("corpCode")) {
										petition_data_corpCode = thirdChilderenNode.getTextContent(); 
										//System.out.println("corpCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("addCorpName")) {
										petition_data_addCorpName = thirdChilderenNode.getTextContent();
										//System.out.println("addCorpName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("addCorpCode")) {
										petition_data_addCorpCode = thirdChilderenNode.getTextContent(); 
										//System.out.println("addCorpCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("mailAttchYnC")) {
										petition_data_mailAttchYnC = thirdChilderenNode.getTextContent(); 
										//System.out.println("mailAttchYnC:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("Petitioner")) {
										// <Data> 밑의 <Petitioner>에는 하위 노드가 더 있음
										
										NodeList fourthChilderenNodes = thirdChilderenNode.getChildNodes();

										//System.out.println("Petitioner의 차일드: " + fourthChilderenNodes.getLength());
										
										for (int q = 0; q < fourthChilderenNodes.getLength(); q++) {
											
											Node fourthChilderenNode = fourthChilderenNodes.item(q);

											// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
											if ("#text".equals(fourthChilderenNodes.item(q).getNodeName())) {
												continue;
											}
											
											String fourthChilderenNodeName = fourthChilderenNode.getNodeName();
											
											if (fourthChilderenNodeName.equals("name")) {
												petition_data_petitioner_name = fourthChilderenNode.getTextContent();
												//System.out.println("name:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("jobCode")) {
												petition_data_petitioner_jobCode = fourthChilderenNode.getTextContent();
												//System.out.println("jobCode:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("jobName")) {
												petition_data_petitioner_jobName = fourthChilderenNode.getTextContent();
												//System.out.println("jobName:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("zipCode")) {
												petition_data_petitioner_zipCode = fourthChilderenNode.getTextContent();
												//System.out.println("zipCode:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("address1")) {
												petition_data_petitioner_address1 = fourthChilderenNode.getTextContent();
												//System.out.println("address1:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("address2")) {
												petition_data_petitioner_address2 = fourthChilderenNode.getTextContent();
												//System.out.println("address2:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("email")) {
												petition_data_petitioner_email = fourthChilderenNode.getTextContent();
												//System.out.println("email:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("cellPhone")) {
												petition_data_petitioner_cellPhone = fourthChilderenNode.getTextContent();
												//System.out.println("cellPhone:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("linePhone")) {
												petition_data_petitioner_linePhone = fourthChilderenNode.getTextContent();
												//System.out.println("linePhone:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("birthDate")) {
												petition_data_petitioner_birthDate = fourthChilderenNode.getTextContent();
												//System.out.println("birthDate:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("sex")) {
												petition_data_petitioner_sex = fourthChilderenNode.getTextContent();
												//System.out.println("sex:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("isKorean")) {
												petition_data_petitioner_isKorean = fourthChilderenNode.getTextContent();
												//System.out.println("isKorean:::" + fourthChilderenNode.getTextContent());
											}
						
										}
										
									} else if (thirdChilderenNodeName.equals("Cancel")) {
										//<Data> 밑의 <Cancel>에는 하위 노드가 더 있음
										
										NodeList fourthChilderenNodes = thirdChilderenNode.getChildNodes();

										//System.out.println("Cancel의 차일드: " + fourthChilderenNodes.getLength());
										
										for (int q = 0; q < fourthChilderenNodes.getLength(); q++) {

											Node fourthChilderenNode = fourthChilderenNodes.item(q);

											// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
											if ("#text".equals(fourthChilderenNodes.item(q).getNodeName())) {
												continue;
											}
											
											String fourthChilderenNodeName = fourthChilderenNode.getNodeName();
											
											if (fourthChilderenNodeName.equals("CANCEL_YN_C")) {
												petition_data_cancel_cancel_yn_c = fourthChilderenNode.getTextContent();
												//System.out.println("CANCEL_YN_C:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("date")) {
												petition_data_cancel_date = fourthChilderenNode.getTextContent();
												//System.out.println("date:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("dateTime")) {
												petition_data_cancel_dateTime = fourthChilderenNode.getTextContent();
												//System.out.println("dateTime:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("reason")) {
												petition_data_cancel_reason = fourthChilderenNode.getTextContent();
												//System.out.println("reason:::" + fourthChilderenNode.getTextContent());
											}
						
										}						
									}
		
								}	
								
							}

						}
						// 첫번째 자식 노드 리스트 중 <Receipt>이면...
					} else if (firstNodeName.equals("Receipt")) {
						
						NodeList secondChildrenNodes = ele.getChildNodes();

						//System.out.println("Receipt의 차일드: " + secondChildrenNodes.getLength());
						
						for (int j = 0; j < secondChildrenNodes.getLength(); j++) {
							
							Node secondChilderenNode = secondChildrenNodes.item(j);

							// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
							if ("#text".equals(secondChildrenNodes.item(j).getNodeName())) {
								continue;
							}

							String secondchildrenNodeName = secondChilderenNode.getNodeName();
							
							if (secondchildrenNodeName.equals("opType")) {
								receipt_opType = secondChilderenNode.getTextContent();
								//System.out.println("opType:::" + secondChilderenNode.getTextContent());
								
							  // <Receipt> 밑의 <Key>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Key")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("key의 차일드: " + thirdChilderenNodes.getLength());

								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();

									if (thirdChilderenNodeName.equals("petiNo")) {
										receipt_key_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("civilNo")) {
										receipt_key_civilNo = thirdChilderenNode.getTextContent();
										//System.out.println("civilNo:::" + thirdChilderenNode.getTextContent());
									}
									
								}

								 // <Receipt> 밑의 <Data>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Data")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("Data의 차일드: " + thirdChilderenNodes.getLength());
								
								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();
									
									if (thirdChilderenNodeName.equals("petiNo")) {
										receipt_data_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("civilNo")) {
										receipt_data_civilNo = thirdChilderenNode.getTextContent();
										//System.out.println("civilNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDate")) {
										receipt_data_regDate = thirdChilderenNode.getTextContent();
										//System.out.println("regDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDateTime")) {
										receipt_data_regDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("regDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isMainAnc")) {
										receipt_data_isMainAnc = thirdChilderenNode.getTextContent();
										//System.out.println("isMainAnc:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("abstract")) {
										receipt_data_abstract = thirdChilderenNode.getTextContent();
										//System.out.println("abstract:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("planEndDate")) {
										receipt_data_planEndDate = thirdChilderenNode.getTextContent();
										//System.out.println("planEndDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("planEndDateTime")) {
										receipt_data_planEndDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("planEndDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusCode")) {
										receipt_data_statusCode = thirdChilderenNode.getTextContent();
										//System.out.println("statusCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusName")) {
										receipt_data_statusName = thirdChilderenNode.getTextContent();
										//System.out.println("statusName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ancId")) {
										receipt_data_ancId = thirdChilderenNode.getTextContent();
										//System.out.println("ancId:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ancName")) {
										receipt_data_ancName = thirdChilderenNode.getTextContent();
										//System.out.println("ancName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ancRegDate")) {
										receipt_data_ancRegDate = thirdChilderenNode.getTextContent();
										//System.out.println("ancRegDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ancRegDateTime")) {
										receipt_data_ancRegDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("ancRegDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("firstEndDate")) {
										receipt_data_firstEndDate = thirdChilderenNode.getTextContent();
										//System.out.println("firstEndDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("firstEndDateTime")) {
										receipt_data_firstEndDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("firstEndDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("doResultDate")) {
										receipt_data_doResultDate = thirdChilderenNode.getTextContent();
										//System.out.println("doResultDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("doResultDateTime")) {
										receipt_data_doResultDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("doResultDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("activeYN")) {
										receipt_data_activeYN = thirdChilderenNode.getTextContent();
										//System.out.println("activeYN:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("busiBranCode")) {
										receipt_data_busiBranCode = thirdChilderenNode.getTextContent();
										//System.out.println("busiBranCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("busiBranCodeName")) {
										receipt_data_busiBranCodeName = thirdChilderenNode.getTextContent();
										//System.out.println("busiBranCodeName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("orgOpenYn")) {
										receipt_data_orgOpenYn = thirdChilderenNode.getTextContent();
										//System.out.println("orgOpenYn:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("Expand")) {
										//<Data> 밑의 <Expand>에는 하위 노드가 더 있음
										
										NodeList fourthChilderenNodes = thirdChilderenNode.getChildNodes();

										//System.out.println("Expand의 차일드: " + fourthChilderenNodes.getLength());
										
										for (int q = 0; q < fourthChilderenNodes.getLength(); q++) {
											
											Node fourthChilderenNode = fourthChilderenNodes.item(q);

											// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
											if ("#text".equals(fourthChilderenNodes.item(q).getNodeName())) {
												continue;
											}
											
											String fourthChilderenNodeName = fourthChilderenNode.getNodeName();
											
											if (fourthChilderenNodeName.equals("title")) {
												receipt_data_expand_title= fourthChilderenNode.getTextContent();
												//System.out.println("title:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("reason")) {
												receipt_data_expand_reason = fourthChilderenNode.getTextContent();
												//System.out.println("reason:::" + fourthChilderenNode.getTextContent());
											}
		
										}
										
									} else if (thirdChilderenNodeName.equals("Satisfy")) {
										//<Data> 밑의 <Satisfy>에는 하위 노드가 더 있음

										NodeList fourthChilderenNodes = thirdChilderenNode.getChildNodes();

										//System.out.println("Satisfy의 차일드: " + fourthChilderenNodes.getLength());
										
										for (int q = 0; q < fourthChilderenNodes.getLength(); q++) {
											
											Node fourthChilderenNode = fourthChilderenNodes.item(q);

											// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
											if ("#text".equals(fourthChilderenNodes.item(q).getNodeName())) {
												continue;
											}
											
											String fourthChilderenNodeName = fourthChilderenNode.getNodeName();
											
											if (fourthChilderenNodeName.equals("Content")) {
												//<Satisfy> 밑의 <Content>에는 하위 노드가 더 있음
												
												NodeList fifthChilderenNodes = fourthChilderenNode.getChildNodes();
												
												//System.out.println("Content의 차일드: " + fifthChilderenNodes.getLength());
												
												for (int t = 0; t < fifthChilderenNodes.getLength(); t++) {
													
													Node fifthChilderenNode = fifthChilderenNodes.item(t);
													
													// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
													if ("#text".equals(fifthChilderenNodes.item(t).getNodeName())) {
														continue;
													}
													
													String fifthChilderenNodeName = fifthChilderenNode.getNodeName();
													
													if (fifthChilderenNodeName.equals("code")) {
														receipt_data_satisfy_content_code = fifthChilderenNode.getTextContent();
														//System.out.println("code:::" + fifthChilderenNode.getTextContent());
													} else if (fifthChilderenNodeName.equals("name")) {
														receipt_data_satisfy_content_name = fifthChilderenNode.getTextContent();
														//System.out.println("name:::" + fifthChilderenNode.getTextContent());
													}	
												}				
												
											} else if (fourthChilderenNodeName.equals("moreWord")) {
												receipt_data_satisfy_moreWord = fourthChilderenNode.getTextContent();
												//System.out.println("moreWord:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("regDate")) {
												receipt_data_satisfy_regDate = fourthChilderenNode.getTextContent();
												//System.out.println("regDate:::" + fourthChilderenNode.getTextContent());
											} else if (fourthChilderenNodeName.equals("regDateTime")) {
												receipt_data_satisfy_regDateTime = fourthChilderenNode.getTextContent();
												//System.out.println("regDateTime:::" + fourthChilderenNode.getTextContent());
											}
										}
		
									}

								}
		
							}
					
						}
						// 첫번째 자식 노드 리스트 중 <Process>면...
					} else if (firstNodeName.equals("Process")) {
						
						NodeList secondChildrenNodes = ele.getChildNodes();

						//System.out.println("Process의 차일드: " + secondChildrenNodes.getLength());
						
						for (int j = 0; j < secondChildrenNodes.getLength(); j++) {
							
							Node secondChilderenNode = secondChildrenNodes.item(j);

							// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
							if ("#text".equals(secondChildrenNodes.item(j).getNodeName())) {
								continue;
							}

							String secondchildrenNodeName = secondChilderenNode.getNodeName();

							if (secondchildrenNodeName.equals("opType")) {
								process_opType = secondChilderenNode.getTextContent();
								//System.out.println("opType:::" + secondChilderenNode.getTextContent());
								
							  // <Process> 밑의 <Key>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Key")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("key의 차일드: " + thirdChilderenNodes.getLength());

								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();

									if (thirdChilderenNodeName.equals("petiNo")) {
										process_key_petiNo= thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("civilNo")) {
										process_key_civilNo = thirdChilderenNode.getTextContent();
										//System.out.println("civilNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptCode")) {
										process_key_deptCode= thirdChilderenNode.getTextContent();
										//System.out.println("deptCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("seq")) {
										process_key_seq = thirdChilderenNode.getTextContent();
										//System.out.println("seq:::" + thirdChilderenNode.getTextContent());
									}

								}
								// <Process> 밑의 <Data>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Data")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("Data의 차일드: " + thirdChilderenNodes.getLength());
								
								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();
									
									if (thirdChilderenNodeName.equals("petiNo")) {
										process_data_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("civilNo")) {
										process_data_civilNo = thirdChilderenNode.getTextContent();
										//System.out.println("civilNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptCode")) {
										process_data_deptCode = thirdChilderenNode.getTextContent();
										//System.out.println("deptCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("seq")) {
										process_data_seq = thirdChilderenNode.getTextContent();
										//System.out.println("seq:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptName")) {
										process_data_deptName = thirdChilderenNode.getTextContent();
										//System.out.println("deptName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("dutyId")) {
										process_data_dutyId = thirdChilderenNode.getTextContent();
										//System.out.println("dutyId:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("dutyDeptCode")) {
										process_data_dutyDeptCode = thirdChilderenNode.getTextContent();
										//System.out.println("dutyDeptCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("dutyName")) {
										process_data_dutyName = thirdChilderenNode.getTextContent();
										//System.out.println("dutyName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("isMainDept")) {
										process_data_isMainDept = thirdChilderenNode.getTextContent();
										//System.out.println("isMainDept:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDate")) {
										process_data_regDate = thirdChilderenNode.getTextContent();
										//System.out.println("regDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDateTime")) {
										process_data_regDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("regDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("doDate")) {
										process_data_doDate = thirdChilderenNode.getTextContent();
										//System.out.println("doDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("doDateTime")) {
										process_data_doDateTime = thirdChilderenNode.getTextContent();
										//System.out.println("doDateTime:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("content")) {
										process_data_content = thirdChilderenNode.getTextContent();
										//System.out.println("content:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusCode")) {
										process_data_statusCode = thirdChilderenNode.getTextContent();
										//System.out.println("statusCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("statusName")) {
										process_data_statusName = thirdChilderenNode.getTextContent();
										//System.out.println("statusName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("abstract")) {
										process_data_abstract = thirdChilderenNode.getTextContent();
										//System.out.println("abstract:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("restatusCode")) {
										process_data_restatusCode = thirdChilderenNode.getTextContent();
										//System.out.println("restatusCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("restatusName")) {
										process_data_restatusName = thirdChilderenNode.getTextContent();
										//System.out.println("restatusName:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("activeYN")) {
										process_data_activeYN = thirdChilderenNode.getTextContent();
										//System.out.println("activeYN:::" + thirdChilderenNode.getTextContent());
									}
								}	
							}	
						}
						// 첫번째 자식 노드 리스트 중 <Extitem>이면...
					} else if (firstNodeName.equals("Extitem")) {
						
						NodeList secondChildrenNodes = ele.getChildNodes();

						//System.out.println("Extitem의 차일드: " + secondChildrenNodes.getLength());
						
						for (int j = 0; j < secondChildrenNodes.getLength(); j++) {
							
							Node secondChilderenNode = secondChildrenNodes.item(j);

							// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
							if ("#text".equals(secondChildrenNodes.item(j).getNodeName())) {
								continue;
							}

							String secondchildrenNodeName = secondChilderenNode.getNodeName();
							
							if (secondchildrenNodeName.equals("opType")) {
								extitem_opType = secondChilderenNode.getTextContent();
								//System.out.println("opType:::" + secondChilderenNode.getTextContent());
								
							  // <Extitem> 밑의 <Key>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Key")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("key의 차일드: " + thirdChilderenNodes.getLength());

								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();
									
									if (thirdChilderenNodeName.equals("petiNo")) {
										extitem_key_petiNo = thirdChilderenNode.getTextContent();
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptCode")) {
										extitem_key_deptCode = thirdChilderenNode.getTextContent();
										//System.out.println("deptCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("channel")) {
										extitem_key_channel = thirdChilderenNode.getTextContent();
										//System.out.println("channel:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("menuCode")) {
										extitem_key_menuCode = thirdChilderenNode.getTextContent();
										//System.out.println("menuCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("code")) {
										extitem_key_code = thirdChilderenNode.getTextContent();
										//System.out.println("code:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("orderNum")) {
										extitem_key_orderNum = thirdChilderenNode.getTextContent();
										//System.out.println("orderNum:::" + thirdChilderenNode.getTextContent());
									}		
								}
								// <Extitem> 밑의 <Data>에는 하위 노드가 더 있음
							} else if (secondchildrenNodeName.equals("Data")) {
								
								NodeList thirdChilderenNodes = secondChilderenNode.getChildNodes();

								//System.out.println("Data의 차일드: " + thirdChilderenNodes.getLength());
								
								for (int p = 0; p < thirdChilderenNodes.getLength(); p++) {
									
									Node thirdChilderenNode = thirdChilderenNodes.item(p);

									// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
									if ("#text".equals(thirdChilderenNodes.item(p).getNodeName())) {
										continue;
									}

									String thirdChilderenNodeName = thirdChilderenNode.getNodeName();
									
									if (thirdChilderenNodeName.equals("petiNo")) {
										extitem_data_petiNo = thirdChilderenNode.getTextContent(); 
										//System.out.println("petiNo:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("deptCode")) {
										extitem_data_deptCode = thirdChilderenNode.getTextContent();
										//System.out.println("deptCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("channel")) {
										extitem_data_channel = thirdChilderenNode.getTextContent();
										//System.out.println("channel:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("menuCode")) {
										extitem_data_menuCode = thirdChilderenNode.getTextContent();
										//System.out.println("menuCode:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("code")) {
										extitem_data_code = thirdChilderenNode.getTextContent();
										//System.out.println("code:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("orderNum")) {
										extitem_data_orderNum = thirdChilderenNode.getTextContent();
										//System.out.println("orderNum:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("title")) {
										extitem_data_title = thirdChilderenNode.getTextContent();
										//System.out.println("title:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("extDesc")) {
										extitem_data_extDesc = thirdChilderenNode.getTextContent();
										//System.out.println("extDesc:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("extType")) {
										extitem_data_extType = thirdChilderenNode.getTextContent();
										//System.out.println("extType:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("txtLen")) {
										extitem_data_txtLen = thirdChilderenNode.getTextContent();
										//System.out.println("txtLen:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansTxt")) {
										extitem_data_ansTxt = thirdChilderenNode.getTextContent();
										//System.out.println("ansTxt:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose0")) {
										extitem_data_ansChoose0 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose0:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose0_nm")) {
										extitem_data_ansChoose0_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose0_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose1")) {
										extitem_data_ansChoose1 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose1:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose1_nm")) {
										extitem_data_ansChoose1_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose1_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose2")) {
										extitem_data_ansChoose2 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose2:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose2_nm")) {
										extitem_data_ansChoose2_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose2_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose3")) {
										extitem_data_ansChoose3 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose3:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose3_nm")) {
										extitem_data_ansChoose3_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose3_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose4")) {
										extitem_data_ansChoose4 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose4:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose4_nm")) {
										extitem_data_ansChoose4_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose4_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose5")) {
										extitem_data_ansChoose5 = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose5:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansChoose5_nm")) {
										extitem_data_ansChoose5_nm = thirdChilderenNode.getTextContent();
										//System.out.println("ansChoose5_nm:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansDt")) {
										extitem_data_ansDt = thirdChilderenNode.getTextContent();
										//System.out.println("ansDt:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansZipCd")) {
										extitem_data_ansZipCd = thirdChilderenNode.getTextContent();
										//System.out.println("ansZipCd:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansAddr1")) {
										extitem_data_ansAddr1 = thirdChilderenNode.getTextContent(); 
										//System.out.println("ansAddr1:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("ansAddr2")) {
										extitem_data_ansAddr2 = thirdChilderenNode.getTextContent();
										//System.out.println("ansAddr2:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("essntlYn")) {
										extitem_data_essntlYn = thirdChilderenNode.getTextContent();
										//System.out.println("essntlYn:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regDate")) {
										extitem_data_regDate = thirdChilderenNode.getTextContent();
										//System.out.println("regDate:::" + thirdChilderenNode.getTextContent());
									} else if (thirdChilderenNodeName.equals("regNm")) {
										extitem_data_regNm = thirdChilderenNode.getTextContent();
										//System.out.println("regNm:::" + thirdChilderenNode.getTextContent());
									}	
								}	
							}
						}	
					}

					//여기서 파일 작성
					try {
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.write(JsonParser.colWrite_String_epe(petition_opType)); // 민원신청_자료변환유형코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_key_petiNo)); //민원신청_수정,삭제시 키_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petiNo)); //민원신청_입력,수정시 자료_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_title)); //민원신청_입력,수정시 자료_신청제목
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_reason)); //민원신청_입력,수정시 자료_내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_returnMethodCode)); //민원신청_입력,수정시 자료_결과통보방법코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_returnMethodName)); //민원신청_입력,수정시 자료_결과통보방법명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_receivePathCode)); //민원신청_입력,수정시 자료_접수경로기관코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_receivePathName)); //민원신청_입력,수정시 자료_접수경로기관명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_parentPetiNo)); //민원신청_입력,수정시 자료_모신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_parentPetiRelation)); //민원신청_입력,수정시 자료_모신청과의관계
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_oldPetiNo)); //민원신청_입력,수정시 자료_기존신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_regDate)); //민원신청_입력,수정시 자료_등록일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_regDateTime)); //민원신청_입력,수정시 자료_등록일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_isOpened)); //민원신청_입력,수정시 자료_공개여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_complexCode)); //민원신청_입력,수정시 자료_복합처리
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_isHighst)); //민원신청_입력,수정시 자료_지정민원여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_isHurry)); //민원신청_입력,수정시 자료_긴급민원여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_statusCode)); //민원신청_입력,수정시 자료_처리상태코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_statusName)); //민원신청_입력,수정시 자료_처리상태명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_typeCode)); //민원신청_입력,수정시 자료_민원유형코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_typeName)); //민원신청_입력,수정시 자료_민원유형명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_isGroup)); //민원신청_입력,수정시 자료_집단민원여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_isManyPeople)); //민원신청_입력,수정시 자료_다수인민원여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_catCode)); //민원신청_입력,수정시 자료_민원카테고리코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_catName)); //민원신청_입력,수정시 자료_민원카테고리명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_menuName)); //민원신청_입력,수정시 자료_민원신청프레임메뉴명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_deptOpenYn)); //민원신청_입력,수정시 자료_타부서공개여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_entCivilYnC)); //민원신청_입력,수정시 자료_기업민원여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_grp3PetiYnC)); //민원신청_입력,수정시 자료_단체민원여부(Y,N,C)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_corpName)); //민원신청_입력,수정시 자료_기업명(상호)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_corpCode)); //민원신청_입력,수정시 자료_사업자등록번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_addCorpName)); //민원신청_입력,수정시 자료_기업명(상호)(보정)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_addCorpCode)); //민원신청_입력,수정시 자료_사업자등록번호(보정)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_mailAttchYnC)); //민원신청_입력,수정시 자료_메일답변첨부여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_name)); //민원신청_입력,수정시 자료_신청인_신청인명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_jobCode)); //민원신청_입력,수정시 자료_신청인_신청인직업코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_jobName)); //민원신청_입력,수정시 자료_신청인_신청인직업명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_zipCode)); //민원신청_입력,수정시 자료_신청인_우편번호(2015년8월부터는 5자리로 변경예정)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_address1)); //민원신청_입력,수정시 자료_신청인_기본주소
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_address2)); //민원신청_입력,수정시 자료_신청인_상세주소
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_email)); //민원신청_입력,수정시 자료_신청인_이메일주소
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_cellPhone)); //민원신청_입력,수정시 자료_신청인_핸드폰번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_linePhone)); //민원신청_입력,수정시 자료_신청인_전화번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_birthDate)); //민원신청_입력,수정시 자료_신청인_생일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_sex)); //민원신청_입력,수정시 자료_신청인_성별(1:남,2:여)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_petitioner_isKorean)); //민원신청_입력,수정시 자료_신청인_내국인여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_cancel_cancel_yn_c)); //민원신청_입력,수정시 자료_취하_취하여부
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_cancel_date)); //민원신청_입력,수정시 자료_취하_취하일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_cancel_dateTime)); //민원신청_입력,수정시 자료_취하_취하일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(petition_data_cancel_reason)); //민원신청_입력,수정시 자료_취하_사유
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_opType)); //민원접수_자료변환유형코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_key_petiNo)); //민원접수_수정,삭제시 키_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_key_civilNo)); //민원접수_수정,삭제시 키_접수번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_petiNo)); //민원접수_입력,수정시 자료_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_civilNo)); //민원접수_입력,수정시 자료_접수번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_regDate)); //민원접수_입력,수정시 자료_등록일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_regDateTime)); //민원접수_입력,수정시 자료_등록일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_isMainAnc)); //민원접수_입력,수정시 자료_주무부처여부
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_abstract)); //민원접수_입력,수정시 자료_요지
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_planEndDate)); //민원접수_입력,수정시 자료_처리종료예정일(YYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_planEndDateTime)); //민원접수_입력,수정시 자료_처리종료예정일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_statusCode)); //민원접수_입력,수정시 자료_기관별처리상태코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_statusName)); //민원접수_입력,수정시 자료_기관별처리상태명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_ancId)); //민원접수_입력,수정시 자료_접수담당자ID
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_ancName)); //민원접수_입력,수정시 자료_접수담당자명(*)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_ancRegDate)); //민원접수_입력,수정시 자료_접수일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_ancRegDateTime)); //민원접수_입력,수정시 자료_접수일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_firstEndDate)); //민원접수_입력,수정시 자료_최초종료예정일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_firstEndDateTime)); //민원접수_입력,수정시 자료_최초종료예정일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_doResultDate)); //민원접수_입력,수정시 자료_처리결과통보일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_doResultDateTime)); //민원접수_입력,수정시 자료_처리결과통보일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_activeYN)); //민원접수_입력,수정시 자료_자료유효여부(Y:유효,N:비활성된자료)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_busiBranCode)); //민원접수_입력,수정시 자료_분류코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_busiBranCodeName)); //민원접수_입력,수정시 자료_분류코드이름
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_orgOpenYn)); //민원접수_입력,수정시 자료_타기관공개여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_expand_title)); //민원접수_입력,수정시 자료_처리연장_제목
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_expand_reason)); //민원접수_입력,수정시 자료_처리연장_사유
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_satisfy_content_code)); //민원접수_입력,수정시 자료_만족도_내용_만족도코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_satisfy_content_name)); //민원접수_입력,수정시 자료_만족도_내용_만족도명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_satisfy_moreWord)); //민원접수_입력,수정시 자료_만족도_한마디더
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_satisfy_regDate)); //민원접수_입력,수정시 자료_만족도_만족도입력일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(receipt_data_satisfy_regDateTime)); //민원접수_입력,수정시 자료_만족도_만족도입력일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_opType)); //처리(부서별)자료변환유형코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_key_petiNo)); //처리(부서별)_수정,삭제시 키_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_key_civilNo)); //처리(부서별)_수정,삭제시 키_접수번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_key_deptCode)); //처리(부서별)_수정,삭제시 키_부서코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_key_seq)); //처리(부서별)_수정,삭제시 키_처리부서일렬번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_petiNo)); //처리(부서별)_입력,수정시 자료_신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_civilNo)); //처리(부서별)_입력,수정시 자료_접수번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_deptCode)); //처리(부서별)_입력,수정시 자료_부서코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_seq)); //처리(부서별)_입력,수정시 자료_처리부서일렬번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_deptName)); //처리(부서별)_입력,수정시 자료_부서명(*)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_dutyId)); //처리(부서별)_입력,수정시 자료_담당자아이디
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_dutyDeptCode)); //처리(부서별)_입력,수정시 자료_담당자부서코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_dutyName)); //처리(부서별)_입력,수정시 자료_담당자명(*)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_isMainDept)); //처리(부서별)_입력,수정시 자료_주무부서여부
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_regDate)); //처리(부서별)_입력,수정시 자료_처리등록일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_regDateTime)); //처리(부서별)_입력,수정시 자료_처리등록일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_doDate)); //처리(부서별)_입력,수정시 자료_처리일(YYYYMMDD)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_doDateTime)); //처리(부서별)_입력,수정시 자료_처리일시(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_content)); //처리(부서별)_입력,수정시 자료_처리결과
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_statusCode)); //처리(부서별)_입력,수정시 자료_부서별처리상태코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_statusName)); //처리(부서별)_입력,수정시 자료_부서별처리상태명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_abstract)); //처리(부서별)_입력,수정시 자료_요약
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_restatusCode)); //처리(부서별)_입력,수정시 자료_재분류지정상태코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_restatusName)); //처리(부서별)_입력,수정시 자료_재분류지정상태명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(process_data_activeYN)); //처리(부서별)_입력,수정시 자료_자료유효여부(Y:유효,N:비활성된자료)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_opType)); //민원 추가수집항목_자료변환유형코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_petiNo)); //민원 추가수집항목_수정,삭제시 키_민원신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_deptCode)); //민원 추가수집항목_수정,삭제시 키_기관코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_channel)); //민원 추가수집항목_수정,삭제시 키_채널코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_menuCode)); //민원 추가수집항목_수정,삭제시 키_메뉴코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_code)); //민원 추가수집항목_수정,삭제시 키_항목코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_key_orderNum)); //민원 추가수집항목_수정,삭제시 키_표현순서
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_petiNo)); //민원 추가수집항목_입력,수정시 자료_민원신청번호
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_deptCode)); //민원 추가수집항목_입력,수정시 자료_기관코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_channel)); //민원 추가수집항목_입력,수정시 자료_채널코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_menuCode)); //민원 추가수집항목_입력,수정시 자료_메뉴코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_code)); //민원 추가수집항목_입력,수정시 자료_항목코드
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_orderNum)); //민원 추가수집항목_입력,수정시 자료_표현순서
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_title)); //민원 추가수집항목_입력,수정시 자료_제목
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_extDesc)); //민원 추가수집항목_입력,수정시 자료_추가수집항목설명
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_extType)); //민원 추가수집항목_입력,수정시 자료_질문속성타입(테스트,라디오버튼,체크박스...)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_txtLen)); //민원 추가수집항목_입력,수정시 자료_글자수(길이)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansTxt)); //민원 추가수집항목_입력,수정시 자료_질문속성타입이텍스트일경우응답내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose0)); //민원 추가수집항목_입력,수정시 자료_radio,selectbox결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose0_nm)); //민원 추가수집항목_입력,수정시 자료_radio,selectbox결과값의응답내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose1)); //민원 추가수집항목_입력,수정시 자료_checkbox첫번째선택결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose1_nm)); //민원 추가수집항목_입력,수정시 자료_checkbox첫번째선택결과값내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose2)); //민원 추가수집항목_입력,수정시 자료_checkbox두번째선택결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose2_nm)); //민원 추가수집항목_입력,수정시 자료_checkbox두번째선택결과값내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose3)); //민원 추가수집항목_입력,수정시 자료_checkbox세번째선택결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose3_nm)); //민원 추가수집항목_입력,수정시 자료_checkbox세번째선택결과값내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose4)); //민원 추가수집항목_입력,수정시 자료_checkbox네번째선택결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose4_nm)); //민원 추가수집항목_입력,수정시 자료_checkbox네번째선택결과값내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose5)); //민원 추가수집항목_입력,수정시 자료_checkbox다섯번째선택결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansChoose5_nm)); //민원 추가수집항목_입력,수정시 자료_checkbox다섯번째선택결과값내용
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansDt)); //민원 추가수집항목_입력,수정시 자료_날짜결과값
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansZipCd)); //민원 추가수집항목_입력,수정시 자료_우편번호(2015년8월부터는5자리로변경예정)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansAddr1)); //민원 추가수집항목_입력,수정시 자료_기본주소
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_ansAddr2)); //민원 추가수집항목_입력,수정시 자료_상세주소
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_essntlYn)); //민원 추가수집항목_입력,수정시 자료_필수여부(Y,N)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_regDate)); //민원 추가수집항목_입력,수정시 자료_응답일자(YYYYMMDDHH24MISS)
						pw.write("|^");
						pw.write(JsonParser.colWrite_String_epe(extitem_data_regNm)); //민원 추가수집항목_입력,수정시 자료_응답자
						pw.println();
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("xml 파싱 프로세스 완료.");

		} else {
			System.out.println("xml 파일의 경로가 필요합니다!");
			System.exit(-1);
		}

	}

}
