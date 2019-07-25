package com.guohe.corecenter.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.HandlerThread;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import android.util.SparseArray;

import com.guohe.corecenter.BuildConfig;
import com.guohe.corecenter.core.CoreApplication;
import com.guohe.corecenter.core.CoreContext;
import com.guohe.corecenter.core.connection.NetworkManager;
import com.guohe.corecenter.core.http.HttpManager;
import com.guohe.corecenter.core.logger.LoggerManager;
import com.guohe.corecenter.core.pereference.PreferencesManager;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by songkang on 2017/7/2.
 */

public class MyApplication extends CoreApplication {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private final AtomicInteger mThreadId = new AtomicInteger(1);

    private static final int PROCESS_MAIN = 0;
    private static final int PROCESS_SERVICE = 1;
    private static final int PROCESS_DEAMON_MIN = 10;
    private static final int PROCESS_DEAMON_1 = 11;
    private static final int PROCESS_DEAMON_2 = 12;
    private static final int PROCESS_DEAMON_3 = 13;
    private static final int PROCESS_DEAMON_MAX = 20;

    private int mProcessType;

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        String channel = "debug";
        if(!BuildConfig.DEBUG) {
            // [可选]设置是否打开debug输出，上线时请关闭，Logcat标签为"MtaSDK"
            //StatConfig.setDebugEnable(true);
        }
        // 基础统计API
//        StatConfig.setInstallChannel(channel);
//        StatService.registerActivityLifecycleCallbacks(this);
        TwinklingRefreshLayout.setDefaultHeader(SinaRefreshView.class.getName());
        TwinklingRefreshLayout.setDefaultFooter(BallPulseView.class.getName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mProcessType = getProcessType(base);
        MultiDex.install(base);
    }

    private int getProcessType(Context context) {
        SparseArray<String> processMap = new SparseArray<>(5);

        try {
            String processName = context.getPackageManager().getApplicationInfo(getPackageName(), 0).processName.trim();
            processMap.put(PROCESS_MAIN, processName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        String processName = "";
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == pid) {
                processName = runningAppProcessInfo.processName.trim();
                break;
            }
        }

        for (int index = 0; index < processMap.size(); index++) {
            if (processMap.valueAt(index).compareTo(processName) == 0) {
                return processMap.keyAt(index);
            }
        }

        return -1;
    }

    private boolean isProcessDeamon() {
        return mProcessType > PROCESS_DEAMON_MIN && mProcessType < PROCESS_DEAMON_MAX;
    }

    @Override
    protected void onInitializeApplicationService(CoreContext coreContext) {
        if (isProcessDeamon()) return;
        coreContext.addApplicationService(new HttpManager());
        coreContext.addApplicationService(new LoggerManager(coreContext));
        coreContext.addApplicationService(new NetworkManager(coreContext));
        coreContext.addApplicationService(new PreferencesManager(coreContext));
    }

    @Override
    protected final ExecutorService onCreateWorkerThreadPool() {
        return createThreadPool(Thread.NORM_PRIORITY);
    }

    @Override
    protected ExecutorService onCreateBackgroundThreadPool() {
        return createThreadPool(Thread.MIN_PRIORITY);
    }

    @Override
    protected ScheduledThreadPoolExecutor onCreateScheduledThreadPool() {
        return new ScheduledThreadPoolExecutor(2) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                logException(r, t);
            }
        };
    }

    @Override
    protected HandlerThread onCreateWorkerThread(String poolName) {
        return new HandlerThread(MyApplication.class.getName());
    }

    private ExecutorService createThreadPool(int priority) {
        int maximumPoolSize = DEFAULT_THREAD_POOL_SIZE + 1;
        final int processors = Runtime.getRuntime().availableProcessors();
        if (processors > 1) {
            maximumPoolSize = processors * 2 + 1;
        }

        return new ThreadPoolExecutor(2, maximumPoolSize, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            private final ThreadGroup mThreadGroup = System.getSecurityManager() != null ? System.getSecurityManager().getThreadGroup() : Thread.currentThread().getThreadGroup();
            private final AtomicInteger mAtomic = new AtomicInteger(1);
            private final String mThreadName = "optimize-master-pool-" + mThreadId.getAndIncrement() + "-thread-";

            @Override
            public Thread newThread(@NonNull Runnable task) {
                Thread thread = new Thread(mThreadGroup, task, mThreadName + mAtomic.getAndIncrement() + "-" + priority, 0);
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }

                thread.setPriority(priority);
                return thread;
            }
        }) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                logException(r, t);
            }
        };
    }
}
