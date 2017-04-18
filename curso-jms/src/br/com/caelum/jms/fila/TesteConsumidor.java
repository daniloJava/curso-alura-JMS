package br.com.caelum.jms.fila;

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

/**Recebe mensagem
 * 
 * @author tapower
 *
 */
public class TesteConsumidor {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		//baseia no arquivo de configuração jndi.properties:
		InitialContext context = new InitialContext(); 

        //imports do package javax.jms
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();
        
        /*
         * A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
         * Além disso, também serve para produzir o 
         * MessageConsumer! É um objeto todo poderoso que criamos a partir da conexão:
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        /* 
         * O ActiveMQ ou MOM em geral pode ter vários consumidores e receber mensagens 
         * de vários clientes. Para organizar o recebimento e a entrega das mensagens
         * criamos destinos (ou Destination) no MOM. A nossa fila.financeiro é um Destination, ou seja, 
         * o lugar concreto onde a mensagem será salvo temporariamente. 
         */
        Destination fila = (Destination) context.lookup("financeiro");
        MessageConsumer consumer = session.createConsumer(fila);
        
        
        consumer.setMessageListener(new MessageListener(){
            @Override
            public void onMessage(Message message){
                TextMessage textMessage  = (TextMessage)message;
                try{
                    System.out.println(textMessage.getText());
                } catch(JMSException e){
                    e.printStackTrace();
                }    
            }
        });

        new Scanner(System.in).nextLine(); //parar o programa para testar a conexao

        connection.close();
        context.close();
        
	}
}
