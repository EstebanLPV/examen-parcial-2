package parkmanager.modelo;

import java.math.BigDecimal;

public class PaseAnual extends Boleto {
    private static final BigDecimal PRECIO_BASE = new BigDecimal("2400.00");

    public PaseAnual() {
        super(TipoBoleto.PASE_ANUAL);
    }

    @Override
    public BigDecimal getPrecioBase() {
        return PRECIO_BASE;
    }
}
