package br.com.test.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import br.com.test.jms.model.Pedido;
//topico � utilizado para enviar a mesma mesagem para varios consumidores diferentes(broadcast)
public class TesteConsumidorTopicoSelector {
	
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//identifica conecao 
		connection.setClientID("testandoTopicoSelector");
		connection.start();
		
		//A Session no JMS abstrai o trabalho transacional e confirma��o do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//� o local concreto no qual a mensagem ira ser salva temporariamente. No caso uma topico teste.
		Topic topic = (Topic) context.lookup("test");
		
		//Assinaturas dur�veis s� existem para t�picos. Uma assinatura dur�vel � nada mais do que um consumidor de um t�pico que se identificou. Ou seja, o t�pico sabe da exist�ncia desse consumidor.
		//O t�pico, por padr�o, n�o garante a entrega da mensagem, pois n�o sabe se existe 1 ou 20 consumidores. Ent�o de cara, o t�pico s� entrega as mensagens para consumidores que estiverem online.
		MessageConsumer consumer = session.createDurableSubscriber(topic, "assinatura-selector", "teste-selector is null OR teste-selector=false", false);

		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
			
//				TextMessage text = (TextMessage) message;
				ObjectMessage objectMessage = (ObjectMessage) message;
				try {
					Pedido pedido =(Pedido)  objectMessage.getObject();
					System.out.println(pedido);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
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
