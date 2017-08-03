package com.example.jykin.concolor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rarepebble.colorpicker.ColorPickerView;

/**
 * Created by TheBlah on 7/31/2017.
 */

public class HSV extends DialogFragment {
    private ColorPickerView colorPickerView;
    private Button bCancel, bOk;
    private int initialColor;
    private final String TAG = "hsv_fragment";

    public HSV(){

    }

    public static HSV newInstance(int color){
        HSV newhsv = new HSV();

        Bundle args = new Bundle();
        args.putInt("initialColor", color);
        newhsv.setArguments(args);

        return newhsv;
    }

    public interface OnDialogCloseListener {
        void closeDialog(int color);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hsv, container, false);
        colorPickerView = (ColorPickerView) view.findViewById(R.id.colorPicker);
        initialColor = getArguments().getInt("initialColor");
        colorPickerView.setColor(initialColor);
        bCancel = (Button) view.findViewById(R.id.button_cancel);
        bCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                HSV.OnDialogCloseListener activity = (HSV.OnDialogCloseListener) getActivity();
                activity.closeDialog(initialColor);
                HSV.this.dismiss();
            }
        });

        bOk = (Button) view.findViewById(R.id.button_ok);
        bOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                HSV.OnDialogCloseListener activity = (HSV.OnDialogCloseListener) getActivity();
                activity.closeDialog(colorPickerView.getColor());
                HSV.this.dismiss();
            }
        });

        return view;
    }

    //sets the size of the dialogfragment to match parent
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
