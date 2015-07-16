package pl.droidsonroids.bootcamp.yo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import pl.droidsonroids.bootcamp.yo.R;
import pl.droidsonroids.bootcamp.yo.api.ApiService;
import pl.droidsonroids.bootcamp.yo.model.Constants;
import pl.droidsonroids.bootcamp.yo.model.User;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserListAdapter extends RecyclerView.Adapter<UserItemViewHolder> {

    private List<User> userList = Collections.emptyList();

    @Override
    public UserItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_item, viewGroup, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder userItemViewHolder, final int i) {
        User user = userList.get(i);
        userItemViewHolder.nameTextView.setText(user.getName());
        userItemViewHolder.textView.setText(user.getName().substring(0,1));
        //userItemViewHolder.bindData(user);
        if(user.isColored()) {
            userItemViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            userItemViewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
        userItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                final int selfId = sharedPreferences.getInt(Constants.KEY_ID, 0);
                if (selfId == 0) {
                    Toast.makeText(context, R.string.registration_not_completed, Toast.LENGTH_SHORT).show();
                    return;
                }

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                ApiService.API_SERVICE.postYo(userList.get(i).getId(), selfId).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Toast.makeText(context, R.string.message_sent, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_title)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void refreshUserList(List<User> userList) {
        this.userList = userList;
        Collections.sort(userList);
        notifyDataSetChanged();
    }

}
