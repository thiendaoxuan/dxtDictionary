/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.Thift;

import com.vng.zing.elasticSearch.ElasticSearch;
import com.vng.zing.dictionaryService.thrift.User;
import com.vng.zing.testapp.model.Thift.Util.DataBase;
import com.vng.zing.testapp.model.Thift.Util.ImageCrawler;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.testapp.model.Thift.Util.AuthenticationUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import static org.apache.commons.lang.StringUtils.split;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author cpu10878-local
 */
public class TAuthenModel {

    private static final Logger _Logger = ZLogger.getLogger(TAuthenModel.class);
    public static final TAuthenModel Instance = new TAuthenModel();

    private static final DataBase DATA_BASE_ACCESS = DataBase.Instance;

    private static final int NOT_USER = 2;
    private static final int USER = 1;
    private static final int ADMIN = 0;

    public int checkAuthen(String username, String passwords) {
        try {
            User tryGetUser = DATA_BASE_ACCESS.getUser(username);
            if (tryGetUser == null) {
                return NOT_USER;
            }
            if(!AuthenticationUtil.create_hash(passwords).equals(tryGetUser.passwords)){
                return NOT_USER;
            }
            return tryGetUser.role;
            
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(TAuthenModel.class.getName()).log(Level.SEVERE, null, ex);
            return NOT_USER;
        }
    }

}
