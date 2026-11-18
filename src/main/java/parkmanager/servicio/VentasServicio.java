package parkmanager.servicio;

import org.springframework.stereotype.Service;
import parkmanager.excepcion.ReglaNegocioException;
import parkmanager.modelo.Boleto;
import parkmanager.modelo.BoletoAlumno;
import parkmanager.modelo.BoletoGeneral;
import parkmanager.modelo.BoletoProfesor;
import parkmanager.modelo.Compra;
import parkmanager.modelo.PaseAnual;
import parkmanager.modelo.TipoBoleto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;

@Service
public class VentasServicio {
    private final Map<TipoBoleto, Boleto> boletos = new EnumMap<>(TipoBoleto.class);
    private long boletosAcumulados;
    private BigDecimal ingresosAcumulados = new BigDecimal("0.00");

    public VentasServicio() {
        boletos.put(TipoBoleto.GENERAL, new BoletoGeneral());
        boletos.put(TipoBoleto.PASE_ANUAL, new PaseAnual());
        boletos.put(TipoBoleto.ALUMNO, new BoletoAlumno());
        boletos.put(TipoBoleto.PROFESOR, new BoletoProfesor());
    }

    public synchronized TipoBoleto configurarDescuento(String tipo, BigDecimal porcentaje) {
        Boleto boleto = buscarBoleto(tipo);
        boleto.configurarDescuento(porcentaje);
        return boleto.getTipo();
    }

    public synchronized TipoBoleto quitarDescuento(String tipo) {
        Boleto boleto = buscarBoleto(tipo);
        boleto.quitarDescuento();
        return boleto.getTipo();
    }

    public synchronized Compra registrarCompra(String tipo, BigInteger cantidad) {
        if (cantidad == null) {
            throw new ReglaNegocioException("SOLICITUD_INVALIDA", "Falta la cantidad de boletos");
        }
        Boleto boleto = buscarBoleto(tipo);
        if (cantidad.signum() <= 0 || cantidad.compareTo(BigInteger.TEN) > 0) {
            throw new ReglaNegocioException("CANTIDAD_INVALIDA", "La cantidad debe estar entre 1 y 10");
        }

        int cantidadBoletos = cantidad.intValue();
        BigDecimal precioFinal = boleto.getPrecioFinal();
        BigDecimal total = precioFinal.multiply(BigDecimal.valueOf(cantidadBoletos));
        ingresosAcumulados = ingresosAcumulados.add(total);
        boletosAcumulados += cantidadBoletos;

        return new Compra(boleto.getTipo(), cantidadBoletos, boleto.getPrecioBase(), boleto.getDescuento(),
                precioFinal, total, ingresosAcumulados);
    }

    public synchronized long getBoletosAcumulados() {
        return boletosAcumulados;
    }

    private Boleto buscarBoleto(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ReglaNegocioException("SOLICITUD_INVALIDA", "Falta el tipo de boleto");
        }
        try {
            return boletos.get(TipoBoleto.valueOf(tipo));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("TIPO_BOLETO_INVALIDO", "Tipo de boleto inválido: " + tipo);
        }
    }
}
