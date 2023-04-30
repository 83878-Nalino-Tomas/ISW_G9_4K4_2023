package isw.grupo9.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private DialogInterface.OnCancelListener cancelListener;

    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener,
                                                 DialogInterface.OnCancelListener cancelListener){
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setListener(listener);
        fragment.setCancelListener(cancelListener);
        return fragment;
    }

    private void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    private void setCancelListener(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        cancelListener.onCancel(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),listener, hora, minuto, true);
        timePickerDialog.setOnCancelListener(cancelListener);
        return timePickerDialog;
    }
}
