/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.launcher3.tapl;

import android.graphics.Point;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

/**
 * Ancestor for AppIcon and AppMenuItem.
 */
class Launchable {
    private static final int DRAG_SPEED = 500;
    protected final LauncherInstrumentation mLauncher;

    protected final UiObject2 mObject;

    Launchable(LauncherInstrumentation launcher, UiObject2 object) {
        mObject = object;
        mLauncher = launcher;
    }

    UiObject2 getObject() {
        return mObject;
    }

    /**
     * Clicks the object to launch its app.
     */
    public Background launch(String expectedPackageName) {
        return launch(expectedPackageName, By.pkg(expectedPackageName).depth(0));
    }

    /**
     * Clicks the object to launch its app.
     */
    public Background launch(String expectedPackageName, String expectedAppText) {
        return launch(expectedPackageName, By.pkg(expectedPackageName).text(expectedAppText));
    }

    private Background launch(String errorMessage, BySelector selector) {
        LauncherInstrumentation.log("Launchable.launch before click " +
                mObject.getVisibleCenter());
        LauncherInstrumentation.assertTrue(
                "Launching an app didn't open a new window: " + mObject.getText(),
                mObject.clickAndWait(Until.newWindow(), LauncherInstrumentation.WAIT_TIME_MS));
        LauncherInstrumentation.assertTrue(
                "App didn't start: " + errorMessage,
                mLauncher.getDevice().wait(Until.hasObject(selector),
                        LauncherInstrumentation.WAIT_TIME_MS));
        return new Background(mLauncher);
    }

    /**
     * Drags an object to the center of homescreen.
     */
    public Workspace dragToWorkspace() {
        final UiDevice device = mLauncher.getDevice();
        mObject.drag(new Point(
                device.getDisplayWidth() / 2, device.getDisplayHeight() / 2), DRAG_SPEED);
        return new Workspace(mLauncher);
    }
}
