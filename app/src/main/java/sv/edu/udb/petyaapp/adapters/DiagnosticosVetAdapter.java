package sv.edu.udb.petyaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sv.edu.udb.petyaapp.Detalle_Diagnostico_Vet;
import sv.edu.udb.petyaapp.Detalles_Diagnostico;
import sv.edu.udb.petyaapp.R;
import sv.edu.udb.petyaapp.models.Diagnosticos;

public class DiagnosticosVetAdapter extends ArrayAdapter<Diagnosticos> {

    ArrayList<Diagnosticos> diagnosticoList;
    Context context;
    int resource;
    public DiagnosticosVetAdapter(Context context, int resource, ArrayList<Diagnosticos> diagnosticoList) {
        super(context, resource, diagnosticoList);
        this.diagnosticoList=diagnosticoList;
        this.context=context;
        this.resource=resource;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.listcitas,null,true);
        }
        Diagnosticos diagnosticoList=getItem(position);

        TextView textView=(TextView)convertView.findViewById(R.id.nommascota);
        TextView textView1=(TextView)convertView.findViewById(R.id.fechadecita);
        //TextView textView2=(TextView)convertView.findViewById(R.id.horadecita);

        String concat="";
        String concat1="";
        // String concat3="";

        concat= "Mascota: " + diagnosticoList.getNombre_mascota();
        concat1= "Fecha: " + diagnosticoList.getCreated_at();
        //concat3= "Hora: " + diagnosticoList.getHora();


        textView.setText(concat);
        textView1.setText(concat1);
        // textView2.setText(concat3);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Detalle_Diagnostico_Vet.class);
                intent.putExtra("nombre",diagnosticoList.getNombre_mascota());
                intent.putExtra("especie",diagnosticoList.getEspecie());
                intent.putExtra("raza",diagnosticoList.getRaza());
                intent.putExtra("edad",diagnosticoList.getEdad());
                intent.putExtra("sexo",diagnosticoList.getSexo());
                intent.putExtra("motivo",diagnosticoList.getMotivo());
                intent.putExtra("peso",diagnosticoList.getPeso());
                intent.putExtra("pulso",diagnosticoList.getPulso());
                intent.putExtra("temperatura",diagnosticoList.getTemperatura());
                intent.putExtra("diagnostico",diagnosticoList.getDiagnostico_final());
                intent.putExtra("tratamiento",diagnosticoList.getTratamiento());
                intent.putExtra("fecha",diagnosticoList.getCreated_at());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
