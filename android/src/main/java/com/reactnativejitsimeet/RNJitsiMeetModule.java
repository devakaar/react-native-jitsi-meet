package com.reactnativejitsimeet;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.module.annotations.ReactModule;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

@ReactModule(name = RNJitsiMeetModule.MODULE_NAME)
public class RNJitsiMeetModule extends ReactContextBaseJavaModule {
    public static final String MODULE_NAME = "RNJitsiMeetModule";
    private final IRNJitsiMeetViewReference mJitsiMeetViewReference;

    public RNJitsiMeetModule(ReactApplicationContext reactContext, IRNJitsiMeetViewReference jitsiMeetViewReference) {
        super(reactContext);
        mJitsiMeetViewReference = jitsiMeetViewReference;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void initialize() {
        Log.d("JitsiMeet", "Initialize is deprecated in v2");
    }

    @ReactMethod
    public void call(String url, ReadableMap userInfo, ReadableMap meetOptions, ReadableMap meetFeatureFlags) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    RNJitsiMeetUserInfo _userInfo = new RNJitsiMeetUserInfo();
                    if (userInfo != null) {
                        if (userInfo.hasKey("displayName")) {
                            _userInfo.setDisplayName(userInfo.getString("displayName"));
                        }
                        if (userInfo.hasKey("email")) {
                            _userInfo.setEmail(userInfo.getString("email"));
                        }
                        if (userInfo.hasKey("avatar")) {
                            String avatarURL = userInfo.getString("avatar");
                            try {
                                _userInfo.setAvatar(new URL(avatarURL));
                            } catch (MalformedURLException e) {
                            }
                        }
                    }
                    RNJitsiMeetConferenceOptions.Builder options = new RNJitsiMeetConferenceOptions.Builder()
                            .setRoom(url)
                            .setToken(meetOptions.hasKey("token") ? meetOptions.getString("token") : "")
                            .setSubject(meetOptions.hasKey("subject") ? meetOptions.getString("subject") : "")
                            .setAudioMuted(meetOptions.hasKey("audioMuted") && meetOptions.getBoolean("audioMuted"))
                            .setAudioOnly(meetOptions.hasKey("audioOnly") && meetOptions.getBoolean("audioOnly"))
                            .setVideoMuted(meetOptions.hasKey("videoMuted") && meetOptions.getBoolean("videoMuted"))
                            .setUserInfo(_userInfo)
                            .setFeatureFlag("add-people.enabled", !meetFeatureFlags.hasKey("addPeopleEnabled") || meetFeatureFlags.getBoolean("addPeopleEnabled"))
                            .setFeatureFlag("calendar.enabled", !meetFeatureFlags.hasKey("calendarEnabled") || meetFeatureFlags.getBoolean("calendarEnabled"))
                            .setFeatureFlag("call-integration.enabled", !meetFeatureFlags.hasKey("callIntegrationEnabled") || meetFeatureFlags.getBoolean("callIntegrationEnabled"))
                            .setFeatureFlag("chat.enabled", !meetFeatureFlags.hasKey("chatEnabled") || meetFeatureFlags.getBoolean("chatEnabled"))
                            .setFeatureFlag("close-captions.enabled", !meetFeatureFlags.hasKey("closeCaptionsEnabled") || meetFeatureFlags.getBoolean("closeCaptionsEnabled"))
                            .setFeatureFlag("invite.enabled", !meetFeatureFlags.hasKey("inviteEnabled") || meetFeatureFlags.getBoolean("inviteEnabled"))
                            .setFeatureFlag("android.screensharing.enabled", !meetFeatureFlags.hasKey("androidScreenSharingEnabled") || meetFeatureFlags.getBoolean("androidScreenSharingEnabled"))
                            .setFeatureFlag("live-streaming.enabled", !meetFeatureFlags.hasKey("liveStreamingEnabled") || meetFeatureFlags.getBoolean("liveStreamingEnabled"))
                            .setFeatureFlag("meeting-name.enabled", !meetFeatureFlags.hasKey("meetingNameEnabled") || meetFeatureFlags.getBoolean("meetingNameEnabled"))
                            .setFeatureFlag("meeting-password.enabled", !meetFeatureFlags.hasKey("meetingPasswordEnabled") || meetFeatureFlags.getBoolean("meetingPasswordEnabled"))
                            .setFeatureFlag("pip.enabled", !meetFeatureFlags.hasKey("pipEnabled") || meetFeatureFlags.getBoolean("pipEnabled"))
                            .setFeatureFlag("kick-out.enabled", !meetFeatureFlags.hasKey("kickOutEnabled") || meetFeatureFlags.getBoolean("kickOutEnabled"))
                            .setFeatureFlag("conference-timer.enabled", !meetFeatureFlags.hasKey("conferenceTimerEnabled") || meetFeatureFlags.getBoolean("conferenceTimerEnabled"))
                            .setFeatureFlag("video-share.enabled", !meetFeatureFlags.hasKey("videoShareEnabled") || meetFeatureFlags.getBoolean("videoShareEnabled"))
                            .setFeatureFlag("recording.enabled", !meetFeatureFlags.hasKey("recordingEnabled") || meetFeatureFlags.getBoolean("recordingEnabled"))
                            .setFeatureFlag("reactions.enabled", !meetFeatureFlags.hasKey("reactionsEnabled") || meetFeatureFlags.getBoolean("reactionsEnabled"))
                            .setFeatureFlag("raise-hand.enabled", !meetFeatureFlags.hasKey("raiseHandEnabled") || meetFeatureFlags.getBoolean("raiseHandEnabled"))
                            .setFeatureFlag("tile-view.enabled", !meetFeatureFlags.hasKey("tileViewEnabled") || meetFeatureFlags.getBoolean("tileViewEnabled"))
                            .setFeatureFlag("toolbox.alwaysVisible", meetFeatureFlags.hasKey("toolboxAlwaysVisible") && meetFeatureFlags.getBoolean("toolboxAlwaysVisible"))
                            .setFeatureFlag("toolbox.enabled", !meetFeatureFlags.hasKey("toolboxEnabled") || meetFeatureFlags.getBoolean("toolboxEnabled"))
                            .setFeatureFlag("welcomepage.enabled", meetFeatureFlags.hasKey("welcomePageEnabled") && meetFeatureFlags.getBoolean("welcomePageEnabled"))
                            .setFeatureFlag("prejoinpage.enabled", meetFeatureFlags.hasKey("prejoinPageEnabled") && meetFeatureFlags.getBoolean("prejoinPageEnabled"));
                    Iterator<Map.Entry<String, Object>> map = meetFeatureFlags.getEntryIterator();
                    map.forEachRemaining(flag -> {
                        String key = flag.getKey();
                        boolean value = (boolean) flag.getValue();
                        options.setFeatureFlag(key, value);
                    });
                    mJitsiMeetViewReference.getJitsiMeetView().join(options.build());
                }
            }
        });
    }

    @ReactMethod
    public void audioCall(String url, ReadableMap userInfo) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    RNJitsiMeetUserInfo _userInfo = new RNJitsiMeetUserInfo();
                    if (userInfo != null) {
                        if (userInfo.hasKey("displayName")) {
                            _userInfo.setDisplayName(userInfo.getString("displayName"));
                        }
                        if (userInfo.hasKey("email")) {
                            _userInfo.setEmail(userInfo.getString("email"));
                        }
                        if (userInfo.hasKey("avatar")) {
                            String avatarURL = userInfo.getString("avatar");
                            try {
                                _userInfo.setAvatar(new URL(avatarURL));
                            } catch (MalformedURLException e) {
                            }
                        }
                    }
                    RNJitsiMeetConferenceOptions options = new RNJitsiMeetConferenceOptions.Builder()
                            .setRoom(url)
                            .setAudioOnly(true)
                            .setUserInfo(_userInfo)
                            .build();
                    mJitsiMeetViewReference.getJitsiMeetView().join(options);
                }
            }
        });
    }

    @ReactMethod
    public void endCall() {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    mJitsiMeetViewReference.getJitsiMeetView().leave();
                }
            }
        });
    }
}
