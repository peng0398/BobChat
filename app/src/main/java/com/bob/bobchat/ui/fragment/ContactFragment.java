package com.bob.bobchat.ui.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;
import com.bob.bobchat.ui.ChatActivity;
import com.bob.bobchat.utils.ChatHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class ContactFragment extends BaseFragment {

    private static final String TAG = "ContactFragment";
    @Bind(R.id.rv_container)
    RecyclerView rv_container;
    private ContactAdapter contactAdapter;

    @Inject
    ChatHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application application = getActivity().getApplication();
        ((BobApplication) application).getBuild().inject(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView() {
        contactAdapter = new ContactAdapter();

        rv_container.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_container.setAdapter(contactAdapter);
    }

    @Override
    protected void initData() {

        helper.getContacts().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "----onCompleted-----");
            }

            @Override
            public void onError(Throwable e) {

                Log.i(TAG, "----onError-----");
            }

            @Override
            public void onNext(List<String> strings) {
//                Toast.makeText(BobApplication.getAppContext(), "获取好友列表成功", Toast.LENGTH_SHORT).show();
                if (strings.size() > 0) {
                    contactAdapter.setUsers(strings);
                    contactAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

        List<String> users = new ArrayList<String>();

        public void setUsers(List<String> users) {
            this.users = users;
        }


        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ContactHolder(
                    View.inflate(BobApplication.getAppContext(), R.layout.user_item, null));
        }

        @Override
        public void onBindViewHolder(final ContactHolder holder, int position) {
            holder.tv_username.setText(users.get(position));
            holder.ll_user_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("user_info", users.get(holder.getAdapterPosition()));
                    getActivity().startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        TextView tv_username;
        LinearLayout ll_user_item;

        public ContactHolder(View itemView) {
            super(itemView);
            tv_username = ((TextView) itemView.findViewById(R.id.tv_username));
            ll_user_item = ((LinearLayout) itemView.findViewById(R.id.ll_user_item));
        }
    }
}
