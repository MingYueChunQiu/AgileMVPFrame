package com.mingyuechunqiu.agileproject.feature.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mingyuechunqiu.agile.feature.helper.ui.transfer.ITransferPageDataHelper;
import com.mingyuechunqiu.agileproject.R;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/8/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class JumpFragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jump, container, false);
        LinearLayout llContainer = view.findViewById(R.id.ll_jump_container);
        llContainer.setBackgroundColor(Color.BLUE);
        Button btnJump = view.findViewById(R.id.btn_jump);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = getActivity();
                if (activity instanceof ITransferPageDataHelper.TransferPageDataCallback) {
                    ((ITransferPageDataHelper.TransferPageDataCallback) activity).onReceiveTransferPageData(new ITransferPageDataHelper.TransferPageDataOwner(getClass().getSimpleName()), null);
                }
            }
        });
        return view;
    }
}
