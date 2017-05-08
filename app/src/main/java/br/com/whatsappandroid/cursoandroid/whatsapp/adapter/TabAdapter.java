package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.whatsappandroid.cursoandroid.whatsapp.fragment.ContatosFragment;
import br.com.whatsappandroid.cursoandroid.whatsapp.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    // RETORNA OS FRAGMENTOS
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    // RETORNA A QUANTIDADE DE ABAS
    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    // RECUPERA OS TÍTULOS DE CADA ABA
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
