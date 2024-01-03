package com.cac.trashpic.ui.trash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cac.trashpic.databinding.FragmentCardboardBinding;
import com.cac.trashpic.databinding.FragmentTrashBinding;

public class TrashFragment extends Fragment {

    private FragmentTrashBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrashViewModel trashViewModel =
                new ViewModelProvider(this).get(TrashViewModel.class);

        binding = FragmentTrashBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textCardboard;
//        cardboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}