import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;

public class HQ_API {
    private String bearer;
    private String countrycode = "US";

    public HQ_API(String bearer_token){
        bearer_token = bearer;
        HQAPIData test = getAPIData();
        System.out.println(test.nextShowPrize);
    }

    public HQAPIData getAPIData(){
        String apiResp = HTTPGet("https://api-quiz.hype.space/shows/now");
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
            System.out.println("You don fucked up: " + e.getMessage());
        }
        return "err";
    }

    public String getSTK() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://api-quiz.hype.space/users/me");
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
            System.out.println("You don fucked up: " + e.getMessage());
        }
        return "err";
    }


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
    public String socketUrl;
}
