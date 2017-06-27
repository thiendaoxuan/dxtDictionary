/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.eventBus;

import com.vng.zing.zeventbus.Watcher;
import com.vng.zing.zeventbus.ZEvbusSubsys;


import com.vng.zing.testapp.model.Thift.Util.DataBase;
import com.vng.zing.testapp.model.Thift.Util.ImageCrawler;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpu10878-local
 */
public class EventBus {

    private static final ZEvbusSubsys bus = new ZEvbusSubsys("main");
    private static final String CRAWL_EVENT = "zte.fresher";
    
    public static final EventBus Instance = new EventBus();

    private EventBus() {
        addImageCrawlerHandler();
        bus.start();
    }

    public void addImageCrawlerJob(String word) {
        bus.notifier.notify(CRAWL_EVENT, word);
    }

    private void addImageCrawlerHandler() {
        Watcher.GenericHandler imageCrawlerHandler = new Watcher.GenericHandler() {
            @Override
            public void onMessageWatResDatasCome(long eventId, String eventName, int typeId, Object data) {
                try {
                    String word = data.toString();
                    ArrayList<String> imagesList = ImageCrawler.getImageUrls(word);
                    DataBase.Instance.putImages(word, imagesList.toString());
                    System.out.println("Crawl for word : " + word);
                } catch (Exception ex) {
                    Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        bus.setHandler(CRAWL_EVENT, imageCrawlerHandler);
    }

}
