package com.example.newsapp.utils;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.newsapp.MainActivity;
import com.example.newsapp.models.News;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReadRSS extends AsyncTask<String, Void, Document> {
    private MainActivity mainActivity;
    ArrayList<News> listNews = new ArrayList<>();

    public ArrayList<News> getListNews() {
        return listNews;
    }

    ProgressDialog dialog;

    public ReadRSS(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mainActivity);
        dialog.setMessage("Đang tải...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Document doInBackground(String... urls) throws RuntimeException {
        try {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(urls[0]);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Document doc) {
        dialog.dismiss();
        XMLDOMParser xmldomParser = new XMLDOMParser();

        NodeList nodeList = doc.getElementsByTagName("item");

        String title = "", link = "", linkImage = null, date = "";

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);

            NodeList titleElement = element.getElementsByTagName("title");

            if (titleElement.getLength() > 0) {
                title = titleElement.item(0).getTextContent();
            }

            NodeList linkElement = element.getElementsByTagName("link");

            if (linkElement.getLength() > 0) {
                link = linkElement.item(0).getTextContent();
            }

            NodeList dateElement = element.getElementsByTagName("pubDate");

            if (dateElement.getLength() > 0) {
                date = dateElement.item(0).getTextContent();
            }


            // Truy cập phần tử CDATA trong phần mô tả (description)
            NodeList descriptionList = element.getElementsByTagName("description");
            if (descriptionList.getLength() > 0) {
                Element descriptionElement = (Element) descriptionList.item(0);
                // Lấy nội dung trong phần tử CDATA
                String cdata = descriptionElement.getTextContent();

                // Trích xuất đường dẫn hình ảnh từ phần tử CDATA
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher m = p.matcher(cdata);
                if (m.find()) {
                    linkImage = m.group(1);
                }
            }

            // Thêm tin tức vào danh sách
            listNews.add(new News(title, link, linkImage, date));
        }

        mainActivity.onRssRead();
    }


}