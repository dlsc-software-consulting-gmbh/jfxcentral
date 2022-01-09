package com.dlsc.jfxcentral.twitter;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dlsc.jfxcentral.twitter.TwitterException.ErrorCodes.NOT_AUTHENTICATED;
import static com.dlsc.jfxcentral.twitter.TwitterException.ErrorCodes.UNAUTHORIZED_APPLICATION;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.WARNING;

/**
 * This class allow to use Twitter in the chat.
 *
 * @author Thierry Wasylczenko
 * @version 1.1
 * @since SlideshowFX 1.0
 */
public class TwitterService {
    private static final Logger LOGGER = Logger.getLogger(TwitterService.class.getName());

    protected static final String PROPERTY_PREFIX = "service.twitter.";
    protected static final String ACCESS_TOKEN_PARAMETER = PROPERTY_PREFIX + "accessToken";
    protected static final String ACCESS_TOKEN_SECRET_PARAMETER = PROPERTY_PREFIX + "accessTokenSecret";
    protected static final String HMAC_SHA1 = "HMAC-SHA1";

    protected long nonce;
    protected long timestampInSeconds;

    protected String consumerKey;
    protected String consumerSecret;

    protected String oauthToken;
    protected String oauthTokenSecret;

    protected String pinCode;

    protected String accessToken;
    protected String accessTokenSecret;

    private HttpURLConnection currentConnection;
    private Thread statusesConsumer;

    public TwitterService() {
        timestampInSeconds = System.currentTimeMillis() / 1000;
        nonce = timestampInSeconds + (new SecureRandom()).nextInt();
        loadTokens();
    }

    // API Key: JMSeBdrai0nH1xhjfLB46M8Ej
    // API Key secret: 0cFX27AtkgCrYBc9Z7xaZveUAEHLieZ7tsGAreKAMdQxRrs9sy
    // Bearer Token: AAAAAAAAAAAAAAAAAAAAACndWwEAAAAABDWq58vaQ8xDKeXTngTiaFp1CwQ%3DTI0u8Bvvla9DeKhCw5TBouy0S2tFvnVtuPwYS2oi1j2GRJ9MSo

    /**
     * Load the access token and the access token secret from the configuration.
     */
    protected void loadTokens() {
        consumerKey = "JMSeBdrai0nH1xhjfLB46M8Ej";
        consumerSecret = "0cFX27AtkgCrYBc9Z7xaZveUAEHLieZ7tsGAreKAMdQxRrs9sy";
        accessToken = "15134800-g3ovjO7Ih6zCAblFrKlISoGr3RUhWYyHOkBBDEyw8";
        accessTokenSecret = "7eecWtPjlZveIaGbYIOswa9TXq9xrxnPcpnAldx4699rw";
    }

    /**
     * Save the access token and the access token secret to the configuration.
     */
    protected void saveTokens() {
//        GlobalConfiguration.setProperty(ACCESS_TOKEN_PARAMETER, this.accessToken);
//        GlobalConfiguration.setProperty(ACCESS_TOKEN_SECRET_PARAMETER, this.accessTokenSecret);
    }

    /**
     * Check if the credentials to authenticated the user are valid. The check consists in :
     * <p>
     * <ul>
     * <li>verifying if both access token and acces token secret aren't {@code null} nor empty;</li>
     * <li>if both access token and access token secret can be used to verify the credentials on Twitter (using the {@link #buildVerifyCredentialsURL()})</li>
     * </ul>
     *
     * @return {@code true} if the credentials allow to authenticate to Twitter, {@code false} otherwise.
     */
    protected boolean canAuthenticate() {
        boolean canAuthenticate = accessToken != null && accessTokenSecret != null
                && !accessToken.isEmpty() && !accessTokenSecret.isEmpty();

        if (canAuthenticate) {
            HttpURLConnection connection = buildVerifyCredentialsURL();

            try {
                canAuthenticate = connection.getResponseCode() == 200;
            } catch (IOException e) {
                LOGGER.log(WARNING, "Can not determine if the user is authenticated", e);
                canAuthenticate = false;
            } finally {
                connection.disconnect();
            }
        }

        return canAuthenticate;
    }

    /**
     * Start the authentication process to Twitter.
     *
     * @throws TwitterException If the user can be authenticated, or don't allow the application to access Twitter.
     */
    protected void authenticate() throws TwitterException {
        currentConnection = buildRequestTokenURL();

        try {
            int responseCode = currentConnection.getResponseCode();

            if (200 == responseCode) {
                String response = readResponse(currentConnection);

                String[] tokens = response.split("&");

                if (tokens.length >= 3) {
                    LOGGER.info("Response content: " + response);
                    final String oauthTokenKey = "oauth_token=";
                    final String oauthTokenSecret = "oauth_token_secret=";

                    Arrays.stream(tokens)
                            .filter(token -> token.startsWith(oauthTokenKey) || token.startsWith(oauthTokenSecret))
                            .forEach(token -> {
                                if (token.startsWith(oauthTokenKey)) {
                                    oauthToken = token.substring(oauthTokenKey.length());
                                } else if (token.startsWith(oauthTokenSecret)) {
                                    this.oauthTokenSecret = token.substring(oauthTokenSecret.length());
                                }
                            });
                    LOGGER.info("OAuth token: " + oauthToken);
                    LOGGER.info("OAuth token secret: " + this.oauthTokenSecret);

                    try {
                        pinCode = obtainPinCode().get();
                        LOGGER.fine("PIN code: " + pinCode);

                        if (pinCode != null && !pinCode.isEmpty()) {
                            currentConnection = buildAccessTokenURL();
                            responseCode = currentConnection.getResponseCode();

                            if (200 == responseCode) {
                                response = readResponse(currentConnection);
                                tokens = response.split("&");

                                Arrays.stream(tokens)
                                        .filter(token -> token.startsWith(oauthTokenKey) || token.startsWith(oauthTokenSecret))
                                        .forEach(token -> {
                                            if (token.startsWith(oauthTokenKey)) {
                                                accessToken = token.substring(oauthTokenKey.length());
                                            } else if (token.startsWith(oauthTokenSecret)) {
                                                accessTokenSecret = token.substring(oauthTokenSecret.length());
                                            }
                                        });

                                oauthToken = null;
                                this.oauthTokenSecret = null;

                                LOGGER.fine("OAuth access token: " + accessToken);
                                LOGGER.fine("OAuth access token secret: " + accessTokenSecret);

                                saveTokens();
                            }
                        } else {
                            throw new TwitterException(NOT_AUTHENTICATED, "Can not obtain PIN code");
                        }
                    } catch (Exception e) {
                        throw new TwitterException(NOT_AUTHENTICATED, "Unable to authenticate", e);
                    }
                } else {
                    LOGGER.fine("Invalid response: " + response);
                    throw new TwitterException(NOT_AUTHENTICATED);
                }
            } else {
                LOGGER.fine("Response code for request token: " + responseCode + ", Message: " + currentConnection.getResponseMessage());
                throw new TwitterException(NOT_AUTHENTICATED);
            }
        } catch (IOException e) {
            LOGGER.log(WARNING, "Can not authenticate", e);
            throw new TwitterException(NOT_AUTHENTICATED, "Can not authentication", e);
        } finally {
            currentConnection.disconnect();
            currentConnection = null;
        }
    }

    /**
     * Build the connection with correct headers to obtain a request token.
     *
     * @return A properly ready to use connection to obtain a request token.
     */
    protected HttpURLConnection buildRequestTokenURL() {
        try {
            URL url = new URL("https://api.twitter.com/oauth/request_token");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", buildAuthorizationHeaderValue(connection));
            return connection;
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error when building the request token URL", e);
        }

        return null;
    }

    /**
     * Build the connection with correct headers to obtain a PIN code and authenticate the user.
     *
     * @return A properly ready to use connection to obtain a PIN code and authenticate the user.
     */
    protected HttpURLConnection buildAuthenticateURL() {
        try {
            URL url = new URL("https://api.twitter.com/oauth/authenticate?oauth_token=" + oauthToken);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            return connection;
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error when building the access token URL", e);
        }

        return null;
    }

    /**
     * Build the connection with correct headers and body to obtain an access token.
     *
     * @return A properly ready to use connection to obtain an access token.
     */
    protected HttpURLConnection buildAccessTokenURL() {
        try {
            URL url = new URL("https://api.twitter.com/oauth/access_token");
            String body = "oauth_verifier=" + encode(pinCode, UTF_8.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", buildAuthorizationHeaderValue(connection));
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
                outputStream.flush();
            }

            return connection;
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error when building the access token URL", e);
        }

        return null;
    }

    /**
     * Build the connection with correct headers to verify an access token and access token secret.
     *
     * @return A properly ready to use connection to verify an access token and access token secret.
     */
    protected HttpURLConnection buildVerifyCredentialsURL() {
        try {
            URL url = new URL("https://api.twitter.com/1.1/account/verify_credentials.json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", buildAuthorizationHeaderValue(connection));

            return connection;
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error when building the access token URL", e);
        }

        return null;
    }

    /**
     * Build the connection with correct headers to obtain and filter the Twitter statuses for a track term.
     *
     * @return A properly ready to use connection to obtain and filter the Twitter statuses for a track term.
     */
    protected HttpURLConnection buildStatusesURL() {
        try {
            URL url = new URL("https://stream.twitter.com/1.1/statuses/filter.json?track=javafx");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", buildAuthorizationHeaderValue(connection));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoInput(true);

            return connection;
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error when building the access token URL", e);
        }

        return null;
    }

    /**
     * Start a WebView for allowing the user to authenticate to Twitter and obtain a PIN code.
     * If the user properly authenticate and authorize the application, the PIN code will be returned. Otherwise the
     * return {@link CompletableFuture} will complete exceptionally.
     *
     * @return a {@link CompletableFuture} with the PIN code.
     */
    protected CompletableFuture<String> obtainPinCode() {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            Platform.runLater(() -> {

                HttpURLConnection authenticateURL = buildAuthenticateURL();
                WebView twitterBrowser = new WebView();
                Scene scene = new Scene(twitterBrowser);
                Stage stage = new Stage();

                twitterBrowser.getEngine().load(authenticateURL.getURL().toExternalForm());

                twitterBrowser.getEngine().getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        HTMLDocument document = (HTMLDocument) twitterBrowser.getEngine().getDocument();

                        if (document.getDocumentURI().equals("https://api.twitter.com/oauth/authorize")) {

                            HTMLElement body = document.getBody();
                            String bodyClasses = body.getClassName();

                            if (bodyClasses.contains("oauth") && bodyClasses.contains("cancelled")) {
                                future.completeExceptionally(new TwitterException(UNAUTHORIZED_APPLICATION));
                                stage.close();
                            } else {
                                Element oauth_pin = document.getElementById("oauth_pin");

                                if (oauth_pin != null) {
                                    NodeList elements = oauth_pin.getElementsByTagName("kbd");

                                    if (elements != null && elements.getLength() > 0) {
                                        future.complete(elements.item(0).getTextContent());
                                        stage.close();
                                    } else {
                                        future.completeExceptionally(new TwitterException(UNAUTHORIZED_APPLICATION));
                                        stage.close();
                                    }
                                }
                            }
                        }
                    } else if (newState == Worker.State.FAILED) {
                        stage.close();
                        future.completeExceptionally(new TwitterException(NOT_AUTHENTICATED));
                    }
                });

                stage.setScene(scene);
                stage.show();
            });

            try {
                return future.get();
            } catch (Exception e1) {
                LOGGER.log(Level.SEVERE, "Error when getting the PIN code", e1);
                return null;
            }
        });

        return future;
    }

    /**
     * Reads the response from a connection.
     *
     * @param connection The connection to read the response.
     * @return The body of the connection.
     * @throws IOException
     */
    protected String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;

            while ((line = input.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    /**
     * This method will check if {@link #accessToken} is not {@code null} and not empty. If so, it will check if
     * {@link #oauthToken} is not {@code null} and not empty. If so, return null;
     *
     * @return The oauth_token or {@code null}?
     */
    protected String getOauthToken() {
        String token;

        if (accessToken != null && !accessToken.isEmpty()) {
            token = accessToken;
        } else if (oauthToken != null && !oauthToken.isEmpty()) {
            token = oauthToken;
        } else {
            token = null;
        }

        return token;
    }

    /**
     * This method will build the correct <b>Authorization</b> header for the given connection.
     *
     * @param connection The connection for which create the <b>Authorization</b> header.
     * @return The value of the <b>Authorization</b> header.
     * @throws UnsupportedEncodingException
     */
    protected String buildAuthorizationHeaderValue(HttpURLConnection connection) throws UnsupportedEncodingException {
        StringBuilder value = new StringBuilder("OAuth ")
                .append("oauth_consumer_key=\"").append(consumerKey).append("\", ")
                .append("oauth_nonce=\"").append(nonce).append("\", ")
                .append("oauth_signature_method=\"").append(HMAC_SHA1).append("\", ")
                .append("oauth_signature=\"").append(encode(buildSignature(connection), UTF_8.toString())).append("\", ")
                .append("oauth_timestamp=\"").append(timestampInSeconds).append("\", ");


        String token = getOauthToken();

        if (token != null) {
            value.append("oauth_token=\"").append(encode(token, UTF_8.toString())).append("\", ");
        }

        value.append("oauth_version=\"1.0\"");

        LOGGER.fine("Authorization header: " + value);

        return value.toString();
    }

    /**
     * Build the OAuth <b>oauth_signature</b> parameter to be included in the <b>Authorization</b> header.
     *
     * @param connection The connection for which build the <b>oauth_signature</b> parameter.
     * @return The value of the <b>oauth_signature</b>.
     */
    protected String buildSignature(HttpURLConnection connection) {
        try {
            URL url = connection.getURL();
            String rawURL = url.getProtocol() + "://" + url.getAuthority() + url.getPath();
            String baseString = new StringBuilder(connection.getRequestMethod()).append("&")
                    .append(encode(rawURL, UTF_8.toString())).append("&")
                    .append(encode(getBaseParamQueryString(connection), UTF_8.toString()))
                    .toString();

            LOGGER.fine("Signature base string: " + baseString);
            Mac mac = Mac.getInstance("HmacSHA1");

            String keyString;
            if (accessTokenSecret != null) {
                keyString = consumerSecret + '&' + encode(accessTokenSecret, UTF_8.toString());
            } else {
                keyString = consumerSecret + '&';
            }

            SecretKeySpec secretKey = new SecretKeySpec(keyString.getBytes(), HMAC_SHA1);
            mac.init(secretKey);

            byte[] byteHMAC = mac.doFinal(baseString.getBytes());
            String signature = Base64.getEncoder().encodeToString(byteHMAC).trim();

            LOGGER.fine("Signature: " + signature);
            return signature;
        } catch (InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to authenticate against Twitter", e);
        }
    }

    /**
     * Build the parameter string to be used in the process of creating the signature. This method will take care of
     * using the correct oauth token as well as include the query string of the given connection.
     *
     * @param connection The connection for which create the parameter string.
     * @return The base parameter query string.
     * @see #buildSignature(HttpURLConnection)
     */
    protected String getBaseParamQueryString(HttpURLConnection connection) {
        StringBuilder queryString = new StringBuilder("oauth_consumer_key=").append(consumerKey).append("&")
                .append("oauth_nonce=").append(nonce).append("&")
                .append("oauth_signature_method=").append(HMAC_SHA1).append("&")
                .append("oauth_timestamp=").append(timestampInSeconds).append("&");

        try {
            String token = getOauthToken();

            if (token != null) {
                queryString.append("oauth_token=").append(encode(token, UTF_8.toString())).append("&");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(WARNING, "Unsupported encoding", e);
        }

        queryString.append("oauth_version=1.0");

        String urlQueryString = connection.getURL().getQuery();
        if (urlQueryString != null && urlQueryString.contains("track")) {
            try {
                queryString.append("&track=").append(encode("javafx", UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(WARNING, "Can not add the track element", e);
            }
        }
        LOGGER.fine("Base parameter string: " + queryString);

        return queryString.toString();
    }

    public void start() {
        String twitterHashtag = "javafx";

        if (twitterHashtag != null && !twitterHashtag.trim().isEmpty()) {

            boolean authenticated = canAuthenticate();

            if (!authenticated) {
                try {
                    authenticate();
                    authenticated = true;
                } catch (TwitterException e) {
                    LOGGER.log(WARNING, "The user is not authenticated", e);
                }
            }

            if (authenticated) {
                Runnable work = () -> {
                    currentConnection = buildStatusesURL();

                    try {
                        int responseCode = currentConnection.getResponseCode();

                        if (200 == responseCode) {
                            StringBuilder tweet = new StringBuilder();
                            InputStream inputStream = currentConnection.getInputStream();
                            byte[] buffer = new byte[512];
                            int bytesRead;

                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                String str = new String(buffer, 0, bytesRead, UTF_8);
                                tweet.append(str);

                                if (str.endsWith("\n") && !tweet.toString().trim().isEmpty()) {
                                    broadcastTweet(tweet.toString().trim());
                                    tweet.setLength(0);
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error when reading tweets", e);
                    } finally {
                        LOGGER.fine("Disconnected from the streaming API");
                    }
                };

                statusesConsumer = new Thread(work);
                statusesConsumer.start();
            }
        }
    }

    private void broadcastTweet(String tweet) {
        System.out.println("tweet: " + tweet);
    }

    public void stop() {
        try {
            if (statusesConsumer != null && statusesConsumer.isAlive()) {
                statusesConsumer.interrupt();
            }

            if (currentConnection != null) {
                currentConnection.disconnect();
                currentConnection = null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can not stop the TwitterService properly", e);
        }
    }

    public static void main(String[] args) {
        TwitterService service = new TwitterService();
        service.start();
    }
}