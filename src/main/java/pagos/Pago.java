package pagos;

public abstract class Pago{
    private String idPago;
    private double monto;

    //PROCESAR PAGO
    public boolean procesarPago(){
        return false;
    }

    //CONFIRMAR PAGO
    public void confirmarPago(boolean validado){

    }
}
