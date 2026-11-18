package parkmanager.modelo;

import java.math.BigDecimal;

public record Compra(
        TipoBoleto tipo,
        int cantidad,
        BigDecimal precioBase,
        BigDecimal descuentoAplicado,
        BigDecimal precioUnitarioFinal,
        BigDecimal total,
        BigDecimal ingresosAcumulados
) {
}
