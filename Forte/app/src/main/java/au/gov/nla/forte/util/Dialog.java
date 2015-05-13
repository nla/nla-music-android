package au.gov.nla.forte.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;

public class Dialog {

	public static AlertDialog createAlert(Context ctx, int title, int message, boolean isCancellable, String buttonText, DialogInterface.OnClickListener onPositiveButtonClick) {
		return createAlert(ctx, ctx.getResources().getString(title), ctx.getResources().getString(message), isCancellable, buttonText, onPositiveButtonClick);
	}
	
	public static AlertDialog createAlert(Context ctx, int title, String message, boolean isCancellable, String buttonText, DialogInterface.OnClickListener onPositiveButtonClick) {
		return createAlert(ctx, ctx.getResources().getString(title), message, isCancellable, buttonText, onPositiveButtonClick);
	}

	public static AlertDialog createConfirm(Context ctx, int title, int message, boolean isCancellable, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClick, String negativeButtonText, DialogInterface.OnClickListener onNegativeButtonClick) {
		return createConfirm(ctx, ctx.getResources().getString(title), ctx.getResources().getString(message), isCancellable, positiveButtonText, onPositiveButtonClick, negativeButtonText, onNegativeButtonClick);
	}
	
	public static AlertDialog createAlert(Context ctx, String title, String message, boolean isCancellable, String buttonText, DialogInterface.OnClickListener onPositiveButtonClick) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(isCancellable);
		dialog.setPositiveButton(buttonText, onPositiveButtonClick);
		return dialog.create();
	}

	public static AlertDialog createConfirm(Context ctx, String title, String message, boolean isCancellable, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClick, String negativeButtonText, DialogInterface.OnClickListener onNegativeButtonClick) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(isCancellable);
		dialog.setPositiveButton(positiveButtonText, onPositiveButtonClick);
		dialog.setNegativeButton(negativeButtonText, onNegativeButtonClick);
		return dialog.create();
	}
	
	public static AlertDialog createAlertWithRadioButtons(Context ctx, String title, ListAdapter options, boolean isCancellable, DialogInterface.OnClickListener onButtonClick) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setSingleChoiceItems(options, -1, onButtonClick);
		dialog.setCancelable(isCancellable);
		return dialog.create();
	}
	
	public static AlertDialog createAlertWithCustomView(Context ctx, String title, View view, boolean isCancellable, String buttonText, DialogInterface.OnClickListener onButtonClick){
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setCancelable(isCancellable);
		dialog.setPositiveButton(buttonText, onButtonClick);
		dialog.setView(view);
		return dialog.create();
	}
	
	public static AlertDialog createConfirmWithCustomView(Context ctx, String title, View view, boolean isCancellable, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClick, String negativeButtonText, DialogInterface.OnClickListener onNegativeButtonClick) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setCancelable(isCancellable);
		dialog.setPositiveButton(positiveButtonText, onPositiveButtonClick);
		dialog.setNegativeButton(negativeButtonText, onNegativeButtonClick);
		dialog.setView(view);
		return dialog.create();
	}
	
	public static AlertDialog createConfirmWithRichText(Context ctx, String title, String richText, boolean isCancellable, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClick, String negativeButtonText, DialogInterface.OnClickListener onNegativeButtonClick) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title);
		dialog.setCancelable(isCancellable);
		dialog.setPositiveButton(positiveButtonText, onPositiveButtonClick);
		dialog.setNegativeButton(negativeButtonText, onNegativeButtonClick);
		WebView webView = new WebView(ctx);
		webView.loadData(richText, "text/html", "UTF-8");
		dialog.setView(webView);
		return dialog.create();
	}
}