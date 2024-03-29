package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Utils.BaseUtil;

public class HomePage extends BaseUtil {

	WebDriver navegador;
	
	public HomePage(WebDriver navegador) {
		this.navegador = navegador;
	}
	
	/**Clica na opção "Contas" no menu superior
	 * 
	 * @param navegador
	 * @return
	 */
	public HomePage clicarNoMenuContas(WebDriver navegador) {
		
		navegador.findElement(By.xpath("//a[@class=\"dropdown-toggle\"]")).click();
		
		return this;
	}
	
	/**Clica na opção "Adicionar" nas opções do menu superior "Contas"
	 * 
	 * @param navegador
	 * @return AdicionarPage
	 */
	public AdicionarPage clicarNaOpcaoAdicionar(WebDriver navegador) {
		
		navegador.findElement(By.xpath("//a[text()=\"Adicionar\"]")).click();
		
		return new AdicionarPage(navegador);
	}
	
	/**Clica na opção "Contas" nas opções do menu superior "Listar"
	 * 
	 * @param navegador
	 * @return ListarPage
	 */
	public ListaDeContasPage clicarNaOpcaoLista(WebDriver navegador) {
		
		navegador.findElement(By.xpath("//a[text()=\"Listar\"]")).click();
		
		return new ListaDeContasPage(navegador);
	}
	
	public HomePage clicarResetDadosBancoDeDados(WebDriver navegador) {
		
		navegador.findElement(By.linkText("reset")).click();
		
		return this;
	}
}
