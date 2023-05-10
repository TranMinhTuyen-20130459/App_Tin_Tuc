package com.example.tablayoutfragviewpager.utils;


import android.app.ProgressDialog;

import android.os.AsyncTask;

import com.example.tablayoutfragviewpager.MainActivity;
import com.example.tablayoutfragviewpager.models.News;
import com.example.tablayoutfragviewpager.utils.XMLDOMParser;

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
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        XMLDOMParser xmldomParser = new XMLDOMParser();
        Document document = xmldomParser.getDocument(s);

        NodeList nodeList = document.getElementsByTagName("item");
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
            Element element = (Element) nodeList.item(i);
            title = xmldomParser.getValue(element, "title");
            link = xmldomParser.getValue(element, "guid");
            date = xmldomParser.getValue(element, "pubDate");
            // số bài trong 1 danh mục
//            if (listNews.size() < 20) {
            listNews.add(new News(title, link, linkImage, date));
//            }
        }

        mainActivity.onRssRead();
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mainActivity);
        dialog.setMessage("Đang tải...");
        dialog.setCancelable(false);
        dialog.show();
    }

}