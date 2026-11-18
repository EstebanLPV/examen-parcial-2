package parkmanager.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import parkmanager.excepcion.ReglaNegocioException;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(ReglaNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespuestaError errorDeNegocio(ReglaNegocioException e) {
        return new RespuestaError(e.getCodigo(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespuestaError errorDeJson() {
        return new RespuestaError("SOLICITUD_INVALIDA", "El cuerpo debe ser JSON con los campos correctos");
    }

    public record RespuestaError(String codigo, String mensaje) {
    }
}
