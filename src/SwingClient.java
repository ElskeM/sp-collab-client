import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
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
	// private JList jList;
	DefaultListModel<Article> model = new DefaultListModel<Article>();
	DefaultListModel<CustomerOrder> orderModel = new DefaultListModel<CustomerOrder>();
	JScrollPane listContainer = new JScrollPane();

	public static void main(String[] args) {
		new SwingClient();

	}

	public SwingClient() {
		
		
		JButton btnArt = new JButton("Articles");
		JButton btnOrders = new JButton("Orders");
		setLayout(new FlowLayout());
		btnOrders.addActionListener(e -> orderScreen());
		btnArt.addActionListener(e -> articleScreen());
		add(btnArt);
		add(btnOrders);
		setBounds(350, 200, 90, 120);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void articleScreen() {
		JFrame articleWindow = new JFrame("Master");
		for (Article a : getAllArticles()) {
			model.addElement(a);
		}

		JScrollPane scrollPane = new JScrollPane();

		JList jList = new JList(model);
		scrollPane.setViewportView(jList);
		articleWindow.add(scrollPane);
		articleWindow.setLayout(new GridLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		JButton btnMoreInfo = new JButton("More info");
		btnMoreInfo.addActionListener(e -> articleInfoScreen(jList.getSelectedValue()));
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> articleInfoScreen(new Article("", "", 0, 0)));
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e -> {
			Main.deleteArticle(((Article) jList.getSelectedValue()).getArtNr());
			refreshList();
			JOptionPane.showMessageDialog(this, "GONE!");
		});
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> refreshList());

		buttons.add(btnMoreInfo);
		buttons.add(btnAdd);
		buttons.add(btnDelete);
		buttons.add(btnRefresh);

		articleWindow.add(buttons);
		articleWindow.setTitle("Articles");
		articleWindow.setBounds(350, 200, 860, 550);
		articleWindow.setVisible(true);
		
	}

	private void refreshList() {
		model.removeAllElements();
		for (Article a : getAllArticles()) {
			model.addElement(a);
		}
	}

	public void orderScreen() {
		orderModel.removeAllElements();
		for (CustomerOrder o : getAllOrders()) {
			orderModel.addElement(o);
		}
		// System.out.println(orderModel);
		JList orderList = new JList(orderModel);
		orderList.setCellRenderer(new OrderRenderer());

		JScrollPane scrollPane = new JScrollPane();
		JFrame orderFrame = new JFrame("INFO");
		orderFrame.setLayout(new GridLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(orderList);
		leftPanel.add(scrollPane);
		orderFrame.add(leftPanel);
		setLayout(new GridLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		JButton btnMoreInfo = new JButton("More info");
		btnMoreInfo.addActionListener(e -> orderInfoScreen((CustomerOrder) (orderList.getSelectedValue())));
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> orderInfoScreen(new CustomerOrder()));
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e -> {
			Main.deleteCustomerOrder(((CustomerOrder) orderList.getSelectedValue()).getOrderNr());
			orderModel.removeAllElements();
			for (CustomerOrder o : getAllOrders()) {
				orderModel.addElement(o);
			}
			JOptionPane.showMessageDialog(orderFrame, "GONE!");
		});
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> {orderModel.removeAllElements();
		for (CustomerOrder o : getAllOrders()) {
			orderModel.addElement(o);
		}});

		buttons.add(btnMoreInfo);
		buttons.add(btnAdd);
		buttons.add(btnDelete);
		buttons.add(btnRefresh);

		orderFrame.add(buttons);
		orderFrame.setTitle("Orders");
		orderFrame.setBounds(350, 200, 860, 550);
		orderFrame.setVisible(true);
	}

	private void orderInfoScreen(CustomerOrder order) {
		JFrame orderFrame = new JFrame("INFO ORDER");
		orderFrame.setLayout(new GridBagLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		leftPanel.add(new JLabel("Order number: " + order.getOrderNr()));

		leftPanel.add(new JLabel("Order date: "));
		JTextField txtODate = new JTextField(order.getorderDate());
		leftPanel.add(txtODate);
//		leftPanel.add(new JLabel("Customer: " + order.getCustomer().getCustomerNr() + " "
//				+ order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName()));
		JComboBox<Customer> comboCustomers = new JComboBox(getAllCustomers().toArray());

		leftPanel.add(comboCustomers);
		JComboBox<Article> comboArticles = new JComboBox(getAllArticles().toArray());
		comboArticles.setRenderer(new ArticleComboRenderer());
		rightPanel.add(comboArticles);
		rightPanel.add(new JLabel("Quantity:"));
		JTextField quantity = new JTextField();
		// quantity.setPreferredSize(new Dimension(50,20));
		rightPanel.add(quantity);
		JButton btnAddArt = new JButton("Add article");
		rightPanel.add(btnAddArt);
		JButton btnDelArt = new JButton("Remove article");
		rightPanel.add(btnDelArt);
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		dtm.setColumnIdentifiers(new String[] { "ARTICLE NR", "QUANTITY" });

		JTable table = new JTable();

		btnAddArt.addActionListener(e -> {
			dtm.addRow(new String[] { "" + ((Article) comboArticles.getSelectedItem()).getArtNr(),
					"" + Integer.parseInt(quantity.getText()) });
		});
		btnDelArt.addActionListener(e -> {
			dtm.removeRow(table.getSelectedRow());
		});
		table.setModel(dtm);
		JScrollPane pain = new JScrollPane(table);
		pain.setPreferredSize(new Dimension(140, 190));
		rightPanel.add(pain);
		rightPanel.add(new JLabel("Dispatch date:"));
		JTextField dispatchDate = new JTextField();
		dispatchDate.setText(order.getDispatchDate());
		rightPanel.add(dispatchDate);
		JButton btnDone = new JButton("Add order");
		btnDone.addActionListener(e -> {
			order.setOrderDate(txtODate.getText());
			order.setCustomer((Customer) comboCustomers.getSelectedItem());
			order.setDispatchDate(dispatchDate.getText());
			Map<String, Integer> updatedArt = new HashMap<String, Integer>();
			for (int i = 0; i < dtm.getRowCount(); i++) {
				updatedArt.put((String) dtm.getValueAt(i, 0), Integer.parseInt((String) dtm.getValueAt(i, 1)));
			}
			order.setArticles(updatedArt);
			updateOrder(order);
			//JOptionPane.showMessageDialog(orderFrame, "DONE!");
		});
		// cO.getArticles(), cO.getDispatchDate());

		if (order.getOrderNr() != 0) {
			for (Entry<String, Integer> entry : order.getArticles().entrySet()) {
				dtm.addRow(new String[] { entry.getKey(), "" + entry.getValue() });

			}
			btnDone.setText("Update");
			comboCustomers.setEditable(true);
			comboCustomers.setSelectedItem(order.getCustomer());
			txtODate.setEditable(false);
			comboCustomers.setEnabled(false);
		}
		rightPanel.add(btnDone);
		orderFrame.add(leftPanel);
		orderFrame.add(rightPanel);
		orderFrame.setBounds(350, 200, 910, 450);
		orderFrame.setVisible(true);
	}

	private List<Customer> getAllCustomers() {
		Response response = client.target("http://localhost:8080/olfdb/Pantheon/customers").request("application/JSON")
				.buildGet().invoke();
		List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {
		});
		return customers;
	}

	private void articleInfoScreen(Object selectedValue) {
		JFrame infoFrame = new JFrame("INFO ARTICLE");
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

		});
		leftPanel.add(btnupdate);
		infoFrame.add(leftPanel);

		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		infoFrame.add(paneScrollPane);
		infoFrame.setBounds(350, 200, 390, 230);
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
		JOptionPane.showMessageDialog(null, "DONE!");
		System.out.println(response.getHeaders().toString());
		System.out.println(response.readEntity(String.class));

		response.close();

	}

	public void updateOrder(CustomerOrder order) {

		Entity artEntity = Entity.entity(order, "application/JSON");
		Response response;
		if (order.getOrderNr() == 0) {
			response = client.target("http://localhost:8080/olfdb/Pantheon/orders/").request().buildPost(artEntity)
					.invoke();
			System.out.println("Inserting Order returned status code of " + response.getStatus());

		} else {
			response = client.target("http://localhost:8080/olfdb/Pantheon/orders/" + order.getOrderNr()).request()
					.buildPut(artEntity).invoke();
			System.out.println("Updating Order returned status code of " + response.getStatus());

		}
		if (response.getStatus() == 201) {
			JOptionPane.showMessageDialog(null, "DONE!");
			System.out.println(response.readEntity(String.class));
		} else {

			JOptionPane.showMessageDialog(null, "NOPE!\n" + response.readEntity(String.class));
		}

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

	class OrderRenderer extends JLabel implements ListCellRenderer<CustomerOrder> {
		private Border border;

		public OrderRenderer() {
			setOpaque(true);

			// This border is placed around a cell that is selected and has focus.

			border = BorderFactory.createLineBorder(Color.RED, 1);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends CustomerOrder> list, CustomerOrder value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText("Order number: " + value.getOrderNr() + " Order date: " + value.getorderDate());
			if (isSelected)

			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setFont(list.getFont());

			setEnabled(list.isEnabled());

			if (isSelected && cellHasFocus)
				setBorder(border);
			else
				setBorder(null);
			return this;
		}

	}

	class ArticleComboRenderer extends JLabel implements ListCellRenderer<Article> {

		public ArticleComboRenderer() {
			setOpaque(true);

			// This border is placed around a cell that is selected and has focus.

		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Article> list, Article value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText("Article number: " + value.getArtNr() + " Name: " + value.getName() + " Stock: "
					+ value.getStock());
			if (isSelected)

			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setFont(list.getFont());

			setEnabled(list.isEnabled());

			return this;
		}

	}
}
