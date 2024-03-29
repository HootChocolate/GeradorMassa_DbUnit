package Utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseUtil {

	
	
	public static WebDriver getChromeWebDriver(String site) {
		System.setProperty("webdriver.chrome.driver", "/home/jay/programs/ChromeDriver/chromedriver");
		
		WebDriver navegador = new ChromeDriver();
		
		navegador.get(site);
		navegador.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return navegador;
	}
	
	public String getMensagemFeedback(WebDriver navegador) {
		return navegador.findElement(By.xpath("//div[@role=\"alert\"]")).getText();
	}
	
}
