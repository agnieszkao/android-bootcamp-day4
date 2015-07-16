package pl.droidsonroids.bootcamp.yo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.droidsonroids.bootcamp.yo.api.ApiService;
import pl.droidsonroids.bootcamp.yo.api.service.RegistrationIntentService;
import pl.droidsonroids.bootcamp.yo.model.Constants;
import pl.droidsonroids.bootcamp.yo.model.User;
import pl.droidsonroids.bootcamp.yo.ui.UserListAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.users_recycler)
    RecyclerView usersRecycler;
    @Bind(R.id.name_edit_text)
    EditText nameEditText;

    final UserListAdapter userListAdapter = new UserListAdapter();
    private static final String TAG = "BootcampGcm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        usersRecycler.setAdapter(userListAdapter);
        setUsername(nameEditText);
        onRefreshButtonClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void setUsername(EditText editText) {
        editText.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_NAME, null));
        editText.setSelection(nameEditText.getText().length());

    }

    @OnClick(R.id.refresh_button)
    public void onRefreshButtonClick() {
        ApiService.API_SERVICE.getUsers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                String username = getIntent().getAction();
                if (username != null) {
                    for(User user : users) {
                        if(user.getName().equals(username)){
                            user.setIsColored(true);
                        }
                    }
                }
                userListAdapter.refreshUserList(users);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            String name = nameEditText.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, R.string.name_missed, Toast.LENGTH_SHORT).show();
            } else {
                intent.setAction(name);
                startService(intent);
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            } else {
                GoogleApiAvailability.getInstance().showErrorDialogFragment(this, resultCode, 0);
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
