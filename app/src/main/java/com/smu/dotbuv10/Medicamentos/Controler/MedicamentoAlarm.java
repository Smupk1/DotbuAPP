package com.smu.dotbuv10.Medicamentos.Controler;

import java.util.Date;

public class MedicamentoAlarm {
    int idMed;
    String medicamento;
    String hora;

    public MedicamentoAlarm(int idMed,
            String medicamento,
            String hora){
        this.idMed=idMed;
        this.medicamento=medicamento;
        this.hora=hora;
    }
}
