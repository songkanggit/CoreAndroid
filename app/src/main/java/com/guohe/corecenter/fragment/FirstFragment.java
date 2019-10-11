package com.guohe.corecenter.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.guohe.corecenter.R;
import com.guohe.corecenter.activity.MomentDetailActivity;
import com.guohe.corecenter.bean.Moment;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.utils.JacksonUtil;
import com.guohe.corecenter.view.AvatarCircleView;
import com.guohe.corecenter.view.CachedImageView;
import com.gyf.immersionbar.ImmersionBar;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends BaseFragment implements FragmentActivityInterface {
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
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mTwinklingRefreshLayout;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MomentAdapter mMomentAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteraction mListener;

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
    protected void init() {
    }

    @Override
    protected void setUpView() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).statusBarDarkFont(true).fitsSystemWindows(true).init();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMomentAdapter = new MomentAdapter(getActivity()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mListener.onRefresh(refreshLayout);
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @OnClick({R.id.iv_scan, R.id.iv_group})
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }

    @OnClick({R.id.tv_hot, R.id.tv_recommend, R.id.tv_follow})
    public void onBottomBarPressed(View view) {
        mTwinklingRefreshLayout.startRefresh();
        toggleTopBar(view.getId());
        onButtonPressed(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            mListener = (OnFragmentInteraction) context;
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

    @Override
    public void onDataUpdate(List<Object> dataList) {
        List<Moment> momentList = new ArrayList<>();
        try {
            if(!dataList.isEmpty()) {
                for(Object data:dataList) {
                    Moment moment = JacksonUtil.convertValue(data, Moment.class);
                    momentList.add(moment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(() -> {
            mMomentAdapter.setData(momentList);
            mTwinklingRefreshLayout.finishRefreshing();
        });
    }

    @Override
    public void onDataUpdate(Object data) {

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

    private static class MomentAdapter extends RecyclerView.Adapter<MomentViewHolder> {
        private List<Moment> mMomentList;
        private Context mContext;

        public MomentAdapter(Context context) {
            mContext = context;
            mMomentList = new ArrayList<>();
        }

        public void setData(List<Moment> dataList) {
            mMomentList.clear();
            mMomentList.addAll(dataList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_moment, parent, false);
            return new MomentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
            Moment moment = mMomentList.get(position);
            String appender = "?roundPic/radius/50";
            holder.contentTV.setText(moment.getContent());
            holder.posterIv.setImageUrl(UrlConst.PICTURE_DOMAIN + moment.getImageList()[0] + appender);
            holder.headImageIV.setImageUrl(UrlConst.PICTURE_DOMAIN + moment.getAccountHeadImage());
            holder.nickNameTV.setText(moment.getAccountName());
            holder.countTV.setText(moment.getLikes());
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, MomentDetailActivity.class);
                mContext.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mMomentList.size();
        }
    }

    private static class MomentViewHolder extends RecyclerView.ViewHolder {
        CachedImageView posterIv;
        AvatarCircleView headImageIV;
        TextView nickNameTV, countTV, contentTV;
        public MomentViewHolder(@NonNull View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.iv_image);
            contentTV = itemView.findViewById(R.id.tv_content);
            headImageIV = itemView.findViewById(R.id.iv_head_image);
            nickNameTV = itemView.findViewById(R.id.tv_nick_name);
            countTV = itemView.findViewById(R.id.tv_count);
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
