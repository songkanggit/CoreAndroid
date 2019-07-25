package com.guohe.corecenter.fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;

import com.guohe.corecenter.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentViewClickListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.ll_choose_picture)
    LinearLayout mChoosePictureLL;
    @BindView(R.id.ll_control_camera)
    LinearLayout mControlCameraLL;
    @BindView(R.id.ll_local_gallery)
    LinearLayout mLocalGalleryLL;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentViewClickListener mListener;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SecondFragment.
     */
    public static SecondFragment newInstance() {
        SecondFragment fragment = new SecondFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_second;
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @OnClick({R.id.ll_local_gallery, R.id.ll_control_camera, R.id.ll_choose_picture})
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
