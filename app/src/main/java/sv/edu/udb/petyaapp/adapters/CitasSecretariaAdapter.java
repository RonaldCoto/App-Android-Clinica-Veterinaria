package sv.edu.udb.petyaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;

import sv.edu.udb.petyaapp.R;
import sv.edu.udb.petyaapp.models.CitasSecretaria;

public class CitasSecretariaAdapter extends ArrayAdapter<CitasSecretaria> {
    ArrayList<CitasSecretaria>citaList;
    Context context;
    int resource;
    public CitasSecretariaAdapter(Context context, int resource, ArrayList<CitasSecretaria> citaList) {
        super(context, resource, citaList);

        this.citaList =citaList;
        this.context=context;
        this.resource=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.listcitas ,null,true);
        }
        CitasSecretaria citaList=getItem(position);

        TextView textView=(TextView)convertView.findViewById(R.id.nommascota);
        TextView textView1=(TextView)convertView.findViewById(R.id.fechadecita);
        TextView textView2=(TextView)convertView.findViewById(R.id.horadecita);
        TextView textView3=(TextView)convertView.findViewById(R.id.estadodecita);
        TextView textView4=(TextView)convertView.findViewById(R.id.encargadomascota);

        String concat="";
        String concat1="";
        String concat3="";
        String concat4="";
        String concat5="";
        concat= "Mascota: " + citaList.getNombre_mascota();
        concat1= "Fecha: " + citaList.getFecha_cita();
        concat3= "Hora: " + citaList.getHora();
        concat4= "Estado: " + citaList.getEstado();
        concat5= "Encargado: " + citaList.getId_cliente();

        textView.setText(concat);
        textView1.setText(concat1);
        textView2.setText(concat3);
        textView3.setText(concat4);
        textView4.setText(concat5);
        return convertView;
    }



}
