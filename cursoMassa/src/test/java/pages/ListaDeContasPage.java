package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Utils.BaseUtil;

public class ListaDeContasPage extends BaseUtil{

	WebDriver navegador = null;
	
	/**
	 * Está classe diz respeito ao operações possíveis de serem realizadas na página de lista de contas
	 * 
	 * @param navegador
	 */
	public ListaDeContasPage(WebDriver navegador) {
		this.navegador = navegador;
	}
	
	public EditarContaPage clicarNoBotaoEditarConta(WebDriver navegador, String nomeConta) {
	
		navegador.findElement(By.xpath("//td[contains(text(), \"" + nomeConta + "\")]/..//a[contains(@href, \"editarConta\")]")).click();
		
		return new EditarContaPage(navegador);
		
	}
	
	public ListaDeContasPage clicarNoBotaoRemoverConta(WebDriver navegador, String nomeConta) {
		
		WebElement remover = navegador.findElement(By.xpath("//td[contains(text(), \"" + nomeConta + "\")]/..//a[contains(@href, \"removerConta\")]"));
		
		remover.click();
		
		return this;
	}
	
	public String consultaUmaContaNaListaDeContas(WebDriver navegador, String conta) {
		
		return navegador.findElement(By.xpath("//table[@id=\"tabelaContas\"]//td[text()= \"" + conta + "\"]")).getText();
	}
}
