package parkmanager.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parkmanager.excepcion.ReglaNegocioException;
import parkmanager.modelo.Compra;
import parkmanager.modelo.TipoBoleto;
import parkmanager.servicio.VentasServicio;

import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class VentasController {
    private final VentasServicio ventas;

    public VentasController(VentasServicio ventas) {
        this.ventas = ventas;
    }

    @PutMapping("/descuentos/{tipo}")
    public RespuestaDescuento configurarDescuento(@PathVariable String tipo,
                                                   @RequestBody SolicitudDescuento solicitud) {
        TipoBoleto tipoBoleto = ventas.configurarDescuento(tipo, solicitud.porcentaje());
        return new RespuestaDescuento(tipoBoleto, solicitud.porcentaje(), "Descuento configurado");
    }

    @DeleteMapping("/descuentos/{tipo}")
    public RespuestaDescuento quitarDescuento(@PathVariable String tipo) {
        TipoBoleto tipoBoleto = ventas.quitarDescuento(tipo);
        return new RespuestaDescuento(tipoBoleto, BigDecimal.ZERO, "Descuento desactivado");
    }

    @PostMapping("/compras")
    public Compra registrarCompra(@RequestBody SolicitudCompra solicitud) {
        JsonNode tipo = solicitud.tipo();
        if (tipo == null || !tipo.isTextual()) {
            throw new ReglaNegocioException("SOLICITUD_INVALIDA", "El tipo de boleto debe ser texto");
        }
        return ventas.registrarCompra(tipo.textValue(), solicitud.cantidad());
    }

    public record SolicitudDescuento(BigDecimal porcentaje) {
    }

    public record SolicitudCompra(JsonNode tipo, BigInteger cantidad) {
    }

    public record RespuestaDescuento(TipoBoleto tipo, BigDecimal porcentaje, String mensaje) {
    }
}
