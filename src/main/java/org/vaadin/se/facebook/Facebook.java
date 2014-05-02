/*
 * Copyright 2014 Sami Ekblad.
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
package org.vaadin.se.facebook;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sami Ekblad
 */
@JavaScript({"Facebook.js"})
public class Facebook extends AbstractJavaScriptExtension {

    private static String STATUS_CONNECTED = "connected";
    private static String STATUS_NOT_AUTHORIZED = "not_authorized";
    private static String STATUS_UNKNOWN = "unknown";

    private List<FacebookListener> listeners = new ArrayList<FacebookListener>();
    private String authToken;

    private String userId;
    private boolean loggedIn;
    private FacebookCallback activeCallback;

    public void addListener(FacebookListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FacebookListener listener) {
        listeners.remove(listener);
    }

    public Facebook(String appId) {
        getState().appId = appId;

        addFunction("onLogin", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                for (FacebookListener listener : Collections.unmodifiableList(listeners)) {
                    Facebook.this.userId = arguments.getString(0);
                    Facebook.this.authToken = arguments.getString(1);
                    Facebook.this.loggedIn = true;
                    listener.onFacebookLogin(userId);
                }
            }
        });

        addFunction("onLogout", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                Facebook.this.loggedIn = false;
                for (FacebookListener listener : Collections.unmodifiableList(listeners)) {
                    listener.onFacebookLogout();
                }
            }
        });

        addFunction("onPostCancel", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                for (FacebookListener listener : Collections.unmodifiableList(listeners)) {
                    listener.onFacebookPostCancel();
                }
            }
        });

        addFunction("onPostSuccess", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                for (FacebookListener listener : Collections.unmodifiableList(listeners)) {
                    listener.onFacebookPost(arguments.getString(0));
                }

            }
        });

        addFunction("onApiCallback", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                FacebookCallback cb = activeCallback;
                activeCallback = null;
                if (cb != null) {
                    cb.facebookResponse(arguments != null && arguments.length() > 0 ? arguments.getJSONObject(0) : null);
                }
            }
        });

    }

    @Override
    public FacebookState getState() {
        return (FacebookState) super.getState();
    }

    public void login() {
        callFunction("login");
    }

    public void logout() {
        callFunction("logout");
    }

    public boolean isLoggedIn() {
        return loggedIn && userId != null;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void share(String link, String caption) {
        Post p = new Post(Post.FEED);
        p.setLink(link);
        p.setCaption(caption);
        ui(p);
    }

    public void sendMessage(String link, String caption) {
        Post p = new Post(Post.SEND);
        p.setLink(link);
        p.setCaption(caption);
        ui(p);
    }

    public void ui(Post params) {
        JSONObject post = new JSONObject(params);
        post.remove("class");
        callFunction("ui", post);
    }

    /**
     * Make asynchronous Facebook api call.
     *
     * Response are returned using the callback.
     *
     * https://developers.facebook.com/docs/graph-api/reference/
     *
     * @param params
     * @param callback
     *
     */
    public void api(String path, String method, JSONObject params, FacebookCallback callback) {
        this.activeCallback = callback;
        callFunction("api", path, method, params != null ? new JSONObject() : params);
    }

    public void api(String path, JSONObject params, FacebookCallback callback) {
        api(path, "GET", params, callback);
    }

    public void api(String path, FacebookCallback callback) {
        api(path, "GET", null, callback);
    }

    /**
     * Generic data object used to post to Facebook.
     *
     */
    public static class Post {

        public static String LOGIN = "login";
        public static String FEED = "feed";
        public static String SEND = "send";
        public static String PAGETAB = "pagetab";
        public static String REQUEST = "apprequests";
        public static String FRIENDS = "friends";

        private String method;

        private String link;
        private String name;
        private String caption;
        private String description;
        private String from;
        private String to;
        private String picture;
        private String source;

        private String display;
        private String redirect_uri;

        private Post() {
        }

        public Post(String method) {
            this.method = method;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getRedirect_uri() {
            return redirect_uri;
        }

        public void setRedirect_uri(String redirect_uri) {
            this.redirect_uri = redirect_uri;
        }

        public String getMethod() {
            return method;
        }

        /**
         * Facebook login options.
         * <a
         * href="https://developers.facebook.com/docs/reference/javascript/FB.login">More
         * info</a>
         */
        public class LoginOptions {

            String[] scopes;
            boolean enable_profile_selector;
            String[] profile_selector_ids;

            public LoginOptions(String... scopes) {
                this.scopes = scopes;
            }

            public String[] getScopes() {
                return scopes;
            }

            public void setScopes(String[] scopes) {
                this.scopes = scopes;
            }

            public boolean isEnable_profile_selector() {
                return enable_profile_selector;
            }

            public void setEnable_profile_selector(boolean enableProfileSelector) {
                this.enable_profile_selector = enableProfileSelector;
            }

            public String[] getProfileSelectorIds() {
                return profile_selector_ids;
            }

            public void setProfile_selector_ids(String... profileSelectorIds) {
                this.profile_selector_ids = profileSelectorIds;
            }

        }
    }
}
