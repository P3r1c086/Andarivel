package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class AdaptadorAuseciasTabs extends FragmentStateAdapter {

    private ArrayList<Fragment> fragments ;

    public AdaptadorAuseciasTabs(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }


    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void setData ( ArrayList<Fragment> fragments ) {
        this . fragments = fragments ; }
}
