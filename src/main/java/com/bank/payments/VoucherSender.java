package com.bank.payments;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class VoucherSender extends JmsTemplate
{
    public void sendMessage(final String queue, final String message)
    {
        this.send(queue, new MessageCreator()
        {
            public Message createMessage(final Session session) throws JMSException
            {
                return session.createTextMessage(message);
            }
        });
    }
}
