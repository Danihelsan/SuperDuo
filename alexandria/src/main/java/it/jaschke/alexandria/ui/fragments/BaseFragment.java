package it.jaschke.alexandria.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Danihelsan
 */
public class BaseFragment extends Fragment {

    protected void loadDetailField(String text, TextView view) {
        if (text!=null){
            view.setText(text);
        } else{
            view.setVisibility(View.INVISIBLE);
        }
    }
}
