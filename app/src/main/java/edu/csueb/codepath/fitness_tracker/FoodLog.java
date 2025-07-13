package edu.csueb.codepath.fitness_tracker;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FoodLog")
public class FoodLog extends ParseObject {
    public String getFoodName() { return getString("foodName"); }
    public void setFoodName(String name) { put("foodName", name); }

    public int getCalories() { return getInt("calories"); }
    public void setCalories(int cal) { put("calories", cal); }

    public String getTimestamp() { return getString("timestamp"); }
    public void setTimestamp(String time) { put("timestamp", time); }

    // ✳️ Liên kết với ParseUser
    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }
}



