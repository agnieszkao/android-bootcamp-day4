package pl.droidsonroids.bootcamp.yo.model;

import android.support.annotation.NonNull;

import java.text.Collator;

public class User implements Comparable<User> {
    int id;
    String name;
    boolean isColored;

    public boolean isColored() {
        return isColored;
    }

    public void setIsColored(boolean isColored) {
        this.isColored = isColored;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull User user) {
        return Collator.getInstance().compare(name.toLowerCase(), user.getName().toLowerCase());
    }
}
