package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdicionarPage {

	WebDriver navegador = null;
	
	public AdicionarPage(WebDriver navegador) {
		this.navegador = navegador;
	}
	
	/**
	 * Insere o valor informado no input de nome
	 * 
	 * @param nome
	 * @return AdicionarPage
	 */
	public AdicionarPage inserirNome(String nome) {
		
		navegador.findElement(By.id("nome")).sendKeys(nome);
		
		return this;
	}
	
	/**
	 * Clica no botão "Salvar" para salvar um contato
	 * 
	 * @param navegador
	 * @return HomePage
	 */
	public ListaDeContasPage clicarNoBotaoSalvar(WebDriver navegador) {
		
		navegador.findElement(By.tagName("button")).click();
		
		return new ListaDeContasPage(navegador);
	}
	
	/**Insere o valor informado no input de nome, e clica no botão "Salvar"
	 * 
	 * @param navegador
	 * @param nome
	 * @return HomePage
	 */
	public HomePage inserirNomeESalvarUsuario(WebDriver navegador, String nome) {
		
		inserirNome(nome);
		clicarNoBotaoSalvar(navegador);
		
		return new HomePage(navegador);
	}
	
}
