package demo.photogallery.util;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import demo.photogallery.R;

public class DialogUtil {
    private static Dialog progressDialog;

    public static void showProgressDialogBar(Context context, String message) {
        if (context == null) {
            return;
        }
        progressDialog = new Dialog(context);
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView dialogMessage = progressDialog.findViewById(R.id.progressbar_message);
        dialogMessage.setText(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }
}
