package com.example.newsapp.utils;

import androidx.annotation.Nullable;

import com.example.newsapp.models.News;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParser {

    public String getValue(Pattern pattern, String data) {
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1) == null ? "" : matcher.group(1);
        }
        return "";
    }

    public String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    private String getTextNodeValue(Node node) {
        if (node != null && node.hasChildNodes()) {
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == Node.TEXT_NODE) {
                    return child.getNodeValue();
                } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                    return child.getTextContent();
                }
            }
        }
        return "";
    }

    @Nullable
    public News parseFirst(String rss) {
        final Pattern desPattern = Pattern.compile("</br>\\s*(.*)");
        final Pattern imgPattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new ByteArrayInputStream(rss.getBytes()));

            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String title = getValue(element, "title");
                String link = getValue(element, "link");
                String date = getValue(element, "pubDate");

                String cdata = element.getElementsByTagName("description").item(0).getTextContent();
                String linkImage = getValue(imgPattern, cdata);
                String description = getValue(desPattern, cdata);

                if (title.isEmpty() || link.isEmpty() || date.isEmpty() || linkImage.isEmpty() || description.isEmpty())
                    continue;

                return new News(title, link, linkImage, date, description);
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
        return null;
    }
}