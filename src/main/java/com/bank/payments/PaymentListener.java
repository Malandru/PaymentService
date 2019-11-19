package com.bank.payments;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PaymentListener implements MessageListener
{
    /**
     * Se encarga de atender la peticion del servicio
     * asincrono. Debe recibir un xml de tipo Payment.
     *
     * Propiedad inyectada por Spring
     * con el metdodo setPaymentBusiness(...)
     */
    private PaymentBusiness paymentBusiness;

    @Override
    public void onMessage(Message message)
    {
        try
        {
            TextMessage textMessage = (TextMessage) message;
            String xml = textMessage.getText();
            paymentBusiness.managePay(xml);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setPaymentBusiness(PaymentBusiness paymentBusiness)
    {
        this.paymentBusiness = paymentBusiness;
    }
}
