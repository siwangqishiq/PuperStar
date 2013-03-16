package cpm.xinlan.purple.thread;

import com.xinlan.puerplestar.service.Translation;

import android.os.Handler;
import android.os.Message;

/**
 * 网络操作线程
 * 
 * @author Administrator
 * 
 */
public class ServiceThread extends Thread {
	private Handler handler;
	private Translation service;
	private int type;
	private String content;

	public ServiceThread(Handler handler, Translation service, int type,
			String content) {
		this.handler = handler;
		this.service = service;
		this.type = type;
		this.content = content;
	}

	private void doTranslation() {
		// 面板左移
		String retContent;
		switch (type) {
		case 0:
			retContent = service.chineseToEnglish(content);
			break;
		case 1:
			retContent = service.chineseToJapanese(content);
			break;
		case 2:
			retContent = service.englishToChinese(content);
			break;
		default:
			retContent = "";
			break;
		}// end switch
		Message msg = new Message();
		msg.obj = retContent;
		handler.sendMessage(msg);
	}

	@Override
	public void run() {
		doTranslation();
	}
}// end class
