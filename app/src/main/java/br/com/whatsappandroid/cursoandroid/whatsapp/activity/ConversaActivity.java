package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.MensagemAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    // DADOS DO DESTINATÁRIO
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    // DADOS DO REMETENTE
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_conversas);

        // DADOS DO USUÁRIO LOGADO
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();

        // TRANSFERINDO DADOS ENTRE AS ACTIVITYS
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }

        // CONFIGURAR TOOLBAR
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // MONTAR LISTVIEW E ADAPTER
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(adapter);

        // RECUPERAR MENSAGENS DO FIREBASE
        firebase = ConfiguracaoFirebase.getFirebase()
                    .child("mensagens")
                    .child(idUsuarioRemetente)
                    .child(idUsuarioDestinatario);

        // CRIAR LISTENER PARA AS MENSAGENS DO FIREBASE
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // LIMPAR MENSAGENS
                mensagens.clear();

                // RECUPERA AS MENSAGENS
                for(DataSnapshot dados: dataSnapshot.getChildren()){

                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);


        // EVENTO DE CLICK DA MENSAGEM
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoMensagem  = editMensagem.getText().toString();

                if(textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem!",
                            Toast.LENGTH_LONG).show();
                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    // SALVANDO A CONVERSA PARA O REMETENTE
                    Boolean retornoMensagemRemetente = salvarMensagem(idUsuarioRemetente,
                            idUsuarioDestinatario, mensagem );
                    
                    if( !retornoMensagemRemetente){

                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar a mensagem! Tente novamente",
                                Toast.LENGTH_LONG)
                                .show();
                    } else {

                        // SALVANDO A MENSAGEM PARA O DESTINATÁRIO
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario,
                                idUsuarioRemetente, mensagem );

                        if(!retornoMensagemDestinatario){

                            Toast.makeText(
                                    ConversaActivity.this,
                                    "Problema ao enviar a mensagem para o Destinatário! " +
                                            "Tente novamente",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    }

                    // SALVANDO A CONVERSA PARA O REMETENTE
                    Conversa conversa = new Conversa();

                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);

                    Boolean retornoConversaRemetente = salvarConversa(idUsuarioRemetente,
                            idUsuarioDestinatario, conversa);

                    if(!retornoConversaRemetente){
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar a conversa! Tente novamente",
                                Toast.LENGTH_LONG)
                                .show();
                    } else {

                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(textoMensagem);

                        // SALVANDO A CONVERSA PARA O DESTINATARIO
                        Boolean retornoConversaDestinatario = salvarConversa(idUsuarioDestinatario,
                                idUsuarioRemetente, conversa);

                        if(!retornoConversaDestinatario){
                            Toast.makeText(
                                    ConversaActivity.this,
                                    "Problema ao salvar a conversa para o destinatário! " +
                                            "Tente novamente",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    }


                    editMensagem.setText("");

                    /*
                    EXEMPLO:

                    MENSAGENS
                        + usuario1@gmail.com (usuário logado que fez o envio da mensagem)
                            + usuario2@gmail.com (contato do usuário logado que recebeu a mensagem)
                                + 01 (identificador)
                                    + Mensagem
                                + 02
                                    + Mensagem
                                etc
                                    etc
                            + usuário3@gmail.com (contato do usuário logado que recebeu a mensagem)
                                + 01 (identificador)
                                    + Mensagem
                                + 02
                                    + Mensagem
                                etc
                                    etc
                     */

                }
            }
        });
    }

    // SALVAR MENSAGEM DO CHAT NO FIREBASE
    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try{

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push() // USADO PARA GERAR IDENTIFICADORES ÚNICOS E ALEATÓRIOS
                    .setValue(mensagem);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){

        try{

            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");

            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);

            return  true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
