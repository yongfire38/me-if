package epe.epoumb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetEpeEpoumb {

	// 국민신문고 대비해서 xml 파싱 테스트
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse("PRI_04_XML.xml");

		document.getDocumentElement().normalize();

		// 첫 번째 방법

		 /*System.out.println("Root element :" +
		 document.getDocumentElement().getNodeName());ㅁ
		 
		 NodeList item = document.getElementsByTagName("item");
		 
		 for(int i = 0; i < item.getLength(); i++){
		 
			 String rn = " "; // 행번호 
			 String ptNo = " "; // 조사지점코드 
			 String ptNm = " "; // 조사지점명 
			 String wmcymd = " "; // 채취일 
			 String act1 = " "; // 측정값 Cs-134(세슘) 
			 String act2 = " "; // 측정값 Cs-137(세슘) 
			 String act3 = " "; //측정값 I-131(요드)
		 
		 for(Node node = item.item(i).getFirstChild(); node != null; node=node.getNextSibling()){
		 
		 if(node.getNodeName().equals("rn")){
			 rn = node.getTextContent(); 
		 }else if(node.getNodeName().equals("ptNo")){
			 ptNo = node.getTextContent(); 
		 } else if(node.getNodeName().equals("ptNm")){
			 ptNm = node.getTextContent(); 
		 } else if(node.getNodeName().equals("wmcymd")){
			 wmcymd = node.getTextContent(); 
		 } else if(node.getNodeName().equals("act1")){
			 act1 = node.getTextContent(); 
		 } else if(node.getNodeName().equals("act2")){
			 act2 = node.getTextContent();
		 } else if(node.getNodeName().equals("act3")){
			 act3 = node.getTextContent(); } 
		 }
		 
			 System.out.println("rn:::"+rn ); System.out.println("ptNo:::"+ptNo );
			 System.out.println("ptNm:::"+ptNm );
			 System.out.println("wmcymd:::"+wmcymd );
			 System.out.println("act1:::"+act1 );
			 System.out.println("act2:::"+act2 );
			 System.out.println("act3:::"+act3 );
		 
		 }*/
		 

		// 두 번째 방법

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		String resultCode_col = " "; // 결과코드
		String resultMsg_col = " "; // 결과메시지

		try {

			// 1.응답 코드
			XPathExpression headerExpr = xpath.compile("/response/header");
			NodeList headerList = (NodeList) headerExpr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < headerList.getLength(); i++) {

				// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
				if ("#text".equals(headerList.item(i).getNodeName())) {
					continue;
				}

				NodeList child = headerList.item(i).getChildNodes();

				for (int j = 0; j < child.getLength(); j++) {

					Node node = child.item(j);

					if (node.getNodeName().equals("resultCode")) {
						resultCode_col = node.getTextContent();
					} else if (node.getNodeName().equals("resultMsg")) {
						resultMsg_col = node.getTextContent();
					}

				}

				/*System.out.println("resultCode_col:::" + resultCode_col);
				System.out.println("resultMsg_col:::" + resultMsg_col);*/

			}

			// 2.가장 마지막인 numOfRows, pageNo, totalCount

			XPathExpression numOfRowsExpr = xpath.compile("/response/body/numOfRows");
			XPathExpression pageNoExpr = xpath.compile("/response/body/pageNo");
			XPathExpression totalCountExpr = xpath.compile("/response/body/totalCount");

			NodeList numOfRowsList = (NodeList) numOfRowsExpr.evaluate(document, XPathConstants.NODESET);
			NodeList pageNoList = (NodeList) pageNoExpr.evaluate(document, XPathConstants.NODESET);
			NodeList totalCountList = (NodeList) totalCountExpr.evaluate(document, XPathConstants.NODESET);

			// 어차피 하나뿐이라...
			String numOfRows = numOfRowsList.item(0).getTextContent();
			String pageNo = pageNoList.item(0).getTextContent();
			String totalCount = totalCountList.item(0).getTextContent();

			// 3.본문이라고 할 수 있는 items 밑의 데이터

			// 절대 경로로 노드를 지정하는 방법
			// XPathExpression itemsExpr =xpath.compile("/response/body/items/item");
			// 상대 경로로 노드를 지정하는 방법
			XPathExpression itemsExpr = xpath.compile("//items/item");
			NodeList itemsList = (NodeList) itemsExpr.evaluate(document, XPathConstants.NODESET);

			// System.out.println("nodeList.getLength()"+nodeList.getLength());

			for (int i = 0; i < itemsList.getLength(); i++) {

				// xml이 들여쓰기 안 되어 있으면 노드를 정확하게 잡지 못하는 현상 방지
				if ("#text".equals(itemsList.item(i).getNodeName())) {
					continue;
				}

				String rn = " "; // 행번호
				String ptNo = " "; // 조사지점코드
				String ptNm = " "; // 조사지점명
				String wmcymd = " "; // 채취일
				String act1 = " "; // 측정값 Cs-134(세슘)
				String act2 = " "; // 측정값 Cs-137(세슘)
				String act3 = " "; // 측정값 I-131(요드)

				NodeList child = itemsList.item(i).getChildNodes();
				for (int j = 0; j < child.getLength(); j++) {

					Node node = child.item(j);

					if (node.getNodeName().equals("rn")) {
						rn = node.getTextContent();
					} else if (node.getNodeName().equals("ptNo")) {
						ptNo = node.getTextContent();
					} else if (node.getNodeName().equals("ptNm")) {
						ptNm = node.getTextContent();
					} else if (node.getNodeName().equals("wmcymd")) {
						wmcymd = node.getTextContent();
					} else if (node.getNodeName().equals("act1")) {
						act1 = node.getTextContent();
					} else if (node.getNodeName().equals("act2")) {
						act2 = node.getTextContent();
					} else if (node.getNodeName().equals("act3")) {
						act3 = node.getTextContent();
					}

				}

				/*
				 * System.out.println("rn:::"+rn );
				 * System.out.println("ptNo:::"+ptNo );
				 * System.out.println("ptNm:::"+ptNm );
				 * System.out.println("wmcymd:::"+wmcymd );
				 * System.out.println("act1:::"+act1 );
				 * System.out.println("act2:::"+act2 );
				 * System.out.println("act3:::"+act3 );
				 */

				File file = new File("TIF_PRI_04_XML_RESULT.dat");

				// step 4. 파일에 쓰기
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write(resultCode_col.trim());
					pw.write("|^");
					pw.write(resultMsg_col.trim());
					pw.write("|^");
					pw.write(rn.trim());
					pw.write("|^");
					pw.write(ptNo.trim());
					pw.write("|^");
					pw.write(ptNm.trim());
					pw.write("|^");
					pw.write(wmcymd.trim());
					pw.write("|^");
					pw.write(act1.trim());
					pw.write("|^");
					pw.write(act2.trim());
					pw.write("|^");
					pw.write(act3.trim());
					pw.write("|^");
					pw.write(numOfRows.trim());
					pw.write("|^");
					pw.write(pageNo.trim());
					pw.write("|^");
					pw.write(totalCount.trim());
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

		System.out.println("xml 파싱 완료.");

	}

}
