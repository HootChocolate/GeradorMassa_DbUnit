package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Utils.BaseUtil;

public class LoginPage extends BaseUtil{

	WebDriver navegador;
	
	public LoginPage(WebDriver navegador) {
		this.navegador = navegador;
	}
	
	public LoginPage informarEmail(String email) {
		
		navegador.findElement(By.id("email")).sendKeys(email);
		
		return this;
	}
	
	public LoginPage informarSenha(String senha) {
		
		navegador.findElement(By.id("senha")).sendKeys(senha);
		
		return this;
	}
	
	public LoginPage informarEmailESenha(String email, String senha) {
		
		informarEmail(email);
		informarSenha(senha);
		
		return this;
	}
	
	public HomePage clicaNoBotaoEntrar() {
		
		navegador.findElement(By.tagName("button")).click();
		
		return new HomePage(navegador);
	}
	
}
