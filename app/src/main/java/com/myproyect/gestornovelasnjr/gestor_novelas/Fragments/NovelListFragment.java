package com.myproyect.gestornovelasnjr.gestor_novelas.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelAdapter;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelViewModel;
import java.util.List;

public class NovelListFragment extends Fragment {

    private RecyclerView recyclerView;
    private NovelAdapter novelAdapter;
    private NovelViewModel novelViewModel;
    private OnNovelSelectedListener callback;

    public interface OnNovelSelectedListener {
        void onNovelSelected(Novel novel);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnNovelSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNovelSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("NovelListFragment", "onCreateView called");

        View view = inflater.inflate(R.layout.fragment_novel_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        novelAdapter = new NovelAdapter(getContext(), novel -> {
            novelViewModel.delete(novel);
        }, novel -> {
            callback.onNovelSelected(novel);
        });
        recyclerView.setAdapter(novelAdapter);

        novelViewModel = new ViewModelProvider(requireActivity()).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(getViewLifecycleOwner(), new Observer<List<Novel>>() {
            @Override
            public void onChanged(List<Novel> novels) {
                novelAdapter.setNovels(novels);
            }
        });

        return view;
    }
}
