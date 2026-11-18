package parkmanager.modelo;

import java.math.BigDecimal;

public class BoletoAlumno extends Boleto {
    private static final BigDecimal PRECIO_BASE = new BigDecimal("650.00");

    public BoletoAlumno() {
        super(TipoBoleto.ALUMNO);
    }

    @Override
    public BigDecimal getPrecioBase() {
        return PRECIO_BASE;
    }
}
