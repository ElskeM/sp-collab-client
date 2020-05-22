import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

public class ArticleClient {
	
	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();
		
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/10000")
				.request("application/JSON").buildGet().invoke();
		
		System.out.println(response.readEntity(String.class));
		System.out.println("Header:" + response.getHeaders().toString());

		System.out.println("Status: " + response.getStatus());
		
		for(Link link :response.getLinks()) {
			System.out.println(link);
		}
		response.close();
		
		
	}

}
