package com.bank.payments;

import com.bank.PaymentDocument;
import com.bank.PaymentDocument.Payment;
import com.bank.VoucherDocument.Voucher;
import com.bank.VoucherDocument;

public class PaymentBusiness
{
    /**
     * Propiedades inyectadas por Spring
     * con los siguentes metodos:
     *
     * setVoucherSender(...)
     * setCuentaDao(...)
     */
    private VoucherSender voucherSender; //agrega a la cola jms/voucherQueue
    private CuentaDao cuentaDao; //Hace las transaccines a bases de datos

    public void setVoucherSender(VoucherSender voucherSender)
    {
        this.voucherSender = voucherSender;
    }

    public void setCuentaDao(CuentaDao cuentaDao)
    {
        this.cuentaDao = cuentaDao;
    }

    /**
     * Equivalente al main de PaymentListener,
     * recibe lo que hay en cola, y lo intenta parsear para hacer el pago
     * @param xml String que está en la jms/paymentQueue
     */
    public void managePay(String xml)
    {
        System.out.println("Mensaje recibido: " + xml);
        System.out.println("Construyendo voucher...");
        Voucher voucher = buildVoucher();
        voucher.setPagado(false);
        try
        {
            System.out.println("Parseando mensaje...");
            PaymentDocument document = PaymentDocument.Factory.parse(xml);
            Payment payment = document.getPayment();

            System.out.println("Haciendo transaccion (haciendo pago)...");
            cuentaDao.makeTransaction(payment.getOrigen(), payment.getDestino(), payment.getCantidad());

            System.out.println("Pagado perro");
            voucher.setPagado(true);
        }catch (Exception e)
        {
            System.out.println("Mostrando errores...");
            e.printStackTrace();
            System.out.println("No se pudo realizar el pago :c");
        }
        /* AQUI SE ENVIA LA RESPUESTA DEL BANCO COMO UN XML*/
        voucherSender.sendMessage("jms/voucherQueue", voucher.xmlText());
    }

    private Voucher buildVoucher()
    {
        return  VoucherDocument.Factory.
                newInstance().addNewVoucher();
    }
}
