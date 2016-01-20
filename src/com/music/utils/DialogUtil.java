package com.music.utils;



import com.music.lu.R;
import com.music.widget.dialog.LoadingView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class DialogUtil {
	private static AlertDialog dialog=null;
	private static Toast toast;
	/**
	 * ����һ����Ϣ��
	 * @param context
	 * @param msg ��Ϣ����
	 */
	public static void showToast(Context context,String msg){
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	/**
	 * ����һ��ѡ��Ի���
	 * @param context
	 * @param title	�Ի���ı���
	 * @param items �Ի��������������
	 * @param dialogInterface ����Ի������������ļ����¼�
	 */
	public static  void showAlertDialog(Context context,String title,String[] items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 * ����һ��ѡ��Ի���
	 * @param context
	 * @param title	�Ի���ı���
	 * @param items �Ի��������������
	 * @param dialogInterface ����Ի������������ļ����¼�
	 */
	public static  void showAlertDialog(Context context,String title,int items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 * �رյ�����
	 */
	public static void closeAlertDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
		}
	}
	/**
	 * show a closeDialog
	 * @param context
	 * @param dialogInterface ȷ����ť�¼�
	 */
	public static void showExitAlertDialog(Context context,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("�˳�").setMessage("��ȷ��Ҫ�Ƴ���")
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeAlertDialog();
			}
		})
		.setPositiveButton("ȷ��", dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	
	
	public static void showWaitDialog(Context context){
		dialog=ProgressDialog.show(context, "����", "��������");
		dialog.show();
	}
	public static void showWaitDialog(Context context,String title,String message){
		dialog=ProgressDialog.show(context, title, message);
		dialog.show();
	}
	public static void showCustomWaitDialog(Context context){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		dialog=builder.create();
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.dialog_custom_wait, null);
		LoadingView loadingView=(LoadingView) view.findViewById(R.id.loadView);
		loadingView.setLoadingText("����������...");
		dialog.show();
	
		
//		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//		params.width = (int) (300);
//		params.height = (int) (300);
//		dialog.getWindow().setAttributes(params);
		dialog.getWindow().setContentView(view);
		
	}
}
