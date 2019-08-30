package com.guohe.corecenter.fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.guohe.corecenter.R;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentViewClickListener} interface
 * to handle interaction events.
 * Use the {@link ForthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForthFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.iv_setting)
    ImageView mSettingIV;

    private OnFragmentViewClickListener mListener;

    public ForthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForthFragment newInstance() {
        ForthFragment fragment = new ForthFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_forth;
    }

    @Override
    protected void setUpView() {
        ImmersionBar.with(this).statusBarColor(R.color.colorTransparent).statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @Override
    protected void setUpData() {

    }

    @OnClick(R.id.iv_setting)
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
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
}
