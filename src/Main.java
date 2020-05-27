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
		// addCustomer();
		// addArticle();
		// addOrder();
		// printCustomer(111);
		// printCustomersByLastname("Johnson");
		// printArticleBetweenId(10003, 10006);
		// printAllArticles();
		// printAllCustomers();
		// printAllOrders();
		// updateCustomer(111);
		// updateArticle(20151);
		// deleteCustomer(111);
		// deleteArticle(15151);
		// printOrdersBetweenDates("2006-01-06", "2006-01-14");
		// deleteCustomerOrder(323);
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

		Customer c = getCustomer(111);

		Article bult = getArticle(10000);

		Map<String, Integer> articles = new HashMap<>();
		articles.put("" + bult.getArtNr(), 5);

		CustomerOrder order = new CustomerOrder();
		order.setArticles(articles);
		order.setCustomer(c);
		order.setOrderDate("2020-05-25");

		Entity orderEntity = Entity.entity(order, "application/JSON");

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/orders").request("application/JSON")
				.buildPost(orderEntity).invoke();

		System.out.println("Creating new order returned status code of " + response.getStatus());
		if (response.getStatus() == 201) {
			System.out.println(response.getHeaders().toString());
			// System.out.println(response.readEntity(Employee.class).getId());
			System.out.println(response.readEntity(String.class));
		}
		response.close();
	}

	private static void printAllArticles() {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles").request("application/JSON")
				.buildGet().invoke();
		System.out.println(response.readEntity(String.class));

	}

	public static void printAllCustomers() {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers").request("application/JSON")
				.buildGet().invoke();

		System.out.println(response.readEntity(String.class));
	}

	public static void printCustomersByLastname(String lastName) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers?lastName=" + lastName)
				.request("application/JSON").buildGet().invoke();
		// System.out.println("String entity: " + response.readEntity(String.class));
		List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {
		});

		for (Customer next : customers) {
			System.out.println(next);
		}
	}

	public static void addCustomer() {

		Customer dan = new Customer("Dan", "Dansson", "Danstig 1", "12332", "Danekil", 0);

		Entity danEntity = Entity.entity(dan, "application/JSON");

		for (int i = 0; i < 100; i++) {
			Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers").request()
					.buildPost(danEntity).invoke();
			System.out.println("Creating new customer returned status code of " + response.getStatus());
			if (response.getStatus() == 201) {
				System.out.println(response.getHeaders().toString());
				System.out.println(response.readEntity(String.class));
			}
			response.close();
		}
	}

	public static void updateCustomer(int id) {

		Customer simon = new Customer("Simon", "Hagelin", "Danstig 1", "12332", "Göteborg", 0);
		Entity simonEntity = Entity.entity(simon, "application/JSON");

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers/" + id).request()
				.buildPut(simonEntity).invoke();

		System.out.println("Header:" + response.getHeaders().toString());
		System.out.println("Status: " + response.getStatus());
		System.out.println("String entity: " + response.readEntity(String.class));
		response.close();
	}

	private static void deleteCustomer(int id) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers/" + id).request()
				.buildDelete().invoke();

		System.out.println("Header:" + response.getHeaders().toString());
		System.out.println("Status: " + response.getStatus());
		System.out.println("String entity: " + response.readEntity(String.class));

		response.close();

	}

	/**
	 * @author elske
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
		Article updatedArt = new Article();
		updatedArt.setDescription("Harvpinne som passar bl.a Browns. Höjd 127mm");
		updatedArt.setStock(60);
		updatedArt.setPrice(70.00);
		Entity artEntity = Entity.entity(updatedArt, "application/JSON");
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + artNr).request()
				.buildPut(artEntity).invoke();
		System.out.println("Updating Article returned status code of " + response.getStatus());
		System.out.println(response.getHeaders().toString());
		System.out.println(response.readEntity(String.class));

		response.close();

	}

	public static void deleteArticle(int artNr) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + artNr).request()
				.buildDelete().invoke();
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

	public static void printCustomer(int id) {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers/" + id)
				.request("application/JSON").buildGet().invoke();

		System.out.println(response.getHeaders().toString());
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
//		Customer cust = response.readEntity(Customer.class);
//		System.out.println(cust);
		response.close();
	}

	public static void printArticleBetweenId(int fId, int sId) {
		Response response = client
				.target("http://localhost:8080/olfdb/Pantheon/articles?firstId=" + fId + "&secondId=" + sId)
				.request("application/JSON").buildGet().invoke();

		List<Article> articles = response.readEntity(new GenericType<List<Article>>() {
		});

		for (Article next : articles) {
			System.out.println(next);
		}
	}

	public static void printOrdersBetweenDates(String fromDate, String toDate) {
		Response response = client.target(
				"http://localhost:8080/olfdb/Pantheon/orders?fromDate=" + fromDate + "&toDate=" + toDate)
				.request("application/JSON").buildGet().invoke();
		List<CustomerOrder> orders = response.readEntity(new GenericType<List<CustomerOrder>>() {
		});
		for (CustomerOrder next: orders) {
			System.out.println(next);
		}
	}
	
	private static void deleteCustomerOrder(int orderNr) {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/orders/" + orderNr).request()
				.buildDelete().invoke();

		System.out.println("Header:" + response.getHeaders().toString());
		System.out.println("Delete status was: " + response.getStatus());

		response.close();

	}

}