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
import sv.edu.udb.petyaapp.models.Empleados;

public class EmpleadosAdapter extends ArrayAdapter<Empleados> {
    private ArrayList<Empleados> empleadosList;

    public EmpleadosAdapter(@NonNull Context context, @NonNull ArrayList<Empleados> empleadosList) {
        super(context, 0, empleadosList);
        empleadosList = new ArrayList<>(empleadosList);
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

        Empleados empleados = getItem(position);

        if(empleados != null){
            textView.setText(empleados.getNombres() +  " " + empleados.getApellidos());
        }

        return convertView;
    }


    @NonNull
    @Override
    public Filter getFilter(){
        return empleadoFiltro;
    }

    private Filter empleadoFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Empleados> emp = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                emp.addAll(empleadosList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Empleados empleados: empleadosList){
                    if(empleados.getNombres().toLowerCase().contains(filterPattern)){
                        emp.add(empleados);
                    }
                }
            }

            results.values = emp;
            results.count = emp.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Empleados> empleadosFiltrados = (ArrayList<Empleados>) results.values;
            if(results != null && results.count > 0){
                clear();
                for (Empleados e: empleadosFiltrados){
                    add(e);
                }
                notifyDataSetChanged();
            }else{

                notifyDataSetInvalidated();
            }


        }

        @Override
        public CharSequence convertResultToString(Object resultValue){
            return ((Empleados) resultValue).getNombres() + " " + ((Empleados) resultValue).getApellidos();
        }

    };


}
