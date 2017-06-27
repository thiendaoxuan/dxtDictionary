/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.handlers.Thrift;

import com.vng.zing.dictionaryService.thrift.DictionaryService;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.testapp.model.Thift.TAuthenModel;
import com.vng.zing.testapp.model.Thift.TDictionaryModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author cpu10878-local
 */
public class TDictionaryHandler implements DictionaryService.Iface {

    private static final Logger _Logger = ZLogger.getLogger(TDictionaryHandler.class);

    public TDictionaryHandler() {

        // test DB
        String testTranslation = TDictionaryModel.Instance.translate("king");
        if (testTranslation == null || testTranslation.isEmpty()) {
            System.err.println("TEst translation on Thirft fail");
        } else {
            System.out.println("Test translation on Thirft successful");
        }

        ArrayList<String> testAutoComplete = TDictionaryModel.Instance.autoComplete("k");
        if (testAutoComplete == null || testAutoComplete.isEmpty()) {
            System.err.println("TEst autocomplete on Thirft fail");
        } else {
            System.out.println("Test autocomplete on Thirft successful");
        }

    }

    @Override
    public String getTranslation(String word) throws TException {
        ThreadProfiler profiler = Profiler.createThreadProfiler("TDictionaryAPIHandler.translate", false);
        try {
            return TDictionaryModel.Instance.translate(word);
        } finally {
            Profiler.closeThreadProfiler();
        }
    }

    @Override
    public Set<String> getAutoCompletes(String word) throws TException {
        ThreadProfiler profiler = Profiler.createThreadProfiler("TDictionaryAPIHandler.autoComplete", false);
        try {
            ArrayList<String> data = TDictionaryModel.Instance.autoComplete(word);
            return new HashSet<>(data);
        } finally {
            Profiler.closeThreadProfiler();
        }
    }

    @Override
    public Set<String> getImage(String word) throws TException {
        ThreadProfiler profiler = Profiler.createThreadProfiler("TDictionaryAPIHandler.autoComplete", false);

        try {
            ArrayList<String> listImages = TDictionaryModel.Instance.getImages(word);
            return new HashSet<>(listImages);
        } catch (Exception ex) {
            _Logger.error(null, ex);
            return new HashSet<>();
        } finally {
            Profiler.closeThreadProfiler();
        }

    }

    @Override
    public boolean updateTranslation(String word, String translation) throws TException {
        return TDictionaryModel.Instance.updateTranslation(word, translation);
    }

    @Override
    public boolean deleteTranslation(String word) throws TException {
        return TDictionaryModel.Instance.deleteTranslation(word);
    }

    @Override
    public int getAuthenLevel(String username, String password) throws TException {
        return TAuthenModel.Instance.checkAuthen(username, password);
    }

    @Override
    public void addUser(String username, String password, int role) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getReadTimes(String word) throws TException {
        return TDictionaryModel.Instance.getCount(word);
    }

    @Override
    public boolean addReadTimes(String word, int times) throws TException {
        return TDictionaryModel.Instance.addCount(word, times);
    }

    @Override
    public String getTopRead() throws TException {
        return TDictionaryModel.Instance.getTopList();
    }
}
