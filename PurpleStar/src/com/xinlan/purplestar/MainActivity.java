package com.xinlan.purplestar;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.puerplestar.service.Translation;
import com.xinlan.puerplestar.service.TranslationFactory;

import cpm.xinlan.purple.thread.ServiceThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText contentText;
	private Translation service;
	private Spinner typeSpinner;
	private Button doTranslationButton;
	private Button doCopyButton;
	private TextView translationText;
	private Handler handler;

	RelativeLayout adContainer;
	DomobAdView mAdview;

	public static final int DURATION = 350;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("口袋翻译");
		setContentView(R.layout.activity_main);
		init();
		addDombAD();
	}

	private void init() {
		contentText = (EditText) this.findViewById(R.id.main_content);
		doTranslationButton = (Button) this.findViewById(R.id.main_doTrans);
		typeSpinner = (Spinner) this.findViewById(R.id.main_spinner);
		translationText = (TextView) this.findViewById(R.id.translationContent);
		doCopyButton = (Button) this.findViewById(R.id.main_doCopy);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.type_array, R.layout.type_item);
		typeSpinner.setAdapter(adapter);
		addService();
		addListener();
	}

	private void addService() {
		service = TranslationFactory.genTranslation();
	}

	private void addListener() {
		doTranslationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v)// 点击翻译
			{

				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						String msgString = (String) msg.obj;
						translationText.setText(msgString);
						TranslateAnimation moveToRight = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, -1,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						moveToRight.setDuration(DURATION);
						moveToRight.setFillAfter(true);
						translationText.setAnimation(moveToRight);
					}
				};

				TranslateAnimation moveToLeft = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, -1,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				moveToLeft.setDuration(DURATION);
				moveToLeft.setFillAfter(true);
				translationText.setAnimation(moveToLeft);

				new ServiceThread(handler, service, typeSpinner
						.getSelectedItemPosition(), contentText.getText()
						.toString().trim()).start();// 开启一个新的查询线程
			}
		});

		doCopyButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v)// 复制到剪切板
			{
				ClipboardManager cm = (ClipboardManager) MainActivity.this
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(translationText.getText().toString().trim());
				Toast.makeText(MainActivity.this, "复制成功!^_^",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void addDombAD() {
		adContainer = (RelativeLayout) this.findViewById(R.id.mainAdContainer);
		mAdview = new DomobAdView(this,
				getResources().getString(R.string.adId),
				DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}
}// end class
