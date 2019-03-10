package com.vladislavmyasnikov.courseproject.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vladislavmyasnikov.courseproject.R;
import com.vladislavmyasnikov.courseproject.models.User;
import com.vladislavmyasnikov.courseproject.ui.components.InitialsRoundView;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.GridUserViewHolder> {

    private List<User> mUsers;
    private int mViewType;

    public static final int LINEAR_USER_VIEW = 1;
    public static final int GRID_USER_VIEW = 2;

    public UserAdapter(List<User> users) {
        mUsers = users;
    }

    public void setList(List<User> users) {
        mUsers = users;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    @NonNull
    @Override
    public GridUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LINEAR_USER_VIEW) {
            return new LinearUserViewHolder(createView(parent, viewType));
        }
        else {
            return new GridUserViewHolder(createView(parent, viewType));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GridUserViewHolder holder, int position) {
        holder.bind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    private View createView(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutItemId;
        switch (viewType) {
            case LINEAR_USER_VIEW:
                layoutItemId = R.layout.linear_user_item;
                break;
            case GRID_USER_VIEW:
                layoutItemId = R.layout.grid_user_item;
                break;
            default:
                return null;
        }
        return inflater.inflate(layoutItemId, parent, false);
    }

    private int getRandomColor() {
        int[] values = new int[] {0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
        int color = 0x00000000;
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            color <<= 4;
            color |= values[random.nextInt(16)];
        }
        return color | 0xFF000000;
    }

    private int getColor(String displayName) {
        int[] values = new int[] {0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
        int color = 0x00000000;
        if (!displayName.equals("")) {
            int hash = Math.abs(displayName.hashCode());
            for (int i = 1; i <= 6; i++) {
                color <<= 4;
                color |= values[hash / (11 * i) % 16];
            }
        }
        return color | 0xFF000000;
    }

    class GridUserViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserNameView;
        private InitialsRoundView mUserIconView;

        GridUserViewHolder(View view) {
            super(view);
            mUserIconView = view.findViewById(R.id.user_icon);
            mUserNameView = view.findViewById(R.id.user_name_field);
        }

        void bind(User user) {
            String initials = "";
            if (!user.getName().equals("")) {
                initials += user.getName().substring(0, 1);
            }
            if (!user.getSurname().equals("")) {
                initials += user.getSurname().substring(0, 1);
            }
            String displayName = user.getName() + " " + user.getSurname();
            mUserNameView.setText(displayName);
            mUserIconView.setText(initials);
            mUserIconView.setIconColor(getColor(displayName));
        }
    }

    class LinearUserViewHolder extends GridUserViewHolder {
        private TextView mUserPointsView;

        LinearUserViewHolder(View view) {
            super(view);
            mUserPointsView = view.findViewById(R.id.user_points_field);
        }

        void bind(User user) {
            super.bind(user);
            mUserPointsView.setText(Integer.toString(user.getPoints()) + "балл");
        }
    }
}