package com.wordpress.stefdb.activemq;

import javax.jms.JMSException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String... args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-context.xml");

		SpringConsumer consumer = (SpringConsumer) context.getBean("consumer");

		try {
			consumer.start();
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		SpringProducer producer = (SpringProducer) context.getBean("producer");
		producer.start();

	}
}
