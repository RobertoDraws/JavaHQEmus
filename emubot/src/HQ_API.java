import com.google.gson.*;
import org.java_websocket.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class HQ_API {
    private String bearer;
    private String countrycode = "US";
    private HQQuestionData lastQuestion;

    private WebSocketClient ws = null;

    public HQ_API(String bearer_token){
        bearer_token = bearer;
        waitForNextGame();
    }

    public static void waitForNextGame(){

    }

    public void openWebSocket(String url){
        try {
            ws = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Established connection to: " + url);
                }

                @Override
                public void onMessage(String s) {
                    JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                    String messageType = jsonObject.get("type").getAsString();

                    if(messageType.equals("question")){
                        HQQuestionData qdata = new Gson().fromJson(s, HQQuestionData.class);
                        onNextQuestion(qdata);
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("Connection closed.");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onNextQuestion(HQQuestionData qdata){
        lastQuestion = qdata;
    }

    public void sendAnswer(HQAnswer answer){
        if(ws != null && ws.isOpen()) {
            HQAPIData apiData = getAPIData();
            String data = String.format("{type: 'answer', broadcastId: %d, answerId: %d, questionId: %d}",
                    apiData.broadcast.broadcastId, answer.answerId, lastQuestion.questionId);
            System.out.println("Sending data: " + data);
            ws.send(data);
        }
    }

    public void sendPing(){
        if(ws != null && ws.isOpen()){
            System.out.println("Sending ping.");
            ws.sendPing();
        }
    }

    public HQAPIData getAPIData(){
        String apiResp = HTTPGet(EndpointSchedule);
        HQAPIData data = new Gson().fromJson(apiResp, HQAPIData.class);
        return data;
    }

    public String HTTPGet(String targetUrl) {
        HttpURLConnection conn = null;

        try{
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("User-Agent", "okhttp/3.8.0");
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
            //System.out.println("You don fucked up: " + e.getMessage());
        }
        return "err";
    }

    public String getSTK() {
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
            //System.out.println("You don fucked up: " + e.getMessage());
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
