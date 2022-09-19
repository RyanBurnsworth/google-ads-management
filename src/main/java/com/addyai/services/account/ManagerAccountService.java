/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.services.account;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.util.Key;
import com.google.auth.oauth2.ClientId;
import com.google.auth.oauth2.UserAuthorizer;
import com.google.auth.oauth2.UserCredentials;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Link A Customer Account to a Manager account
 */
public class ManagerAccountService {
    public static final String OAUTH2_CALLBACK_BASE_URI = "http://127.0.0.1";
    private static final String ADWORDS_SCOPE = "https://www.googleapis.com/auth/adwords";

    /**
     * Initialize the connection with a manager account
     *
     * @param clientId              the OAUTH2 client id
     * @param clientSecret          the OAUTH2 client secret
     * @param loginEmailAddressHint the login email address for the manager account
     * @throws Exception simpleCallbackServer failure
     */
    public void initiateGoogleAdsAccountLinking(
            String clientId, String clientSecret, String loginEmailAddressHint)
            throws Exception {
        // Creates an anti-forgery state token
        String state = new BigInteger(130, new SecureRandom()).toString(32);

        // create a list of scopes
        List<String> scopeArray = new ArrayList<>();
        scopeArray.add(ADWORDS_SCOPE);

        // Creates an HTTP server that will listen for the OAuth2 callback request.
        URI baseUri;
        UserAuthorizer userAuthorizer;
        AuthorizationResponse authorizationResponse;
        try (SimpleCallbackServer simpleCallbackServer = new SimpleCallbackServer()) {
            userAuthorizer =
                    UserAuthorizer.newBuilder()
                            .setClientId(ClientId.of(clientId, clientSecret))
                            .setScopes(scopeArray)
                            // Provides an empty callback URI so that no additional suffix is added to the
                            // redirect. By default, UserAuthorizer will use "/oauth2callback" if this is either
                            // not set or set to null.
                            .setCallbackUri(URI.create(""))
                            .build();
            baseUri = URI.create(OAUTH2_CALLBACK_BASE_URI + ":" + simpleCallbackServer.getLocalPort());
            System.out.printf(
                    "Paste this url in your browser:%n%s%n",
                    userAuthorizer.getAuthorizationUrl(loginEmailAddressHint, state, baseUri));

            // Waits for the authorization code.
            simpleCallbackServer.accept();
            authorizationResponse = simpleCallbackServer.authorizationResponse;
        }

        if (authorizationResponse == null || authorizationResponse.code == null) {
            throw new NullPointerException(
                    "OAuth2 callback did not contain an authorization code: " + authorizationResponse);
        }

        // Confirms that the state in the response matches the state token used to generate the
        // authorization URL.
        if (!state.equals(authorizationResponse.state)) {
            throw new IllegalStateException("State does not match expected state");
        }

        // Exchanges the authorization code for credentials and print the refresh token.
        UserCredentials userCredentials =
                userAuthorizer.getCredentialsFromCode(authorizationResponse.code, baseUri);
        System.out.printf("Your refresh token is: %s%n", userCredentials.getRefreshToken());

        // Prints the configuration file contents.
        Properties adsProperties = new Properties();
        adsProperties.put(GoogleAdsClient.Builder.ConfigPropertyKey.CLIENT_ID.getPropertyKey(), clientId);
        adsProperties.put(GoogleAdsClient.Builder.ConfigPropertyKey.CLIENT_SECRET.getPropertyKey(), clientSecret);
        adsProperties.put(
                GoogleAdsClient.Builder.ConfigPropertyKey.REFRESH_TOKEN.getPropertyKey(), userCredentials.getRefreshToken());
        adsProperties.put(
                GoogleAdsClient.Builder.ConfigPropertyKey.DEVELOPER_TOKEN.getPropertyKey(), "INSERT_DEVELOPER_TOKEN_HERE"); //TODO

        showConfigurationFile(adsProperties);
    }

    private void showConfigurationFile(Properties adsProperties) throws IOException {
        System.out.printf(
                "Copy the text below into a file named %s in your home directory, and replace "
                        + "INSERT_XXX_HERE with your configuration:%n",
                GoogleAdsClient.Builder.DEFAULT_PROPERTIES_CONFIG_FILE_NAME);
        System.out.println(
                "######################## Configuration file start ########################");
        adsProperties.store(System.out, null);
        System.out.printf(
                "# Required for manager accounts only: Specify the login customer ID used to%n"
                        + "# authenticate API calls. This will be the customer ID of the authenticated%n"
                        + "# manager account. You can also specify this later in code if your application%n"
                        + "# uses multiple manager account + OAuth pairs.%n"
                        + "#%n");
        System.out.println(
                "# " + GoogleAdsClient.Builder.ConfigPropertyKey.LOGIN_CUSTOMER_ID.getPropertyKey() + "=INSERT_LOGIN_CUSTOMER_ID");
        System.out.println(
                "######################## Configuration file end ##########################");
    }

    /**
     * Basic server that listens for the OAuth2 callback.
     */
    private static class SimpleCallbackServer extends ServerSocket {

        private AuthorizationResponse authorizationResponse;

        SimpleCallbackServer() throws IOException {
            // Passes a port # of zero so that a port will be automatically allocated.
            super(0);
        }

        /**
         * Blocks until a connection is made to this server. After this method completes, the
         * authorizationResponse of this server will be set, provided the request line is in the
         * expected format.
         */
        @Override
        public Socket accept() throws IOException {
            Socket socket = super.accept();

            try (BufferedReader in =
                         new BufferedReader(
                                 new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                String callbackRequest = in.readLine();
                // Uses a regular expression to extract the request line from the first line of the
                // callback request, e.g.:
                //   GET /?code=AUTH_CODE&state=XYZ&scope=https://www.googleapis.com/auth/adwords HTTP/1.1
                Pattern pattern = Pattern.compile("GET +([^ ]+)");
                Matcher matcher = pattern.matcher(Strings.nullToEmpty(callbackRequest));
                if (matcher.find()) {
                    String relativeUrl = matcher.group(1);
                    authorizationResponse = new AuthorizationResponse(OAUTH2_CALLBACK_BASE_URI + relativeUrl);
                }
                try (Writer outputWriter = new OutputStreamWriter(socket.getOutputStream())) {
                    outputWriter.append("HTTP/1.1 ");
                    outputWriter.append(Integer.toString(HttpStatusCodes.STATUS_CODE_OK));
                    outputWriter.append(" OK\n");
                    outputWriter.append("Content-Type: text/html\n\n");

                    outputWriter.append("<b>");
                    if (authorizationResponse.code != null) {
                        outputWriter.append("Authorization code was successfully retrieved.");
                    } else {
                        outputWriter.append("Failed to retrieve authorization code.");
                    }
                    outputWriter.append("</b>");
                    outputWriter.append("<p>Please check the console output from <code>");
                    outputWriter.append(ManagerAccountService.class.getSimpleName());
                    outputWriter.append("</code> for further instructions.");
                }
            }
            return socket;
        }
    }

    /**
     * Response object with attributes corresponding to OAuth2 callback parameters.
     */
    static class AuthorizationResponse extends GenericUrl {

        /**
         * The authorization code to exchange for an access token and (optionally) a refresh token.
         */
        @Key
        String code;

        /**
         * Error from the request or from the processing of the request.
         */
        @Key
        String error;

        /**
         * State parameter from the callback request.
         */
        @Key
        String state;

        /**
         * Constructs a new instance based on an absolute URL. All fields annotated with the {@link Key}
         * annotation will be set if they are present in the URL.
         *
         * @param encodedUrl absolute URL with query parameters.
         */
        public AuthorizationResponse(String encodedUrl) {
            super(encodedUrl);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(getClass())
                    .add("code", code)
                    .add("error", error)
                    .add("state", state)
                    .toString();
        }
    }
}
