package com.enfin.ofabee3.ui.module.home;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;

public class HomeMenuListAdapter extends RecyclerView.Adapter<HomeMenuListAdapter.MenuViewHolder> {

    private ArrayList<String> menuList;
    private Context mContext;
    private RecyclerViewItemClickListener itemClickListener;
    private long mLastClickTime = 0;
    public boolean canStart = true;

    public HomeMenuListAdapter(Context context, ArrayList<String> menuList) {
        this.mContext = context;
        this.menuList = menuList;
    }

    public void setItemClickListener(RecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.menu_item, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.title.setText(menuList.get(position));
        holder.title.setOnClickListener(view -> {
            mLastClickTime = SystemClock.elapsedRealtime();
            if (canStart) {
                canStart = false;
                itemClickListener.onItemSelected(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setCanStart(boolean can){
        canStart = can;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private OpenSansTextView title;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.menu_title_tv);
        }
    }

}
