package br.com.test.jms;

import java.util.Scanner;

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

//Por padrao o active mq tenta 6 vezes entregar a mensagem caso não entregue ele manda para uma fila especifica chamada DLQ =  Dead letter Queue
public class TesteConsumidorFilaDLQ {
	
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//é o local concreto no qual a mensagem ira ser salva temporariamente. No caso uma fila teste.
		Destination fila = (Destination) context.lookup("DLQ");
		
		MessageConsumer consumer = session.createConsumer(fila);

		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				System.out.println(message);
			}
			
		});
		
		new Scanner(System.in).nextLine();
		session.close();
		connection.close();
		context.close();
	}
	
}
