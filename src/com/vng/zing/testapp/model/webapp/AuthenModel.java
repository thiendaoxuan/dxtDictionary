/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.webapp;

import com.vng.zing.dictionaryService.thrift.ROLE;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.testapp.handlers.Thrift.TDictionaryHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author cpu10878-local
 */
public class AuthenModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(StatsModel.class);
    public static final AuthenModel Instance = new AuthenModel();

    // tam thoi lam vay
    private static final TDictionaryHandler tmp = new TDictionaryHandler();

    private AuthenModel() {

        /*ZClientPoolUtil.SetDefaultPoolProp(ThriftServers.class //clazzOfCfg
                ,
                 _Tname //instName
                ,
                 null//host
                ,
                 null //auth
                ,
                 86400 //timeout
                ,
                 ZCommonDef.TClientNRetriesDefault //nretry
                ,
                 ZCommonDef.TClientMaxRdAtimeDefault //maxRdAtime
                ,
                 ZCommonDef.TClientMaxWrAtimeDefault //maxWrAtime
        );
        ZClientPoolUtil.GetListPools(ThriftServers.class, _Tname, new DictionaryService.Client.Factory()); //auto create pools
        _bizzCfg = ZClientPoolUtil.GetBizzCfg(ThriftServers.class, _Tname);
        
        System.out.println(_bizzCfg);*/
        // dictionaryClientPool = TClientFactory<DictionaryService.Client>.Borrow();
        // dictionaryClientPool = new TClientPool<DictionaryService.Client>();
    }

    /* private TClientPool<DictionaryService.Client> getClientPool() {
        return (TClientPool<DictionaryService.Client>) ZClientPoolUtil.GetPool(ThriftServers.class, _Tname);
    }*/
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) {

        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "output");
        resp.setContentType("text/html; charset=UTF-8");

        try {
            ThriftClient client = ThriftClient.Instance;
            //DictionaryService.Client cli = pool.borrowClient();

            if (client == null) {
                System.err.println("Thirft client fail");
                return;
            }

            String username = "";
            String passwords = "";

            if (req.getParameter("logout") == null) {

                username = req.getParameter("username");
                passwords = req.getParameter("passwords");

                int authenLevel = client.getAuthen(username, passwords);
                req.getSession().setAttribute("authenLevel", authenLevel);

            } else {
                req.getSession().setAttribute("authenLevel", ROLE.NO_ROLE.getValue());

            }
        } catch (Exception ex) {
            _Logger.error(null, ex);
        } finally {

            profiler.pop(this.getClass(), "output");
        }
    }
}
