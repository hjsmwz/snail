package com.acgist.snail.net.application;

import com.acgist.snail.net.TcpServer;
import com.acgist.snail.system.config.SystemConfig;

/**
 * 系统服务端
 * 
 * @author acgist
 * @since 1.0.0
 */
public class ApplicationServer extends TcpServer<ApplicationMessageHandler> {

	private static final ApplicationServer INSTANCE = new ApplicationServer();
	
	private ApplicationServer() {
		super("Application Server", ApplicationMessageHandler.class);
	}
	
	public static final ApplicationServer getInstance() {
		return INSTANCE;
	}
	
	@Override
	public boolean listen() {
		return listen(SystemConfig.getServicePort());
	}
	
}