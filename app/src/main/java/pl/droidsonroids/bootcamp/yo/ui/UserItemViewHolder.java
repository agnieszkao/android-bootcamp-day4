package pl.droidsonroids.bootcamp.yo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.droidsonroids.bootcamp.yo.R;
import pl.droidsonroids.bootcamp.yo.model.User;

public class UserItemViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView textView;
    public UserItemViewHolder(View itemView) {
        super(itemView);
        nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        textView = (TextView) itemView.findViewById(R.id.textView);
    }

//    public void bindData(User user) {
//
//        ((TextView) itemView).setText(user.getName());
//    }
}
