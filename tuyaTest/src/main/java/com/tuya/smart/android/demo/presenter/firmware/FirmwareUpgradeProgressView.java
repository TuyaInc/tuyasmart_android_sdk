package com.tuya.smart.android.demo.presenter.firmware;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.widget.circleprogress.CircleProgressView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

/**
 * Created by letian on 16/4/19.
 */
public class FirmwareUpgradeProgressView extends BasePresenter {
    private final Context mContext;
    private TextView mProgressTip;
    private CircleProgressView mCircleProgressView;
    private AlertDialog mAlertDialog;
    private boolean mSpin;

    public FirmwareUpgradeProgressView(Context context) {
        mContext = context;
        initDialog();
    }

    public void initDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_firmupdate_progress, null);
        mAlertDialog = new AlertDialog.Builder(mContext).setView(view).create();
        mProgressTip = (TextView) view.findViewById(R.id.tv_upgrade_progress_tip);
        mCircleProgressView = (CircleProgressView) view.findViewById(R.id.circleView);
        mCircleProgressView.setValue(0);
        mCircleProgressView.spin();
        mSpin = true;
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        mAlertDialog.dismiss();
                        ((Activity) mContext).finish();
                        break;
                }
                return false;
            }
        });
    }

    public void showSuccessView() {
        hideDialog();
        ToastUtil.shortToast(mContext, mContext.getString(R.string.firmware_upgrade_success));
    }

    public void showFailureView() {
        hideDialog();
        final AlertDialog alertDialog = DialogUtil.customDialog(mContext, mContext.getString(R.string.firmware_upgrade_failure),
                mContext.getString(R.string.firmware_upgrade_failure_description),
                mContext.getString(R.string.cancel), null, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                ((Activity) mContext).finish();
                                break;
                        }
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showProgress(int progress) {
        if (mSpin) {
            mCircleProgressView.stopSpinning();
            mSpin = false;
        }
        mCircleProgressView.setValueAnimated(progress);
    }


    public void showProgressSpin() {
        mCircleProgressView.setValue(0);
        if (!mSpin) return;
        mCircleProgressView.spin();
        mSpin = true;
    }


    public void showTip(String tip) {
        if (!mProgressTip.getText().equals(tip)) {
            mProgressTip.setText(tip);
        }
    }

    private void hideDialog() {
        if (isShow()) {
            mAlertDialog.dismiss();
        }
    }

    public void showDialog() {
        if (!isShow()) {
            mAlertDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpin) {
            mCircleProgressView.stopSpinning();
            mSpin = false;
        }
        mCircleProgressView.clearAnimation();
        hideDialog();
    }

    public boolean isShow() {
        return mAlertDialog.isShowing();
    }

    public void showGWUpgrading() {
        showTip(mContext.getString(R.string.firmware_gw_updating));
    }

    public void showDevUpgrading() {
        showTip(mContext.getString(R.string.firmware_device_updating));
    }

    public void showUpgradingTip(FirmwareUpgradeEnum to) {
        showDialog();
        if (to == FirmwareUpgradeEnum.TY_DEV) {
            showDevUpgrading();
        } else showGWUpgrading();
    }
}
