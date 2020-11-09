package com.project2.bodycheck.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project2.bodycheck.R;

public class ImageFragment extends Fragment {

    public static ImageFragment newInstance(int imageUrl) {
        Bundle args = new Bundle();
        args.putInt("imageUrl", imageUrl);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_container);
        imageView.setImageResource(getArguments().getInt("imageUrl"));

        return view;
    }
}
