/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.ZiCache;

import com.vng.zing.common.ByteBufferUtil;
import com.vng.zing.common.IZStructor;
import com.vng.zing.common.PutPolicy;
import com.vng.zing.jni.zicache.ZiCache;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.testapp.model.webapp.DictionaryAPIModel;
import com.vng.zing.testapp.model.webapp.ThriftClient;
import com.vng.zing.zidb.thrift.TValue;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class DictionaryCache {

    private static final Logger _Logger = ZLogger.getLogger(DictionaryCache.class);
    ZiCache<String, TValue> _cache;
    String WORD_PREFIX = "WORD";
    String IMAGE_PREFIX = "IMAGE";
    String COUNT_PREFIX = "COUNT";

    public static final DictionaryCache Instance = new DictionaryCache("zicacheThien");

    private DictionaryCache(String name) {
        _cache = new ZiCache<>(name,
                new IZStructor.StringStructor(), new TValueStructor());

        _cache.clear();

        // warm up by topList
        warmUpCache();

    }

    public final boolean warmUpCache (){
        
        ThriftClient CLIENT = ThriftClient.Instance;
        
        try {
            String topList = CLIENT.getTopList();

            JSONObject topListJSON = new JSONObject(topList);

            Iterator<String> keys = topListJSON.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                
                if(getTranslation(key) == null ){
                    updateTranslation(key, CLIENT.translate(key));
                }
            }
            return true;
        }
        catch (Exception ex){
            _Logger.error(null,ex);
            System.out.println("Warm up fail");
            return false;
        }
    }
    
    
    public final boolean updateTranslation(String word, String translation) {
        if (word == null || word.isEmpty() || translation == null || translation.isEmpty()) {
            return false;
        }

        word = WORD_PREFIX + word.toUpperCase();
        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(translation));
        return _cache.put(word, value, PutPolicy.ADD_OR_UDP);
    }

    public final String getTranslation(String word) {
        word = WORD_PREFIX + word.toUpperCase();
        TValue result = _cache.get(word);
        if (result == null) {
            return null;
        }
        return ByteBufferUtil.toString(result.data) + "(cache)";
    }

    public boolean updateImagesList(String word, String imagesList) {
        if (word == null || word.isEmpty() || imagesList == null || imagesList.isEmpty()) {
            return false;
        }
        word = IMAGE_PREFIX + word.toUpperCase();
        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(imagesList));
        return _cache.put(word, value, PutPolicy.ADD_OR_UDP);
    }

    public boolean removeTranslation(String word) {
        word = WORD_PREFIX + word.toUpperCase();
        return _cache.remove(word);
    }

    public boolean removeImagesList(String word) {
        word = IMAGE_PREFIX + word.toUpperCase();
        return _cache.remove(word);
    }

    public String getImaghesList(String word) {
        word = IMAGE_PREFIX + word.toUpperCase();
        TValue result = _cache.get(word);
        if (result == null) {
            return null;
        }
        return ByteBufferUtil.toString(result.data);
    }

    public int getCount(String word) {
        word = COUNT_PREFIX + word.toUpperCase();
        TValue result = _cache.get(word);
        if (result == null) {
            return -1;
        }
        try {
            return Integer.parseInt(ByteBufferUtil.toString(result.data));
        } catch (NumberFormatException ex) {
            _Logger.error(null, ex);
            return -1;
        }
    }

    public boolean setCount(String word, int times) {
        word = COUNT_PREFIX + word.toUpperCase();

        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(Integer.toString(times)));
        return _cache.put(word, value, PutPolicy.ADD_OR_UDP);

    }

    public class TValueStructor implements IZStructor<TValue> {

        @Override
        public TValue ctor() {
            return new TValue();
        }
    }
    /*
    private static class TValueLoadCb implements ILoadCallback<String, String> {

        @Override
        public TValue perform(LoadWorker<String, String> sender, String key) {
            TValue val = new TValue();
            val.value = key;
            return val;
        }
    }*/
}
