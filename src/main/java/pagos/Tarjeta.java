package pagos;

public class Tarjeta extends Pago{
    private int numTarjeta;
    private int codigoSeguridad;
    private String nombreTitular;

    //VERIFICAR SALDO
    public boolean verifSaldo(int numTarjeta, int codigoSeguridad){
        return true;
    }
}
