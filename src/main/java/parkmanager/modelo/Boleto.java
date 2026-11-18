package parkmanager.modelo;

import java.math.BigDecimal;

public abstract class Boleto {
    private final TipoBoleto tipo;

    protected Boleto(TipoBoleto tipo) {
        this.tipo = tipo;
    }

    public TipoBoleto getTipo() {
        return tipo;
    }

    public abstract BigDecimal getPrecioBase();
}
