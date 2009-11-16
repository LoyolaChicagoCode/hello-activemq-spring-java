package com.wordpress.stefdb.activemq;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

public class SpringConsumer implements MessageListener {

	private String id = "foo";

	private JmsTemplate template;

	private Destination destination;

	private Connection connection;

	private Session session;

	private MessageConsumer consumer;

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void start() throws JMSException {
		String selector = "next = '" + id + "'";

		ConnectionFactory connectionFactory = template.getConnectionFactory();
		connection = connectionFactory.createConnection();

		synchronized (connection) {
			if (connection.getClientID() == null) {
				connection.setClientID(id);
			}
		}

		connection.start();

		session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
		consumer = session.createConsumer(destination, selector, false);
		consumer.setMessageListener(this);
	}

	public void stop() throws JMSException {
		if (consumer != null)
			consumer.close();
		if (session != null)
			session.close();
		if (connection != null)
			connection.close();
	}

	public void onMessage(Message message) {
		try {
			// do something funky with the text message
			System.out.println("Got the message!");
			System.out.println("Message properties:");
			for (final Enumeration<String> e = message.getPropertyNames(); e.hasMoreElements(); ) {
				final String name = e.nextElement();
				System.out.println("  " + name + ": " + message.getObjectProperty(name));
			}
			if (message instanceof TextMessage) {
				System.out.println("It's a text message: " + ((TextMessage) message).getText());
			}
			message.acknowledge();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
