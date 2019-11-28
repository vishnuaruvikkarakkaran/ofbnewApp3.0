package com.enfin.ofabee3.ui.module.userprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.enfin.ofabee3.utils.OpenSansTextViewBold;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;

import hk.ids.gws.android.sclick.SClick;

public class UserSettingsListAdapter extends RecyclerView.Adapter<UserSettingsListAdapter.MenuViewHolder> {

    private ArrayList<String> settingsItemList;
    private Context mContext;
    private RecyclerViewItemClickListener itemClickListener;
    private int TYPE_TEXT = 1, TYPE_TEXT_ICON = 2, TYPE_BUTTON = 3;
    public boolean canStart = true;

    public UserSettingsListAdapter(Context context, ArrayList<String> settingsList) {
        this.mContext = context;
        this.settingsItemList = settingsList;
    }

    public void setItemClickListener(RecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TEXT) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.settings_item, parent, false);
        } else if (viewType == TYPE_BUTTON) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.settings_sign_out_button, parent, false);
        } else if (viewType == TYPE_TEXT_ICON) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.settings_item_with_icon, parent, false);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.settings_item, parent, false);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.title.setText(settingsItemList.get(position));
        holder.title.setOnClickListener(view -> {
            //if (canStart) {
                canStart = false;
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            itemClickListener.onItemSelected(position, true);
            //}
        });
    }

    @Override
    public int getItemCount() {
        return settingsItemList.size();
    }

    public void setCanStart(boolean can){
        canStart = can;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return TYPE_TEXT;
        } else if (position == settingsItemList.size() - 1) {
            return TYPE_BUTTON;
        } else
            return TYPE_TEXT;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private OpenSansTextView title;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.settings_item_title_tv);
        }
    }

}
