package br.com.alura.loja;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTest {
	
	private HttpServer server;

	@Before
	public void startServidor() {
		server = Servidor.inicializaServidor();
	}
	
	@After
	public void mataServidor() {
		server.stop();
	}

	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		Projeto projeto = target.path("/projetos/1").request().get(Projeto.class);
		Assert.assertEquals("Minha loja", projeto.getNome());
	}
	
	@Test
	public void testaQueSuportaNovosProjetos() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		Projeto projeto = new Projeto(2l, "Novo projeto", 1986);
		
        Entity<Projeto> entity = Entity.entity(projeto, MediaType.APPLICATION_XML);

        Response response = target.path("/projetos").request().post(entity);
        assertEquals(201, response.getStatus());
        
        String location = response.getHeaderString("Location");
        Projeto projetoCarregado = client.target(location).request().get(Projeto.class);
        Assert.assertTrue("Novo projeto".equals(projetoCarregado.getNome()));
	}

}
