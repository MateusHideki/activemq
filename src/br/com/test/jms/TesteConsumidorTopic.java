package br.com.test.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
//topico é utilizado para enviar a mesma mesagem para varios consumidores diferentes(broadcast)
public class TesteConsumidorTopic {
	
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//identifica conecao 
		connection.setClientID("testandoTopico");
		connection.start();
		
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//é o local concreto no qual a mensagem ira ser salva temporariamente. No caso uma topico teste.
		Topic topic = (Topic) context.lookup("teste");
		
		//Assinaturas duráveis só existem para tópicos. Uma assinatura durável é nada mais do que um consumidor de um tópico que se identificou. Ou seja, o tópico sabe da existência desse consumidor.
		//O tópico, por padrão, não garante a entrega da mensagem, pois não sabe se existe 1 ou 20 consumidores. Então de cara, o tópico só entrega as mensagens para consumidores que estiverem online.
		MessageConsumer consumer = session.createDurableSubscriber(topic, "assinatura");

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
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}
	
}
