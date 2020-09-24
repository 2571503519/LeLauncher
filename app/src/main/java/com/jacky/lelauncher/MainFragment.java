package com.jacky.lelauncher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private View containerView;

    private Button btn1;
    private Button btn2;
    private ArcSeekBar arcSeekBar;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerView = inflater.inflate(R.layout.fragment_main, container, false);
        btn1 = containerView.findViewById(R.id.btn1);
        btn2 = containerView.findViewById(R.id.btn2);
        arcSeekBar = containerView.findViewById(R.id.arcSeekBar);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        arcSeekBar.setShowTick(true);
        return containerView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                arcSeekBar.showAnimation(arcSeekBar.getProgress() == 100 ? 0 :arcSeekBar.getProgress(),100,3000);
                break;
            case R.id.btn2:
                arcSeekBar.showAnimation(arcSeekBar.getProgress() == 0 ? 0 :arcSeekBar.getProgress(),0,3000);
                break;
        }
    }
}