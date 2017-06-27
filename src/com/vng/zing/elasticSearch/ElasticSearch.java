/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.elasticSearch;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.testapp.model.Thift.Util.DataReader;
import com.vng.zing.testapp.model.webapp.HomePageModel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ElasticSearch {

    private static final org.apache.log4j.Logger _Logger = ZLogger.getLogger(HomePageModel.class);
    
    private TransportClient client;
    //public static final ElasticSearch Instance = new ElasticSearch();
    public static final ElasticSearch Instance = new ElasticSearch("testapp");

    private String _defaultHost = "0.0.0.0";
    private String _host = _defaultHost;

    private int _defaultPort = 9320;
    private int _port = _defaultPort;

    private String _defaultIndice = "thien_dictionary";
    private String _indice = _defaultIndice;

    private String _defaultType = "word";
    private String _type = _defaultType;

    private boolean isReady = true;
    

    public ElasticSearch(String _name) {
        client = TransportClient.builder().build();

        try {
            _host = ZConfig.Instance.getString(this.getClass(), _name, "host", _defaultHost);
            _port = Integer.parseInt(ZConfig.Instance.getString(this.getClass(), _name, "port", Integer.toString(_defaultPort)));
            _indice = ZConfig.Instance.getString(this.getClass(), _name, "index_name", _defaultIndice);
            _type = ZConfig.Instance.getString(this.getClass(), _name, "type", _defaultType);
            Settings settings = Settings.builder().put("cluster.name", "es_zchatsearch").build();
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(_host), _port));

            //initSample();
        } catch (NumberFormatException | UnknownHostException e) {
            isReady = false;
            if (client != null) {
                client.close();
            }
        }

    }

    private ElasticSearch() {
        isReady = false;
    }

    public TransportClient getClient() {
        return client;
    }

    public void close() {
        client.close();
    }

    public void resetConnection() {
        client.close();

    }

    public boolean index(String value) throws IOException {
        if (!isReady) {
            return false;
        }
        IndexResponse ir = client.prepareIndex(_indice, _type)
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("word", value)
                        .endObject())
                .get();
        return (ir.getIndex() != null);
    }

    public boolean removeIndex(String value) {
        if (!isReady) {
            return false;
        }

        String id = getWordId(value);
        if (id == null) {
            return true;
        }
        DeleteResponse dr = client.prepareDelete(_indice, _type, id).get();
        return true;
                
    }

    public boolean isExist(String word) {
        if (!isReady) {
            return false;
        }
        ArrayList<String> listNearWord = getNearWord(word);
        for (int i = 0; i > listNearWord.size(); i++) {
            if (listNearWord.get(i).toUpperCase().equals(word.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private void delete(String key) {
        if (!isReady) {
            return;
        }
        DeleteResponse response = client.prepareDelete(_indice, _type, key)
                .get();
    }

    private boolean initSample() {
        JSONObject sampleData = DataReader.readSampleData();
        for (int i = 0; i < sampleData.length(); i++) {
            try {
                String currentKey = sampleData.names().get(i).toString();
                delete(currentKey);
                boolean success = index(currentKey);
                if (success) {
                    System.out.println("Added to es word : " + currentKey + " at " + i);
                } else {
                    System.err.println("Failed to es word : " + currentKey);
                }
            } catch (IOException | JSONException ex) {
                _Logger.error(null, ex);
            }
        }
        return true;

    }

    public ArrayList<String> getNearWord(String words) {
       

        try {
            SearchResponse response = client.prepareSearch(_indice)
                    .setTypes(_type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.prefixQuery("word", words))
                    //        .setFrom(0).setSize(60).setExplain(true)
                    .get();

            
            SearchHit [] sh = response.getHits().getHits();
            
            

            ArrayList<String> results = new ArrayList<>();
           
            for(SearchHit i : sh){
                results.add(i.getSource().get("word").toString());
            }
            
            
            return results;
        } catch (Exception ex) {
            Logger.getLogger(ElasticSearch.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }

    }

    private String getWordId(String word) {
        try {
            SearchResponse response = client.prepareSearch(_indice)
                    .setTypes(_type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.termQuery("word", word))
                    //        .setFrom(0).setSize(60).setExplain(true)
                    .get();

            String data = response.toString();

            JSONObject jObj = new JSONObject(data);
            JSONObject shards = jObj.getJSONObject("_shards");

            if (shards.get("successful").toString().equals("0")) {
                return null;
            }

            JSONArray hits = jObj.getJSONObject("hits").getJSONArray("hits");

            for (int i = 0; i < hits.length(); i++) {

                JSONObject hit = hits.getJSONObject(i);
                JSONObject source = hit.getJSONObject("_source");

                if (source.get("word").toString().toUpperCase().equals(word)) {
                    return hit.getString("_id");
                }
            }
            return null;

        } catch (JSONException ex) {
            Logger.getLogger(ElasticSearch.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
