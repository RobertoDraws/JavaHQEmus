package emubot.src;

import com.google.gson.*;
import org.java_websocket.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//https://gimmeproxy.com/api/getProxy?api_key=95ad3d1e-703c-4cbe-964a-4a5f81d14565&supportsHttps=true

public class HQ_API {
    public static int totalGamesFinished = 0;
    public static int totalWinners = 0;
    public static int totalBotsInTheGame = 0;

    public boolean display = false;
    public boolean inTheGame = false;

    private String username;
    public String bearer;
    private String countrycode = "US";
    public static HQQuestionData lastQuestion;
    public static BroadcastData currentBroadcast;

    private WebSocketClient ws = null;

    public HQ_API(String bearer_token){
        bearer = bearer_token;
        System.out.println("Loaded account.");
    }

    //TODO: Unfinished function to autostart the bot when the game goes live
    public void waitForNextGame(){
        try {
            HQAPIData data = getAPIData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = sdf.parse(data.nextShowTime);

            new Thread(() -> {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            HQAPIData data;
                            while (!(data = getAPIData()).active) {
                                Thread.sleep(30000);
                            }
                            openWebSocket(data.broadcast.socketUrl);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }, date);
            }).start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getUsername(){
        JsonObject jsonObject = new JsonParser().parse(HttpGet(EndpointMe)).getAsJsonObject();
        return jsonObject.get("username").getAsString();
    }

    public double getBalance(){
        String payoutsget = HttpGet(EndpointMe);
        JsonObject jsonObject = new JsonParser().parse(payoutsget).getAsJsonObject();
        return Double.parseDouble(jsonObject.getAsJsonObject("leaderboard").get("unclaimed").getAsString().replaceAll("[^\\d.,]", ""));
    }

    public char getCountryChar(){
        JsonObject jsonObject = new JsonParser().parse(HttpGet(EndpointPayouts)).getAsJsonObject();
        return jsonObject.getAsJsonObject("balance").get("prizeTotal").getAsString().charAt(0);
    }

    public void openWebSocket(String url){
        totalWinners = 0;
        totalBotsInTheGame = 0;
        try {
            Map<String, String> _headers = new HashMap<>();
            _headers.put("Authorization","Bearer " + bearer);

            ws = new WebSocketClient(new URI(url), _headers) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Established connection to: " + url);
                    incTotalBotsInTheGame();

                    totalGamesFinished = 0;
                    inTheGame = true;

                    if(display){
                        currentBroadcast = getAPIData().broadcast;
                    }
                    startHeartbeat();
                }

                @Override
                public void onMessage(String s) {
                    JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                    String messageType = jsonObject.get("type").getAsString();

                    if(display && !messageType.equals("interaction"))
                        System.out.println(s);

                    if(messageType.equals("question")){
                        Main.gui.resetButtons();
                        HQQuestionData qdata = new Gson().fromJson(s, HQQuestionData.class);

                        if(display)
                            onNextQuestion(qdata);
                    } else if(messageType.equals("questionSummary")){
                        Main.gui.resetAnswersSubmitted();
                        Main.gui.resetButtons();
                        boolean correct = jsonObject.get("youGotItRight").getAsBoolean();
                        int advancing = jsonObject.get("advancingPlayersCount").getAsInt();
                        int eliminated = jsonObject.get("eliminatedPlayersCount").getAsInt();
                        int extraLives = jsonObject.get("extraLivesRemaining").getAsInt();

                        if(!correct) {
                            if(extraLives > 0) {
                                new Thread(() -> {
                                    String json = String.format("{\"type\": \"useExtraLife\", \"broadcastId\": %d, \"questionId\": %d}", currentBroadcast.broadcastId, lastQuestion.questionId);
                                    System.out.println("Sending Extra Life Request: " + json);
                                    ws.send(json);
                                }).start();
                            } else if(inTheGame){
                                decTotalBotsInTheGame();
                                inTheGame = false;
                            }
                        }

                        if(display)
                            System.out.println("Advancing Users: " + advancing + ", Eliminated Users: " + eliminated);
                    } else if(messageType.equals("gameSummary")){
                        boolean youWon = jsonObject.get("youWon").getAsBoolean();
                        if(youWon)
                            totalWinners++;
                        totalGamesFinished++;

                        System.out.println("Total Games Finished: " + totalGamesFinished + ", Total Winners: " + totalWinners);
                        if(display){
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Main.gui.sendDialogBox("You won on " + totalWinners + " accounts.");
                                } catch(Exception e){e.printStackTrace();}
                            });
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    inTheGame = false;
                    System.out.println("Connection closed: " + s);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            ws.connect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void incTotalBotsInTheGame(){
        totalBotsInTheGame++;
        Main.gui.setTotalConnAccounts(String.format("%d / %d", HQ_API.totalBotsInTheGame, Main.HQAccounts.size()));
    }

    private void decTotalBotsInTheGame(){
        totalBotsInTheGame--;
        Main.gui.setTotalConnAccounts(String.format("%d / %d", HQ_API.totalBotsInTheGame, Main.HQAccounts.size()));
    }

    private void startHeartbeat(){
        new Thread(() -> {
            try {
                while (ws != null && ws.isOpen()) {
                    ws.sendPing();
                    Thread.sleep(10000);
                }
            } catch(Exception e){
                System.out.println("Failed to send heartbeat.");
            }
        }).start();
    }

    public void closeWebSocket(){
        if(ws != null && ws.isOpen())
            ws.close();
    }

    public void onNextQuestion(HQQuestionData qdata){
        lastQuestion = qdata;
        Main.gui.setQuestionText(qdata.question);
        Main.gui.setAnswer1Text(qdata.answers.get(0).text);
        Main.gui.setAnswer2Text(qdata.answers.get(1).text);
        Main.gui.setAnswer3Text(qdata.answers.get(2).text);
    }

    public void sendAnswer(HQAnswer answer){
        if(ws != null && ws.isOpen() && inTheGame) {
            String data = String.format("{\"type\": \"answer\", \"broadcastId\": %d, \"answerId\": %d, \"questionId\": %d}",
                currentBroadcast.broadcastId, answer.answerId, lastQuestion.questionId);

            if(display)
                System.out.println("Sending data: " + data);
            ws.send(data);
            Main.gui.answerSubmitted();
        }
    }

    public void extraLife(){
        String req = HttpPost(EndpointMakeItRain, "");
        System.out.println(req);
    }

    public boolean cashout(String email){
        String json = String.format("{\"email\":\""+email+"\"}");
        String req = HttpPost(EndpointPayouts, json);
        System.out.println(req);

        return true;
    }

    public HQAPIData getAPIData(){
        String apiResp = HttpGet(EndpointSchedule);
        HQAPIData data = new Gson().fromJson(apiResp, HQAPIData.class);
        return data;
    }

    private String HttpGet(String targetUrl) {
        HttpURLConnection conn = null;

        try{
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("User-Agent", "okhttp/3.8.0");
            conn.setRequestProperty("Authorization", "Bearer " + bearer);
            conn.setRequestProperty("x-hq-client", "Android/1.8.1");
            conn.setRequestProperty("x-hq-country", countrycode);
            conn.setRequestProperty("x-hq-lang", "en");
            conn.setRequestProperty("x-hq-stk", getSTK());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resp.append(inputLine);
            }
            in.close();

            return resp.toString();
        } catch(Exception e){
            System.out.println("You don fucked up: " + e.getMessage());
        }
        return "err";
    }

    private String HttpPost(String targetUrl, String request){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "okhttp/3.8.0");
            conn.setRequestProperty("Authorization", "Bearer " + bearer);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("x-hq-client", "Android/1.8.1");
            conn.setRequestProperty("x-hq-country", countrycode);
            conn.setRequestProperty("x-hq-lang", "en");
            conn.setRequestProperty("x-hq-stk", getSTK());

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(request);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resp.append(inputLine);
            }
            in.close();

            return resp.toString();
        } catch(Exception e){
            try {
                StringBuffer resp = new StringBuffer();
                BufferedReader error = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorLine;
                while ((errorLine = error.readLine()) != null) {
                    resp.append(errorLine);
                }
                return resp.toString();
            } catch(Exception ex){
                return "oof";
            }
        }
    }

    private String getSTK() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(EndpointMe);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("User-Agent", "okhttp/3.8.0");
            conn.setRequestProperty("x-hq-client", "Android/1.8.1");
            conn.setRequestProperty("x-hq-country", countrycode);
            conn.setRequestProperty("x-hq-lang", "en");
            conn.setRequestProperty("authorization", "Bearer " + bearer);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resp.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = new JsonParser().parse(resp.toString()).getAsJsonObject();
            return jsonObject.get("stk").getAsString();
        } catch (Exception e) {
            System.out.println("get stk error: " + e.getMessage());
        }
        return "err";
    }


    public static String EndpointBase = "https://api-quiz.hype.space/";
    public static String EndpointUsers = EndpointBase + "users/";
    public static String EndpointMe = EndpointUsers + "me/";
    public static String EndpointPayouts = EndpointMe + "payouts/";
    public static String EndpointShows = EndpointBase + "shows/";
    public static String EndpointSchedule = EndpointShows + "now?type=hq";
    public static String EndpointAvatarURL = EndpointMe + "avatarUrl/";
    public static String EndpointFriends    = EndpointBase + "friends/";
    public static String EndpointVerifications = EndpointBase + "verifications/";
    public static String EndpointEasterEggs = EndpointBase + "easter-eggs/";
    public static String EndpointAWS = EndpointBase + "credentials/s3";
    public static String EndpointMakeItRain = EndpointEasterEggs + "makeItRain/";
    public static String EndpointTokens     = EndpointBase + "tokens/";
}

class HQAPIData
{
    public Boolean active;
    public Boolean atCapacity;
    public String showId;
    public String showType;
    public String startTime;
    public String nextShowTime;
    public String nextShowPrize;
    public List<ScheduledGame> upcoming;
    public String prize;
    public BroadcastData broadcast;
    public Boolean broadcastFull;
}

class ScheduledGame
{
    public String time;
    public String prize;
}

class BroadcastData
{
    public long broadcastId;
    public String socketUrl;
}

class HQQuestionData
{
    public String type;
    public String ts;
    public long totalTimeMs;
    public long timeLeftMs;
    public long questionId;
    public String question;
    public String category;
    public List<HQAnswer> answers;
    public int questionNumber;
    public int questionCount;
    public String askTime;
    public String sent;
}

class HQAnswer
{
    public long answerId;
    public String text;
}
