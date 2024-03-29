package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Utils.BaseUtil;

public class EditarContaPage extends BaseUtil{

	WebDriver navegador = null;
	
	public EditarContaPage(WebDriver navegador) {
		this.navegador = navegador;
	}
	
	/**Limpa o campo de nome da conta e insere o novo valor, que é passado
	 * 
	 * @param navegador
	 * @param novoNome
	 * @return EditarContaPage
	 */
	public EditarContaPage inserirNovoValorNoCampoNome(WebDriver navegador, String novoNome) {
		
		WebElement inputNome = navegador.findElement(By.id("nome"));
		inputNome.clear();
		
		inputNome.sendKeys(novoNome);
		
		return this;
		
	}
	
	public String getValorCampoNome(WebDriver navegador) {
		
		return navegador.findElement(By.id("nome")).getAttribute("value");
	}
	
	public ListaDeContasPage clicarNoBotaoSalvar(WebDriver navegador) {
		
		navegador.findElement(By.tagName("button")).click();
		
		return new ListaDeContasPage(navegador);
	}

}
