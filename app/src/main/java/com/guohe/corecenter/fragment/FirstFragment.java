package com.guohe.corecenter.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
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
import com.guohe.corecenter.bean.Moment;
import com.guohe.corecenter.constant.DomainConst;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.view.AvatarCircleView;
import com.guohe.corecenter.view.CachedImageView;
import com.gyf.immersionbar.ImmersionBar;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mTwinklingRefreshLayout;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<Moment> mMomentList;
    private MomentAdapter mMomentAdapter;

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
    protected void init() {
        mMomentList = new ArrayList<>();
        Moment moment = new Moment();
        moment.setImageUrl(UrlConst.PICTURE_DOMAIN + "/ImageCache/23c19b652c27451eb1f8e525920180bf.jpg");
        moment.setAccountNickName("超能拳拳酱");
        moment.setContent("#晒娃大赛#");
        moment.setFavorite("11");
        mMomentList.add(moment);
        moment = new Moment();
        moment.setImageUrl(UrlConst.PICTURE_DOMAIN + "/ImageCache/9680299b049d466dbd0e330fe9987fcd.jpg");
        moment.setAccountNickName("我家嘟嘟");
        moment.setContent("昨天给娃买了个机器人天天抱着不撒手#科...");
        moment.setFavorite("121");
        mMomentList.add(moment);
        moment = new Moment();
        moment.setImageUrl(UrlConst.PICTURE_DOMAIN + "/ImageCache/23c19b652c27451eb1f8e525920180bf.jpg");
        moment.setAccountNickName("超能拳拳酱");
        moment.setContent("#科技宝宝#");
        moment.setFavorite("28");
        mMomentList.add(moment);
        moment = new Moment();
        moment.setImageUrl(UrlConst.PICTURE_DOMAIN + "/ImageCache/9680299b049d466dbd0e330fe9987fcd.jpg");
        moment.setAccountNickName("我家嘟嘟");
        moment.setContent("柴犬和小孩摘桑葚吃美了#晒娃大赛#");
        moment.setFavorite("66");
        mMomentList.add(moment);
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
        mRecyclerView.setAdapter(mMomentAdapter = new MomentAdapter(getContext()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));
        mMomentAdapter.setData(mMomentList);

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingDown(refreshLayout, fraction);
            }

            @Override
            public void onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingUp(refreshLayout, fraction);
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullDownReleasing(refreshLayout, fraction);
            }

            @Override
            public void onPullUpReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullUpReleasing(refreshLayout, fraction);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> refreshLayout.finishRefreshing(),1000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> refreshLayout.finishLoadmore(),1000);
            }

            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
            }

            @Override
            public void onFinishLoadMore() {
                super.onFinishLoadMore();
            }

            @Override
            public void onRefreshCanceled() {
                super.onRefreshCanceled();
            }

            @Override
            public void onLoadmoreCanceled() {
                super.onLoadmoreCanceled();
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
            holder.contentTV.setText(moment.getContent());
            holder.posterIv.setImageUrl(moment.getImageUrl());
            holder.headImageIV.setImageUrl(moment.getAccountImageUrl());
            holder.nickNameTV.setText(moment.getAccountNickName());
            holder.countTV.setText(moment.getFavorite());
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
