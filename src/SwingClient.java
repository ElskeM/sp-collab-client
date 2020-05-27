import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import domain.Article;
import domain.Customer;
import domain.CustomerOrder;

public class SwingClient extends JFrame {

	Client client = ClientBuilder.newClient();
	private JList jList;
	DefaultListModel<Article> model = new DefaultListModel<Article>();
	DefaultListModel<CustomerOrder> orderModel = new DefaultListModel<CustomerOrder>();
	JScrollPane listContainer = new JScrollPane();

	public static void main(String[] args) {
		new SwingClient();

	}

	public SwingClient() {
		
		for(Article a : getAllArticles()) {
			model.addElement(a);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		
		jList =new JList(model);
		scrollPane.setViewportView(jList);
		add(scrollPane);
		setLayout(new GridLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		
		JButton btnMoreInfo = new JButton("More info");
		btnMoreInfo.addActionListener(e -> infoScreen(jList.getSelectedValue()));
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> infoScreen(new Article("", "", 0, 0)));
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e -> Main.deleteArticle(((Article) jList.getSelectedValue()).getArtNr()));
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> refreshList());
		JButton btnOrders = new JButton("Orders");
		btnOrders.addActionListener(e -> orderScreen());

		buttons.add(btnMoreInfo);
		buttons.add(btnAdd);
		buttons.add(btnDelete);
		buttons.add(btnRefresh);
		buttons.add(btnOrders);

		add(buttons);
		setTitle("Master");
		setBounds(350, 200, 860, 550);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void refreshList() {
		model.removeAllElements();
		for(Article a : getAllArticles()) {
			model.addElement(a);
		}
	}
	
	public void orderScreen() {
		for(CustomerOrder o : getAllOrders()) {
			orderModel.addElement(o);
		}
		//System.out.println(orderModel);
		JList orderList =new JList(orderModel);
		JScrollPane scrollPane = new JScrollPane();
		JFrame orderFrame = new JFrame("INFO");
		orderFrame.setLayout(new FlowLayout());
		orderFrame.add(orderList);
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(orderList);
		leftPanel.add(scrollPane);
		setLayout(new GridLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		
		JButton btnMoreInfo = new JButton("More info");
		btnMoreInfo.addActionListener(e -> infoScreen(jList.getSelectedValue()));
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> infoScreen(new Article("", "", 0, 0)));
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e -> Main.deleteArticle(((Article) jList.getSelectedValue()).getArtNr()));
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> refreshList());

		buttons.add(btnMoreInfo);
		buttons.add(btnAdd);
		buttons.add(btnDelete);
		buttons.add(btnRefresh);

		orderFrame.add(buttons);
		orderFrame.setTitle("Orders");
		orderFrame.setBounds(350, 200, 860, 550);
		orderFrame.setVisible(true);
	}

	private void infoScreen(Object selectedValue) {
		JFrame infoFrame = new JFrame("INFO");
		infoFrame.setLayout(new FlowLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		Article article = (Article) selectedValue;
		System.out.println(article.getArtNr());
		leftPanel.add(new JLabel("Name: "));
		JTextField textField3 = new JTextField(article.getName());
		String btnText;
		boolean update;
		if (article.getName() != "") {
			textField3.setEditable(false);
			update = true;
			btnText = "Update";
		} else {
			update = false;
			btnText = "Add";
		}
		leftPanel.add(textField3);
		leftPanel.add(new JLabel("ArtNr: " + article.getArtNr()));
		leftPanel.add(new JLabel("Stock:"));
		JTextField textField1 = new JTextField("" + article.getStock());
		leftPanel.add(textField1);
		JTextField textField2 = new JTextField("" + article.getPrice());
		leftPanel.add(new JLabel("Price:"));
		leftPanel.add(textField2);
		JButton btnupdate = new JButton(btnText);
		JTextPane textPane = new JTextPane();
		textPane.setText(article.getDescription());
		btnupdate.addActionListener(e -> {
			btnupdate.add(textField1);
			article.setName(textField3.getText());
			article.setDescription(textPane.getText());
			article.setStock(Integer.parseInt(textField1.getText()));
			article.setPrice(Double.parseDouble(textField2.getText()));
			updateArticle(article, update);
			JOptionPane.showMessageDialog(infoFrame, "DONE!");
		});
		leftPanel.add(btnupdate);
		infoFrame.add(leftPanel);

		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		infoFrame.add(paneScrollPane);
		infoFrame.setBounds(350, 200, 860, 550);
		infoFrame.setVisible(true);
	}

	private List<Article> getAllArticles() {

		Response response = client.target("http://localhost:8080/olfdb/Pantheon/articles").request("application/JSON")
				.buildGet().invoke();
		List<Article> articles = response.readEntity(new GenericType<List<Article>>() {
		});

		return articles;

	}

	public void updateArticle(Article article, boolean isUpdate) {

		Entity artEntity = Entity.entity(article, "application/JSON");
		Response response;
		if (!isUpdate) {
			response = client.target("http://localhost:8080/olfdb/Pantheon/articles/").request().buildPost(artEntity)
					.invoke();
			System.out.println("Inserting Article returned status code of " + response.getStatus());
		} else {
			response = client.target("http://localhost:8080/olfdb/Pantheon/articles/" + article.getArtNr()).request()
					.buildPut(artEntity).invoke();
			System.out.println("Updating Article returned status code of " + response.getStatus());
		}
		System.out.println(response.getHeaders().toString());
		System.out.println(response.readEntity(String.class));

		response.close();

	}

	public List<CustomerOrder> getAllOrders() {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/orders").request("application/JSON")
				.buildGet().invoke();
		List<CustomerOrder> orders = response.readEntity(new GenericType<List<CustomerOrder>>() {
		});
		return orders;

		// System.out.println(response.readEntity(String.class));
	}
}
