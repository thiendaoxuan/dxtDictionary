/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.handlers.webapp;

import com.vng.zing.testapp.handlers.Thrift.TDictionaryHandler;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.testapp.model.webapp.DictionaryAPIModel;
import com.vng.zing.testapp.model.webapp.StatsModel;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author cpu10878-local
 */
public class DictionaryAPIHandler extends HttpServlet {
    private static final Logger _Logger = ZLogger.getLogger(TDictionaryHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doProcess(req, resp);
	}

	private void doProcess(HttpServletRequest req, HttpServletResponse resp) {
		ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("DictionaryAPIHandler", req);
		try {
			DictionaryAPIModel.Instance.process(req, resp);
		} catch (Exception ex) {
			_Logger.error(null, ex);
		} finally {
			Profiler.closeThreadProfiler();
		}
	}
}
