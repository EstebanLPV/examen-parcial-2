package parkmanager.modelo;

import parkmanager.excepcion.ReglaNegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Boleto {
    private final TipoBoleto tipo;
    private BigDecimal descuento = BigDecimal.ZERO;

    protected Boleto(TipoBoleto tipo) {
        this.tipo = tipo;
    }

    public TipoBoleto getTipo() {
        return tipo;
    }

    public abstract BigDecimal getPrecioBase();

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void configurarDescuento(BigDecimal porcentaje) {
        if (porcentaje == null) {
            throw new ReglaNegocioException("SOLICITUD_INVALIDA", "Falta el porcentaje");
        }
        if (porcentaje.signum() < 0 || porcentaje.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ReglaNegocioException("DESCUENTO_INVALIDO", "El porcentaje debe estar entre 0 y 100");
        }
        descuento = porcentaje;
    }

    public void quitarDescuento() {
        descuento = BigDecimal.ZERO;
    }

    public BigDecimal getPrecioFinal() {
        BigDecimal factor = BigDecimal.ONE.subtract(descuento.movePointLeft(2));
        return getPrecioBase().multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
