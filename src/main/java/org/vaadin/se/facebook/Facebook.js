window.org_vaadin_se_facebook_Facebook = function() {

    var self = this;

    this.fbRoot = document.getElementById("fb-root");
    if (!this.fbRoot) {
        this.fbRoot = document.createElement("div");
        this.fbRoot.id = "fb-root";
        document.body.appendChild(this.fbRoot);
    }

    this.apiInit = false;

    this.onStateChange = function() {
        if (!self.apiInit) {
            self.initApi(self.getState().appId);
            self.apiInit = true;
        }
    };

    this.initApi = function(fbAppId) {

        window.fbAsyncInit = function() {
            FB.init({
                appId: fbAppId,
                cookie: true,
                status: true,
                xfbml: true
            });

            /* Subscribe to all facebook login events */
            FB.Event.subscribe('auth.login', function(response) {

            });

            FB.Event.subscribe('auth.logout', function(response) {
            });

            FB.Event.subscribe('auth.statusChange', function(response) {
                if (response.status === "connected") {
                    var r = response.authResponse;
                    self.onLogin(r.userID, r.accessToken);
                } else
                    self.onLogout();
            });

        };
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {
                return;
            }
            js = d.createElement(s);
            js.id = id;
            js.src = "//connect.facebook.net/en_US/all.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
    };

    this.login = function() {
        FB.login(function(response) {
            // We subscribed the events, so don't need to do anything here.
        });
    };

    this.logout = function() {
        FB.logout(function(response) {
            // We subscribed the events, so don't need to do anything here.
        });
    };

    this.api = function(path, method, params) {
        FB.api(path, method, params, self.onApiCallback);
    };


    this.ui = function(params) {
        FB.ui(
                params,
                function(response) {
                    if (response && response.post_id) {
                        self.onPostSuccess(response.post_id, response);
                    } else {
                        self.onPostCancel();
                    }
                }
        );
    };

};


window.org_vaadin_se_facebook_LoginButton = function() {

    var self = this;

    this.init = function(showFaces) {
        
         
        self.getElement().innerHTML = "<fb:login-button "+(showFaces?"show-faces=\"true\"":"")+" width=\"200\" max-rows=\"1\"></fb:login-button>";
        if (window.FB) {
            FB.XFBML.parse();
        }
    };

};










