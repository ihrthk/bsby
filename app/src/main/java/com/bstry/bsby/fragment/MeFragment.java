package com.bstry.bsby.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bstry.bsby.R;
import com.bstry.bsby.activity.AboutActivity;

/**
 * Created by zhangls on 2/7/15.
 */
public class MeFragment extends BaseFragment {

    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        RelativeLayout rlAdvise = (RelativeLayout) view.findViewById(R.id.rl_advise);
        rlAdvise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FeedbackAgent agent = new FeedbackAgent(getActivity());
//                agent.startFeedbackActivity();
            }
        });
        RelativeLayout rlAboutBs = (RelativeLayout) view.findViewById(R.id.rl_about_bs);
        rlAboutBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }
}
