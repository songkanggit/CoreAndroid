package com.guohe.corecenter.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.guohe.corecenter.R;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.view.AvatarCircleView;
import com.guohe.corecenter.view.CachedImageView;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link BaseFragment} subclass.
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
    @BindView(R.id.iv_head_image)
    AvatarCircleView avatarCircleView;
    @BindView(R.id.ll_favorite)
    LinearLayout mFavoriteLL;
    @BindView(R.id.ll_follower)
    LinearLayout mFollowerLL;
    @BindView(R.id.ll_sunshine)
    LinearLayout mSunshineLL;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private OnFragmentViewClickListener mListener;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;

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
        avatarCircleView.setImageUrl("http://img.guostory.com//ImageCache/16a341b3713c4c32ac2f39b523674099.png");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mTimeLineAdapter = new TimeLineAdapter(getActivity()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));
        List<String> dataList = new ArrayList<>();
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_1.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_2.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_3.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_4.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_5.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_6.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_7.jpg");
        dataList.add(UrlConst.PICTURE_DOMAIN + "demo_8.jpg");
        mTimeLineAdapter.setData(dataList);
    }

    @Override
    protected void setUpData() {

    }

    @OnClick({R.id.iv_setting, R.id.iv_head_image, R.id.ll_sunshine, R.id.ll_follower, R.id.ll_favorite})
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

    private static class TimeLineAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Context mContext;
        private List<String> mDataList;
        public TimeLineAdapter(Context context) {
            mContext = context;
            mDataList = new ArrayList<>();
        }

        private void setData(List<String> dataList) {
            mDataList.clear();
            for(String data:dataList) {
                mDataList.add(data + "?roundPic/radius/30");
            }
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_time_line, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.imageView.setImageUrl(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private CachedImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int s) {
            space = s;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view) % 2 == 0) {
                outRect.left = space;
                outRect.right = space/2;
            } else {
                outRect.left = space/2;
                outRect.right = space;
            }
            outRect.bottom = space;
        }
    }
}
