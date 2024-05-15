package ApplicationPages;

import WebConnector.webconnector;
import org.testng.Assert;

public class Blogpage {
	webconnector wc=new webconnector();

	public void verifyPageTitle() {
    	try {
    		wc.PerformActionOnElement("RecentPosts_BlogPage","WaitForElementDisplay","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		public void verifyPageTitles(String exptitle) {
			try {
				String actualtitle=wc.getPageTitle();
				Assert.assertEquals(exptitle,actualtitle);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}