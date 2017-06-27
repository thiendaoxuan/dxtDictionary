/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.webapp;

import com.vng.zing.common.ZCommonDef;
import com.vng.zing.dictionaryService.thrift.DictionaryService;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.thriftpool.TClientPool;
import com.vng.zing.thriftpool.ZClientPoolUtil;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author cpu10878-local
 */
public class ThriftClient {

    private static final Class _ThisClass = ThriftClient.class;
    private static final Logger _Logger = ZLogger.getLogger(_ThisClass);
    private final String _name;
    private TClientPool.BizzConfig _bizzCfg;

    public static final ThriftClient Instance = new ThriftClient("client");

    private static TClientPool<DictionaryService.Client> pool;

    private ThriftClient(String name) {
        assert (name != null && !name.isEmpty());
        _name = name;
        _initialize();
        pool = getClientPool();

    }

    public String getName() {
        return _name;
    }

    private void _initialize() {
        ZClientPoolUtil.SetDefaultPoolProp(_ThisClass //clazzOfCfg
                ,
                 _name //instName
                ,
                 null //host
                ,
                 null //auth
                ,
                 20000 //timeout
                ,
                 ZCommonDef.TClientNRetriesDefault //nretry
                ,
                 ZCommonDef.TClientMaxRdAtimeDefault //maxRdAtime
                ,
                 ZCommonDef.TClientMaxWrAtimeDefault //maxWrAtime
        );
        ZClientPoolUtil.GetListPools(_ThisClass, _name, new DictionaryService.Client.Factory()); //auto create pools
        _bizzCfg = ZClientPoolUtil.GetBizzCfg(_ThisClass, _name);

        TClientPool.BizzConfig bCfg = getBizzCfg();

    }

    private TClientPool<DictionaryService.Client> getClientPool() {
        return (TClientPool<DictionaryService.Client>) ZClientPoolUtil.GetPool(_ThisClass, _name);
    }

    private TClientPool.BizzConfig getBizzCfg() {
        return _bizzCfg;
    }

    public String translate(String word) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getTranslation(word);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return null;
        } finally {
            pool.returnClient(cli);
        }
    }

    public Set<String> autoComplete(String word) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getAutoCompletes(word);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return new HashSet();
        } finally {
            pool.returnClient(cli);
        }
    }

    public boolean updateWord(String word, String translation) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            cli.updateTranslation(word, translation);
            return true;
        } catch (TException ex) {
            _Logger.error(null, ex);
            return false;
        } finally {
            pool.returnClient(cli);
        }

    }

    public Set<String> getImages(String word) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getImage(word);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return new HashSet();
        } finally {
            pool.returnClient(cli);
        }
    }

    public int getAuthen(String username, String password) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getAuthenLevel(username, password);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return 2;
        } finally {
            pool.returnClient(cli);
        }
    }

    public boolean deleteTranslation(String word) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.deleteTranslation(word);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return false;
        } finally {
            pool.returnClient(cli);
        }
    }

    public int getCount(String word) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getReadTimes(word);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return 0;
        } finally {
            pool.returnClient(cli);
        }
    }

    public boolean addCount(String word, int times) {
        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.addReadTimes(word, times);
        } catch (TException ex) {
            _Logger.error(null, ex);
            return false;
        } finally {
            pool.returnClient(cli);
        }
    }

    public String getTopList() {

        DictionaryService.Client cli = pool.borrowClient();
        try {
            return cli.getTopRead();
        } catch (TException ex) {
            _Logger.error(null, ex);
            return "{}";
        } finally {
            pool.returnClient(cli);
        }
    }

}
