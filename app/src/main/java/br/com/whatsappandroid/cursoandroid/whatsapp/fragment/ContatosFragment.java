package br.com.whatsappandroid.cursoandroid.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.activity.ConversaActivity;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.ContatoAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;


public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    // SOMENTE ACIONAR O 'LISTENER' QUANDO O FRAGMENT FOR INICIALIZADO
    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener", "onStart CONTATOS");
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener", "onStop CONTATOS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        // INSTANCIAR OS CONTATOS
        contatos = new ArrayList<>();

        // MONTAGEM DO LISTVIEW E ADAPTER
        listView = (ListView) view.findViewById(R.id.lv_contatos);
        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);

        // RECUPERAR DADOS DO USUÁRIO
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();


        // RECUPERAR CONTATOS DO FIREBASE
        firebase = ConfiguracaoFirebase.getFirebase()
                   .child("contatos")
                   .child(identificadorUsuarioLogado);


        // LISTENER PARA RECUPERAR CONTATOS DO FIREBASE
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // LIMPAR LISTA
                contatos.clear();

                // LISTAR CONTATOS
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // EVENTO DE CLICK EM CADA ITEM DA LISTA
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                // RECUPERAR DADOS A SEREM PASSADOS
                Contato contato = contatos.get(i);

                // ENVIANDO DADOS PARA 'CONVERSA ACTIVITY'
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail() );

                startActivity(intent);
            }
        });

        return view;
    }

}
