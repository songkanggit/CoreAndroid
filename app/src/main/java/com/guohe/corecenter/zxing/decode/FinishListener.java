/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guohe.corecenter.zxing.decode;

import android.app.Activity;
import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
 * Simple listener used to exit the app in a few cases.
 *
 */
public final class FinishListener
        implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {

    private final WeakReference<Activity> activityToFinishRf;

    public FinishListener(Activity activityToFinish) {
        activityToFinishRf = new WeakReference<>(activityToFinish);
    }

    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    public void run() {
        if(activityToFinishRf.get() != null) {
            activityToFinishRf.get().finish();
        }
    }

}
