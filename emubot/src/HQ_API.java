package emubot.src;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static emubot.src.Main.*;

//https://gimmeproxy.com/api/getProxy?api_key=95ad3d1e-703c-4cbe-964a-4a5f81d14565&supportsHttps=true

public class HQ_API {
    public static int totalGamesFinished = 0;
    public static int totalWinners = 0;

    public static int totalBotsInTheGame(){
        int accountsIn = 0;
        for(HQ_API client : Main.HQAccounts){
            if(client.inTheGame)
                accountsIn++;
        }
        return accountsIn;
    }

    public static ArrayList<HQ_API> getAllBotsInTheGame(){
        ArrayList<HQ_API> botsIn = new ArrayList<>();
        for(HQ_API client : Main.HQAccounts){
            if(client.inTheGame)
                botsIn.add(client);
        }
        return botsIn;
    }

    public boolean display = false;
    public boolean inTheGame = false;

    private String username;
    public String bearer;
    private String countrycode = "US";
    public static HQQuestionData lastQuestion;
    public static BroadcastData currentBroadcast;

    private WebSocketClient ws = null;

    public HQ_API(String username, String bearer){
        this.bearer = bearer;
        this.username = username;
        log("Loaded account.");
        botsLoaded++;

        if(headless && botsLoaded == accountsLimit || botsLoaded == accountdata.size()){
            Main.loadingComplete();
        }
    }

    public HQ_API(String bearer){
        this.bearer = bearer;
        new Thread(() -> {
            username = getUsername();
            Collections.replaceAll(accountdata, bearer, String.format("{\"username\": \"%s\", \"bearer\": \"%s\"}", username, bearer));
            log("Loaded account.");
            botsLoaded++;

            if(botsLoaded == accountsLimit || botsLoaded == accountdata.size()){
                new Thread(() -> {
                    try{Thread.sleep(1000);}catch(Exception exce){}
                    System.out.println("Loading complete.");
                }).start();
            }
        }).start();
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
        if(username != null)
            return username;
        JsonObject jsonObject = new JsonParser().parse(GetEndpointMe()).getAsJsonObject();
        return jsonObject.get("username").getAsString();
    }

    public double getBalance(){
        String payoutsget = HttpGet(EndpointMe);
        JsonObject jsonObject = new JsonParser().parse(payoutsget).getAsJsonObject();
        if(jsonObject.get("error") == null)
            return Double.parseDouble(jsonObject.getAsJsonObject("leaderboard").get("unclaimed").getAsString().replaceAll("[^\\d.,]", ""));
        return 0.0;
    }

    public char getCountryChar(){
        JsonObject jsonObject = new JsonParser().parse(HttpGet(EndpointPayouts)).getAsJsonObject();
        return jsonObject.getAsJsonObject("balance").get("prizeTotal").getAsString().charAt(0);
    }

    public void openWebSocket(String url){
        totalWinners = 0;
        try {
            Map<String, String> _headers = new HashMap<>();
            _headers.put("Authorization","Bearer " + bearer);

            ws = new WebSocketClient(new URI(url), _headers) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    if(!headless)
                        System.out.println("Established connection to: " + url);

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

                    if(display && !messageType.equals("interaction") && !headless)
                        System.out.println(s);

                    if(messageType.equals("question")){
                        if(!headless)
                            Main.gui.resetButtons();
                        HQQuestionData qdata = new Gson().fromJson(s, HQQuestionData.class);

                        if(display)
                            onNextQuestion(qdata);

                        if(display && lastQuestion == null)
                            refreshMainScreen();
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
                                    log("Sending Extra Use Life Request");
                                    ws.send(json);
                                }).start();
                            } else if(inTheGame){
                                inTheGame = false;
                            }
                        }

                        if(display) {
                            System.out.println("\nAdvancing Users: " + advancing + ", Eliminated Users: " + eliminated);
                            new Thread(() -> {
                                Main.sleep(1000);
                                System.out.println("You have " + totalBotsInTheGame() + " / " + botsLoaded + " bots still in the game.");
                            }).start();
                        }
                    } else if(messageType.equals("gameSummary")){
                        boolean youWon = jsonObject.get("youWon").getAsBoolean();
                        if(youWon)
                            totalWinners++;
                        totalGamesFinished++;

                        //System.out.println("Total Games Finished: " + totalGamesFinished + ", Total Winners: " + totalWinners);
                        if(display){
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    if(headless){
                                        System.out.println("You won on " + totalWinners + " accounts.");
                                        finishedCmdExec = true;
                                    } else {
                                        Main.gui.sendDialogBox("You won on " + totalWinners + " accounts.");
                                    }
                                } catch(Exception e){e.printStackTrace();}
                            }).start();
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    inTheGame = false;
                    if(!headless)
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

    private void refreshMainScreen(){
        clearScreen();
        System.out.println("Connected To Game!\nWaiting for game to start.\n");
    }

    public void log(String text){
        System.out.println(String.format("[%s] %s", username, text));
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
        if(headless){
            Main.clearScreen();
            System.out.println("You have " + totalBotsInTheGame() + " / " + botsLoaded + " bots still in the game.");
            System.out.println(qdata.question+"\n");
            System.out.println("[1] " + qdata.answers.get(0).text);
            System.out.println("[2] " + qdata.answers.get(1).text);
            System.out.println("[3] " + qdata.answers.get(2).text);

            Scanner scanner = new Scanner(System.in);
            System.out.print("[1, 2, 3, s]: ");
            String command = scanner.nextLine();

            if(command.startsWith("1")) {
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                    }).start();
                }
            } else if(command.startsWith("2")){
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(1));
                    }).start();
                }
            } else if(command.startsWith("3")){
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(2));
                    }).start();
                }
            } else if(command.startsWith("s")){
                int splitAmt = Main.HQAccounts.size() / 3;

                for (int i = 0; i < splitAmt; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                    }).start();
                }

                for (int i = splitAmt; i < splitAmt * 2; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(1));
                    }).start();
                }

                for (int i = splitAmt * 2; i < splitAmt * 3; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(2));
                    }).start();
                }
            }
        } else {
            Main.gui.setQuestionText(qdata.question);
            Main.gui.setAnswer1Text(qdata.answers.get(0).text);
            Main.gui.setAnswer2Text(qdata.answers.get(1).text);
            Main.gui.setAnswer3Text(qdata.answers.get(2).text);
        }
    }

    public void sendAnswer(HQAnswer answer){
        if(ws != null && ws.isOpen() && inTheGame) {
            String data = String.format("{\"type\": \"answer\", \"broadcastId\": %d, \"answerId\": %d, \"questionId\": %d}",
                /*currentBroadcast.broadcastId*/0, answer.answerId, lastQuestion.questionId);

                log("Answer submitted: " + answer.text);
            ws.send(data);

            if(!headless)
                Main.gui.answerSubmitted();
        }
    }

    public void extraLife(){
        String req = HttpPost(EndpointMakeItRain, "");
        System.out.println(req);
    }
    public boolean cashout(String email){
        String json = "{\"email\":\""+email+"\", \"country\": \"US\"}";
        //String json = "{\"user\": \"5365037\",\"country\": \"US\"}";
        String req = HttpPost(EndpointPayouts, json);
        System.out.println(req);

        return true;
    }

    public HQAPIData getAPIData(){
        String apiResp = HttpGet(EndpointSchedule);
        HQAPIData data = new Gson().fromJson(apiResp, HQAPIData.class);
        return data;
    }

    public static void joinGameHeadless() {
        if (debug) {
            String wsurl = "ws://69.143.151.72:80";
            for (HQ_API client : Main.HQAccounts) {
                new Thread(() -> {
                    client.openWebSocket(wsurl);
                }).start();
            }
        } else {
            HQAPIData apiData = Main.HQAccounts.get(0).getAPIData();
            if (apiData.active) {
                String wsurl = Main.HQAccounts.get(0).getAPIData().broadcast.socketUrl.replace("https", "wss");
                for (HQ_API client : Main.HQAccounts) {
                    new Thread(() -> {
                        client.openWebSocket(wsurl);
                    }).start();
                }
                Main.HQAccounts.get(0).display = true;
            } else {
                System.out.println("HQ WebSocket is not live!");
                Main.finishedCmdExec = true;
            }
        }
    }

    private String GetEndpointMe() {
        HttpURLConnection conn = null;

        try{
            URL url = new URL(EndpointMe);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("User-Agent", "okhttp/3.8.0");
            conn.setRequestProperty("Authorization", "Bearer " + bearer);
            conn.setRequestProperty("x-hq-client", "Android/1.8.1");
            conn.setRequestProperty("x-hq-country", countrycode);
            conn.setRequestProperty("x-hq-lang", "en");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resp.append(inputLine);
            }
            in.close();

            String respStr = resp.toString();
            conn.disconnect();

            return respStr;
        } catch(Exception e){
            System.out.println("You don fucked up: " + e.getMessage());
        }
        return "{\"error\": true}";
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

            String respStr = resp.toString();
            conn.disconnect();

            return respStr;
        } catch(Exception e){
            System.out.println("You don fucked up: " + e.getMessage());
        }
        return "{\"error\": true}";
    }

    private String HttpPost(String targetUrl, String request){
        try {

            HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

            HttpPost postRequest = new HttpPost(
                    targetUrl);

            StringEntity input = new StringEntity(request);
            input.setContentType("application/json");
            postRequest.setHeader("User-Agent", "okhttp/3.8.0");
            postRequest.setHeader("Authorization", "Bearer " + bearer);
            postRequest.setHeader("accept", "*/*");
            postRequest.setHeader("x-hq-client", "Android/1.8.1");
            postRequest.setHeader("x-hq-country", countrycode);
            postRequest.setHeader("x-hq-lang", "en");
            postRequest.setHeader("x-hq-stk", getSTK());
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }
            httpClient.getConnectionManager().shutdown();
            return output;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "{\"error\": true}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": true}";
        }
    }

    public String getSTK() {
        if(Main.stk != null)
            return Main.stk;

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

            Main.stk = jsonObject.get("stk").getAsString();

            conn.disconnect();
            return Main.stk;
        } catch (Exception e) {
            System.out.println("get stk error: " + e.getMessage());
        }
        return "{\"error\": true}";
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
