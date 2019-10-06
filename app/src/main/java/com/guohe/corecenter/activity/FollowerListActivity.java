package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.bean.Account;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.view.AvatarCircleView;

import java.util.ArrayList;
import java.util.List;

public class FollowerListActivity extends BaseActivity implements View.OnClickListener {
    public static final String FOLLOWER_TYPE = "follower";
    public static final String FAVORITE_TYPE = "favorite";

    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private RecyclerView mRecyclerView;
    private List<Account> mAccountList;
    private FollowerListAdapter mListAdapter;
    private String mType;

    protected void parseNonNullBundle(Bundle bundle){
        mType = bundle.getString("type");
    }
    protected void initDataIgnoreUi() {
        mAccountList = new ArrayList<>();
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_follower_list;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mRecyclerView = fvb(R.id.recycler_view);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        if(mType.equals(FOLLOWER_TYPE)) {
            mTitleTV.setText("我的粉丝");
        } else {
            mTitleTV.setText("我的关注");
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mListAdapter = new FollowerListAdapter(FollowerListActivity.this));
        Account account = new Account();
        account.setHeadImage("/ImageCache/3b4f030606694cefbe967cacd71040bc.jpg");
        account.setNickName("小阿飞");
        mAccountList.add(account);
        account = new Account();
        account.setHeadImage("/ImageCache/06f644c782b74422b47b6dc43132d613.jpg");
        account.setNickName("甜心");
        mAccountList.add(account);
        account = new Account();
        account.setHeadImage("/ImageCache/09ffcdf0a236449da10fa990516623eb.jpg");
        account.setNickName("烈火🔥");
        mAccountList.add(account);
        account = new Account();
        account.setHeadImage("/ImageCache/09a85cbec9de402cb97dd850e99d83ae.png");
        account.setNickName("佳美");
        mAccountList.add(account);
        account = new Account();
        account.setHeadImage("/ImageCache/114db1ab034842bba9164af3ae8e79e5.png");
        account.setNickName("小琪琪");
        mAccountList.add(account);
        account = new Account();
        account.setHeadImage("/ImageCache/17aca4a591574c69b1013fb399a843f3.jpg");
        account.setNickName("礼物🎁");
        mAccountList.add(account);
        mListAdapter.setData(mAccountList);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    private static class FollowerListAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Context mContext;
        private List<Account> mDataList;


        public FollowerListAdapter(Context context) {
            mContext = context;
            mDataList = new ArrayList<>();
        }

        public void setData(List<Account> accountList) {
            mDataList.clear();
            mDataList.addAll(accountList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_follower, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Account account = mDataList.get(position);
            holder.nameTV.setText(account.getNickName());
            holder.headImage.setImageUrl(UrlConst.PICTURE_DOMAIN + account.getHeadImage());
            holder.followTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private AvatarCircleView headImage;
        private TextView nameTV, followTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.iv_avatar);
            nameTV = itemView.findViewById(R.id.tv_name);
            followTV = itemView.findViewById(R.id.tv_follow);
        }
    }
}
