/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.Thift;

import com.vng.zing.eventBus.EventBus;
import com.vng.zing.elasticSearch.ElasticSearch;
import com.vng.zing.testapp.model.Thift.Util.DataBase;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author cpu10878-local
 */
public class TDictionaryModel {

    private static final Logger _Logger = ZLogger.getLogger(TDictionaryModel.class);
    public static final TDictionaryModel Instance = new TDictionaryModel();

    private static final DataBase DATA_BASE_ACCESS = DataBase.Instance;
    private static final ElasticSearch ELASTIC_SEARCH = ElasticSearch.Instance;

    private final Class clazz = this.getClass();
    //private String workName = "TModel";

    public String translate(String word) {

        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(clazz, "translate");
        String translation = DATA_BASE_ACCESS.getTranslation(word);

        profiler.pop(clazz, "translate");
        return translation;
    }

    public ArrayList<String> autoComplete(String word) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "autoComplete");
        try {
            ArrayList<String> result = ElasticSearch.Instance.getNearWord(word);
            return result;
        } 
        finally {
            profiler.pop(clazz, "autoComplete");
        }

    }

    public ArrayList<String> getImages(String word) throws Exception {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "getImages");
        try {
            String getFromDb = DATA_BASE_ACCESS.getImages(word);

            ArrayList result = new ArrayList<>();

            if (getFromDb == null || getFromDb.length() < 20) {
                EventBus.Instance.addImageCrawlerJob(word);
            } else {
                result = new ArrayList<String>(Arrays.asList(getFromDb.replace("[", "").replace("]", "").split(",")));
            }
            return result;
        } finally {
            profiler.pop(clazz, "getImages");

        }
    }

    public boolean updateTranslation(String word, String translation) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "update");

        try {
            if (ELASTIC_SEARCH.index(word)) {
                return DATA_BASE_ACCESS.updateTranslation(word, translation);
            }
            return false;
        } catch (Exception e) {
            _Logger.error(null, e);
            return false;
        } finally {
            profiler.pop(this.getClass(), "update");
        }

    }

    public boolean deleteTranslation(String word) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "delete");
        try {
            ELASTIC_SEARCH.removeIndex(word);
            return DATA_BASE_ACCESS.deleteTranslation(word);
        } catch (Exception e) {
            _Logger.error(null, e);
            return false;
        } finally {
            profiler.pop(this.getClass(), "delete");
        }

    }

    

    public int getCount(String word) {
        return DATA_BASE_ACCESS.getCount(word);
    }

    public boolean addCount(String word, int times) {
        return DATA_BASE_ACCESS.addCount(word, times);
    }

    public String getTopList() {
        return DATA_BASE_ACCESS.getTopList();
    }

}
