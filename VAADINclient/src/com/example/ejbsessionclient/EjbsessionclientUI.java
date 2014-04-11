package com.example.ejbsessionclient;

import gmx.sessionbeans.interfaces.GreetingInterfaceRemote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("ejbsessionclient")
public class EjbsessionclientUI extends UI {
	private Button btnLogin = new Button("Login");
    private TextField username = new TextField ( "Username");
	private GreetingInterfaceRemote remoteinterface = null;
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = EjbsessionclientUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		Button button = new Button("Click Me to get tablenames of eutdij database!");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				DataSource ds;				 
				try {
					Context ctx = new InitialContext();
					ds = (DataSource)ctx.lookup("java:/jdbc/eutdij");
					Connection con = ds.getConnection();
					Statement statement = con.createStatement();
					statement.setQueryTimeout(30);
					String sql = "show tables";
					ResultSet result = statement.executeQuery(sql);   
					if ( result.first()) {
						String tablanevek = "";
						do {
							String tabla = result.getString(1);
							tablanevek = tablanevek + tabla + ",";
						} while ( result.next());
						Notification.show("Táblanevek:", tablanevek, Notification.Type.TRAY_NOTIFICATION);
						
					}
					
				  } catch (Exception e) {
					e.printStackTrace();
					layout.addComponent(new Label(e.toString()));
				  }
			}
		});
		layout.addComponent(button);
		
		layout.addComponent ( new Label ("Please login in order to use the application") );
		layout.addComponent ( new Label () );
		layout.addComponent ( username );
		layout.addComponent ( btnLogin );
		btnLogin.addClickListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					Properties properties = new Properties();
		            properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		 
		            Context initialContext = new InitialContext(properties);
		            String appName = "";
		            String moduleName = "EJBSessionBean";
		            String distinctName = "";
		            String beanName = "GreetingMessage";
		            String interfaceName = "gmx.sessionbeans.interfaces.GreetingInterfaceRemote";
		            String name = "java:global" + appName + "/" + moduleName + "/" +distinctName    + "/" + beanName + "!" + interfaceName;
		            
		            // Ez összerakva így néz ki :
		            //name = "java:global/EJBSessionBean/GreetingMessage!gmx.sessionbeans.interfaces.GreetingInterfaceRemote";
		            
		            remoteinterface = (GreetingInterfaceRemote) initialContext.lookup(name);
		            if ( remoteinterface != null ) {
		            	String user = username.getValue();
		            	if ( user == null || user == "") {
		            		user = "Idegen";
		            	}
		            	String hello = remoteinterface.sayHello(user);
		            	Notification.show("Válasz üzenet:", hello, Notification.Type.TRAY_NOTIFICATION);
		            } else {
		            	Notification.show("Válasz üzenet:", "Fail !", Notification.Type.ERROR_MESSAGE);
		            }
		            
				  } catch (Exception e) {
					Notification.show("HIBA!", e.toString(), Notification.Type.TRAY_NOTIFICATION);
				  }
				
			}
		});
	}

}