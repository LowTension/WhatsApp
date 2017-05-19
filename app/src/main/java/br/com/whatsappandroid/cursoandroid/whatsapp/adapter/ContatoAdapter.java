package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(Context c, ArrayList<Contato> objects) {

        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    // MONTAGEM DOS ITENS DA LISTA
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // VERIFICAR SE A LISTA ESTÁ PREENCHIDA
        if(contatos != null){

            // INICIALIZAR OBJETO DE MONTAGEM DO LAYOUT
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.
                    LAYOUT_INFLATER_SERVICE);

            // MONTAR VIEW A PARTIR DO XML
            view = inflater.inflate(R.layout.lista_contato, parent, false );

            // RECUPERAR O ELEMENTO PARA EXIBIÇÃO
            TextView nomeContato  = (TextView) view.findViewById(R.id.tv_nome);
            TextView emailContato  = (TextView) view.findViewById(R.id.tv_email);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());
        }

        return view;
    }
}
