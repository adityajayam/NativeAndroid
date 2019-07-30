package demo.photogallery.util;

import android.app.Dialog;
import android.widget.TextView;

import demo.photogallery.R;

public class DialogUtil {

    public static void showProgressDialogBar(Dialog progressDialog, String message) {
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView dialogMessage = progressDialog.findViewById(R.id.progressbar_message);
        dialogMessage.setText(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
