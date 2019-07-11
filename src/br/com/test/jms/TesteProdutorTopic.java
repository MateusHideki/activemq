package br.com.test.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteProdutorTopic {
	
	public static void main(String[] args) throws Exception {
		
		
		//Outra forma de atribuir as propriedades de configuração
		
		/*Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
		properties.setProperty("queue.financeiro", "fila.financeiro");
		InitialContext context = new InitialContext(properties);*/		
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//é o local concreto no qual a mensagem ira ser salva temporariamente. No caso uma fila teste.
		Destination fila = (Destination) context.lookup("teste");
		
		
		MessageProducer produtor = session.createProducer(fila);

		for (int i = 0; i < 1000; i++) {
			Message message = session.createTextMessage("teste: "+ i);
			message.setBooleanProperty("teste-selector", false);
			produtor.send(message);
		}
		
		
		session.close();
		connection.close();
		context.close();
	}
	
}
