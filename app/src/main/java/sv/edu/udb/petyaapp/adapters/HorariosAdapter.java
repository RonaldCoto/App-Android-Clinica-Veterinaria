package sv.edu.udb.petyaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import sv.edu.udb.petyaapp.R;
import sv.edu.udb.petyaapp.models.Horarios;

public class HorariosAdapter extends ArrayAdapter<Horarios> {
    private ArrayList<Horarios> horariosArrayList;

    public HorariosAdapter(@NonNull Context context, @NonNull ArrayList<Horarios> horariosArrayList){
        super(context, 0, horariosArrayList);
        horariosArrayList = new ArrayList<>(horariosArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dropdown_item, parent, false
            );
        }

        TextView textView = convertView.findViewById(R.id.textView_nombre);

        Horarios horarios = getItem(position);

        if(horarios != null){
            textView.setText(horarios.getHora_inicio() + " - " + horarios.getHora_fin());
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter(){
        return horarioFiltro;
    }


    private Filter horarioFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Horarios> hor = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                hor.addAll(horariosArrayList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Horarios horario: horariosArrayList){
                    if(horario.getHora_inicio().toLowerCase().contains(filterPattern)){
                        hor.add(horario);
                    }
                }
            }

            results.values = hor;
            results.count = hor.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Horarios> horariosFiltrados = (ArrayList<Horarios>) results.values;
            if(results != null && results.count > 0){
                clear();
                for (Horarios h: horariosFiltrados){
                    add(h);
                }
                notifyDataSetChanged();
            }else{

                notifyDataSetInvalidated();
            }


        }

        @Override
        public CharSequence convertResultToString(Object resultValue){
            return ((Horarios) resultValue).getHora_inicio() + " a " + ((Horarios) resultValue).getHora_fin();
        }

    };

}
