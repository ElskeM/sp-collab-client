
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import domain.Customer;
import domain.CustomerOrder;


public class Main {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		//addCustomer(client);
		// updateCustomer(client);
		// deleteCustomer(client);
		//printCustomer(client);
		//printCustomersByLastname(client);
		printAllArticles(client);
		printAllCustomers(client);
		printAllOrders(client);
	}
	
	
	private static void printAllArticles(Client client) {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles")
				.request("application/JSON").buildGet().invoke();
		System.out.println(response.readEntity(String.class));
		
	}


	public static void printAllCustomers(Client c) {
		
		Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers")
				.request("application/JSON").buildGet().invoke();
		
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

		for(int i = 0; i < 100; i++) {
			Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers")
					.request().buildPost(danEntity).invoke();
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

	public static void addArticle(Client c) {

	}

	public static void addOrder(Client c) {

	}
	
	public static void printAllOrders(Client c) {
		Response response = c.target("http://localhost:8080/olfdb/Pantheon/orders")
				.request("application/JSON").buildGet().invoke();
		List<CustomerOrder> orders = response.readEntity(new GenericType<List<CustomerOrder>>() {});
		for (CustomerOrder next : orders) {
			System.out.println(next);
		}
		
		//System.out.println(response.readEntity(String.class));
	}
	
	public static void printCustomer(Client c) {

		Response response = c.target("http://localhost:8080/olfdb/Pantheon/customers/111")
				.request("application/JSON").buildGet().invoke();

		System.out.println(response.getHeaders().toString());
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
//		Customer cust = response.readEntity(Customer.class);
//		System.out.println(cust);
		response.close();
	}

}
