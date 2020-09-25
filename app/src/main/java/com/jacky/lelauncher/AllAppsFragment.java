package com.jacky.lelauncher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAppsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAppsFragment extends Fragment {

    private View containerView;

    private RecyclerView allAppsContainer;

    public AllAppsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllAppsFragment newInstance(String param1, String param2) {
        AllAppsFragment fragment = new AllAppsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_allapps, container, false);
//        allAppsContainer = containerView.findViewById(R.id.all_apps);
//        List<String> allApps = new ArrayList<>();
//        allApps.add("QQ Music");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//        allApps.add("高德地图");
//
//        AllAppsAdapter adapter = new AllAppsAdapter(allApps);
//        allAppsContainer.setAdapter(adapter);
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
//        allAppsContainer.setLayoutManager(layoutManager);

        return containerView;
    }

    private class AllAppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

        private List<String> mAllApps;

        public AllAppsAdapter(List<String> allApps) {
            mAllApps = allApps;
        }

        @NonNull
        @Override
        public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AppViewHolder(
                    LayoutInflater.from(getContext()).inflate(R.layout.layout_app, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
            holder.mTextView.setText(mAllApps.get(position));
        }

        @Override
        public int getItemCount() {
            return mAllApps.size();
        }
    }

    private class AppViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
//            mTextView = itemView.findViewById(R.id.textView);
        }
    }

}