package xyz.wbsite.wbsiteui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class LoginFragment extends BaseSPAFragment {

    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.llyUsername)
    LinearLayout llyUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.llyPassword)
    LinearLayout llyPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.chkPwd)
    CheckBox chkPwd;
    @BindView(R.id.chkAuto)
    CheckBox chkAuto;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeLoading();
                        startFragment(new MainFragment());
                    }
                }, 500);
            }
        });

    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
