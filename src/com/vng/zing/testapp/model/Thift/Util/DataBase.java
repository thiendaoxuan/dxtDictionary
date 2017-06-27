/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.Thift.Util;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.dictionaryService.thrift.ROLE;

import com.vng.zing.zidb.thrift.wrapper.ZiDBClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.vng.zing.common.ByteBufferUtil;
import com.vng.zing.common.TDeserializerFactory;
import com.vng.zing.common.TSerializerFactory;
import com.vng.zing.common.ZErrorHelper;
import com.vng.zing.dictionaryService.thrift.User;
import com.vng.zing.zcommon.thrift.PutPolicy;
import com.vng.zing.zidb.thrift.TValue;
import com.vng.zing.zidb.thrift.TValueResult;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

/**
 *
 * @author cpu10878-local
 */

// Contain all connection to Kyoto Cabinet activity

public class DataBase {

    public static final DataBase Instance = new DataBase();

    private static final Logger _Logger = ZLogger.getLogger(DataBase.class);
    private static ZiDBClient ZIDB_CLIENT;

    private static TSerializer serializer;
    TDeserializer deserializer;

    private static final String WORD_PREFIX = "WORD";
    private static final String IMAGE_PREFIX = "IMAGE";
    private static final String USER_PREFIX = "USER";
    private static final String COUNT_PREFIX = "COUNT";

    private static final String TOP_COUNT_LIST = "TOP_COUNT_LIST";
    private static final int TOP_LIST_MAX_LENGTH = 20;

    private DataBase() {
        try {
            serializer = TSerializerFactory.Borrow();
            deserializer = TDeserializerFactory.Borrow();
            ZIDB_CLIENT = new ZiDBClient("fresher");

            //updateTranslation("queen", "A powerful man");
            //System.out.println(getTranslation("king"));
            //System.out.println(getTranslation("queen"));
            // System.out.println(getTranslation("dog"));
            if (ZIDB_CLIENT != null) {
                //initSampleDataBase();
                //putUser("thiendaoxuan", "1", ROLE.ROLE_ADMIN.getValue());
                //deleteImage("dog");
                //deleteTopList();

            }
        } catch (Exception ex) {
            _Logger.error(null, ex);
            System.err.println("Cannot init SampleDataBase : " + ex.toString());
        }
    }

    private boolean initSampleDataBase() throws JSONException {
        JSONObject sampleData = DataReader.readSampleData();
        for (int i = 0; i < sampleData.length(); i++) {
            String currentKey = sampleData.names().get(i).toString();
            boolean success = updateTranslation(currentKey, sampleData.getString(currentKey));

            if (success) {
                System.out.println("Added to db word : " + currentKey + " at " + i + "with val : " + sampleData.getString(currentKey));
            } else {
                System.err.println("Failed to add word : " + currentKey);
            }
        }
        return true;
    }

    public boolean updateTranslation(String word, String translation) {
        word = WORD_PREFIX + word.toUpperCase();
        ByteBuffer key = ByteBufferUtil.fromString(word);
        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(translation));
        long sucess = ZIDB_CLIENT.put(key, value, PutPolicy.ADD_OR_UDP);

        return (ZErrorHelper.isSuccess(sucess));
    }

    public boolean deleteTranslation(String word) {
        word = WORD_PREFIX + word.toUpperCase();
        ByteBuffer key = ByteBufferUtil.fromString(word);
        long sucess = ZIDB_CLIENT.remove(key);
        
        return (ZErrorHelper.isSuccess(sucess));
    }

    public String getTranslation(String word) {
        word = WORD_PREFIX + word.toUpperCase();
        TValueResult getValue = ZIDB_CLIENT.get(ByteBufferUtil.fromString(word));
        if (ZErrorHelper.isSuccess(getValue.error)) {
            return ByteBufferUtil.toString(getValue.value.data);
        } else {
            return null;
        }
    }

    public boolean putImages(String word, String imageLinkArray) {
        word = IMAGE_PREFIX + word.toUpperCase();
        ByteBuffer key = ByteBufferUtil.fromString(word);
        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(imageLinkArray));
        ZIDB_CLIENT.put(key, value, PutPolicy.ADD_OR_UDP);

        String checkImage = getImages(word);
        if (checkImage == null) {
            return false;
        }
        return true;
    }

    public String getImages(String word) {
        try {
            word = IMAGE_PREFIX + word.toUpperCase();
            TValueResult getValue = ZIDB_CLIENT.get(ByteBufferUtil.fromString(word));
            return ByteBufferUtil.toString(getValue.value.data);
        } catch (Exception ex) {
            _Logger.error(null, ex);
            return null;
        }
    }

    private boolean deleteImage(String word) {
        word = IMAGE_PREFIX + word.toUpperCase();
        ByteBuffer key = ByteBufferUtil.fromString(word);
        long sucess = ZIDB_CLIENT.remove(key);
        return (ZErrorHelper.isSuccess(sucess));
    }

    public User getUser(String userName) {
        userName = USER_PREFIX + userName;
        TValueResult getValueResult = ZIDB_CLIENT.get(ByteBufferUtil.fromString(userName));
        if (ZErrorHelper.isSuccess(getValueResult.error)) {
            try {
                TValue getValue = getValueResult.value;
                User selectedUser = new User();
                deserializer.deserialize(selectedUser, getValue.getData());
                return selectedUser;
            } catch (TException ex) {
                _Logger.error(null, ex);
                return null;
            }
        }
        return null;
    }

    public boolean putUser(String userName, String passwords, int role) {
        try {
            // check if account alrady exist
            if (userName.isEmpty() || passwords.isEmpty()) {
                return false;
            }

            // allow admin account to be put mutiple time for testing
            if (getUser(userName) != null && !userName.equals("thiendaoxuan")) {
                return false;
            }

            User newUser = new User(userName, AuthenticationUtil.create_hash(passwords));
            newUser.role = role;

            try {
                ByteBuffer key = ByteBufferUtil.fromString(USER_PREFIX + userName);
                byte[] userData = serializer.serialize(newUser);
                TValue value = new TValue();
                value.setData(userData);
                ZIDB_CLIENT.put(key, value, PutPolicy.ADD);
            } catch (TException ex) {
                _Logger.error(null, ex);
                return false;
            }
            return true;
        } catch (UnsupportedEncodingException ex) {
            _Logger.error(null, ex);
            return false;
        }
    }

    public int getCount(String word) {
        if (getTranslation(word) == null) {
            return 0;
        }
        word = COUNT_PREFIX + word.toUpperCase();

        TValueResult getValue = ZIDB_CLIENT.get(ByteBufferUtil.fromString(word));
        if (ZErrorHelper.isSuccess(getValue.error)) {
            try {
                ByteBuffer data = getValue.value.data;
                return Integer.parseInt(ByteBufferUtil.toString(data));
            } catch (NumberFormatException ex) {
                _Logger.error(null, ex);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean addCount(String word, int times) {
        int newCount = getCount(word) + times;
        String tmpWord = COUNT_PREFIX + word.toUpperCase();
        ByteBuffer key = ByteBufferUtil.fromString(tmpWord);
        TValue value = new TValue();
        value.setData(ByteBufferUtil.fromString(Integer.toString(newCount)));
        long sucess = ZIDB_CLIENT.put(key, value, PutPolicy.ADD_OR_UDP);

        maintainTopCountWordsMap(word, newCount);

        return (ZErrorHelper.isSuccess(sucess));

    }

    private void maintainTopCountWordsMap(String word, int times) {

        try {
            JSONObject topList = new JSONObject();
            ByteBuffer ListKeyBuffer = ByteBufferUtil.fromString(TOP_COUNT_LIST);

            TValueResult getValueResult = ZIDB_CLIENT.get(ListKeyBuffer);

            if (ZErrorHelper.isSuccess(getValueResult.error)) {
                try {
                    TValue getValue = getValueResult.value;
                    String JSONString = ByteBufferUtil.toString(getValue.data);
                    topList = new JSONObject(JSONString);
                } catch (Exception ex) {
                    _Logger.error(null, ex);
                    topList = new JSONObject();
                }
            }

            if (topList.has(word) || topList.length() < TOP_LIST_MAX_LENGTH) {
                topList.put(word, times);
            } else {
                
                Iterator<String> keys = topList.keys();

                while (keys.hasNext()) {
                    try {
                        String key = (String) keys.next();
                        if (Integer.parseInt(topList.getString(key)) < times) {
                            topList.remove(key);
                            topList.append(word, times);
                            break;
                        }
                    } catch (JSONException ex) {
                        _Logger.error(null, ex);
                        return;

                    }
                }
            }

            String finalJSONString = topList.toString();

            TValue value = new TValue();
            value.setData(ByteBufferUtil.fromString(finalJSONString));
            ZIDB_CLIENT.put(ListKeyBuffer, value, PutPolicy.ADD_OR_UDP);
        } catch (JSONException ex) {
            _Logger.error(null, ex);
        }
    }

    public String getTopList() {
        try {
            ByteBuffer ListKeyBuffer = ByteBufferUtil.fromString(TOP_COUNT_LIST);

            TValueResult getValueResult = ZIDB_CLIENT.get(ListKeyBuffer);
            TValue getValue = getValueResult.value;
            String JSONString = ByteBufferUtil.toString(getValue.data);
            return JSONString;
        } catch (Exception ex) {
            _Logger.error(null, ex);
            return "{}";
        }

    }

    
    // For testing purpourse
    private void deleteTopList() {
        ByteBuffer key = ByteBufferUtil.fromString(TOP_COUNT_LIST);
        ZIDB_CLIENT.remove(key);
    }

}
