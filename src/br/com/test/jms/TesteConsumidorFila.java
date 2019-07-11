package br.com.test.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidorFila {
	
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//é o local concreto no qual a mensagem ira ser salva temporariamente. No caso uma fila teste.
		Destination fila = (Destination) context.lookup("teste");
		
		MessageConsumer consumer = session.createConsumer(fila);

		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
			
				TextMessage text = (TextMessage) message;
				
				try {
					System.out.println(text.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		System.out.wait(100000);
		session.close();
		connection.close();
		context.close();
	}
	
}
