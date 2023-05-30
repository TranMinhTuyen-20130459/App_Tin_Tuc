package com.example.newsapp.utils;


import android.app.ProgressDialog;

import android.os.AsyncTask;

import com.example.newsapp.MainActivity;
import com.example.newsapp.models.News;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadRSS extends AsyncTask<String, Void, String> {
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
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println(content.toString());
        return content.toString();
    }

    // thực thi trên luồng UI sau khi doinback hoàn thành
    // nhận input từ kết quả của doinback
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        XMLDOMParser xmldomParser = new XMLDOMParser();
        Document document = xmldomParser.getDocument(s);
        // Lấy danh sách các phần tử "item"
        NodeList nodeList = document.getElementsByTagName("item");
        // Lấy danh sách các phần tử "description"
        NodeList nodeListDescription = document.getElementsByTagName("description");

        String title = "";
        String link = "";
        String linkImage = "";
        String date = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
            String cdata = nodeListDescription.item(i + 1).getTextContent();
            Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
            Matcher m = p.matcher(cdata);
            if (m.find()) {
                linkImage = m.group(1);
            }
            // Lấy các giá trị từ các phần tử "item"
            Element element = (Element) nodeList.item(i);
            title = xmldomParser.getValue(element, "title");
            link = xmldomParser.getValue(element, "guid");
            date = xmldomParser.getValue(element, "pubDate");

            // Tạo đối tượng News và thêm vào danh sách
            listNews.add(new News(title, link, linkImage, date));
        }
        // Gọi phương thức callback trong MainActivity
        mainActivity.onRssRead();
    }


}