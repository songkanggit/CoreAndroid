package com.guohe.corecenter.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import com.guohe.corecenter.core.logger.Logger;
import com.guohe.corecenter.utils.JacksonUtil;

import org.intellij.lang.annotations.MagicConstant;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class X5WebView extends WebView {
	private static final String TAG = X5WebView.class.getSimpleName();
	private Context mContext;
	private OnScrollChangeListener mOnScrollChangeListener;
	private WeakReference<WebViewCallback> mCallbackRf;

	@MagicConstant(stringValues = {JsToAppType.MENU_INIT, JsToAppType.SHARE, JsToAppType.PAY, JsToAppType.JUMP_ORDER, JsToAppType.ORDER_STATE, JsToAppType.GET_TOKEN, JsToAppType.SET_BACK})
	public @interface JsToAppType {
		String MENU_INIT = "0";
		String SHARE = "1";
		String PAY = "2";
		String JUMP_ORDER = "3";
		String ORDER_STATE = "4";
		String GET_TOKEN = "5";
		String SET_BACK = "6";
	}

	@MagicConstant(stringValues = {AppToJsType.INIT, AppToJsType.SEND_TOKEN})
	public @interface AppToJsType {
		String INIT = "0";
		String MENU = "1";
		String SEND_TOKEN = "2";
		String PAY_RESULT = "3";
	}

	private WebViewClient mClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {
			Logger.d(TAG, url);
			if(url.startsWith("weixin://wap/pay")) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				mContext.startActivity(intent);
				return true;
			}

			if(url.startsWith("alipays:") || url.startsWith("alipay")) {
				try {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					mContext.startActivity(intent);
				} catch (Exception e) {
					new AlertDialog.Builder(mContext)
							.setMessage("未检测到支付宝客户端，请安装后重试。")
							.setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									Uri alipayUrl = Uri.parse("https://d.alipay.com");
									mContext.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
								}
							}).setNegativeButton("取消", null).show();
				}
				return true;
			}
			if(TextUtils.isEmpty(url) || url.startsWith("http://") || url.startsWith("https://")) return false;
			try {
				if(appInstalledOrNot(url)) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					view.getContext().startActivity(intent);
				} else {
					Logger.d(TAG, "APP NOT INSTALL:" + url);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if(mCallbackRf.get() != null) {
				mCallbackRf.get().onPageStart();
			}
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			if(mCallbackRf.get() != null) {
				mCallbackRf.get().onPageFinished();
			}
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				Log.i(TAG, "Error code:" + error.getErrorCode() + "," + request.getMethod());
			}
//			view.loadUrl("file:///android_asset/internet_offline.html");
		}
	};

	public X5WebView(Context context) {
		this(context, null);
		mContext = context;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	public X5WebView(Context context, AttributeSet arg1) {
		super(context, arg1);
		mContext = context;
		setWebViewClient(mClient);
		initWebViewSettings();
		setClickable(true);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mOnScrollChangeListener != null) {
			mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
		}
	}

	public void setOnScrollChangeListener(OnScrollChangeListener listener) {
		mOnScrollChangeListener = listener;
	}

	public void appToJs(final JsData data) {
		post(() -> {
            try {
                final String param = JacksonUtil.convertObjectToString(data, true);
                loadUrl("javascript:appToJs("+ param +")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
	}

	public void setWebviewCallback(WebViewCallback callback) {
		mCallbackRf = new WeakReference<>(callback);
		addJavascriptInterface(new WebViewInterface(callback), WebViewInterface.WEB_INTERFACE_NAME);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Integer.MAX_VALUE);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
		// LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
		// LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
		// LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = mContext.getPackageManager();
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return false;
	}

	public interface WebViewCallback extends JavaScriptCallback{
		void onPageFinished();
		void onPageStart();
	}

	public static class WebViewInterface {
		private WeakReference<JavaScriptCallback> javaScriptCallbackWR;

		public static final String WEB_INTERFACE_NAME = "Android";

		public WebViewInterface(JavaScriptCallback callback) {
			javaScriptCallbackWR = new WeakReference<>(callback);
		}

		@JavascriptInterface
		public void jsToApp(String param){
			if(javaScriptCallbackWR.get() != null) {
				try {
					JsData jsData = JacksonUtil.readValue(param, JsData.class);
					javaScriptCallbackWR.get().onJsData(jsData);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public interface JavaScriptCallback {
		void onJsData(final JsData param);
	}

	public static class JsData {
		private String flag;
		private Object data;

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

	public interface OnScrollChangeListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
}
