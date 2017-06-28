/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.webapp;

import com.vng.zing.ZiCache.DictionaryCache;
import com.vng.zing.dictionaryService.thrift.ROLE;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.testapp.handlers.Thrift.TDictionaryHandler;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author cpu10878-local
 */
public class DictionaryAPIModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(StatsModel.class);

    public static final DictionaryAPIModel Instance = new DictionaryAPIModel();

    // tam thoi lam vay
    private static final TDictionaryHandler tmp = new TDictionaryHandler();

    private final DictionaryCache CACHE = DictionaryCache.Instance;
    
    ThriftClient CLIENT = ThriftClient.Instance;


    private final int countThreshold = 5;

    private final String WORD_NOT_FOUND = "word not found";
    private final String IMAGE_NOT_FOUND = "[http://www.homefinder.com/image/noPhotoYet.gif]";

    private boolean isTesting = false;

    private DictionaryAPIModel() {

    }

    
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) {

        ThreadProfiler profiler = Profiler.getThreadProfiler();
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = null;

        boolean isValid = true;

        if (CLIENT == null) {
            System.err.println("Thirft client fail");
            return;
        }

        try {
            profiler.push(this.getClass(), "output");
            out = resp.getWriter();
            String API_request = req.getParameter("requestType");
            if (API_request == null) {
                isValid = false;
            }

            int authenLevel = 2;

            // check authentication
            Object SessionAuthenLevel = req.getSession().getAttribute("authenLevel");
            if (SessionAuthenLevel != null) {
                authenLevel = (int) SessionAuthenLevel;
            }

            String word = req.getParameter("word");
            if (word == null || word.isEmpty()) {
                isValid = false;
            }

            //StressTest - mod for stressing HTTP request
            if (!isValid) {
                try {
                    String testParam = req.getParameter("stressTest");
                    word = testParam.split("00")[0];
                    API_request = testParam.split("00")[1];
                    
                    isValid = true;
                    isTesting = true;
                    
                } catch (Exception e) {                    
                    isValid = false;
                }
            }
            
            
            System.out.println(API_request);
            if (!isValid) {
                return;
            }

            word = word.toLowerCase();

            switch (API_request) {
                case "translate":
                    String translation = tranlate(word);
                    out.println(translation);
                    break;
                case "autocomplete":
                    Set data = CLIENT.autoComplete(word);
                    if (data == null || data.isEmpty()) {
                        out.println("[]");
                    } else {
                        out.println(new ArrayList(data));
                    }
                    break;
                case "image":
                    String imagesData = getImagesList(word);
                    out.println(imagesData);
                    break;
                case "update":
                    String updatedTranlation = req.getParameter("translation");
                    if (updatedTranlation == null || updatedTranlation.isEmpty() || authenLevel != ROLE.ROLE_ADMIN.getValue()) {
                        break;
                    }
                    if (CLIENT.updateWord(word, updatedTranlation)) {
                        CACHE.updateTranslation(word, updatedTranlation);
                    }
                    break;
                case "delete":
                    if (authenLevel != (ROLE.ROLE_ADMIN.getValue())) {
                        break;
                    }
                    CLIENT.deleteTranslation(word);
                    CACHE.removeTranslation(word);
                    CACHE.removeImagesList(word);

                case "topList":
                    if (authenLevel != (ROLE.ROLE_ADMIN.getValue())) {
                        out.println("{}");
                        break;
                    }
                    out.println(getTopList());

            }

        } catch (IOException ex) {
            _Logger.error(null, ex);
        } finally {
            profiler.pop(this.getClass(), "output");
            if (out != null) {
                out.close();
            }

        }
    }

    public String tranlate(String word) {

        String translation = CACHE.getTranslation(word);

        //StressTest = no cache
        if (isTesting) {
            translation = null;
        }

        if (translation != null) {

            updateCount(word);
            return translation;
        }

        translation = CLIENT.translate(word);
        if (translation != null && !translation.isEmpty()) {
            updateCount(word);
            CACHE.updateTranslation(word, translation);
            return translation;
        }

        return WORD_NOT_FOUND;
    }

    public boolean updateCacheTranslation(String word, String translation) {
        return CACHE.updateTranslation(word, translation);
    }

    public String getImagesList(String word) {
        String imagesData = CACHE.getImaghesList(word);
        if (imagesData != null && imagesData.length() > 20) {
            return imagesData;
        }
        Set imagesDataSet = CLIENT.getImages(word);

        if (imagesDataSet != null && imagesDataSet.size() > 0) {
            ArrayList imagesDataArr = new ArrayList(imagesDataSet);
            CACHE.updateImagesList(word, imagesDataArr.toString());
            return imagesDataArr.toString();
        }

        return IMAGE_NOT_FOUND;

    }

    private void updateCount(String word) {
        
        // synchronized to prevent many concurent request
        
        synchronized (this) {
            int currentCount = CACHE.getCount(word);

            //if count not found on Cache, check and sync with db, only happen when reset cache
            if (currentCount == -1) {
                currentCount = CLIENT.getCount(word);
            }
            
            // store count on cache, write to db when reach a threshold
            currentCount++;
            if (currentCount % countThreshold == 0) {
                CLIENT.addCount(word, countThreshold);
            }
            CACHE.setCount(word, currentCount);
        }
    }

    public String getTopList() {
        return CLIENT.getTopList();
    }

}
