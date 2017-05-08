package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.activity.ConversaActivity;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }


    // MONTAGEM DOS ITENS DA LISTA
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // VERIFICAR SE A LISTA ESTÁ PREENCHIDA
        if(mensagens != null){

            // RECUPERAR OS DADOS DO USUÁRIO REMETENTE
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            // INICIALIZAR OBJETO DE MONTAGEM DO LAYOUT
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.
                    LAYOUT_INFLATER_SERVICE);

            // RECUPERAR MENSAGEM
            Mensagem mensagem = mensagens.get(position);

            // MONTAR VIEW A PARTIR DO XML
            if(idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }


            // RECUPERAR O ELEMENTO PARA EXIBIÇÃO
            TextView textoMensagem  = (TextView) view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());


        }

        return view;
    }
}
