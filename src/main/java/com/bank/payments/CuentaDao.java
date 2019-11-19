package com.bank.payments;

import org.springframework.jdbc.core.JdbcTemplate;

public class CuentaDao
{
    /**
     * Propiedad inyectada por Spring
     * con el metodo setJdbcTemplate(...)
     * */
    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Funcion utilizada por PaymentBusiness para realizar el pago
     * @param origen cuenta de la que se va a sustraer dinero
     * @param destino cuenta a la que se le abonará dinero
     * @param cantidad la cantidad de dinero a sustraer/abonar
     */
    public void makeTransaction(String origen, String destino, double cantidad)
    {
        takeMoneyFrom(origen, cantidad);
        addMoneyTo(destino, cantidad);
    }

    private void takeMoneyFrom(String cuenta, double cantidad)
    {
        String update = "UPDATE cuentas SET saldo = saldo - %s WHERE no_cuenta = %s";
        update = String.format(update, cantidad, cuenta);
        jdbcTemplate.execute(update);
    }

    private void addMoneyTo(String cuenta, double cantidad)
    {
        String update = "UPDATE cuentas SET saldo = saldo + %s WHERE no_cuenta = %s";
        update = String.format(update, cantidad, cuenta);
        jdbcTemplate.execute(update);
    }
}
