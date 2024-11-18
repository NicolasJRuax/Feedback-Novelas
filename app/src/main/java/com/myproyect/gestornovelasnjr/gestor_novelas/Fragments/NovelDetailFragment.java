package com.myproyect.gestornovelasnjr.gestor_novelas.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;

public class NovelDetailFragment extends Fragment {

    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewYear;
    private TextView textViewSynopsis;

    public static NovelDetailFragment newInstance(Novel novel) {
        NovelDetailFragment fragment = new NovelDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("novel", novel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("NovelDetailFragment", "onCreateView called");

        View view = inflater.inflate(R.layout.fragment_novel_detail, container, false);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewAuthor = view.findViewById(R.id.textViewAuthor);
        textViewYear = view.findViewById(R.id.textViewYear);
        textViewSynopsis = view.findViewById(R.id.textViewSynopsis);

        if (getArguments() != null) {
            Novel novel = getArguments().getParcelable("novel");
            if (novel != null) {
                textViewTitle.setText(novel.getTitle());
                textViewAuthor.setText(novel.getAuthor());
                textViewYear.setText(String.valueOf(novel.getYear()));
                textViewSynopsis.setText(novel.getSynopsis());
            }
        }

        return view;
    }
}
