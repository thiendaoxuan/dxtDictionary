/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.testapp.servers;

import com.vng.zing.dictionaryService.thrift.DictionaryService;
import com.vng.zing.thriftserver.ThriftServers;


import com.vng.zing.testapp.handlers.Thrift.TDictionaryHandler;

/**
 *
 * @author namnq
 */
public class TServers {

    public boolean setupAndStart() {
        ThriftServers servers = new ThriftServers("testapp");
        DictionaryService.Processor processor = new DictionaryService.Processor(new TDictionaryHandler());
        servers.setup(processor);
        return servers.start();
    }
}
