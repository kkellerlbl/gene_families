package us.kbase.kbasegenefamilies;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppEventListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			KBaseGeneFamiliesServer.getTaskQueue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			KBaseGeneFamiliesServer.getTaskQueue().stopAllThreads();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
