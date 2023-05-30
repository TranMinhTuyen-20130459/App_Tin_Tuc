package com.example.newsapp.utils;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParser {
    public Document getDocument(String xml) {
        Document document = null;
        // Khởi tạo một DocumentBuilderFactory để đọc XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            // Tạo một DocumentBuilder từ DocumentBuilderFactory
            DocumentBuilder db = factory.newDocumentBuilder();

            // Tạo một nguồn đầu vào từ chuỗi XML
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            is.setEncoding("UTF-8");

            // Phân tích chuỗi XML thành một đối tượng Document
            document = db.parse(is);
        } catch (ParserConfigurationException e) {
            // Xử lý ngoại lệ ParserConfigurationException
            Log.e("Error: ", e.getMessage(), e);
            return null;
        } catch (IOException e) {
            // Xử lý ngoại lệ IOException
            Log.e("Error: ", e.getMessage(), e);
            return null;
        } catch (SAXException e) {
            // Xử lý ngoại lệ SAXException
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }
        return document;
    }

    public String getValue(Element item, String name) {
        // Lấy danh sách các phần tử con có tên tương ứng
        NodeList nodes = item.getElementsByTagName(name);
        // Trả về giá trị của phần tử đầu tiên bằng cách gọi phương thức getTextNodeValue
        return this.getTextNodeValue(nodes.item(0));
    }

    private final String getTextNodeValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                // Duyệt qua các nút con
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    // Kiểm tra nếu nút là nút văn bản
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        // Trả về giá trị văn bản của nút
                        return child.getNodeValue();
                    }
                }
            }
        }
        // Trả về chuỗi rỗng nếu không tìm thấy giá trị văn bản
        return "";
    }
}
