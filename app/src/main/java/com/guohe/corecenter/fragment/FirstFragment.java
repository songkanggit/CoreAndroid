package com.guohe.corecenter.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guohe.corecenter.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentViewClickListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_scan)
    ImageView mScanIV;
    @BindView(R.id.iv_group)
    ImageView mGroupIV;
    @BindView(R.id.iv_follow)
    ImageView mFollowIV;
    @BindView(R.id.iv_recommend)
    ImageView mRecommendIV;
    @BindView(R.id.iv_hot)
    ImageView mHotIV;
    @BindView(R.id.tv_follow)
    TextView mFollowTV;
    @BindView(R.id.tv_recommend)
    TextView mRecommendTV;
    @BindView(R.id.tv_hot)
    TextView mHotTV;

    private View mRootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentViewClickListener mListener;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FirstFragment.
     */
    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_first;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @OnClick({R.id.iv_scan, R.id.iv_group})
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }

    @OnClick({R.id.tv_hot, R.id.tv_recommend, R.id.tv_follow})
    public void onBottomBarPressed(View view) {
        toggleTopBar(view.getId());
        onButtonPressed(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentViewClickListener) {
            mListener = (OnFragmentViewClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void toggleTopBar(final int viewId) {
        mFollowIV.setVisibility(View.GONE);
        mFollowTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mFollowTV.setTextColor(Color.parseColor("#B0B0DA"));
        mRecommendIV.setVisibility(View.GONE);
        mRecommendTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mRecommendTV.setTextColor(Color.parseColor("#B0B0DA"));
        mHotIV.setVisibility(View.GONE);
        mHotTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mHotTV.setTextColor(Color.parseColor("#B0B0DA"));
        switch (viewId) {
            case R.id.tv_follow: {
                mFollowIV.setVisibility(View.VISIBLE);
                mFollowTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mFollowTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
            case R.id.tv_recommend: {
                mRecommendIV.setVisibility(View.VISIBLE);
                mRecommendTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mRecommendTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
            case R.id.tv_hot: {
                mHotIV.setVisibility(View.VISIBLE);
                mHotTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mHotTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
        }
    }
}
