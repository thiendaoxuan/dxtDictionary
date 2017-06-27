/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.Thift.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cpu10878-local
 */

// read sampleData from Dictionary.json

public class DataReader {

    public static JSONObject readSampleData() {
        String fileName = "./SampleData/dictionary.json";
        String data = new String();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileName));
            while ((sCurrentLine = br.readLine()) != null) {
                data += sCurrentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        JSONObject jObj;
        
        try {
            jObj = new JSONObject(data);
        } catch (JSONException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return jObj;
    }
}
