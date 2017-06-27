/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.webapp;

/**
 *
 * @author cpu10878-local
 */
/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.common.TemplateManager;
import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Note: Class model xử lý business chính cho mỗi loại handler/controller ,
 * được thiết kế theo pattern Singleton Object, thiết kế kiểu này cho phép các
 * Model object truy xuất được lẫn nhau (cùng package nên truy xuất được các
 * thuộc tính protected của nhau), có thể bổ sung thiết kế bằng cách tạo ra 1
 * BaseModel xử lý các hàm tiện ích chung, các Biz Model khác thừa kế từ đó
 *
 * @author namnq
 */
public class HomePageModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(HomePageModel.class);
    public static final HomePageModel Instance = new HomePageModel();
    private Template template;

    private static final int MAX_TOP_WORDS = 5;

    private HomePageModel() {
        try {
            template = TemplateManager.getTemplate("index.xtm");
        } catch (TemplateException ex) {
            _Logger.error(null, ex);
        }

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        prepareHeaderHtml(response);
        PrintWriter out = null;
        profiler.push(this.getClass(), "output");

        try {
            out = response.getWriter();
            boolean isValid = true;

            String word = request.getParameter("word");

            if (word == null || word.isEmpty()) {
                isValid = false;
            }

            TemplateDictionary homePageDictionary;
            homePageDictionary = new TemplateDictionary();

            int authenLevel = 2;
            try {
                authenLevel = (int) request.getSession().getAttribute("authenLevel");
            } catch (Exception e) {

            }

            String authenTodo = "LOG OUT";

            if (authenLevel >= 2 || authenLevel <= -1) {
                homePageDictionary.addSection("loginSection");
                authenTodo = "LOG IN";
            }

            // admin Section
            if (authenLevel == 0) {
                homePageDictionary.addSection("adminSection");                
                homePageDictionary.addSection("newWordSection");
                
                TemplateDataDictionary topListDictionary = homePageDictionary.addSection("topListSection");
                parseTopList(topListDictionary, DictionaryAPIModel.Instance.getTopList());
            }
            
            // translate Section
            if (isValid) {
                homePageDictionary.addSection("responseSection");
                
                homePageDictionary.setVariable("translatedWord", word);
                homePageDictionary.setVariable("translation",  DictionaryAPIModel.Instance.tranlate(word));
                if (authenLevel == 0) {
                    homePageDictionary.addSection("editSection");
                }
            }

            // LOGIN button
            homePageDictionary.setVariable("AuthenTODO", authenTodo);

            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(template.renderToString(homePageDictionary));

        } catch (Exception ex) {
            _Logger.error(null, ex);
        } finally {

            if (out != null) {
                out.close();
            }

            profiler.pop(this.getClass(), "output");

        }
    }

    
    // Loop through topList object and use Selection sort to add data to template
    private boolean parseTopList(TemplateDataDictionary topListDictionary, String topList) {
        try {          

            JSONObject topListJSON = new JSONObject(topList);

            int i = 0;
            while (topListJSON.length() > 0) { // Selection Sort
                if (i == MAX_TOP_WORDS) {
                    break;
                }
                Iterator<String> keys = topListJSON.keys();
                String highestWord = keys.next();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    int value = topListJSON.getInt(key);

                    if (topListJSON.getInt(highestWord) < value) {
                        highestWord = key;
                    }
                }                
                TemplateDataDictionary topWordDictionary = topListDictionary.addSection("topWord");
                topWordDictionary.setVariable("word", highestWord.toLowerCase() );
                topWordDictionary.setVariable("time", topListJSON.getString(highestWord));                
                
                topListJSON.remove(highestWord);
                i++;
            }
            return true;
        } catch (JSONException ex) {
            _Logger.error(null, ex);
            return false;
        }

    }
}
