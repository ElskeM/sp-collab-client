import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import domain.Article;
import domain.Customer;
import domain.CustomerOrder;

public class Main {

	static Client client = ClientBuilder.newClient();

	public static void main(String[] args) {
		// addCustomer(client);
		// updateCustomer(client);
		// deleteCustomer(client);
		// printCustomer(client);
		// printCustomersByLastname(client);
		printAllArticles();
		printAllCustomers();
		printAllOrders();
		// addOrder();
		printAllOrders();
		// addArticle();
		//updateArticle();
		deleteArticle(15151);
	}

	private static Customer getCustomer(int id) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers/" + id)
				.request("application/JSON").buildGet().invoke();

		Customer c = response.readEntity(Customer.class);
		response.close();

		return c;
	}

	private static Article getArticle(int id) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + id)
				.request("application/JSON").buildGet().invoke();

		Article a = response.readEntity(Article.class);
		response.close();

		return a;
	}

	private static void addOrder() {

//		Customer c = getCustomer(111);
//		
//		Article bult = getArticle(10000);
//		
//		Map<Article, Integer> articles = new HashMap<>();
//		articles.put(bult, 5);
//		
//		CustomerOrder order = new CustomerOrder();
//		order.setArticles(articles);
//		order.setCustomer(c);
//		order.setOrderDate("2020-05-25");
//		
//		Entity orderEntity = Entity.entity(order, "application/JSON");
//		
//		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles")
//				.request("application/JSON").buildPost(orderEntity).invoke();
//		
//		System.out.println("Creating new order returned status code of " + response.getStatus());
//		if (response.getStatus() == 201) {
//			System.out.println(response.getHeaders().toString());
//			// System.out.println(response.readEntity(Employee.class).getId());
//			System.out.println(response.readEntity(String.class));
//		}
//		response.close();
	}

	private static void printAllArticles() {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles").request("application/JSON")
				.buildGet().invoke();
		System.out.println(response.readEntity(String.class));

	}

	public static void printAllCustomers() {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers").request("application/JSON")
				.buildGet().invoke();

//		List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {});
//		
//		for (Customer next : customers) {
//			System.out.println(next);
//		}

		System.out.println(response.readEntity(String.class));
	}

	public static void printCustomersByLastname(Client client) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers?lastName=Johnsson")
				.request("application/JSON").buildGet().invoke();
		// System.out.println("String entity: " + response.readEntity(String.class));
		List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {
		});

		for (Customer next : customers) {
			System.out.println(next);
		}
	}

	public static void addCustomer(Client c) {

		Customer dan = new Customer("Dan", "Dansson", "Danstig 1", "12332", "Danekil", 0);

		Entity danEntity = Entity.entity(dan, "application/JSON");

		for (int i = 0; i < 100; i++) {
			Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers").request()
					.buildPost(danEntity).invoke();
			System.out.println("Creating new customer returned status code of " + response.getStatus());
			if (response.getStatus() == 201) {
				System.out.println(response.getHeaders().toString());
				// System.out.println(response.readEntity(Employee.class).getId());
				System.out.println(response.readEntity(String.class));
			}
			response.close();
		}
	}

	public static void updateCustomer(Client c) {

		Customer simon = new Customer("Simon", "Hagelin", "Danstig 1", "12332", "Göteborg", 0);
		Entity simonEntity = Entity.entity(simon, "application/JSON");

		Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers?id=111").request()
				.buildPut(simonEntity).invoke();

		System.out.println("Header:" + response.getHeaders().toString());
		System.out.println("Status: " + response.getStatus());
		System.out.println("String entity: " + response.readEntity(String.class));
		response.close();
	}

	private static void deleteCustomer(Client client) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers/111").request().buildDelete()
				.invoke();

		System.out.println("Header:" + response.getHeaders().toString());
		System.out.println("Status: " + response.getStatus());
		System.out.println("String entity: " + response.readEntity(String.class));

//		response = client.target("http://localhost:8080/olfdb/Pantheon/customers/111").request("application/JSON")
//				.buildGet().invoke();
//		System.out.println("Header:" + response.getHeaders().toString());
//		System.out.println("Status: " + response.getStatus());
//		System.out.println("String entity: " + response.readEntity(String.class));
		response.close();

	}

	
	/**
	 *@author elske 
	 */
	public static void addArticle() {
		Article art = new Article("Harvpinne 8 mm", "Harvpinne som passar bl.a Browns", 20, 71.00);
		Entity artEntity = Entity.entity(art, "application/JSON");
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles").request()
				.buildPost(artEntity).invoke();
		System.out.println("Creating new Article returned status code of " + response.getStatus());
		if (response.getStatus() == 201) {
			System.out.println(response.getHeaders().toString());
			System.out.println(response.readEntity(String.class));

		}
		response.close();

	}

	/**
	 * @author elske
	 */
	public static void updateArticle(int artNr) {
		Article art = new Article("Harvpinne 8 mm", "Harvpinne som passar bl.a Browns. Höjd 127mm", 60, 71.00);
		Entity artEntity = Entity.entity(art, "application/JSON");
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + artNr).request()
				.buildPut(artEntity).invoke();
		System.out.println("Updating Article returned status code of " + response.getStatus());
		System.out.println(response.getHeaders().toString());
		System.out.println(response.readEntity(String.class));

		response.close();

	}
	
	public static void deleteArticle(int artNr) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + artNr).request().buildDelete()
				.invoke();
		System.out.println("Delete status was: " + response.getStatus());
		response.close();
	}
	
	

	public static void printAllOrders() {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/orders").request("application/JSON")
				.buildGet().invoke();
		List<CustomerOrder> orders = response.readEntity(new GenericType<List<CustomerOrder>>() {
		});
		for (CustomerOrder next : orders) {
			System.out.println(next);
		}

		// System.out.println(response.readEntity(String.class));
	}

	public static void printCustomer(Client c) {

		Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers/111").request("application/JSON")
				.buildGet().invoke();

		System.out.println(response.getHeaders().toString());
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
//		Customer cust = response.readEntity(Customer.class);
//		System.out.println(cust);
		response.close();
	}

}