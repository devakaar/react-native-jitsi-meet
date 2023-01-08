package com.reactnativejitsimeet;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.module.annotations.ReactModule;

import org.jitsi.meet.sdk.JitsiMeetViewListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import static java.security.AccessController.getContext;

import android.os.Build;

import androidx.annotation.RequiresApi;

@ReactModule(name = RNJitsiMeetViewManager.REACT_CLASS)
public class RNJitsiMeetViewManager extends SimpleViewManager<RNJitsiMeetView> implements JitsiMeetViewListener {
    public static final String REACT_CLASS = "RNJitsiMeetView";
    private IRNJitsiMeetViewReference mJitsiMeetViewReference;
    private ReactApplicationContext mReactContext;

    public RNJitsiMeetViewManager(ReactApplicationContext reactContext, IRNJitsiMeetViewReference jitsiMeetViewReference) {
        mJitsiMeetViewReference = jitsiMeetViewReference;
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public RNJitsiMeetView createViewInstance(ThemedReactContext context) {
        if (mJitsiMeetViewReference.getJitsiMeetView() == null) {
            RNJitsiMeetView view = new RNJitsiMeetView(context.getCurrentActivity());
            view.setListener(this);
            mJitsiMeetViewReference.setJitsiMeetView(view);
        }
        return mJitsiMeetViewReference.getJitsiMeetView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @ReactProp(name = "options")
    public void options(RNJitsiMeetView view, ReadableMap data){
        String url= data.getString("url");
        ReadableMap meetOptions=  data.getMap("meetOptions");
        ReadableMap meetFeatureFlags=  data.getMap("meetFeatureFlags");
        ReadableMap userInfo=data.getMap("userInfo");

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
                .setAudioMuted(meetOptions.hasKey("audioMuted") ? meetOptions.getBoolean("audioMuted") : false)
                .setAudioOnly(meetOptions.hasKey("audioOnly") ? meetOptions.getBoolean("audioOnly") : false)
                .setVideoMuted(meetOptions.hasKey("videoMuted") ? meetOptions.getBoolean("videoMuted") : false)
                .setUserInfo(_userInfo)
                .setFeatureFlag("add-people.enabled", meetFeatureFlags.hasKey("addPeopleEnabled") ? meetFeatureFlags.getBoolean("addPeopleEnabled") : true)
                .setFeatureFlag("calendar.enabled", meetFeatureFlags.hasKey("calendarEnabled") ? meetFeatureFlags.getBoolean("calendarEnabled") : true)
                .setFeatureFlag("call-integration.enabled", meetFeatureFlags.hasKey("callIntegrationEnabled") ? meetFeatureFlags.getBoolean("callIntegrationEnabled") : true)
                .setFeatureFlag("chat.enabled", meetFeatureFlags.hasKey("chatEnabled") ? meetFeatureFlags.getBoolean("chatEnabled") : true)
                .setFeatureFlag("close-captions.enabled", meetFeatureFlags.hasKey("closeCaptionsEnabled") ? meetFeatureFlags.getBoolean("closeCaptionsEnabled") : true)
                .setFeatureFlag("invite.enabled", meetFeatureFlags.hasKey("inviteEnabled") ? meetFeatureFlags.getBoolean("inviteEnabled") : true)
                .setFeatureFlag("android.screensharing.enabled", meetFeatureFlags.hasKey("androidScreenSharingEnabled") ? meetFeatureFlags.getBoolean("androidScreenSharingEnabled") : true)
                .setFeatureFlag("live-streaming.enabled", meetFeatureFlags.hasKey("liveStreamingEnabled") ? meetFeatureFlags.getBoolean("liveStreamingEnabled") : true)
                .setFeatureFlag("meeting-name.enabled", meetFeatureFlags.hasKey("meetingNameEnabled") ? meetFeatureFlags.getBoolean("meetingNameEnabled") : true)
                .setFeatureFlag("meeting-password.enabled", meetFeatureFlags.hasKey("meetingPasswordEnabled") ? meetFeatureFlags.getBoolean("meetingPasswordEnabled") : true)
                .setFeatureFlag("pip.enabled", meetFeatureFlags.hasKey("pipEnabled") ? meetFeatureFlags.getBoolean("pipEnabled") : true)
                .setFeatureFlag("kick-out.enabled", meetFeatureFlags.hasKey("kickOutEnabled") ? meetFeatureFlags.getBoolean("kickOutEnabled") : true)
                .setFeatureFlag("conference-timer.enabled", meetFeatureFlags.hasKey("conferenceTimerEnabled") ? meetFeatureFlags.getBoolean("conferenceTimerEnabled") : true)
                .setFeatureFlag("video-share.enabled", meetFeatureFlags.hasKey("videoShareEnabled") ? meetFeatureFlags.getBoolean("videoShareEnabled") : true)
                .setFeatureFlag("recording.enabled", meetFeatureFlags.hasKey("recordingEnabled") ? meetFeatureFlags.getBoolean("recordingEnabled") : true)
                .setFeatureFlag("reactions.enabled", meetFeatureFlags.hasKey("reactionsEnabled") ? meetFeatureFlags.getBoolean("reactionsEnabled") : true)
                .setFeatureFlag("raise-hand.enabled", meetFeatureFlags.hasKey("raiseHandEnabled") ? meetFeatureFlags.getBoolean("raiseHandEnabled") : true)
                .setFeatureFlag("tile-view.enabled", meetFeatureFlags.hasKey("tileViewEnabled") ? meetFeatureFlags.getBoolean("tileViewEnabled") : true)
                .setFeatureFlag("toolbox.alwaysVisible", meetFeatureFlags.hasKey("toolboxAlwaysVisible") ? meetFeatureFlags.getBoolean("toolboxAlwaysVisible") : false)
                .setFeatureFlag("toolbox.enabled", meetFeatureFlags.hasKey("toolboxEnabled") ? meetFeatureFlags.getBoolean("toolboxEnabled") : true)
                .setFeatureFlag("welcomepage.enabled", meetFeatureFlags.hasKey("welcomePageEnabled") ? meetFeatureFlags.getBoolean("welcomePageEnabled") : false)
                .setFeatureFlag("prejoinpage.enabled", meetFeatureFlags.hasKey("prejoinPageEnabled") ? meetFeatureFlags.getBoolean("prejoinPageEnabled") : false);
        Iterator<Map.Entry<String, Object>> map = meetFeatureFlags.getEntryIterator();
        map.forEachRemaining(flag -> {
            String key = flag.getKey();
            boolean value = (boolean) flag.getValue();
            options.setFeatureFlag(key, value);
        });
        mJitsiMeetViewReference.getJitsiMeetView().join(options.build());
    }

    public void onConferenceJoined(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceJoined",
                event);
    }

    public void onConferenceTerminated(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        event.putString("error", (String) data.get("error"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceTerminated",
                event);
    }

    public void onConferenceWillJoin(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceWillJoin",
                event);
    }

    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("conferenceJoined", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceJoined")))
                .put("conferenceTerminated", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceTerminated")))
                .put("conferenceWillJoin", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceWillJoin")))
                .build();
    }
}
