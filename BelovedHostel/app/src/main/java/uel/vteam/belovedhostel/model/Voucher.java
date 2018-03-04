package uel.vteam.belovedhostel.model;

/**
 * Created by Hieu on 12/5/2016.
 */

public class Voucher   {
     String idVoucher;
    String  percent;

    public Voucher() {
    }

    public Voucher(String idVoucher,String percent) {

        this.idVoucher = idVoucher;
        this.percent = percent;
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
