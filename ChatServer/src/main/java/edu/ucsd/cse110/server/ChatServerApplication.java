/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.server;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Session;
import javax.jms.MessageListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
//import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
@ComponentScan
public class ChatServerApplication {

    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(
                new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL));
    }
    
    @Bean
    MessageListenerAdapter receiver() {
        return new MessageListenerAdapter(new Server()) {{
        	setDefaultListenerMethod( "receive");
        	setMessageConverter(null);
        }};
    }
    
    @Bean
    SimpleMessageListenerContainer container(final MessageListener messageListener,
            final ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer() {{
            setMessageListener(messageListener);
            setConnectionFactory(connectionFactory);
            setDestinationName(Constants.QUEUENAME);
        }};
    }

    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
    

	public static void main(String[] args) throws Throwable {
		BrokerService broker = new BrokerService();
		broker.addConnector(Constants.ACTIVEMQ_URL);
		broker.setPersistent(false);
		broker.start();
		System.out.println("Sending a new message:");
		AnnotationConfigApplicationContext context = 
		          new AnnotationConfigApplicationContext(ChatServerApplication.class);
		
		MessageCreator messageCreator = new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("ping!");
			}
        }; 
        
        //JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        
        //jmsTemplate.send(Constants.QUEUENAME, messageCreator);
        /*
        MessageListenerAdapter mla = context.getBean(MessageListenerAdapter.class);*/
        
        //Message sent = jmsTemplate.receive( Constants.QUEUENAME );
        //new Server().receive( ((TextMessage)sent).getText() );
        //context.close();
	}

}
